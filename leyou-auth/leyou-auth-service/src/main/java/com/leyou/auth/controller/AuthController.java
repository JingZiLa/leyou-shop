package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Mirror
 * @CreateDate 2020/4/11.
 */
@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登陆方法
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/user/login")
    public ResponseEntity<Void> login(
            @RequestParam("username")String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {
      String token = this.authService.login(username,password);

      if (StringUtils.isBlank(token)) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
        CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getExpire()*60);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 验证登陆用户信息
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/user/verify")
    public ResponseEntity<UserInfo> userVerify(
            @CookieValue("LY_TOKEN")String token,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            //使用公钥解析jwt
            UserInfo   user = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            //刷新jwt有效时间
            JwtUtils.generateToken(user,jwtProperties.getPrivateKey(),jwtProperties.getExpire());
            //刷新cookie有效时间
            CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getExpire()*60);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
