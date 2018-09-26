package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AddUserInfo implements Serializable {
	private  String account; //用户账号
	private  String password;//用户密码
	private  String nickName;//用户昵称
	private  String headIcon;//用户头像
	private  String realName;//用户姓名
	private  String mobilePhone;//	用户联系号码
	private  String email;//用户邮箱
	private  int enabledMark;//是否有效状态
	private  String description;//用户信息备注
	private  String organize_Id;//属于机构或项目
	private  String role_Id;//用户属于的角色
	private  int uSER_RANKID;//用户等级id
	private  String Authorization;//签名校正码和访问令牌，用空格隔开
	private  String roleid;//角色权限ID
	private  String userid;//用户帐号ID
	private  String organizeid;//机构ID
	private  String timestamp;//时间戳

	protected AddUserInfo(Parcel in) {
		account = in.readString();
		password = in.readString();
		nickName = in.readString();
		headIcon = in.readString();
		realName = in.readString();
		mobilePhone = in.readString();
		email = in.readString();
		enabledMark = in.readInt();
		description = in.readString();
		organize_Id = in.readString();
		role_Id = in.readString();
		uSER_RANKID = in.readInt();
		Authorization = in.readString();
		roleid = in.readString();
		userid = in.readString();
		organizeid = in.readString();
		timestamp = in.readString();
	}



	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadIcon() {
		return headIcon;
	}
	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getEnabledMark() {
		return enabledMark;
	}
	public void setEnabledMark(int enabledMark) {
		this.enabledMark = enabledMark;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOrganize_Id() {
		return organize_Id;
	}
	public void setOrganize_Id(String organize_Id) {
		this.organize_Id = organize_Id;
	}
	public String getRole_Id() {
		return role_Id;
	}
	public void setRole_Id(String role_Id) {
		this.role_Id = role_Id;
	}
	public int getuSER_RANKID() {
		return uSER_RANKID;
	}
	public void setuSER_RANKID(int uSER_RANKID) {
		this.uSER_RANKID = uSER_RANKID;
	}
	public String getAuthorization() {
		return Authorization;
	}
	public void setAuthorization(String authorization) {
		Authorization = authorization;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getOrganizeid() {
		return organizeid;
	}
	public void setOrganizeid(String organizeid) {
		this.organizeid = organizeid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public AddUserInfo(String account, String password, String nickName, String headIcon, String realName,
			String mobilePhone, String email, int enabledMark, String description, String organize_Id, String role_Id,
			int uSER_RANKID, String authorization, String roleid, String userid, String organizeid, String timestamp) {
		super();
		this.account = account;
		this.password = password;
		this.nickName = nickName;
		this.headIcon = headIcon;
		this.realName = realName;
		this.mobilePhone = mobilePhone;
		this.email = email;
		this.enabledMark = enabledMark;
		this.description = description;
		this.organize_Id = organize_Id;
		this.role_Id = role_Id;
		this.uSER_RANKID = uSER_RANKID;
		Authorization = authorization;
		this.roleid = roleid;
		this.userid = userid;
		this.organizeid = organizeid;
		this.timestamp = timestamp;
	}
	public AddUserInfo() {
		super();
	}
	@Override
	public String toString() {
		return "AddUserInfo [account=" + account + ", password=" + password + ", nickName=" + nickName + ", headIcon="
				+ headIcon + ", realName=" + realName + ", mobilePhone=" + mobilePhone + ", email=" + email
				+ ", enabledMark=" + enabledMark + ", description=" + description + ", organize_Id=" + organize_Id
				+ ", role_Id=" + role_Id + ", uSER_RANKID=" + uSER_RANKID + ", Authorization=" + Authorization
				+ ", roleid=" + roleid + ", userid=" + userid + ", organizeid=" + organizeid + ", timestamp="
				+ timestamp + "]";
	}

}
