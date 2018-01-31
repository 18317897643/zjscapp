package com.zhongjian.webserver.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SigninAward {
    private Integer id;

    private Integer userid;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date date;

    private Integer sevendaysaward;

    private Integer fourteendaysaward;

    private Integer thirtydaysaward;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

	public Integer getSevendaysaward() {
		return sevendaysaward;
	}

	public void setSevendaysaward(Integer sevendaysaward) {
		this.sevendaysaward = sevendaysaward;
	}

	public Integer getFourteendaysaward() {
		return fourteendaysaward;
	}

	public void setFourteendaysaward(Integer fourteendaysaward) {
		this.fourteendaysaward = fourteendaysaward;
	}

	public Integer getThirtydaysaward() {
		return thirtydaysaward;
	}

	public void setThirtydaysaward(Integer thirtydaysaward) {
		this.thirtydaysaward = thirtydaysaward;
	}

}