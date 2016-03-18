package com.renyu.alumni.model;

import java.io.Serializable;

public class AluassociationinfoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String admin_name="";
    int aluassociation_num=0;
    int aluassociation_id=0;
    int aluassociation_role_state=0;
    int aluassociation_admin=0;
    String aluassociation_pic="";
    String aluassociation_name="";
    int aluassociation_apply=0; 
    String aluassociation_desc="";
    String refuseStr="";
    
	public String getRefuseStr() {
		return refuseStr;
	}
	public void setRefuseStr(String refuseStr) {
		this.refuseStr = refuseStr;
	}
	public String getAluassociation_desc() {
		return aluassociation_desc;
	}
	public void setAluassociation_desc(String aluassociation_desc) {
		this.aluassociation_desc = aluassociation_desc;
	}
	public int getAluassociation_apply() {
		return aluassociation_apply;
	}
	public void setAluassociation_apply(int aluassociation_apply) {
		this.aluassociation_apply = aluassociation_apply;
	}
	public String getAdmin_name() {
		return admin_name;
	}
	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}
	public int getAluassociation_num() {
		return aluassociation_num;
	}
	public void setAluassociation_num(int aluassociation_num) {
		this.aluassociation_num = aluassociation_num;
	}
	public int getAluassociation_id() {
		return aluassociation_id;
	}
	public void setAluassociation_id(int aluassociation_id) {
		this.aluassociation_id = aluassociation_id;
	}
	public int getAluassociation_role_state() {
		return aluassociation_role_state;
	}
	public void setAluassociation_role_state(int aluassociation_role_state) {
		this.aluassociation_role_state = aluassociation_role_state;
	}
	public int getAluassociation_admin() {
		return aluassociation_admin;
	}
	public void setAluassociation_admin(int aluassociation_admin) {
		this.aluassociation_admin = aluassociation_admin;
	}
	public String getAluassociation_pic() {
		return aluassociation_pic;
	}
	public void setAluassociation_pic(String aluassociation_pic) {
		this.aluassociation_pic = aluassociation_pic;
	}
	public String getAluassociation_name() {
		return aluassociation_name;
	}
	public void setAluassociation_name(String aluassociation_name) {
		this.aluassociation_name = aluassociation_name;
	}
    
}
