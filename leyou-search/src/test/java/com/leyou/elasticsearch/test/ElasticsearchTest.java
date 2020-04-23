package com.leyou.elasticsearch.test;


import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Mirror
 * @CreateDate 2020/4/3.
 */
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class ElasticsearchTest {
/*
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void test(){
        //创建索引库
        this.elasticsearchTemplate.createIndex(Goods.class);
        //创建映射
        this.elasticsearchTemplate.putMapping(Goods.class);

        //导入数据
        //分页查询spu
        int page = 1;
        int rows = 100;

        do {
            PageResult<SpuBo> result = this.goodsClient.querySpuBoByPage(null,null,page,rows);
            //获取当前页的数据
            List<SpuBo> items = result.getList();
            //处理List<SpuBo>  ->  List<Goods>
            List<Goods> goodsList = items.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());

            //新增数据
            this.goodsRepository.saveAll(goodsList);

            rows = items.size();
            page++;

        }while (rows == 100);

    }*/
}
