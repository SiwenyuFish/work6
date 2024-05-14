package com.memo.user.controller;


import com.memo.common.pojo.Result;
import com.memo.common.util.Md5Util;
import com.memo.common.util.ThreadLocalUtil;
import com.memo.user.pojo.User;
import com.memo.user.service.UserService;
import com.memo.common.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户注册 用户名必须为2-16个字符，密码必须为6-16个字符
     * 用户名唯一
     */

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{2,16}$") String username, @Pattern(regexp = "^\\S{6,16}$") String password){
        User user=userService.findByUserName(username);
        if(user==null){
            userService.register(username,password);
            return Result.success();
        }else {
            return Result.error("用户名已被占用");
        }
    }

    /**
     * 登录 使用JwtUtil获取token
     */

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{2,16}$") String username, @Pattern(regexp = "^\\S{6,16}$") String password) {
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //判断该用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误");
        }
        //判断密码是否正确  loginUser对象中的password是密文
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    @GetMapping("/{id}")
    public String getUsername(@PathVariable("id") Long id) {
        return userService.findUsernameById(id);
    }

    @PutMapping("/count/{count}")
    public void updateUserInfo(@PathVariable("count") int count) {
        Long id = ThreadLocalUtil.get();
        userService.updateUserInfo(id,count);
        log.info("id"+id);
        log.info("进入远程调用");
    }


}
