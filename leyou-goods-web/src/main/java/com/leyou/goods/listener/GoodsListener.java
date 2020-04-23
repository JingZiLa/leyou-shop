package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author Mirror
 * @CreateDate 2020/4/8.
 * rabbitmq的消息监听类
 */
@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 同步商品新增 / 更新 消息 进行页面静态化
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.ITEM.SAVE_GOODS.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}

    ))
    public void saveGoods(Long spuId) {
        if (spuId == null) {
            return;
        }
        this.goodsHtmlService.createHtml(spuId);
    }
    /**
     * 同步商品删除消息 把静态化页面删除
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.ITEM.DELETE_GOODS.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void deleteGoods (Long spuId) {
        if (spuId == null) {
            return;
        }
        this.goodsHtmlService.deleteGoodsHtml(spuId);
    }
}
