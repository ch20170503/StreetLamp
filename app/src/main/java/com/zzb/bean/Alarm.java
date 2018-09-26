package com.zzb.bean;


import java.io.Serializable;

public class Alarm implements Serializable {
	private String S_Id; 
	private String S_Message;//信息内容
	private int S_Type; //信息类型
	private int S_ReadStaus;//是否读取
	private String S_Time; //消息时间
	private String S_TimeStamp; 
	private String S_Account; //处理人员
	private String SL_Organize_S_Id; //所属项目
	private String S_ItemId; //注册包或者编号

	public Alarm() {
	}

	public Alarm(String s_Id, String s_Message, int s_Type, int s_ReadStaus, String s_Time, String s_TimeStamp, String s_Account, String SL_Organize_S_Id, String s_ItemId) {
		S_Id = s_Id;
		S_Message = s_Message;
		S_Type = s_Type;
		S_ReadStaus = s_ReadStaus;
		S_Time = s_Time;
		S_TimeStamp = s_TimeStamp;
		S_Account = s_Account;
		this.SL_Organize_S_Id = SL_Organize_S_Id;
		S_ItemId = s_ItemId;
	}

	@Override
	public String toString() {
		return "Alarm{" +
				"S_Id='" + S_Id + '\'' +
				", S_Message='" + S_Message + '\'' +
				", S_Type=" + S_Type +
				", S_ReadStaus=" + S_ReadStaus +
				", S_Time='" + S_Time + '\'' +
				", S_TimeStamp='" + S_TimeStamp + '\'' +
				", S_Account='" + S_Account + '\'' +
				", SL_Organize_S_Id='" + SL_Organize_S_Id + '\'' +
				", S_ItemId='" + S_ItemId + '\'' +
				'}';
	}

	public String getsS_Id() {
		return S_Id;
	}

	public void setsS_Id(String s_Id) {
		S_Id = s_Id;
	}

	public String getsS_Message() {
		return S_Message;
	}

	public void setsS_Message(String s_Message) {
		S_Message = s_Message;
	}

	public int getsS_Type() {
		return S_Type;
	}

	public void setsS_Type(int s_Type) {
		S_Type = s_Type;
	}

	public int getsS_ReadStaus() {
		return S_ReadStaus;
	}

	public void setsS_ReadStaus(int s_ReadStaus) {
		S_ReadStaus = s_ReadStaus;
	}

	public String getsS_Time() {
		return S_Time;
	}

	public void setsS_Time(String s_Time) {
		S_Time = s_Time;
	}

	public String getsS_TimeStamp() {
		return S_TimeStamp;
	}

	public void setsS_TimeStamp(String s_TimeStamp) {
		S_TimeStamp = s_TimeStamp;
	}

	public String getsS_Account() {
		return S_Account;
	}

	public void setsS_Account(String s_Account) {
		S_Account = s_Account;
	}

	public String getSL_Organize_S_Id() {
		return SL_Organize_S_Id;
	}

	public void setSL_Organize_S_Id(String SL_Organize_S_Id) {
		this.SL_Organize_S_Id = SL_Organize_S_Id;
	}

	public String getsS_ItemId() {
		return S_ItemId;
	}

	public void setsS_ItemId(String s_ItemId) {
		S_ItemId = s_ItemId;
	}
}
