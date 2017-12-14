package com.zhongjian.webserver.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Province {
    private Integer id;

    private Integer countryid;

    private String provincename;

    private Integer curstatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCountryid() {
        return countryid;
    }

    public void setCountryid(Integer countryid) {
        this.countryid = countryid;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public Integer getCurstatus() {
        return curstatus;
    }

    public void setCurstatus(Integer curstatus) {
        this.curstatus = curstatus;
    }
}