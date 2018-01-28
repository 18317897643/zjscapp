package com.zhongjian.webserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.Bankcardlist;

public interface BankcardlistMapper {
    int deleteBankAccount(@Param("id") Integer id,@Param("userId") Integer userId);

    int insert(Bankcardlist record);

    List<Bankcardlist> selectByUserId(Integer userId);

}