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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(length = 12)
    public String name = "知乎刘看山";

    @Column(unique = true, length = 32, nullable = false)
    public String email;

    @Column(length = 32, nullable = false)
    public String password;

    public String image = "http://localhost/image/avatar.jpeg";

    @Column(length = 25)
    public String sign = "发现更大的世界.";

    /* register time in millis */
    public long st = System.currentTimeMillis();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", sign='" + sign + '\'' +
                ", st=" + st +
                '}';
    }
}
