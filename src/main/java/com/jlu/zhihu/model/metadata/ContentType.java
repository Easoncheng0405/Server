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

package com.jlu.zhihu.model.metadata;

public enum ContentType {

    ARTICLE("article"),
    ANSWER("answer"),
    IDEA("idea"),
    USER("user");
    private String string;

    ContentType(String s) {
        this.string = s;
    }

    public static ContentType fromString(String s) {
        switch (s) {
            case "article":
                return ARTICLE;
            case "answer":
                return ANSWER;
            case "idea":
                return IDEA;
        }
        return null;
    }


    @Override
    public String toString() {
        return this.string;
    }
}
