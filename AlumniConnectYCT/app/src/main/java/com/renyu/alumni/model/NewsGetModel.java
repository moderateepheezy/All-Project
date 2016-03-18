package com.renyu.alumni.model;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsGetModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<NewsModel> head_news=null;
	ArrayList<NewsModel> news=null;
	public ArrayList<NewsModel> getHead_news() {
		return head_news;
	}
	public void setHead_news(ArrayList<NewsModel> head_news) {
		this.head_news = head_news;
	}
	public ArrayList<NewsModel> getNews() {
		return news;
	}
	public void setNews(ArrayList<NewsModel> news) {
		this.news = news;
	}
	
	
}
