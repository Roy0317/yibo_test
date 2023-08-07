package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simon.utils.LogUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.NumbersAdapter;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.LocPlaysWraper;
import com.yibo.yiboapp.entify.OpenResultDetail;
import com.yibo.yiboapp.entify.OpenResultDetailWraper;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.manager.GameManager;
import com.yibo.yiboapp.manager.ManagerFactory;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * 彩票开奖结果列表
 */
public class KaijianListActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    public static final String TAG = KaijianListActivity.class.getSimpleName();
    private XListView listView;
    private EmptyListView empty;
    List<OpenResultDetail> kjList;
    ListViewAdapter adapter;
    public static final int LOAD_LIST_REQUEST = 0x01;
    public static final int PLAY_RULES_REQUEST = 0x02;
    String cpBianma;//彩票编码
    String cpType = "";
    int pageNo = 1;
    int pageSize = 20;
    private SysConfig sysConfig = UsualMethod.getConfigFromJson(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaijian_list);
        initView();
        kjList = new ArrayList<>();
        cpBianma = getIntent().getStringExtra("cp_code");
        cpType = getIntent().getStringExtra("cpType");
        adapter = new ListViewAdapter(this, kjList, R.layout.caipiao_kaijian_list_item, cpType);
        listView.setAdapter(adapter);
        actionGetList(true);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("立即投注");
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsualMethod.syncLotteryPlaysByCode(KaijianListActivity.this, cpBianma,
                        PLAY_RULES_REQUEST, KaijianListActivity.this);
            }
        });
    }

    private void actionGetList(boolean showDialog) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.OPEN_RESULT_DETAIL_URL);
        configUrl.append("?lotCode=").append(cpBianma);
        configUrl.append("&page=").append(pageNo);
        configUrl.append("&rows=").append(pageSize);
        CrazyRequest<CrazyResult<OpenResultDetailWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(LOAD_LIST_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(true)
                .refreshAfterCacheHit(true)
                .placeholderText(getString(R.string.loading))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<OpenResultDetailWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();

        RequestManager.getInstance().startRequest(this, request);
    }

    /**
     * 过滤并重新排序从接口回传的开奖结果
     */
    private List<OpenResultDetail> filterAndSortResults(List<OpenResultDetail> results){
        Map<String, OpenResultDetail> map = new HashMap<>();
        for(OpenResultDetail detail: results){
            map.put(detail.getQiHao(), detail);
        }

        List<OpenResultDetail> list = new ArrayList<>(map.values());
        Collections.sort(list, (o1, o2) -> {
            try{
                long n1 = Long.parseLong(o1.getQiHao());
                long n2 = Long.parseLong(o2.getQiHao());
                return (int) -(n1 - n2);
            }catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        });
        return list;
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getIntent().getStringExtra("cp_name"));
        tvBackText.setVisibility(View.VISIBLE);

        listView = (XListView) findViewById(R.id.xlistview);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setDivider(getResources().getDrawable(R.color.driver_line_color));
        listView.setXListViewListener(new ListviewListener());
        listView.setDividerHeight(3);
        listView.setVisibility(View.VISIBLE);
        empty = (EmptyListView) findViewById(R.id.empty);
        empty.setListener(emptyListviewListener);
    }

    public static void createIntent(Context context, String name, String cpCode, String cpType) {
        Intent intent = new Intent(context, KaijianListActivity.class);
        intent.putExtra("cp_name", name);
        intent.putExtra("cp_code", cpCode);
        intent.putExtra("cpType", cpType);
        context.startActivity(intent);
    }

    /**
     * 列表下拉，上拉监听器
     *
     * @author johnson
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        public void onRefresh() {
            pageNo = 1;
            actionGetList(false);
        }

        public void onLoadMore() {
            actionGetList(false);
        }
    }

    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override
        public void onEmptyListviewClick() {
            actionGetList(true);
        }
    };

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
//        RequestManager.getInstance().afterRequest(response);
        if (!handleCrazyResult(response)) {
            return;
        }
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == LOAD_LIST_REQUEST) {
            if (listView.isRefreshing()) {
                listView.stopRefresh();
            } else if (listView.isPullLoading()) {
                listView.stopLoadMore();
            }
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_fail);
                return;
            }
            Object regResult = result.result;
            OpenResultDetailWraper reg = (OpenResultDetailWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.acquire_fail));
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                if (pageNo == 1) {
                    kjList.clear();
                    kjList.addAll(reg.getContent());
                }else {
                    kjList.addAll(reg.getContent());
                    kjList = filterAndSortResults(kjList);
                }
            }

            adapter.setmDatas(kjList);
            adapter.notifyDataSetChanged();
            if (response.pickType != CrazyResponse.CACHE_REQUEST) {
                pageNo++;
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
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                String json = new Gson().toJson(reg.getContent(), LotteryData.class);
                boolean isPeilvVersion = UsualMethod.isSixMark(this, reg.getContent().getCode()) ?
                        true : UsualMethod.isPeilvVersionMethod(this);
                String cpVersion = UsualMethod.isSixMark(this, reg.getContent().getCode()) ?
                        String.valueOf(Constant.lottery_identify_V2) : YiboPreference.instance(this).getGameVersion();
                boolean isSimpleStyle = sysConfig.getBet_page_style().equalsIgnoreCase("V1");

                String gameCode = reg.getContent().getCode();
                if (isSimpleStyle) {
                    TouzhuSimpleActivity.createIntent(this, json, gameCode, isPeilvVersion, cpVersion);
                } else {
                    TouzhuActivity.createIntent(this, json, gameCode, isPeilvVersion, cpVersion);
                }

            }
        }
    }

    private final class ListViewAdapter extends LAdapter<OpenResultDetail> {

        Context context;
        String cpType;
        boolean scrollState = false;
        GameManager gameManager = ManagerFactory.INSTANCE.getGameManager();

        public ListViewAdapter(Context mContext, List<OpenResultDetail> mDatas, int layoutId, String cpType) {
            super(mContext, mDatas, layoutId);
            this.cpType = cpType;
            context = mContext;
        }

        public boolean isScrollState() {
            return scrollState;
        }

        public void setScrollState(boolean scrollState) {
            this.scrollState = scrollState;
        }

        private List<BallListItemInfo> convertNumbers(String numbers) {
            if (Utils.isEmptyString(numbers)) {
                return null;
            }
            List<String> nums = Utils.splitString(numbers, ",");
            List<BallListItemInfo> ballons = new ArrayList<BallListItemInfo>();
            for (String number : nums) {
                BallListItemInfo info = new BallListItemInfo();
                info.setNum(number);
                ballons.add(info);
            }
            return ballons;
        }


        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final OpenResultDetail item) {
            TextView qihao = holder.getView(R.id.qihao);
            GridView numbersView = holder.getView(R.id.numbers);
            TextView emptyView = holder.getView(R.id.empty_numbers);
            TextView tvLoading = holder.getView(R.id.tv_loading);
            LinearLayout secondLayout = holder.getView(R.id.layout2);
            TextView zhonghe = (TextView) secondLayout.findViewById(R.id.zhonghe);
            TextView bigsmall = (TextView) secondLayout.findViewById(R.id.bigsmall);
            TextView singledouble = (TextView) secondLayout.findViewById(R.id.singledouble);

            qihao.setText(String.format(getString(R.string.qihao_string), item.getQiHao().length() <= 6 ? item.getQiHao() :
                    item.getQiHao().substring(item.getQiHao().length() - 6)));
            List<String> haoMa = item.getHaoMaList();
            String haoma = "";
            if (haoMa != null && !haoMa.isEmpty()) {
                for (String hm : haoMa) {
                    haoma += hm + ",";
                }
                if (haoma.endsWith(",")) {
                    haoma = haoma.substring(0, haoma.length() - 1);
                }
                List<BallListItemInfo> numbers = convertNumbers(haoma);
                if (gameManager.needCalculateTotalDSDXByCpType(cpType)) {
                    List<BallListItemInfo> appendInfos = gameManager.figureOutALLDXDS(KaijianListActivity.this, numbers, cpType, cpBianma);
                    secondLayout.setVisibility(View.VISIBLE);
                    if (cpBianma.equalsIgnoreCase("PCEGG") || cpBianma.equalsIgnoreCase("JND28") ||
                            cpBianma.equalsIgnoreCase("FC3D") || cpBianma.equalsIgnoreCase("PL3")) {
                        String n = haoMa.get(haoMa.size() - 1);
                        if (Utils.isEmptyString(n) || !Utils.isNumeric(n)) {
                            zhonghe.setText(String.format("总和:%s", "无"));
                            bigsmall.setText(String.format("大小:%s", "无"));
                            singledouble.setText(String.format("单双:%s", "无"));
                        } else {
                            zhonghe.setText(String.format("总和:%s", n));
                            int total = /*Utils.getMaxOpenNumFromLotCode(cpBianma, numbers);*/27;
                            bigsmall.setText(String.format("大小:%s", Integer.parseInt(n) <= total / 2 ? "小" : "大"));
                            singledouble.setText(String.format("单双:%s", Integer.parseInt(n) % 2 == 0 ? "双" : "单"));
                        }
                    } else {
                        if (appendInfos != null && appendInfos.size() == 3) {
                            zhonghe.setVisibility(View.VISIBLE);
                            zhonghe.setText(String.format("总和:%s", appendInfos.get(0).getNum()));
                            bigsmall.setText(String.format("大小:%s", appendInfos.get(1).getNum()));
                            singledouble.setText(String.format("单双:%s", appendInfos.get(2).getNum()));
                        }
                    }
                    String bigsml = bigsmall.getText().toString();
                    String singdoub = singledouble.getText().toString();
                    String stationCode = sysConfig.getStationCode();
                    if (/*!"a179".equalsIgnoreCase(stationCode) && */(!Utils.isEmptyString(bigsml) && bigsml.equals("大小:小"))) {
                        bigsmall.setTextColor(context.getResources().getColor(R.color.blue2));
                    } else if (/*!"a179".equalsIgnoreCase(stationCode) && */(!Utils.isEmptyString(bigsml) && bigsml.equals("大小:大"))) {
                        bigsmall.setTextColor(context.getResources().getColor(R.color.red));
                    } else {
                        bigsmall.setTextColor(context.getResources().getColor(R.color.black));
                    }

                    if (/*!"a179".equalsIgnoreCase(stationCode) && */(!Utils.isEmptyString(singdoub) && singdoub.equals("单双:单"))) {
                        singledouble.setTextColor(context.getResources().getColor(R.color.blue2));
                    } else if (/*!"a179".equalsIgnoreCase(stationCode) && */(!Utils.isEmptyString(singdoub) && singdoub.equals("单双:双"))) {
                        singledouble.setTextColor(context.getResources().getColor(R.color.red));
                    } else {
                        singledouble.setTextColor(context.getResources().getColor(R.color.black));
                    }
                } else {
                    secondLayout.setVisibility(View.GONE);
                }


                if (gameManager.isKuaiSan(cpType)) {
                    boolean isOff = sysConfig.getK3_baozi_daXiaoDanShuang().equals("off");
                    //如果开关关闭 就显示豹子
                    if (isOff) {
                        if (numbers != null && numbers.size() >= 5) {
                            if (numbers.get(0).getNum().equals(numbers.get(1).getNum()) &&
                                    numbers.get(0).getNum().equals(numbers.get(2).getNum())) {
                                numbers.get(3).setNum("豹");
                                numbers.get(4).setNum("豹");
                            }
                        }
                    }
                }

                String numbersStr = "";
                for (BallListItemInfo hm : numbers) {
                    numbersStr += hm.getNum() + ",";
                }
                if (numbersStr.endsWith(",")) {
                    numbersStr = numbersStr.substring(0, numbersStr.length() - 1);
                }

                if (!Utils.isEmptyString(numbersStr)) {
                    if (numbersStr.contains(",")) {
                        emptyView.setVisibility(View.GONE);
                        numbersView.setVisibility(View.VISIBLE);
                        long date = 0;
                        if (!Utils.isEmptyString(item.getDate())) {
                            date = Utils.date2TimeStamp(item.getDate(), Utils.DATE_FORMAT);
                        }

                        NumbersAdapter adapter = new NumbersAdapter(context, Utils.splitString(numbersStr, ","),
                                R.layout.number_gridview_item, String.valueOf(cpType), cpBianma, date);
                        adapter.setShowShenxiao(true);

                        numbersView.setAdapter(adapter);
                        Utils.setListViewHeightBasedOnChildren(numbersView, 10);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        numbersView.setVisibility(View.GONE);
                        emptyView.setText(numbersStr);
                    }
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    numbersView.setVisibility(View.GONE);
                    emptyView.setText("等待开奖");
                }
            }
            TextView time = holder.getView(R.id.kaijian_time);
            time.setText((!Utils.isEmptyString(item.getDate()) ? item.getDate() : "") + " " +
                    (!Utils.isEmptyString(item.getTime()) ? item.getTime() : ""));
        }


    }

}

