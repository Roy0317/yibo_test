package com.example.anuo.immodule.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.base.BaseLVAdapter;
import com.example.anuo.immodule.base.LViewHolder;
import com.example.anuo.immodule.lottery.LotteryUtils;
import com.example.anuo.immodule.utils.ScreenUtil;

import java.util.List;

public class ChatNumbersAdapter extends BaseLVAdapter<String> {

    Context context;
    String codeType;
    List<String> mDatas;
    String cpVersion;
    private String[] shengxiao;
    private String[] arr;
    boolean forOpenResult;
    private int defaultWidth; // 默认的宽高
    private int farmWidth;  //幸运农场宽高
    private int saicheWidth; //赛车item宽高
    private boolean isNew = false;
    private long date;//开奖日期

    public ChatNumbersAdapter(Context mContext, List<String> mDatas, String cpVersion,
                          int layoutId, String codeType, boolean forOpenResult) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        this.codeType = codeType;
        this.mDatas = mDatas;
        this.cpVersion = cpVersion;
        this.forOpenResult = forOpenResult;
        defaultWidth = ScreenUtil.dip2px(context, 35);
        farmWidth = ScreenUtil.dip2px(context, 30);
        saicheWidth = ScreenUtil.dip2px(context, 20);

        shengxiao =  LotteryUtils.getNumbersFromShengXiao(context, true);
        arr = context.getResources().getStringArray(R.array.shengxiao_str);

    }

    public ChatNumbersAdapter(Context mContext, List<String> mDatas, String cpVersion,
                          int layoutId, String codeType, boolean forOpenResult, long date) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        this.codeType = codeType;
        this.mDatas = mDatas;
        this.cpVersion = cpVersion;
        this.forOpenResult = forOpenResult;
        this.date = date;
        defaultWidth = ScreenUtil.dip2px(context, 35);
        farmWidth = ScreenUtil.dip2px(context, 30);
        saicheWidth = ScreenUtil.dip2px(context, 20);

        shengxiao = LotteryUtils.getNumbersFromShengXiao(context, true);
        arr = context.getResources().getStringArray(R.array.shengxiao_str);

    }


    @Override
    public void convert(final int position, LViewHolder holder, ViewGroup parent, final String item) {
        TextView ball = holder.getView(R.id.ball);
        figureImgeByCpCode(holder, item, ball, position);
    }

    /**
     * 快三
     *
     * @param num
     * @param ball
     */
    private void setKuai3NumImage(String num, TextView ball) {
        LotteryUtils.renderKuai3BgForNumber(ball, num);
        ball.setText("");
    }


    /**
     * 幸运农场
     *
     * @param num
     * @param ball
     */
    private void figureXYNCImage(String num, TextView ball) {
        if (num.equals("?")) {
            ball.setBackground(context.getResources().getDrawable(R.drawable.bet_open_result_ball));
            ball.setText(num);
        } else {
            LotteryUtils.renderFarmBgForNumber(ball, num);
            ball.setText("");
        }

    }

    /**
     * 赛车
     *
     * @param num
     * @param ball
     */
    private void figureSaiCheImage(String num, TextView ball, boolean forOpenResult) {
        if (num.equals("?")) {
            ball.setBackground(context.getResources().getDrawable(R.drawable.bet_open_result_ball));
            ball.setText(num);
        } else {
            if (num.equals("01")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_01 : R.drawable.sc_01_title);
            } else if (num.equals("02")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_02 : R.drawable.sc_02_title);
            } else if (num.equals("03")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_03 : R.drawable.sc_03_title);
            } else if (num.equals("04")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_04 : R.drawable.sc_04_title);
            } else if (num.equals("05")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_05 : R.drawable.sc_05_title);
            } else if (num.equals("06")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_06 : R.drawable.sc_06_title);
            } else if (num.equals("07")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_07 : R.drawable.sc_07_title);
            } else if (num.equals("08")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_08 : R.drawable.sc_08_title);
            } else if (num.equals("09")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_09 : R.drawable.sc_09_title);
            } else if (num.equals("10")) {
                ball.setBackgroundResource(!forOpenResult ? R.drawable.sc_10 : R.drawable.sc_10_title);
            }
            ball.setText("");
        }
    }

    private void figureLhcImage(String num, TextView ball) {

        LotteryUtils.renderBgForNumber(ball, num, isNew);
        if (isNew) {
            ball.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            ball.setTextColor(ContextCompat.getColor(context, R.color.color_txt));
        }
        ball.setText(num);
    }

    private void figureImgeByCpCode(LViewHolder holder, String num, TextView ball, int position) {
        ViewGroup.LayoutParams params = ball.getLayoutParams();
        params.width = defaultWidth;
        params.height = defaultWidth;
        ball.setTextSize(17);

        switch (codeType) {
            case "1":
            case "2": //时时彩
            case "5": //11选5
            case "7": //pc蛋蛋，加拿大28
            case "8": //福彩3D，排列三
                ball.setBackground(context.getResources().getDrawable(R.drawable.bet_open_result_ball));
                ball.setText(num);
                break;
            case "3": //极速赛车，北京赛车，幸运飞艇
                params.width = saicheWidth;
                params.height = saicheWidth;
                figureSaiCheImage(num, ball, forOpenResult);
                break;
            case "4": //快三
                setKuai3NumImage(num, ball);
                break;
            case "6": //六合彩，十分六合彩
            case "66":
                defaultWidth = ScreenUtil.dip2px(context, 28);
                params.width = defaultWidth;
                params.height = defaultWidth;
                TextView txtName = holder.getView(R.id.txtName);
                TextView txtSymbol = holder.getView(R.id.txtSymbol);
                if (position == mDatas.size() - 2) { //显示加号
                    if (txtName != null) {
                        txtName.setVisibility(View.GONE);
                    }
                    if (txtSymbol != null) {
                        txtSymbol.setVisibility(View.VISIBLE);
                    }
                    ball.setVisibility(View.GONE);
                } else {
                    if (txtSymbol != null) {
                        txtSymbol.setVisibility(View.GONE);
                    }
                    ball.setVisibility(View.VISIBLE);
                    String sx = LotteryUtils.getShenxiaoFromNumberAndDate(context, num, date);
                    if (txtName != null) {
                        txtName.setVisibility(View.VISIBLE);
                        txtName.setText(sx);
                    }
//                    txtName.setText(Mytools.getZodiacByNum(shengxiao, arr, num));
                    figureLhcImage(num, ball);
                }
                break;
            case "9": //重庆幸运农场,湖南快乐十分,广东快乐十分
                if (cpVersion.equals(String.valueOf(LotteryUtils.VERSION_2))) {
                    params.width = farmWidth;
                    params.height = farmWidth;
                    figureXYNCImage(num, ball);
                }
                break;
        }
        ball.requestLayout();
    }


}