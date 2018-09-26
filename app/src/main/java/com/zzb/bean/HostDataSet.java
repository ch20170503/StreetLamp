package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class HostDataSet implements  Serializable {

    private String S_RegPackage;//注册包
    private String S_Version;//
    private String S_LoopState;//主机回路状态
    private String S_Voltage;//主机的三相电压
    private String S_Current;//主机三相电流
    private String S_power;//主机三相功率
    private String S_Temperature;//主机温度
    private String S_Longitude;//经度
    private String S_Latitude;//纬度
    private String S_TimeZone;
    private int S_Online;//主机在线状态
    private String S_TimeStamp;//主机更新的时间戳
    private String S_UpdateTime;//主机信息的最后更新时间
    private String SL_Organize_S_Id;//所属项目
	private String S_Fname;

	public HostDataSet() {
		super();
	}

	public HostDataSet(String s_RegPackage, String s_Version, String s_LoopState, String s_Voltage, String s_Current, String s_power, String s_Temperature, String s_Longitude, String s_Latitude, String s_TimeZone, int s_Online, String s_TimeStamp, String s_UpdateTime, String SL_Organize_S_Id, String s_Fname) {
		S_RegPackage = s_RegPackage;
		S_Version = s_Version;
		S_LoopState = s_LoopState;
		S_Voltage = s_Voltage;
		S_Current = s_Current;
		S_power = s_power;
		S_Temperature = s_Temperature;
		S_Longitude = s_Longitude;
		S_Latitude = s_Latitude;
		S_TimeZone = s_TimeZone;
		S_Online = s_Online;
		S_TimeStamp = s_TimeStamp;
		S_UpdateTime = s_UpdateTime;
		this.SL_Organize_S_Id = SL_Organize_S_Id;
		S_Fname = s_Fname;
	}

	@Override
	public String toString() {
		return "HostDataSet{" +
				"S_RegPackage='" + S_RegPackage + '\'' +
				", S_Version='" + S_Version + '\'' +
				", S_LoopState='" + S_LoopState + '\'' +
				", S_Voltage='" + S_Voltage + '\'' +
				", S_Current='" + S_Current + '\'' +
				", S_power='" + S_power + '\'' +
				", S_Temperature='" + S_Temperature + '\'' +
				", S_Longitude='" + S_Longitude + '\'' +
				", S_Latitude='" + S_Latitude + '\'' +
				", S_TimeZone='" + S_TimeZone + '\'' +
				", S_Online=" + S_Online +
				", S_TimeStamp='" + S_TimeStamp + '\'' +
				", S_UpdateTime='" + S_UpdateTime + '\'' +
				", SL_Organize_S_Id='" + SL_Organize_S_Id + '\'' +
				", S_Fname='" + S_Fname + '\'' +
				'}';
	}

	public String getsS_RegPackage() {
		return S_RegPackage;
	}

	public void setsS_RegPackage(String s_RegPackage) {
		S_RegPackage = s_RegPackage;
	}

	public String getsS_Version() {
		return S_Version;
	}

	public void setsS_Version(String s_Version) {
		S_Version = s_Version;
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

	public String getsS_TimeZone() {
		return S_TimeZone;
	}

	public void setsS_TimeZone(String s_TimeZone) {
		S_TimeZone = s_TimeZone;
	}

	public int getsS_Online() {
		return S_Online;
	}

	public void setsS_Online(int s_Online) {
		S_Online = s_Online;
	}

	public String getsS_TimeStamp() {
		return S_TimeStamp;
	}

	public void setsS_TimeStamp(String s_TimeStamp) {
		S_TimeStamp = s_TimeStamp;
	}

	public String getsS_UpdateTime() {
		return S_UpdateTime;
	}

	public void setsS_UpdateTime(String s_UpdateTime) {
		S_UpdateTime = s_UpdateTime;
	}

	public String getSL_Organize_S_Id() {
		return SL_Organize_S_Id;
	}

	public void setSL_Organize_S_Id(String SL_Organize_S_Id) {
		this.SL_Organize_S_Id = SL_Organize_S_Id;
	}

	public String getsS_Fname() {
		return S_Fname;
	}

	public void setsS_Fname(String s_Fname) {
		S_Fname = s_Fname;
	}
}
