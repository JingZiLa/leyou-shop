package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/23.
 */
public interface GoodsApi {

    /**
     * 根据SpuID查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);
    /**
     * 根据条件查询商品信息（分页）
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    PageResult<SpuBo> querySpuBoByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows
    );
    /**
     * 根据SpuID查询Sku信息
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkusBySpuId(@RequestParam(name = "id")Long spuId);

    /**
     * 根据SpuId查询spu信息
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}")
    Spu querySpuById(@PathVariable("spuId") Long spuId);

    /**
     * 根据skuId查询sku信息
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    Sku querySkuById(@PathVariable("skuId")Long skuId);
}
