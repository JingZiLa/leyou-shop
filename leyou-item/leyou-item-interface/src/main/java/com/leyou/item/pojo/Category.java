package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Mirror
 * @CreateDate 2020/3/16.
 * 商品分类实体
 */
@Data
@Table(name="tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;    //分类ID
    private String name;    //分类名称
    private Long parentId;  //父类目ID，顶级类目为0
    private Boolean isParent;   //  是否为父节点 ？ 0：否 1：是
    private Integer sort;       //  排序指数，越小越靠前


}
