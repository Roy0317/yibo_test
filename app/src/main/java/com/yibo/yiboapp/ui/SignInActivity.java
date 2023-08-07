package com.yibo.yiboapp.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.ActiveDetailActivity;
import com.yibo.yiboapp.activity.BaseActivity;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.Meminfo;
import com.yibo.yiboapp.entify.SignInWrapper;
import com.yibo.yiboapp.network.ApiParams;
import com.yibo.yiboapp.network.HttpCallBack;
import com.yibo.yiboapp.network.HttpUtil;
import com.yibo.yiboapp.network.NetworkResult;
import com.yibo.yiboapp.utils.OnResultListener;
import com.yibo.yiboapp.utils.Utils;
import com.yibo.yiboapp.views.DialogSignInRecord;
import com.yibo.yiboapp.views.calendarview.listener.OnPagerChangeListener;
import com.yibo.yiboapp.views.calendarview.utils.CalendarUtil;
import com.yibo.yiboapp.views.calendarview.weiget.CalendarView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 每日签到页面
 */
public class SignInActivity extends BaseActivity {

    private TextView txtSignInRecord;
    private TextView txtMonth;
    private TextView signInDay;
    private TextView signInBtn; //点击签到按钮
    private ImageView imageBg;
    private CalendarView calendarView;
    private List<SignInBean> signInBeans = new ArrayList<>();

    private DialogSignInRecord dialogSignInRecord;
    private Meminfo meminfo;

    private String todayDate;
    private boolean isSignInToday = false;  //是否已经签到
    private int[] cDate = CalendarUtil.getCurrentDate();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText("签到");
        tvRightText.setText("签到规则");
        tvRightText.setVisibility(View.VISIBLE);
        txtSignInRecord = findViewById(R.id.txtSignInRecord);
        txtMonth = findViewById(R.id.txtMonth);
        signInDay = findViewById(R.id.signInDay);
        signInBtn = findViewById(R.id.signInBtn);
        imageBg = findViewById(R.id.imageBg);
        calendarView = findViewById(R.id.calendarView);

        ViewGroup.LayoutParams layoutParams = imageBg.getLayoutParams();
        layoutParams.height = Utils.getDensityHeight(this) / 5;
        imageBg.requestLayout();
        initListener();
        initData();
    }


    protected void initListener() {
        txtSignInRecord.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        findViewById(R.id.imageLeftMoth).setOnClickListener(this);
        findViewById(R.id.imageRightMoth).setOnClickListener(this);
        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                txtMonth.setText(date[0] + "年" + date[1] + "月");
            }
        });
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSignInRule();
            }
        });
    }


    protected void initData() {
        if (cDate[1] < 10) {
            if (cDate[2] < 10) {
                todayDate = cDate[0] + ".0" + cDate[1] + ".0" + cDate[2];
            } else {
                todayDate = cDate[0] + ".0" + cDate[1] + "." + cDate[2];
            }
        } else {
            if (cDate[2] < 10) {
                todayDate = cDate[0] + "." + cDate[1] + ".0" + cDate[2];
            } else {
                todayDate = cDate[0] + "." + cDate[1] + "." + cDate[2];
            }
        }

        txtMonth.setText(cDate[0] + "年" + cDate[1] + "月");
        String date = cDate[0] + "." + cDate[1];
        //只允许查看今年的
        calendarView.setStartEndDate(cDate[0] + "." + 1, cDate[0] + "." + 12)
                .setInitDate(date)
                .init();

        String signLogo = UsualMethod.getConfigFromJson(this).getPc_sign_logo();
        if (!TextUtils.isEmpty(signLogo)) {
//            ImageUtils.(this, imageBg, signLogo, R.drawable.sign_in_bg);
            Glide.with(this).asBitmap().load(signLogo.trim()).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Drawable drawable = new BitmapDrawable(resource);
                    imageBg.setBackground(drawable);
                }
            });
        }

        getSignInRecord(true);
        getUserInfo();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.txtSignInRecord: //签到记录
                showRecordDialog();
                break;
            case R.id.signInBtn: //点击签到
                if (!isSignInToday)
                    signIn();
                break;
            case R.id.imageLeftMoth:
                calendarView.lastMonth();
                break;
            case R.id.imageRightMoth:
                calendarView.nextMonth();
                break;
        }
    }


    /**
     * 签到成功刷新日历
     */
    private void refreshToday() {
        List<String> dates = new ArrayList<>();
        dates.add(cDate[0] + "." + cDate[1] + "." + cDate[2]);
        calendarView.setDefaultMultiDate(dates);
        getSignInRecord(false);
    }


    /**
     * 显示签到记录
     */
    private void showRecordDialog() {
        if (dialogSignInRecord == null)
            dialogSignInRecord = new DialogSignInRecord(this);
        dialogSignInRecord.setBeanList(signInBeans);
        dialogSignInRecord.setMeminfo(meminfo);
        getUserInfo();
        dialogSignInRecord.show();
    }


    @SuppressLint("CheckResult")
    private void setSignDayData(final List<SignInBean> signData) {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                List<String> dates = new ArrayList<>();
                for (SignInBean temp : signData) {
                    String dateStr = Utils.getDateFormat(temp.getSignDate(), "yyyy.MM.dd");
                    if (todayDate.equals(dateStr)) {
                        isSignInToday = true;
                    }
                    dates.add(dateStr);
                }
                emitter.onNext(dates);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        calendarView.setDefaultMultiDate(strings);
                        if (isSignInToday)
                            signInBtn.setBackgroundResource(R.drawable.selector_btn_cancel);
                        else
                            signInBtn.setBackgroundResource(R.drawable.selector_btn_confirm_fillet);
                    }
                });
    }

    private int pageSize = 30;//
    private int pageNumber = 1;

    /**
     * 获取签到记录数据
     */
    private void getSignInRecord(final boolean setDayData) {
        ApiParams params = new ApiParams();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", pageSize);
        HttpUtil.postForm(this, Urls.API_NATIVE_SIGN_RECORD, params, true, "正在获取签到记录...", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    SignInWrapper signInWrapper = new Gson().fromJson(result.getContent(), SignInWrapper.class);
                    if (signInWrapper != null && signInWrapper.getList() != null && signInWrapper.getList().size() > 0) {
                        if (pageNumber == 1) {
                            signInBeans.clear();
                        }
                        signInBeans.addAll(signInWrapper.getList());
//                        signInDay.setText(signInBeans.get(0).getSignDays());
                        if (setDayData) {
                            setSignDayData(signInWrapper.getList());
                        }

                        int curNum = Integer.parseInt(signInWrapper.getStart());
                        int total = Integer.parseInt(signInWrapper.getTotalCount());
                        if (total - curNum > pageSize) { //有下一页
                            pageNumber++;
                            getSignInRecord(true);
                        }
                    }
                } else {
                    showToast(result.getMsg());
                }
            }
        });
    }


    /**
     * 用户签到
     */
    private void signIn() {
        HttpUtil.postForm(getApplicationContext(), Urls.SIGN_URL, null, true, "正在签到...", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    refreshToday();
                    showToast("签到成功");
                    isSignInToday = true;
                    signInBtn.setBackgroundResource(R.drawable.selector_btn_cancel);
                } else {
                    if ("今日已签到".equals(result.getMsg()) && result.getCode() == 10000) {
                        refreshToday();
                        isSignInToday = true;
                        signInBtn.setBackgroundResource(R.drawable.selector_btn_cancel);
                    }
                    showToast(result.getMsg());
                }
            }
        });
    }


    /**
     * 获取签到规则
     */
    private void getSignInRule() {
        HttpUtil.postForm(getApplicationContext(), Urls.SIGN_URL_RULE, null, true, "获取签到规则", new HttpCallBack() {
            @Override
            public void receive(NetworkResult result) {
                if (result.isSuccess()) {
                    if (!TextUtils.isEmpty(result.getContent())) {
                        String html = result.getContent();
                        if (result.getContent().contains("font-size:")) {
                            int start = result.getContent().indexOf("font-size:");
                            int end = result.getContent().indexOf("px", start);
                            html = result.getContent().substring(0, start) + "font-size:38" + result.getContent().substring(end); //字体变大
                        }
                        if (result.getContent().contains("style=")) {
                            int start = result.getContent().indexOf("style=");
                            html = result.getContent().substring(0, start) + "style=\"font-size:38px;" + result.getContent().substring(start + 7);
                        }
                        ActiveDetailActivity.createIntent(SignInActivity.this, html, "签到规则", true, true);
                    } else {
                        showToast("签到规则为空");
                    }
                } else {
                    showToast("获取签到规则失败");
                }
            }
        });
    }


    private void getUserInfo() {
        UsualMethod.getUserInfo(this, false, "", false, new OnResultListener<Meminfo>() {
            @Override
            public void onResultListener(Meminfo info) {
                if (info != null) {
                    meminfo = info;
                    dialogSignInRecord.setUserInfo(meminfo);
                }
            }
        });
    }


}
