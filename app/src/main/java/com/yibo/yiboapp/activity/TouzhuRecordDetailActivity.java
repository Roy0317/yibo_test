package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.BcLotteryOrder;
import com.yibo.yiboapp.entify.LotteryRecordDetailWraper;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * @author johnson
 * 投注记录详情页
 */
public class TouzhuRecordDetailActivity extends BaseActivity implements
        SessionResponse.Listener<CrazyResult<LotteryRecordDetailWraper>> {

    XListView listView;
    EmptyListView empty;
    DetailListAdapter recordAdapter;
    List<String> listDatas;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static final int TOUZHU_RECORD_DETAIL = 0x01;
    String orderId;
    String cpBianma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touzhu_record_detail);
        initView();
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        orderId = getIntent().getStringExtra("orderId");
        cpBianma = getIntent().getStringExtra("cpBianma");
        listDatas = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getRecordDetail(orderId, cpBianma);
                recordAdapter = new DetailListAdapter(TouzhuRecordDetailActivity.this, listDatas, R.layout.touzhou_detail_listitem);
                listView.setAdapter(recordAdapter);
            }
        }, 300);

    }

    /**
     * 获取投注记录
     *
     * @param lotCode 彩种编码
     * @param orderId 订单号
     */
    private void getRecordDetail(String orderId, String lotCode) {

        StringBuilder configUrl = new StringBuilder();

        if (UsualMethod.isPeilvVersionMethod(this) || UsualMethod.isSixMark(this, lotCode)) {
            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_RECORD_DETAILV2_URL);
        } else {
            configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.LOTTERY_RECORD_DETAIL_URL);
        }
        configUrl.append("?orderId=").append(orderId).append("&");
        configUrl.append("lotCode=").append(lotCode);

        CrazyRequest<CrazyResult<LotteryRecordDetailWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(TOUZHU_RECORD_DETAIL)
                .headers(Urls.getHeader(this))
                .cachePeroid(5 * 60 * 1000)
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_detail_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<LotteryRecordDetailWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.touzhu_order_detail));
        listView = (XListView) findViewById(R.id.xlistview);
        empty = (EmptyListView) findViewById(R.id.empty);
        empty.setListener(new EmptyListView.EmptyListviewListener() {
            @Override
            public void onEmptyListviewClick() {
                getRecordDetail(orderId, cpBianma);
            }
        });
    }

    public static void createIntent(Context context, String orderId, String cpBianma) {
        Intent intent = new Intent(context, TouzhuRecordDetailActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("cpBianma", cpBianma);
        context.startActivity(intent);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<LotteryRecordDetailWraper>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == TOUZHU_RECORD_DETAIL) {
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
            CrazyResult<LotteryRecordDetailWraper> result = response.result;
            if (result == null) {
                showToast(R.string.get_data_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.get_data_fail);
                return;
            }
            LotteryRecordDetailWraper reg = result.result;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.get_data_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            listDatas.clear();
            BcLotteryOrder content = reg.getContent();
            if (content != null) {
                listDatas.add("订单号：" + content.getOrderId());
                boolean isPeilvVersion = UsualMethod.isSixMark(getApplicationContext(), cpBianma) ? true : UsualMethod.isPeilvVersionMethod(this);
                float unitMoney = isPeilvVersion ?
                        content.getBuyMoney() / content.getBuyZhuShu() : 2.0f / content.getModel();
                listDatas.add("单注金额：" + decimalFormat.format(unitMoney));
                listDatas.add("下注时间：" + Utils.formatBeijingTime(content.getCreateTime()) + " (北京时间)");
                listDatas.add("投注注数：" + content.getBuyZhuShu());
                listDatas.add("彩种：" + content.getLotName());
                listDatas.add("倍数：" + content.getMultiple());
                if (isPeilvVersion) {
                    listDatas.add("赔率：" + content.getPeilv() + "/" + content.getMinBonusOdds());
                } else {
                    listDatas.add("奖金：" + decimalFormat.format(content.getMinBonusOdds()));
                }
                listDatas.add("期号：" + content.getQiHao());
                listDatas.add("投注总额：" + decimalFormat.format(content.getBuyMoney()));
//                if (!Utils.isEmptyString(content.getGroupName())){
//                    listDatas.add("玩法："+content.getGroupName()+"-"+content.getPlayName());
//                }else{
                listDatas.add("玩法：" + content.getPlayName());
//                }
                listDatas.add("投注号码：" + content.getHaoMa());
                listDatas.add("中奖注数：" + (content.getWinZhuShu() != null ? content.getWinZhuShu().intValue() : 0));
                listDatas.add("状态：" + getStatusStr(content.getStatus()));
                listDatas.add("开奖号码：" + (!Utils.isEmptyString(content.getOpenHaoma()) ? content.getOpenHaoma() : ""));
                listDatas.add("中奖金额：" + decimalFormat.format(content.getWinMoney()));
                listDatas.add("盈亏：" + (content.getStatus() == Constant.WAIT_KAIJIAN_STATUS ? 0 :
                        decimalFormat.format(content.getYingKui())));
            }
            recordAdapter.notifyDataSetChanged();
        }
    }

    private String getStatusStr(int status) {
        if (status == Constant.WAIT_KAIJIAN_STATUS) {
            return "未开奖";
        } else if (status == Constant.ALREADY_WIN_STATUS) {
            return "已中奖";
        } else if (status == Constant.NOT_WIN_STATUS) {
            return "未中奖";
        } else if (status == Constant.CANCEL_ORDER_STATUS) {
            return "已撤单";
        } else if (status == Constant.ROLLBACK_SUCCESS_STATUS) {
            return "派奖回滚成功";
        } else if (status == Constant.ROLLBACK_FAIL_STATUS) {
            return "回滚异常";
        } else if (status == Constant.EXCEPTION_KAIJIAN_STATUS) {
            return "开奖异常";
        } else if (status == Constant.ACCOUNT_CHANGE_RECORD_STATUS) {
            return "和局";
        }
        return "";
    }


    public class DetailListAdapter extends LAdapter<String> {

        Context context;

        public DetailListAdapter(Context mContext, List<String> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final String item) {
            TextView cpname = holder.getView(R.id.name);

            if (item.equals("状态：已中奖")) {
                cpname.setTextColor(Color.RED);
            }
            cpname.setText(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listDatas.clear();
    }
}
