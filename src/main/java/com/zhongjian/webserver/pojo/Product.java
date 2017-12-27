package com.zhongjian.webserver.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Product {
    private Integer id;

    private String productno;

    private String productname;

    private Integer categoryid;

    private Integer subcategoryid;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    private Integer stocknum;

    private BigDecimal price;

    private BigDecimal oldprice;

    private BigDecimal freight;

    private Integer elecnum;

    private Integer curstatus;

    private Integer tag;

    private Integer isrecommended;

    private Integer startnum;

    private Integer salenum;

    private Integer commentnum;

    private String producerno;

    private String producertel;

    private String producername;

    private String placeofdelivery;

    private String htmltext;
    
    private List<Productphoto> productphotos;
    
    private List<ProductSpec> productspecs;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductno() {
        return productno;
    }

    public void setProductno(String productno) {
        this.productno = productno;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public Integer getSubcategoryid() {
        return subcategoryid;
    }

    public void setSubcategoryid(Integer subcategoryid) {
        this.subcategoryid = subcategoryid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getStocknum() {
        return stocknum;
    }

    public void setStocknum(Integer stocknum) {
        this.stocknum = stocknum;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOldprice() {
        return oldprice;
    }

    public void setOldprice(BigDecimal oldprice) {
        this.oldprice = oldprice;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public Integer getElecnum() {
        return elecnum;
    }

    public void setElecnum(Integer elecnum) {
        this.elecnum = elecnum;
    }

    public Integer getCurstatus() {
        return curstatus;
    }

    public void setCurstatus(Integer curstatus) {
        this.curstatus = curstatus;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Integer getIsrecommended() {
        return isrecommended;
    }

    public void setIsrecommended(Integer isrecommended) {
        this.isrecommended = isrecommended;
    }

    public Integer getStartnum() {
        return startnum;
    }

    public void setStartnum(Integer startnum) {
        this.startnum = startnum;
    }

    public Integer getSalenum() {
        return salenum;
    }

    public void setSalenum(Integer salenum) {
        this.salenum = salenum;
    }

    public Integer getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(Integer commentnum) {
        this.commentnum = commentnum;
    }

    public String getProducerno() {
        return producerno;
    }

    public void setProducerno(String producerno) {
        this.producerno = producerno;
    }

    public String getProducertel() {
        return producertel;
    }

    public void setProducertel(String producertel) {
        this.producertel = producertel;
    }

    public String getProducername() {
        return producername;
    }

    public void setProducername(String producername) {
        this.producername = producername;
    }

    public String getPlaceofdelivery() {
        return placeofdelivery;
    }

    public void setPlaceofdelivery(String placeofdelivery) {
        this.placeofdelivery = placeofdelivery;
    }

    public String getHtmltext() {
        return htmltext;
    }

    public void setHtmltext(String htmltext) {
        this.htmltext = htmltext;
    }

	public List<Productphoto> getProductphotos() {
		return productphotos;
	}

	public void setProductphotos(List<Productphoto> productphotos) {
		this.productphotos = productphotos;
	}

	public List<ProductSpec> getProductspecs() {
		return productspecs;
	}

	public void setProductspecs(List<ProductSpec> productspecs) {
		this.productspecs = productspecs;
	}
	
	
    
}