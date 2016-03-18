package com.renyu.alumni.model;

public class ReceiverPrivateLetterModel {

	int user_id=0;
	String username="";
	String avatar_large="";
	String content="";
	long time=0;
	int noReadCount=0;
	int type=0;
	
	public int getNoReadCount() {
		return noReadCount;
	}
	public void setNoReadCount(int noReadCount) {
		this.noReadCount = noReadCount;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar_large() {
		return avatar_large;
	}
	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
