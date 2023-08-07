package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.CampionResult;
import com.yibo.yiboapp.entify.SportListItem;
import com.yibo.yiboapp.entify.SportOrderDetail;
import com.yibo.yiboapp.entify.SportOrderDetailWraper;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;


public class SportOderDetailActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>>{

    XListView listView;
    EmptyListView empty;
    ListAdapter recordAdapter;
    List<SportListItem> listDatas;
    DecimalFormat decimalFormat;

    public static final int RECORD_DETAIL = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_oder_detail);
        initView();

        decimalFormat = new DecimalFormat("0.00");
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listDatas = new ArrayList<>();
        recordAdapter = new ListAdapter(this, listDatas, R.layout.sport_detail_layout);
        listView.setAdapter(recordAdapter);
        long id = getIntent().getLongExtra("id",0);
        getDetails(id,true);
    }

    private void getDetails(long id,boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SPORT_RECORDS_DETAIL);
        configUrl.append("?id=").append(id);

        CrazyRequest<CrazyResult<SportOrderDetailWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(RECORD_DETAIL)
                .headers(Urls.getHeader(this))
                .cachePeroid(60*1000)
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<SportOrderDetailWraper>(){}.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this,request);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("体育投注详情");
        listView = (XListView) findViewById(R.id.xlistview);
        empty = (EmptyListView) findViewById(R.id.empty);
        empty.setListener(emptyListviewListener);

    }
    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override public void onEmptyListviewClick() {
            long id = getIntent().getLongExtra("id",0);
            getDetails(id,true);
        }
    };

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == RECORD_DETAIL) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.acquire_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.acquire_fail);
                return;
            }
            Object regResult =  result.result;
            SportOrderDetailWraper reg = (SportOrderDetailWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg())?reg.getMsg():
                        getString(R.string.acquire_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                    empty.setVisibility(View.VISIBLE);
                    listView.setEmptyView(empty);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                listDatas.clear();
                SportOrderDetail record = reg.getContent();

                SportListItem item1 = new SportListItem();
                item1.setName("注单编号");
                item1.setValue(record.getBettingCode());
                listDatas.add(item1);

                SportListItem item2 = new SportListItem();
                item2.setName("投注时间");
                item2.setValue(Utils.formatTime(record.getBettingDate()));
                listDatas.add(item2);

                SportListItem item3 = new SportListItem();
                item3.setName("赛事情况");
                item3.setValue(getMatchInfo(record));
                listDatas.add(item3);

                SportListItem item4 = new SportListItem();
                item4.setName("球类");
                item4.setValue(getSportName(record.getSportType()));
                listDatas.add(item4);

                SportListItem item5 = new SportListItem();
                item5.setName("类型");
                item5.setValue(record.getTypeNames());
                listDatas.add(item5);

                SportListItem item6 = new SportListItem();
                item6.setName("下注金额");
                item6.setValue(decimalFormat.format(record.getBettingMoney()));
                listDatas.add(item6);

                SportListItem item7 = new SportListItem();
                item7.setName("提交状态");
                item7.setValue(UsualMethod.convertSportCommitStatus(record.getBettingStatus()));
                listDatas.add(item7);

                SportListItem item8 = new SportListItem();
                item8.setName("结算状态");
                item8.setValue(convertBalanceStatus(record.getBalance()));
                listDatas.add(item8);

                if(record.getBalance() == Constant.BALANCE_DONE ||
                        record.getBalance() == Constant.BALANCE_AGENT_HAND_DONE ||
                        record.getBalance() == Constant.BALANCE_BFW_DONE){
                    SportListItem item9 = new SportListItem();
                    item9.setName("派彩金额");
                    item9.setValue(record.getBettingResult() !=0 ? record.getBettingResult()+"元":"-");
                    listDatas.add(item9);
                }
                recordAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getMatchInfo(SportOrderDetail detail){
        if (detail == null) {
            return "";
        }
        String remark = detail.getRemark();
        if (Utils.isEmptyString(remark)) {
            return "";
        }
        if (detail.getMix() != 2) {
            CampionResult result = new Gson().fromJson(remark, CampionResult.class);
            if (result != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(toBetJson(detail,result));
                String html = "<html><head></head><body>"+sb.toString()+"</body></html>";
                return html;
            }
        }else{
            Type listType = new TypeToken<ArrayList<CampionResult>>() {}.getType();
            List<CampionResult> results = new Gson().fromJson(remark, listType);
            if (results == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<results.size();i++) {
                CampionResult campionResult = results.get(i);
                sb.append(toBetJson(detail,campionResult));
                if (i != results.size()-1) {
                    sb.append("<br/>")
                            .append("..................................................")
                            .append("<br/>");
                }
            }
            String html = "<html><head></head><body>"+sb.toString()+"</body></html>";
            return html;
        }
        return "";
    }

    private String toBetJson(SportOrderDetail detail,CampionResult campionResult) {
        if (campionResult == null) {
            return "";
        }
        String con = campionResult.getCon();
        boolean homeFirst = detail.getHomeTeam()  == campionResult.getFirstTeam();//主队是否在前
        String scoreStr = "";

        if(detail.getGameTimeType() == 1){
            if (detail.getScoreC() != null && detail.getScoreH() != null) {
                if(homeFirst){
                    scoreStr = "("+detail.getScoreH()+":"+detail.getScoreC()+")";
                }else{
                    scoreStr = "("+detail.getScoreC()+":"+detail.getScoreH()+")";
                }
            }else{
                scoreStr = "(0:0)";
            }
        }
        String home = campionResult.getFirstTeam();
        String guest = campionResult.getLastTeam();

        if(campionResult.isHalf() == true && detail.getMix() == 2){
            home = home + "<font color='gray'>[上半]</font>";
            guest = guest + "<font color='gray'>[上半]</font>";
        }

        String html  = campionResult.getLeague() +"<br/>" +
                home + "&nbsp;&nbsp;" + con + "&nbsp;&nbsp;" + guest + scoreStr + "<br/>" +
                "<font color='red'>"+campionResult.getResult()+ "</font>&nbsp;&nbsp;"
                +"@" + "&nbsp;&nbsp;<font color='red'>"
                + campionResult.getOdds() +"</font>";
        long balance = detail.getMix() != 2 ? detail.getBalance() : campionResult.getBalance();
        long bt = detail.getBettingStatus();
        if(balance == 4){
            html = "<s style='color:red;'>" + html+"(赛事腰斩)</s>";
        }else if(bt == 3 || bt == 4){
            html = "<s style='color:red;'>" + html+"("+(!Utils.isEmptyString(detail.getStatusRemark())
                    ?detail.getStatusRemark():"未填写取消说明")+")</s>";
        }else if(balance == 2 || balance == 5 || balance == 6){
            String mr = detail.getMix() != 2 ? detail.getResult():campionResult.getMatchResult();
            if(homeFirst){
                html = html + "&nbsp;<font color='blue'>("+mr+")</font>";
            }else{
                String[] ss = mr.split(":");
                html = html + "&nbsp;<font color='blue'>("+ss[0]+":"+ss[1]+")</font>";
            }
        }
        return html;
    }

    private String getSportName(long type){
        if(type == 1){
            return "足球";
        }
        if(type == 2){
            return "篮球";
        }
        return "其他";
    }

    private String convertBalanceStatus(long status) {
        if (status == Constant.BALANCE_UNDO) {
            return "未结算";
        }
        if (status == Constant.BALANCE_CUT_GAME) {
            return "赛事取消";
        }

        if(status == Constant.BALANCE_DONE || status == Constant.BALANCE_AGENT_HAND_DONE
                || status == Constant.BALANCE_BFW_DONE){
            return "已结算";
        }
        return "未结算";
    }


    public class ListAdapter extends LAdapter<SportListItem> {

        Context context;

        public ListAdapter(Context mContext, List<SportListItem> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override public void convert(int position, LViewHolder holder, ViewGroup parent, final SportListItem item) {
            TextView sportName = holder.getView(R.id.name);
            TextView sportValue = holder.getView(R.id.value);
            sportName.setText(item.getName());
            sportValue.setText(Html.fromHtml(item.getValue(), null, null));
        }
    }

    public static void createIntent(Context context,long id) {
        Intent intent = new Intent(context, SportOderDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listDatas.clear();
    }


}
