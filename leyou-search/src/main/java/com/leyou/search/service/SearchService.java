package com.leyou.search.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.vo.SearchRequest;
import com.leyou.search.vo.SearchResult;

import java.io.IOException;

/**
 * @Author Mirror
 * @CreateDate 2020/4/3.
 */
public interface SearchService {

   Goods buildGoods(Spu spu) throws IOException;


    SearchResult search(SearchRequest request);

    void saveGoods(Long spuId) throws IOException;

    void deleteGoods(Long spuId);
}
