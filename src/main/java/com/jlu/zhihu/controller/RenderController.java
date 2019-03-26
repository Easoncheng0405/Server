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
import com.jlu.zhihu.model.Article;
import com.jlu.zhihu.model.Question;
import com.jlu.zhihu.model.Response;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ext.anchorlink.AnchorLink;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataSet;
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

@RestController
@RequestMapping("/api/render")
public class RenderController {

    private final ThymeleafViewResolver resolver;
    private static final Iterable<Extension> EXTENSIONS = Collections.singletonList(TablesExtension.create());

    public RenderController(ThymeleafViewResolver resolver) {
        this.resolver = resolver;
    }

    @PostMapping("/recommend")
    public Response<String> recommendQuestion(@RequestBody Question question) {
        Response<String> result = new Response<>();
        result.msg = "render success";
        result.body = process("recommend", "recommend", question);
        return result;
    }

    @PostMapping("/question")
    public Response<String> normalQuestion(@RequestBody Question question) {
        Response<String> result = new Response<>();
        result.msg = "render success";
        result.body = process("question", "question", question);
        return result;
    }

    @PostMapping("/answer")
    public Response<String> answer(@RequestBody Answer answer) {
        answer.content = markdown(answer.content);
        Response<String> result = new Response<>();
        result.msg = "render success";
        result.body = process("answer", "answer", answer);
        return result;
    }

    @PostMapping("/article")
    public Response<String> article(@RequestBody Article article) {
        article.content = markdown(article.content);
        Response<String> result = new Response<>();
        result.msg = "render success";
        result.body = process("article", "article", article);
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
    private String process(String templateName, String argName, Object argValue) {
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
