package com.yibo.yiboapp.entify;

import java.math.BigDecimal;

public class BcLotteryPlay {
	private Long id;
	
	private Long stationId;

	private Long groupId;

	private String name;

	/**
	 * 最大中奖金额
	 */
	private BigDecimal maxBonusOdds;

	/**
	 * 最小中奖金额
	 */
	private BigDecimal minBonusOdds;

	/**
	 * 最低返水
	 */
	private BigDecimal minRakeback;
	
	/**
	 * 最高返水
	 */
	private BigDecimal maxRakeback;

	/**
	 * 状态，1=启用，2=关闭
	 */
	private Integer status;

	/**
	 * 最高注数
	 */
	private Integer maxNumber;

	/**
	 * 序号
	 */
	private Integer sortNo;
	
	/**
	 * 彩种类型，1=系统彩，2=时时彩，3=pk10，4=排列三，5=11选5，6=香港彩，7=PC蛋蛋
	 */
	private Integer lotType;

	/**
	 * 详细介绍
	 */
	private String detailDesc;

	/**
	 * 中奖范例
	 */
	private String winExample;

	/**
	 * 玩法介绍
	 */
	private String playMethod;

	/**
	 * 玩法编码
	 */
	private String code;
	
	/**
	 * 模板状态
	 */
	private Integer modelStatus;

	public Integer getLotType() {
		return lotType;
	}

	public void setLotType(Integer lotType) {
		this.lotType = lotType;
	}

	public Integer getModelStatus() {
		return modelStatus;
	}

	public void setModelStatus(Integer modelStatus) {
		this.modelStatus = modelStatus;
	}
	
	
	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getMaxBonusOdds() {
		return maxBonusOdds;
	}

	public void setMaxBonusOdds(BigDecimal maxBonusOdds) {
		this.maxBonusOdds = maxBonusOdds;
	}

	public BigDecimal getMinBonusOdds() {
		return minBonusOdds;
	}

	public void setMinBonusOdds(BigDecimal minBonusOdds) {
		this.minBonusOdds = minBonusOdds;
	}
	
	public BigDecimal getMinRakeback() {
		return minRakeback;
	}

	public void setMinRakeback(BigDecimal minRakeback) {
		this.minRakeback = minRakeback;
	}

	public BigDecimal getMaxRakeback() {
		return maxRakeback;
	}

	public void setMaxRakeback(BigDecimal maxRakeback) {
		this.maxRakeback = maxRakeback;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(Integer maxNumber) {
		this.maxNumber = maxNumber;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getDetailDesc() {
		return detailDesc;
	}

	public void setDetailDesc(String detailDesc) {
		this.detailDesc = detailDesc;
	}

	public String getWinExample() {
		return winExample;
	}

	public void setWinExample(String winExample) {
		this.winExample = winExample;
	}

	public String getPlayMethod() {
		return playMethod;
	}

	public void setPlayMethod(String playMethod) {
		this.playMethod = playMethod;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
