/**
  * Copyright 2017 bejson.com 
  */
package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Auto-generated: 2017-06-17 8:42:38
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class User implements Serializable {

	private int code;
	private String message;
	private UserDate date;
	public class UserDate {
		private String UserID;
		private String OrganizeId;
		private String RoleId;
		private int SL_USER_RANKID;
		private String ApiKey;
		public String getUserID() {
			return UserID;
		}
		public void setUserID(String userID) {
			UserID = userID;
		}
		public String getOrganizeId() {
			return OrganizeId;
		}
		public void setOrganizeId(String organizeId) {
			OrganizeId = organizeId;
		}
		public String getRoleId() {
			return RoleId;
		}
		public void setRoleId(String roleId) {
			RoleId = roleId;
		}
		public int getSL_USER_RANKID() {
			return SL_USER_RANKID;
		}
		public void setSL_USER_RANKID(int SL_USER_RANKID) {
			this.SL_USER_RANKID = SL_USER_RANKID;
		}
		public String getApiKey() {
			return ApiKey;
		}
		public void setApiKey(String apiKey) {
			ApiKey = apiKey;
		}
		@Override
		public String toString() {
			return "UserDate [UserID=" + UserID + ", OrganizeId=" + OrganizeId + ", RoleId=" + RoleId + ", SL_USER_RANKID="
					+ SL_USER_RANKID + ", ApiKey=" + ApiKey + "]";
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

	@Override
	public String toString() {
		return "User [code=" + code + ", message=" + message + ", date=" + date + "]";
	}

	public UserDate getdate() {
		return date;
	}

	public void setdate(UserDate date) {
		this.date = date;
	}



}