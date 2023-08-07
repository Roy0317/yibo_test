package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
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
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.activity.LoginActivity;
import com.yibo.yiboapp.activity.LoginAndRegisterActivity;
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
import com.yibo.yiboapp.entify.SignResultWraper;
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

public class PersonCenterMenuHeader extends FrameLayout implements View.OnClickListener,
        SessionResponse.Listener<CrazyResult<Object>> {

    Activity activity;
    Context context;
    ImageView bgImage;
    SimpleDraweeView header;
    ImageView level;
    TextView levelName;
    TextView accountTV;
    TextView tv_refresh;
    TextView tvYinCang;
    TextView signBtn;
    //    CircleImageView qrImg;
    TextView balanceTV;
    //    LinearLayout btnsLayout;
    TextView loginRegister;
    Group loginStatusGroup;
    TextView chargeBtn;
    TextView withdrawBtn;
    TextView convertBtn;
    RotateAnimation rotate;
    private LinearLayout ll_moneyinfo;

    public static final int SIGN_REQUEST = 0x01;
    public static final int ACCOUNT_REQUEST = 0x02;
    public static final int GET_HEADER = 0x03;
    String leftMoneyName;
    String accountName;
    boolean isShow = true;

    PersonCenterMenuHeader.FaceUploadListener faceUploadListener;

    public interface FaceUploadListener {
        void onHeader();
    }

    public PersonCenterMenuHeader(Context context) {
        super(context);
        this.context = context;
    }

    public PersonCenterMenuHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PersonCenterMenuHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public PersonCenterMenuHeader.FaceUploadListener getFaceUploadListener() {
        return faceUploadListener;
    }

    public void setFaceUploadListener(PersonCenterMenuHeader.FaceUploadListener faceUploadListener) {
        this.faceUploadListener = faceUploadListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        bgImage = (ImageView) findViewById(R.id.header_bg);
        header = (SimpleDraweeView) findViewById(R.id.header);
        level = (ImageView) findViewById(R.id.level);
        levelName = (TextView) findViewById(R.id.level_name);
        header.setOnClickListener(this);
        accountTV = (TextView) findViewById(R.id.name);
        balanceTV = (TextView) findViewById(R.id.left_money);
        loginRegister = (TextView) findViewById(R.id.loginRegister);
        loginStatusGroup = (Group) findViewById(R.id.loginStatusGroup);
        loginRegister.setOnClickListener(this);
        tv_refresh = (TextView) findViewById(R.id.tv_refresh);
//        qrImg = (CircleImageView) findViewById(R.id.qrcode);
        tvYinCang = (TextView) findViewById(R.id.tvYinCang);
//        qrImg.setOnClickListener(this);
        tvYinCang.setOnClickListener(this);
//        btnsLayout = (LinearLayout) findViewById(R.id.btns);
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
        SysConfig sys = UsualMethod.getConfigFromJson(context);
        if (sys != null) {
            String logoUrl = sys.getMember_center_logo_url();
            if (!Utils.isEmptyString(YiboPreference.instance(context).getUserHeader())) {
                logoUrl = YiboPreference.instance(context).getUserHeader();
            }
//            if (!Utils.isEmptyString(logoUrl)) {
//                updateHeaderWithLotteryLogo(header, logoUrl);
//                header.setImageURI(Uri.parse(logoUrl.trim()));
//            } else {
                header.setImageURI(Uri.parse("res:///" + R.drawable.member_headers));
//                Glide.with(context).load(R.drawable.member_headers).asBitmap().transform(new GlideCircleTransform(context)).into(header);
//                header.setBackground(getResources().getDrawable(R.drawable.member_headers));
//            }
            //根据后台配置的头部背景图片地址设置背景
            String bgUrl = sys.getMember_center_bg_url();
            if (!Utils.isEmptyString(bgUrl)) {
                updateHeaderBG(bgImage, bgUrl);
            }
        }

        //获取一下帐号对应的头像
        UsualMethod.syncHeader(context, GET_HEADER, false, this);
        syncHeaderWebDatas(context);
    }

    private void updateHeaderWithLotteryLogo(ImageView lotImageView, String imgUrl) {
        if (Utils.isEmptyString(imgUrl)) {
            return;
        }
        GlideUrl glideUrl = UsualMethod.getGlide(context, imgUrl);
        RequestOptions options = new RequestOptions().
                fitCenter()
                .transform(new GlideCircleTransform(context))
                .placeholder(R.drawable.member_headers)
                .error(R.drawable.member_headers);

        Glide.with(context.getApplicationContext()).load(glideUrl)
                .apply(options)
                .into(lotImageView);
    }

    public void updateCircleHeader() {
        UsualMethod.LoadUserImage(getContext(), header);
    }


    private void updateHeaderBG(final ImageView bg, String imgUrl) {
        if (Utils.isEmptyString(imgUrl)) {
            return;
        }

        //Glide --- 3.7版本
//        Glide.with(context).load(imgUrl.trim()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                Drawable drawable = new BitmapDrawable(resource);
//                bg.setBackground(drawable);
//            }
//        });

        Glide.with(context).asBitmap().load(imgUrl.trim()).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                bg.setBackground(new BitmapDrawable(resource));
            }
        });
    }

    private void updateLevelIcon(final ImageView bg, String imgUrl) {
        if (Utils.isEmptyString(imgUrl)) {
            bg.setVisibility(View.VISIBLE);
            levelName.setVisibility(View.VISIBLE);
            return;
        }
        //Glide ----3.7版本
//        Glide.with(context).load(imgUrl.trim()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                Drawable drawable = new BitmapDrawable(resource);
//                bg.setVisibility(VISIBLE);
//                bg.setBackground(drawable);
//            }
//        });
        Glide.with(context).asBitmap().load(imgUrl.trim()).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Drawable drawable = new BitmapDrawable(resource);
                bg.setVisibility(VISIBLE);
                bg.setBackground(drawable);
            }
        });
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
                .headers(Urls.getHeader(context))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MemInfoWraper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(context, accountRequest);
    }

    private void hideUIByConfig() {
        SysConfig sc = UsualMethod.getConfigFromJson(context);
        if (sc == null) {
            return;
        }
        //签到开关是否要开启
        if (!Utils.isEmptyString(sc.getOnoff_sign_in()) && sc.getOnoff_sign_in().equals("on")) {
            signBtn.setVisibility(View.VISIBLE);
        } else {
            signBtn.setVisibility(View.GONE);
        }
//        if (UsualMethod.showConvertFee(context) && sc.getSwitch_feeconvert_beside_pickmoney().equalsIgnoreCase("on")) {
//            convertBtn.setVisibility(VISIBLE);
//        } else {
//            convertBtn.setVisibility(GONE);
//        }
    }

    public void updatePersonCenterMenuUI() {
        loginStatusGroup.setVisibility(YiboPreference.instance(context).isLogin() ? VISIBLE : GONE);
        loginRegister.setVisibility(YiboPreference.instance(context).isLogin() ? GONE : VISIBLE);
        syncHeaderWebDatas(context);
        updateCircleHeader();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.charge:
                if (Utils.shiwanFromMobile(context)) {
                    Toast.makeText(context, "操作权限不足，请联系客服", Toast.LENGTH_SHORT).show();
                    return;
                }

                String username = TextUtils.isEmpty(accountName) ? YiboPreference.instance(context).getUsername() : accountName;
                Intent intentCharge = BankingManager.Companion.openChargePage(getContext(), username, leftMoneyName);
                getContext().startActivity(intentCharge);
                break;
            case R.id.tikuan:
                if (Utils.shiwanFromMobile(context)) {
                    Toast.makeText(context, "操作权限不足，请联系客服", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = BankingManager.Companion.openWithdrawPage(context, 0f);
                context.startActivity(intent);
                break;
            case R.id.tvYinCang:
                changeJinE();
                break;
            case R.id.convert:
                if (YiboPreference.instance(getContext()).getAccountMode() == Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
                    showToast("操作权限不足，请联系客服！");
                    return;
                }
                QuotaConvertActivity.createIntent(context);
                break;
            case R.id.qrcode:
                QrcodeActivity.createIntent(context);
                break;
            case R.id.header_bg:
                UserCenterActivity.createIntent(context);
                break;
            case R.id.header:
                if (faceUploadListener != null) {
                    faceUploadListener.onHeader();
                }
                break;
            case R.id.sign:
//                UsualMethod.actionSign(context,SIGN_REQUEST,this);
                getContext().startActivity(new Intent(getContext(), SignInActivity.class));
            case R.id.ll_moneyinfo:
                tv_refresh.setAnimation(rotate);
                syncHeaderWebDatas(context);
                updateCircleHeader();
                break;
            case R.id.loginRegister:
                SysConfig sc = UsualMethod.getConfigFromJson(context);
                if (sc != null && sc.getNewmainpage_switch().equals("on")) {
                    LoginAndRegisterActivity.createIntent(context, YiboPreference.instance(context).getUsername(), YiboPreference.instance(context).getPwd(), 0);
                }else{
                    LoginActivity.createIntent(context, YiboPreference.instance(context).getUsername(), YiboPreference.instance(context).getPwd());
                }
                break;
        }
    }

    private void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if ((this.context instanceof Activity && ((Activity) context).isFinishing()) || response == null) {
            return;
        }
        int action = response.action;
        if (action == SIGN_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(context.getString(R.string.sign_fail));
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? context.getString(R.string.sign_fail) : errorString);
                return;
            }
            SignResultWraper stw = (SignResultWraper) result.result;
            if (!stw.isSuccess()) {
                showToast(Utils.isEmptyString(stw.getMsg()) ?
                        context.getString(R.string.sign_fail) : stw.getMsg());
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (stw.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(context);
                }
                return;
            }
            YiboPreference.instance(context).setToken(stw.getAccessToken());
            if (stw.getContent() != null) {
//                if (stw.getContent().getScore() == 0) {
//                    showToast("签到成功");
//                    return;
//                }else{
//                    long days = stw.getContent().getDays();
//                    if (days > 0) {
//                        String content = "恭喜您已连续签到"+days+"天，获得积分"+stw.getContent().getScore();
//                        UsualMethod.showSignSuccessDialog(context,content);
//                    }else{
//                        showToast("签到成功");
//                        return;
//                    }
//                }

                long days = stw.getContent().getDays();
                String content = "恭喜您已连续签到" + days + "天，获得积分" + stw.getContent().getScore();
                UsualMethod.showSignSuccessDialog(context, content);

            }

        } else if (action == ACCOUNT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? context.getString(R.string.request_fail) : errorString);
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
//                UsualMethod.loginWhenSessionInvalid(context);
                return;
            }
            YiboPreference.instance(context).setToken(reg.getAccessToken());
            //更新帐户名，余额等信息
            updateAccount(reg.getContent());
        } else if (action == GET_HEADER) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
//                String errorString = Urls.parseResponseResult(result.error);
//                showToast(Utils.isEmptyString(errorString) ? context.getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            MemberHeaderWraper reg = (MemberHeaderWraper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
//            UsualMethod.getConfigFromJson(getContext());
            YiboPreference.instance(context).setUserHeader(reg.getContent());
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
        SysConfig sys = UsualMethod.getConfigFromJson(context);
        if (sys != null) {
            if (sys.getShowuser_levelicon().equals("on")) {
                updateLevelIcon(level, meminfo.getLevel_icon());
                levelName.setText(meminfo.getLevel());
            }
        }

        if(activity != null && activity instanceof MainActivity){
            ((MainActivity) activity).refreshNewMainPageLoginBlock(YiboPreference.instance(context).isLogin(), accountName, balance);
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

    public String getLeftMoneyName() {
        return leftMoneyName;
    }

    //切换显示隐藏余额
    void changeJinE() {
        String str;
        if (isShow) {
            isShow = false;
        } else {
            isShow = true;
        }
        showYue(isShow);
    }

    void showYue(boolean isShow) {
        if (isShow) {
            balanceTV.setText(leftMoneyName);
            tvYinCang.setBackgroundResource(R.drawable.jijin_kejian);
        } else {
            balanceTV.setText("****");
            tvYinCang.setBackgroundResource(R.drawable.jijin_bukejian);
        }

    }
}
