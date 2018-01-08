package com.zhongjian.webserver.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.User;

public interface UserMapper {

    int insertSelective(User record);

    Integer checkUserNameExists(String userName);
    
    Integer selectUserMaxSysID();
    
    Integer checkUserNameAndPassword(@Param("userName") String userName,@Param("password") String password);
    
    Integer checkUserNameAndPayPassword(@Param("userName") String userName,@Param("payPassword") String payPassword);
    
    Integer updatePassword(@Param("userName") String userName,@Param("password") String password);
    
    Map<String, Object> selectPersonalInform(String userName);
    
    Integer getUserIdByUserName(String userName);
    
    Integer updateByUserNameSelective(User user);
    
    Integer InviteCodeIsExists(Integer inviteCode);
        
}