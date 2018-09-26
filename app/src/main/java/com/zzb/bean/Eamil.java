package com.zzb.bean;

import android.os.Parcel;


import java.io.Serializable;

public class Eamil implements Serializable {
	 private int code;
	 private String message;
	 private EamilDate date;

	protected Eamil(Parcel in) {
		code = in.readInt();
		message = in.readString();
	}




	public class EamilDate {
		  private String S_Account;//用户名
		  private String S_Blacklist;//黑名单
		  private int S_Type;//类型
		public String getS_Account() {
			return S_Account;
		}
		public void setS_Account(String s_Account) {
			S_Account = s_Account;
		}
		public String getS_Blacklist() {
			return S_Blacklist;
		}
		public void setS_Blacklist(String s_Blacklist) {
			S_Blacklist = s_Blacklist;
		}
		public int getS_Type() {
			return S_Type;
		}
		public void setS_Type(int s_Type) {
			S_Type = s_Type;
		}
		public EamilDate(String s_Account, String s_Blacklist, int s_Type) {
			super();
			S_Account = s_Account;
			S_Blacklist = s_Blacklist;
			S_Type = s_Type;
		}
		public EamilDate() {
			super();
		}
		@Override
		public String toString() {
			return "EamilDate [S_Account=" + S_Account + ", S_Blacklist=" + S_Blacklist + ", S_Type=" + S_Type + "]";
		}
		  
		}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public EamilDate getDate() {
		return date;
	}
	public void setDate(EamilDate date) {
		this.date = date;
	}
	public Eamil() {
		super();
	}
	public Eamil(int code, String message, EamilDate date) {
		super();
		this.code = code;
		this.message = message;
		this.date = date;
	}
	@Override
	public String toString() {
		return "Eamil [code=" + code + ", message=" + message + ", date=" + date + "]";
	}
	 
}
