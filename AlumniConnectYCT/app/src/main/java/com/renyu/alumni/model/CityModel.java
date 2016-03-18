package com.renyu.alumni.model;

import java.io.Serializable;

public class CityModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String cityCode="";
	String cityName="";
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
