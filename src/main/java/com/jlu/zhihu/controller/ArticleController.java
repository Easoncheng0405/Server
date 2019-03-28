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

import com.jlu.zhihu.model.Article;
import com.jlu.zhihu.model.Comment;
import com.jlu.zhihu.model.Response;
import com.jlu.zhihu.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/create")
    public Response<Article> createArticle(@RequestBody Article article) {
        Response<Article> response = new Response<>();
        response.body = articleService.createArticle(article);
        return response;
    }

    @GetMapping("/author/{aid}")
    public Response<List<Article>> findAllByAuthor(@PathVariable int aid) {
        Response<List<Article>> response = new Response<>();
        response.body = articleService.findAllByAuthor(aid);
        response.msg = "find " + response.body.size() + "articles.";
        return response;
    }

    @GetMapping("/{aid}")
    public Response<Article> findById(@PathVariable int aid) {
        Response<Article> response = new Response<>();
        response.body = articleService.findById(aid);
        return response;
    }

    @GetMapping("/recommend")
    public Response<Article> findAll(@RequestParam int page) {
        Response<Article> response = new Response<>();
        Pageable pageable = PageRequest.of(page, 1, new Sort(Sort.Direction.ASC, "id"));
        response.body = articleService.recommend(pageable);
        return response;
    }

    @GetMapping("/count")
    public Response<Long> countAll() {
        Response<Long> response = new Response<>();
        response.body = articleService.countAll();
        response.msg = "find " + response.body + " articles";
        return response;
    }

    @PostMapping("/comment/{id}")
    public Response<Article> comment(@RequestBody Comment comment, @PathVariable int id) {
        Response<Article> response = new Response<>();
        response.body = articleService.createComment(comment, id);
        return response;
    }
}
