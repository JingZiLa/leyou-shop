package com.leyou.user.service;

import com.leyou.user.pojo.User;

/**
 * @Author Mirror
 * @CreateDate 2020/4/9.
 * 用户业务层接口
 */
public interface UserService {
    Boolean checkUserData(String data, Integer type);

    void sendVerifyCode(String phone);

    void register(User user, String code);

    User queryUser(String username, String password);
}
