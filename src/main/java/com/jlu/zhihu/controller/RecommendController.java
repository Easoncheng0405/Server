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

import com.jlu.zhihu.model.Recommend;
import com.jlu.zhihu.model.Response;
import com.jlu.zhihu.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {


    private final RecommendService recommendService;

    @Autowired
    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @GetMapping("/count")
    public Response<Long> countAll() {
        Response<Long> response = new Response<>();
        response.body = recommendService.countAll();
        return response;
    }

    @GetMapping("/all")
    public Response<List<Recommend>> all(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 5, new Sort(Sort.Direction.ASC, "id"));
        Response<List<Recommend>> response = new Response<>();
        response.body = recommendService.findAll(pageable);
        response.msg = "find " + response.body.size() + " answers.";
        return response;
    }

    @GetMapping("/error")
    public void test500(){
        throw new RuntimeException();
    }
}
