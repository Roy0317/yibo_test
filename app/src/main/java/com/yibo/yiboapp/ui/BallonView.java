package com.yibo.yiboapp.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;

/**
 * 投注球
 */
public class BallonView extends androidx.appcompat.widget.AppCompatTextView {

    int ballonType = Constant.BALLON_VIEW;//球类的类型
    int viewIndex;//球在TouzhuFuncView组包包装父view中的索引
    boolean isChecked;
    Drawable unSelectDrawable;
    Drawable selectDrawable;
    int viewColorNormal = R.color.light_color_red;
    int viewColorSelect = R.color.colorPrimary;
    int viewSize;
    String number;
    String numberString;
    boolean animEffect;
    int posInArray;//球所在的球组在LineBreakLayout中的位置
    boolean fakePos;//是否有真实位置坐标

    public BallonView(Context context) {
        super(context);
    }

    public BallonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BallonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBallonType(int ballonType) {
        this.ballonType = ballonType;
    }

    public int getBallonType() {
        return ballonType;
    }

    public void setIcChecked(boolean selected) {
        isChecked = selected;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setViewIndex(int viewIndex) {
        this.viewIndex = viewIndex;
    }

    public int getViewIndex() {
        return viewIndex;
    }

    public Drawable getUnSelectDrawable() {
        return unSelectDrawable;
    }

    public void setUnSelectDrawable(Drawable unSelectDrawable) {
        this.unSelectDrawable = unSelectDrawable;
    }

    public Drawable getSelectDrawable() {
        return selectDrawable;
    }

    public void setSelectDrawable(Drawable selectDrawable) {
        this.selectDrawable = selectDrawable;
    }

    public int getViewColorNormal() {
        return viewColorNormal;
    }

    public void setViewColorNormal(int viewColorNormal) {
        this.viewColorNormal = viewColorNormal;
    }

    public int getViewColorSelect() {
        return viewColorSelect;
    }

    public void setViewColorSelect(int viewColorSelect) {
        this.viewColorSelect = viewColorSelect;
    }

    public int getViewSize() {
        return viewSize;
    }

    public void setViewSize(int viewSize) {
        this.viewSize = viewSize;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isAnimEffect() {
        return animEffect;
    }

    public void setAnimEffect(boolean animEffect) {
        this.animEffect = animEffect;
    }

    public int getPosInArray() {
        return posInArray;
    }

    public void setPosInArray(int posInArray) {
        this.posInArray = posInArray;
    }

    public String getNumberString() {
        return numberString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }

    public boolean isFakePos() {
        return fakePos;
    }

    public void setFakePos(boolean fakePos) {
        this.fakePos = fakePos;
    }
}
