package com.zzb.bean;



import java.io.Serializable;

public class Organ implements  Serializable {


	    private String S_Id; //机构id
	    private String S_ParentId; //是否是机构或者项目
	    private String S_FullName;  //名称
	    private String S_CategoryId; //类型
	    private String S_ManagerId; //机构负责人
	    private String S_TelePhone; //联系电话
	    private String S_Address; //地址
	    private int S_EnabledMark; //是否有效
	    private String S_Description;  //备注
	    private String S_CreatorTime; // 时间
	    private String S_CreatorUserAccount;
	    private String S_LastModifyTime;
	    private String S_LastModifyUserAccount;

	    private int S_Layers; //用户或项目的层次

	public Organ() {
	}

	public Organ(String s_Id, String s_ParentId, String s_FullName, String s_CategoryId, String s_ManagerId, String s_TelePhone, String s_Address, int s_EnabledMark, String s_Description, String s_CreatorTime, String s_CreatorUserAccount, String s_LastModifyTime, String s_LastModifyUserAccount, int s_Layers) {
		S_Id = s_Id;
		S_ParentId = s_ParentId;
		S_FullName = s_FullName;
		S_CategoryId = s_CategoryId;
		S_ManagerId = s_ManagerId;
		S_TelePhone = s_TelePhone;
		S_Address = s_Address;
		S_EnabledMark = s_EnabledMark;
		S_Description = s_Description;
		S_CreatorTime = s_CreatorTime;
		S_CreatorUserAccount = s_CreatorUserAccount;
		S_LastModifyTime = s_LastModifyTime;
		S_LastModifyUserAccount = s_LastModifyUserAccount;
		S_Layers = s_Layers;
	}

	@Override
	public String toString() {
		return "Organ{" +
				"S_Id='" + S_Id + '\'' +
				", S_ParentId='" + S_ParentId + '\'' +
				", S_FullName='" + S_FullName + '\'' +
				", S_CategoryId='" + S_CategoryId + '\'' +
				", S_ManagerId='" + S_ManagerId + '\'' +
				", S_TelePhone='" + S_TelePhone + '\'' +
				", S_Address='" + S_Address + '\'' +
				", S_EnabledMark=" + S_EnabledMark +
				", S_Description='" + S_Description + '\'' +
				", S_CreatorTime='" + S_CreatorTime + '\'' +
				", S_CreatorUserAccount='" + S_CreatorUserAccount + '\'' +
				", S_LastModifyTime='" + S_LastModifyTime + '\'' +
				", S_LastModifyUserAccount='" + S_LastModifyUserAccount + '\'' +
				", S_Layers=" + S_Layers +
				'}';
	}

	public String getsS_Id() {
		return S_Id;
	}

	public void setsS_Id(String s_Id) {
		S_Id = s_Id;
	}

	public String getsS_ParentId() {
		return S_ParentId;
	}

	public void setsS_ParentId(String s_ParentId) {
		S_ParentId = s_ParentId;
	}

	public String getsS_FullName() {
		return S_FullName;
	}

	public void setsS_FullName(String s_FullName) {
		S_FullName = s_FullName;
	}

	public String getsS_CategoryId() {
		return S_CategoryId;
	}

	public void setsS_CategoryId(String s_CategoryId) {
		S_CategoryId = s_CategoryId;
	}

	public String getsS_ManagerId() {
		return S_ManagerId;
	}

	public void setsS_ManagerId(String s_ManagerId) {
		S_ManagerId = s_ManagerId;
	}

	public String getsS_TelePhone() {
		return S_TelePhone;
	}

	public void setsS_TelePhone(String s_TelePhone) {
		S_TelePhone = s_TelePhone;
	}

	public String getsS_Address() {
		return S_Address;
	}

	public void setsS_Address(String s_Address) {
		S_Address = s_Address;
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

	public int getsS_Layers() {
		return S_Layers;
	}

	public void setsS_Layers(int s_Layers) {
		S_Layers = s_Layers;
	}
}
