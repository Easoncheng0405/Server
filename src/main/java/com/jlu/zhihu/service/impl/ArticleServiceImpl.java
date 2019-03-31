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
import com.jlu.zhihu.repository.ArticleRepository;
import com.jlu.zhihu.repository.CommentRepository;
import com.jlu.zhihu.repository.MetaDataRepository;
import com.jlu.zhihu.repository.UserRepository;
import com.jlu.zhihu.service.ArticleService;
import com.jlu.zhihu.util.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final MetaDataRepository metaDataRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository,
                              UserRepository userRepository,
                              CommentRepository commentRepository,
                              MetaDataRepository metaDataRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.metaDataRepository = metaDataRepository;
    }


    @Override
    public Article findById(int id) {
        Article article = articleRepository.findById(id);
        setMetaData(article);
        article.content = Encoder.unCompressContent(article.content);
        return article;
    }

    @Override
    public List<Article> findAllByAuthor(int aid) {
        User author = userRepository.findById(aid);
        List<Article> list = articleRepository.findAllByAuthor(author);
        for (Article article : list) {
            setMetaData(article);
            article.content = Encoder.unCompressContent(article.content);
        }
        return list;
    }

    @Override
    public Article createArticle(Article article) {
        setMetaData(article);
        article.content = Encoder.compressContent(article.content);
        return articleRepository.save(article);
    }

    @Override
    public Article recommend(Pageable pageable) {
        List<Article> list = articleRepository.findAll(pageable).getContent();
        if (list.size() == 0) return null;
        Article article = list.get(0);
        setMetaData(article);
        article.content = Encoder.unCompressContent(article.content);
        return article;
    }

    @Override
    public long countAll() {
        return articleRepository.count();
    }

    @Override
    public Article createComment(Comment comment, int id) {
        comment = commentRepository.save(comment);
        Article article = articleRepository.findById(id);
        article.comments.add(comment);
        return articleRepository.save(article);
    }

    private void setMetaData(Article article) {
        if (article == null) return;
        article.comment = article.comments == null ? 0 : article.comments.size();
        article.agree = metaDataRepository.countAllByContentTypeAndOperationTypeAndIid(
                ContentType.ARTICLE, OperationType.AGREE, article.id
        );
        article.collect = metaDataRepository.countAllByContentTypeAndOperationTypeAndIid(
                ContentType.ARTICLE, OperationType.COLLECT, article.id
        );
    }
}
