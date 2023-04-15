package com.tanquandan.blogsystem.Mapper;

import com.tanquandan.blogsystem.DAO.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user value(#{id},#{account_id},#{name},#{token},#{gmt_create},#{gmt_modified},#{bio})")
    int insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(String token);
}
