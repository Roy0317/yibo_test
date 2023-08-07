package com.yibo.yiboapp.entify;

import java.util.List;

public class PlayItem {

	String name;
	String code;
	boolean isActivated =false;
	boolean openStatus;


	List<SubPlayItem> rules;
	/**
	 * 彩种类型，1=系统彩，2=时时彩，3=pk10，4=11选5，5=排列三
	 */
	int lotType;//所对应的彩种类型
	long groupId;//大玩法所在组ID


	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean activated) {
		isActivated = activated;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public int getLotType() {
		return lotType;
	}

	public void setLotType(int lotType) {
		this.lotType = lotType;
	}

	public boolean isOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(boolean openStatus) {
		this.openStatus = openStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SubPlayItem> getRules() {
		return rules;
	}

	public void setRules(List<SubPlayItem> rules) {
		this.rules = rules;
	}
}
