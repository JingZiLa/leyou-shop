package com.leyou.auth.common.test;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.common.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "D:\\leyou\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\leyou\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

//    @Test
//    public void testRsa() throws Exception {
//        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
//    }
//
//    @Before
//    public void testGetRsa() throws Exception {
//        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
//        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
//    }
//
//    @Test
//    public void testGenerateToken() throws Exception {
//        // 生成token
//        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
//        System.out.println("token = " + token);
//    }
//
//    @Test
//    public void testParseToken() throws Exception {
//        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU4NjUxNDQ0Nn0.RYzWjlQET2m5_3Uu1vw0BKfteRvani1Id7YLuyCsefZbu9UzSPXU6Mq9mauKPsgtp9Cb1DcWikfOdBrG9Qs-jPxq6YbC2hegbDshj1pXaHuYT7kfWnC9ubws-GKPb9te_ECH8X5JfKWeePtO7qzMljr4UNAt45Z5QmLGer7J9Bk";
//
//        // 解析token
//        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
//        System.out.println("id: " + user.getId());
//        System.out.println("userName: " + user.getUsername());
//    }
}