package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author Mirror
 * @CreateDate 2020/3/18.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LeyouUploadAppcation {

    public static void main(String[] args) {
        SpringApplication.run(LeyouUploadAppcation.class,args);
    }
}
