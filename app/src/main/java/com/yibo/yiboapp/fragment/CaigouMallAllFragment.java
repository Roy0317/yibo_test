package com.yibo.yiboapp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
public class CaigouMallAllFragment extends Fragment implements SessionResponse.Listener
        <CrazyResult<Object>>, Handler.Callback {


    private static final String TAG = "CaigouMallContainerFrag";

    private View view;

    private ArrayList<GoucaiResult> cpResultList;
    public static final int DATA_REQUEST = 0x01;
    public static final int PLAY_RULES_REQUEST = 0x02;
    public static final int DATA_REQUEST_ITEM = 0x03;
    private XListView listView;
    private RecyclerView mRecycle;
    private EmptyListView empty;
    private CountDownTimerAdapter mCountDownTimerAdapter;
    public static final String[] lowRatelots = new String[]{"LHC", "FC3D", "PL3", "TTLHC", "QMLHC",
            "FLBLHC", "AMLHC2", "AMLHC", "TWLHC", "DMLHC", "YNLHC"};
    int pageindex = 0;
    int pagecount = 0;
    public int currentPage = 0;
    public ArrayList<String> arrs;
    Timer mTimer;
    MyTask myTask;
    private Handler handler;
    //    public MyRunnable timeThread;
    private boolean isThreadStart = false;
    List<LotteryData> list;//彩票类型数组
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static CaigouMallAllFragment newInstance(int currentPage) {
        CaigouMallAllFragment fragment = new CaigouMallAllFragment();
        Bundle args = new Bundle();
        args.putInt("currentPage", currentPage);
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.caigou_mall_container_new_fragment, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        currentPage = getArguments().getInt("currentPage");
        cpResultList = new ArrayList<>();
        requestbyCurrentPage();
        if (currentPage == 0) {
            actionAcquireData(true);
        } else {
            actionAcquireData(false);
        }
    }


    private void initView(View view) {
        mCountDownTimerAdapter = new CountDownTimerAdapter(getContext(), cpResultList);
        mRecycle = view.findViewById(R.id.mRecyclerView);
        mRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycle.setAdapter(mCountDownTimerAdapter);
//        empty.setVisibility(View.GONE);
        handler = new Handler(this);
        arrs = new ArrayList<String>();
        mTimer = new Timer();
        myTask = new MyTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        actionAcquireData(false);
    }

    /**
     * 根据页面类型计算请求总页数
     */
    void requestbyCurrentPage() {
        String lotteryJson = YiboPreference.instance(getActivity()).getLotterys();
        if (!Utils.isEmptyString(lotteryJson)) {
            Type listType = new TypeToken<ArrayList<LotteryData>>() {
            }.getType();
            list = new Gson().fromJson(lotteryJson, listType);

            switch (currentPage) {
                case 0://全部
                    if (pagecount == 0) {
                        pagecount = (int) Math.ceil((double) list.size() / 5);
                    }
                    break;
                case 1://高频彩
                    List<LotteryData> list2 = new ArrayList<LotteryData>();
                    for (int i = 0; i < list.size(); i++) {
                        if (!Arrays.asList(lowRatelots).contains(list.get(i).getCode())) {
                            list2.add(list.get(i));
                        }
                    }
                    list.clear();
                    list.addAll(list2);
                    if (pagecount == 0) {
                        pagecount = (int) Math.ceil((double) list.size() / 5);
                    }
                    break;
                case 2://低频彩
                    List<LotteryData> list3 = new ArrayList<LotteryData>();
                    for (int i = 0; i < list.size(); i++) {
                        if (Arrays.asList(lowRatelots).contains(list.get(i).getCode())) {
                            list3.add(list.get(i));
                        }
                    }
                    list.clear();
                    list.addAll(list3);
                    if (pagecount == 0) {
                        pagecount = (int) Math.ceil((double) list.size() / 5);
                    }

                    break;
            }
        }

    }

    /**
     * 请求多次刷新数据
     *
     * @param showDialog
     */
    public void actionAcquireData(boolean showDialog) {

        if (!Utils.isNetworkAvailable(getActivity())) {
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            return;
        }
        StringBuilder sb = new StringBuilder();
        //每五个数据一次请求
        if (list != null && !list.isEmpty()) {
            for (int i = pageindex * 5; i < ((pageindex * 5) + 5 < list.size() ? (pageindex * 5) + 5 : list.size()); i++) {
                if (list.get(i).getCode() != null) {
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

    /**
     * 请求单个刷新数据
     *
     * @param showDialog
     * @param postion
     * @param flag
     */
    private void actionAcquireData(boolean showDialog, final int postion, boolean flag) {
        if (!Utils.isNetworkAvailable(getActivity())) {
//            empty.setVisibility(View.VISIBLE);
//            listView.setEmptyView(empty);
            return;
        }
        ApiParams params = new ApiParams();
        params.put("lotCodes", cpResultList.get(postion).getLotCode());
        HttpUtil.get(getActivity(), Urls.ALL_LOTTERYS_COUNTDOWN_URL, params, showDialog, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    List<GoucaiResult> list = new Gson().fromJson(result.getContent(), new TypeToken<List<GoucaiResult>>() {
                    }.getType());
                    mCountDownTimerAdapter.getMlist().set(postion, list.get(0));
                    updateListView(postion);
                }

            }
        });
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
//            if (listView.isRefreshing()) {
//                listView.stopRefresh();
//            } else if (listView.isPullLoading()) {
//                listView.stopLoadMore();
//            }
//            empty.setVisibility(View.VISIBLE);
//            listView.setEmptyView(empty);
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
                }
                cpResultList.addAll(reg.getContent());
                updateListView();
                if (pageindex < pagecount - 1) {
                    pageindex++;
                    if (list.get(pageindex * 5) != null && list.get(pageindex * 5).getCode() != null) {
                        actionAcquireData(false);
                    }
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
                boolean isPeilvVersion = UsualMethod.isSixMark(getContext(), reg.getContent().getCode()) ?
                        true : UsualMethod.isPeilvVersionMethod(getActivity());
                String cpVersion = UsualMethod.isSixMark(getContext(), reg.getContent().getCode()) ?
                        String.valueOf(Constant.lottery_identify_V2) : YiboPreference.instance(getActivity()).getGameVersion();
                boolean isSimpleStyle = UsualMethod.getConfigFromJson(getActivity()).getBet_page_style().equalsIgnoreCase("V1");

                String gameCode = reg.getContent().getCode();
                if (isSimpleStyle) {
                    TouzhuSimpleActivity.createIntent(getActivity(), json, gameCode, isPeilvVersion, cpVersion);
                } else {
                    TouzhuActivity.createIntent(getActivity(), json, gameCode, isPeilvVersion, cpVersion);
                }

            }
        }
    }


    /**
     * 列表下拉，上拉监听器
     *
     * @author zhangy
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        public void onRefresh() {
//            pageindex = 0;
//            actionAcquireData(pageindex, false);
        }

        public void onLoadMore() {
        }
    }

    EmptyListView.EmptyListviewListener emptyListviewListener =
            new EmptyListView.EmptyListviewListener() {
                @Override
                public void onEmptyListviewClick() {
                    pageindex = 0;
                    actionAcquireData(true);
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
    }

    /**
     * 批量刷新列表
     */
    public void updateListView() {
        mCountDownTimerAdapter.setMlist(cpResultList);
        //增量刷新适配器而不是全局刷新
        mCountDownTimerAdapter.notifyItemRangeChanged(pageindex * 5, cpResultList.size());
        if (!isThreadStart) {
            mTimer.schedule(myTask, 0, 1000);
            isThreadStart = true;
        }
    }

    /**
     * 根据索引更新列表
     **/
    public void updateListView(int position) {
        mCountDownTimerAdapter.notifyItemChanged(position, "updateother");
        arrs.remove(position + "");

    }

    public class CountDownTimerAdapter extends RecyclerView.Adapter<CountDownTimerAdapter.Holder> {
        private ArrayList<GoucaiResult> mlist;
        private Context mContext;

        CountDownTimerAdapter(Context mContext, ArrayList<GoucaiResult> mlist) {
            setMlist(mlist);
            this.mContext = mContext;
        }

        public void setMlist(ArrayList<GoucaiResult> mlist) {

            this.mlist = mlist;

        }

        public List<GoucaiResult> getMlist() {
            return mlist;
        }

        @Override
        public CountDownTimerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.caipiao_list_item1, parent, false);
            return new Holder(view);
        }

        /**
         * 选择需要改变的列表部分
         *
         * @param holder
         * @param position
         * @param payloads
         */
        @Override
        public void onBindViewHolder(@NonNull CountDownTimerAdapter.Holder holder, int position, @NonNull List<Object> payloads) {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
                return;
            }
            GoucaiResult item = mlist.get(position);
            Holder viewHolder = (Holder) holder;
            //时间到0点,并且不是目前尚未开盘的时候刷新整个页面
            for (int i = 0; i < payloads.size(); i++) {
                if (payloads.get(i).equals("updatetime"))
                    if (!TextUtils.isEmpty(item.time)) {
//
                        if (mlist.size() != 0) {

                            if (item.getHaoMa() == null || !item.getHaoMa().contains(",")) {
                                viewHolder.tv_time_count.setText("等待开奖");
                            } else {
                                viewHolder.tv_time_count.setText(String.format("第%s期投注时间还有:%s",
                                        item.getQiHao(),
                                        item.time));
                            }
                        }
                    }
                if (payloads.get(i).equals("updateother")) {
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
                    viewHolder.name.setText(spanUtils.create());


                    if (!Utils.isEmptyString(item.getHaoMa())) {
                        if (item.getHaoMa().contains(",")) {
                            viewHolder.numbersTV.setVisibility(View.GONE);
                            viewHolder.numbersView.setVisibility(View.VISIBLE);
                            NumbersAdapter adapter = new NumbersAdapter(mContext, Utils.splitString(item.getHaoMa(), ","),
                                    R.layout.number_gridview_item, item.getCodeType(), item.getLotCode());
                            int column = 10;
                            viewHolder.numbersView.setNumColumns(column);
                            viewHolder.numbersView.setAdapter(adapter);
                            Utils.setListViewHeightBasedOnChildren(viewHolder.numbersView, column, 10);
                        } else {
                            viewHolder.numbersTV.setVisibility(View.VISIBLE);
                            viewHolder.numbersView.setVisibility(View.GONE);
                            viewHolder.numbersTV.setText(item.getHaoMa());
                        }
                    } else {
                        viewHolder.numbersTV.setVisibility(View.VISIBLE);
                        viewHolder.numbersView.setVisibility(View.GONE);
                        viewHolder.numbersTV.setText("等待开奖");
                    }

                    if ((!TextUtils.isEmpty(item.time))) {
                        viewHolder.tv_time_count.setText("第" + item.getQiHao() + "期投注时间还有:" + item.time);
                    }
                }
            }

        }

        @Override
        public void onBindViewHolder(@NonNull CountDownTimerAdapter.Holder holder, int position) {
            final GoucaiResult item = mlist.get(position);
            final Holder viewHolder = (Holder) holder;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UsualMethod.syncLotteryPlaysByCode(mContext, item.getLotCode(),
                            PLAY_RULES_REQUEST, CaigouMallAllFragment.this);
                }
            });

            if (!Utils.isEmptyString(item.getLotIcon())) {
                UsualMethod.updateLocImageWithUrl(mContext, viewHolder.header, item.getLotIcon().trim());
            } else {
                UsualMethod.updateLocImage(mContext, viewHolder.header, item.getLotCode());
            }

            viewHolder.numbersView.setEnabled(false);
            viewHolder.numbersView.setPressed(false);
            viewHolder.numbersView.setClickable(false);

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
            viewHolder.name.setText(spanUtils.create());


            if (!Utils.isEmptyString(item.getHaoMa())) {
                if (item.getHaoMa().contains(",")) {
                    viewHolder.numbersTV.setVisibility(View.GONE);
                    viewHolder.numbersView.setVisibility(View.VISIBLE);
                    NumbersAdapter adapter = new NumbersAdapter(mContext, Utils.splitString(item.getHaoMa(), ","),
                            R.layout.number_gridview_item, item.getCodeType(), item.getLotCode());
                    int column = 10;
                    viewHolder.numbersView.setNumColumns(column);
                    viewHolder.numbersView.setAdapter(adapter);
                    Utils.setListViewHeightBasedOnChildren(viewHolder.numbersView, column, 10);
                } else {
                    viewHolder.numbersTV.setVisibility(View.VISIBLE);
                    viewHolder.numbersView.setVisibility(View.GONE);
                    viewHolder.numbersTV.setText(item.getHaoMa());
                }
            } else {
                viewHolder.numbersTV.setVisibility(View.VISIBLE);
                viewHolder.numbersView.setVisibility(View.GONE);
                viewHolder.numbersTV.setText("等待开奖");
            }

            if ((!TextUtils.isEmpty(item.time))) {
                viewHolder.tv_time_count.setText("第" + item.getQiHao() + "期投注时间还有:" + item.time);
            }
        }

        @Override
        public int getItemCount() {
            return mlist == null ? 0 : mlist.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            ImageView header;
            TextView name;
            TextView numbersTV;
            GridView numbersView;
            TextView lastQihaoTV;
            TextView deadLineTV;
            TextView tv_time_count;
            LinearLayout itemView;

            public Holder(View ItemView) {
                super(ItemView);
                header = ItemView.findViewById(R.id.header);
                name = ItemView.findViewById(R.id.name);
                numbersTV = ItemView.findViewById(R.id.open_number_tv);
                numbersView = ItemView.findViewById(R.id.numbers);
                lastQihaoTV = ItemView.findViewById(R.id.last_qihao);
                deadLineTV = ItemView.findViewById(R.id.dead_time);
                tv_time_count = ItemView.findViewById(R.id.tv_time_count);
                itemView = ItemView.findViewById(R.id.item);

            }
        }
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            if (mCountDownTimerAdapter.getMlist().isEmpty()) {
                return;
            }
            int size = mCountDownTimerAdapter.getMlist().size();
            GoucaiResult bean;
            for (int i = 0; i < size; i++) {
                bean = mCountDownTimerAdapter.getMlist().get(i);
                long countTime = Math.abs(bean.getActiveTime() - System.currentTimeMillis() > 1000 ? bean.getActiveTime() - System.currentTimeMillis() : 0);
                bean.time = Utils.int2Time(countTime);
//                        int ago = list.get(i).getAgo();
                //如果时间差大于1秒钟，将每件商品的时间差减去一秒钟
                // 并保存在每件商品的countDownTime属性内
                if (!TextUtils.isEmpty(bean.getHaoMa())) {
                    if ("00:00:00".equals(bean.time) && ",".contains(bean.getHaoMa())) {
                        Message m = Message.obtain();
                        m.arg1 = i;
                        m.what = 2;
                        handler.sendMessage(m);
                    }
                }
                mCountDownTimerAdapter.getMlist().set(i, bean);
                Message message = handler.obtainMessage(1);
                message.arg1 = i;
                handler.sendMessage(message);
            }

        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                //刷新适配器
                //优化刷新adapter的方法
                //局部刷新，只刷新时间字段
                mCountDownTimerAdapter.notifyItemChanged(msg.arg1, "updatetime");
                if (BuildConfig.DEBUG) {
//                    Log.e(TAG, "单独刷新");
                }
                break;
            case 2:
                //用来过滤掉重复请求
                if (arrs.size() != 0) {
                    for (String a : arrs) {
                        if (a.equals(msg.arg1 + ""))
                            return false;
                    }
                }
                arrs.add(msg.arg1 + "");
                actionAcquireData(false, msg.arg1, false);
                break;

            case 3:
                break;

        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        endThread = true;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}
