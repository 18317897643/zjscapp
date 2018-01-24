package com.zhongjian.webserver.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class TxElec {
    private Integer id;

    private Integer userid;

    private Date createtime;

    private BigDecimal amount;

    private BigDecimal handamount;

    private String currency;

    private Integer points;

    private Integer curstatus;

    private String memo;

    private String txtype;

    private String cardno;

    private String truename;

    private String bankname;

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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getHandamount() {
        return handamount;
    }

    public void setHandamount(BigDecimal handamount) {
        this.handamount = handamount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getCurstatus() {
        return curstatus;
    }

    public void setCurstatus(Integer curstatus) {
        this.curstatus = curstatus;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTxtype() {
        return txtype;
    }

    public void setTxtype(String txtype) {
        this.txtype = txtype;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }
}