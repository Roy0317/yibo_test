package com.yibo.yiboapp.entify;

import java.util.List;


public class BcLotteryPlayGroup {
	private Long id;

	/**
	 * 彩种类型，1=系统彩，2=时时彩，3=pk10，4=11选5，5=排列三
	 */
	private Integer lotType;

	/**
	 * 玩法分组名称
	 */
	private String name;

	/**
	 * 序号
	 */
	private Integer sortNo;

	/**
	 * 状态，1=开启，2=关闭
	 */
	private Integer status;
	/**
	 * 站点ID
	 */
	private Long stationId;
	/**
	 * 玩法分组编码，同一彩种中，编码必须唯一
	 */
	private String code;

	/**
	 * 模板状态
	 */
	private Integer modelStatus;

	
	
	public Integer getModelStatus() {
		return modelStatus;
	}

	public void setModelStatus(Integer modelStatus) {
		this.modelStatus = modelStatus;
	}
	
	private List<BcLotteryPlay> smallList;
	
	
	
	public List<BcLotteryPlay> getSmallList() {
		return smallList;
	}

	public void setSmallList(List<BcLotteryPlay> smallList) {
		this.smallList = smallList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getLotType() {
		return lotType;
	}

	public void setLotType(Integer lotType) {
		this.lotType = lotType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
