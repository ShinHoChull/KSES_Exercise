package com.m2comm.module.models;

public class ContentDTO {

    String imgUrl;
    String sid;
    String groupSid;
    String contentSid;
    String Title;
    String videoUrl;

    public ContentDTO(String imgUrl, String sid, String groupSid, String title, String videoUrl ,String contentSid) {
        this.imgUrl = imgUrl;
        this.sid = sid;
        this.groupSid = groupSid;
        Title = title;
        this.videoUrl = videoUrl;
        this.contentSid = contentSid;
    }

    public void setContentSid(String contentSid) {
        this.contentSid = contentSid;
    }

    public String getContentSid() {
        return contentSid;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setGroupSid(String groupSid) {
        this.groupSid = groupSid;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSid() {
        return sid;
    }

    public String getGroupSid() {
        return groupSid;
    }

    public String getTitle() {
        return Title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
