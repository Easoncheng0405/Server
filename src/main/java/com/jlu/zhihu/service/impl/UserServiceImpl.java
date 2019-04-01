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
import com.jlu.zhihu.model.Article;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.model.UserMetaData;
import com.jlu.zhihu.model.metadata.ContentType;
import com.jlu.zhihu.model.metadata.OperationType;
import com.jlu.zhihu.repository.*;
import com.jlu.zhihu.service.AnswerService;
import com.jlu.zhihu.service.ArticleService;
import com.jlu.zhihu.service.QuestionService;
import com.jlu.zhihu.service.UserService;
import com.jlu.zhihu.util.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MetaDataRepository metaDataRepository;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final ArticleService articleService;

    @Autowired
    public UserServiceImpl(UserRepository repository,
                           MetaDataRepository metaDataRepository,
                           AnswerService answerService,
                           QuestionService questionService,
                           ArticleService articleService) {
        this.userRepository = repository;
        this.metaDataRepository = metaDataRepository;
        this.answerService = answerService;
        this.questionService = questionService;
        this.articleService = articleService;
    }

    @Override
    public User login(User user) {
        user.email = Encoder.md5(user.email);
        user.password = Encoder.md5(user.password);
        return userRepository.findByEmailAndPassword(user.email, user.password);
    }

    @Override
    public User register(User user) {
        user.email = Encoder.md5(user.email);
        user.password = Encoder.md5(user.password);
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserMetaData metaData(int id) {
        User user = userRepository.findById(id);
        UserMetaData metaData = new UserMetaData();
        metaData.user = user;
        metaData.agree = 0;
        metaData.thanks = 0;
        metaData.focus = metaDataRepository.
                countAllByContentTypeAndOperationTypeAndIid(
                        ContentType.USER, OperationType.FOCUS, id
                );

        metaData.answers = answerService.findAllByAuthor(id);
        metaData.questions = questionService.findAllByAuthor(id);
        metaData.articles = articleService.findAllByAuthor(id);

        for (Answer answer : metaData.answers) {
            metaData.agree += answer.agree;
            metaData.thanks += answer.collect;
        }

        for (Article article : metaData.articles) {
            metaData.agree += article.agree;
            metaData.thanks += article.collect;
        }
        return metaData;
    }

}
