package com.example.simpumind.tofunmiapp.com.example.simpumind.home;

/**
 * Created by simpumind on 4/8/15.
 */
public class PostItems {

    public String name;
    public String time;
    public String thumbNail;
    public String post;

    public PostItems(String name, String time, String thumbNail, String post) {
        this.name = name;
        this.time = time;
        this.thumbNail = thumbNail;
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
