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
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.NewCampionResult;
import com.yibo.yiboapp.entify.NewSportOrderBean;
import com.yibo.yiboapp.entify.SportListItem;
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
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * 新体育下注记录详情
 */

public class NewSportOrderDetailActivity extends BaseActivity implements
        SessionResponse.Listener<CrazyResult<Object>>{

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
        String content = getIntent().getStringExtra("content");
        fillContents(content);
    }

    private void fillContents(String content) {

        if (Utils.isEmptyString(content)) {
            return;
        }

        NewSportOrderBean record = new Gson().fromJson(content, NewSportOrderBean.class);
        listDatas.clear();

        SportListItem item1 = new SportListItem();
        item1.setName("注单编号");
        item1.setValue(record.getOrderId());
        listDatas.add(item1);

        SportListItem item2 = new SportListItem();
        item2.setName("投注时间");
        item2.setValue(Utils.formatTime(record.getCreateDatetime()));
        listDatas.add(item2);

        SportListItem item3 = new SportListItem();
        item3.setName("赛事情况");
        item3.setValue(getMatchInfo(record));
        listDatas.add(item3);

        SportListItem item4 = new SportListItem();
        item4.setName("球类");
        item4.setValue(UsualMethod.convertSportBallon(record.getSportType()));
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
        String balance = "";
        if (record.getResultStatus() == Constant.BALANCE_HALF_WIN ||
                record.getResultStatus() == Constant.BALANCE_ALL_WIN) {
            balance = String.format("%s(%.2f元)", convertBalanceStatus(record.getResultStatus(),""), record.getWinMoney());
        }else{
            balance = convertBalanceStatus(record.getResultStatus(),record.getStatusRemark());
        }
        item8.setValue(balance);
        listDatas.add(item8);

        if(record.getBettingStatus() == Constant.BALANCE_HALF_WIN ||
                record.getBalance() == Constant.BALANCE_ALL_WIN){
            SportListItem item9 = new SportListItem();
            item9.setName("派彩金额");
            item9.setValue(String.format("%.1f元",record.getWinMoney()));
            listDatas.add(item9);
        }

//        SportListItem item11 = new SportListItem();
//        item11.setName("开赛时间");
//        item11.setValue(Utils.formatTime(record.getStartDatetime()));
//        listDatas.add(item11);

        recordAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("体育投注详情");
        listView = (XListView) findViewById(R.id.xlistview);
        empty = (EmptyListView) findViewById(R.id.empty);
    }

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

            }
        }
    }

    private String getMatchInfo(NewSportOrderBean bean){
        if (bean == null) {
            return "";
        }
        if (Utils.isEmptyString(bean.getRemark())) {
            return "";
        }
        if (bean.getMix() != 2) {
            NewCampionResult result = new Gson().fromJson(bean.getRemark(), NewCampionResult.class);
            if (result != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(toBetJson(bean,result));
                String html = "<html><head></head><body>"+sb.toString()+"</body></html>";
                return html;
            }
        }else{
            Type listType = new TypeToken<ArrayList<NewCampionResult>>() {}.getType();
            List<NewCampionResult> results = new Gson().fromJson(bean.getRemark(), listType);
            if (results == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<results.size();i++) {
                NewCampionResult campionResult = results.get(i);
                sb.append(toBetJson(bean,campionResult));
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

    private String toBetJson(NewSportOrderBean detail,NewCampionResult campionResult) {
        if (campionResult == null) {
            return "";
        }
        String con = campionResult.getCon();
        String scoreStr = "";

        if(!Utils.isEmptyString(detail.getResult())){
            scoreStr = "("+detail.getResult()+")";
        }else if(detail.getTimeType() == 1){
            scoreStr = "("+detail.getHomeScore()+":"+detail.getGuestScore()+")";
        }

        String home = campionResult.getHomeTeam();
        String guest = campionResult.getGuestTeam();
        if(campionResult.isHalf() == true && detail.getMix() == 2){
            home = home + "<font color='gray'>[上半]</font>";
            guest = guest + "<font color='gray'>[上半]</font>";
        }

        long beijingTime = campionResult.getStartTime() + 12 * 3600 * 1000;//接口回传的是美东时间的时间戳
        String html  = campionResult.getLeague() +"<br/>" +
                home + "&nbsp;&nbsp;" + con + "&nbsp;&nbsp;" + guest + "<br/>" +
                "<font color='red'>"+campionResult.getResult()+ "</font>&nbsp;&nbsp;"
                +"@" + "&nbsp;&nbsp;<font color='red'>"
                + campionResult.getOdds() +"</font>&nbsp;&nbsp;<font color='blue'>"+scoreStr+"</font>" + "<br/>" +
                "开赛时间(北京):" + Utils.formatTime(beijingTime);

        return html;
    }

    private String convertBalanceStatus(long status, String statusRemark) {
        if (status == Constant.BALANCE_ALL_LOST) {
            return "全输";
        }else if (status == Constant.BALANCE_HALF_LOST) {
            return "输一半";
        }else if (status == Constant.BALANCE_DRAW) {
            return "平局";
        }else if (status == Constant.BALANCE_HALF_WIN) {
            return "赢一半";
        }else if (status == Constant.BALANCE_ALL_WIN) {
            return "全赢";
        } else if (status == 6){
            return  statusRemark;
        }
        return "等待开奖";
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

    public static void createIntent(Context context,String content) {
        Intent intent = new Intent(context, NewSportOrderDetailActivity.class);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listDatas.clear();
    }


}
