package com.memo.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/user/{id}")
    String findUsernameById(@PathVariable("id") Long id);

    @PutMapping("user/count/{count}")
    void updateUserInfo(@PathVariable("count") int count);
}
