package com.example.anuo.immodule.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAuditListBean {

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

	/**
	 * {
	 * "account":"qqq999",
	 * "applyTime":"2020-10-26 19:28",
	 * "auditStatus":3,
	 * "createTime":1603711698230,
	 * "roomId":"5b119a0cb5a64d778f71daa2109e21be",
	 * "updateTime":1603711723245,
	 * "userId":"dbf66b0cc9454f35b36807cc11365928"
	 * }
	 */
	public class UserAuditItem {

		@SerializedName("createTime")
		private long createTime;

		@SerializedName("auditStatus")
		private int auditStatus;

		@SerializedName("updateTime")
		private long updateTime;

		@SerializedName("applyTime")
		private String applyTime;

		@SerializedName("userId")
		private String userId;

		@SerializedName("account")
		private String account;

		@SerializedName("roomId")
		private String roomId;

		public long getCreateTime(){
			return createTime;
		}

		public int getAuditStatus(){
			return auditStatus;
		}

		public long getUpdateTime(){
			return updateTime;
		}

		public String getApplyTime(){
			return applyTime;
		}

		public String getUserId(){
			return userId;
		}

		public String getAccount(){
			return account;
		}

		public String getRoomId(){
			return roomId;
		}
	}

	public class Msg{

		@SerializedName("listUserAudit")
		private List<UserAuditItem> listUserAudit;

		public List<UserAuditItem> getListUserAudit(){
			return listUserAudit;
		}
	}
}