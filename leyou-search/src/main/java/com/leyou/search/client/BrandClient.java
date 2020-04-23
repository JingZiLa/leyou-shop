package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author Mirror
 * @CreateDate 2020/3/27.
 */
@FeignClient(value = "item-service")
public interface BrandClient extends BrandApi {
}
