package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Mirror
 * @CreateDate 2020/3/19.
 *
 * 规格参数Controller
 */
@Controller
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;


    /**
     * 根据分类id查询规格参数分组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable(name = "cid") Long cid){
        List<SpecGroup> specGroups = this.specificationService.queryGroupsByCid(cid);
        if (specGroups != null && CollectionUtils.isEmpty(specGroups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specGroups);
    }

    /**
     * 根据条件 查询 具体的规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(name = "gid", required = false) Long gid,
            @RequestParam(name = "cid",required = false) Long cid,
            @RequestParam(value = "generic", required = false)Boolean generic,
            @RequestParam(value = "searching", required = false)Boolean searching
    ){
        List<SpecParam> specParams = this.specificationService.queryParams(gid,cid,generic,searching);
        if (specParams != null && CollectionUtils.isEmpty(specParams)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specParams);
    }

    /**
     * 根据分类id查询规格参数组信息
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("cid") Long cid) {
        List<SpecGroup> groups = this.specificationService.queryGroupsWithParam(cid);
        if (groups != null && CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 添加规格参数组
     * @param specGroup
     * @return
     */
    @PostMapping("/group")
    public ResponseEntity<Void> addSpecGroup(@RequestBody SpecGroup specGroup) {
        this.specificationService.addSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 修改规格参数组
     * @param specGroup
     * @return
     */
    @PutMapping("/group")
    public ResponseEntity<Void> editSpecGroup(@RequestBody SpecGroup specGroup) {
        this.specificationService.editSpecGroup(specGroup);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 添加具体的规格参数
     * @param specParam
     * @return
     */
    @PostMapping("/param")
    public ResponseEntity<Void> addSpecParam(@RequestBody SpecParam specParam) {
        this.specificationService.addSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 修改具体规格参数组
     * @param specParam
     * @return
     */
    @PutMapping("/param")
    public ResponseEntity<Void> editSpecParam(@RequestBody SpecParam specParam) {
        this.specificationService.editSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除规格参数组
     * @param id
     * @return
     */
    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void>  delSpecGroup(@PathVariable("id")Long id) {
        this.specificationService.delSpecGroup(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * 删除具体的规格参数
     * @param id
     * @return
     */
    @DeleteMapping("/param/{id}")
    public ResponseEntity<Void>  delSpecParam(@PathVariable("id") Long id) {
        this.specificationService.delSpecParam(id);
        return ResponseEntity.noContent().build();
    }
}
