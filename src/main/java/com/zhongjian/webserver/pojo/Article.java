package com.zhongjian.webserver.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Article {
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createtime;
    
    private String title;
    
    private String memo;

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
