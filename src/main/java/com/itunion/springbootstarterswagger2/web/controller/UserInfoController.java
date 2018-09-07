package com.itunion.springbootstarterswagger2.web.controller;

import com.itunion.springbootstarterswagger2.model.UserInfo;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "用户", description = "用户")
@RequestMapping("/userInfo")
@RestController
public class UserInfoController {
    @ApiOperation(value = "登录接口-多值传值方式", notes = "输入用户名和密码登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserInfo.class, responseContainer = "userInfo"),
            @ApiResponse(code = 405, message = "账号名或密码错误")
    })
    @ApiImplicitParam(name = "map", value = "{\"userName\":\"JackMa\",\"passWord\":\"123\"}")
    @RequestMapping(value = "loginForMap", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<UserInfo> loginForMap(@RequestBody Map<String, String> map) {
        if (!map.get("userName").equalsIgnoreCase("JackMa") || !map.get("passWord").equalsIgnoreCase("123")) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        UserInfo user = new UserInfo();
        user.setId(1L);
        user.setUserName("JackMa");
        user.setFirstName("马");
        user.setLastName("云");
        user.setEmail("zhenghhgz@163.com");
        user.setUserStatus(1);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "登录接口-多值传输方式", notes = "输入用户名和密码登录")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserInfo.class, responseContainer = "userInfo"),
            @ApiResponse(code = 405, message = "账号名或密码错误")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "passWord",value = "密码", required = true, dataType = "string",paramType = "query"),
    })
    @RequestMapping(value = "loginForParams", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<UserInfo> loginForMap(@RequestParam String userName, @RequestParam String passWord) {
        if (!userName.equalsIgnoreCase("JackMa") || !passWord.equalsIgnoreCase("123")) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        UserInfo user = new UserInfo();
        user.setId(1L);
        user.setUserName("JackMa");
        user.setFirstName("马");
        user.setLastName("云");
        user.setEmail("jackma@163.com");
        user.setUserStatus(1);
        return ResponseEntity.ok(user);
    }

}
