package com.yibo.yiboapp.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.simon.widget.ToastUtil;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.CouponRecordAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.entify.CouponBean;
import com.yibo.yiboapp.interfaces.OnCouponDeleteListener;
import com.yibo.yiboapp.interfaces.OnCouponUseListener;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 代金券页面
 * craate
 */
public class CouponActivity extends BaseActivity implements OnCouponDeleteListener, OnCouponUseListener {

    private static final String TAG = "CouponActivity";

    CouponRecordAdapter adapter;
    List<CouponBean.ListBean> data1;
    List<CouponBean.ListBean> data2;
    List<CouponBean.ListBean> data3;
    int currentIndex = 0;

    LinearLayout ll_content_coupon;
    Button btnNoUse;
    Button btnAlreadyUse;
    Button btnOutOfDate;
    LinearLayout llTitle;
    RecyclerView rcv;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_coupon);
        super.initView();
        initView();
        initListener();
        getData();
    }

    protected void initView() {
        ll_content_coupon = findViewById(R.id.ll_content_coupon);
        btnNoUse = findViewById(R.id.btn_no_use);
        btnAlreadyUse = findViewById(R.id.btn_already_use);
        btnOutOfDate = findViewById(R.id.btn_out_of_date);
        llTitle = findViewById(R.id.ll_title);
        rcv = findViewById(R.id.rcv);

        tvMiddleTitle.setText("代金券中心");
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new CouponRecordAdapter(R.layout.item_coupon_record_list, CouponActivity.this);
        rcv.setAdapter(adapter);
        //空数据的view
        View view = View.inflate(this, R.layout.listview_empty_view, null);
        TextView clickRefresh = view.findViewById(R.id.click_refresh);
        clickRefresh.setOnClickListener(v -> getData());
        adapter.setEmptyView(view);
        adapter.setOnCouponDeleteListener(this);
        adapter.setOnCouponUseListener(this);
    }


    protected void initListener() {
        for (int i = 0; i < llTitle.getChildCount(); i++) {
            int finalI = i;
            llTitle.getChildAt(i).setOnClickListener(v -> {
                for (int i1 = 0; i1 < llTitle.getChildCount(); i1++) {
                    llTitle.getChildAt(i1).setBackgroundColor(getResources().getColor(R.color.white));
                    ((Button) llTitle.getChildAt(i1)).setTextColor(getResources().getColor(R.color.black));
                }
                llTitle.getChildAt(finalI).setBackgroundColor(getResources().getColor(R.color.light_black));
                ((Button) llTitle.getChildAt(finalI)).setTextColor(getResources().getColor(R.color.white));
                adapter.getData().clear();
                adapter.notifyDataSetChanged();
                currentIndex = finalI;
                if (finalI == 0) {
                    if (data1 != null && data1.size() != 0) {
                        adapter.addData(data1);
                    }
                } else if (finalI == 1) {
                    if (data2 != null && data2.size() != 0) {
                        adapter.addData(data2);
                    }
                } else if (finalI == 2) {
                    if (data3 != null && data3.size() != 0) {
                        adapter.addData(data3);
                    }
                }
            });
        }

    }


    private void getData() {
        HttpUtil.get(this, Urls.GET_COUPON_RECORD, null, true, result -> {
            if (result.isSuccess()) {
                CouponBean couponBean = new Gson().fromJson(result.getContent(), CouponBean.class);
                    List<CouponBean.ListBean> rows = couponBean.getList();
                    if (rows != null && rows.size() != 0) {
                        adapter.getData().clear();
                        filterData(rows);
                    }
                }

        });

    }

    private void filterData(List<CouponBean.ListBean> rows) {
        data1 = new ArrayList<>();
        data2 = new ArrayList<>();
        data3 = new ArrayList<>();
        for (CouponBean.ListBean datum : rows) {
            if (datum.getStatus() == 1) {
                data1.add(datum);
            } else if (datum.getStatus() == 2) {
                data2.add(datum);
            } else {
                data3.add(datum);
            }
        }

        if (currentIndex == 0) {
            adapter.addData(data1);
        } else if (currentIndex == 1) {
            adapter.addData(data2);
        } else if (currentIndex == 2) {
            adapter.addData(data3);
        } else {
            adapter.addData(data1);
        }
    }


    @Override
    public void onCouponDeleteListener(BaseViewHolder helper, CouponBean.ListBean item) {
        DialogUtil.alert(CouponActivity.this, "确定要删除吗？", (dialog, which) -> {
            ApiParams params = new ApiParams();
            params.put("id", item.getId());
            HttpUtil.get(CouponActivity.this, Urls.DELETE_COUPON, params, false, result -> {
                if (result.isSuccess()) {
                    ToastUtil.showToast(CouponActivity.this, "删除成功");
                    if (currentIndex == 0) {
                        data1.remove(item);
                    } else if (currentIndex == 1) {
                        data2.remove(item);
                    } else if (currentIndex == 2) {
                        data3.remove(item);
                    }
                    adapter.getData().remove(item);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    public void onCouponUseListener(BaseViewHolder helper, CouponBean.ListBean item) {
        DialogUtil.alert(CouponActivity.this, "确定要使用吗?", (dialog, which) -> {
            ApiParams params = new ApiParams();
            params.put("id", item.getId());
            params.put("cid", item.getCid());
            HttpUtil.get(CouponActivity.this, Urls.GET_COUPON_RECHARGE, params, false, result -> {
                if (result.isSuccess()) {
                    ToastUtil.showToast(CouponActivity.this, "使用成功");
                    getData();
                } else {
                    ToastUtil.showToast(CouponActivity.this, TextUtils.isEmpty(result.getMsg()) ? "使用失败" : result.getMsg());
                }
            });
        });
    }
}
