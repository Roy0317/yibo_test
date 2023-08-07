package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.NoticeNewBean;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.dialog.CustomConfirmDialog;
import crazy_wrapper.Crazy.dialog.OnBtnClickL;

public class NoticesPageActivity extends BaseActivity {


    public static void createIntent(Context context, String notices) {
        Intent intent = new Intent(context, NoticesPageActivity.class);
        intent.putExtra("notices", notices);
        context.startActivity(intent);
    }

    public static final String TAG = NoticesPageActivity.class.getSimpleName();
    private XListView listView;
    private EmptyListView empty;
    ListViewAdapter adapter;
    List<NoticeNewBean.ContentBean> notices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices_page);
        initView();
        notices = new ArrayList<>();
        String str = getIntent().getStringExtra("notices");
        if (!Utils.isEmptyString(str)) {
            Type listType = new TypeToken<ArrayList<NoticeNewBean.ContentBean>>() {
            }.getType();
            notices = new Gson().fromJson(str, listType);
        }

        //如果是从通知栏点击进入的，则重新拉取网络数据
        if (notices == null || notices.size() == 0) {
            getNoticeData();
        } else {
            adapter = new ListViewAdapter(NoticesPageActivity.this, notices, R.layout.notice_item);
            listView.setAdapter(adapter);
        }

    }


    private void getNoticeData() {
        ApiParams apiParams = new ApiParams();
        apiParams.put("code", "13");
        HttpUtil.get(this, Urls.NOTICE_URL_V2, apiParams, true, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    //解析数组这样解析
                    notices = new Gson().fromJson(result.getContent(), new TypeToken<List<NoticeNewBean.ContentBean>>() {
                    }.getType());
                    YiboPreference.instance(NoticesPageActivity.this).setToken(result.getAccessToken());
                    adapter = new ListViewAdapter(NoticesPageActivity.this, notices, R.layout.notice_item);
                    listView.setAdapter(adapter);
                }
            }
        });
    }


    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.notice_message));
        listView = (XListView) findViewById(R.id.xlistview);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listView.setDivider(getResources().getDrawable(R.color.driver_line_color));
        listView.setDividerHeight(3);
        listView.setVisibility(View.VISIBLE);
        empty = (EmptyListView) findViewById(R.id.empty);
        listView.setEmptyView(empty);
    }


    private final class ListViewAdapter extends LAdapter<NoticeNewBean.ContentBean> {

        Context context;

        public ListViewAdapter(Context mContext, List<NoticeNewBean.ContentBean> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            this.context = mContext;
        }

        private void showNoticeDialog(String title, String content) {
            final CustomConfirmDialog ccd = new CustomConfirmDialog(context);
            ccd.setBtnNums(1);
            ccd.setTitle(title);
            ccd.setContent(content);
            ccd.setMiddleBtnText("确定");
            ccd.setHtmlContent(true);
            ccd.setBaseUrl(Urls.BASE_URL);
            ccd.setMiddleBtnClickListener(new OnBtnClickL() {
                public void onBtnClick() {
                    ccd.dismiss();
                }
            });
            ccd.createDialog();
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final NoticeNewBean.ContentBean item) {

            RelativeLayout itemLayout = holder.getView(R.id.item);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNoticeDialog("最新公告", item.getContent());
                }
            });
            TextView content = holder.getView(R.id.content);
            String html = "<html><head></head><body>" + item.getTitle() + "</body></html>";
            content.setText(Html.fromHtml(html, null, null));

        }
    }
}
