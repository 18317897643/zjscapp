package com.zhongjian.webserver.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderHeadDto {

	private Integer id; 
	
	private Date createTime;

	private String orderNo;

	private Integer userId;

	private String receiverName;

	private String receiverPhone;

	private Integer countryId;

	private Integer provinceId;

	private Integer cityId;

	private Integer regionId;

	private String address;

	private BigDecimal freight;

	private String memo;

	private BigDecimal totalAmount;

	private BigDecimal useElecNum;

	private BigDecimal useCoupon;

	private BigDecimal usePointNum;

	private BigDecimal useVIPRemainNum;

	private BigDecimal realPay;
	
	private Integer curStatus;
	
	private String producerno;
	
	private String producertel;
	
	private String producername;

	private List<OrderLineDto> orderLines;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getUseElecNum() {
		return useElecNum;
	}

	public void setUseElecNum(BigDecimal useElecNum) {
		this.useElecNum = useElecNum;
	}

	public BigDecimal getUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(BigDecimal useCoupon) {
		this.useCoupon = useCoupon;
	}

	public BigDecimal getUsePointNum() {
		return usePointNum;
	}

	public void setUsePointNum(BigDecimal usePointNum) {
		this.usePointNum = usePointNum;
	}

	public BigDecimal getUseVIPRemainNum() {
		return useVIPRemainNum;
	}

	public void setUseVIPRemainNum(BigDecimal useVIPRemainNum) {
		this.useVIPRemainNum = useVIPRemainNum;
	}

	public List<OrderLineDto> getOrderLines() {
		return orderLines;
	}

	public void setOrderLines(List<OrderLineDto> orderLines) {
		this.orderLines = orderLines;
	}

	public BigDecimal getRealPay() {
		return realPay;
	}

	public void setRealPay(BigDecimal realPay) {
		this.realPay = realPay;
	}

	public Integer getCurStatus() {
		return curStatus;
	}

	public void setCurStatus(Integer curStatus) {
		this.curStatus = curStatus;
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
	
	

}
