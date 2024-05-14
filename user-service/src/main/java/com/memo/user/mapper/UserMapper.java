package com.memo.user.mapper;

import com.memo.user.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select("select user.id,user.username,user.password from user where binary username =#{username}")
    User findByUserName(String username);

    @Insert("insert into `memo-user`.user (id, username, password) VALUES (#{id},#{username},MD5(#{password}))")
    void add(Long id, String username, String password);
}
