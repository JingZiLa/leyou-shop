package com.leyou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author Mirror
 * @CreateDate 2020/3/16.
 * Corc跨域请求过滤器
 */
@Configuration
public class LeyouCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        // 初始cors配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许跨域的域名，如果携带Cookie，则不能写*    *：代表所有域名都可以跨域访问
        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
        //是否允许携带cookie
        corsConfiguration.setAllowCredentials(true);
        //* : 代表所有类型的请求方法 GET POST PUT....
        corsConfiguration.addAllowedMethod("*");
        //允许携带任何头信息
        corsConfiguration.addAllowedHeader("*");
        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        //校验所有请求路劲
        corsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        //返回CorsFilter实列，参数:cors配置源对象
        return new CorsFilter(corsConfigurationSource);
    }
}
