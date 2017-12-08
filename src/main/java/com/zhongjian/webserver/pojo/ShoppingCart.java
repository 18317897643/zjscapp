package com.zhongjian.webserver.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ShoppingCart {
    private Integer id;

    private Integer userid;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    private Product product;

    private ProductSpec productSpec;

    private Integer productnum;

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

  
    public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductSpec getProductSpec() {
		return productSpec;
	}

	public void setProductSpec(ProductSpec productSpec) {
		this.productSpec = productSpec;
	}

	public Integer getProductnum() {
        return productnum;
    }

    public void setProductnum(Integer productnum) {
        this.productnum = productnum;
    }
}