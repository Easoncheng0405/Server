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

import com.jlu.zhihu.model.Answer;
import com.jlu.zhihu.model.Question;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.repository.AnswerRepository;
import com.jlu.zhihu.repository.QuestionRepository;
import com.jlu.zhihu.repository.UserRepository;
import com.jlu.zhihu.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerServiceImpl(UserRepository userRepository,
                             QuestionRepository questionRepository,
                             AnswerRepository answerRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }


    @Override
    public Answer findById(long id) {
        return answerRepository.findById(id);
    }

    @Override
    public Answer findByAuthorAndQuestion(int uid, long qid) {
        User author = userRepository.findById(uid);
        Question question = questionRepository.findById(qid);
        return answerRepository.findByAuthorAndQuestion(author, question);
    }

    @Override
    public List<Answer> findAllByQuestion(long qid) {
        Question question = questionRepository.findById(qid);
        return answerRepository.findByQuestion(question);
    }

    @Override
    public List<Answer> findAllByAuthor(int uid) {
        User author = userRepository.findById(uid);
        return answerRepository.findByAuthor(author);
    }
}
