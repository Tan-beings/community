package com.tanquandan.blogsystem.Mapper;

import com.tanquandan.blogsystem.DAO.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into user value(#{id},#{account_id},#{name},#{token},#{gmt_create},#{gmt_modified},#{bio},#{avatar})")
    int insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(String token);

    @Select("select * from user where account_id = #{account_id}")
    User findById(String account_id);
}
