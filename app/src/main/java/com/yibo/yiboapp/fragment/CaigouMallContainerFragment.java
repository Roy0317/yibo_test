package com.yibo.yiboapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SpanUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.TouzhuActivity;
import com.yibo.yiboapp.activity.TouzhuSimpleActivity;
import com.yibo.yiboapp.adapter.NumbersAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.GoucaiResult;
import com.yibo.yiboapp.entify.GoucaiResultWraper;
import com.yibo.yiboapp.entify.LocPlaysWraper;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * @author: soxin
 * @version: ${VERSION}
 * @project: trunk
 * @package: com.yibo.yiboapp.fragment
 * @description: ${DESP}
 * @date: 2019/1/10
 * @time: 下午3:20
 */
public class CaigouMallContainerFragment extends Fragment implements SessionResponse.Listener
        <CrazyResult<Object>>, Handler.Callback {


    private static final String TAG = "CaigouMallContainerFrag";

    private View view;
    private List<GoucaiResult> cpResultList;
    private List<GoucaiResult> cpLowList;
    private List<GoucaiResult> cpHighList;

    public static final int DATA_REQUEST = 0x01;
    public static final int PLAY_RULES_REQUEST = 0x02;
    public static final int DATA_REQUEST_ITEM = 0x03;

    private XListView listView;
    private EmptyListView empty;
    private ListViewAdapter adapter;

    public static final String[] lowRatelots = new String[]{"LHC", "FC3D", "PL3"};
    public static final int ALL_LOT_PAGE = 0;
    public static final int HIGH_LOT_PAGE = 1;
    public static final int LOW_LOT_PAGE = 2;
    int pageindex = 0;
    int pagecount = 0;
    public int currentPage = 0;

    private Handler handler;
    public MyRunnable timeThread;
    private boolean isThreadStart = false;


//    public static CaigouMallContainerFragment newInstance(int currentPage) {
//        CaigouMallContainerFragment fragment = new CaigouMallContainerFragment();
//        Bundle args = new Bundle();
//        args.putInt("currentPage", currentPage);
//        fragment.setArguments(args);
//        return fragment;
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.caigou_mall_container_fragment, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        currentPage = getArguments().getInt("currentPage");
        cpResultList = new ArrayList<>();
        cpLowList = new ArrayList<>();
        cpHighList = new ArrayList<>();
//        updateListView(currentPage);
        pageindex = 0;
        actionAcquireData(pageindex, false);
    }


    private void initView(View view) {
        listView = (XListView) view.findViewById(R.id.xlistview);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setDivider(getResources().getDrawable(R.color.driver_line_color));
        listView.setXListViewListener(new ListviewListener());
        listView.setDividerHeight(3);
        listView.setVisibility(View.VISIBLE);
        //不要过界的效果
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        empty = (EmptyListView) view.findViewById(R.id.empty);
        empty.setListener(emptyListviewListener);
//        empty.setVisibility(View.GONE);
        handler = new Handler(this);

        adapter = new ListViewAdapter();
        adapter.setLayoutId(R.layout.caipiao_list_item1);
        adapter.setmContext(getActivity());
        adapter.setmDatas(cpResultList);
        listView.setAdapter(adapter);
    }


    public void actionAcquireData(int pageindex, boolean showDialog) {

        if (!Utils.isNetworkAvailable(getActivity())) {
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            return;
        }

        String lotteryJson = YiboPreference.instance(getActivity()).getLotterys();
        StringBuilder sb = new StringBuilder();
        if (!Utils.isEmptyString(lotteryJson)) {
            Type listType = new TypeToken<ArrayList<LotteryData>>() {
            }.getType();
            List<LotteryData> list = new Gson().fromJson(lotteryJson, listType);
            if (pagecount == 0) {
                pagecount = (int) Math.ceil(list.size() / 5);
            }
            if (list != null && !list.isEmpty()) {
                for (int i = pageindex * 5; i < ((pageindex * 5) + 5 < list.size() ? (pageindex * 5) + 5 : list.size()); i++) {
                    sb.append(list.get(i).getCode()).append(",");
                }
            }
        }
        if (sb.toString().length() > 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ALL_LOTTERYS_COUNTDOWN_URL);
        configUrl.append("?lotCodes=").append(sb.toString());
        CrazyRequest<CrazyResult<GoucaiResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(DATA_REQUEST)
                .headers(Urls.getHeader(getActivity()))
                .shouldCache(true)
                .refreshAfterCacheHit(true)
                .listener(this)
                .placeholderText(getActivity().getString(R.string.loading))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<GoucaiResultWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(getActivity(), request);

    }


    private void actionAcquireData(boolean showDialog, int postion, boolean flag) {

        if (!Utils.isNetworkAvailable(getActivity())) {
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            return;
        }

        String lotteryJson = YiboPreference.instance(getActivity()).getLotterys();
        StringBuilder sb = new StringBuilder();
        if (!Utils.isEmptyString(lotteryJson)) {
            Type listType = new TypeToken<ArrayList<LotteryData>>() {
            }.getType();
            List<LotteryData> list = new Gson().fromJson(lotteryJson, listType);
            if (pagecount == 0) {
                pagecount = (int) Math.ceil(list.size() / 5);
            }
            if (list != null && !list.isEmpty()) {
                for (int i = pageindex * 5; i < ((pageindex * 5) + 5 < list.size() ? (pageindex * 5) + 5 : list.size()); i++) {
                    sb.append(list.get(i).getCode()).append(",");
                }
            }
        }
        if (sb.toString().length() > 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ALL_LOTTERYS_COUNTDOWN_URL);
        configUrl.append("?lotCodes=").append(sb.toString());
        CrazyRequest<CrazyResult<GoucaiResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(DATA_REQUEST_ITEM)
                .headers(Urls.getHeader(getActivity()))
                .shouldCache(true)
                .refreshAfterCacheHit(true)
                .listener(this)
                .placeholderText(getActivity().getString(R.string.loading))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<GoucaiResultWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(getActivity(), request);

    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (null == getActivity()) return;
        if (getActivity().isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == DATA_REQUEST) {
            if (listView.isRefreshing()) {
                listView.stopRefresh();
            } else if (listView.isPullLoading()) {
                listView.stopLoadMore();
            }
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.get_goucai_data_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.get_goucai_data_fail);
                return;
            }
            Object regResult = result.result;
            GoucaiResultWraper reg = (GoucaiResultWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.get_goucai_data_fail));
                return;
            }
            YiboPreference.instance(getActivity()).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                if (pageindex == 0) {
                    cpResultList.clear();
                    cpLowList.clear();
                    cpHighList.clear();
                }
                if (pageindex < pagecount - 1) {
                    pageindex++;
                }
                cpResultList.addAll(reg.getContent());

                for (GoucaiResult r : cpResultList) {
                    if (Arrays.asList(lowRatelots).contains(r.getLotCode())) {
                        cpLowList.add(r);
                    } else {
                        cpHighList.add(r);
                    }
                }
                updateListView(currentPage);
                if (pageindex < pagecount - 1) {
                    actionAcquireData(pageindex, false);
                }
            }
        } else if (action == PLAY_RULES_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.request_fail) : errorString);
                return;
            }

            Object regResult = result.result;
            LocPlaysWraper reg = (LocPlaysWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(getActivity()).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                String json = new Gson().toJson(reg.getContent(), LotteryData.class);
                boolean isPeilvVersion = UsualMethod.isSixMark(getContext(),reg.getContent().getCode()) ?
                        true : UsualMethod.isPeilvVersionMethod(getActivity());
                String cpVersion = UsualMethod.isSixMark(getContext(),reg.getContent().getCode()) ?
                        String.valueOf(Constant.lottery_identify_V2) : YiboPreference.instance(getActivity()).getGameVersion();
                boolean isSimpleStyle = UsualMethod.getConfigFromJson(getActivity()).getBet_page_style().equalsIgnoreCase("V1");

                String gameCode = reg.getContent().getCode();
                if (isSimpleStyle) {
                    TouzhuSimpleActivity.createIntent(getActivity(), json, gameCode, isPeilvVersion, cpVersion);
                } else {
                    TouzhuActivity.createIntent(getActivity(), json, gameCode, isPeilvVersion, cpVersion);
                }

            }
        } else if (action == DATA_REQUEST_ITEM) {
            if (listView.isRefreshing()) {
                listView.stopRefresh();
            } else if (listView.isPullLoading()) {
                listView.stopLoadMore();
            }
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.get_goucai_data_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.get_goucai_data_fail);
                return;
            }
            Object regResult = result.result;
            GoucaiResultWraper reg = (GoucaiResultWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.get_goucai_data_fail));
                return;
            }
            YiboPreference.instance(getActivity()).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                if (pageindex == 0) {
                    cpResultList.clear();
                    cpLowList.clear();
                    cpHighList.clear();
                }
                if (pageindex < pagecount - 1) {
                    pageindex++;
                }
                cpResultList.addAll(reg.getContent());
                for (GoucaiResult r : cpResultList) {
                    if (Arrays.asList(lowRatelots).contains(r.getLotCode())) {
                        cpLowList.add(r);
                    } else {
                        cpHighList.add(r);
                    }
                }
//                listView.setPullLoadEnable(true);
                if (pageindex < pagecount - 1) {
                    actionAcquireData(false, position, false);
                }
                updateListView(currentPage, position);
            }
        }
    }

    protected int position = 0;

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                //刷新适配器
                //mRecommendActivitiesAdapter.notifyDataSetChanged();
                //优化刷新adapter的方法
                adapter.notifyData();
                if (BuildConfig.DEBUG) {
//                    Log.e(TAG, "单独刷新");
                }
                break;
            case 2:
//                pageindex = 0;
//                actionAcquireData(pageindex, false);
                break;

            case 3:
                pageindex = 0;
                position = msg.arg1;
//                actionAcquireData(false, position, false);
                break;

        }
        return false;
    }

    /**
     * 列表下拉，上拉监听器
     *
     * @author zhangy
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        public void onRefresh() {
            pageindex = 0;
            actionAcquireData(pageindex, false);
        }

        public void onLoadMore() {
        }
    }

    EmptyListView.EmptyListviewListener emptyListviewListener =
            new EmptyListView.EmptyListviewListener() {
                @Override
                public void onEmptyListviewClick() {
                    pageindex = 0;
                    actionAcquireData(pageindex, true);
                }
            };

    private void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    private void showToast(int showText) {
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cpResultList.clear();
        cpHighList.clear();
        cpLowList.clear();
    }

    public void updateListView(int currentPage) {
        if (currentPage == ALL_LOT_PAGE) {
            adapter.setmDatas(cpResultList);
        } else if (currentPage == HIGH_LOT_PAGE) {
            adapter.setmDatas(cpHighList);
        } else if (currentPage == LOW_LOT_PAGE) {
            adapter.setmDatas(cpLowList);
        }
        adapter.countDatas();
        adapter.notifyDataSetChanged();
        startThread();
    }


    private void startThread() {
        System.out.println("isThreadStart:" + isThreadStart);
        if (pageindex == pagecount - 1)
            if (!isThreadStart) {
//            Log.e(TAG, "线程启动了几次");
                isThreadStart = true;
                timeThread = new MyRunnable(adapter.getmDatas());

                timeThread.start();
            }
    }

    public void updateListView(int currentPage, int position) {
        if (currentPage == ALL_LOT_PAGE) {
            adapter.setmDatas(cpResultList);
        } else if (currentPage == HIGH_LOT_PAGE) {
            adapter.setmDatas(cpHighList);
        } else if (currentPage == LOW_LOT_PAGE) {
            adapter.setmDatas(cpLowList);
        }


        Log.e(TAG, "POSITION:" + position);
        adapter.countDatas();
        adapter.setLayoutId(R.layout.caipiao_list_item1);
        adapter.notifyDataSetItemChanged(listView, position);

    }

    //用来停止线程
    boolean endThread = false;

    public class MyRunnable extends Thread {

        private List<GoucaiResult> list;

        public void setList(List<GoucaiResult> list) {
            this.list = list;
        }

        MyRunnable(List<GoucaiResult> list) {
            this.list = list;
        }

        @Override
        public void run() {
            while (!endThread) {
//                Log.e("线程名称", "线程名称" + Thread.currentThread().getName() + "线程名称");
                try {
                    if (list != null) {
                        for (GoucaiResult goucaiResult : list) {
                            long agoCountTime = goucaiResult.agoDownTime;
                            long countTime = goucaiResult.countDownTime;

                            goucaiResult.time = Utils.int2Time(countTime);
//                        int ago = list.get(i).getAgo();
                            //如果时间差大于1秒钟，将每件商品的时间差减去一秒钟
                            // 并保存在每件商品的countDownTime属性内
                            if (agoCountTime > 1000) {
                                goucaiResult.agoDownTime = agoCountTime - 1000;
                            }
                            if (countTime > 1000) {
                                goucaiResult.countDownTime = countTime - 1000;
                            }
                        }
                        if (BuildConfig.DEBUG) {
//                            Log.e(TAG, "sendMessage:1");
                        }
                        handler.sendEmptyMessage(1);
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Adapter
     */
    private final class ListViewAdapter extends LAdapter<GoucaiResult> {

        private List<LViewHolder> mViewHolderList = new ArrayList<>();


        public ListViewAdapter() {

        }

        public void countDatas() {
            //遍历所有数据，算出时间差并保存在每个countDownTime属性内
            for (GoucaiResult mData : mDatas) {
                mData.countDownTime = Math.abs(mData.getActiveTime() - mData.getServerTime());
                mData.agoDownTime = Math.abs(mData.getActiveTime() - mData.getServerTime() - mData.getAgo() * 1000);
            }
        }
//        public ListViewAdapter(Context mContext, List<GoucaiResult> mDatas, int layoutId) {
//            super(mContext, mDatas, layoutId);
//            context = mContext;
//            this.mDatas = mDatas;
//            //遍历所有数据，算出时间差并保存在每个countDownTime属性内
//            for (GoucaiResult mData : mDatas) {
//                mData.countDownTime = Math.abs(mData.getActiveTime() - mData.getServerTime());
//                mData.agoDownTime = Math.abs(mData.getActiveTime() - mData.getServerTime() - mData.getAgo() * 1000);
//            }
//        }


        /**
         * 局部更新数据，调用一次getView()方法；
         * Google推荐的做法
         *
         * @param listView 要更新的listview
         * @param position 要更新的位置
         */
        public void notifyDataSetItemChanged(ListView listView, int position) {
            //第一个可见的位置
            int firstVisiblePosition = listView.getFirstVisiblePosition();
            //最后一个可见的位置
            int lastVisiblePosition = listView.getLastVisiblePosition();

            //在看见范围内才更新，不可见的滑动后自动会调用getView方法更新
            if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
                //获取指定位置view对象
                View view = listView.getChildAt(position - firstVisiblePosition);
                try {
                    getView(position, view, listView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


        private boolean isVisible(int item) {

            //第一个可见的位置
            int firstVisiblePosition = listView.getFirstVisiblePosition();
            //最后一个可见的位置
            int lastVisiblePosition = listView.getLastVisiblePosition();

            //在看见范围内才更新，不可见的滑动后自动会调用getView方法更新
            return item >= firstVisiblePosition && item <= lastVisiblePosition;

        }


        //遍历list，刷新相应holder的TextView
        public void notifyData() {
            for (int i = 0; i < mViewHolderList.size()-1; i++) {
                LViewHolder lViewHolder = mViewHolderList.get(i);
                if (mDatas.size() != 0) {
                    GoucaiResult goucaiResult = mDatas.get(lViewHolder.getPosition());
                    //ago到了的时候再次全部刷新
                    if (goucaiResult.agoDownTime <= 1000 && goucaiResult.getHaoMa().contains(",")) {
                        Message m = Message.obtain();
                        m.arg1 = i;
                        m.what = 2;
                        handler.sendMessage(m);
                        break;

                    }

                    //时间到0点,并且不是目前尚未开盘的时候刷新整个页面
                    if (null == goucaiResult.time) {
                        continue;
                    } else if (goucaiResult.time.equals("00:00:00") && goucaiResult.getHaoMa().contains(",")) {
                        Message m = Message.obtain();
                        m.arg1 = i;
                        m.what = 2;
                        handler.sendMessage(m);
                        break;
                    }
                }

                TextView textView = lViewHolder.getView(R.id.tv_time_count);
                if (mDatas.size() != 0) {
                    if (!mDatas.get(lViewHolder.getPosition()).getHaoMa().contains(",")) {
                        textView.setText("等待开奖");
                    } else {
                        textView.setText(String.format("第%s期投注时间还有:%s",
                                mDatas.get(lViewHolder.getPosition()).getQiHao(),
                                mDatas.get(lViewHolder.getPosition()).time));

                    }
                }

            }
        }


        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final GoucaiResult item) {

            //用holder绑定对应的position
            if (holder == null) {
                return;
            }
            holder.setDataPosition(position);

            if (!mViewHolderList.contains(holder)) {
                mViewHolderList.add(holder);
            }
            LinearLayout itemView = holder.getView(R.id.item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UsualMethod.syncLotteryPlaysByCode(mContext, item.getLotCode(), PLAY_RULES_REQUEST, CaigouMallContainerFragment.this);
                }
            });
            ImageView header = holder.getView(R.id.header);
            if (!Utils.isEmptyString(item.getLotIcon())) {
                UsualMethod.updateLocImageWithUrl(mContext, header, item.getLotIcon().trim());
            } else {
                UsualMethod.updateLocImage(mContext, header, item.getLotCode());
            }
            TextView name = holder.getView(R.id.name);
            TextView numbersTV = holder.getView(R.id.open_number_tv);
            GridView numbersView = holder.getView(R.id.numbers);
            numbersView.setEnabled(false);
            numbersView.setPressed(false);
            numbersView.setClickable(false);
            TextView lastQihaoTV = holder.getView(R.id.last_qihao);
            TextView deadLineTV = holder.getView(R.id.dead_time);
            TextView tv_time_count = holder.getView(R.id.tv_time_count);

            String lastQiHao;

            if (!Utils.isEmptyString(item.getLastQihao())) {
                lastQiHao = String.format(getString(R.string.di_qihao_format), item.getLastQihao().length() <= 6 ? item.getLastQihao() : item.getLastQihao().substring(
                        item.getLastQihao().length() - 6
                ));
            } else {
                lastQiHao = "暂无期号";
            }

            SpanUtils spanUtils = new SpanUtils();
            spanUtils.append(!Utils.isEmptyString(item.getLotName()) ? item.getLotName() :
                    getActivity().getString(R.string.temp_nocaizhong))
                    .append(" " + lastQiHao + " ").setForegroundColor(Color.parseColor("#ff5050"))
                    .append("开奖结果");
            name.setText(spanUtils.create());


            if (!Utils.isEmptyString(item.getHaoMa())) {
                if (item.getHaoMa().contains(",")) {
                    numbersTV.setVisibility(View.GONE);
                    numbersView.setVisibility(View.VISIBLE);
                    NumbersAdapter adapter = new NumbersAdapter(mContext, Utils.splitString(item.getHaoMa(), ","),
                            R.layout.number_gridview_item, item.getCodeType(), item.getLotCode());
//                    int column = (screenWidth - Utils.dip2px(context, 80) - 5) / Utils.dip2px(context, FIXED_BALL_WIDTH);
//                    Utils.LOG(TAG, "the figure out column == " + column);
                    int column = 10;
                    numbersView.setNumColumns(column);
                    numbersView.setAdapter(adapter);
                    Utils.setListViewHeightBasedOnChildren(numbersView, column, 10);
                } else {
                    numbersTV.setVisibility(View.VISIBLE);
                    numbersView.setVisibility(View.GONE);
                    numbersTV.setText(item.getHaoMa());
                }
            } else {
                numbersTV.setVisibility(View.VISIBLE);
                numbersView.setVisibility(View.GONE);
                numbersTV.setText("等待开奖");
            }

//            String text = String.format(getString(R.string.deadtime_touzhu_format),
//                    currentQihao) + Utils.int2Time(livetime);

            if ((!TextUtils.isEmpty(item.time))) {
                tv_time_count.setText("第" + item.getQiHao() + "期投注时间还有:" + item.time);
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        endThread = true;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}
