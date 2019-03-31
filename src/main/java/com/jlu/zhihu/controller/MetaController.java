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

package com.jlu.zhihu.controller;

import com.jlu.zhihu.model.*;
import com.jlu.zhihu.model.metadata.ContentType;
import com.jlu.zhihu.model.metadata.MetaData;
import com.jlu.zhihu.model.metadata.OperationType;
import com.jlu.zhihu.repository.MetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metadata")
public class MetaController {

    private final MetaDataRepository metaDataRepository;

    @Autowired
    public MetaController(MetaDataRepository metaDataRepository) {
        this.metaDataRepository = metaDataRepository;
    }

    @PostMapping("/{cType}/{oType}/{iid}")
    public Response<MetaData> agree(@RequestBody User user,
                                    @PathVariable String cType,
                                    @PathVariable String oType,
                                    @PathVariable long iid) {
        Response<MetaData> response = new Response<>();
        ContentType contentType = ContentType.fromString(cType);
        OperationType operationType = OperationType.fromString(oType);
        if (contentType == null || operationType == null) {
            response.status = HttpStatus.NOT_FOUND.value();
            response.msg = "404 not found";
            return response;
        }
        MetaData metaData = metaDataRepository.
                findByContentTypeAndOperationTypeAndUserAndIid(
                        contentType, operationType, user, iid
                );
        if (metaData != null) {
            metaDataRepository.delete(metaData);
            response.status = HttpStatus.OK.value();
            response.msg = "cancel " + oType + " " + cType;
            response.body = metaData;
            return response;
        }

        metaData = new MetaData();
        metaData.user = user;
        metaData.contentType = contentType;
        metaData.operationType = operationType;
        metaData.iid = iid;
        metaData = metaDataRepository.save(metaData);
        response.status = HttpStatus.CREATED.value();
        response.msg = "create " + oType + " " + cType;
        response.body = metaData;
        return response;
    }

    @PostMapping("/exist/{cType}/{oType}/{iid}")
    public Response<Boolean> exist(@RequestBody User user,
                                   @PathVariable String cType,
                                   @PathVariable String oType,
                                   @PathVariable long iid) {
        Response<Boolean> response = new Response<>();
        ContentType contentType = ContentType.fromString(cType);
        OperationType operationType = OperationType.fromString(oType);
        response.body = metaDataRepository.
                findByContentTypeAndOperationTypeAndUserAndIid(
                        contentType, operationType, user, iid
                ) != null;
        return response;
    }
}
