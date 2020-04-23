package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/17.
 * 品牌业务处理接口
 */
public interface BrandService {
    PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    void saveBrand(Brand brand, List<Long> cids);

    List<Brand> queryBrandsByCid(Long cid);

    Brand queryBrandById(Long id);

    void editBrand(Brand brand ,List<Long> cids);

    void delBrandCategoryByBrandId(Long bid);

    void delBrandByBrandId(Long id);
}
