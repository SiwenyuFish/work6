package com.memo.transaction.listener;

import com.memo.transaction.pojo.CacheMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class RabbitMqListener {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqListener.class);


    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;


    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "redis.queue"),
            exchange = @Exchange(name = "redis.topic",type = ExchangeTypes.TOPIC),key = "redis"))
    public void listenRedisQueue(CacheMessage cacheMessage){

        Long userId= cacheMessage.getKey();

        for(int i =0;i<3;i++) {
            try {
                redisTemplate.opsForZSet().removeRange("用户" + userId + "代办事项", 0, -1);
                redisTemplate.opsForZSet().removeRange("用户" + userId + "完成事项", 0, -1);
                redisTemplate.opsForZSet().removeRange("用户" + userId + "所有事项", 0, -1);

                redisTemplate.delete("用户" + userId + "代办事项记录数");
                redisTemplate.delete("用户" + userId + "完成事项记录数");
                redisTemplate.delete("用户" + userId + "所有事项记录数");

                break;
            } catch (Exception e) {
                log.info("重试删除");
            }
        }

    }




    }



