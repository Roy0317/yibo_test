package com.yibo.yiboapp.entify;
/**
 * 球赛类型
 * @author admin
 *
 */
public enum SportType {
	//足球
	FOOTBALL(1),
	//蓝美
	BASKETBALL(2);
	
	private SportType(int type){
		this.type = type;
	}
	
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public static SportType getSportType(int type){
		SportType [] sts = SportType.values();
		for (int i = 0; i < sts.length; i++) {
			SportType st = sts[i];
			if(st.getType() == type){
				return st;
			}
		}
		return null;
	}
}
