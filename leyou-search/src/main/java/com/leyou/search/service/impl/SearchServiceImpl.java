package com.leyou.search.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import com.leyou.search.vo.SearchRequest;
import com.leyou.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Mirror
 * @CreateDate 2020/3/27.
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private GoodsRepository goodsRepository;


    /**
     * 构建goods信息
     * @param spu
     * @return
     * @throws IOException
     */
    @Override
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        //根据分类ID 查询分类名称
        List<String> names = this.categoryClient.queryNameByids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        //根据品牌ID 查询品牌信息
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //根据spuId查询所有的Sku
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spu.getId());

        //初始化价格集合,收集所有的sku价格
        List<Long> prices = new ArrayList<>();

        //收集Sku的必须字段
        List<Map<String,Object>> skuMapLIst = new ArrayList<>();

        skus.forEach(sku -> {
            prices.add(sku.getPrice());

            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            //获取sku的图片，数据库中的图片可能是多张是以 "," 分隔，以逗号切割返回的图片数组，获取第一张图片
            map.put("image",StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);

            skuMapLIst.add(map);
        });

        //根据spu的第三级分类ID 查询所有的规格参数
        List<SpecParam> specParams = this.specificationClient.queryParams(null, spu.getCid3(), null, true);

        //根据spuId查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());

        //反序列化通用的规格参数值
        Map<String, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {});
        //反序列化特殊的规格参数
        Map<String, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {});

        Map<String,Object> specs = new HashMap<>();

        specParams.forEach(param ->{
            //判断规格参数的类型是否为通用的规格参数
            if (param.getGeneric()) {
                //如果是通用类型的参数，从genericSpecMap获取参数
                String value = genericSpecMap.get(param.getId().toString()).toString();
                //判断是否是数值类型，如果是数值类型，应该就是一个区间数值
                if (param.getNumeric()) value = chooseSegment(value,param);

                specs.put(param.getName(),value);
            }else {
                //如果是特殊规格参数，从specialSpecMap获取值
                List<Object> value = specialSpecMap.get(param.getId().toString());
                specs.put(param.getName(),value);
            }
        });
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        //拼接All字段，需要分类名称以及品牌名称
        goods.setAll(spu.getTitle()+" " + StringUtils.join(names," ") + " " + brand.getName());
        //获取Spu的所有sku价格
        goods.setPrice(prices);
        //获取spu下的所有sku，并转化成json字符串
        goods.setSkus(MAPPER.writeValueAsString(skuMapLIst));
        //获取所有的查询规格查询参数{name:value} map
        goods.setSpecs(specs);

        return goods;
    }

    /**
     * 搜索商品
     * @param request
     * @return
     */
    @Override
    public SearchResult search(SearchRequest request) {
        //判断请求条件是否为空
        if (StringUtils.isBlank(request.getKey())){
            return null;
        }
        //自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加查询条件
        //QueryBuilder basicQuery = QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND);
        BoolQueryBuilder basicQuery =  buildBoolQueryBuilder(request);

        queryBuilder.withQuery(basicQuery);
        //添加分页, 分页页码从0开始
        queryBuilder.withPageable(PageRequest.of(request.getPage()-1,request.getSize()));
        //添加结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        //添加聚合
        //定义分类和品牌聚合名称
        String  categoryAggName = "categories";
        String brandAggName = "brands";
        //添加分类和品牌的聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));


        //执行查询  AggregatedPage:除了普通结果集还具有聚合结果集
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());


        //获取聚合结果集并进行解析
        List<Map<String,Object>> categories = getCategoryAggResult(goodsPage.getAggregation(categoryAggName));
        List<Brand> brands = getBrandAggResult(goodsPage.getAggregation(brandAggName));

        List<Map<String,Object>> specs = null;

        //判断是否只有一个分类，只有一个分类时才去做规格参数聚合
        if (!CollectionUtils.isEmpty(categories) && categories.size() == 1) {
            //对规格参数进行聚合
            specs =  getParamAggResult((Long)categories.get(0).get("id"),basicQuery);
        }
        return new SearchResult(goodsPage.getTotalElements(),goodsPage.getTotalPages(),goodsPage.getContent(),categories,brands,specs);
    }

    /**
     * 新增或更新goods信息
     * @param spuId
     */
    @Override
    public void saveGoods(Long spuId) throws IOException {
        Spu spu = this.goodsClient.querySpuById(spuId);
        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }

    /**
     * 删除goods信息
     * @param spuId
     */
    @Override
    public void deleteGoods(Long spuId) {
        this.goodsRepository.deleteById(spuId);
    }

    /**
     * 构建布尔查询
     * @param request
     * @return
     */
    private BoolQueryBuilder buildBoolQueryBuilder(SearchRequest request) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //添加基本查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));

        //添加过滤条件
        //获取用户选择的过滤条件
        Map<String, Object> filter = request.getFilter();
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.equals("品牌",key)) {
                key = "brandId";
            } else if (StringUtils.equals("分类",key)) {
                key = "cid3";
            } else {
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }
        return boolQueryBuilder;
    }

    /**
     * 根据查询条件聚合规格参数
     * @param cid
     * @param basicQuery
     * @return
     */
    private List<Map<String, Object>> getParamAggResult(Long cid, QueryBuilder basicQuery) {
        //自定义查询对象构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加基本查询条件
        queryBuilder.withQuery(basicQuery);
        //查询需要集合的规格参数
        List<SpecParam> specParams = this.specificationClient.queryParams(null, cid, null, true);
        //添加规格参数的聚合
        specParams.forEach(specParam -> {
            queryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword"));
        });
        //添加结果集过滤：不需要普通的结果集
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String []{} ,null));
        //执行聚合查询
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());

        //定义返回结果集
        List<Map<String, Object>> specs = new ArrayList<>();
        //解析聚合结果集
        //map : key = 聚合名称||规格参数名  value = 聚合对象
        Map<String, Aggregation> aggregationMap = goodsPage.getAggregations().asMap();
        for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
            //初始化map key = 规格参数名 options = 聚合规格参数值
            Map<String,Object> map = new HashMap<>();
            map.put("key",entry.getKey());

            //获取聚合并获取聚合中的桶收集聚合的规格参数值
            //初始化用于收集规格参数值的集合
            List<String> options = new ArrayList<>();
            ((StringTerms)entry.getValue()).getBuckets().forEach(bucket -> {
                options.add(bucket.getKeyAsString());
            });
            map.put("options",options);
            specs.add(map);
        }
        return specs;
    }

    /**
     * 解析分类的聚合结果集
     * @param aggregation
     * @return
     */
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        //获取桶的集合转化成List<Map<String,Object>>
        return ((LongTerms)aggregation).getBuckets().stream().map(bucket -> {
            Map<String,Object> map = new HashMap<>();
            //获取桶的key=分类id
            Long id = bucket.getKeyAsNumber().longValue();
            //根据分类id查询分类名称
            List<String> names = this.categoryClient.queryNameByids(Arrays.asList(id));

            map.put("id",id);
            map.put("name",names.get(0));
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * 解析品牌的聚合结果集
     * @param aggregation
     * @return
     */
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        //通过解析聚合中的桶得到品牌信息结果集
        return ((LongTerms) aggregation).getBuckets().stream().map(bucket -> {
            //通过桶的KEY去查询对应的品牌信息并返回
           return this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());
    }



    /**
     * 处理过滤参数中的区间数值参数
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }



}
