package com.yibo.yiboapp.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.BaseActivity;
import com.yibo.yiboapp.adapter.TeamViewListAdapter;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.TeamOverViewWrapWraper;
import com.yibo.yiboapp.entify.TeamViewBean;
import com.yibo.yiboapp.ui.EmptyListView;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.DateUtils;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.CustomDatePicker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * Author: Ray
 * created on 2018/10/16
 * description : 团队总蓝
 */
public class TeamOverViewListActivity extends BaseActivity implements
        SessionResponse.Listener<CrazyResult> {

    private static final String TAG = "TeamOverViewListActivity";
    private XListView xListView;
    private EmptyListView emptyListView;
    private LinearLayout llSearchFrame;//搜索部分布局
    private TextView tvStartTime;//开始时间
    private TextView tvEndTime;//结束时间
    private String now;//当前时间
    private EditText etUserName;
    private static final int GET_MEMBER_LIST_REQUEST = 0x01;

    private TextView tvTeamBalance; //团队余额
    private TextView tvTeamRecharge; //团队充值
    private TextView tvTeamReturnPoint; //团队返点
    private TextView tvTeamPeopleNum; //团队人数

    private CustomDatePicker customDatePicker1, customDatePicker2;

    private String startTime;//开始时间
    private String endTime;//结束时间


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_teamview_list);
        initView();
    }


    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("团队总览");
        tvRightText.setText("筛选");
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setOnClickListener(this);

        etUserName = (EditText) findViewById(R.id.et_user_name);
        llSearchFrame = (LinearLayout) findViewById(R.id.ll_search);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        xListView = (XListView) findViewById(R.id.xlistview);

        tvTeamBalance = findViewById(R.id.tv_team_balance);
        tvTeamRecharge = findViewById(R.id.tv_team_recharge);
        tvTeamReturnPoint = findViewById(R.id.tv_team_return_point);
        tvTeamPeopleNum = findViewById(R.id.tv_team_people_num);

        emptyListView = (EmptyListView) findViewById(R.id.item);
        emptyListView.setListener(emptyListviewListener);

        xListView.setDivider(getResources().getDrawable(R.color.gray));
        xListView.setDividerHeight(5);
        xListView.setPullLoadEnable(false);
        xListView.setPullRefreshEnable(false);


        findViewById(R.id.btn_confirm).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        now = sdf.format(new Date());

        initDatePicker();

        //进来就查询所有数据
        requestInterface();
    }

    EmptyListView.EmptyListviewListener emptyListviewListener = new EmptyListView.EmptyListviewListener() {
        @Override
        public void onEmptyListviewClick() {

            requestInterface();
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_cancel:
                llSearchFrame.setVisibility(View.GONE);
                break;
            case R.id.btn_confirm:
                llSearchFrame.setVisibility(View.GONE);
                requestInterface();
                break;
            case R.id.right_text:
                llSearchFrame.setVisibility(llSearchFrame.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.tv_end_time:
                customDatePicker2.show(endTime);
                break;
            case R.id.tv_start_time:
                customDatePicker1.show(startTime);
                break;
        }
    }

    private void requestInterface() {

        StringBuilder params = new StringBuilder();
        String userName = TextUtils.isEmpty(etUserName.getText().toString().trim()) ? ""
                : etUserName.getText().toString().trim();

        String sTime = DateUtils.encodeTime(startTime + ":00");
        String eTime = DateUtils.encodeTime(endTime.substring(0,10) + " 23:59:59");

        params.append("account").append("=").append(userName).append("&");
        params.append("endTime").append("=").append(eTime).append("&");
        params.append("startTime").append("=").append(sTime);

        Utils.LOG(TAG, "the add member params = " + params.toString());
        //Log.e(TAG, "requestInterface: ");

        //访问的url
        String urls = Urls.BASE_URL + Urls.PORT + Urls.TEAM_OVERVIEW_LIST + "?" + params;

        //构建http请求
        CrazyRequest<CrazyResult<TeamOverViewWrapWraper>> request = new AbstractCrazyRequest.Builder().
                url(urls)
                .seqnumber(GET_MEMBER_LIST_REQUEST)
                .listener(this)
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.search_teamover_ongoing))
                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())//Post方式
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<TeamOverViewWrapWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();

        RequestManager.getInstance().startRequest(this, request);
    }


    /**
     * 网络请求回调昂发
     *
     * @param response 返回的数据实体类型
     */
    @Override
    public void onResponse(SessionResponse<CrazyResult> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        switch (action) {
            case GET_MEMBER_LIST_REQUEST:
                CrazyResult result = response.result;
                if (result == null || !result.crazySuccess) {
                    showToast(getString(R.string.acquire_fail));
                    return;
                }
                TeamOverViewWrapWraper memberListBean = (TeamOverViewWrapWraper) result.result;


                if (!memberListBean.isSuccess()) {
                showToast(!Utils.isEmptyString(memberListBean.getMsg()) ? memberListBean.getMsg() :
                        getString(R.string.get_member_list_fail));
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (memberListBean.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }

                if (memberListBean.getContent() == null) {
                    showToast(getString(R.string.acquire_fail));
                    return;
                }

                List<TeamViewBean> list = memberListBean.getContent().getTeamAccount();
                if (memberListBean.getContent() != null) {

                    String username = YiboPreference.instance(this).getUsername();
                    YiboPreference.instance(this).setToken(memberListBean.getAccessToken());

                    tvTeamBalance.setText(memberListBean.getContent().getTeamMaps().getMoney()+ "");
                    tvTeamPeopleNum.setText(memberListBean.getContent().getTeamMaps().getAccountcount()+ "");

                    float recharge = 0;
                    float returnPoint = 0;

                    if (null == list || 0 == list.size()) {
                        tvTeamRecharge.setText("0");
                        tvTeamReturnPoint.setText("0");
                        xListView.setVisibility(View.GONE);
                        emptyListView.setVisibility(View.VISIBLE);
                        return;
                    }

                    List<TeamViewBean> memberListData = new ArrayList<>(list);

                    List<TeamViewBean> memberListData2 = new ArrayList<>(); //去除自己的返点记录

//                    for (int i = 0; i < memberListData.size(); i++) {
//                        if (!username.equals(memberListData.get(i).getAccount())) {
//                            recharge = recharge + memberListData.get(i).getDepositamount();//充值不包括自己
//                            memberListData2.add(memberListData.get(i));
//                        }
//                        returnPoint = returnPoint + memberListData.get(i).getProxyrebateamount(); //返点包括自己
//                    }

                    for (int i = 0; i < memberListData.size(); i++) {
                        if (username.equals(memberListData.get(i).getAccount())) { //只统计自己返点 不包括自己的充值
                            returnPoint = returnPoint + memberListData.get(i).getProxyrebateamount();
                        } else {
                            recharge = recharge + memberListData.get(i).getDepositamount();
                            memberListData2.add(memberListData.get(i));
                        }
                    }


                    tvTeamRecharge.setText(Utils.keepTwoDecimal(recharge));
                    tvTeamReturnPoint.setText(Utils.keepTwoDecimal(returnPoint ));

                    xListView.setVisibility(View.VISIBLE);
                    emptyListView.setVisibility(View.GONE);
                    xListView.setAdapter(new TeamViewListAdapter(this, memberListData2, R.layout.adapter_teamview_list));

                } else {
                    tvTeamRecharge.setText("0");
                    tvTeamReturnPoint.setText("0");
                    xListView.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
                }

                break;
        }
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
                tvStartTime.setText(time.substring(0,10));
                startTime = time;
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动


        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvEndTime.setText(time.substring(0, 10));
                endTime = time;
            }
        }, defaultStartTime, defaultEndTime, "yyyy-MM-dd HH:mm"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(false); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = new Date();
        //当前时间

        //开始时间默认为当天的零时零分零秒
        startTime = DateUtils.getPastDate(7) + " 00:00:00";
        //结束时间默认为当天的23:59:59
        endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date) + "";

        tvStartTime.setText(DateUtils.getPastDate(7));
        tvEndTime.setText(endTime.substring(0, 10));

    }


}

