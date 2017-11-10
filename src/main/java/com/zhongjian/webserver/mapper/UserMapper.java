package com.zhongjian.webserver.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.zhongjian.webserver.pojo.User;

@Mapper
public interface UserMapper {
	User findUserById(Integer id);
}
