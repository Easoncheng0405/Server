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

import com.jlu.zhihu.model.Comment;
import com.jlu.zhihu.model.Idea;
import com.jlu.zhihu.model.Response;
import com.jlu.zhihu.service.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/idea")
public class IdeaController {

    private final IdeaService ideaService;

    @Autowired
    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @GetMapping("/count")
    public Response<Long> countAll() {
        Response<Long> response = new Response<>();
        response.body = ideaService.countAll();
        return response;
    }


    @PostMapping("/create")
    public Response<Idea> createIdea(@RequestBody Idea idea) {
        Response<Idea> response = new Response<>();
        response.body = ideaService.createIdea(idea);
        return response;
    }

    @GetMapping("/all")
    public Response<List<Idea>> all(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 5, new Sort(Sort.Direction.ASC, "st"));
        Response<List<Idea>> response = new Response<>();
        response.body = ideaService.findAll(pageable);
        response.msg = "find " + response.body.size() + " ideas.";
        return response;
    }

    @PostMapping("/comment/{id}")
    public Response<Idea> comment(@PathVariable int id, @RequestBody Comment comment) {
        Response<Idea> response = new Response<>();
        response.body = ideaService.createComment(id, comment);
        return response;
    }
}
