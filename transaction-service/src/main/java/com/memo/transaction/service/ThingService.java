package com.memo.transaction.service;

import com.memo.transaction.pojo.PageBean;
import com.memo.transaction.pojo.Thing;

public interface ThingService {

    void saveThing(Long id, Long userId, String content);

    Long getUserId(Long id);

    void updateTodo(Long id);

    void updateDone(Long id);

    void updateTodoAll(Long userId);

    void updateDoneAll(Long userId);

    PageBean<Thing> getThingTodoAll(Long userId, Integer pageNum, Integer pageSize);

    PageBean<Thing> getThingDoneAll(Long userId, Integer pageNum, Integer pageSize);

    PageBean<Thing> getThingAll(Long userId, Integer pageNum, Integer pageSize);

    PageBean<Thing> getThingAllByKeyord(String keyword, Integer pageNum, Integer pageSize);

    void removeThing(Long id);


    void removeThingTodoAll(Long userId);

    void removeThingDoneAll(Long userId);

    void removeThingAll(Long userId);
}
