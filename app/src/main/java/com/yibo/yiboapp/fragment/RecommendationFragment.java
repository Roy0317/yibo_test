package com.yibo.yiboapp.fragment;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.MyRecommendInfoBean;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.utils.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.zxing.EncodingHandler;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * Author: Ray
 * created on 2018年10月12日21:26:33
 * description : 推荐信息Fragment
 */
public class RecommendationFragment extends RecommendationBaseFragment implements View.OnClickListener, SessionResponse.Listener<CrazyResult> {


    private TextView userNameTv; //我的用户名
    private TextView codeTv;//我的推广码
    private TextView appLinkTv;//App链接
    private TextView tjLinkTv;//推荐链接
    private TextView pswtjLinkTv;//加密推荐链接
    private LinearLayout appLinkLayout;
    private LinearLayout tjLinkLayout;
    private LinearLayout pswtjLinkLayout;
    private LinearLayout cpRollLayout;
    private LinearLayout realRollLayout;
    private LinearLayout gameRollLayout;
    private LinearLayout sportRollLayout;
    private LinearLayout sbsportRollLayout;
    private LinearLayout chessRollLayout;
    private LinearLayout lhcRollLayout;
    private LinearLayout thirdsportRollLayout;
    private TextView thirdSportReturnTv;
    private TextView lotteryReturnTv;//彩票返点
    private TextView realPersonReturnTv;//真人返点
    private TextView electricReturnTv;//电子返点
    private TextView sportsReturnTv;//体育返点
    private TextView sbSportsReturnTv;//沙巴体育返点
    private TextView chessReturnTv;//棋牌返点
    private TextView lhcReturnTv;//lhc返点
    private TextView ifUseful;//是否可用
    private LinearLayout linearCommissionCalculation;
    private TextView tv_commission_calculation_content;
    private ImageView qrPromotion;

    public static final int RECOMMEND_REQUEST = 0x01; //请求的标志位

    /**
     * @return 初始化布局
     */
    @Override
    public View initView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.fragment_recommendation, null);
    }


    /**
     * 初始化控件
     */
    @Override
    public void initWidget() {
        CardView codeCopyCardView = find(R.id.cd_extension_copy);
        CardView linkCopyCardView = find(R.id.cd_copy_link);
        CardView tjCopyCardView = find(R.id.cd_copy_tuijianlink);
        CardView pswCopyCardView = find(R.id.cd_copy_pswtuijianlink);
        userNameTv = find(R.id.tv_my_user_name);
        codeTv = find(R.id.tv_my_command_code);
        appLinkTv = find(R.id.tv_app_link);
        tjLinkTv = find(R.id.tv_tuijian_link);
        pswtjLinkTv = find(R.id.tv_app_pswtuijianlink);
        lotteryReturnTv = find(R.id.tv_lottery_rebate);
        realPersonReturnTv = find(R.id.tv_person_rebate);
        electricReturnTv = find(R.id.tv_electric_rebate);
        sportsReturnTv = find(R.id.tv_sports_rebate);
        sbSportsReturnTv = find(R.id.tv_sb_sports_rebate);
        chessReturnTv = find(R.id.tv_chess_rebate);
        lhcReturnTv = find(R.id.tv_lhc_rebate);
        ifUseful = find(R.id.tv_if_useful);
        linearCommissionCalculation = find(R.id.linear_commission_calculation);
        tv_commission_calculation_content = find(R.id.tv_commission_calculation_content);
        thirdSportReturnTv = find(R.id.tv_third_sports_rebate);

        cpRollLayout = find(R.id.cproll);
        appLinkLayout = find(R.id.ll_app_link);
        tjLinkLayout = find(R.id.ll_tuijian_link);
        pswtjLinkLayout = find(R.id.ll_app_pswtuijianlink);
        realRollLayout = find(R.id.realroll);
        gameRollLayout = find(R.id.gameroll);
        sportRollLayout = find(R.id.sportroll);
        sbsportRollLayout = find(R.id.sbsportroll);
        chessRollLayout = find(R.id.chessroll);
        lhcRollLayout = find(R.id.lhcroll);
        thirdsportRollLayout = find(R.id.thirdsportroll);
        qrPromotion = find(R.id.qrPromotion);

        linkCopyCardView.setOnClickListener(this);
        codeCopyCardView.setOnClickListener(this);
        tjCopyCardView.setOnClickListener(this);
        pswCopyCardView.setOnClickListener(this);

    }


    /**
     * 请求网络数据
     * 回调则是在下面的方法触发：
     * onResponse(SessionResponse<CrazyResult> response){}
     */
    @Override
    public void fetchData() {
        //我的推荐
        String configUrl = Urls.BASE_URL + Urls.PORT + Urls.MY_RECOMMEND_URL;
        CrazyRequest<CrazyResult<MyRecommendInfoBean>> request = new AbstractCrazyRequest.Builder().
                url(configUrl.toString())
                .seqnumber(RECOMMEND_REQUEST)
                .headers(Urls.getHeader(getActivity()))
                .shouldCache(false)
                .listener(this)
                .placeholderText(activity.getString(R.string.loading))
                .priority(CrazyRequest.Priority.HIGH.ordinal())
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<MyRecommendInfoBean>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();

        RequestManager.getInstance().startRequest(activity, request);

    }


    @Override
    public void onResponse(SessionResponse<CrazyResult> response) {
        RequestManager.getInstance().afterRequest(response);
        if (activity.isFinishing() || response == null) return;
        int action = response.action;
        switch (action) {
            //我的推荐请求标志位
            case RECOMMEND_REQUEST:
                CrazyResult result = response.result;
                //第一步先判断result是否为null或者result.crazySuccess是否为真
                if (result == null || !result.crazySuccess) {
                    showToast(getString(R.string.get_recommend_info_fail));
                    return;
                }
                MyRecommendInfoBean infoBean = (MyRecommendInfoBean) result.result;

                if (!infoBean.isSuccess()) {
                    showToast(!Utils.isEmptyString(infoBean.getMsg())?infoBean.getMsg():
                            getString(R.string.get_recommend_info_fail));
                    //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                    //所以此接口当code == 0时表示帐号被踢，或登录超时
                    if (infoBean.getCode() == 0) {
                        UsualMethod.loginWhenSessionInvalid(activity);
                    }
                    return;
                }

                if (!infoBean.isSuccess()) {
                    getString(R.string.get_recommend_info_fail);
                    return;
                }
                YiboPreference.instance(activity).setToken(infoBean.getAccessToken());
                //更新我的推荐信息
                updateRecommendInfo(infoBean.getContent());
                break;
        }
    }


    private void updateRecommendInfo(MyRecommendInfoBean.ContentBean content) {
        userNameTv.setText(content.getUsername());
        MyRecommendInfoBean.ContentBean.DataBean data = content.getData();
        codeTv.setText(data.getLinkKey());
        //App链接：
        String sysConfig = YiboPreference.instance(activity).getSysConfig();
        SysConfig sc = new Gson().fromJson(sysConfig, SysConfig.class);
        if (sc != null) {
            String url = sc.getApp_download_link_android();
            if (!Utils.isEmptyString(url)) {
                appLinkTv.setText(url);
            }
        }

        SysConfig config = UsualMethod.getConfigFromJson(getActivity());
        if (config != null) {
            if (config.getOnoff_dian_zi_you_yi().equalsIgnoreCase("on")) {
                gameRollLayout.setVisibility(View.VISIBLE);
            }else{
                gameRollLayout.setVisibility(View.GONE);
            }
            if (config.getOnoff_zhen_ren_yu_le().equalsIgnoreCase("on")) {
                realRollLayout.setVisibility(View.VISIBLE);
            }else{
                realRollLayout.setVisibility(View.GONE);
            }
            if (config.getOnoff_sports_game().equalsIgnoreCase("on")) {
                sportRollLayout.setVisibility(View.VISIBLE);
            }else{
                sportRollLayout.setVisibility(View.GONE);
            }
            if (config.getOnoff_shaba_sports_game().equalsIgnoreCase("on")) {
                sbsportRollLayout.setVisibility(View.VISIBLE);
            }else{
                sbsportRollLayout.setVisibility(View.GONE);
            }
            if (config.getOnoff_third_sport().equalsIgnoreCase("on")){
                thirdsportRollLayout.setVisibility(View.VISIBLE);
            }else {
                thirdsportRollLayout.setVisibility(View.GONE);
            }
            if (config.getOnoff_chess().equalsIgnoreCase("on")) {
                chessRollLayout.setVisibility(View.VISIBLE);
            }else{
                chessRollLayout.setVisibility(View.GONE);
            }

            if (config.getPromp_link_version().equalsIgnoreCase("v1")) {
                tjLinkLayout.setVisibility(View.GONE);
                pswtjLinkLayout.setVisibility(View.GONE);
                appLinkLayout.setVisibility(View.VISIBLE);
            }else{
                tjLinkLayout.setVisibility(View.VISIBLE);
                pswtjLinkLayout.setVisibility(View.VISIBLE);
                appLinkLayout.setVisibility(View.GONE);
            }
        }

        lotteryReturnTv.setText(data.getCpRolling() + "‰");
        lhcReturnTv.setText(data.getLhcRolling() + "‰");
        realPersonReturnTv.setText(data.getRealRolling() + "‰");
        electricReturnTv.setText(data.getEgameRolling() + "‰");
        sportsReturnTv.setText(data.getSportRolling() + "‰");
        sbSportsReturnTv.setText(data.getSbSportRolling() + "‰");
        thirdSportReturnTv.setText(data.getThirdSportRolling() + "‰");
        chessReturnTv.setText(data.getChessRolling() + "‰");
        ifUseful.setText(data.getType() == 2 ? "可用" : "不可用");
        tjLinkTv.setText(data.getLinkUrl());
        pswtjLinkTv.setText(data.getLinkUrlEn());


        boolean showCaipiaoModule = !Utils.isEmptyString(sc.getOnoff_lottery_game()) && sc.getOnoff_lottery_game().equals("on");
        boolean showSportModule = (!Utils.isEmptyString(sc.getOnoff_sports_game()) && sc.getOnoff_sports_game().equals("on")) ||
                (!Utils.isEmptyString(sc.getOnoff_shaba_sports_game()) && sc.getOnoff_shaba_sports_game().equals("on"));
        boolean showRealPersonModule = !Utils.isEmptyString(sc.getOnoff_zhen_ren_yu_le()) && sc.getOnoff_zhen_ren_yu_le().equals("on");
        boolean showGameModule = !Utils.isEmptyString(sc.getOnoff_dian_zi_you_yi()) && sc.getOnoff_dian_zi_you_yi().equals("on");
        boolean showChessModule = !Utils.isEmptyString(sc.getNbchess_showin_mainpage()) && sc.getNbchess_showin_mainpage().equals("on");
        if((showCaipiaoModule && data.getCpRolling() != 0) ||
                data.getLhcRolling() != 0 ||
                (showRealPersonModule && data.getRealRolling() != 0) ||
                (showGameModule && data.getEgameRolling() != 0) ||
                (showSportModule && data.getSportRolling() != 0) ||
                (showSportModule && data.getSbSportRolling() != 0) ||
                (showChessModule && data.getChessRolling() != 0)){

            StringBuilder sb = new StringBuilder();
            sb.append("您推荐的会员在下注结算后，佣金会自动按照比例加到您的资金账户上.注：计算方式为（");
            if (showCaipiaoModule) {
                sb.append("彩票投注金额*彩票返点");
            }
            if (showRealPersonModule) {
                sb.append("+真人投注金额*真人返点");
            }
            if (showGameModule) {
                sb.append("+电子投注金额*电子返点");
            }
            if (showSportModule) {
                sb.append("+体育投注金额*体育返点");
            }
            if (showChessModule) {
                sb.append("+棋牌投注金额*棋牌返点）");
            }
            linearCommissionCalculation.setVisibility(View.VISIBLE);
            tv_commission_calculation_content.setText(sb.toString());
        }else {
            linearCommissionCalculation.setVisibility(View.GONE);
        }

        //显示推广码QR CODE
        String str = Urls.BASE_URL+"/registersFixedAlone.do?init="+data.getLinkKey();
        if (!Utils.isEmptyString(str)) {
            try {
                Bitmap bitmap = EncodingHandler.createQRCode(str, Utils.dip2px(activity, 350));
                qrPromotion.setImageBitmap(bitmap);
                qrPromotion.setOnClickListener(v -> imgMax(bitmap));
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param tv copyText
     */
    protected void copy(TextView tv) {
        String content = tv.getText().toString();
        if (!Utils.isEmptyString(content)) {
            UsualMethod.copy(content, activity);
            showToast("复制成功");
        } else {
            showToast("没有内容，无法复制");
        }
    }


    /**
     * onClickEvent
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cd_extension_copy:
                copy(codeTv);
                break;
            case R.id.cd_copy_link:
                copy(appLinkTv);
                break;
                case R.id.cd_copy_tuijianlink:
                copy(tjLinkTv);
                break;
            case R.id.cd_copy_pswtuijianlink:
                copy(pswtjLinkTv);
                break;
        }
    }

    /**
     * 点击查看大图
     */
    public void imgMax(Bitmap bitmap) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null);
        // 加载自定义的布局文件
        final AlertDialog dialog = new  AlertDialog.Builder(getContext(),R.style.DialogTransparent).create();
        ImageView img = imgEntryView.findViewById(R.id.large_image);
        img.setImageBitmap(bitmap);
        // 这个是加载网络图片的，可以是自己的图片设置方法
        // imageDownloader.download(imageBmList.get(0),img);
        dialog.setView(imgEntryView); // 自定义dialog
        dialog.show();
        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        imgEntryView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                dialog.cancel();
            }
        });
    }
}
