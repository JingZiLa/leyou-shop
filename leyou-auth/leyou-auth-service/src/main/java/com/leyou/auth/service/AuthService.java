package com.leyou.auth.service;

/**
 * @Author Mirror
 * @CreateDate 2020/4/11.
 * 授权中心业务层接口
 */
public interface AuthService {
    String login(String username, String password);
}
