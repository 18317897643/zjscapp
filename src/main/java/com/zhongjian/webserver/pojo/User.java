package com.zhongjian.webserver.pojo;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class User {
    private Integer id;
    //为了token校验
    private String token;
    
    private String username;
    private String truename;
    private String password;
    private String paypassword;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createtime;
    private String sex;
    private String phone;
    private String email;
    private String alipay;
    private String wxopenid;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date birthday;
    private BigDecimal coupon;
    
    private BigDecimal remainpoints;
    private BigDecimal remainamount;
    private BigDecimal remainamountUsd;
    private BigDecimal remainelecnum;
    private BigDecimal remainvipamount;
    private BigDecimal remainstream;
    private Integer curstatus;
    private Integer invitecode;
    private Integer beinvitecode;
    private Integer isvip;
    private String headphoto;
    private String nickname;
    private String idcardphoto;
    private String idcardphoto2;
    private String idcardphoto3;
    private String idcardno;
    private Integer isauth;
    private Integer lev;
    private Integer issubproxy;
    private BigDecimal totalcost;
    private BigDecimal teamtotalcost;
    private BigDecimal subteamtotalcost;
    private Integer sysid;
    private Integer allowfenyong;
    private Integer proxystar;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTruename() {
        return truename;
    }
    public void setTruename(String truename) {
        this.truename = truename;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPaypassword() {
        return paypassword;
    }
    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }
    public Date getCreatetime() {
        return createtime;
    }
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAlipay() {
        return alipay;
    }
    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }
    public String getWxopenid() {
        return wxopenid;
    }
    public void setWxopenid(String wxopenid) {
        this.wxopenid = wxopenid;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public BigDecimal getRemainpoints() {
        return remainpoints;
    }
    public void setRemainpoints(BigDecimal remainpoints) {
        this.remainpoints = remainpoints;
    }
    public BigDecimal getRemainamount() {
        return remainamount;
    }
    public void setRemainamount(BigDecimal remainamount) {
        this.remainamount = remainamount;
    }
    public BigDecimal getRemainamountUsd() {
        return remainamountUsd;
    }
    public void setRemainamountUsd(BigDecimal remainamountUsd) {
        this.remainamountUsd = remainamountUsd;
    }
    public BigDecimal getRemainelecnum() {
        return remainelecnum;
    }
    public void setRemainelecnum(BigDecimal remainelecnum) {
        this.remainelecnum = remainelecnum;
    }
    public BigDecimal getRemainvipamount() {
        return remainvipamount;
    }
    public void setRemainvipamount(BigDecimal remainvipamount) {
        this.remainvipamount = remainvipamount;
    }
    public BigDecimal getRemainstream() {
        return remainstream;
    }
    public void setRemainstream(BigDecimal remainstream) {
        this.remainstream = remainstream;
    }
    public Integer getCurstatus() {
        return curstatus;
    }
    public void setCurstatus(Integer curstatus) {
        this.curstatus = curstatus;
    }
    public Integer getInvitecode() {
        return invitecode;
    }
    public void setInvitecode(Integer invitecode) {
        this.invitecode = invitecode;
    }
    public Integer getBeinvitecode() {
        return beinvitecode;
    }
    public void setBeinvitecode(Integer beinvitecode) {
        this.beinvitecode = beinvitecode;
    }
    public Integer getIsvip() {
        return isvip;
    }
    public void setIsvip(Integer isvip) {
        this.isvip = isvip;
    }
    public String getHeadphoto() {
        return headphoto;
    }
    public void setHeadphoto(String headphoto) {
        this.headphoto = headphoto;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getIdcardphoto() {
        return idcardphoto;
    }
    public void setIdcardphoto(String idcardphoto) {
        this.idcardphoto = idcardphoto;
    }
    public String getIdcardphoto2() {
        return idcardphoto2;
    }
    public void setIdcardphoto2(String idcardphoto2) {
        this.idcardphoto2 = idcardphoto2;
    }
    public String getIdcardphoto3() {
        return idcardphoto3;
    }
    public void setIdcardphoto3(String idcardphoto3) {
        this.idcardphoto3 = idcardphoto3;
    }
    public String getIdcardno() {
        return idcardno;
    }
    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }
    public Integer getIsauth() {
        return isauth;
    }
    public void setIsauth(Integer isauth) {
        this.isauth = isauth;
    }
    public Integer getLev() {
        return lev;
    }
    public void setLev(Integer lev) {
        this.lev = lev;
    }
    public Integer getIssubproxy() {
        return issubproxy;
    }
    public void setIssubproxy(Integer issubproxy) {
        this.issubproxy = issubproxy;
    }
    public BigDecimal getTotalcost() {
        return totalcost;
    }
    public void setTotalcost(BigDecimal totalcost) {
        this.totalcost = totalcost;
    }
    public BigDecimal getTeamtotalcost() {
        return teamtotalcost;
    }
    public void setTeamtotalcost(BigDecimal teamtotalcost) {
        this.teamtotalcost = teamtotalcost;
    }
    public BigDecimal getSubteamtotalcost() {
        return subteamtotalcost;
    }
    public void setSubteamtotalcost(BigDecimal subteamtotalcost) {
        this.subteamtotalcost = subteamtotalcost;
    }
    public Integer getSysid() {
        return sysid;
    }
    public void setSysid(Integer sysid) {
        this.sysid = sysid;
    }
    public Integer getAllowfenyong() {
        return allowfenyong;
    }
    public void setAllowfenyong(Integer allowfenyong) {
        this.allowfenyong = allowfenyong;
    }
    public Integer getProxystar() {
        return proxystar;
    }
    public void setProxystar(Integer proxystar) {
        this.proxystar = proxystar;
    }
    public BigDecimal getCoupon() {
        return coupon;
    }
    public void setCoupon(BigDecimal coupon) {
        this.coupon = coupon;
    }
    
}