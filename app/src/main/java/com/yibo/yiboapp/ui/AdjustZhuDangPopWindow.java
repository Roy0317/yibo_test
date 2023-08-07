package com.yibo.yiboapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.AdjustData;
import com.yibo.yiboapp.utils.Utils;

import java.text.DecimalFormat;

/**
 * Created by johnson on 2017/9/19.
 * 投注时，调整注单信息的弹出窗
 */

public class AdjustZhuDangPopWindow extends PopupWindow {

    private Context mContext;
    XEditText inputBeishu;
    TextView yuan;
    TextView jiao;
    TextView fen;
    TextView fanli;
    TextView totalMoney;
    TextView xiazhuTV;
    TextView jianjinTV;

    int selectModeIndex;//选择的金额模式-索引，0-元，1-角 2-分
    int selectZhushu;
    int selectBeishu;
    float selectMoney;//金额，单位为元
    CheckBox noFloatAgain;//不再自动弹出

    AdjustData adjustData;
    AdjustListener adjustListener;

    boolean autoPop = false;//自动弹出注单调整框


    public AdjustZhuDangPopWindow(final Context mContext) {

        this.mContext = mContext;
        View content = LayoutInflater.from(mContext).inflate(R.layout.zhudang_setting, null);
        content.isFocusableInTouchMode();
        PaneClickListener listener = new PaneClickListener();
        Button cancelBtn = (Button) content.findViewById(R.id.cancel);
        Button okBtn = (Button) content.findViewById(R.id.ok);
        TextView jianTxt = (TextView) content.findViewById(R.id.jian);
        TextView addTxt = (TextView) content.findViewById(R.id.add);
        inputBeishu = (XEditText) content.findViewById(R.id.input_beishu);
        totalMoney = (TextView) content.findViewById(R.id.total_money);
        xiazhuTV = (TextView) content.findViewById(R.id.zhushu);
        jianjinTV = (TextView) content.findViewById(R.id.jianjin_single);
        fanli = (TextView) content.findViewById(R.id.fanli);
        noFloatAgain = (CheckBox) content.findViewById(R.id.checkbox_auto);
        noFloatAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autoPop = false;
                }else{
                    autoPop = true;
                }
            }
        });

        inputBeishu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                if (Utils.isEmptyString(s)) {
                    selectBeishu = 1;
                    adjustMoney(selectBeishu,selectZhushu,selectModeIndex);
                    return;
                }
                if (!Utils.isNumeric(s)) {
                    return;
                }
                int bsInt = Integer.parseInt(s);
                if (bsInt <= 0){
                    return;
                }
//                inputBeishu.setText(String.valueOf(bsInt));
                selectBeishu = bsInt;
                adjustMoney(selectBeishu,selectZhushu,selectModeIndex);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        yuan = (TextView) content.findViewById(R.id.yuan);
        jiao = (TextView) content.findViewById(R.id.jiao);
        fen = (TextView) content.findViewById(R.id.fen);
        if (mContext instanceof View.OnClickListener) {
            jianTxt.setOnClickListener(listener);
            addTxt.setOnClickListener(listener);
            cancelBtn.setOnClickListener(listener);
            okBtn.setOnClickListener(listener);
            yuan.setOnClickListener(listener);
            jiao.setOnClickListener(listener);
            fen.setOnClickListener(listener);
        }
        // 设置外部可点击
        this.setOutsideTouchable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!(mContext instanceof Activity)){
                    throw new IllegalStateException("attach window is not in activity");
                }
                final Activity activity = (Activity) mContext;
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1.0f;
                activity.getWindow().setAttributes(params);
            }
        });
        setOutsideTouchable(true);

        this.setContentView(content);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.adjust_window_anim);
    }

    public void setData(AdjustData adjustData) {
        this.adjustData = adjustData;
        selectModeIndex = adjustData.getModeIndex();
        selectMoney = adjustData.getMoney();
        selectBeishu = adjustData.getBeishu();
        selectZhushu = adjustData.getZhushu();
        float jianjian = adjustData.getJianjian();

        DecimalFormat decimalFormat =new DecimalFormat("0.00");
        String jianjinFee = decimalFormat.format(jianjian);

//        if (fanliValue > 0) {
//            fanli.setVisibility(View.VISIBLE);
//            fanli.setText("返利:"+fanliValue+"%");
//        }else{
//            fanli.setVisibility(View.GONE);
//        }
        xiazhuTV.setText(String.format(mContext.getString(R.string.xiazhu_zhushu_format),selectZhushu));
        inputBeishu.setText(String.valueOf(selectBeishu));
        jianjinTV.setText(String.format(mContext.getString(R.string.jianjin_single_zhushu_format),
                jianjinFee));
        switchMode(selectModeIndex);

        String moneyString = decimalFormat.format(selectMoney);
        totalMoney.setText(String.format(mContext.getString(R.string.xiazhu_fee_format),moneyString));
    }

    public void showWindow(View attach,boolean shoudong) {
        if (!shoudong) {
            if (!autoPop) {
                return;
            }
        }
        if (!(mContext instanceof Activity)){
            throw new IllegalStateException("attach window is not in activity");
        }
        final Activity activity = (Activity) mContext;
        showAtLocation(attach, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha=0.7f;
        activity.getWindow().setAttributes(params);
    }


    public interface AdjustListener{
        void onAdjustResult(AdjustData adjustData);
    }

    public void setAdjustListener(AdjustListener adjustListener) {
        this.adjustListener = adjustListener;
    }

    private final class PaneClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel:
                    dismiss();
                    break;
                case R.id.ok:
                    dismiss();
                    if (adjustListener != null) {
                        if (adjustData != null) {
                            adjustData.setModeIndex(selectModeIndex);
                            adjustData.setMoney(selectMoney);
                            adjustData.setZhushu(selectZhushu);
                            adjustData.setBeishu(selectBeishu);
                        }
                        adjustListener.onAdjustResult(adjustData);
                    }
                    break;
                case R.id.jian:
                    String jian = inputBeishu.getText().toString().trim();
                    if (Utils.isEmptyString(jian)) {
                        showToast(mContext.getString(R.string.input_beishu));
                        return;
                    }
                    if (!Utils.isNumeric(jian)) {
                        showToast(mContext.getString(R.string.input_correct_beishu_format));
                        return;
                    }
                    int bsInt = Integer.parseInt(jian);
                    bsInt--;
                    if (bsInt <= 0){
                        return;
                    }
                    inputBeishu.setText(String.valueOf(bsInt));
                    selectBeishu = bsInt;
                    adjustMoney(selectBeishu,selectZhushu,selectModeIndex);
                    break;
                case R.id.add:
                    String add = inputBeishu.getText().toString().trim();
                    if (Utils.isEmptyString(add)) {
                        showToast(mContext.getString(R.string.input_beishu));
                        return;
                    }
                    if (!Utils.isNumeric(add)) {
                        showToast(mContext.getString(R.string.input_correct_beishu_format));
                        return;
                    }
                    int addInt = Integer.parseInt(add);
                    addInt++;
                    inputBeishu.setText(String.valueOf(addInt));
                    selectBeishu = addInt;
                    adjustMoney(selectBeishu,selectZhushu,selectModeIndex);
                    break;
                case R.id.yuan:
                    selectModeIndex = 0;
                    switchMode(selectModeIndex);
                    adjustMoney(selectBeishu,selectZhushu,selectModeIndex);
                    break;
                case R.id.jiao:
                    selectModeIndex = 1;
                    switchMode(selectModeIndex);
                    adjustMoney(selectBeishu,selectZhushu,selectModeIndex);
                    break;
                case R.id.fen:
                    selectModeIndex = 2;
                    switchMode(selectModeIndex);
                    adjustMoney(selectBeishu,selectZhushu,selectModeIndex);
                    break;
            }
        }
    }

    /**
     * 调整总金额
     * @param beishu 倍数
     * @param zhushu 注数
     * @param selectModeIndex 模式索引
     */
    private void adjustMoney(int beishu,int zhushu,int selectModeIndex) {
        float money = adjustData.getBasicMoney();
        money = money*beishu*zhushu;
        if (selectModeIndex == 0) {
        } else if (selectModeIndex == 1) {
            money = money/10;
        } else if (selectModeIndex == 2) {
            money = money/100;
        }
        selectMoney = money;
        DecimalFormat decimalFormat =new DecimalFormat("0.00");
        String moneyString = decimalFormat.format(selectMoney);
        totalMoney.setText(String.format(mContext.getString(R.string.xiazhu_fee_format),moneyString));
    }

    /**
     *
     * @param selectMode 以选择的金额模式 索引 ，0-元 1-角 2-分
     */
    private void switchMode(int selectMode){

        String yjfMode = YiboPreference.instance(mContext).getYjfMode();
        int yjfValue = Constant.YUAN_MODE;
        yjfValue = Integer.parseInt(yjfMode);
        if (yjfValue == Constant.YUAN_MODE) {
            yuan.setVisibility(View.VISIBLE);
            jiao.setVisibility(View.GONE);
            fen.setVisibility(View.GONE);
            yuan.setBackground(mContext.getResources().getDrawable(
                    R.drawable.light_gred_border_middle_segment_press));
            yuan.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        } else if (yjfValue == Constant.JIAO_MODE) {
            yuan.setVisibility(View.VISIBLE);
            jiao.setVisibility(View.VISIBLE);
            fen.setVisibility(View.GONE);
            if (selectMode == 0) {
                yuan.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment_press));
                jiao.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment));
                yuan.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                jiao.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else if (selectMode == 1) {
                yuan.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment));
                jiao.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment_press));
                yuan.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                jiao.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            }
        } else if (yjfValue == Constant.FEN_MODE) {
            yuan.setVisibility(View.VISIBLE);
            jiao.setVisibility(View.VISIBLE);
            fen.setVisibility(View.VISIBLE);
            if (selectMode == 0) {
                yuan.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment_press));
                jiao.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment));
                fen.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment));
                yuan.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                jiao.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                fen.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else if (selectMode == 1) {
                yuan.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment));
                jiao.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment_press));
                fen.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment));
                yuan.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                jiao.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                fen.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else if (selectMode == 2) {
                yuan.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment));
                jiao.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment));
                fen.setBackground(mContext.getResources().getDrawable(
                        R.drawable.light_gred_border_middle_segment_press));
                yuan.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                jiao.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                fen.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            }
        }
        selectMode = yjfValue;
    }

    private void showToast(String showText) {
        if (Utils.isEmptyString(showText)) {
            return;
        }
        Toast.makeText(mContext,showText,Toast.LENGTH_SHORT).show();
    }

}