package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/19.
 * 规格参数组 and 规格参数 业务接口
 */
public interface SpecificationService {
    List<SpecGroup> queryGroupsByCid(Long cid);

    List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching);

    List<SpecGroup> queryGroupsWithParam(Long cid);

    void addSpecGroup(SpecGroup specGroup);

    void editSpecGroup(SpecGroup specGroup);

    void addSpecParam(SpecParam specParam);

    void editSpecParam(SpecParam specParam);

    void delSpecGroup(Long id);

    void delSpecParam(Long id);
}
