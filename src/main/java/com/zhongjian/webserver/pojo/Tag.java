package com.zhongjian.webserver.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Tag {

	Integer tagId;
	
	String tagName;
	
	Integer ord;
	
	List<Product> productsOfTag;

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public List<Product> getProductsOfTag() {
		return productsOfTag;
	}

	public void setProductsOfTag(List<Product> productsOfTag) {
		this.productsOfTag = productsOfTag;
	}
	
}
