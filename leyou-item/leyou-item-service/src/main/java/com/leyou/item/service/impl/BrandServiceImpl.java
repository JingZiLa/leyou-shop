package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/17.
 *
 * 品牌业务处理接口实现
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryService categoryService;
    /**
     * 根据条件查询品牌信息(分页)
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        //根据name模糊查询，或者根据首字母查询品牌信息
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("name","%" + key +"%").orEqualTo("letter",key);
        }
        //添加分页条件
        PageHelper.startPage(page,rows);
        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+ (desc ? "desc":"asc"));
        }

        List<Brand> brands = this.brandMapper.selectByExample(example);
        if(rows != null && rows < 0 || rows == -1){
            rows = brands.size();
            //添加分页条件
            PageHelper.startPage(page,rows);
            //执行查询，获取List<Spu>
            brands = this.brandMapper.selectByExample(example);
        }
        //包装结果数据
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        PageResult<Brand> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;
    }

    /**
     * 保存新增品牌信息
     * @param brand
     * @param cids
     * @return
     */
    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌信息
        this.brandMapper.insert(brand);
        //新增品牌与分类关系
        cids.forEach(cid -> {
            this.brandMapper.insertBrandAndCategory(cid, brand.getId());
        });

    }

    /**
     * 根据分类ID查询品牌
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return this.brandMapper.queryBrandsByCid(cid);
    }
    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @Override
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改品牌信息
     * @param brand
     */
    @Override
    public void editBrand(Brand brand , List<Long> cids) {
        for (int i = 0 ; i < cids.size(); i++) {
            List<Brand> brands = queryBrandsByCid(cids.get(i));
            if (!CollectionUtils.isEmpty(brands)){
                this.delBrandCategoryByBrandId(brand.getId());
            }
        }
        //更新品牌
        this.brandMapper.updateByPrimaryKeySelective(brand);
        //添加品牌于分类的关系
        cids.forEach(cid -> this.brandMapper.insertBrandAndCategory(cid,brand.getId()));
    }


    /**
     * 根据品牌ID删除品牌分类中间表数据
     * @param bid
     */
    @Override
    public void delBrandCategoryByBrandId(Long bid) {
        this.brandMapper.delBrandCategoryByBrandId(bid);
    }

    /**
     * 根据品牌ID删除品牌信息
     * @param id
     */
    @Override
    public void delBrandByBrandId(Long id) {

        this.brandMapper.deleteByPrimaryKey(id);

        List<Category> categories = this.categoryService.queryByBrandId(id);
        if (!CollectionUtils.isEmpty(categories)) {
            this.delBrandCategoryByBrandId(id);
        }

    }


}
