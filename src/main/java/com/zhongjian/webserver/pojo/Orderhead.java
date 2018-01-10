package com.zhongjian.webserver.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Orderhead {
    private Integer id;

    private String orderno;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    private Integer userid;

    private Integer isdistributed;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receivedtime;

    private Integer discount;

    private BigDecimal totalamount;
    
    private BigDecimal usecoupon;

    private BigDecimal useelecnum;

    private BigDecimal usepointnum;

    private BigDecimal usevipremainnum;

    private BigDecimal freight;

    private BigDecimal realpay;

    private Integer countryid;

    private Integer provinceid;

    private Integer cityid;

    private Integer regionid;

    private String address;

    private String receivername;

    private String receiverphone;

    private Integer curstatus;

    private Integer iscomment;

    private String logisticsinfo;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date senddate;

    private String memo;

    private String retexpressname;

    private String retexpressno;

    private String retexpresspic;

    private Integer retconfirmed;

    private String expressname;

    private String expressno;
    
    private List<Orderline> orderlines;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getIsdistributed() {
        return isdistributed;
    }

    public void setIsdistributed(Integer isdistributed) {
        this.isdistributed = isdistributed;
    }

    public Date getReceivedtime() {
        return receivedtime;
    }

    public void setReceivedtime(Date receivedtime) {
        this.receivedtime = receivedtime;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(BigDecimal totalamount) {
        this.totalamount = totalamount;
    }

    public BigDecimal getUseelecnum() {
        return useelecnum;
    }

    public void setUseelecnum(BigDecimal useelecnum) {
        this.useelecnum = useelecnum;
    }

    public BigDecimal getUsepointnum() {
        return usepointnum;
    }

    public void setUsepointnum(BigDecimal usepointnum) {
        this.usepointnum = usepointnum;
    }

    public BigDecimal getUsevipremainnum() {
        return usevipremainnum;
    }

    public void setUsevipremainnum(BigDecimal usevipremainnum) {
        this.usevipremainnum = usevipremainnum;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getRealpay() {
        return realpay;
    }

    public void setRealpay(BigDecimal realpay) {
        this.realpay = realpay;
    }

    public Integer getCountryid() {
        return countryid;
    }

    public void setCountryid(Integer countryid) {
        this.countryid = countryid;
    }

    public Integer getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(Integer provinceid) {
        this.provinceid = provinceid;
    }

    public Integer getCityid() {
        return cityid;
    }

    public void setCityid(Integer cityid) {
        this.cityid = cityid;
    }

    public Integer getRegionid() {
        return regionid;
    }

    public void setRegionid(Integer regionid) {
        this.regionid = regionid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public String getReceiverphone() {
        return receiverphone;
    }

    public void setReceiverphone(String receiverphone) {
        this.receiverphone = receiverphone;
    }

    public Integer getCurstatus() {
        return curstatus;
    }

    public void setCurstatus(Integer curstatus) {
        this.curstatus = curstatus;
    }

    public Integer getIscomment() {
        return iscomment;
    }

    public void setIscomment(Integer iscomment) {
        this.iscomment = iscomment;
    }

    public String getLogisticsinfo() {
        return logisticsinfo;
    }

    public void setLogisticsinfo(String logisticsinfo) {
        this.logisticsinfo = logisticsinfo;
    }

    public Date getSenddate() {
        return senddate;
    }

    public void setSenddate(Date senddate) {
        this.senddate = senddate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRetexpressname() {
        return retexpressname;
    }

    public void setRetexpressname(String retexpressname) {
        this.retexpressname = retexpressname;
    }

    public String getRetexpressno() {
        return retexpressno;
    }

    public void setRetexpressno(String retexpressno) {
        this.retexpressno = retexpressno;
    }

    public String getRetexpresspic() {
        return retexpresspic;
    }

    public void setRetexpresspic(String retexpresspic) {
        this.retexpresspic = retexpresspic;
    }

    public Integer getRetconfirmed() {
        return retconfirmed;
    }

    public void setRetconfirmed(Integer retconfirmed) {
        this.retconfirmed = retconfirmed;
    }

    public String getExpressname() {
        return expressname;
    }

    public void setExpressname(String expressname) {
        this.expressname = expressname;
    }

    public String getExpressno() {
        return expressno;
    }

    public void setExpressno(String expressno) {
        this.expressno = expressno;
    }

	public BigDecimal getUsecoupon() {
		return usecoupon;
	}

	public void setUsecoupon(BigDecimal usecoupon) {
		this.usecoupon = usecoupon;
	}

	public List<Orderline> getOrderlines() {
		return orderlines;
	}

	public void setOrderlines(List<Orderline> orderlines) {
		this.orderlines = orderlines;
	}
}