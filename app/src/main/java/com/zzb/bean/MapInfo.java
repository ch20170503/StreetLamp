package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MapInfo implements Serializable {
	  private String S_FullName;//路灯主机的名称
	  private String S_RegPackage;//主机注册包
	  private String S_Longitude;//主机的经度
	  private String S_Latitude;//主机的纬度
	  private String SL_Organize_S_Id;//机构或机构主键的ID
	  private int S_Online;//主机在线状态
	  private String S_Number; //分控器编号
	  private String CS_FullName;//分控器名称
	  private String CS_Longitude;//分控的经度
	  private String CS_Latitude;//分控的纬度
	  private String S_LoopState;//主机回路状态
	  private int S_LightHD;//灯具调光口


	protected MapInfo(Parcel in) {
		S_FullName = in.readString();
		S_RegPackage = in.readString();
		S_Longitude = in.readString();
		S_Latitude = in.readString();
		SL_Organize_S_Id = in.readString();
		S_Online = in.readInt();
		S_Number = in.readString();
		CS_FullName = in.readString();
		CS_Longitude = in.readString();
		CS_Latitude = in.readString();
		S_LoopState = in.readString();
		S_LightHD = in.readInt();
	}



	@Override
	public String toString() {
		return "MapInfo [S_FullName=" + S_FullName + ", S_RegPackage=" + S_RegPackage + ", S_Longitude=" + S_Longitude
				+ ", S_Latitude=" + S_Latitude + ", SL_Organize_S_Id=" + SL_Organize_S_Id + ", S_Online=" + S_Online
				+ ", S_Number=" + S_Number + ", CS_FullName=" + CS_FullName + ", CS_Longitude=" + CS_Longitude
				+ ", CS_Latitude=" + CS_Latitude + ", S_LoopState=" + S_LoopState + ", S_LightHD=" + S_LightHD + "]";
	}
	public MapInfo() {
		super();
	}
	public MapInfo(String s_FullName, String s_RegPackage, String s_Longitude, String s_Latitude,
			String sL_Organize_S_Id, int s_Online, String s_Number, String cS_FullName, String cS_Longitude,
			String cS_Latitude, String s_LoopState, int s_LightHD) {
		super();
		S_FullName = s_FullName;
		S_RegPackage = s_RegPackage;
		S_Longitude = s_Longitude;
		S_Latitude = s_Latitude;
		SL_Organize_S_Id = sL_Organize_S_Id;
		S_Online = s_Online;
		S_Number = s_Number;
		CS_FullName = cS_FullName;
		CS_Longitude = cS_Longitude;
		CS_Latitude = cS_Latitude;
		S_LoopState = s_LoopState;
		S_LightHD = s_LightHD;
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
	public String getsSL_Organize_S_Id() {
		return SL_Organize_S_Id;
	}
	public void setsSL_Organize_S_Id(String sL_Organize_S_Id) {
		SL_Organize_S_Id = sL_Organize_S_Id;
	}
	public int getsS_Online() {
		return S_Online;
	}
	public void setsS_Online(int s_Online) {
		S_Online = s_Online;
	}
	public String getsS_Number() {
		return S_Number;
	}
	public void setsS_Number(String s_Number) {
		S_Number = s_Number;
	}
	public String getsCS_FullName() {
		return CS_FullName;
	}
	public void setsCS_FullName(String cS_FullName) {
		CS_FullName = cS_FullName;
	}
	public String getsCS_Longitude() {
		return CS_Longitude;
	}
	public void setsCS_Longitude(String cS_Longitude) {
		CS_Longitude = cS_Longitude;
	}
	public String getsCS_Latitude() {
		return CS_Latitude;
	}
	public void setsCS_Latitude(String cS_Latitude) {
		CS_Latitude = cS_Latitude;
	}
	public String getsS_LoopState() {
		return S_LoopState;
	}
	public void setsS_LoopState(String s_LoopState) {
		S_LoopState = s_LoopState;
	}
	public int getsS_LightHD() {
		return S_LightHD;
	}
	public void setsS_LightHD(int s_LightHD) {
		S_LightHD = s_LightHD;
	}

}
