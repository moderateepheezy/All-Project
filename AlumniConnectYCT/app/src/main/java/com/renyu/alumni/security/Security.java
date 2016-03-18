package com.renyu.alumni.security;

public class Security {

	public native String getToken(String timestamps, String functionName, String param, Object obj);
	
	static {
		System.loadLibrary("security");
	}
}
