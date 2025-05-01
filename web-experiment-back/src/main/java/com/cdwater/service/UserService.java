package com.cdwater.service;

import com.cdwater.pojo.PageResult;
import com.cdwater.pojo.User;
import com.cdwater.pojo.UserQueryParam;

public interface UserService {
    void insertUser(User user);

    void deleteUser(Integer id);

    void updateUser(User user);

    PageResult<User> selectUserByPage(UserQueryParam userQueryParam);

    User selectUserById(Integer id);
}
