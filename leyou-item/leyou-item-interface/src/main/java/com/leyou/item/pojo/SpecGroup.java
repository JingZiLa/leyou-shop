package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;

    @Transient  //由于数据库没有该字段，使用Transient进行忽略
    private List<SpecParam> params;
}