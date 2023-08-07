package com.yibo.yiboapp.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.BaseActivity;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.ActiveStationData;
import com.yibo.yiboapp.entify.ActivesResultWraper;
import com.yibo.yiboapp.entify.ActivesStationWraper;
import com.yibo.yiboapp.utils.LoadImageUtil;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.ActiviteSearchDialog;

import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

public class ActiveStationActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<ActivesStationWraper>> {

    RecyclerView recyclerView;
    StationAdapter adapter;
    List<ActiveStationData.Aactivebean> data;
    public static final int LIST_RESAULT = 0x01;

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, ActiveStationActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activtiy_activite_station);
        initView();
        initData();
    }


    protected void initView() {
        super.initView();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new StationAdapter(R.layout.item_active_station, data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActiveStationDetailActivity.createIntent(ActiveStationActivity.this, data.get(position).getId());
            }
        });
        tvMiddleTitle.setText("活动大厅");
        ivMoreMenu.setVisibility(View.VISIBLE);
        ivMoreMenu.setImageResource(R.drawable.icon_active_search);
        ivMoreMenu.setOnClickListener(this);

    }

    protected void initData() {
        getdata(true);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<ActivesStationWraper>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == LIST_RESAULT) {
            CrazyResult<ActivesStationWraper> result = response.result;
            if (result == null) {
                showToast(R.string.get_record_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.get_record_fail);
                return;
            }
            Object regResult = result.result;
            ActivesStationWraper reg = (ActivesStationWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.get_record_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());

            if (reg.getContent() != null) {
                data = reg.getContent().getActive();
                adapter.getData().clear();
                adapter.addData(data);

            }
        }

    }

    private void getdata(boolean showDialog) {
        if (this.isFinishing()) {
            return;
        }
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_ACTIVE_LOBBY_LIST);

        CrazyRequest<CrazyResult<ActivesResultWraper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(LIST_RESAULT)
                .headers(Urls.getHeader(this))
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<ActivesStationWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    private class StationAdapter extends BaseQuickAdapter<ActiveStationData.Aactivebean, BaseViewHolder> {


        public StationAdapter(int layoutResId, @Nullable List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ActiveStationData.Aactivebean item) {
            helper.setText(R.id.tv_title, item.getTitle());
            LoadImageUtil.loadPicImage(mContext, helper.getView(R.id.img), item.getImg());

        }


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_more_menu:
                new ActiviteSearchDialog(ActiveStationActivity.this, data).show();
                break;

        }
    }
}
