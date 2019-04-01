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
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.model.metadata.ContentType;
import com.jlu.zhihu.model.metadata.OperationType;
import com.jlu.zhihu.repository.AnswerRepository;
import com.jlu.zhihu.repository.MetaDataRepository;
import com.jlu.zhihu.repository.QuestionRepository;
import com.jlu.zhihu.repository.UserRepository;
import com.jlu.zhihu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final MetaDataRepository metaDataRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository repository,
                               UserRepository userRepository,
                               AnswerRepository answerRepository,
                               MetaDataRepository metaDataRepository) {
        this.questionRepository = repository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.metaDataRepository = metaDataRepository;
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> findAllByAuthor(int id) {
        User user = userRepository.findById(id);
        List<Question> list = questionRepository.findByAuthor(user);
        for (Question question : list) {
            setMetaData(question);
        }
        return list;
    }

    @Override
    public List<Question> findAll(Pageable pageable) {
        List<Question> list = questionRepository.findAll(pageable).getContent();
        for (Question question : list) {
            setMetaData(question);
        }
        return list;
    }

    @Override
    public long countAll() {
        return questionRepository.count();
    }

    @Override
    public Question findById(long id) {
        Question question = questionRepository.findById(id);
        setMetaData(question);
        return question;
    }

    private void setMetaData(Question question) {
        question.answer = answerRepository.countByQid(question.id);
        question.focus = metaDataRepository.countAllByContentTypeAndOperationTypeAndIid(
                ContentType.QUESTION, OperationType.FOCUS, question.id
        );
    }
}
