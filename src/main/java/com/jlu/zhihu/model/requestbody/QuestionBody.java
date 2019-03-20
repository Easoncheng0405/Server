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

package com.jlu.zhihu.model.requestbody;

import com.jlu.zhihu.model.Question;
import com.jlu.zhihu.model.QuestionContent;
import com.jlu.zhihu.util.Encoder;

import java.util.Base64;

public class QuestionBody extends Question {

    public String content;

    public QuestionContent getQuestionContent(long id) {
        QuestionContent questionContent = new QuestionContent();
        questionContent.id = id;
        questionContent.content = Base64.getEncoder().encodeToString(Encoder.compress(content));
        return questionContent;
    }

    public Question getQuestion() {
        Question question = new Question();
        question.id = this.id;
        question.summary = this.summary;
        question.author = this.author;
        question.focus = this.focus;
        question.title = this.title;
        question.answer = this.answer;
        return question;
    }

    @Override
    public String toString() {
        return "QuestionBody{" +
                "content='" + content + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", author=" + author +
                ", answer=" + answer +
                ", focus=" + focus +
                '}';
    }
}
