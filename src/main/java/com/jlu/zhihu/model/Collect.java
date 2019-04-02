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

package com.jlu.zhihu.model;

public class Collect {

    public String title;

    public String type;

    public User author;

    public int agree;

    public int collect;

    public int comment;

    public String href;

    public String timeFormat;

    public static Collect fromAnswer(Answer answer) {
        Collect collect = new Collect();
        collect.title = answer.title;
        collect.type = "回答";
        collect.author = answer.author;
        collect.agree = answer.agree;
        collect.comment = answer.comment;
        collect.collect = answer.collect;
        collect.timeFormat = answer.getTimeFormat();
        collect.href = "http://localhost/content.html?type=answer&page=0&id=" + answer.id;
        return collect;
    }

    public static Collect fromArticle(Article article) {
        Collect collect = new Collect();
        collect.title = article.title;
        collect.type = "文章";
        collect.author = article.author;
        collect.agree = article.agree;
        collect.comment = article.comment;
        collect.collect = article.collect;
        collect.timeFormat = article.getTimeFormat();
        collect.href = "http://localhost/content.html?type=article&id=" + article.id;
        return collect;
    }
}
