package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.activity.MainActivity;
import com.yibo.yiboapp.activity.QrcodeActivity;
import com.yibo.yiboapp.activity.QuotaConvertActivity;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.UserCenterActivity;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.MemInfoWraper;
import com.yibo.yiboapp.entify.MemberHeaderWraper;
import com.yibo.yiboapp.entify.Meminfo;
import com.yibo.yiboapp.entify.SysConfig;

import com.yibo.yiboapp.manager.BankingManager;
import com.yibo.yiboapp.manager.ManagerFactory;
import com.yibo.yiboapp.mvvm.banking.PickMoneyActivity;
import com.yibo.yiboapp.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * Created by johnson on 2018/3/31.
 */

public class MenuHeader extends FrameLayout implements View.OnClickListener,
        SessionResponse.Listener<CrazyResult<Object>> {

    ImageView bgImage;
    SimpleDraweeView header;
    ImageView level;
    TextView levelName;
    TextView accountTV;
    TextView tv_refresh;
    TextView tvYinCang;
    TextView signBtn;
    TextView balanceTV;
    TextView chargeBtn;
    TextView withdrawBtn;
    TextView convertBtn;
    RotateAnimation rotate;
    private LinearLayout ll_moneyinfo;

    public static final int ACCOUNT_REQUEST = 0x02;
    public static final int GET_HEADER = 0x03;
    String leftMoneyName;
    String accountName;
    boolean isShow = true;

    public MenuHeader(Context context) {
        super(context);
    }

    public MenuHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        bgImage = (ImageView) findViewById(R.id.header_bg);
        header = (SimpleDraweeView) findViewById(R.id.header);
        level = (ImageView) findViewById(R.id.level);
        levelName = (TextView) findViewById(R.id.level_name);
        accountTV = (TextView) findViewById(R.id.name);
        balanceTV = (TextView) findViewById(R.id.left_money);
        tv_refresh = (TextView) findViewById(R.id.tv_refresh);
        tvYinCang = (TextView) findViewById(R.id.tvYinCang);
        tvYinCang.setOnClickListener(this);
        chargeBtn = (TextView) findViewById(R.id.charge);
        withdrawBtn = (TextView) findViewById(R.id.tikuan);
        convertBtn = (TextView) findViewById(R.id.convert);
        signBtn = (TextView) findViewById(R.id.sign);
        ll_moneyinfo = (LinearLayout) findViewById(R.id.ll_moneyinfo);
        chargeBtn.setOnClickListener(this);
        withdrawBtn.setOnClickListener(this);
        convertBtn.setOnClickListener(this);
        signBtn.setOnClickListener(this);
        ll_moneyinfo.setOnClickListener(this);
        bgImage.setOnClickListener(this);
        //根据帐户类型是否显示充值提款按钮
        refreshiAnimation();
        hideUIByConfig();
        //根据后台配置的彩票logo图上更新头像图
        SysConfig sys = UsualMethod.getConfigFromJson(getContext());
        if (sys != null) {
            updateCircleHeader();
            //根据后台配置的头部背景图片地址设置背景
            String bgUrl = sys.getMember_center_bg_url();
            if (!Utils.isEmptyString(bgUrl)) {
                updateHeaderBG(bgImage, bgUrl);
            } else {
                bgImage.setBackground(getResources().getDrawable(R.drawable.member_page_header_bg));
            }
        }

        //获取一下帐号对应的头像
        UsualMethod.syncHeader(getContext(), GET_HEADER, false, this);
        syncHeaderWebDatas(getContext());
    }

    public void updateCircleHeader() {
        UsualMethod.LoadUserImage(getContext(), header);
    }


    private void updateHeaderBG(final ImageView bg, String imgUrl) {
        if (Utils.isEmptyString(imgUrl)) {
            return;
        }

        Glide.with(getContext())
                .asBitmap()
                .load(imgUrl.trim())
                .into(bg);
    }

    //获取一些主页显示的后台数据
    public void syncHeaderWebDatas(Context context) {
        StringBuilder accountUrls = new StringBuilder();
        accountUrls.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.MEMINFO_URL);
        CrazyRequest<CrazyResult<MemInfoWraper>> accountRequest = new AbstractCrazyRequest.Builder().
                url(accountUrls.toString())
                .seqnumber(ACCOUNT_REQUEST)
                .listener(this)
                .placeholderText("刷新中")
                .headers(Urls.getHeader(getContext()))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MemInfoWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(getContext(), accountRequest);
    }

    private void hideUIByConfig() {
        SysConfig sc = UsualMethod.getConfigFromJson(getContext());
        if (sc == null) {
            return;
        }
        //签到开关是否要开启
        if (!Utils.isEmptyString(sc.getOnoff_sign_in()) && sc.getOnoff_sign_in().equals("on")) {
            signBtn.setVisibility(View.VISIBLE);
        } else {
            signBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.charge:
                if (Utils.shiwanFromMobile(getContext())) {
                    Toast.makeText(getContext(), "操作权限不足，请联系客服", Toast.LENGTH_SHORT).show();
                    return;
                }
                String username = TextUtils.isEmpty(accountName) ? YiboPreference.instance(getContext()).getUsername() : accountName;
                Intent intentCharge = BankingManager.Companion.openChargePage(getContext(), username, leftMoneyName);
                getContext().startActivity(intentCharge);
                break;
            case R.id.tikuan:
                if (Utils.shiwanFromMobile(getContext())) {
                    Toast.makeText(getContext(), "操作权限不足，请联系客服", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = BankingManager.Companion.openWithdrawPage(getContext(), 0f);
                getContext().startActivity(intent);
                break;
            case R.id.tvYinCang:
                changeJinE();
                break;
            case R.id.convert:
                if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                    showToast("操作权限不足，请联系客服！");
                    return;
                }
                QuotaConvertActivity.createIntent(getContext());
                break;
            case R.id.qrcode:
                QrcodeActivity.createIntent(getContext());
                break;
            case R.id.header_bg:
                UserCenterActivity.createIntent(getContext());
                break;
            case R.id.sign:
                getContext().startActivity(new Intent(getContext(), SignInActivity.class));
            case R.id.ll_moneyinfo:
                tv_refresh.setAnimation(rotate);
                syncHeaderWebDatas(getContext());
                updateCircleHeader();
                break;
        }
    }

    private void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(getContext(), showText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if ((this.getContext() instanceof Activity && ((Activity) getContext()).isFinishing()) || response == null) {
            return;
        }
        int action = response.action;
        if (action == ACCOUNT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getContext().getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            MemInfoWraper reg = (MemInfoWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
            //所以此接口当code == 0时表示帐号被踢，或登录超时
            if (!reg.getContent().isLogin()) {
//                UsualMethod.loginWhenSessionInvalid(getContext());
                return;
            }
            YiboPreference.instance(getContext()).setToken(reg.getAccessToken());
            //更新帐户名，余额等信息
            updateAccount(reg.getContent());
        } else if (action == GET_HEADER) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult = result.result;
            MemberHeaderWraper reg = (MemberHeaderWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(getContext()).setUserHeader(reg.getContent());
            //更新头像
            updateCircleHeader();
        }
    }

    private double balance = 0;

    //更新帐户相关信息
    private void updateAccount(Meminfo meminfo) {
        if (meminfo == null) {
            return;
        }
        accountName = !Utils.isEmptyString(meminfo.getAccount()) ? meminfo.getAccount() : "暂无名称";
        accountTV.setText(accountName);
        if (!TextUtils.isEmpty(meminfo.getBalance())) {
            balance = Double.parseDouble(meminfo.getBalance());
        }
        if (!Utils.isEmptyString(meminfo.getBalance())) {
            //保留两位小数
            BigDecimal bg = new BigDecimal(meminfo.getBalance()).setScale(2, RoundingMode.DOWN);
            leftMoneyName = bg.toString();
            showYue(isShow);
        }
        ManagerFactory.INSTANCE.getBankingManager().saveUserBalance(getContext(), leftMoneyName);
//        UI改版  侧边栏用户等级不显示  2021/10/8
//        if (sys.getShowuser_levelicon().equals("on")) {
//            updateLevelIcon(level, meminfo.getLevel_icon());
//            levelName.setText(meminfo.getLevel());
//        }

        if(getContext() != null && getContext() instanceof MainActivity){
            ((MainActivity) getContext()).refreshNewMainPageLoginBlock(YiboPreference.instance(getContext()).isLogin(), accountName, balance);
        }
    }


    void refreshiAnimation() {
        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(500);//设置动画持续周期
        rotate.setRepeatCount(1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(0);//执行前的等待时间
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountName() {
        return accountName;
    }

    //切换显示隐藏余额
    private void changeJinE() {
        isShow = !isShow;
        showYue(isShow);
    }

    private void showYue(boolean isShow) {
        if (isShow) {
            balanceTV.setText(leftMoneyName);
            tvYinCang.setBackgroundResource(R.drawable.jijin_kejian);
        } else {
            balanceTV.setText("***元");
            tvYinCang.setBackgroundResource(R.drawable.jijin_bukejian);
        }

    }
}
