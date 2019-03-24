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

import com.jlu.zhihu.model.Answer;
import com.jlu.zhihu.model.Response;
import com.jlu.zhihu.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/{id}")
    public Response<Answer> findById(@PathVariable long id) {
        Response<Answer> response = new Response<>();
        response.body = answerService.findById(id);
        return getAnswerResponse(response);
    }

    @GetMapping("/all")
    public Response<List<Answer>> all(){
        Response<List<Answer>> response = new Response<>();
        response.body = answerService.findAll();
        response.msg = "find " + response.body.size() + " answers.";
        return response;
    }

    @GetMapping("/author/{uid}")
    public Response<List<Answer>> findAllByAuthor(@PathVariable int uid) {
        Response<List<Answer>> response = new Response<>();
        response.body = answerService.findAllByAuthor(uid);
        response.msg = "find " + response.body.size() + " answers.";
        return response;
    }

    @GetMapping("/question/{qid}")
    public Response<List<Answer>> findAllByQuestion(@PathVariable long qid) {
        Response<List<Answer>> response = new Response<>();
        response.body = answerService.findAllByQuestion(qid);
        response.msg = "find " + response.body.size() + " answers.";
        return response;
    }

    @PostMapping("/create")
    public Response<Answer> createAnswer(@RequestBody Answer answer){
        Response<Answer> response = new Response<>();
        response.body = answerService.createAnswer(answer);
        return response;
    }

    @GetMapping
    public Response<Answer> findByAuthorAndQuestion(@RequestParam int uid,
                                                    @RequestParam long qid) {
        Response<Answer> response = new Response<>();
        response.body = answerService.findByAuthorAndQuestion(uid, qid);
        return getAnswerResponse(response);
    }

    private Response<Answer> getAnswerResponse(Response<Answer> response) {
        if (response.body == null) {
            response.status = HttpStatus.NOT_FOUND.value();
            response.msg = "answer not found.";
        }
        return response;
    }
}
