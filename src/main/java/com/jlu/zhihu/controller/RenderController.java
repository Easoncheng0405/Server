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

import com.jlu.zhihu.model.*;
import com.jlu.zhihu.model.Response;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/render")
public class RenderController {

    private final ThymeleafViewResolver resolver;

    private static final Iterable<Extension> EXTENSIONS = Collections.singletonList(TablesExtension.create());

    @Autowired
    public RenderController(ThymeleafViewResolver resolver) {
        this.resolver = resolver;
    }

    @PostMapping("/recommend")
    public Response<String> recommendQuestion(@RequestBody Recommend recommend) {
        Response<String> result = new Response<>();
        result.msg = "render success";
        recommend.summary = markdown(recommend.summary);
        result.body = processArgs("recommend", "recommend", recommend);
        return result;
    }

    @PostMapping("/question")
    public Response<String> normalQuestion(@RequestBody Question question) {
        Response<String> result = new Response<>();
        result.msg = "render success";
        result.body = processArgs("question", "question", question);
        return result;
    }

    @PostMapping("/answer")
    public Response<String> answer(@RequestBody Answer answer) {
        answer.content = markdown(answer.content);
        Response<String> result = new Response<>();
        result.msg = "render success";
        result.body = processArgs("answer", "answer", answer);
        return result;
    }

    @PostMapping("/article")
    public Response<String> article(@RequestBody Article article) {
        article.content = markdown(article.content);
        Response<String> result = new Response<>();
        result.msg = "render success";
        result.body = processArgs("article", "article", article);
        return result;
    }

    @PostMapping("idea")
    public Response<String> idea(@RequestBody List<Idea> list) {
        Response<String> result = new Response<>();
        result.msg = "render success";
        result.body = processArgs("idea", "list", list);
        return result;
    }

    @PostMapping("profile")
    public Response<String> profile(@RequestBody UserMetaData metaData) {
        Response<String> result = new Response<>();
        result.msg = "render success";
        result.body = processArgs("profile", "data", metaData);
        return result;
    }


    private String markdown(String md) {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, EXTENSIONS);
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(md);
        return renderer.render(document).replaceAll("<table>", "<table class=\"table table-bordered\">");
    }

    @SuppressWarnings("ConstantConditions")
    private String processArgs(String templateName, String argName, Object argValue) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        WebContext context = new WebContext(request, response,
                request.getServletContext());
        context.setVariable(argName, argValue);
        return resolver.getTemplateEngine().process(templateName, context);
    }
}
