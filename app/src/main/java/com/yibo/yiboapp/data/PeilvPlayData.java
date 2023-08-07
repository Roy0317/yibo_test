package com.yibo.yiboapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.yibo.yiboapp.entify.PeilvWebResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by johnson on 2017/9/26.
 * 赔率版面板中每项gridview中每项的显示数据
 */

public class PeilvPlayData implements Cloneable, Parcelable {
    String peilv;
    float money;
    String number;
    String itemName;//所属栏项标签名称，如时时彩-整合玩法中的，万位
    String helpNumber;//辅助号码
    boolean checkbox;//是否多选
    boolean isSelected;//是否被选中了
    boolean appendTag;//号码是否要加上tagName
    boolean appendTagToTail;//号码是否要加上tagName,且添加到尾部
    boolean filterSpecialSuffix;//是否过滤特殊号码前缀
    int focusDrawable;//选中或未选中的图片
    int color;//字体颜色

    public PeilvPlayData(){}

    public PeilvPlayData(Parcel in) {
        peilv = in.readString();
        money = in.readFloat();
        number = in.readString();
        itemName = in.readString();
        helpNumber = in.readString();
        checkbox = in.readByte() != 0;
        isSelected = in.readByte() != 0;
        appendTag = in.readByte() != 0;
        appendTagToTail = in.readByte() != 0;
        filterSpecialSuffix = in.readByte() != 0;
        focusDrawable = in.readInt();
        color = in.readInt();
    }

    public static final Creator<PeilvPlayData> CREATOR = new Creator<PeilvPlayData>() {
        @Override
        public PeilvPlayData createFromParcel(Parcel in) {
            return new PeilvPlayData(in);
        }

        @Override
        public PeilvPlayData[] newArray(int size) {
            return new PeilvPlayData[size];
        }
    };

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isAppendTagToTail() {
        return appendTagToTail;
    }

    public void setAppendTagToTail(boolean appendTagToTail) {
        this.appendTagToTail = appendTagToTail;
    }

    public int getFocusDrawable() {
        return focusDrawable;
    }

    public void setFocusDrawable(int focusDrawable) {
        this.focusDrawable = focusDrawable;
    }

    public boolean isFilterSpecialSuffix() {
        return filterSpecialSuffix;
    }

    public void setFilterSpecialSuffix(boolean filterSpecialSuffix) {
        this.filterSpecialSuffix = filterSpecialSuffix;
    }

    public boolean isAppendTag() {
        return appendTag;
    }

    public void setAppendTag(boolean appendTag) {
        this.appendTag = appendTag;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    PeilvWebResult peilvData;//后台获取的赔率数据
    List<PeilvWebResult> allDatas;//所有赔率数据；当多选时需要根据勾选的号码数从中选择适当的赔率信息

    public List<PeilvWebResult> getAllDatas() {
        return allDatas;
    }

    public void setAllDatas(List<PeilvWebResult> allDatas) {
        this.allDatas = allDatas;
    }

    public PeilvWebResult getPeilvData() {
        return peilvData;
    }

    public void setPeilvData(PeilvWebResult peilvData) {
        this.peilvData = peilvData;
    }

    public String getHelpNumber() {
        return helpNumber;
    }

    public void setHelpNumber(String helpNumber) {
        this.helpNumber = helpNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getPeilv() {
        return peilv;
    }

    public void setPeilv(String peilv) {
        this.peilv = peilv;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        PeilvPlayData o = null;
        try {
            o = (PeilvPlayData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(peilv);
        dest.writeFloat(money);
        dest.writeString(number);
        dest.writeString(itemName);
        dest.writeString(helpNumber);
        dest.writeByte((byte) (checkbox ? 1 : 0));
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (appendTag ? 1 : 0));
        dest.writeByte((byte) (appendTagToTail ? 1 : 0));
        dest.writeByte((byte) (filterSpecialSuffix ? 1 : 0));
        dest.writeInt(focusDrawable);
        dest.writeInt(color);
    }
}
