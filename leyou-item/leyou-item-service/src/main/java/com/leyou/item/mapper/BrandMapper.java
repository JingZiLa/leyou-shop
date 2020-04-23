package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 品牌mapper
 */
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 新增商品分类和品牌中间表数据
     * @param cid 商品分类id
     * @param bid 品牌id
     * @return
     */
    @Insert("INSERT INTO tb_category_brand(category_id, brand_id) VALUES (#{cid},#{bid})")
    void insertBrandAndCategory(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据分类ID查询品牌
     * @param cid
     * @return
     */
    @Select("SELECT * FROM tb_brand b INNER JOIN tb_category_brand c ON b.id = c.brand_id WHERE c.category_id=#{cid}")
    List<Brand> queryBrandsByCid(Long cid);

    @Delete("DELETE FROM tb_category_brand  WHERE brand_id = #{bid}")
    void delBrandCategoryByBrandId(@Param("bid") Long bid);

}