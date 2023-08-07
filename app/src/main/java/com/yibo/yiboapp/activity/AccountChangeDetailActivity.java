package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.AccountRecord;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author johnson
 * 用户帐变详情页
 */
public class AccountChangeDetailActivity extends BaseActivity {

    XListView listView;
    EmptyListView empty;
    DetailListAdapter recordAdapter;
    List<String> listDatas;
    DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_change_detail);
        initView();

        decimalFormat = new DecimalFormat("0.00");
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listDatas = new ArrayList<String>();

        String detailJson = getIntent().getStringExtra("data");
        if (!Utils.isEmptyString(detailJson)) {
            AccountRecord record = new Gson().fromJson(detailJson, AccountRecord.class);
            if (record != null) {
                listDatas.add("订单号：" + record.getOrderno());
                listDatas.add("会员名：" + record.getAccount());
                listDatas.add("变动前金额：" + decimalFormat.format(record.getMoneyBefore()));
                listDatas.add("变动后金额：" + decimalFormat.format(record.getMoneyAfter()));
                BigDecimal big = BigDecimal.valueOf(record.getMoneyAfter()).subtract(BigDecimal.valueOf(record.getMoneyBefore()));
                listDatas.add("变动金额：" + decimalFormat.format(big.doubleValue()));
                listDatas.add("交易类型：" + UsualMethod.convertAccountChangeTypeToString(record.getType()));
                if(record.getType() == 4){
                    listDatas.add("提款手续费："+ record.getFee());
                }
                listDatas.add("日期：" + record.getTimeStr());
                listDatas.add("变动情况：" + record.getRemark());
            }
        }
        if (listDatas.isEmpty()) {
            listView.setEmptyView(empty);
            return;
        }
        recordAdapter = new DetailListAdapter(this, listDatas, R.layout.touzhou_detail_listitem);
        listView.setAdapter(recordAdapter);

    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.account_change_detail));
        listView = (XListView) findViewById(R.id.xlistview);
        empty = (EmptyListView) findViewById(R.id.empty);
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
            cpname.setText(item);
        }
    }

    public static void createIntent(Context context, String detail) {
        Intent intent = new Intent(context, AccountChangeDetailActivity.class);
        intent.putExtra("data", detail);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listDatas.clear();
    }
}
