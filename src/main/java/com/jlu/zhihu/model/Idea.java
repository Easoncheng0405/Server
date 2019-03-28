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

package com.jlu.zhihu.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String content;

    @OneToOne
    public User author;

    public long st = System.currentTimeMillis();

    @Transient
    public int agree;

    @Transient
    public int comment;

    @OneToMany(fetch = FetchType.EAGER)
    public List<Comment> comments;

    public String getDateYMD() {
        return new java.text.SimpleDateFormat("yyyy年MM月dd日").format(new Date(st));
    }

    public String getDateHM() {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new Date(st));
    }

    public String getMeteData() {
        return String.format("%d 赞同 %d 评论", agree, comment);
    }
}
