package com.example.simpumind.tofunmiapp.com.example.simpumind.home;

/**
 * Created by simpumind on 4/8/15.
 */
public class AllPostItems {

    public String name;
    public String time;
    public String post;

    public AllPostItems(String name, String time, String post) {
        this.name = name;
        this.time = time;
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


    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
