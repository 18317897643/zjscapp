package com.zhongjian.webserver.mapper;



import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    Integer checkUserNameExists(String userName);
    
    Integer selectUserMaxSysID();
    
    Integer checkUserNameAndPassword(@Param("userName") String userName,@Param("password") String password);
    
    Integer updatePassword(@Param("userName") String userName,@Param("password") String password);
    
}