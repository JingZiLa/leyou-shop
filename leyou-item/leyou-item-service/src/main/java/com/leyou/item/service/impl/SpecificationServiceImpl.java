package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/19.
 * 规格参数组 And 规格参数 业务接口
 * 因为SpecGroupMapper  And SpecParam 是紧紧相连的并且业务操作也不复杂，所以可以用同一个Service处理业务。
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {


    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据分类id查询规格参数分组
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.specGroupMapper.select(specGroup);
    }

    /**
     * 根据条件 查询 具体的规格参数
     * @param gid
     * @return
     */
    @Override
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        return this.specParamMapper.select(specParam);
    }

    /**
     * 根据分类ID查询规格参数组信息
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupsWithParam(Long cid) {
        List<SpecGroup> specGroups = this.queryGroupsByCid(cid);
        specGroups.forEach(group -> {
            List<SpecParam> specParams = this.queryParams(group.getId(),null,null,null);
            group.setParams(specParams);
        });
        return specGroups;
    }

    /**
     * 添加规格参数组
     * @param specGroup
     * @return
     */
    @Override
    public void addSpecGroup(SpecGroup specGroup) {
        this.specGroupMapper.insertSelective(specGroup);
    }
    /**
     * 修改规格参数组
     * @param specGroup
     * @return
     */
    @Override
    public void editSpecGroup(SpecGroup specGroup) {
        this.specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }

    /**
     * 添加具体的规格参数组
     * @param specParam
     * @return
     */
    @Override
    public void addSpecParam(SpecParam specParam) {
        this.specParamMapper.insertSelective(specParam);

    }
    /**
     * 添加具体的规格参数组
     * @param specParam
     * @return
     */
    @Override
    public void editSpecParam(SpecParam specParam) {
        this.specParamMapper.updateByPrimaryKeySelective(specParam);
    }
    /**
     * 删除具体的规格参数组
     * @param id
     * @return
     */
    @Override
    public void delSpecGroup(Long id) {

        this.specGroupMapper.deleteByPrimaryKey(id);

        Example example = new Example(SpecParam.class);
        example.createCriteria().andEqualTo("groupId",id);
        this.specParamMapper.deleteByExample(example);
    }
    /**
     * 删除具体的规格参数
     * @param id
     * @return
     */
    @Override
    public void delSpecParam(Long id) {
        this.specParamMapper.deleteByPrimaryKey(id);
    }
}
