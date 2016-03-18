package com.renyu.alumni.model;

import java.util.ArrayList;

public class PostUploadModel {

	//上传文件
	ArrayList<String> uploadList=null;
	//上传成功文件
	ArrayList<String> uploadCompList=null;
	String title="";
	String content="";
	String publish_author="";
	int resource_category=1;
	int resource_type=0;
	String resource_tags="";
	int publish_type=2;

	//完成上传数量
	int finishCount=0;
	
	public PostUploadModel() {
		uploadCompList=new ArrayList<String>();
	}
	
	public int getFinishCount() {
		return finishCount;
	}
	
	/**
	 * 完成数量添加
	 */
	public void setFinish() {
		finishCount++;
	}

	public ArrayList<String> getUploadList() {
		return uploadList;
	}

	public void setUploadList(ArrayList<String> uploadList) {
		this.uploadList = uploadList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<String> getUploadCompList() {
		return uploadCompList;
	}

	public String getPublish_author() {
		return publish_author;
	}

	public void setPublish_author(String publish_author) {
		this.publish_author = publish_author;
	}

	public int getResource_category() {
		return resource_category;
	}

	public void setResource_category(int resource_category) {
		this.resource_category = resource_category;
	}

	public int getResource_type() {
		return resource_type;
	}

	public void setResource_type(int resource_type) {
		this.resource_type = resource_type;
	}

	public String getResource_tags() {
		return resource_tags;
	}

	public void setResource_tags(String resource_tags) {
		this.resource_tags = resource_tags;
	}

	public int getPublish_type() {
		return publish_type;
	}
	
}
