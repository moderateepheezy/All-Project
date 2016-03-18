package com.renyu.alumni.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String token="";
	//用户标识
	private int user_id=0; 
	//必填信息
	private String user_name=""; //用户姓名
	private String mail=""; //用户邮箱
	private String password="";//密码
	private int year=0;//学籍
	private int college_id=0;//院系标识
	//选填信息
	private String companyname="";//用户所在公司
	private int company_secret=0;//是否公开 0 不公开 1 公开 默认公开
	private String company_department="";//部门
	private int department_secret=0;//是否公开
	private String company_position="";//职位
	private int position_secret=0;//是否公开
	private String phonenum="";//手机号码
	private int phonenum_secret=0;//是否公开
	private long qq=0;//qq号
	private int qq_secret=0;//是否公开
	private String industry;//所属行业
	private int industry_secret;//所属行业保密状态
	private int mail_secret=0;//邮箱是否公开
	private String city_name="";//城市名称
	private int city_secret=0;//是否公开
	private String service_name="";//提供的服务名称
	private int service_secret=0;//是否公开
	private String avatar_large="";
	private int is_authentication=0;//是否认证
	private String class_name="";
	private String college_name="";
	private ArrayList<String> personal_tags=null;
	
	public ArrayList<String> getPersonal_tags() {
		return personal_tags;
	}
	public void setPersonal_tags(ArrayList<String> personal_tags) {
		this.personal_tags = personal_tags;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getService_name() {
		return service_name;
	}
	public void setService_name(String service_name) {
		this.service_name = service_name;
	}
	public int getService_secret() {
		return service_secret;
	}
	public void setService_secret(int service_secret) {
		this.service_secret = service_secret;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getCollege_id() {
		return college_id;
	}
	public void setCollege_id(int college_id) {
		this.college_id = college_id;
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
	public String getCompany_department() {
		return company_department;
	}
	public void setCompany_department(String company_department) {
		this.company_department = company_department;
	}
	public int getDepartment_secret() {
		return department_secret;
	}
	public void setDepartment_secret(int department_secret) {
		this.department_secret = department_secret;
	}
	public String getCompany_position() {
		return company_position;
	}
	public void setCompany_position(String company_position) {
		this.company_position = company_position;
	}
	public int getPosition_secret() {
		return position_secret;
	}
	public void setPosition_secret(int position_secret) {
		this.position_secret = position_secret;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public int getPhonenum_secret() {
		return phonenum_secret;
	}
	public void setPhonenum_secret(int phonenum_secret) {
		this.phonenum_secret = phonenum_secret;
	}
	public long getQq() {
		return qq;
	}
	public void setQq(long qq) {
		this.qq = qq;
	}
	public int getQq_secret() {
		return qq_secret;
	}
	public void setQq_secret(int qq_secret) {
		this.qq_secret = qq_secret;
	}
	public int getMail_secret() {
		return mail_secret;
	}
	public void setMail_secret(int mail_secret) {
		this.mail_secret = mail_secret;
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
	public String getCollege_name() {
		return college_name;
	}
	public void setCollege_name(String college_name) {
		this.college_name = college_name;
	}
}
