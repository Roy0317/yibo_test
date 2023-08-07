package com.yibo.yiboapp.entify;
/**
 * 玩法类型
 * @author admin
 * GameTimeType =》DataType =》BetType =》BetItemType
 */
public enum DataType {
	
	//独赢 ＆ 让球 ＆ 大小 & 单 / 双
	MAIN(1),
	
	//波胆  上半场
	TIME_HALF(2),
	
	//波胆  全场
	TIME_FULL(3),
	
	//总入球数
	BALL_COUNT(4),
	
	//全场半场输赢
	FULL_AND_HALF(5),
	
	//混合过关
	MIX(6),
	
	//冠军
	CHAMPION(7);
	
	
	private DataType(int val){
		this.val = val;
	}
	
	private int val;
	
	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}
	
	public static DataType getDataType(int type){
		DataType [] sts = DataType.values();
		for (int i = 0; i < sts.length; i++) {
			DataType st = sts[i];
			if(st.getVal() == type){
				return st;
			}
		}
		return null;
	}
}
