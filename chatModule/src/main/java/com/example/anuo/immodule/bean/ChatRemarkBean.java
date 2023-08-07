package com.example.anuo.immodule.bean;

import com.google.gson.annotations.SerializedName;


public class ChatRemarkBean{

	//{"msg":"操作成功。","code":"R0500","success":true,"source":{"msg":"设置备注成功","code":"R0500","success":true,"type":"4"},"status":"b1"}


	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private String code;

	@SerializedName("success")
	private boolean success;

	@SerializedName("source")
	private Source source;

	@SerializedName("status")
	private String status;

	public String getMsg(){
		return msg;
	}

	public String getCode(){
		return code;
	}

	public boolean isSuccess(){
		return success;
	}

	public Source getSource(){
		return source;
	}

	public String getStatus(){
		return status;
	}

	public class Source{

		@SerializedName("msg")
		private String msg;

		@SerializedName("code")
		private String code;

		@SerializedName("success")
		private boolean success;

		@SerializedName("type")
		private String type;

		public String getMsg(){
			return msg;
		}

		public String getCode(){
			return code;
		}

		public boolean isSuccess(){
			return success;
		}

		public String getType(){
			return type;
		}
	}
}