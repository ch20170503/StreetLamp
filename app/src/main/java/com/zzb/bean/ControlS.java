package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ControlS implements  Serializable {
    private String S_Id; //分控主机
    private int S_Number; //分控器编号
    private String S_FullName;//分控器名称
    private String S_PoleName;//绑定的灯杆名
    private String S_Address;//分控地址
    private String S_Description;//分控器的信息备注
    private int S_EnabledMark;//是否有效状态
    private String S_Longitude;//主机的经度
    private String S_Latitude;//主机的纬度
    private String S_CreatorTime;
    private String S_CreatorUserAccount;//
    private String S_LastModifyTime;//
    private String S_LastModifyUserAccount;//
    private String SL_HostBast_RegPackage;//分控对应的主机注册包

	protected ControlS(Parcel in) {
		S_Id = in.readString();
		S_Number = in.readInt();
		S_FullName = in.readString();
		S_PoleName = in.readString();
		S_Address = in.readString();
		S_Description = in.readString();
		S_EnabledMark = in.readInt();
		S_Longitude = in.readString();
		S_Latitude = in.readString();
		S_CreatorTime = in.readString();
		S_CreatorUserAccount = in.readString();
		S_LastModifyTime = in.readString();
		S_LastModifyUserAccount = in.readString();
		SL_HostBast_RegPackage = in.readString();
	}


	@Override
	public String toString() {
		return "Control [S_Id=" + S_Id + ", S_Number=" + S_Number + ", S_FullName=" + S_FullName + ", S_PoleName="
				+ S_PoleName + ", S_Address=" + S_Address + ", S_Description=" + S_Description + ", S_EnabledMark="
				+ S_EnabledMark + ", S_Longitude=" + S_Longitude + ", S_Latitude=" + S_Latitude + ", S_CreatorTime="
				+ S_CreatorTime + ", S_CreatorUserAccount=" + S_CreatorUserAccount + ", S_LastModifyTime="
				+ S_LastModifyTime + ", S_LastModifyUserAccount=" + S_LastModifyUserAccount
				+ ", SL_HostBast_RegPackage=" + SL_HostBast_RegPackage + "]";
	}
	public ControlS() {
		super();
	}
	public ControlS(String s_Id, int s_Number, String s_FullName, String s_PoleName, String s_Address,
			String s_Description, int s_EnabledMark, String s_Longitude, String s_Latitude, String s_CreatorTime,
			String s_CreatorUserAccount, String s_LastModifyTime, String s_LastModifyUserAccount,
			String sL_HostBast_RegPackage) {
		super();
		S_Id = s_Id;
		S_Number = s_Number;
		S_FullName = s_FullName;
		S_PoleName = s_PoleName;
		S_Address = s_Address;
		S_Description = s_Description;
		S_EnabledMark = s_EnabledMark;
		S_Longitude = s_Longitude;
		S_Latitude = s_Latitude;
		S_CreatorTime = s_CreatorTime;
		S_CreatorUserAccount = s_CreatorUserAccount;
		S_LastModifyTime = s_LastModifyTime;
		S_LastModifyUserAccount = s_LastModifyUserAccount;
		SL_HostBast_RegPackage = sL_HostBast_RegPackage;
	}
	public String getsS_Id() {
		return S_Id;
	}
	public void setsS_Id(String s_Id) {
		S_Id = s_Id;
	}
	public int getsS_Number () {
		return S_Number;
	}
	public void setsS_Number(int s_Number) {
		S_Number = s_Number;
	}
	public String getsS_FullName() {
		return S_FullName;
	}
	public void setsS_FullName(String s_FullName) {
		S_FullName = s_FullName;
	}
	public String getsS_PoleName() {
		return S_PoleName;
	}
	public void setsS_PoleName(String s_PoleName) {
		S_PoleName = s_PoleName;
	}
	public String getsS_Address() {
		return S_Address;
	}
	public void setsS_Address(String s_Address) {
		S_Address = s_Address;
	}
	public String getsS_Description() {
		return S_Description;
	}
	public void setsS_Description(String s_Description) {
		S_Description = s_Description;
	}
	public int getsS_EnabledMark() {
		return S_EnabledMark;
	}
	public void setsS_EnabledMark(int s_EnabledMark) {
		S_EnabledMark = s_EnabledMark;
	}
	public String getsS_Longitude() {
		return S_Longitude;
	}
	public void setsS_Longitude(String s_Longitude) {
		S_Longitude = s_Longitude;
	}
	public String getsS_Latitude() {
		return S_Latitude;
	}
	public void setsS_Latitude(String s_Latitude) {
		S_Latitude = s_Latitude;
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
	public String getsSL_HostBast_RegPackage() {
		return SL_HostBast_RegPackage;
	}
	public void setsSL_HostBast_RegPackage(String sL_HostBast_RegPackage) {
		SL_HostBast_RegPackage = sL_HostBast_RegPackage;
	}


}
