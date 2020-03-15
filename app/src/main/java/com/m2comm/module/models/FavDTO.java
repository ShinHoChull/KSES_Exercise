package com.m2comm.module.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FavDTO extends RealmObject {

    @PrimaryKey
    int num;
    int groupNum;
    int depth2Num;
    int depth3Num;
    String url;
    String groupTitle;
    String content_title;

    public FavDTO(){}

    public FavDTO(int num, int groupNum, int depth2Num, int depth3Num, String url, String groupTitle, String content_title) {
        this.num = num;
        this.groupNum = groupNum;
        this.depth2Num = depth2Num;
        this.depth3Num = depth3Num;
        this.url = url;
        this.groupTitle = groupTitle;
        this.content_title = content_title;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public void setDepth2Num(int depth2Num) {
        this.depth2Num = depth2Num;
    }

    public void setDepth3Num(int depth3Num) {
        this.depth3Num = depth3Num;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public void setContent_title(String content_title) {
        this.content_title = content_title;
    }

    public int getNum() {
        return num;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public int getDepth2Num() {
        return depth2Num;
    }

    public int getDepth3Num() {
        return depth3Num;
    }

    public String getUrl() {
        return url;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public String getContent_title() {
        return content_title;
    }
}
