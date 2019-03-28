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

package com.jlu.zhihu.service;

import com.jlu.zhihu.model.Answer;
import com.jlu.zhihu.model.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnswerService {

    Answer findById(long id);

    Answer findByAuthorAndQuestion(int uid, long qid);

    List<Answer> findAllByQuestion(long qid, Pageable pageable);

    List<Answer> findAllByAuthor(int uid);

    List<Answer> findAll(Pageable pageable);

    Answer createAnswer(Answer answer);

    int countByQuestion(long qid);

    long countAll();

    Answer findFirstByQuestion(long qid);

    Answer createComment(long id, Comment comment);
}
