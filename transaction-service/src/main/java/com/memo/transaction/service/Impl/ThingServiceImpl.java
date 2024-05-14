package com.memo.transaction.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.memo.api.client.UserClient;
import com.memo.common.util.SnowFlakeUtil;
import com.memo.common.util.ThreadLocalUtil;
import com.memo.transaction.mapper.ThingMapper;
import com.memo.transaction.pojo.CacheMessage;
import com.memo.transaction.pojo.PageBean;
import com.memo.transaction.pojo.Thing;
import com.memo.transaction.service.ThingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ThingServiceImpl implements ThingService {

    @Autowired
    private ThingMapper thingMapper;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserClient userClient;


    private static final Logger log = LoggerFactory.getLogger(ThingService.class);

    public void cacheDelete(Long userId){
        redisTemplate.opsForZSet().removeRange("用户" + userId + "代办事项",0,-1);
        redisTemplate.opsForZSet().removeRange("用户" + userId + "完成事项",0,-1);
        redisTemplate.opsForZSet().removeRange("用户" + userId + "所有事项",0,-1);

        redisTemplate.delete("用户" + userId + "代办事项记录数");
        redisTemplate.delete("用户" + userId + "完成事项记录数");
        redisTemplate.delete("用户" + userId + "所有事项记录数");
    }

    @Override
    public void saveThing(Long id, Long userId, String content) {

        //更新数据库
        thingMapper.saveThing(id,userId,content);
        userClient.updateUserInfo(1);

        //删除缓存
        try {
            cacheDelete(userId);
        } catch (Exception e) {
            //删除缓存失败则将key发送到消息队列重试删除
            CacheMessage cacheMessage=new CacheMessage(SnowFlakeUtil.getSnowFlakeId(),userId);
            rabbitTemplate.convertAndSend("redis.topic","redis",cacheMessage);
        }

    }

    @Override
    public Long getUserId(Long id) {
        return thingMapper.getUserId(id);
    }

    @Override
    public void updateTodo(Long id) {

        //更新数据库
        thingMapper.updateTodo(id);

        Long userId = ThreadLocalUtil.get();

        //删除缓存
        try {
            cacheDelete(userId);
        } catch (Exception e) {
            //删除缓存失败则将key发送到消息队列重试删除
            CacheMessage cacheMessage=new CacheMessage(SnowFlakeUtil.getSnowFlakeId(),userId);
            rabbitTemplate.convertAndSend("redis.topic","redis",cacheMessage);
        }

    }

    @Override
    public void updateDone(Long id) {

        //更新数据库
        thingMapper.updateDone(id);

        Long userId = ThreadLocalUtil.get();

        //删除缓存
        try {
            cacheDelete(userId);
        } catch (Exception e) {
            //删除缓存失败则将key发送到消息队列重试删除
            CacheMessage cacheMessage=new CacheMessage(SnowFlakeUtil.getSnowFlakeId(),userId);
            rabbitTemplate.convertAndSend("redis.topic","redis",cacheMessage);
        }

    }

    @Override
    public void updateTodoAll(Long userId) {

        //更新数据库
        thingMapper.updateTodoAll(userId);

        //删除缓存
        try {
            cacheDelete(userId);
        } catch (Exception e) {
            //删除缓存失败则将key发送到消息队列重试删除
            CacheMessage cacheMessage=new CacheMessage(SnowFlakeUtil.getSnowFlakeId(),userId);
            rabbitTemplate.convertAndSend("redis.topic","redis",cacheMessage);
        }
    }

    @Override
    public void updateDoneAll(Long userId) {

        //更新数据库
        thingMapper.updateDoneAll(userId);

        //删除缓存
        try {
            cacheDelete(userId);
        } catch (Exception e) {
            //删除缓存失败则将key发送到消息队列重试删除
            CacheMessage cacheMessage=new CacheMessage(SnowFlakeUtil.getSnowFlakeId(),userId);
            rabbitTemplate.convertAndSend("redis.topic","redis",cacheMessage);
        }
    }

    @Override
    public PageBean<Thing> getThingTodoAll(Long userId, Integer pageNum, Integer pageSize) {

        PageBean<Thing> thingPageBean =new PageBean<>();

        List<Thing> things;

        long start=(long) (pageNum - 1) *pageSize;
        long end = (long) pageNum * pageSize-1;

        //超过记录条数直接返回
        Long number = null;
        try {
            number = (Long.valueOf((Integer)redisTemplate.opsForValue().get("用户" + userId + "代办事项记录数")));
            if(start>=number){
                thingPageBean.setTotal(number);
                thingPageBean.setItems(null);
                return thingPageBean;
            }
        } catch (Exception e) {
            log.info("...");
        }

        //先从redis中获取未完成事项
        List range = new ArrayList<>(redisTemplate.opsForZSet().range("用户" + userId + "代办事项", start, end)) ;
        if (range != null && !range.isEmpty()) {
            things = range;
            thingPageBean.setTotal(number);
            thingPageBean.setItems(things);
            log.info("从redis中获取");
            return thingPageBean;
        }

        PageHelper.startPage(pageNum, pageSize);
        //redis未命中，则从数据库获取并写入redis
        things = thingMapper.getThingTodoAll(userId);

        Page<Thing> thingPage = (Page<Thing>) things;

        thingPageBean.setItems(thingPage.getResult());
        thingPageBean.setTotal(thingPage.getTotal());


        //存入redis
        List<Thing> thingList;
        thingList = thingMapper.getThingTodoAll(userId);
        for (Thing thing : thingList) {
            redisTemplate.opsForZSet().addIfAbsent("用户" + userId + "代办事项", thing, thing.getId());
        }
        redisTemplate.expire("用户" + userId + "代办事项", 12, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("用户" + userId + "代办事项记录数", thingPage.getTotal(),12, TimeUnit.HOURS);
        log.info("从mysql中获取");
        return thingPageBean;
    }

    @Override
    public PageBean<Thing> getThingDoneAll(Long userId, Integer pageNum, Integer pageSize) {

        PageBean<Thing> thingPageBean =new PageBean<>();

        List<Thing> things;

        long start=(long) (pageNum - 1) *pageSize;
        long end = (long) pageNum * pageSize-1;

        //超过记录条数直接返回
        Long number = null;
        try {
            number = (Long.valueOf((Integer)redisTemplate.opsForValue().get("用户" + userId + "完成事项记录数")));
            if(start>=number){
                thingPageBean.setTotal(number);
                thingPageBean.setItems(null);
                return thingPageBean;
            }
        } catch (Exception e) {
            log.info("...");
        }

        //先从redis中获取未完成事项
        List range = new ArrayList<>(redisTemplate.opsForZSet().range("用户" + userId + "完成事项", start, end)) ;
        if (range != null && !range.isEmpty()) {
            things = range;
            thingPageBean.setTotal(number);
            thingPageBean.setItems(things);
            log.info("从redis中获取");
            return thingPageBean;
        }

        PageHelper.startPage(pageNum, pageSize);
        //redis未命中，则从数据库获取并写入redis
        things = thingMapper.getThingDoneAll(userId);

        Page<Thing> thingPage = (Page<Thing>) things;

        thingPageBean.setItems(thingPage.getResult());
        thingPageBean.setTotal(thingPage.getTotal());


        //存入redis
        List<Thing> thingList;
        thingList = thingMapper.getThingDoneAll(userId);
        for (Thing thing : thingList) {
            redisTemplate.opsForZSet().addIfAbsent("用户" + userId + "完成事项", thing, thing.getId());
        }
        redisTemplate.expire("用户" + userId + "完成事项", 12, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("用户" + userId + "完成事项记录数", thingPage.getTotal(),12, TimeUnit.HOURS);
        log.info("从mysql中获取");
        return thingPageBean;
    }

    @Override
    public PageBean<Thing> getThingAll(Long userId, Integer pageNum, Integer pageSize) {

        PageBean<Thing> thingPageBean =new PageBean<>();

        List<Thing> things;

        long start=(long) (pageNum - 1) *pageSize;
        long end = (long) pageNum * pageSize-1;

        //超过记录条数直接返回
        Long number = null;
        try {
            number = (Long.valueOf((Integer)redisTemplate.opsForValue().get("用户" + userId + "所有事项记录数")));
            if(start>=number){
                thingPageBean.setTotal(number);
                thingPageBean.setItems(null);
                return thingPageBean;
            }
        } catch (Exception e) {
           log.info("...");
        }

        //先从redis中获取未完成事项
        List range = new ArrayList<>(redisTemplate.opsForZSet().range("用户" + userId + "所有事项", start, end)) ;
        if (range != null && !range.isEmpty()) {
            things = range;
            thingPageBean.setTotal(number);
            thingPageBean.setItems(things);
            log.info("从redis中获取");
            return thingPageBean;
        }

        PageHelper.startPage(pageNum, pageSize);
        //redis未命中，则从数据库获取并写入redis
        things = thingMapper.getThingAll(userId);

        Page<Thing> thingPage = (Page<Thing>) things;

        thingPageBean.setItems(thingPage.getResult());
        thingPageBean.setTotal(thingPage.getTotal());


        //存入redis
        List<Thing> thingList;
        thingList = thingMapper.getThingAll(userId);
        for (Thing thing : thingList) {
            redisTemplate.opsForZSet().addIfAbsent("用户" + userId + "所有事项", thing, thing.getId());
        }
        redisTemplate.expire("用户" + userId + "所有事项", 12, TimeUnit.HOURS);
        redisTemplate.opsForValue().set("用户" + userId + "所有事项记录数", thingPage.getTotal(),12, TimeUnit.HOURS);
        log.info("从mysql中获取");
        return thingPageBean;

    }

    @Override
    public PageBean<Thing> getThingAllByKeyord(String keyword, Integer pageNum, Integer pageSize) {

        PageBean<Thing> thingPageBean =new PageBean<>();

        Long userId = ThreadLocalUtil.get();

        List<Thing> things;

        long start=(long) (pageNum - 1) *pageSize;
        long end = (long) pageNum * pageSize-1;

        //超过记录条数直接返回
        Long number = null;
        try {
            number = (Long.valueOf((Integer)redisTemplate.opsForValue().get("用户" + userId + "关键词"+keyword+"事项记录数")));
            if(start>=number){
                thingPageBean.setTotal(number);
                thingPageBean.setItems(null);
                return thingPageBean;
            }
        } catch (Exception e) {
            log.info("...");
        }

        //先从redis中获取未完成事项
        List range = new ArrayList<>(redisTemplate.opsForZSet().range("用户" + userId + "关键词"+keyword+"事项", start, end)) ;
        if (range != null && !range.isEmpty()) {
            things = range;
            thingPageBean.setTotal(number);
            thingPageBean.setItems(things);
            log.info("从redis中获取");
            return thingPageBean;
        }

        PageHelper.startPage(pageNum, pageSize);
        //redis未命中，则从数据库获取并写入redis
        things = thingMapper.getThingAllByKeyword(userId,keyword);

        Page<Thing> thingPage = (Page<Thing>) things;

        thingPageBean.setItems(thingPage.getResult());
        thingPageBean.setTotal(thingPage.getTotal());


        //存入redis
        List<Thing> thingList;
        thingList = thingMapper.getThingAllByKeyword(userId,keyword);
        for (Thing thing : thingList) {
            redisTemplate.opsForZSet().addIfAbsent("用户" + userId + "关键词"+keyword+"事项", thing, thing.getId());
        }
        redisTemplate.expire("用户" + userId + "关键词事项", 2, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("用户" + userId + "关键词"+keyword+"事项记录数", thingPage.getTotal(),2, TimeUnit.SECONDS);
        log.info("从mysql中获取");
        return thingPageBean;
    }

    @Override
    public void removeThing(Long id) {

        thingMapper.removeThing(id);

        Long userId =ThreadLocalUtil.get();
        userClient.updateUserInfo(-1);

        //删除缓存
        try {
            cacheDelete(userId);
        } catch (Exception e) {
            //删除缓存失败则将key发送到消息队列重试删除
            CacheMessage cacheMessage=new CacheMessage(SnowFlakeUtil.getSnowFlakeId(),userId);
            rabbitTemplate.convertAndSend("redis.topic","redis",cacheMessage);
        }
    }

    @Override
    public void removeThingTodoAll(Long userId) {

        int i = thingMapper.removeThingTodoAll(userId);
        userClient.updateUserInfo((-1)*i);

        //删除缓存
        try {
            cacheDelete(userId);
        } catch (Exception e) {
            //删除缓存失败则将key发送到消息队列重试删除
            CacheMessage cacheMessage=new CacheMessage(SnowFlakeUtil.getSnowFlakeId(),userId);
            rabbitTemplate.convertAndSend("redis.topic","redis",cacheMessage);
        }
    }

    @Override
    public void removeThingDoneAll(Long userId) {

        int i = thingMapper.removeThingDoneAll(userId);
        userClient.updateUserInfo((-1)*i);

        //删除缓存
        try {
            cacheDelete(userId);
        } catch (Exception e) {
            //删除缓存失败则将key发送到消息队列重试删除
            CacheMessage cacheMessage=new CacheMessage(SnowFlakeUtil.getSnowFlakeId(),userId);
            rabbitTemplate.convertAndSend("redis.topic","redis",cacheMessage);
        }
    }

    @Override
    public void removeThingAll(Long userId) {

        int i = thingMapper.removeThingAll(userId);
        userClient.updateUserInfo((-1)*i);

        //删除缓存
        try {
            cacheDelete(userId);
        } catch (Exception e) {
            //删除缓存失败则将key发送到消息队列重试删除
            CacheMessage cacheMessage=new CacheMessage(SnowFlakeUtil.getSnowFlakeId(),userId);
            rabbitTemplate.convertAndSend("redis.topic","redis",cacheMessage);
        }
    }

    @Override
    public String test() {
        Long userId = ThreadLocalUtil.get();
        return userClient.findUsernameById(userId);
    }
}
