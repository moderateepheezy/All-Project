package com.renyu.alumni.model;

import java.util.ArrayList;

public class PostCopDetailModel {
	int view_times=0;    
    int resource_type=0;
    String publish_author="";
    int user_id=0;
    int resource_id=0;
    int resource_category=0;
    String generate_time="";
    String user_name="";
    int commentscount=0;
    String resource_content="";
    int publish_type=0;
    String resource_title="";
    String resource_tags="";
    String avatar_large="";
    String college_name="";
    String year="";
    String city_name="";
    String industry="";
    ArrayList<String> picInfoList=null;
	public int getView_times() {
		return view_times;
	}
	public void setView_times(int view_times) {
		this.view_times = view_times;
	}
	public int getResource_type() {
		return resource_type;
	}
	public void setResource_type(int resource_type) {
		this.resource_type = resource_type;
	}
	public String getPublish_author() {
		return publish_author;
	}
	public void setPublish_author(String publish_author) {
		this.publish_author = publish_author;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getResource_id() {
		return resource_id;
	}
	public void setResource_id(int resource_id) {
		this.resource_id = resource_id;
	}
	public int getResource_category() {
		return resource_category;
	}
	public void setResource_category(int resource_category) {
		this.resource_category = resource_category;
	}
	public String getGenerate_time() {
		return generate_time;
	}
	public void setGenerate_time(String generate_time) {
		this.generate_time = generate_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getCommentscount() {
		return commentscount;
	}
	public void setCommentscount(int commentscount) {
		this.commentscount = commentscount;
	}
	public String getResource_content() {
		return resource_content;
	}
	public void setResource_content(String resource_content) {
		this.resource_content = resource_content;
	}
	public int getPublish_type() {
		return publish_type;
	}
	public void setPublish_type(int publish_type) {
		this.publish_type = publish_type;
	}
	public String getResource_title() {
		return resource_title;
	}
	public void setResource_title(String resource_title) {
		this.resource_title = resource_title;
	}
	public String getResource_tags() {
		return resource_tags;
	}
	public void setResource_tags(String resource_tags) {
		this.resource_tags = resource_tags;
	}
	public String getAvatar_large() {
		return avatar_large;
	}
	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}
	public String getCollege_name() {
		return college_name;
	}
	public void setCollege_name(String college_name) {
		this.college_name = college_name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public ArrayList<String> getPicInfoList() {
		return picInfoList;
	}
	public void setPicInfoList(ArrayList<String> picInfoList) {
		this.picInfoList = picInfoList;
	}
}
