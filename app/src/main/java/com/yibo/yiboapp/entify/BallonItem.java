package com.yibo.yiboapp.entify;

import com.yibo.yiboapp.ui.BallonView;

/**
 * 投注球
 */
public class BallonItem {

    public String num;//球的号码或文字
    public String numStr;//号码串
    public int posInArray;//球VIEW在球组中的位置
    public int typeOfBallon;//球的类型，如彩票红球
    public BallonView ballon;//球VIEW
    public boolean isBallonSelect;//是否选中状态
    public String ballonKey;//球的索引键，方便唯一确定球
}
