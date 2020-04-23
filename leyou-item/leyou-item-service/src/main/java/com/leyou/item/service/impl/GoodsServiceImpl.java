package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Mirror
 * @CreateDate 2020/3/23.
 *
 * 商品业务处理实现
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 根据条件查询商品信息（分页）
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows) {
        //初始化example对象
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //添加查询条件
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }

        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }

        //添加分页条件
        PageHelper.startPage(page,rows);
        //执行查询，获取List<Spu>
        List<Spu> spus = this.spuMapper.selectByExample(example);

        if(rows != null && rows < 0 || rows == -1){
            rows = spus.size();
            //添加分页条件
            PageHelper.startPage(page,rows);
            //执行查询，获取List<Spu>
            spus = this.spuMapper.selectByExample(example);
        }
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);


        //spu集合转化成spubo集合
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            // copy共同属性的值到新的对象
            BeanUtils.copyProperties(spu, spuBo);

            //获取分类名称
            List<String> cname = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(cname," - "));

            //获取品牌名称
            spuBo.setBname(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            return spuBo;
        }).collect(Collectors.toList());
        //返回PageResult<SpuBo>

        return new PageResult<>(pageInfo.getTotal(),spuBos);
    }

    /**
     *
     * 新增商品
     * @param spuBo
     */
    @Override
    public void saveGoods(SpuBo spuBo) {
        //新增spu
        // 设置默认字段
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());

        this.spuMapper.insertSelective(spuBo);
        //新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());

        this.spuDetailMapper.insertSelective(spuDetail);
        //新增sku信息
        this.saveSku(spuBo);
        //发布消息到交换机
        sendMsg("item.insert",spuBo.getId());
    }

    /**
     * 发布订阅消息到交换机
     * @param spuId
     */
    private void sendMsg(String type , Long spuId) {
        try {

            this.amqpTemplate.convertAndSend(type,spuId);
        }catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据SpuID查询SpuDetail
     * @param spuId
     * @return
     */
    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据SpuID查询Sku信息
     * @param spuId
     * @return
     */
    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);
        skus.forEach(sku -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });

        return skus;
    }

    /**
     * 更新商品信息
     * @param spuBo
     */
    @Override
    public void updateGoods(SpuBo spuBo) {
        delSku(spuBo.getId());

        //新增Sku
        //新增Stock
        this.saveSku(spuBo);
        //更新Spu和SpuDetail
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        //发布消息到交换机
        sendMsg("item.update",spuBo.getId());
    }

    private void delSku(Long spuId) {
        //删除Stock
        List<Sku> skus = this.querySkusBySpuId(spuId);
        if (!CollectionUtils.isEmpty(skus)){
            skus.forEach(sku -> {
                this.stockMapper.deleteByPrimaryKey(sku.getId());
            });

            Sku sku = new Sku();
            sku.setSpuId(spuId);
            //删除Sku
            this.skuMapper.delete(sku);
        }
    }

    /**
     * 根据spuId查询信息
     * @param spuId
     * @return
     */
    @Override
    public Spu querySpuById(Long spuId) {
        return this.spuMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据skuId查询sku信息
     * @param skuId
     * @return
     */
    @Override
    public Sku querySkuById(Long skuId) {
        return this.skuMapper.selectByPrimaryKey(skuId);
    }

    /**
     * 根据spuID删除商品
     * @param id
     */
    @Override
    public void delGoods(Long id) {
        this.delSku(id);
        this.spuMapper.deleteByPrimaryKey(id);
        this.spuDetailMapper.deleteByPrimaryKey(id);
        //发布消息到交换机
        sendMsg("item.delete",id);

    }

    /**
     * 根据SpuID上下架商品
     * @param id
     */
    @Override
    public void goodsSoldOut(Long id) {

        //根据spuId查询商品
        Spu spu = this.spuMapper.selectByPrimaryKey(id);
        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",id);
        List<Sku> skus = this.skuMapper.selectByExample(example);

        if (spu.getSaleable()) {

            spu.setSaleable(false);
            this.spuMapper.updateByPrimaryKeySelective(spu);

            skus.forEach(sku -> {
                if (sku.getEnable()) {
                    sku.setEnable(false);
                    this.skuMapper.updateByPrimaryKeySelective(sku);
                }
            });
        }else {
            spu.setSaleable(true);
            this.spuMapper.updateByPrimaryKeySelective(spu);

            skus.forEach(sku -> {
                if (sku.getEnable()) {
                    sku.setEnable(true);
                    this.skuMapper.updateByPrimaryKeySelective(sku);
                }
            });
        }
        sendMsg("item.update",id);
    }


    /**
     * 新增Sku信息
     * @param spuBo
     */
    private void  saveSku(SpuBo spuBo){
        spuBo.getSkus().forEach(sku -> {
            //新增sku信息
            sku.setSpuId(spuBo.getId());

            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());

            this.skuMapper.insertSelective(sku);

            //新增库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            this.stockMapper.insertSelective(stock);
        });
    }


}
