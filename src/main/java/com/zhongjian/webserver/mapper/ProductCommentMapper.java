package com.zhongjian.webserver.mapper;

import org.apache.ibatis.annotations.Param;

import com.zhongjian.webserver.pojo.ProductComment;

public interface ProductCommentMapper {

    int insertSelective(ProductComment record);
    
    void insertProductCommetPhoto(@Param("CommentId") Integer commentId,@Param("Photo") String photo);

}