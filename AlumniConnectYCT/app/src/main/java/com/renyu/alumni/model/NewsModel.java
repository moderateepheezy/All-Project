package com.renyu.alumni.model;

import java.io.Serializable;

public class NewsModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String title="";
    String pic="";
    String url="";
    String newstime="";
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNewstime() {
		return newstime;
	}
	public void setNewstime(String newstime) {
		this.newstime = newstime;
	}
}
