package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Group implements  Serializable {
    private String S_Id;//分组id
    private String S_GroupName;//分组名称
    private int S_Type;//分组类型
    private int S_Number;//分组编号
    private String S_GroupContent;//分组内容
    private int S_EnabledMark;//是否有效状态
    private String S_Description;//分组信息备注
    private String S_CreatorTime;
    private String S_CreatorUserAccount;
    private String S_LastModifyTime;
    private String S_LastModifyUserAccount;
    private String SL_Organize_S_Id;//机构或机构主键的ID
    private String SL_HostBase_S_RegPackage;//主机信息表的注册包

	protected Group(Parcel in) {
		S_Id = in.readString();
		S_GroupName = in.readString();
		S_Type = in.readInt();
		S_Number = in.readInt();
		S_GroupContent = in.readString();
		S_EnabledMark = in.readInt();
		S_Description = in.readString();
		S_CreatorTime = in.readString();
		S_CreatorUserAccount = in.readString();
		S_LastModifyTime = in.readString();
		S_LastModifyUserAccount = in.readString();
		SL_Organize_S_Id = in.readString();
		SL_HostBase_S_RegPackage = in.readString();
	}



	@Override
	public String toString() {
		return "Group [S_Id=" + S_Id + ", S_GroupName=" + S_GroupName + ", S_Type=" + S_Type + ", S_Number=" + S_Number
				+ ", S_GroupContent=" + S_GroupContent + ", S_EnabledMark=" + S_EnabledMark + ", S_Description="
				+ S_Description + ", S_CreatorTime=" + S_CreatorTime + ", S_CreatorUserAccount=" + S_CreatorUserAccount
				+ ", S_LastModifyTime=" + S_LastModifyTime + ", S_LastModifyUserAccount=" + S_LastModifyUserAccount
				+ ", SL_Organize_S_Id=" + SL_Organize_S_Id + ", SL_HostBase_S_RegPackage=" + SL_HostBase_S_RegPackage
				+ "]";
	}
	public Group() {
		super();
	}
	public Group(String s_Id, String s_GroupName, int s_Type, int s_Number, String s_GroupContent, int s_EnabledMark,
			String s_Description, String s_CreatorTime, String s_CreatorUserAccount, String s_LastModifyTime,
			String s_LastModifyUserAccount, String sL_Organize_S_Id, String sL_HostBase_S_RegPackage) {
		super();
		S_Id = s_Id;
		S_GroupName = s_GroupName;
		S_Type = s_Type;
		S_Number = s_Number;
		S_GroupContent = s_GroupContent;
		S_EnabledMark = s_EnabledMark;
		S_Description = s_Description;
		S_CreatorTime = s_CreatorTime;
		S_CreatorUserAccount = s_CreatorUserAccount;
		S_LastModifyTime = s_LastModifyTime;
		S_LastModifyUserAccount = s_LastModifyUserAccount;
		SL_Organize_S_Id = sL_Organize_S_Id;
		SL_HostBase_S_RegPackage = sL_HostBase_S_RegPackage;
	}
	public String getsS_Id() {
		return S_Id;
	}
	public void setsS_Id(String s_Id) {
		S_Id = s_Id;
	}
	public String getsS_GroupName() {
		return S_GroupName;
	}
	public void setsS_GroupName(String s_GroupName) {
		S_GroupName = s_GroupName;
	}
	public int getsS_Type() {
		return S_Type;
	}
	public void setsS_Type(int s_Type) {
		S_Type = s_Type;
	}
	public int getsS_Number() {
		return S_Number;
	}
	public void setsS_Number(int s_Number) {
		S_Number = s_Number;
	}
	public String getsS_GroupContent() {
		return S_GroupContent;
	}
	public void setsS_GroupContent(String s_GroupContent) {
		S_GroupContent = s_GroupContent;
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
	public void setsSL_Organize_S_Id(String sL_Organize_S_Id) {
		SL_Organize_S_Id = sL_Organize_S_Id;
	}
	public String getsSL_HostBase_S_RegPackage() {
		return SL_HostBase_S_RegPackage;
	}
	public void setsSL_HostBase_S_RegPackage(String sL_HostBase_S_RegPackage) {
		SL_HostBase_S_RegPackage = sL_HostBase_S_RegPackage;
	}
}
