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

import com.jlu.zhihu.util.Encoder;

public class Recommend {

    public long aid;

    public long qid;

    public String title;

    public String summary;

    public User author;

    public int agree;

    public int collect;

    public int comment;

    public static Recommend create(Question question, Answer answer) {
        if (answer == null) return null;
        Recommend recommend = new Recommend();
        recommend.aid = answer.id;
        recommend.qid = question.id;
        recommend.title = question.title;
        recommend.summary = Encoder.unCompressContent(answer.content);
        if (recommend.summary.length() > 200)
            recommend.summary = recommend.summary.substring(0, 200);
        recommend.author = answer.author;
        recommend.agree = answer.agree;
        recommend.collect = answer.collect;
        recommend.comment = answer.comment;
        return recommend;
    }
}
