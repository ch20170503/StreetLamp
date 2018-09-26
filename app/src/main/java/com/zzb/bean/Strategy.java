package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Strategy implements  Serializable {
	  private String S_Id;//策略id
	    private int S_Number;//策略编号
	    private String S_FullName;//策略名称
	    private int S_Enabled;//使能开关 1 开 0关
	    private int S_Priority;//策略优先级
	    private int S_Cycle;//策略实行周期
	    private int S_StartYear;//策略起始年
	    private int S_StartMonth;//策略起始月
	    private int S_StartDay;//策略起始日
	    private int S_StartWeek;//策略起始周
	    private int S_EndYear;//策略结束年
	    private int S_EndMonth;//策略结束月
	    private int S_EndDay;//策略结束日
	    private int S_EndWeek;//策略结束周
	    private int S_Reference;//策略时间参考点
	    private int S_Hour;//策略触发时
	    private int S_Minute;//策略触发分
	    private String S_GroupMask;//策略分组掩码
	    private int S_DimEnabled;//调光使能开关
	    private int S_DimValue;//调光值
	    private int S_Switch;//回路开关
	    private String S_LoopMsk;//回路掩码
	    private String S_Description;//策略信息备注
	    private String S_CreatorTime;
	    private String S_CreatorUserAccount;
	    private String S_LastModifyTime;
	    private String S_LastModifyUserAccount;
	    private String SL_Organize_S_Id;//机构或机构主键的ID

	protected Strategy(Parcel in) {
		S_Id = in.readString();
		S_Number = in.readInt();
		S_FullName = in.readString();
		S_Enabled = in.readInt();
		S_Priority = in.readInt();
		S_Cycle = in.readInt();
		S_StartYear = in.readInt();
		S_StartMonth = in.readInt();
		S_StartDay = in.readInt();
		S_StartWeek = in.readInt();
		S_EndYear = in.readInt();
		S_EndMonth = in.readInt();
		S_EndDay = in.readInt();
		S_EndWeek = in.readInt();
		S_Reference = in.readInt();
		S_Hour = in.readInt();
		S_Minute = in.readInt();
		S_GroupMask = in.readString();
		S_DimEnabled = in.readInt();
		S_DimValue = in.readInt();
		S_Switch = in.readInt();
		S_LoopMsk = in.readString();
		S_Description = in.readString();
		S_CreatorTime = in.readString();
		S_CreatorUserAccount = in.readString();
		S_LastModifyTime = in.readString();
		S_LastModifyUserAccount = in.readString();
		SL_Organize_S_Id = in.readString();
	}

	public String getsS_Id() {
			return S_Id;
		}
		public void setsS_Id(String s_Id) {
			S_Id = s_Id;
		}
		public int getsS_Number() {
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
		public int getsS_Enabled() {
			return S_Enabled;
		}
		public void setsS_Enabled(int s_Enabled) {
			S_Enabled = s_Enabled;
		}
		public int getsS_Priority() {
			return S_Priority;
		}
		public void setsS_Priority(int s_Priority) {
			S_Priority = s_Priority;
		}
		public int getsS_Cycle() {
			return S_Cycle;
		}
		public void setsS_Cycle(int s_Cycle) {
			S_Cycle = s_Cycle;
		}
		public int getsS_StartYear() {
			return S_StartYear;
		}
		public void setsS_StartYear(int s_StartYear) {
			S_StartYear = s_StartYear;
		}
		public int getsS_StartMonth() {
			return S_StartMonth;
		}
		public void setsS_StartMonth(int s_StartMonth) {
			S_StartMonth = s_StartMonth;
		}
		public int getsS_StartDay() {
			return S_StartDay;
		}
		public void setsS_StartDay(int s_StartDay) {
			S_StartDay = s_StartDay;
		}
		public int getsS_StartWeek() {
			return S_StartWeek;
		}
		public void setsS_StartWeek(int s_StartWeek) {
			S_StartWeek = s_StartWeek;
		}
		public int getsS_EndYear() {
			return S_EndYear;
		}
		public void setsS_EndYear(int s_EndYear) {
			S_EndYear = s_EndYear;
		}
		public int getsS_EndMonth() {
			return S_EndMonth;
		}
		public void setsS_EndMonth(int s_EndMonth) {
			S_EndMonth = s_EndMonth;
		}
		public int getsS_EndDay() {
			return S_EndDay;
		}
		public void setsS_EndDay(int s_EndDay) {
			S_EndDay = s_EndDay;
		}
		public int getsS_EndWeek() {
			return S_EndWeek;
		}
		public void setsS_EndWeek(int s_EndWeek) {
			S_EndWeek = s_EndWeek;
		}
		public int getsS_Reference() {
			return S_Reference;
		}
		public void setsS_Reference(int s_Reference) {
			S_Reference = s_Reference;
		}
		public int getsS_Hour() {
			return S_Hour;
		}
		public void setsS_Hour(int s_Hour) {
			S_Hour = s_Hour;
		}
		public int getsS_Minute() {
			return S_Minute;
		}
		public void setsS_Minute(int s_Minute) {
			S_Minute = s_Minute;
		}
		public String getsS_GroupMask() {
			return S_GroupMask;
		}
		public void setsS_GroupMask(String s_GroupMask) {
			S_GroupMask = s_GroupMask;
		}
		public int getsS_DimEnabled() {
			return S_DimEnabled;
		}
		public void setsS_DimEnabled(int s_DimEnabled) {
			S_DimEnabled = s_DimEnabled;
		}
		public int getsS_DimValue() {
			return S_DimValue;
		}
		public void setsS_DimValue(int s_DimValue) {
			S_DimValue = s_DimValue;
		}
		public int getsS_Switch() {
			return S_Switch;
		}
		public void setsS_Switch(int s_Switch) {
			S_Switch = s_Switch;
		}
		public String getsS_LoopMsk() {
			return S_LoopMsk;
		}
		public void setsS_LoopMsk(String s_LoopMsk) {
			S_LoopMsk = s_LoopMsk;
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
		public Strategy(String s_Id, int s_Number, String s_FullName, int s_Enabled, int s_Priority, int s_Cycle,
				int s_StartYear, int s_StartMonth, int s_StartDay, int s_StartWeek, int s_EndYear, int s_EndMonth,
				int s_EndDay, int s_EndWeek, int s_Reference, int s_Hour, int s_Minute, String s_GroupMask,
				int s_DimEnabled, int s_DimValue, int s_Switch, String s_LoopMsk, String s_Description,
				String s_CreatorTime, String s_CreatorUserAccount, String s_LastModifyTime,
				String s_LastModifyUserAccount, String sL_Organize_S_Id) {
			super();
			S_Id = s_Id;
			S_Number = s_Number;
			S_FullName = s_FullName;
			S_Enabled = s_Enabled;
			S_Priority = s_Priority;
			S_Cycle = s_Cycle;
			S_StartYear = s_StartYear;
			S_StartMonth = s_StartMonth;
			S_StartDay = s_StartDay;
			S_StartWeek = s_StartWeek;
			S_EndYear = s_EndYear;
			S_EndMonth = s_EndMonth;
			S_EndDay = s_EndDay;
			S_EndWeek = s_EndWeek;
			S_Reference = s_Reference;
			S_Hour = s_Hour;
			S_Minute = s_Minute;
			S_GroupMask = s_GroupMask;
			S_DimEnabled = s_DimEnabled;
			S_DimValue = s_DimValue;
			S_Switch = s_Switch;
			S_LoopMsk = s_LoopMsk;
			S_Description = s_Description;
			S_CreatorTime = s_CreatorTime;
			S_CreatorUserAccount = s_CreatorUserAccount;
			S_LastModifyTime = s_LastModifyTime;
			S_LastModifyUserAccount = s_LastModifyUserAccount;
			SL_Organize_S_Id = sL_Organize_S_Id;
		}
		public Strategy() {
			super();
		}
		@Override
		public String toString() {
			return "Strategy [S_Id=" + S_Id + ", S_Number=" + S_Number + ", S_FullName=" + S_FullName + ", S_Enabled="
					+ S_Enabled + ", S_Priority=" + S_Priority + ", S_Cycle=" + S_Cycle + ", S_StartYear=" + S_StartYear
					+ ", S_StartMonth=" + S_StartMonth + ", S_StartDay=" + S_StartDay + ", S_StartWeek=" + S_StartWeek
					+ ", S_EndYear=" + S_EndYear + ", S_EndMonth=" + S_EndMonth + ", S_EndDay=" + S_EndDay
					+ ", S_EndWeek=" + S_EndWeek + ", S_Reference=" + S_Reference + ", S_Hour=" + S_Hour + ", S_Minute="
					+ S_Minute + ", S_GroupMask=" + S_GroupMask + ", S_DimEnabled=" + S_DimEnabled + ", S_DimValue="
					+ S_DimValue + ", S_Switch=" + S_Switch + ", S_LoopMsk=" + S_LoopMsk + ", S_Description="
					+ S_Description + ", S_CreatorTime=" + S_CreatorTime + ", S_CreatorUserAccount="
					+ S_CreatorUserAccount + ", S_LastModifyTime=" + S_LastModifyTime + ", S_LastModifyUserAccount="
					+ S_LastModifyUserAccount + ", SL_Organize_S_Id=" + SL_Organize_S_Id + "]";
		}
}
