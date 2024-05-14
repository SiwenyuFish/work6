package com.memo.transaction.mapper;

import com.memo.transaction.pojo.Thing;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ThingMapper {

    @Insert("insert into things (id, userid, content, created_at, updated_at) VALUES (#{id},#{userId},#{content},now(),now())")
    void saveThing( Long id,Long userId, String content);

    @Select("select things.userid from things where id = #{id}")
    Long getUserId(Long id);

    @Update("update things set status = 0,things.updated_at = now() where id = #{id}")
    void updateTodo(Long id);

    @Update("update things set status = 1,things.updated_at = now() where id = #{id}")
    void updateDone(Long id);

    @Update("update things set status = 0,things.updated_at = now() where userid = #{userId} and status = 1")
    void updateTodoAll(Long userId);

    @Update("update things set status = 1,things.updated_at = now() where userid = #{userId} and status = 0")
    void updateDoneAll(Long userId);

    @Select("select things.id,things.userid,things.content,things.status,things.created_at,things.updated_at from things where userid=#{userId} and status= 0")
    List<Thing> getThingTodoAll(Long userId);

    @Select("select things.id,things.userid,things.content,things.status,things.created_at,things.updated_at from things where userid=#{userId} and status= 1")
    List<Thing> getThingDoneAll(Long userId);

    @Select("select things.id,things.userid,things.content,things.status,things.created_at,things.updated_at from things where userid=#{userId}")
    List<Thing> getThingAll(Long userId);

    @Select("select * from things where content like concat('%',#{keyword},'%') and userid = #{userId}")
    List<Thing> getThingAllByKeyword(Long userId, String keyword);

    @Delete("delete from things where id = #{id}")
    void removeThing(Long id);

    @Delete("delete from things where userid = #{userId} and status = 0")
    void removeThingTodoAll(Long userId);

    @Delete("delete from things where userid = #{userId} and status = 1")
    void removeThingDoneAll(Long userId);

    @Delete("delete from things where userid = #{userId}")
    void removeThingAll(Long userId);
}
