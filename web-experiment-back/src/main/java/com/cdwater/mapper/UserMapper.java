package com.cdwater.mapper;

import com.cdwater.pojo.User;
import com.cdwater.pojo.UserQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into user(name,province,city,address,zip,update_time) " +
            "values (#{name},#{province},#{city},#{address},#{zip},#{updateTime})")
    void insertUser(User user);

    @Delete("delete from user where id=#{id}")
    void deleteUser(Integer id);

    @Update("update user set name=#{name},province=#{province},city=#{city}" +
            ",address=#{address},zip=#{zip},update_time=#{updateTime} where id=#{id}")
    void updateUser(User user);

    List<User> selectUserByPage(UserQueryParam userQueryParam);

    @Select("select id,name,province,city,address,zip,update_time from user where id=#{id}")
    User selectUserById(Integer id);
}
