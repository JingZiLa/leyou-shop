package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/23.
 * 商品业务处理接口
 */
public interface GoodsService {
    PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows);


    void saveGoods(SpuBo spuBo);

    SpuDetail querySpuDetailBySpuId(Long spuId);

    List<Sku> querySkusBySpuId(Long spuId);

    void updateGoods(SpuBo spuBo);

    Spu querySpuById(Long spuId);

    Sku querySkuById(Long skuId);

    void delGoods(Long id);

    void goodsSoldOut(Long id);
}
