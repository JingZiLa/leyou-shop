package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/15.
 *
 * 商品类别 Controller
 */
@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据多个分类ID查询分类名称
     * @param ids
     * @return
     */
    @GetMapping
    public List<String > queryNameByids(@RequestParam(name = "ids")List<Long> ids);
}
