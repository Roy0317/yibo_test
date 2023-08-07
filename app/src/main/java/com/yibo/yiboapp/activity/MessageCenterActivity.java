package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.MessageData;
import com.yibo.yiboapp.entify.MessageResultWrapper;
import com.yibo.yiboapp.entify.MessageStatusWrapper;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.CustomDialogManager;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;


public class MessageCenterActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    public static final String TAG = MessageCenterActivity.class.getSimpleName();
    private XListView listView;
    private EmptyListView empty;
    List<MessageData> datas;
    int pageIndex = 1;
    int pageSize = 20;
    ListViewAdapter adapter;

    public static final int UNREAD_STATUS = 1;
    public static final int READ_STATUS = 2;

    public static final int ACQUIRE_MESSAGE_REQUEST = 0x01;
    public static final int SET_READ_REQUEST = 0x02;
    public static final int DELETE_MESSAGE_REQUEST = 0x03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        initView();
        datas = new ArrayList<>();
        adapter = new ListViewAdapter(this, datas, R.layout.mesage_center_listitem);
        listView.setAdapter(adapter);
        getRecords(pageIndex, pageSize, true);
    }

    /**
     * 获取站内信记录
     *
     * @param page     页码
     * @param pageSize 每页条数
     */
    private void getRecords(int page, int pageSize, boolean showDialog) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.MESSAGE_LIST_URL);
        configUrl.append("?pageNumber=").append(page).append("&");
        configUrl.append("pageSize=").append(pageSize).append("&");
        configUrl.append("status=").append(0);

        CrazyRequest<CrazyResult<MessageResultWrapper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(ACQUIRE_MESSAGE_REQUEST)
                .headers(Urls.getHeader(this))
                .refreshAfterCacheHit(false)
                .shouldCache(false).placeholderText(getString(R.string.acquire_message_going))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MessageResultWrapper>() {
                }.getType()))
                .loadMethod(showDialog ? CrazyRequest.LOAD_METHOD.LOADING.ordinal() :
                        CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
        if (!showDialog) {
            startProgress();
        }
    }

    /**
     * 置已读
     */
    private void syncRead(long id) {

        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SET_READ_URL);
        configUrl.append("?id=").append(id);

        CrazyRequest<CrazyResult<MessageStatusWrapper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(SET_READ_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MessageStatusWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
        startProgress();
    }

    public static void createIntent(Context context) {
        Intent intent = new Intent(context, MessageCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.message_center_string));
        listView = (XListView) findViewById(R.id.xlistview);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setDivider(getResources().getDrawable(R.color.driver_line_color));
        listView.setXListViewListener(new ListviewListener());
        listView.setDividerHeight(3);
        listView.setVisibility(View.VISIBLE);
        empty = (EmptyListView) findViewById(R.id.empty);
        listView.setEmptyView(empty);
        empty.setListener(emptyListviewListener);
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == ACQUIRE_MESSAGE_REQUEST) {
            Log.e(TAG, "onResponse:");
            if (listView.isRefreshing()) {
                listView.stopRefresh();
            } else if (listView.isPullLoading()) {
                listView.stopLoadMore();
            }
            empty.setVisibility(View.VISIBLE);
            listView.setEmptyView(empty);
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
            MessageResultWrapper reg = (MessageResultWrapper) regResult;
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
            if (reg.getContent() == null) {
                return;
            }
            if (pageIndex == 1) {
                datas.clear();
            }
            datas = handleListResult(datas, reg.getContent().getDatas(), response.url, pageIndex == 1);
            adapter.notifyDataSetChanged();
            listView.setPullLoadEnable(reg.getContent().getTotalCount() <= datas.size() ? false : true);
            if (response.pickType != CrazyResponse.CACHE_REQUEST) {
                pageIndex++;
            }
        } else if (action == SET_READ_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.setread_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.setread_fail);
                return;
            }
            Object regResult = result.result;
            MessageStatusWrapper reg = (MessageStatusWrapper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.setread_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
//            getRecords(pageIndex,pageSize,false);
        } else if (action == DELETE_MESSAGE_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.delete_fail_string);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.delete_fail_string);
                return;
            }
            Object regResult = result.result;
            MessageStatusWrapper reg = (MessageStatusWrapper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() :
                        getString(R.string.delete_fail_string));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (reg.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            showToast("删除成功");
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            pageIndex = 1;
            pageSize = 20;
            getRecords(pageIndex, pageSize, false);
        }
    }

    private void showFuncList(final MessageData data) {
        String[] stringItems = new String[]{"查看", "删除"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.title("选择操作");
        dialog.isTitleShow(true).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (position == 0) {
                    showContentDialog(data.getTitle(), data.getMessage());
                } else if (position == 1) {
                    actionDeleteMessage(data.getUserMessageId());
                }
            }
        });
    }

    private void actionDeleteMessage(long id) {
        StringBuilder configUrl = new StringBuilder();
        configUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.DELETE_MESSAGE_URL);
        configUrl.append("?id=").append(id);
        CrazyRequest<CrazyResult<MessageStatusWrapper>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(DELETE_MESSAGE_REQUEST)
                .headers(Urls.getHeader(this))
                .shouldCache(false)
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MessageStatusWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
        startProgress();
    }


    private void showContentDialog(String title, String content) {
        final CustomConfirmDialog ccd = new CustomConfirmDialog(this);
        ccd.setBtnNums(1);
        ccd.setTitle(title);
        ccd.setContent(content);
        ccd.setMiddleBtnText("确定");
        if (content.contains("</") || content.contains("<img")) {
            ccd.setHtmlContent(true);
        } else {
            ccd.setHtmlContent(false);
        }
//        ccd.setToastShow(true);
        ccd.setBaseUrl(Urls.BASE_URL);
        ccd.setMiddleBtnClickListener(new OnBtnClickL() {
            public void onBtnClick() {
                ccd.dismiss();
            }
        });
        CustomDialogManager dialogManager = (CustomDialogManager) ccd;
        dialogManager.createDialog();
    }


    /**
     * 列表下拉，上拉监听器
     *
     * @author johnson
     */
    private final class ListviewListener implements XListView.IXListViewListener {

        public void onRefresh() {
            pageIndex = 1;
            getRecords(pageIndex, pageSize, false);
        }

        public void onLoadMore() {
            getRecords(pageIndex, pageSize, false);
        }
    }

    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override
        public void onEmptyListviewClick() {
            pageIndex = 1;
            getRecords(pageIndex, pageSize, false);
        }
    };

    private final class ListViewAdapter extends LAdapter<MessageData> {

        public ListViewAdapter(Context mContext, List<MessageData> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final MessageData item) {

            RelativeLayout itemLayout = holder.getView(R.id.item);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFuncList(item);
                    if (item.getStatus() != READ_STATUS) {
                        syncRead(item.getId());
                        MessageData newData = datas.get(position);
                        newData.setStatus(READ_STATUS);
                        datas.set(position, newData);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            TextView mesage = holder.getView(R.id.message);
            TextView content = holder.getView(R.id.content);
            TextView time = holder.getView(R.id.time);
            TextView status = holder.getView(R.id.status);
            if (item.getStatus() == UNREAD_STATUS || item.getStatus() == 0) {
                status.setText(getString(R.string.unread_status_str));
                status.setTextColor(getResources().getColor(R.color.colorPrimary));
                mesage.getPaint().setFakeBoldText(true);
                content.getPaint().setFakeBoldText(true);
            } else {
                status.setText(getString(R.string.read_status_str));
                status.setTextColor(getResources().getColor(R.color.grey));
                mesage.getPaint().setFakeBoldText(false);
                content.getPaint().setFakeBoldText(false);
            }
            content.setText(Html.fromHtml(item.getMessage()));
            mesage.setText(!Utils.isEmptyString(item.getTitle()) ? item.getTitle() : "暂无消息标题");
            time.setText(Utils.formatTime(item.getCreateTime()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        datas.clear();
    }
}
