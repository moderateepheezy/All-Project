package com.renyu.alumni.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ClassInfoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int class_id=0;
    int student_num=0;
    String class_pic="";
    String class_name="";
    int class_admin=0;
    int class_role_state=0;
    String admin_name="";
    int student_apply=0;
    ArrayList<TeacherinfosModel> teacher=null;
    String degree_name="";
    int year=0;
    String teacherName="";
    String college_name="";
    String refuseStr="";
    
	public String getRefuseStr() {
		return refuseStr;
	}
	public void setRefuseStr(String refuseStr) {
		this.refuseStr = refuseStr;
	}
	public String getDegree_name() {
		return degree_name;
	}
	public void setDegree_name(String degree_name) {
		this.degree_name = degree_name;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getCollege_name() {
		return college_name;
	}
	public void setCollege_name(String college_name) {
		this.college_name = college_name;
	}
	public ArrayList<TeacherinfosModel> getTeacher() {
		return teacher;
	}
	public void setTeacher(ArrayList<TeacherinfosModel> teacher) {
		this.teacher = teacher;
	}
	public int getStudent_apply() {
		return student_apply;
	}
	public void setStudent_apply(int student_apply) {
		this.student_apply = student_apply;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public int getStudent_num() {
		return student_num;
	}
	public void setStudent_num(int student_num) {
		this.student_num = student_num;
	}
	public String getClass_pic() {
		return class_pic;
	}
	public void setClass_pic(String class_pic) {
		this.class_pic = class_pic;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public int getClass_admin() {
		return class_admin;
	}
	public void setClass_admin(int class_admin) {
		this.class_admin = class_admin;
	}
	public int getClass_role_state() {
		return class_role_state;
	}
	public void setClass_role_state(int class_role_state) {
		this.class_role_state = class_role_state;
	}
	public String getAdmin_name() {
		return admin_name;
	}
	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}
    
}
