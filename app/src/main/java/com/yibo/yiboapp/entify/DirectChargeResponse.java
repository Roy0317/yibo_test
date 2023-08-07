package com.yibo.yiboapp.entify;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DirectChargeResponse{

	@SerializedName("prePage")
	private int prePage;

	@SerializedName("nextPage")
	private int nextPage;

	@SerializedName("start")
	private int start;

	@SerializedName("pageSize")
	private int pageSize;

	@SerializedName("totalPageCount")
	private int totalPageCount;

	@SerializedName("hasNext")
	private boolean hasNext;

	@SerializedName("list")
	private List<DCRecord> list;

	@SerializedName("totalCount")
	private int totalCount;

	@SerializedName("hasPre")
	private boolean hasPre;

	@SerializedName("currentPageNo")
	private int currentPageNo;

	public int getPrePage(){
		return prePage;
	}

	public int getNextPage(){
		return nextPage;
	}

	public int getStart(){
		return start;
	}

	public int getPageSize(){
		return pageSize;
	}

	public int getTotalPageCount(){
		return totalPageCount;
	}

	public boolean isHasNext(){
		return hasNext;
	}

	public List<DCRecord> getList(){
		return list;
	}

	public int getTotalCount(){
		return totalCount;
	}

	public boolean isHasPre(){
		return hasPre;
	}

	public int getCurrentPageNo(){
		return currentPageNo;
	}

	public class DCRecord{

		@SerializedName("createDatetime")
		private String createDatetime;

		@SerializedName("accountId")
		private int accountId;

		@SerializedName("giftMoney")
		private double giftMoney;

		@SerializedName("betRate")
		private double betRate;

		@SerializedName("giftMoneyPercent")
		private double giftMoneyPercent;

		@SerializedName("money")
		private double money;

		@SerializedName("modifyDatetime")
		private String modifyDatetime;

		@SerializedName("modifyAccount")
		private String modifyAccount;

		@SerializedName("id")
		private int id;

		@SerializedName("account")
		private String account;

		@SerializedName("stationId")
		private int stationId;

		@SerializedName("status")
		private int status;

		public String getCreateDatetime(){
			return createDatetime;
		}

		public int getAccountId(){
			return accountId;
		}

		public double getGiftMoney(){
			return giftMoney;
		}

		public double getBetRate(){
			return betRate;
		}

		public double getGiftMoneyPercent(){
			return giftMoneyPercent;
		}

		public double getMoney(){
			return money;
		}

		public String getModifyDatetime(){
			return modifyDatetime;
		}

		public String getModifyAccount(){
			return modifyAccount;
		}

		public int getId(){
			return id;
		}

		public String getAccount(){
			return account;
		}

		public int getStationId(){
			return stationId;
		}

		public int getStatus(){
			return status;
		}
	}
}