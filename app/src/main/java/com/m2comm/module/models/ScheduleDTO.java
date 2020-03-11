package com.m2comm.module.models;

import java.io.Serializable;

public class ScheduleDTO implements Serializable {

    private String sid;
    private String subject;
    private String category;
    private String sdate;
    private String edate;

    public ScheduleDTO(String sid, String subject, String category, String sdate, String edate) {
        this.sid = sid;
        this.subject = subject;
        this.category = category;
        this.sdate = sdate;
        this.edate = edate;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getSid() {
        return sid;
    }

    public String getSubject() {
        return subject;
    }

    public String getSdate() {
        return sdate;
    }

    public String getEdate() {
        return edate;
    }

    public String getCategory() {
        return category;
    }

}
