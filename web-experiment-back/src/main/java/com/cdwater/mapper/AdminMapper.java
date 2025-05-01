package com.cdwater.mapper;

import com.cdwater.pojo.Admin;
import com.cdwater.pojo.AdminQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("select id, username, password, name, phone, email, birthday, money, address, avatar,essay_count from admin where username=#{username}")
    Admin selectAdminByUsername(String username);

    @Update("update admin set name=#{name},phone=#{phone},email=#{email},birthday=#{birthday},money=#{money}," +
            "address=#{address},avatar=#{avatar} where username=#{username}")
    void updateAdminInfo(Admin admin);

    @Update("update admin set password=#{newPassword} where username=#{username}")
    void updateAdminPassword(Admin admin);

    @Select("select *from admin")
    List<Admin> selectAdminByPage(AdminQueryParam adminQueryParam);

    @Select("select id,username from admin where username=#{username} and password=#{password}")
    Admin login(Admin admin);

    @Insert("insert into admin (username, password, email, birthday, avatar) values " +
            "(#{username},#{password},#{email},#{birthday},#{avatar})")
    void register(Admin admin);
}
