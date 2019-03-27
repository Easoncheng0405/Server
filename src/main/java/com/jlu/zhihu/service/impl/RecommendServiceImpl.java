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

package com.jlu.zhihu.service.impl;

import com.jlu.zhihu.model.Question;
import com.jlu.zhihu.model.Recommend;
import com.jlu.zhihu.service.AnswerService;
import com.jlu.zhihu.service.QuestionService;
import com.jlu.zhihu.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @Autowired
    public RecommendServiceImpl(AnswerService answerService,
                                QuestionService questionService) {
        this.answerService = answerService;
        this.questionService = questionService;
    }

    @Override
    public long countAll() {
        return answerService.countAll();
    }

    @Override
    public List<Recommend> findAll(Pageable pageable) {
        List<Question> list = questionService.findAll(pageable);
        List<Recommend> result = new ArrayList<>();
        for (Question question : list) {
            Recommend recommend = Recommend.create(question,
                    answerService.findFirstByQuestion(question.id));
            if(recommend!=null)
            result.add(recommend);
        }
        return result;
    }
}
