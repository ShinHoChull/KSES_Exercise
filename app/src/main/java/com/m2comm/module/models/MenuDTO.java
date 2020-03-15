package com.m2comm.module.models;

import java.io.Serializable;

public class MenuDTO implements Serializable {

    String title;
    String url;
    int sid;

    public MenuDTO(String title, String url, int sid) {
        this.title = title;
        this.url = url;
        this.sid = sid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public int getSid() {
        return sid;
    }
}
