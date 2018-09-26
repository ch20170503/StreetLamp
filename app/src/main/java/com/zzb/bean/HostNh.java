package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class HostNh implements Serializable {
	private String S_Id;//主机能耗表主键
	private int S_Year;//能耗年份
	private String S_January;//主机1月每天的能耗
	private String S_February;//主机2月每天的能耗
	private String S_March;//主机3月每天的能耗
	private String S_April;//主机4月每天的能耗
	private String S_May;//主机5月每天的能耗
	private String S_June;//主机6月每天的能耗
	private String S_July;//主机7月每天的能耗
	private String S_August;//主机8月每天的能耗
	private String S_September;//主机9月每天的能耗
	private String S_October;//主机10月每天的能耗
	private String S_November;//主机11月每天的能耗
	private String S_December;//主机12月每天的能耗
	private String SL_HostBase_RegPackage;//主机注册包

	public HostNh() {
	}

	public HostNh(String s_Id, int s_Year, String s_January, String s_February, String s_March, String s_April, String s_May, String s_June, String s_July, String s_August, String s_September, String s_October, String s_November, String s_December, String SL_HostBase_RegPackage) {
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
		this.SL_HostBase_RegPackage = SL_HostBase_RegPackage;
	}

	@Override
	public String toString() {
		return "HostNh{" +
				"S_Id='" + S_Id + '\'' +
				", S_Year=" + S_Year +
				", S_January=" + S_January +
				", S_February=" + S_February +
				", S_March=" + S_March +
				", S_April=" + S_April +
				", S_May=" + S_May +
				", S_June=" + S_June +
				", S_July=" + S_July +
				", S_August=" + S_August +
				", S_September=" + S_September +
				", S_October=" + S_October +
				", S_November=" + S_November +
				", S_December=" + S_December +
				", SL_HostBase_RegPackage='" + SL_HostBase_RegPackage + '\'' +
				'}';
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

	public String getsS_January() {
		return S_January;
	}

	public void setsS_January(String s_January) {
		S_January = s_January;
	}

	public String getsS_February() {
		return S_February;
	}

	public void setsS_February(String s_February) {
		S_February = s_February;
	}

	public String getsS_March() {
		return S_March;
	}

	public void setsS_March(String s_March) {
		S_March = s_March;
	}

	public String getsS_April() {
		return S_April;
	}

	public void setsS_April(String s_April) {
		S_April = s_April;
	}

	public String getsS_May() {
		return S_May;
	}

	public void setsS_May(String s_May) {
		S_May = s_May;
	}

	public String getsS_June() {
		return S_June;
	}

	public void setsS_June(String s_June) {
		S_June = s_June;
	}

	public String getsS_July() {
		return S_July;
	}

	public void setsS_July(String s_July) {
		S_July = s_July;
	}

	public String getsS_August() {
		return S_August;
	}

	public void setsS_August(String s_August) {
		S_August = s_August;
	}

	public String getsS_September() {
		return S_September;
	}

	public void setsS_September(String s_September) {
		S_September = s_September;
	}

	public String getsS_October() {
		return S_October;
	}

	public void setsS_October(String s_October) {
		S_October = s_October;
	}

	public String getsS_November() {
		return S_November;
	}

	public void setsS_November(String s_November) {
		S_November = s_November;
	}

	public String getsS_December() {
		return S_December;
	}

	public void setsS_December(String s_December) {
		S_December = s_December;
	}

	public String getsSL_HostBase_RegPackage() {
		return SL_HostBase_RegPackage;
	}

	public void setsSL_HostBase_RegPackage(String SL_HostBase_RegPackage) {
		this.SL_HostBase_RegPackage = SL_HostBase_RegPackage;
	}

	
}
