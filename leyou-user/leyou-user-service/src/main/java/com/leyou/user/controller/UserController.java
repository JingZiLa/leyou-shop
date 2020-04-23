package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @Author Mirror
 * @CreateDate 2020/4/9.
 * 用户控制层
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 校验用户数据是否可用
     * @param data
     * @param type
     * @return  Boolean
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData (@PathVariable("data") String data,@PathVariable("type") Integer type) {
        Boolean boo = this.userService.checkUserData(data, type);
        if (boo == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boo);
    }

    /**
     * 生成短信验证码
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone) {
        this.userService.sendVerifyCode(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 用户注册
     * @param user
     * @param code
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code) {
        this.userService.register(user,code);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam("password")String password) {
        User user = this.userService.queryUser(username,password);
        if (user == null) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}
