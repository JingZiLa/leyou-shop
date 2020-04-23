package com.leyou.search.controller;

import com.leyou.search.service.SearchService;
import com.leyou.search.vo.SearchRequest;
import com.leyou.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author Mirror
 * @CreateDate 2020/4/4.
 * 搜索微服务控制层
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;


    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request){
        SearchResult result = this.searchService.search(request);
        if (result == null || CollectionUtils.isEmpty(result.getList())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
