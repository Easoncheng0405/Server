/*
 *    Copyright [2019] [chengjie.jlu@qq.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jlu.zhihu.controller;

import com.jlu.zhihu.model.Response;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.model.UserMetaData;
import com.jlu.zhihu.security.TokenManager;
import com.jlu.zhihu.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final TokenManager tokenManager;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, TokenManager tokenManager) {
        this.userService = userService;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public Response<String> login(@RequestBody User user) {
        logger.debug("user login: " + user.toString());
        Response<String> response = new Response<>();
        User result = userService.login(user);
        if (result == null) {
            response.status = HttpStatus.NOT_FOUND.value();
            response.msg = "user not exist or password wrong.";
        } else {
            response.msg = "login success.";
            response.body = tokenManager.generateToken(result);
        }
        return response;
    }

    @PostMapping("/register")
    public Response<String> register(@RequestBody User user) {
        logger.debug("user register: " + user.toString());
        Response<String> response = new Response<>();
        if (userService.findByEmail(user.email) != null) {
            response.status = HttpStatus.BAD_REQUEST.value();
            response.msg = "email address already user.";
            response.body = null;
            return response;
        }
        user.st = System.currentTimeMillis();
        response.msg = "register success.";
        response.body = tokenManager.generateToken(userService.register(user));
        return response;
    }

    @GetMapping("/tokenActive")
    public Response<User> isTokenActive(@RequestParam String token) {
        Response<User> response = new Response<>();
        if (tokenManager.isTokenActive(token)) {
            response.body = userService.findByEmail(tokenManager.getEmail(token));
        } else {
            response.status = HttpStatus.NOT_FOUND.value();
            response.msg = "token not exist";
        }
        return response;
    }

    @PostMapping("/modify/{id}")
    public Response<User> modify(@RequestBody User user, @PathVariable int id) {
        User u = userService.findUserById(id);
        u.name = user.name;
        u.image = user.image;
        u.sign = user.sign;
        Response<User> response = new Response<>();
        response.body=userService.save(u);
        return response;
    }

    @GetMapping("/metadata/{id}")
    public Response<UserMetaData> userMetaData(@PathVariable int id){
        Response<UserMetaData> response = new Response<>();
        response.body = userService.metaData(id);
        return response;
    }
}
