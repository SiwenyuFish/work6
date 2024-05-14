package com.memo.transaction.controller;


import cn.hutool.core.util.StrUtil;
import com.memo.common.pojo.Result;
import com.memo.common.util.SnowFlakeUtil;
import com.memo.common.util.ThreadLocalUtil;
import com.memo.transaction.pojo.PageBean;
import com.memo.transaction.pojo.Thing;
import com.memo.transaction.service.ThingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/thing")
public class ThingController {

    @Autowired
    private ThingService thingService;


    @ApiOperation("新增待办事项")
    @PostMapping("/add")
    public Result saveThing(String content){

        if(StrUtil.isBlank(content)){
            return Result.error("内容不能为空");
        }

        Long userId = ThreadLocalUtil.get();
        Long snowFlakeId = SnowFlakeUtil.getSnowFlakeId();
        thingService.saveThing(snowFlakeId,userId,content);
        return Result.success();
    }

    @ApiOperation("将一条事项设置为代办/已完成")
    @PutMapping("/update")
    public Result updateThing(Long id,Integer actionType){

        if(id==null){
            return Result.error("事项编号不能为空");
        }

        if(actionType!=0&&actionType!=1){
            return Result.error("操作类型必须为0或者1");
        }

        Long LocalUserId = ThreadLocalUtil.get();
        Long userId=thingService.getUserId(id);

        if(userId==null){
            return Result.error("没有找到该事项");
        }

        if(!Objects.equals(userId, LocalUserId)){
            return Result.error("不能修改其它用户的事项");
        }

        if(actionType==0){
            thingService.updateTodo(id);
            return Result.success();
        }else {
            thingService.updateDone(id);
            return Result.success();
        }

    }

    @ApiOperation("将所有代办/完成事项设置为完成/代办")
    @PutMapping("/updateAll")
    public Result updateThingAll(Integer actionType){

        if(actionType!=0&&actionType!=1){
            return Result.error("操作类型必须为0或者1");
        }

        Long userId = ThreadLocalUtil.get();

        if(actionType==0){
            thingService.updateTodoAll(userId);
            return Result.success();
        }else {
            thingService.updateDoneAll(userId);
            return Result.success();
        }
    }

    @ApiOperation("分页查询事项")
    @GetMapping("/getAll")
    public Result<PageBean<Thing>> getThingAll(Integer actionType,Integer pageNum,Integer pageSize){

        if(pageNum==null||pageSize==null){
            return Result.error("必须输入页数或页码");
        }

        if(actionType!=0&&actionType!=1&&actionType!=2){
            return Result.error("操作类型必须为0或者1或者2");
        }

        Long userId = ThreadLocalUtil.get();

        if(actionType==0){
            PageBean<Thing> thingPageBean=thingService.getThingTodoAll(userId,pageNum,pageSize);
            return Result.success(thingPageBean);
        }else if(actionType==1){
            PageBean<Thing> thingPageBean=thingService.getThingDoneAll(userId,pageNum,pageSize);
            return Result.success(thingPageBean);
        }else {
            PageBean<Thing> thingPageBean=thingService.getThingAll(userId,pageNum,pageSize);
            return Result.success(thingPageBean);
        }
    }

    @ApiOperation("关键词查询事项")
    @GetMapping("/getAllByKeyword")
    public Result<PageBean<Thing>> getThingByKeyword(String keyword,Integer pageNum,Integer pageSize){
        if(pageNum==null||pageSize==null){
            return Result.error("必须输入页数或页码");
        }

        PageBean<Thing> thingPageBean=thingService.getThingAllByKeyord(keyword,pageNum,pageSize);
        return Result.success(thingPageBean);

    }

    @ApiOperation("删除一条事项")
    @DeleteMapping("/removeThing")
    public Result removeThing(Long id){
        if(id==null){
            return Result.error("事项编号不能为空");
        }


        Long LocalUserId = ThreadLocalUtil.get();
        Long userId=thingService.getUserId(id);
        if(userId==null){
            return Result.error("没有找到该事项");
        }
        if(!Objects.equals(userId, LocalUserId)){
            return Result.error("不能修改其它用户的事项");
        }

        thingService.removeThing(id);
        return Result.success();
    }

    @ApiOperation("删除所有代办/完成事项")
    @DeleteMapping("/removeThingAll")
    public Result removeThingAll(Integer actionType){

        if(actionType!=0&&actionType!=1&&actionType!=2){
            return Result.error("操作类型必须为0或者1或者2");
        }

        Long userId = ThreadLocalUtil.get();

        if(actionType==0){
            thingService.removeThingTodoAll(userId);
            return Result.success();
        }else if(actionType==1){
            thingService.removeThingDoneAll(userId);
            return Result.success();
        }else {
            thingService.removeThingAll(userId);
            return Result.success();
        }
    }




}
