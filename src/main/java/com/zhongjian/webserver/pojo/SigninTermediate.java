package com.zhongjian.webserver.pojo;

import java.util.Date;

public class SigninTermediate {
    private Integer id;

    private Integer userid;

    private Date lastsigntime;

    private Integer continueday;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getLastsigntime() {
        return lastsigntime;
    }

    public void setLastsigntime(Date lastsigntime) {
        this.lastsigntime = lastsigntime;
    }

    public Integer getContinueday() {
        return continueday;
    }

    public void setContinueday(Integer continueday) {
        this.continueday = continueday;
    }
}