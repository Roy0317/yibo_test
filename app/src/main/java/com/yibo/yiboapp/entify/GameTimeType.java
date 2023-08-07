package com.yibo.yiboapp.entify;
/**
 * 采集数据类型
 * @author admin
 *  GameTimeType =》DataType =》BetType =》BetItemType
 */
public enum GameTimeType {
	RUNBALL(1), //滚球
	TODAY(2),  //今日赛事
	FUTURE(3); //早盘
	
	
	private GameTimeType(int val){
		this.val = val;
	}
	
	private int val;

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}
	
	public static GameTimeType getGameTimeType(int type){
		GameTimeType [] sts = GameTimeType.values();
		for (int i = 0; i < sts.length; i++) {
			GameTimeType st = sts[i];
			if(st.getVal() == type){
				return st;
			}
		}
		return null;
	}
}
