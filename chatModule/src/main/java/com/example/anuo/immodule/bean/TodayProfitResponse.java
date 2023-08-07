package com.example.anuo.immodule.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TodayProfitResponse{

	@SerializedName("msg")
	private Msg msg;

	@SerializedName("code")
	private String code;

	@SerializedName("success")
	private boolean success;

	@SerializedName("status")
	private String status;

	public Msg getMsg(){
		return msg;
	}

	public String getCode(){
		return code;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getStatus(){
		return status;
	}

	public static class Msg{

		@SerializedName("winningList")
		private List<Prize> winningList;

		@SerializedName("prizeType")
		private String prizeType;

		public List<Prize> getWinningList(){
			return winningList;
		}

		public String getPrizeType(){
			return prizeType;
		}
	}

	public static class Prize {

		@SerializedName("userLevelName")
		private String userLevelName;

		@SerializedName("prizeDate")
		private long prizeDate;

		@SerializedName("prizeMoney")
		private double prizeMoney;

		@SerializedName("prizeType")
		private String prizeType;

		@SerializedName("id")
		private String id;

		@SerializedName("prizeProject")
		private String prizeProject;

		@SerializedName("userName")
		private String userName;

		@SerializedName("stationId")
		private String stationId;

		public String getUserLevelName(){
			return userLevelName;
		}

		public long getPrizeDate(){
			return prizeDate;
		}

		public double getPrizeMoney(){
			return prizeMoney;
		}

		public String getPrizeType(){
			return prizeType;
		}

		public String getId(){
			return id;
		}

		public String getPrizeProject(){
			return prizeProject;
		}

		public String getUserName(){
			return userName;
		}

		public String getStationId(){
			return stationId;
		}
	}
}