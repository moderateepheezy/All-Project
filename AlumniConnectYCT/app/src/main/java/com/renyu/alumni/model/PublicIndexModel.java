package com.renyu.alumni.model;

import java.io.Serializable;

public class PublicIndexModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String resource_id="";
    int view_times=0;
    String resource_title="";
    String generate_time="";
    String resource_pic="";
    int resource_type=0;
    String user_name="";
    String resource_url="";
	public String getResource_id() {
		return resource_id;
	}
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}
	public int getView_times() {
		return view_times;
	}
	public void setView_times(int view_times) {
		this.view_times = view_times;
	}
	public String getResource_title() {
		return resource_title;
	}
	public void setResource_title(String resource_title) {
		this.resource_title = resource_title;
	}
	public String getGenerate_time() {
		return generate_time;
	}
	public void setGenerate_time(String generate_time) {
		this.generate_time = generate_time;
	}
	public String getResource_pic() {
		return resource_pic;
	}
	public void setResource_pic(String resource_pic) {
		this.resource_pic = resource_pic;
	}
	public int getResource_type() {
		return resource_type;
	}
	public void setResource_type(int resource_type) {
		this.resource_type = resource_type;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getResource_url() {
		return resource_url;
	}
	public void setResource_url(String resource_url) {
		this.resource_url = resource_url;
	}
}
