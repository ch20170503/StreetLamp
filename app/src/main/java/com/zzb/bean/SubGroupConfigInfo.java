package com.zzb.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SubGroupConfigInfo implements  Serializable {
	   private int ErrNum;
	    private String Message;

	protected SubGroupConfigInfo(Parcel in) {
		ErrNum = in.readInt();
		Message = in.readString();
	}


	public int getErrNum() {
			return ErrNum;
		}
		public SubGroupConfigInfo() {
			super();
		}
		public SubGroupConfigInfo(int errNum, String message) {
			super();
			ErrNum = errNum;
			Message = message;
		}
		@Override
		public String toString() {
			return "SubGroupConfigInfo [ErrNum=" + ErrNum + ", Message=" + Message + "]";
		}
		public void setErrNum(int errNum) {
			ErrNum = errNum;
		}
		public String getMessage() {
			return Message;
		}
		public void setMessage(String message) {
			Message = message;
		}


}
