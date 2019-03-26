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
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.repository.AnswerRepository;
import com.jlu.zhihu.repository.UserRepository;
import com.jlu.zhihu.service.AnswerService;
import com.jlu.zhihu.util.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerServiceImpl(UserRepository userRepository,
                             AnswerRepository answerRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }


    @Override
    public Answer findById(long id) {
        Answer answer = answerRepository.findById(id);
        answer.content = Encoder.unCompressContent(answer.content);
        return answer;
    }

    @Override
    public Answer findByAuthorAndQuestion(int uid, long qid) {
        User author = userRepository.findById(uid);
        Answer answer = answerRepository.findByAuthorAndQid(author, qid);
        answer.content = Encoder.unCompressContent(answer.content);
        return answer;
    }

    @Override
    public List<Answer> findAllByQuestion(long qid, Pageable pageable) {
        List<Answer> list = answerRepository.findByQid(qid, pageable);
        for (Answer answer : list) {
            answer.content = Encoder.unCompressContent(answer.content);
        }
        return list;
    }

    @Override
    public List<Answer> findAllByAuthor(int uid) {
        User author = userRepository.findById(uid);
        List<Answer> list = answerRepository.findByAuthor(author);
        for (Answer answer : list) {
            answer.content = Encoder.unCompressContent(answer.content);
        }
        return list;
    }

    @Override
    public List<Answer> findAll() {
        List<Answer> list = answerRepository.findAll();
        for (Answer answer : list) {
            answer.content = Encoder.unCompressContent(answer.content);
        }
        return list;
    }

    @Override
    public Answer createAnswer(Answer answer) {
        String temp = answer.content;
        answer.content = Encoder.compressContent(answer.content);
        answer = answerRepository.save(answer);
        answer.content = temp;
        return answer;
    }

    @Override
    public int countByQuestion(long qid) {
        return answerRepository.countByQid(qid);
    }
}
