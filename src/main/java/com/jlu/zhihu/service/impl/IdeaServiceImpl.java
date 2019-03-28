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

import com.jlu.zhihu.model.Comment;
import com.jlu.zhihu.model.ContentType;
import com.jlu.zhihu.model.Idea;
import com.jlu.zhihu.model.OperationType;
import com.jlu.zhihu.repository.CommentRepository;
import com.jlu.zhihu.repository.IdeaRepository;
import com.jlu.zhihu.repository.MetaDataRepository;
import com.jlu.zhihu.service.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IdeaServiceImpl implements IdeaService {

    private final IdeaRepository ideaRepository;
    private final CommentRepository commentRepository;
    private final MetaDataRepository metaDataRepository;

    @Autowired
    public IdeaServiceImpl(IdeaRepository ideaRepository,
                           CommentRepository commentRepository,
                           MetaDataRepository metaDataRepository) {
        this.ideaRepository = ideaRepository;
        this.commentRepository = commentRepository;
        this.metaDataRepository = metaDataRepository;
    }

    @Override
    public long countAll() {
        return ideaRepository.count();
    }

    @Override
    public List<Idea> findAll(Pageable pageable) {
        List<Idea> list = ideaRepository.findAll(pageable).getContent();
        for (Idea idea : list) {
            setMetaData(idea);
        }
        return list;
    }

    @Override
    public Idea createIdea(Idea idea) {
        Idea result = ideaRepository.save(idea);
        result.comments = new ArrayList<>(0);
        return result;
    }

    @Override
    public Idea createComment(int id, Comment comment) {
        comment = commentRepository.save(comment);
        Idea idea = ideaRepository.findIdeaById(id);
        idea.comments.add(comment);
        idea = ideaRepository.save(idea);
        setMetaData(idea);
        return idea;
    }

    private void setMetaData(Idea idea) {
        idea.comment = idea.comments == null ? 0 : idea.comments.size();
        idea.agree = metaDataRepository.countAllByContentTypeAndOperationTypeAndIid(
                ContentType.IDEA, OperationType.AGREE, idea.id
        );
    }
}
