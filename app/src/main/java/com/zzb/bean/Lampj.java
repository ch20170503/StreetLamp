package com.zzb.bean;


import java.io.Serializable;

public class Lampj implements  Serializable {
	  	private String S_Id;//灯具表的主键
	    private int S_Number;//灯具编号
	    private int S_LightHD;//灯具调光口

	    private String S_Type;//灯具类型
	    private int S_Brightness;//亮度
	    private String S_Voltage;//
	    private String S_Current;//
	    private String S_power;//
	    private String S_Temperature;//
	    private String S_Frequency;//
	    private String S_TimeStamp;//
	    private String S_UpdateTime;//
	    private String SL_SubController_Id;//分控主建随机数
	    private int S_SubNum;//分控编号
		private String S_line;//在线状态

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

	public int getsS_LightHD() {
		return S_LightHD;
	}

	public void setsS_LightHD(int s_LightHD) {
		S_LightHD = s_LightHD;
	}

	public String getsS_Type() {
		return S_Type;
	}

	public void setsS_Type(String s_Type) {
		S_Type = s_Type;
	}

	public int getsS_Brightness() {
		return S_Brightness;
	}

	public void setsS_Brightness(int s_Brightness) {
		S_Brightness = s_Brightness;
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

	public String getsS_Frequency() {
		return S_Frequency;
	}

	public void setsS_Frequency(String s_Frequency) {
		S_Frequency = s_Frequency;
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

	public String getsSL_SubController_Id() {
		return SL_SubController_Id;
	}

	public void setsSL_SubController_Id(String SL_SubController_Id) {
		this.SL_SubController_Id = SL_SubController_Id;
	}

	public int getsS_SubNum() {
		return S_SubNum;
	}

	public void setsS_SubNum(int s_SubNum) {
		S_SubNum = s_SubNum;
	}

	public String getsS_line() {
		return S_line;
	}

	public void setsS_line(String s_line) {
		S_line = s_line;
	}

	public Lampj(String s_Id, int s_Number, int s_LightHD, String s_Type, int s_Brightness, String s_Voltage, String s_Current, String s_power, String s_Temperature, String s_Frequency, String s_TimeStamp, String s_UpdateTime, String SL_SubController_Id, int s_SubNum, String s_line) {

		S_Id = s_Id;
		S_Number = s_Number;
		S_LightHD = s_LightHD;
		S_Type = s_Type;
		S_Brightness = s_Brightness;
		S_Voltage = s_Voltage;
		S_Current = s_Current;
		S_power = s_power;
		S_Temperature = s_Temperature;
		S_Frequency = s_Frequency;
		S_TimeStamp = s_TimeStamp;
		S_UpdateTime = s_UpdateTime;
		this.SL_SubController_Id = SL_SubController_Id;
		S_SubNum = s_SubNum;
		S_line = s_line;
	}

	public Lampj() {
	}

	@Override
	public String toString() {
		return "Lampj{" +
				"S_Id='" + S_Id + '\'' +
				", S_Number=" + S_Number +
				", S_LightHD=" + S_LightHD +
				", S_Type='" + S_Type + '\'' +
				", S_Brightness=" + S_Brightness +
				", S_Voltage='" + S_Voltage + '\'' +
				", S_Current='" + S_Current + '\'' +
				", S_power='" + S_power + '\'' +
				", S_Temperature='" + S_Temperature + '\'' +
				", S_Frequency='" + S_Frequency + '\'' +
				", S_TimeStamp='" + S_TimeStamp + '\'' +
				", S_UpdateTime='" + S_UpdateTime + '\'' +
				", SL_SubController_Id='" + SL_SubController_Id + '\'' +
				", S_SubNum=" + S_SubNum +
				", S_line='" + S_line + '\'' +
				'}';
	}
}
