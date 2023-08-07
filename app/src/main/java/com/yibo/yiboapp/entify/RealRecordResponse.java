package com.yibo.yiboapp.entify;

import java.util.List;

public class RealRecordResponse{
	private double sumWin;
	private int code;
	private List<RealRecordDetail> data;
	private double sumBet;
	private double sumRealBet;
	private String message;

	public double getSumWin(){
		return sumWin;
	}

	public int getCode(){
		return code;
	}

	public List<RealRecordDetail> getData(){
		return data;
	}

	public double getSumBet(){
		return sumBet;
	}

	public double getSumRealBet() {
		return sumRealBet;
	}

	public String getMessage(){
		return message;
	}

	public class RealRecordDetail {
		private String gameType;
		private double bettingMoney;
		private double winMoney;
		private String orderId;
		private String thirdExtInfo;
		private String platformType;
		private String md5Str;
		private int type;
		private int serverId;
		private int staId;
		private double netAmountBonus;
		private String loginIp;
		private int id;
		private String thirdMemberAccount;
		private double realBettingMoney;
		private int stationId;
		private long bettingTime;
		private double betAmountBonus;
		private long createDatetime;
		private String gameTypeCode;
		private int accountId;
		private String bettingContent;
		private String bettingCode;
		private long bettingTimeGmt4;
		private int thirdMemberId;
		private String gameCode;
		private String account;
		private String parents;

		public String getGameType(){
			return gameType;
		}

		public double getBettingMoney(){
			return bettingMoney;
		}

		public double getWinMoney(){
			return winMoney;
		}

		public String getOrderId(){
			return orderId;
		}

		public String getThirdExtInfo(){
			return thirdExtInfo;
		}

		public String getPlatformType(){
			return platformType;
		}

		public String getMd5Str(){
			return md5Str;
		}

		public int getType(){
			return type;
		}

		public int getServerId(){
			return serverId;
		}

		public int getStaId(){
			return staId;
		}

		public double getNetAmountBonus(){
			return netAmountBonus;
		}

		public String getLoginIp(){
			return loginIp;
		}

		public int getId(){
			return id;
		}

		public String getThirdMemberAccount(){
			return thirdMemberAccount;
		}

		public double getRealBettingMoney(){
			return realBettingMoney;
		}

		public int getStationId(){
			return stationId;
		}

		public long getBettingTime(){
			return bettingTime;
		}

		public double getBetAmountBonus(){
			return betAmountBonus;
		}

		public long getCreateDatetime(){
			return createDatetime;
		}

		public String getGameTypeCode(){
			return gameTypeCode;
		}

		public int getAccountId(){
			return accountId;
		}

		public String getBettingContent(){
			return bettingContent;
		}

		public String getBettingCode(){
			return bettingCode;
		}

		public long getBettingTimeGmt4(){
			return bettingTimeGmt4;
		}

		public int getThirdMemberId(){
			return thirdMemberId;
		}

		public String getGameCode(){
			return gameCode;
		}

		public String getAccount(){
			return account;
		}

		public String getParents(){
			return parents;
		}
	}
}