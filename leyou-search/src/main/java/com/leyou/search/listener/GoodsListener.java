package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author Mirror
 * @CreateDate 2020/4/8.
 * rabbitmq的消息监听类
 */
@Component
public class GoodsListener {

    @Autowired
    private SearchService searchService;

    /**
     * 同步商品新增 / 更新 消息 进行goods数据更新
     * @param spuId
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SEARCH.SAVE_GOODS.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void saveGoods(Long spuId) throws IOException {
        if (spuId == null) {
            return;
        }
        this.searchService.saveGoods(spuId);
    }

    /**
     * 同步商品删除消息 进行goods数据删除
     * @param spuId
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SEARCH.DELETE_GOODS.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void deleteGoods(Long spuId) throws IOException {
        if (spuId == null) {
            return;
        }
        this.searchService.deleteGoods(spuId);
    }
}
