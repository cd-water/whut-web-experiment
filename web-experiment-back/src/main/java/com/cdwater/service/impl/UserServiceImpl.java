package com.cdwater.service.impl;

import com.cdwater.mapper.UserMapper;
import com.cdwater.pojo.PageResult;
import com.cdwater.pojo.User;
import com.cdwater.pojo.UserQueryParam;
import com.cdwater.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insertUser(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateUser(user);
    }

    @Override
    public PageResult<User> selectUserByPage(UserQueryParam userQueryParam) {
        PageHelper.startPage(userQueryParam.getPage(), userQueryParam.getPageSize());
        List<User> userList = userMapper.selectUserByPage(userQueryParam);
        Page<User> p = (Page<User>) userList;
        return new PageResult<User>(p.getTotal(), p.getResult());
    }

    @Override
    public User selectUserById(Integer id) {
        return userMapper.selectUserById(id);
    }
}
