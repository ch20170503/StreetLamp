package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SubNh2 implements Serializable {

	    private String S_Id;//分控能耗表主键
	    private int S_Year;//能耗年份
	    private float S_January;//分控1月每天的能耗
	    private float S_February;//分控2月每天的能耗
	    private float S_March;//分控3月每天的能耗
	    private float S_April;//分控4月每天的能耗
	    private float S_May;//分控5月每天的能耗
	    private float S_June;//分控6月每天的能耗
	    private float S_July;//分控7月每天的能耗
	    private float S_August;//分控8月每天的能耗
	    private float S_September;//分控9月每天的能耗
	    private float S_October;//分控10月每天的能耗

	protected SubNh2(Parcel in) {
		S_Id = in.readString();
		S_Year = in.readInt();
		SL_HostBase_RegPackage = in.readString();
		SL_SubController_S_Id = in.readString();
	}



	@Override
		public String toString() {
			return "SubNh2 [S_Id=" + S_Id + ", S_Year=" + S_Year + ", S_January=" + S_January + ", S_February="
					+ S_February + ", S_March=" + S_March + ", S_April=" + S_April + ", S_May=" + S_May + ", S_June="
					+ S_June + ", S_July=" + S_July + ", S_August=" + S_August + ", S_September=" + S_September
					+ ", S_October=" + S_October + ", S_November=" + S_November + ", S_December=" + S_December
					+ ", S_TotalEnergy=" + S_TotalEnergy + ", SL_HostBase_RegPackage=" + SL_HostBase_RegPackage
					+ ", SL_SubController_S_Id=" + SL_SubController_S_Id + "]";
		}
		public SubNh2() {
			super();
		}
		public SubNh2(String s_Id, int s_Year, float s_January, float s_February, float s_March, float s_April,
				float s_May, float s_June, float s_July, float s_August, float s_September, float s_October,
				float s_November, float s_December, float s_TotalEnergy, String sL_HostBase_RegPackage,
				String sL_SubController_S_Id) {
			super();
			S_Id = s_Id;
			S_Year = s_Year;
			S_January = s_January;
			S_February = s_February;
			S_March = s_March;
			S_April = s_April;
			S_May = s_May;
			S_June = s_June;
			S_July = s_July;
			S_August = s_August;
			S_September = s_September;
			S_October = s_October;
			S_November = s_November;
			S_December = s_December;
			S_TotalEnergy = s_TotalEnergy;
			SL_HostBase_RegPackage = sL_HostBase_RegPackage;
			SL_SubController_S_Id = sL_SubController_S_Id;
		}
		public String getsS_Id() {
			return S_Id;
		}
		public void setsS_Id(String s_Id) {
			S_Id = s_Id;
		}
		public int getsS_Year() {
			return S_Year;
		}
		public void setsS_Year(int s_Year) {
			S_Year = s_Year;
		}
		public float getsS_January() {
			return S_January;
		}
		public void setsS_January(float s_January) {
			S_January = s_January;
		}
		public float getsS_February() {
			return S_February;
		}
		public void setsS_February(float s_February) {
			S_February = s_February;
		}
		public float getsS_March() {
			return S_March;
		}
		public void setsS_March(float s_March) {
			S_March = s_March;
		}
		public float getsS_April() {
			return S_April;
		}
		public void setsS_April(float s_April) {
			S_April = s_April;
		}
		public float getsS_May() {
			return S_May;
		}
		public void setsS_May(float s_May) {
			S_May = s_May;
		}
		public float getsS_June() {
			return S_June;
		}
		public void setsS_June(float s_June) {
			S_June = s_June;
		}
		public float getsS_July() {
			return S_July;
		}
		public void setsS_July(float s_July) {
			S_July = s_July;
		}
		public float getsS_August() {
			return S_August;
		}
		public void setsS_August(float s_August) {
			S_August = s_August;
		}
		public float getsS_September() {
			return S_September;
		}
		public void setsS_September(float s_September) {
			S_September = s_September;
		}
		public float getsS_October() {
			return S_October;
		}
		public void setsS_October(float s_October) {
			S_October = s_October;
		}
		public float getsS_November() {
			return S_November;
		}
		public void setsS_November(float s_November) {
			S_November = s_November;
		}
		public float getsS_December() {
			return S_December;
		}
		public void setsS_December(float s_December) {
			S_December = s_December;
		}
		public float getsS_TotalEnergy() {
			return S_TotalEnergy;
		}
		public void setsS_TotalEnergy(float s_TotalEnergy) {
			S_TotalEnergy = s_TotalEnergy;
		}
		public String getsSL_HostBase_RegPackage() {
			return SL_HostBase_RegPackage;
		}
		public void setsSL_HostBase_RegPackage(String sL_HostBase_RegPackage) {
			SL_HostBase_RegPackage = sL_HostBase_RegPackage;
		}
		public String getsSL_SubController_S_Id() {
			return SL_SubController_S_Id;
		}
		public void setsSL_SubController_S_Id(String sL_SubController_S_Id) {
			SL_SubController_S_Id = sL_SubController_S_Id;
		}
		private float S_November;//分控11月每天的能耗
	    private float S_December;//分控12月每天的能耗
	    private float S_TotalEnergy;//累计总能耗
	    private String SL_HostBase_RegPackage;//主机注册包（沉余字段）
	    private String SL_SubController_S_Id;//分控I

}
