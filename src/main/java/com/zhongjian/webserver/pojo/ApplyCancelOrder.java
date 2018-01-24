package com.zhongjian.webserver.pojo;

import java.util.Date;

public class ApplyCancelOrder {
    private Integer id;

    private Date createtime;

    private Integer orderid;

    private String memo;

    private Integer curstatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getCurstatus() {
        return curstatus;
    }

    public void setCurstatus(Integer curstatus) {
        this.curstatus = curstatus;
    }
}