package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @Author Mirror
 * @CreateDate 2020/4/6.
 */
@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;
    /**
     * 加载商品详情也数据
     * @param spuId
     * @param model
     * @return
     */
    @GetMapping("item/{spuId}.html")
    public String toItemPage(@PathVariable("spuId")Long spuId, Model model) {
        Map<String, Object> map = this.goodsService.loadItemData(spuId);
        model.addAllAttributes(map);
        //生成静态页面
        this.goodsHtmlService.createHtml(spuId);
        return "item";
    }
}
