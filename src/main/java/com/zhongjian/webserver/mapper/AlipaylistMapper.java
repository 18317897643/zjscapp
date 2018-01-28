package com.zhongjian.webserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.Alipaylist;

public interface AlipaylistMapper {
    int deleteAliAccount(@Param("id") Integer id,@Param("userId") Integer userId);
 
    int insert(Alipaylist record);

    List<Alipaylist> selectByUserId(Integer userId);

}