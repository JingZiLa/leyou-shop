package com.leyou.goods.service.impl;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.goods.service.GoodsService;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author Mirror
 * @CreateDate 2020/4/7.
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    /**
     * 加载商品详情数据
     * @param spuId
     * @return
     */
    @Override
    public Map<String,Object> loadItemData (Long spuId) {
        Map<String,Object> model = new HashMap<>();

        //根据spuID查询spu信息
        Spu spu = this.goodsClient.querySpuById(spuId);
        //根据spuId查询spuDetail信息
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
        //根据spuId查询skus信息
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);
        //根据spu的BrandId查询品牌信息
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //根据分类Ids查询分类名称
        List<Long> cids = Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3());
        List<String> names = this.categoryClient.queryNameByids(cids);
        //初始化存储分类信息的List<map<String,Object>>
        List<Map<String,Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }
        //根据三级分类的id查询参数组
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());

        //根据三级分类Id查询特殊的规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        Map<Long,String> paramMap = new HashMap<>();
        params.forEach(param ->{
            paramMap.put(param.getId(),param.getName());
        });

        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("skus",skus);
        model.put("brand",brand);
        model.put("categories",categories);
        model.put("groups",groups);
        model.put("paramMap",paramMap);

        return model;
    }
}
