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

import com.jlu.zhihu.model.*;
import com.jlu.zhihu.model.metadata.ContentType;
import com.jlu.zhihu.model.metadata.OperationType;
import com.jlu.zhihu.repository.*;
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
    private final CommentRepository commentRepository;
    private final MetaDataRepository metaDataRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public AnswerServiceImpl(UserRepository userRepository,
                             AnswerRepository answerRepository,
                             CommentRepository commentRepository,
                             MetaDataRepository metaDataRepository,
                             QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.commentRepository = commentRepository;
        this.metaDataRepository = metaDataRepository;
        this.questionRepository = questionRepository;
    }


    @Override
    public Answer findById(long id) {
        Answer answer = answerRepository.findById(id);
        setMetaData(answer);
        answer.content = Encoder.unCompressContent(answer.content);
        return answer;
    }

    @Override
    public Answer findByAuthorAndQuestion(int uid, long qid) {
        User author = userRepository.findById(uid);
        Answer answer = answerRepository.findByAuthorAndQid(author, qid);
        if (answer == null) return null;
        setMetaData(answer);
        answer.content = Encoder.unCompressContent(answer.content);
        return answer;
    }

    @Override
    public List<Answer> findAllByQuestion(long qid, Pageable pageable) {
        List<Answer> list = answerRepository.findByQid(qid, pageable);
        for (Answer answer : list) {
            setMetaData(answer);
            answer.content = Encoder.unCompressContent(answer.content);
        }
        return list;
    }

    @Override
    public List<Answer> findAllByAuthor(int uid) {
        User author = userRepository.findById(uid);
        List<Answer> list = answerRepository.findByAuthor(author);
        for (Answer answer : list) {
            setMetaData(answer);
            answer.content = Encoder.unCompressContent(answer.content);
        }
        return list;
    }

    @Override
    public List<Answer> findAll(Pageable pageable) {
        List<Answer> list = answerRepository.findAll(pageable).getContent();
        for (Answer answer : list) {
            setMetaData(answer);
            answer.content = Encoder.unCompressContent(answer.content);
        }
        return list;
    }

    @Override
    public Answer createAnswer(Answer answer) {
        String temp = answer.content;
        answer.content = Encoder.compressContent(answer.content);
        answer.title = questionRepository.findById(answer.qid).title;
        answer = answerRepository.save(answer);
        answer.content = temp;
        return answer;
    }

    @Override
    public int countByQuestion(long qid) {
        return answerRepository.countByQid(qid);
    }

    @Override
    public long countAll() {
        return answerRepository.count();
    }

    @Override
    public Answer findFirstByQuestion(long qid) {
        Answer answer = answerRepository.findFirstByQid(qid);
        if (answer == null) return null;
        setMetaData(answer);
        answer.content = Encoder.unCompressContent(answer.content);
        return answer;
    }

    @Override
    public Answer createComment(long id, Comment comment) {
        comment = commentRepository.save(comment);
        Answer answer = answerRepository.findById(id);
        answer.comments.add(comment);
        return answerRepository.save(answer);
    }

    private void setMetaData(Answer answer) {
        if (answer == null) return;
        answer.comment = answer.comments == null ? 0 : answer.comments.size();
        answer.agree = metaDataRepository.countAllByContentTypeAndOperationTypeAndIid(
                ContentType.ANSWER, OperationType.AGREE, answer.id
        );
        answer.collect = metaDataRepository.countAllByContentTypeAndOperationTypeAndIid(
                ContentType.ANSWER, OperationType.COLLECT, answer.id
        );
    }
}
