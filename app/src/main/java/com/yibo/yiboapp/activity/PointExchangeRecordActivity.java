package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.adapter.PointExchangeRecordAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.PointExchangeReocrdWraper;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.views.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 积分兑换记录
 */
public class PointExchangeRecordActivity extends BaseActivity {


    private TextView tvStartTime;//开始时间
    private TextView tvEndTime;//结束时间
    private TextView tvType;

    private LinearLayout llSearchLayout;
    private String startTime;//开始时间
    private String endTime;//结束时间
    private PopupWindow popupWindowState;

    private XListView xlistview;
    private EmptyListView empty;
    private int type = 0 ;

    private boolean isFirstSet = true;
    private CustomDatePicker customDatePicker1, customDatePicker2;
    private PointExchangeRecordAdapter adapter;

    public static void createIntent(Context context) {
        context.startActivity(new Intent(context, PointExchangeRecordActivity.class));
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_point_exchange_record);
        //开始时间
        tvStartTime = findViewById(R.id.tv_start_time);
        //结束时间
        tvEndTime = findViewById(R.id.tv_end_time);
        initView();
        initListener();
        //初始化时间选择器
        initDatePicker();
        //初始化当前时间
        initCurrentTime();
        //默认
        actionRecords();
    }


    @Override
    protected void initView() {
        super.initView();
        xlistview = findViewById(R.id.xlistview);
        tvType = findViewById(R.id.tv_type);
        empty=findViewById(R.id.empty);
        //设置listView的各种属性
        xlistview.setPullLoadEnable(false);
        xlistview.setPullRefreshEnable(false);
        xlistview.setDivider(getResources().getDrawable(R.color.driver_line_color));
        xlistview.setDividerHeight(3);
        xlistview.setEmptyView(empty);

        adapter = new PointExchangeRecordAdapter();
        adapter.setLayoutId(R.layout.item_point_exchange_record);
        adapter.setmContext(this);
        xlistview.setAdapter(adapter);

        //头部搜索布局
        llSearchLayout = findViewById(R.id.ll_search);
        //初始化标题
        tvMiddleTitle.setText(getString(R.string.point_exchange_record));
        //右面的筛选文字
        tvRightText.setText("筛选");
        tvRightText.setVisibility(View.VISIBLE);

        tvRightText.setOnClickListener(v -> {
            llSearchLayout.setVisibility(llSearchLayout.isShown() ? View.GONE : View.VISIBLE);
            if (llSearchLayout.isShown()){
                tvType.post(() -> {
                    popupWindowState = new PopupWindow(PointExchangeRecordActivity.this);
                    String[] arrays = getResources().getStringArray(R.array.point_exchange_record);
                    initPopupWindowContent(popupWindowState, arrays, tvType);
                });
            }
        });
    }

    private void initListener() {
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvType.setOnClickListener(this);
        //取消和确定的按钮
        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
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
                llSearchLayout.setVisibility(View.GONE);
                actionRecords();
                break;
            case R.id.tv_type:
                tvType.setTextColor(getResources().getColor(R.color.colorPrimary));
                popupWindowState.showAsDropDown(v);
                break;

        }
    }

    private void actionRecords() {
//        native/memberScoreRecord.do;//会员积分记录；参数：String account, String begin, String end, Long type
        ApiParams params = new ApiParams();
        params.put("account", YiboPreference.instance(this).getUsername());
        params.put("begin", startTime);
        params.put("end", endTime);
        params.put("type", type);

        HttpUtil.get(this, Urls.GET_MEMBER_SCORE_RECORD, params, true, new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()){
                    PointExchangeReocrdWraper wraper = new Gson().fromJson(result.getContent(),PointExchangeReocrdWraper.class);
                    adapter.setmDatas(wraper.getList());
                    adapter.notifyDataSetChanged();
                }


            }
        });
    }




    /**
     * 初始化时间选择器
     */
    private void initDatePicker() {
        String defaultStartTime = "2010-01-01";
        String defaultEndTime = "2050-12-31";
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvStartTime.setText(time.split(" ")[0]);
                startTime = time.split(" ")[0];
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动
        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvEndTime.setText(time.split(" ")[0]);
                endTime = time.split(" ")[0];
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(false); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动

    }


    /**
     * 初始化当前时间
     */
    private void initCurrentTime() {
        Date date = new Date();
        //开始时间默认为当天的零时零分零秒
        startTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date) ;
        //结束时间默认为当天的23:59:59
        endTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date);

        tvStartTime.setText(startTime);
        tvEndTime.setText(endTime);


    }


    /**
     * 初始化popupWindow内容
     *
     * @param popupWindow
     * @param arrays
     * @param tv
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

//        showToast("listItemHeight：" + listItemHeight + ",width:" + tv.getWidth() + ",arrays的长度:" + arrays.length);

        //判断listView的总高度如果大于tv.getWidth(),则设置高度为tv.getWidth()
        if (arrays.length * listItemHeight >= tv.getWidth()) {
            popupWindow.setHeight(tv.getWidth());
        }

        //设置内容
        popupWindow.setContentView(listView);


        popupWindow.setOnDismissListener(() -> tvType.setTextColor(getResources().getColor(R.color.system_default_color)));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String item = arrays[position];
            tvType.setText(item);
            adapter.setSelectPosition(position);
            popupWindow.dismiss();
            type= position;
        });


    }


    public class MyBaseAdapter extends BaseAdapter {

        private String[] arrays;

        public MyBaseAdapter(String[] arrays) {
            this.arrays = arrays;
        }

        private int selectPosition = 0;

        public void setSelectPosition(int selectPosition) {
            this.selectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return arrays.length;
        }

        @Override
        public Object getItem(int position) {
            return arrays[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(PointExchangeRecordActivity.this, R.layout.adapter_item_popwindow, null);
                holder.textView = convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            String newText = arrays[position];
            if (newText.contains(",")) {
                newText = newText.split(",")[0];
            }

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

}
