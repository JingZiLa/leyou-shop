package com.leyou.common.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/17.
 * 分页结果集封装类
 */
@Data
public class PageResult<T> {

    private Long total; //总条数
    private Integer totalPage;  //总页数
    private List<T> list;       //当前页数据

    public PageResult() {
    }

    public PageResult(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public PageResult(Long total, Integer totalPage, List<T> list) {
        this.total = total;
        this.totalPage = totalPage;
        this.list = list;
    }
}

