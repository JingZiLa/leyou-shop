package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Mirror
 * @CreateDate 2020/4/11.
 * 用户Api接口
 */
public interface UserApi {
    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public User queryUser(@RequestParam("username")String username, @RequestParam("password")String password);

}
