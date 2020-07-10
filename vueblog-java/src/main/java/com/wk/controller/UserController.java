package com.wk.controller;

import com.wk.entity.User;
import com.wk.service.UserService;
import com.wk.utils.Result;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService  userService;

    @RequiresAuthentication     //登录后才能访问
    @GetMapping("/{id}")
    public Result<User> getOne(@PathVariable("id") Long id){
        return Result.ok(userService.getById(id));
    }

    @PostMapping
    public Result<User> save(@Validated @RequestBody User user) {
        /*userService.save(user);*/
        return Result.ok(user);
    }
}

