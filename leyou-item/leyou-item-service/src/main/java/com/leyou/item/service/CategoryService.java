package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/15.
 *
 */
public interface CategoryService {
    List<Category> queryCategoriesByPid(Long pid);

    List<String> queryNamesByIds(List<Long> ids);

    void addCategory(Category category);

    void editCategory(Category category);

    void delCategory(Long id);

    List<Category> queryByBrandId(Long bid);

}
