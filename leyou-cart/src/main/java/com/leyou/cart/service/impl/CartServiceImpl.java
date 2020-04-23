package com.leyou.cart.service.impl;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/4/12.
 * 购物车业务层
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GoodsClient goodsClient;

    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @Override
    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //记录商品数量
        Integer num = cart.getNum();
        //记录skuId
        Long skuId = cart.getSkuId();

        //查询购物车记录
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("userId").is(userInfo.getId());
        criteria.and("skuId").is(skuId);
        query.addCriteria(criteria);

         cart = mongoTemplate.findOne(query, Cart.class);
        //判断当前商品是否已在购物车中
        if (cart == null) {
            Sku sku = goodsClient.querySkuById(skuId);
            cart = new Cart();
            cart.setNum(num);
            cart.setTitle(sku.getTitle());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            cart.setUserId(userInfo.getId());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setSkuId(skuId);
            cart.setPrice(sku.getPrice());
        } else {
            cart.setNum(cart.getNum()+num);
        }
        this.mongoTemplate.save(cart);
    }

    /**
     * 查询用户购物车商品
     * @return
     */
    @Override
    public List<Cart> queryCart() {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        Query query = new Query( Criteria.where("userId").is(userInfo.getId()));

        return this.mongoTemplate.find(query, Cart.class);

    }

    /**
     * 更新购物车商品数量
     * @param cart
     */
    @Override
    public void updateCartNum(Cart cart) {
        Query query = new Query( Criteria.where("_id").is(cart.get_id()));
        this.mongoTemplate.updateFirst(query, Update.update("num",cart.getNum()),"cart");
    }

    /**
     * 删除购物车商品
     * @param id
     */
    @Override
    public void delCart(String id) {
        Query query = new Query( Criteria.where("_id").is(id));
        this.mongoTemplate.remove(query,"cart");
    }


}
