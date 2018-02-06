package com.zhongjian.webserver.pojo;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class ProductCommentShow {
    private Integer id;
    private Integer productid;
    private Integer specid;
    private Integer orderid;
    private Integer userid;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    private String memo;
    private Integer star;
    private Integer curstatus;
    private List<ProductCommentPhoto> productCommentPhotos;
    
    private User user;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getProductid() {
        return productid;
    }
    public void setProductid(Integer productid) {
        this.productid = productid;
    }
    public Integer getSpecid() {
        return specid;
    }
    public void setSpecid(Integer specid) {
        this.specid = specid;
    }
    public Integer getOrderid() {
        return orderid;
    }
    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
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
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public Integer getStar() {
        return star;
    }
    public void setStar(Integer star) {
        this.star = star;
    }
    public Integer getCurstatus() {
        return curstatus;
    }
    public void setCurstatus(Integer curstatus) {
        this.curstatus = curstatus;
    }
    public List<ProductCommentPhoto> getProductCommentPhotos() {
        return productCommentPhotos;
    }
    public void setProductCommentPhotos(List<ProductCommentPhoto> productCommentPhotos) {
        this.productCommentPhotos = productCommentPhotos;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
