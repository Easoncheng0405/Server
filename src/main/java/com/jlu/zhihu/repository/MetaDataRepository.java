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

package com.jlu.zhihu.repository;

import com.jlu.zhihu.model.metadata.ContentType;
import com.jlu.zhihu.model.metadata.MetaData;
import com.jlu.zhihu.model.metadata.OperationType;
import com.jlu.zhihu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaDataRepository extends JpaRepository<MetaData, Long> {

    int countAllByContentTypeAndOperationTypeAndIid(
            ContentType contentType,
            OperationType operationType,
            long iid
    );

    MetaData findByContentTypeAndOperationTypeAndUserAndIid(
            ContentType contentType,
            OperationType operationType,
            User user,
            long iid
    );

    List<MetaData> findByContentTypeAndOperationTypeAndUser(
            ContentType contentType,
            OperationType operationType,
            User user
    );
}
