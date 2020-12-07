package com.m2comm.module.models;

import java.io.Serializable;

public class MenuDTO implements Serializable {

    String title;
    String url;
    int sid;
    String value;
    String thumbnail;

    public MenuDTO(String title, String url, int sid, String value, String thumbnail) {
        this.title = title;
        this.url = url;
        this.sid = sid;
        this.value = value;
        this.thumbnail = thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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
