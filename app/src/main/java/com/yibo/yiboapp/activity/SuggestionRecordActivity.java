package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.SuggestionRecordResponse;
import com.yibo.yiboapp.data.SuggestionRecordResponse.*;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SuggestionRecordActivity extends BaseActivity {
    private static final int PAGE_SIZE = 20;

    public static Intent createIntent(Context context){
        Intent intent = new Intent(context, SuggestionRecordActivity.class);
        return intent;
    }

    private LinearLayout llSearchLayout;
    private XListView recordList;
    private EmptyListView emptyView;
    private TextView tvState;//状态
    private TextView tvStartTime;//开始时间
    private TextView tvEndTime;//结束时间
    private FrameLayout flContent;


    private String startTime;//开始时间
    private String endTime;//结束时间
    private PopupWindow popupWindowState;//状态的popupWindow
    private CustomDatePicker customDatePicker1, customDatePicker2;


    private String[] statusOptions;
    private boolean isFirstSet = true;
    private RecordAdapter recordAdapter;
    private int pageIndex = 1;
    private List<SuggestionRecord> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_record);
        initView();
        initParam();
    }

    @Override
    protected void initView() {
        super.initView();

        tvMiddleTitle.setText(getString(R.string.feedback_record));
        //右面的筛选文字
        tvRightText.setText("筛选");
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setOnClickListener(this);

        llSearchLayout = findViewById(R.id.ll_search);
        //状态
        tvState = findViewById(R.id.tv_state);
        //开始时间
        tvStartTime = findViewById(R.id.tv_start_time);
        //结束时间
        tvEndTime = findViewById(R.id.tv_end_time);
        //内容部分
        flContent = findViewById(R.id.frameContent);

        View mContentView = View.inflate(this, R.layout.touzhu_record_content, null);
        recordList = mContentView.findViewById(R.id.xlistview);
        recordList.setPullLoadEnable(false);
        recordList.setPullRefreshEnable(true);
        recordList.setDivider(getResources().getDrawable(R.color.driver_line_color));
        recordList.setDividerHeight(3);
        recordList.setXListViewListener(new ListViewListener());
        emptyView = mContentView.findViewById(R.id.empty_list);
        emptyView.setListener(emptyListviewListener);
        flContent.addView(mContentView);

        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvState.setOnClickListener(this);
    }

    private void initParam(){
        statusOptions = getResources().getStringArray(R.array.feedback_status);
        initDatePicker();
        initCurrentTime();
        fetchRecords(true);
    }

    /**
     * 初始化时间选择器
     */
    private void initDatePicker() {
        String defaultStartTime = "2010-01-01 00:00";
        String defaultEndTime = "2050-12-31 23:59";
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvStartTime.setText(time);
                startTime = time;
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动

        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvEndTime.setText(time);
                endTime = time;
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }

    /**
     * 初始化当前时间
     */
    private void initCurrentTime() {
        Date date = new Date();
        //开始时间默认为当天的零时零分零秒
        startTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date) + " 00:00";
        //结束时间默认为当天的23:59:59
        endTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date) + " 23:59";
        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);
    }

    private void fetchRecords(boolean showDialog){
        if (!Utils.isNetworkAvailable(this)) {
            showToast(getString(R.string.network_invalid));
            return;
        }

        String status = "";
        String statusString = tvState.getText().toString();
        if(statusString.equals(statusOptions[1])){
            status = "1";
        }else if(statusString.equals(statusOptions[2])){
            status = "2";
        }

        ApiParams params = new ApiParams();
        params.put("status", status);
        params.put("startTime", startTime + ":00");
        params.put("endTime", endTime + ":00");
        params.put("page", pageIndex);
        params.put("rows", PAGE_SIZE);

        HttpUtil.postForm(this, Urls.FEEDBACK_RECORDS, params, showDialog, "", result -> {
            if (isFinishing()) {
                return;
            }

            if (recordList.isRefreshing()) {
                recordList.stopRefresh();
            } else if (recordList.isPullLoading()) {
                recordList.stopLoadMore();
            }

            if(recordAdapter == null){
                records = new ArrayList<>();
                recordAdapter = new RecordAdapter(this, records, R.layout.item_feedback_record);
                recordList.setAdapter(recordAdapter);
            }

            try{
                String content = result.getContent();
                if (content == null || content.length() == 0) {
                    showToast(getString(R.string.request_fail));
                    if(records.isEmpty()){
                        emptyView.setVisibility(View.VISIBLE);
                        recordList.setEmptyView(emptyView);
                    }
                }else {
                    SuggestionRecordResponse response = new Gson().fromJson(content, SuggestionRecordResponse.class);
                    if(response.getTotalPageCount() > pageIndex){
                        pageIndex++;
                        recordList.setPullLoadEnable(true);
                    }else {
                        recordList.setPullLoadEnable(false);
                    }

                    records.addAll(response.getRecords());
                    if(records.isEmpty()){
                        emptyView.setVisibility(View.VISIBLE);
                        recordList.setEmptyView(emptyView);
                    }else {
                        recordAdapter.notifyDataSetChanged();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                showToast(getString(R.string.request_fail));
                if(records.isEmpty()){
                    emptyView.setVisibility(View.VISIBLE);
                    recordList.setEmptyView(emptyView);
                }
            }
        });
    }

    private void initLinearLayoutState() {
        popupWindowState = new PopupWindow(this);
        llSearchLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llSearchLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initPopupWindowContent(popupWindowState, statusOptions, tvState);
            }
        });
    }

    /**
     * 初始化popupWindow内容
     */
    private void initPopupWindowContent(final PopupWindow popupWindow, final String[] arrays, TextView tv) {
        popupWindow.setWidth(tv.getWidth());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);//必须写,不写后果自负
        ListView listView = new ListView(this);
        listView.setDividerHeight(2);
        listView.setBackgroundColor(Color.WHITE);
        final MyBaseAdapter adapter = new MyBaseAdapter(arrays);
        listView.setAdapter(adapter);
        adapter.setSelectPosition(0);

        //获取listView的每一个item的高度
        View listItem = listView.getAdapter().getView(0, null, listView);
        listItem.measure(0, 0);
        int listItemHeight = listItem.getMeasuredHeight();
//        showToast("listItemHeight = " + listItemHeight + ", width = " + tv.getWidth() + ", arrays的长度 = " + arrays.length);
        //判断listView的总高度如果大于tv.getWidth(),则设置高度为tv.getWidth()
        if (arrays.length * listItemHeight >= tv.getWidth()) {
            popupWindow.setHeight(tv.getWidth());
        }

        //设置内容
        popupWindow.setContentView(listView);
        popupWindow.setOnDismissListener(() -> {
            tvState.setTextColor(getResources().getColor(R.color.system_default_color));
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String item = arrays[position];
            tvState.setText(item);
            adapter.setSelectPosition(position);
            popupWindow.dismiss();
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_text:
                llSearchLayout.setVisibility(llSearchLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                //设置显示或隐藏项
                if (isFirstSet && llSearchLayout.getVisibility() == View.VISIBLE) {
                    initLinearLayoutState();
                    isFirstSet = false;
                }
                break;
            case R.id.tv_state:
                tvState.setTextColor(getResources().getColor(R.color.colorPrimary));
                popupWindowState.showAsDropDown(v);
                break;
            case R.id.tv_start_time:
                customDatePicker1.show(startTime);
                break;
            case R.id.tv_end_time:
                customDatePicker2.show(endTime);
                break;
            case R.id.btn_cancel:
                llSearchLayout.setVisibility(View.GONE);
                break;
            case R.id.btn_confirm:
                //访问接口
                if (!Utils.judgeTime(tvStartTime.getText().toString(), tvEndTime.getText().toString())) {
                    showToast("开始时间不能大于结束时间");
                    return;
                }

                pageIndex = 1;
                records.clear();
                fetchRecords(true);
                break;
        }
    }

    /**
     * 狀態列表adapter
     */
    public class MyBaseAdapter extends BaseAdapter {
        private String[] arrays;
        private int selectPosition = 0;

        public MyBaseAdapter(String[] arrays) {
            this.arrays = arrays;
        }

        public void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() { return arrays.length; }

        @Override
        public Object getItem(int position) { return arrays[position]; }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyBaseAdapter.Holder holder;
            if (convertView == null) {
                holder = new MyBaseAdapter.Holder();
                convertView = View.inflate(SuggestionRecordActivity.this,
                        R.layout.adapter_item_popwindow, null);
                holder.textView = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (MyBaseAdapter.Holder) convertView.getTag();
            }
            String newText = arrays[position];
            holder.textView.setText(newText);

            if (selectPosition == position) {
                holder.textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                holder.textView.setBackgroundResource(R.color.check_bg);
            } else {
                holder.textView.setTextColor(getResources().getColor(R.color.grey));
                holder.textView.setBackgroundResource(R.color.white);
            }

            return convertView;
        }

        class Holder {
            TextView textView;
        }
    }

    private EmptyListView.EmptyListviewListener emptyListviewListener = () -> {
        pageIndex = 1;
        records.clear();
        fetchRecords(true);
    };

    /**
     * 列表下拉，上拉监听器
     */
    private final class ListViewListener implements XListView.IXListViewListener {

        ListViewListener() { }

        public void onRefresh() {
            pageIndex = 1;
            records.clear();
            fetchRecords(false);
        }

        public void onLoadMore() { fetchRecords(false); }
    }

    private static class RecordAdapter extends LAdapter<SuggestionRecord> {

        private String suggestion;
        private String complaint;
        private String statusNotReplied;
        private String statusReplied;

        public RecordAdapter(Context context, List<SuggestionRecord> mDatas, int layoutId) {
            super(context, mDatas, layoutId);
            suggestion = context.getString(R.string.suggestion_title);
            complaint = context.getString(R.string.complaint_title);
            statusNotReplied = context.getString(R.string.feedback_record_not_replied);
            statusReplied = context.getString(R.string.feedback_record_replied);
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final SuggestionRecord record) {
            TextView textType = holder.getView(R.id.textType);
            TextView textStatus = holder.getView(R.id.textStatus);
            TextView textDatetime = holder.getView(R.id.textDatetime);
            TextView textDescription = holder.getView(R.id.textDescription);
            TextView textReplyDatetime = holder.getView(R.id.textReplyDatetime);

            textType.setText(record.getSendType() == 1 ? suggestion : complaint);
            textStatus.setText(record.getStatus() == 1 ? statusNotReplied : statusReplied);
            textDatetime.setText(record.getCreateTime());
            textDescription.setText(record.getContent());
            textReplyDatetime.setText(record.getFinalTime());
        }
    }
}