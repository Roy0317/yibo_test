package com.yibo.yiboapp.data;

import com.yibo.yiboapp.utils.Utils;


public enum LotteryEnum {
	BJSC("北京赛车", 10, 179, "5分钟"), XYFT("幸运飞艇", 10, 180, "5分钟"), SFSC("极速赛车", 10, 480, "3分钟", true),

	SH11X5("上海11选5", 5, 90, "10分钟"), JX11X5("江西11选5", 5, 84, "10分钟"), SD11X5("山东11选5", 5, 87, "10分钟"), GD11X5("广东11选5",
			5, 84, "10分钟"),

	PL3("排列三", 3, 1, "24小时"), FC3D("福彩3D", 3, 1, "24小时"),

	CQSSC("重庆时时彩", 5, 120, "5/10分钟"), XJSSC("新疆时时彩", 5, 96, "10分钟"), TJSSC("天津时时彩", 5, 84, "10分钟"),

	FFC("分分彩", 5, 1440, "1分钟", true), EFC("二分彩", 5, 720, "2分钟", true), WFC("五分彩", 5, 288, "5分钟", true),

	LHC("六合彩", 7, 1, "二 四 六"), SFLHC("10分六合彩", 7, 144, "10分钟", true),

	HNKLSF("湖南快乐十分", 8, 84, "10分钟"), GDKLSF("广东快乐十分", 8, 84, "10分钟"), CQXYNC("重庆幸运农场", 8, 97, "10分钟"),

	JSSB3("江苏骰宝(快3)", 3, 82, "10分钟"), AHK3("安徽快三", 3, 80, "10分钟"), HBK3("湖北快三", 3, 78, "10分钟")

	, HEBK3("河北快三", 3, 81, "10分钟"), GXK3("广西快三", 3, 78, "10分钟"), SHHK3("上海快3", 3, 82, "10分钟")

	, BJK3("北京快三", 3, 89, "10分钟"), JXK3("江西快三", 3, 84, "10分钟"), GSK3("甘肃快三", 3, 72, "10分钟")

	, FFK3("极速快三", 3, 1440, "1分钟", true), WFK3("幸运快三", 3, 288, "5分钟", true)

	, PCEGG("PC蛋蛋", 3, 179, "5分钟"), JND28("加拿大28", 3, 396, "3分钟半");

	private String lotName;// 彩票名称
	private Integer ballNums;// 球数
	private Integer qiShu;// 一天期数
	private String pinLv; // 开奖频率
	private boolean sysLot = false;// 系统彩

	private LotteryEnum(String lotName, Integer ballNums, Integer qiShu, String pinLv) {
		this.lotName = lotName;
		this.ballNums = ballNums;
		this.qiShu = qiShu;
		this.pinLv = pinLv;
		this.sysLot = false;
	}

	private LotteryEnum(String lotName, Integer ballNums, Integer qiShu, String pinLv, boolean sysLot) {
		this.lotName = lotName;
		this.ballNums = ballNums;
		this.qiShu = qiShu;
		this.pinLv = pinLv;
		this.sysLot = sysLot;
	}

	public String getLotName() {
		return lotName;
	}

	public Integer getBallNums() {
		return ballNums;
	}

	public Integer getQiShu() {
		return qiShu;
	}

	public String getPinLv() {
		return pinLv;
	}

	public boolean isSysLot() {
		return sysLot;
	}

	public static boolean isSysLot(String name) {
		if (Utils.isEmptyString(name))
			return false;
		try {
			LotteryEnum le = LotteryEnum.valueOf(name.toUpperCase());
			if (le != null) {
				return le.isSysLot();
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static LotteryEnum getEnum(String name) {
		if (Utils.isEmptyString(name))
			return null;
		try {
			return LotteryEnum.valueOf(name.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}
}
