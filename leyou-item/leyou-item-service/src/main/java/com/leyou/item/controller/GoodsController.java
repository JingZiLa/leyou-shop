package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/23.
 */
@Controller
public class GoodsController {


    @Autowired
    private GoodsService goodsService;

    /**
     * 根据条件查询商品信息（分页）
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuBoByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows
    ){
        PageResult<SpuBo> spuBoPageResult = this.goodsService.querySpuBoByPage(key,saleable,page,rows);

        if (CollectionUtils.isEmpty(spuBoPageResult.getList())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuBoPageResult);
    }

    /**
     * 新增商品
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据SpuID查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(spuId);
        if (spuDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据SpuID查询Sku信息
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam(name = "id")Long spuId){
        List<Sku> skus = this.goodsService.querySkusBySpuId(spuId);
        if (CollectionUtils.isEmpty(skus)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * 更新商品信息
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        this.goodsService.updateGoods(spuBo);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据SpuId查询spu信息
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("spuId") Long spuId) {
        Spu spu = this.goodsService.querySpuById(spuId);

        if (spu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }

    /**
     * 根据skuId查询sku信息
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("skuId")Long skuId) {
        Sku sku = this.goodsService.querySkuById(skuId);
        if (sku == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sku);
    }

    /**
     * 根据SpuID删除商品
     * @param id
     * @return
     */
    @GetMapping("/goods/del/{id}")
    public ResponseEntity<Void> delGoods(@PathVariable("id") Long id) {
        this.goodsService.delGoods(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据spuID上下架商品
     * @param id
     * @return
     */
    @GetMapping("/goods/soldOut/{id}")
    public ResponseEntity<Void> goodsSoldOut(@PathVariable("id")Long id) {
        this.goodsService.goodsSoldOut(id);
        return ResponseEntity.noContent().build();
    }
}
