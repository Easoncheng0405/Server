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
import com.jlu.zhihu.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Response<User> login(@RequestBody User user) {
        logger.debug(user.toString());
        Response<User> response = new Response<>();
        response.msg = "login success.";
        response.body = userService.login(user);
        if (response.body == null) {
            response.status = 404;
            response.msg = "user not exist or password wrong.";
        }
        return response;
    }

    @PostMapping("/register")
    public Response<User> register(@RequestBody User user) {
        Response<User> response = new Response<>();
        user.st = System.currentTimeMillis();
        if (userService.findByEmail(user.email) != null) {
            response.status = 400;
            response.msg = "email address already user.";
            response.body = null;
            return response;
        }
        response.msg = "register success.";
        response.body = userService.register(user);
        return response;
    }
}
