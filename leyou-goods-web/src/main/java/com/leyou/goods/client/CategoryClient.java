package com.leyou.goods.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author Mirror
 * @CreateDate 2020/3/27.
 */
@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi {

}
