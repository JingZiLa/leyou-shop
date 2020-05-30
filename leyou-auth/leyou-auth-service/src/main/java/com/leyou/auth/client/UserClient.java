package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author Mirror
 * @CreateDate 2020/4/11.
 */
@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}
