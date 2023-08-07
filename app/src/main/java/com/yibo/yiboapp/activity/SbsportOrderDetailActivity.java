package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.SBSportOrder;
import com.yibo.yiboapp.entify.SportListItem;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class SbsportOrderDetailActivity extends BaseActivity {

    XListView listView;
    EmptyListView empty;
    ListAdapter recordAdapter;
    List<SportListItem> listDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_oder_detail);
        initView();
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listDatas = new ArrayList<>();
        recordAdapter = new ListAdapter(this, listDatas, R.layout.sport_detail_layout);
        listView.setAdapter(recordAdapter);
        String order = getIntent().getStringExtra("order");
        if (!Utils.isEmptyString(order)) {
            formDatas(order);
        }
    }

    private void formDatas(String order) {
        SBSportOrder bean = new Gson().fromJson(order, SBSportOrder.class);
        if (bean != null) {
            listDatas.clear();

            SportListItem item1 = new SportListItem();
            item1.setName("注单编号");
            item1.setValue(bean.getTransId());
            listDatas.add(item1);

            SportListItem item2 = new SportListItem();
            item2.setName("投注时间");
            item2.setValue(Utils.formatTime(bean.getTransactionTime()));
            listDatas.add(item2);

            SportListItem item3 = new SportListItem();
            item3.setName("赛事情况");
            item3.setValue(getMatchInfo(bean));
            listDatas.add(item3);

            SportListItem item4 = new SportListItem();
            item4.setName("球类");

            if (bean.getMix() == 2) {
                String ballName = "";
                if (bean.getChildrens() != null) {
                    for (SBSportOrder sbSportOrder : bean.getChildrens()) {
                        ballName = sbSportOrder.getSportTypeName();
                    }
                }
                String a = ballName + "("+bean.getBetTypeName()+")";
                item4.setValue(a);
            }else{
                item4.setValue(bean.getSportTypeName()+"("+bean.getBetTypeName()+")");
            }

            listDatas.add(item4);

            SportListItem item5 = new SportListItem();
            item5.setName("类型");
            if (bean.getMix() == 2) {
                String pansName = "";
                if (bean.getChildrens() != null) {
                    for (SBSportOrder sbSportOrder : bean.getChildrens()) {
                        pansName += sbSportOrder.getBetTypeName()+"/";
                    }
                }
                item5.setValue(pansName);
            }else{
                StringBuilder sb = new StringBuilder();
                sb.append(bean.getBetOddsTypeName()).append("--");
                if (!Utils.isEmptyString(bean.getBetTeamName())) {
                    if (bean.getBetTeamName().equalsIgnoreCase("小")) {
                        sb.append("小");
                    } else if (bean.getBetTeamName().equalsIgnoreCase("大")) {
                        sb.append("大");
                    }else if (bean.getBetTeamName().equalsIgnoreCase("客队")) {
                        sb.append((!Utils.isEmptyString(bean.getAwayName()) ? bean.getAwayName() : "")).append(" ");
                    }else{
                        sb.append((!Utils.isEmptyString(bean.getHomeName()) ? bean.getHomeName() : "")).append(" ");
                    }
                }else{
                    sb.append((!Utils.isEmptyString(bean.getHomeName()) ? bean.getHomeName() : "")).append(" ");
                }
                sb.append(((bean.getHdp() != null && bean.getHdp() != 0) ? bean.getHdp() : "") + "@" + bean.getBetOdds());
                item5.setValue(sb.toString());
            }
            listDatas.add(item5);

            SportListItem item6 = new SportListItem();
            item6.setName("下注金额");
            item6.setValue(String.format("%.2f元",bean.getAccountBetMoney()));
            listDatas.add(item6);

            SportListItem item7 = new SportListItem();
            item7.setName("提交状态");
            item7.setValue(UsualMethod.getOrderStatus(bean.getTicketStatus()));
            listDatas.add(item7);

            SportListItem item8 = new SportListItem();
            item8.setName("结算状态");
            item8.setValue(UsualMethod.getBetStatus(bean.getTicketStatus()));
            listDatas.add(item8);

            SportListItem item9 = new SportListItem();
            item9.setName("派彩金额");
            item9.setValue(String.format("%.2f元",bean.getWinLostMoney()));
            listDatas.add(item9);

            recordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("体育投注详情");
        listView = (XListView) findViewById(R.id.xlistview);
        empty = (EmptyListView) findViewById(R.id.empty);
    }

    private String getMatchInfo(SBSportOrder detail){
        if (detail == null) {
            return "";
        }
        if (detail.getMix() == 1) {

            StringBuilder sb = new StringBuilder();
            sb.append(detail.getBetOddsTypeName()).append("--");
            if (!Utils.isEmptyString(detail.getBetTeamName())) {
                if (detail.getBetTeamName().equalsIgnoreCase("小")) {
                    sb.append("小");
                } else if (detail.getBetTeamName().equalsIgnoreCase("大")) {
                    sb.append("大");
                }else if (detail.getBetTeamName().equalsIgnoreCase("客队")) {
                    sb.append((!Utils.isEmptyString(detail.getAwayName()) ? detail.getAwayName() : "")).append(" ");
                }else{
                    sb.append((!Utils.isEmptyString(detail.getHomeName()) ? detail.getHomeName() : "")).append(" ");
                }
            }else{
                sb.append((!Utils.isEmptyString(detail.getHomeName()) ? detail.getHomeName() : "")).append(" ");
            }
            sb.append(((detail.getHdp() != null && detail.getHdp() != 0) ? detail.getHdp() : "") + "@" + detail.getBetOdds());

//            String project = (!Utils.isEmptyString(detail.getBetTeamName()) ? detail.getBetTeamName() : "") + " " + detail.getHdp() + "@" + detail.getBetOdds();
            String html  = detail.getLeagueName() +"<br/>" +
                    detail.getHomeName() + "&nbsp;&nbsp;" + "vs." + "&nbsp;&nbsp;" + detail.getAwayName() +
                    "<font color='red'>"+"["+detail.getBetTypeName()+"]"+ "</font>"+"<br/>"
                    + "<font color='red'>"
//                    + (detail.getBetTypeName().equalsIgnoreCase("让球") ? (detail.getHomeName() + "@" + detail.getBetOdds()): mainTeamAndOdd)
                    + sb.toString()
                    + "</font>";
            return html;
        }

        if (detail.getMix() == 2) {
            if (detail.getChildrens() != null) {
                StringBuilder sb = new StringBuilder();
                for (SBSportOrder order : detail.getChildrens()) {

                    StringBuilder infos = new StringBuilder();

                    infos.append(detail.getBetOddsTypeName()).append("--");
                    if (!Utils.isEmptyString(detail.getBetTeamName())) {
                        if (detail.getBetTeamName().equalsIgnoreCase("小")) {
                            infos.append("小");
                        } else if (detail.getBetTeamName().equalsIgnoreCase("大")) {
                            infos.append("大");
                        }else if (detail.getBetTeamName().equalsIgnoreCase("客队")) {
                            infos.append((!Utils.isEmptyString(detail.getAwayName()) ? detail.getAwayName() : "")).append(" ");
                        }else{
                            infos.append((!Utils.isEmptyString(detail.getHomeName()) ? detail.getHomeName() : "")).append(" ");
                        }
                    }else{
                        infos.append((!Utils.isEmptyString(detail.getHomeName()) ? detail.getHomeName() : "")).append(" ");
                    }
                    infos.append(((detail.getHdp() != null && detail.getHdp() != 0) ? detail.getHdp() : "") + "@" + detail.getBetOdds());
                    String html  = order.getLeagueName() +"<br/>" +
                            order.getHomeName() + "&nbsp;&nbsp;" + "vs." + "&nbsp;&nbsp;" + order.getAwayName() +
                            "<font color='red'>"+"["+order.getBetTypeName()+"]"+ "</font>"+"<br/>"
                            + "<font color='red'>"
                            + infos.toString()
                            + "</font>";
                    sb.append(html);
                    sb.append("<br/>");
                    sb.append("..................................................");
                    sb.append("<br/>");
                }
                return sb.toString();
            }
        }
        return "";
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
            if (!Utils.isEmptyString(item.getValue())) {
                sportValue.setText(Html.fromHtml(item.getValue(), null, null));
            }
        }
    }

    public static void createIntent(Context context,String order) {
        Intent intent = new Intent(context, SbsportOrderDetailActivity.class);
        intent.putExtra("order", order);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listDatas.clear();
    }


}
