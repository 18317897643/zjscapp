package com.zhongjian.webserver.dto;

import java.util.List;

public class OrderHeadEXDto {

	private Integer adrressId;
	
	private List<OrderHeadDto> orderHeads;


	public Integer getAdrressId() {
		return adrressId;
	}

	public void setAdrressId(Integer adrressId) {
		this.adrressId = adrressId;
	}

	public List<OrderHeadDto> getOrderHeads() {
		return orderHeads;
	}

	public void setOrderHeads(List<OrderHeadDto> orderHeads) {
		this.orderHeads = orderHeads;
	}
	
}
