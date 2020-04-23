package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/19.
 *
 * 规格参数Controller
 */
@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 根据条件 查询 具体的规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    List<SpecParam> queryParams(
            @RequestParam(name = "gid", required = false) Long gid,
            @RequestParam(name = "cid",required = false) Long cid,
            @RequestParam(value = "generic", required = false)Boolean generic,
            @RequestParam(value = "searching", required = false)Boolean searching
    );

    /**
     * 根据分类id查询规格参数组信息
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    List<SpecGroup> queryGroupsWithParam(@PathVariable("cid") Long cid);
}
