package com.leyou.user.service.impl;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author Mirror
 * @CreateDate 2020/4/9.
 * 用户业务层实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final  String KEY_PREFIX = "user:verify";

    @Autowired
    private UserMapper userMapper;
    /**
     * 校验用户数据是否可用
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkUserData(String data, Integer type) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        User redord = new User();
        if (type == 1) {
            redord.setUsername(data);
        } else if (type == 2) {
            redord.setPhone(data);
        } else {
            return null;
        }
        return this.userMapper.selectCount(redord) == 0;
    }

    @Override
    public void sendVerifyCode(String phone) {
        if(StringUtils.isBlank(phone)) {
            return;
        }

        //生成随机验证码
        String code = NumberUtils.generateCode(6);
        //发送消息到rabbitmq
        Map<String, String> msg = new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        System.out.println("手机验证码："+code);
        this.amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE","verifycode.sms",msg);
        //保存验证码到redis:十分钟有效期
        this.stringRedisTemplate.opsForValue().set(KEY_PREFIX+phone,code,10, TimeUnit.MINUTES);
    }

    /**
     * 用户注册
     * @param user
     * @param code
     */
    @Override
    public void register(User user, String code) {
        //获取redis中的验证码
        String redisCode = this.stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //校验验证码
        if (!StringUtils.equals(code,redisCode)) {
            return;
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        //加盐加密
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        //新增用户
        user.setId(null);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);
        //删除redis缓存中的验证码
        this.stringRedisTemplate.delete(KEY_PREFIX + user.getPhone());

    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        //根据用户名查询用户信息
        User user = this.userMapper.selectOne(record);
        //判断用户信息是否为空
        if (user == null) {
            return null;
        }
        //获取盐，对用户输入的密码进行加密
        password = CodecUtils.md5Hex(password,user.getSalt());

        //与数据库中的密码比较
        if (StringUtils.equals(password,user.getPassword())) {
            return user;
        }
        return null;
    }
}
