package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class HostInfo implements  Serializable {
	private String S_Id;//路灯主机的ID
    private String S_FullName;//路灯主机的名称
    private String S_RegPackage;//主机注册包
    private String S_Heart;//主机心跳包
    private String S_Longitude;//主机的经度
    private String S_Latitude;//主机的纬度
    private String S_Phone;//主机为移动网络时的手机号码
    private String S_Address;//主机的地址
    private String S_Description;//主机的备注信息
    private int S_EnabledMark;////是否有效状态
    private String SL_Organize_S_Id;//机构或机构主键的ID
    private String S_CreatorTime;
    private String S_CreatorUserAccount;//
    private String S_LastModifyTime;//
    private String S_LastModifyUserAccount;//
    private String S_LongitudeS_Latitude;//经度纬度
 
	private String S_LoopState;//主机回路状态
    private String S_Voltage;//主机的三相电压
    private String S_Current;//主机三相电流
    private String S_power;//主机三相功率
    private String S_Temperature;//主机温度
    private int S_Online;//主机在线状态

	protected HostInfo(Parcel in) {
		S_Id = in.readString();
		S_FullName = in.readString();
		S_RegPackage = in.readString();
		S_Heart = in.readString();
		S_Longitude = in.readString();
		S_Latitude = in.readString();
		S_Phone = in.readString();
		S_Address = in.readString();
		S_Description = in.readString();
		S_EnabledMark = in.readInt();
		SL_Organize_S_Id = in.readString();
		S_CreatorTime = in.readString();
		S_CreatorUserAccount = in.readString();
		S_LastModifyTime = in.readString();
		S_LastModifyUserAccount = in.readString();
		S_LongitudeS_Latitude = in.readString();
		S_LoopState = in.readString();
		S_Voltage = in.readString();
		S_Current = in.readString();
		S_power = in.readString();
		S_Temperature = in.readString();
		S_Online = in.readInt();
	}


	@Override
	public String toString() {
		return "HostInfo [S_Id=" + S_Id + ", S_FullName=" + S_FullName + ", S_RegPackage=" + S_RegPackage + ", S_Heart="
				+ S_Heart + ", S_Longitude=" + S_Longitude + ", S_Latitude=" + S_Latitude + ", S_Phone=" + S_Phone
				+ ", S_Address=" + S_Address + ", S_Description=" + S_Description + ", S_EnabledMark=" + S_EnabledMark
				+ ", SL_Organize_S_Id=" + SL_Organize_S_Id + ", S_CreatorTime=" + S_CreatorTime
				+ ", S_CreatorUserAccount=" + S_CreatorUserAccount + ", S_LastModifyTime=" + S_LastModifyTime
				+ ", S_LastModifyUserAccount=" + S_LastModifyUserAccount + ", S_LongitudeS_Latitude="
				+ S_LongitudeS_Latitude + ", S_LoopState=" + S_LoopState + ", S_Voltage=" + S_Voltage + ", S_Current="
				+ S_Current + ", S_power=" + S_power + ", S_Temperature=" + S_Temperature + ", S_Online=" + S_Online
				+ "]";
	}
	public HostInfo() {
		super();
	}
	public HostInfo(String s_Id, String s_FullName, String s_RegPackage, String s_Heart, String s_Longitude,
			String s_Latitude, String s_Phone, String s_Address, String s_Description, int s_EnabledMark,
			String sL_Organize_S_Id, String s_CreatorTime, String s_CreatorUserAccount, String s_LastModifyTime,
			String s_LastModifyUserAccount, String s_LongitudeS_Latitude, String s_LoopState, String s_Voltage,
			String s_Current, String s_power, String s_Temperature, int s_Online) {
		super();
		S_Id = s_Id;
		S_FullName = s_FullName;
		S_RegPackage = s_RegPackage;
		S_Heart = s_Heart;
		S_Longitude = s_Longitude;
		S_Latitude = s_Latitude;
		S_Phone = s_Phone;
		S_Address = s_Address;
		S_Description = s_Description;
		S_EnabledMark = s_EnabledMark;
		SL_Organize_S_Id = sL_Organize_S_Id;
		S_CreatorTime = s_CreatorTime;
		S_CreatorUserAccount = s_CreatorUserAccount;
		S_LastModifyTime = s_LastModifyTime;
		S_LastModifyUserAccount = s_LastModifyUserAccount;
		S_LongitudeS_Latitude = s_LongitudeS_Latitude;
		S_LoopState = s_LoopState;
		S_Voltage = s_Voltage;
		S_Current = s_Current;
		S_power = s_power;
		S_Temperature = s_Temperature;
		S_Online = s_Online;
	}
	public String getsS_LongitudeS_Latitude() {
		return S_LongitudeS_Latitude;
	}
	public void setsS_LongitudeS_Latitude(String s_LongitudeS_Latitude) {
		S_LongitudeS_Latitude = s_LongitudeS_Latitude;
	}
	public String getsS_Id() {
		return S_Id;
	}
	public void setsS_Id(String s_Id) {
		S_Id = s_Id;
	}
	public String getsS_FullName() {
		return S_FullName;
	}
	public void setsS_FullName(String s_FullName) {
		S_FullName = s_FullName;
	}
	public String getsS_RegPackage() {
		return S_RegPackage;
	}
	public void setsS_RegPackage(String s_RegPackage) {
		S_RegPackage = s_RegPackage;
	}
	public String getsS_Heart() {
		return S_Heart;
	}
	public void setsS_Heart(String s_Heart) {
		S_Heart = s_Heart;
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
	public String getsS_Phone() {
		return S_Phone;
	}
	public void setsS_Phone(String s_Phone) {
		S_Phone = s_Phone;
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
	public String getsSL_Organize_S_Id() {
		return SL_Organize_S_Id;
	}
	public void setsSL_Organize_S_Id(String sL_Organize_S_Id) {
		SL_Organize_S_Id = sL_Organize_S_Id;
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
	public String getsS_LoopState() {
		return S_LoopState;
	}
	public void setsS_LoopState(String s_LoopState) {
		S_LoopState = s_LoopState;
	}
	public String getsS_Voltage() {
		return S_Voltage;
	}
	public void setsS_Voltage(String s_Voltage) {
		S_Voltage = s_Voltage;
	}
	public String getsS_Current() {
		return S_Current;
	}
	public void setsS_Current(String s_Current) {
		S_Current = s_Current;
	}
	public String getsS_power() {
		return S_power;
	}
	public void setsS_power(String s_power) {
		S_power = s_power;
	}
	public String getsS_Temperature() {
		return S_Temperature;
	}
	public void setsS_Temperature(String s_Temperature) {
		S_Temperature = s_Temperature;
	}
	public int getsS_Online() {
		return S_Online;
	}
	public void setsS_Online(int s_Online) {
		S_Online = s_Online;
	}


}
