package com.cdwater.controller;

import com.cdwater.pojo.PageResult;
import com.cdwater.pojo.Result;
import com.cdwater.pojo.User;
import com.cdwater.pojo.UserQueryParam;
import com.cdwater.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public Result insertUser(@RequestBody User user){
        log.info("新增用户：{}",user);
        userService.insertUser(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id){
        log.info("删除用户：{}",id);
        userService.deleteUser(id);
        return Result.success();
    }

    @PutMapping
    public Result updateUser(@RequestBody User user){
        log.info("修改用户：{}",user);
        userService.updateUser(user);
        return Result.success();
    }

    @GetMapping
    public Result selectUserByPage(UserQueryParam userQueryParam){
        log.info("分页查询用户：{}",userQueryParam);
        PageResult<User> pageResult=userService.selectUserByPage(userQueryParam);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result selectUserById(@PathVariable Integer id){
        log.info("ID查询用户：{}",id);
        User user=userService.selectUserById(id);
        return Result.success(user);
    }
}
