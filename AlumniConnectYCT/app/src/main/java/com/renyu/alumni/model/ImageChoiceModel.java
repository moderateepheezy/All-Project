package com.renyu.alumni.model;

import java.io.Serializable;

public class ImageChoiceModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id=0;
	String path="";
	String content="";
	//ÉÏ´«×´Ì¬
	public final static int ADD=0;
	public final static int NORMAL=1;
	public final static int UPDATE=2;
	public final static int DELETE=3;
	int flag=NORMAL;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
