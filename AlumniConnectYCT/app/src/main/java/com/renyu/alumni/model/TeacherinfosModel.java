package com.renyu.alumni.model;

import java.io.Serializable;

public class TeacherinfosModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String degree_name="";
	String teacher="";
	String college_name="";
	public String getDegree_name() {
		return degree_name;
	}
	public void setDegree_name(String degree_name) {
		this.degree_name = degree_name;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getCollege_name() {
		return college_name;
	}
	public void setCollege_name(String college_name) {
		this.college_name = college_name;
	}
	 
	 
}
