package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/15.
 *
 * 商品类别 Controller
 */
@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点ID查询子节点
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid){
        if (pid == null || pid < 0 ){
            //状态码   400 参数异常
            //1. return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            //2. return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().build();
        }
       List<Category> categories =  this.categoryService.queryCategoriesByPid(pid);
        //判断集合是否为空
        if (CollectionUtils.isEmpty(categories)){
            //状态码： 404 资源服务器未找到
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.notFound().build();
        }
        //状态码 200 成功
        return ResponseEntity.ok(categories);

//      如果程序出错默认就会返回 状态码 500 HttpStatus.INTERNAL_SERVER_ERROR 所以不需要try catch
    }

    /**
     * 根据多个分类ID查询分类名称
     * @param ids
     * @return
     */
    @GetMapping
    public ResponseEntity<List<String >> queryNameByids(@RequestParam(name = "ids")List<Long> ids){
        List<String> names = this.categoryService.queryNamesByIds(ids);
        //判断集合是否为空
        if (CollectionUtils.isEmpty(names)){
            //状态码： 404 资源服务器未找到
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.notFound().build();
        }
        //状态码 200 成功
        return ResponseEntity.ok(names);
    }

    /**
     * 添加分类节点
     * @param category
     */
    @PostMapping("/add")
    public ResponseEntity<Void> addCategory(@RequestBody Category category) {
        this.categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据ID修改分类节点
     * @param category
     */
    @PutMapping("/edit")
    public ResponseEntity<Void> editCategory(@RequestBody Category category) {
        this.categoryService.editCategory(category);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据ID删除节点
     * @param id
     * @return
     */
    @GetMapping("/del/{id}")
    public ResponseEntity<Void> delCategory(@PathVariable("id") Long id) {
        this.categoryService.delCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改品牌信息时，商品分类的回显
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid){
        List<Category> list = this.categoryService.queryByBrandId(bid);
        if(list == null || list.size() < 1){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }
}
