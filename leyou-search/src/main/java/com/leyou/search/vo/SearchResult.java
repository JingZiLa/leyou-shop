package com.leyou.search.vo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.search.pojo.Goods;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author Mirror
 * @CreateDate 2020/4/5.
 *搜索结果数据
 */
@Data
public class SearchResult extends PageResult<Goods> {
    private List<Map<String,Object>> categories; ///分类聚合数据

    private List<Brand> brands; //品牌聚合数据

    private List<Map<String,Object>> specs; //规格参数聚合数据

    public SearchResult() {
    }

    public SearchResult(Long total, List<Goods> list) {
        super(total, list);
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> list) {
        super(total, totalPage, list);
    }

    public SearchResult(List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, List<Goods> list, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, list);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> list, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, list);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
