package com.yibo.yiboapp.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.Event.ActiveDataEvent;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.RecordAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.ActivesResultWraper;
import com.yibo.yiboapp.fragment.fragment.BaseFragment;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

public class ActivePageFragment extends BaseFragment implements SessionResponse.Listener
        <CrazyResult<Object>> {

    public static final int RECORD_REQUEST = 0x01;
    //    public static final int SETREAD_REQUEST = 0x02;
    XListView recordList;
    EmptyListView empty;
    RecordAdapter recordAdapter;
    List<ActivesResultWraper.ContentBean> listDatas;
    Context context;

    public static ActivePageFragment newInstance() {
        ActivePageFragment fragment = new ActivePageFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_active_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        context = getActivity();
        initView();
        listDatas = new ArrayList<>();
        recordAdapter = new RecordAdapter(context, listDatas, R.layout.active_list_item);
        recordList.setAdapter(recordAdapter);

    }


    protected void initView() {
        recordList = getView().findViewById(R.id.xlistview);
        recordList.setPullLoadEnable(false);
        recordList.setPullRefreshEnable(true);
        recordList.setDivider(null);
        recordList.setXListViewListener(new ListviewListener());
        empty = getView().findViewById(R.id.empty);
        empty.setListener(emptyListviewListener);
    }

    EmptyListView.EmptyListviewListener emptyListviewListener = () -> getRecords(true);

    private final class ListviewListener implements XListView.IXListViewListener {
        public void onRefresh() {
            getRecords(false);
        }

        public void onLoadMore() {
            getRecords(false);
        }
    }

    private void getRecords(boolean showDialog) {
        if (getActivity() != null && getActivity().isFinishing()) {
            return;
        }

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ACQUIRE_ACTIVES_URL_V2);
        CrazyRequest request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(RECORD_REQUEST)
                .headers(Urls.getHeader(context))
                .refreshAfterCacheHit(true)
                .shouldCache(true).placeholderText(getString(R.string.get_recording))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .listener(this)
                .convertFactory(GsonConverterFactory.create(new TypeToken<ActivesResultWraper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, request);
    }


    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (getActivity() != null && getActivity().isFinishing()) {
            return;
        }
        int action = response.action;
        if (action == RECORD_REQUEST) {
            if (recordList.isRefreshing()) {
                recordList.stopRefresh();
            } else if (recordList.isPullLoading()) {
                recordList.stopLoadMore();
            }
            empty.setVisibility(View.VISIBLE);
            recordList.setEmptyView(empty);
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.get_record_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.get_record_fail);
                return;
            }
            Object regResult = result.result;
            ActivesResultWraper reg = (ActivesResultWraper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.get_record_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(context);
                }
                return;
            }
            YiboPreference.instance(context).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                List<ActivesResultWraper.ContentBean> list = new ArrayList<>();
                handleListResult(list, reg.getContent(), response.url, true);
                if (currentIndex != 0) {
                    listDatas.clear();
                    updateListData(list);
                } else {
                    listDatas.clear();
                    listDatas.addAll(list);
                }
                recordAdapter.notifyDataSetChanged();
            }
        }
    }

    private int currentIndex;
    private List<ActivesResultWraper.ContentBean> allList = new ArrayList<>();

    @Subscribe
    public void getOnEventListener(ActiveDataEvent activeDataEvent) {
        currentIndex = activeDataEvent.getIndex();
        recordList.setEmptyView(empty);
        if (allList.isEmpty()) {
            allList = new Gson().fromJson(activeDataEvent.getData(),
                    new TypeToken<List<ActivesResultWraper.ContentBean>>() {
                    }.getType());
        }

        if (listDatas != null) {
            listDatas.clear();
        }
        switch (activeDataEvent.getTag()) {
            case "updateIndex":
                //刷新列表
                if (currentIndex > 0) {
                    updateListData(allList);
                    recordAdapter.notifyDataSetChanged();
                    break;
                }
            case "initData":
                //刷新数据
                listDatas.addAll(allList);
                recordAdapter.notifyDataSetChanged();
                break;
            case "initFailed":
                break;
        }

    }

    private void updateListData(List<ActivesResultWraper.ContentBean> list) {
        if (list != null && list.size() > 0) {
            ActivesResultWraper.ContentBean bean = list.get(0);
            for (ActivesResultWraper.ContentBean contentBean : list) {
                if (contentBean.getTypeId() == bean.getTypeList().get(currentIndex - 1).getId()) {
                    listDatas.add(contentBean);
                }
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
