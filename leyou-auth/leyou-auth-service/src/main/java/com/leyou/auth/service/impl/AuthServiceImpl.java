package com.leyou.auth.service.impl;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Mirror
 * @CreateDate 2020/4/11.
 * 授权中心业务层实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登陆认证方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public String login(String username, String password) {
        //根据用户名查询用户
        User user = this.userClient.queryUser(username, password);
        //判断用户是否存在
        if (user == null) {
            return null;
        }

        try {
            //生成token
            return JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
