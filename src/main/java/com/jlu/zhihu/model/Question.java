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

@Entity
@SuppressWarnings("unused")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    // question title
    public String title;

    // question summary
    public String content;

    @OneToOne
    public User author;

    // answer count
    public int answer;

    //focus count
    public int focus;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", answer=" + answer +
                ", focus=" + focus +
                '}';
    }
}
