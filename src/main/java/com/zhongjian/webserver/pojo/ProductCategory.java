package com.zhongjian.webserver.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductCategory {
    private Integer id;

    private String categoryname;

    private String icon;

    private Integer ord;

    private Integer curstatus;
    
    private List<ProductSubCategory> productSubCategories;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public Integer getCurstatus() {
        return curstatus;
    }

    public void setCurstatus(Integer curstatus) {
        this.curstatus = curstatus;
    }

	public List<ProductSubCategory> getProductSubCategories() {
		return productSubCategories;
	}

	public void setProductSubCategories(List<ProductSubCategory> productSubCategories) {
		this.productSubCategories = productSubCategories;
	}
    
}