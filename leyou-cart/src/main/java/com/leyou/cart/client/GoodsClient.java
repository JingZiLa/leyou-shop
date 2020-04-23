package com.leyou.cart.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author Mirror
 * @CreateDate 2020/3/27.
 */
@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsApi {
}
