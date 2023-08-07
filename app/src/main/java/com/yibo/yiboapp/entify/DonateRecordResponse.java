package com.yibo.yiboapp.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DonateRecordResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("msg")
	private String msg;

	@SerializedName("content")
	private DonateContent donateContent;

	public boolean isSuccess(){
		return success;
	}

	public String getMsg() {
		return msg;
	}

	public DonateContent getContent(){
		return donateContent;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setContent(DonateContent donateContent) {
		this.donateContent = donateContent;
	}

	public class DonateContent {

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
		private List<DonateRecord> list;

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

		public List<DonateRecord> getList(){
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

		public void setPrePage(int prePage) {
			this.prePage = prePage;
		}

		public void setNextPage(int nextPage) {
			this.nextPage = nextPage;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public void setTotalPageCount(int totalPageCount) {
			this.totalPageCount = totalPageCount;
		}

		public void setHasNext(boolean hasNext) {
			this.hasNext = hasNext;
		}

		public void setList(List<DonateRecord> list) {
			this.list = list;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public void setHasPre(boolean hasPre) {
			this.hasPre = hasPre;
		}

		public void setCurrentPageNo(int currentPageNo) {
			this.currentPageNo = currentPageNo;
		}
	}

	public class DonateRecord{

		@SerializedName("createDatetime")
		private String createDatetime;

		@SerializedName("accountId")
		private int accountId;

		@SerializedName("donateMoney")
		private int donateMoney;

		@SerializedName("modifyDatetime")
		private String modifyDatetime;

		@SerializedName("modifyAccount")
		private String modifyAccount;

		@SerializedName("remark")
		private String remark;

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

		public int getDonateMoney(){
			return donateMoney;
		}

		public String getModifyDatetime(){
			return modifyDatetime;
		}

		public String getModifyAccount(){
			return modifyAccount;
		}

		public String getRemark(){
			return remark;
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

		public void setCreateDatetime(String createDatetime) {
			this.createDatetime = createDatetime;
		}

		public void setAccountId(int accountId) {
			this.accountId = accountId;
		}

		public void setDonateMoney(int donateMoney) {
			this.donateMoney = donateMoney;
		}

		public void setModifyDatetime(String modifyDatetime) {
			this.modifyDatetime = modifyDatetime;
		}

		public void setModifyAccount(String modifyAccount) {
			this.modifyAccount = modifyAccount;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public void setStationId(int stationId) {
			this.stationId = stationId;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}
}