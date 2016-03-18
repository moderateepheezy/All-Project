package org.simpumind.com.yctalumniconnect;

public class ItemNewsList {

	private String NewsHeading;
	private String NewsImage;
	private String NewsDate;



 	public String getNewsHeading() {
		return NewsHeading;
	}

	public void setNewsHeading(String newsheading) {
		this.NewsHeading = newsheading;
	}
	public String getNewsImage() {
		return NewsImage;
	}

	public void setNewsImage(String newsimage) {
		this.NewsImage = newsimage;
	}
	public String getNewsDate() {
		return NewsDate;
	}

	public void setNewsDate(String newsdate) {
		this.NewsDate = newsdate;
	}

	public ItemNewsList(String newsHeading,  String newsDate) {
		NewsHeading = newsHeading;
		NewsDate = newsDate;
	}
}
