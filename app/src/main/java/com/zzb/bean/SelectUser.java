package com.zzb.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SelectUser implements Serializable {

	private String S_Id; //用户ID

	private String S_Account;//用户账号

	private String S_Password;//用户密码

	private String S_NickName;//用户昵称

	private String S_HeadIcon;//用户头像

	private String S_RealName;//用户姓名

	private String S_MobilePhone;//	用户联系号码

	private String S_Email;//用户邮箱

	private int S_IsAdministrator;

	private int S_EnabledMark;//是否有效状态

	private String S_Description;//用户信息备注

	private String S_CreatorTime; //创建时间戳

	private String S_CreatorUserAccount;

	private String S_LastModifyTime; //时间

	private String S_LastModifyUserAccount;

	private String SL_Organize_S_Id;//属于机构或项目

	private String SL_Role_S_Id;//用户属于的角色

	private int S_USER_RANKID;//用户等级id

	private String S_Project;//用户管理项目

	public String getsS_Id() {
		return S_Id;
	}

	public void setsS_Id(String s_Id) {
		S_Id = s_Id;
	}

	public String getsS_Account() {
		return S_Account;
	}

	public void setsS_Account(String s_Account) {
		S_Account = s_Account;
	}

	public String getsS_Password() {
		return S_Password;
	}

	public void setsS_Password(String s_Password) {
		S_Password = s_Password;
	}

	public String getsS_NickName() {
		return S_NickName;
	}

	public void setsS_NickName(String s_NickName) {
		S_NickName = s_NickName;
	}

	public String getsS_HeadIcon() {
		return S_HeadIcon;
	}

	public void setsS_HeadIcon(String s_HeadIcon) {
		S_HeadIcon = s_HeadIcon;
	}

	public String getsS_RealName() {
		return S_RealName;
	}

	public void setsS_RealName(String s_RealName) {
		S_RealName = s_RealName;
	}

	public String getsS_MobilePhone() {
		return S_MobilePhone;
	}

	public void setsS_MobilePhone(String s_MobilePhone) {
		S_MobilePhone = s_MobilePhone;
	}

	public String getsS_Email() {
		return S_Email;
	}

	public void setsS_Email(String s_Email) {
		S_Email = s_Email;
	}

	public int getsS_IsAdministrator() {
		return S_IsAdministrator;
	}

	public void setsS_IsAdministrator(int s_IsAdministrator) {
		S_IsAdministrator = s_IsAdministrator;
	}

	public int getsS_EnabledMark() {
		return S_EnabledMark;
	}

	public void setsS_EnabledMark(int s_EnabledMark) {
		S_EnabledMark = s_EnabledMark;
	}

	public String getsS_Description() {
		return S_Description;
	}

	public void setsS_Description(String s_Description) {
		S_Description = s_Description;
	}

	public String getsS_CreatorTime() {
		return S_CreatorTime;
	}

	public void setsS_CreatorTime(String s_CreatorTime) {
		S_CreatorTime = s_CreatorTime;
	}

	public String getsS_CreatorUserAccount() {
		return S_CreatorUserAccount;
	}

	public void setsS_CreatorUserAccount(String s_CreatorUserAccount) {
		S_CreatorUserAccount = s_CreatorUserAccount;
	}

	public String getsS_LastModifyTime() {
		return S_LastModifyTime;
	}

	public void setsS_LastModifyTime(String s_LastModifyTime) {
		S_LastModifyTime = s_LastModifyTime;
	}

	public String getsS_LastModifyUserAccount() {
		return S_LastModifyUserAccount;
	}

	public void setsS_LastModifyUserAccount(String s_LastModifyUserAccount) {
		S_LastModifyUserAccount = s_LastModifyUserAccount;
	}

	public String getsSL_Organize_S_Id() {
		return SL_Organize_S_Id;
	}

	public void setsSL_Organize_S_Id(String SL_Organize_S_Id) {
		this.SL_Organize_S_Id = SL_Organize_S_Id;
	}

	public String getsSL_Role_S_Id() {
		return SL_Role_S_Id;
	}

	public void setsSL_Role_S_Id(String SL_Role_S_Id) {
		this.SL_Role_S_Id = SL_Role_S_Id;
	}

	public int getsS_USER_RANKID() {
		return S_USER_RANKID;
	}

	public void setsS_USER_RANKID(int s_USER_RANKID) {
		S_USER_RANKID = s_USER_RANKID;
	}

	public String getsS_Project() {
		return S_Project;
	}

	public void setsS_Project(String s_Project) {
		S_Project = s_Project;
	}

	@Override
	public String toString() {
		return "SelectUser{" +
				"S_Id='" + S_Id + '\'' +
				", S_Account='" + S_Account + '\'' +
				", S_Password='" + S_Password + '\'' +
				", S_NickName='" + S_NickName + '\'' +
				", S_HeadIcon='" + S_HeadIcon + '\'' +
				", S_RealName='" + S_RealName + '\'' +
				", S_MobilePhone='" + S_MobilePhone + '\'' +
				", S_Email='" + S_Email + '\'' +
				", S_IsAdministrator=" + S_IsAdministrator +
				", S_EnabledMark=" + S_EnabledMark +
				", S_Description='" + S_Description + '\'' +
				", S_CreatorTime='" + S_CreatorTime + '\'' +
				", S_CreatorUserAccount='" + S_CreatorUserAccount + '\'' +
				", S_LastModifyTime='" + S_LastModifyTime + '\'' +
				", S_LastModifyUserAccount='" + S_LastModifyUserAccount + '\'' +
				", SL_Organize_S_Id='" + SL_Organize_S_Id + '\'' +
				", SL_Role_S_Id='" + SL_Role_S_Id + '\'' +
				", S_USER_RANKID=" + S_USER_RANKID +
				", S_Project='" + S_Project + '\'' +
				'}';
	}

	public SelectUser() {
	}

	public SelectUser(String s_Id, String s_Account, String s_Password, String s_NickName, String s_HeadIcon, String s_RealName, String s_MobilePhone, String s_Email, int s_IsAdministrator, int s_EnabledMark, String s_Description, String s_CreatorTime, String s_CreatorUserAccount, String s_LastModifyTime, String s_LastModifyUserAccount, String SL_Organize_S_Id, String SL_Role_S_Id, int s_USER_RANKID, String s_Project) {

		S_Id = s_Id;
		S_Account = s_Account;
		S_Password = s_Password;
		S_NickName = s_NickName;
		S_HeadIcon = s_HeadIcon;
		S_RealName = s_RealName;
		S_MobilePhone = s_MobilePhone;
		S_Email = s_Email;
		S_IsAdministrator = s_IsAdministrator;
		S_EnabledMark = s_EnabledMark;
		S_Description = s_Description;
		S_CreatorTime = s_CreatorTime;
		S_CreatorUserAccount = s_CreatorUserAccount;
		S_LastModifyTime = s_LastModifyTime;
		S_LastModifyUserAccount = s_LastModifyUserAccount;
		this.SL_Organize_S_Id = SL_Organize_S_Id;
		this.SL_Role_S_Id = SL_Role_S_Id;
		S_USER_RANKID = s_USER_RANKID;
		S_Project = s_Project;
	}
}
