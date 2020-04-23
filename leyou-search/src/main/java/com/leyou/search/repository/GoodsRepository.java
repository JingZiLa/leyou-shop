package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author Mirror
 * @CreateDate 2020/4/3.
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
