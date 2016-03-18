package com.renyu.alumni.model;

import java.io.Serializable;

public class CreateclassinfoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String class_name="";
    int class_id=0;
    int class_state=0;
    String class_pic="";
    String create_username="";
    int super_admin=0;
    String user_id="";
    String refuseStr="";
    
	public String getRefuseStr() {
		return refuseStr;
	}
	public void setRefuseStr(String refuseStr) {
		this.refuseStr = refuseStr;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getSuper_admin() {
		return super_admin;
	}
	public void setSuper_admin(int super_admin) {
		this.super_admin = super_admin;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public int getClass_state() {
		return class_state;
	}
	public void setClass_state(int class_state) {
		this.class_state = class_state;
	}
	public String getClass_pic() {
		return class_pic;
	}
	public void setClass_pic(String class_pic) {
		this.class_pic = class_pic;
	}
	public String getCreate_username() {
		return create_username;
	}
	public void setCreate_username(String create_username) {
		this.create_username = create_username;
	}

}
