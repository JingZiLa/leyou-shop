package com.leyou.gateway.Filter;

import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/4/11.
 * 登陆过滤器
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        //初始化zuul运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取访问路径
        String url = context.getRequest().getRequestURI();

        //获取白名单
        List<String> allowPaths = filterProperties.getAllowPaths();

        for (String allowPath : allowPaths) {
            if (StringUtils.contains(url,allowPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        //初始化zuul运行上下文
        RequestContext context = RequestContext.getCurrentContext();

        String token = CookieUtils.getCookieValue(context.getRequest(), jwtProperties.getCookieName());

        if (StringUtils.isBlank(token)) {
            //不转发
            context.setSendZuulResponse(false);
            //设置响应状态码
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        try {

            JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

        } catch (Exception e) {
            e.printStackTrace();
            //不转发
            context.setSendZuulResponse(false);
            //设置响应状态码
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
