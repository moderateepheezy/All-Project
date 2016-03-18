package com.renyu.alumni.model;

import java.io.Serializable;

public class ClassUserModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int class_role_state=0;
    int user_id=0;
    int class_admin=0;
    String user_name="";
    String avatar_large="";
    int is_authentication=0;
    
    String city_name="";
    int city_secret=0;
    String industry="";
    int industry_secret=0;
    String companyname="";
    int company_secret=0;
    String year="";
    String class_name="";
    
	public int getIs_authentication() {
		return is_authentication;
	}
	public void setIs_authentication(int is_authentication) {
		this.is_authentication = is_authentication;
	}
	public String getAvatar_large() {
		return avatar_large;
	}
	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}
	public int getClass_role_state() {
		return class_role_state;
	}
	public void setClass_role_state(int class_role_state) {
		this.class_role_state = class_role_state;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getClass_admin() {
		return class_admin;
	}
	public void setClass_admin(int class_admin) {
		this.class_admin = class_admin;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public int getCity_secret() {
		return city_secret;
	}
	public void setCity_secret(int city_secret) {
		this.city_secret = city_secret;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public int getIndustry_secret() {
		return industry_secret;
	}
	public void setIndustry_secret(int industry_secret) {
		this.industry_secret = industry_secret;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public int getCompany_secret() {
		return company_secret;
	}
	public void setCompany_secret(int company_secret) {
		this.company_secret = company_secret;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

}
