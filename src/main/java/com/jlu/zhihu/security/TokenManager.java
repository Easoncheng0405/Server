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

package com.jlu.zhihu.security;

import com.jlu.zhihu.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenManager {

    private final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    private static final long INTERVAL = 15 * 60 * 60 * 1000;

    private final Set<String> tokenPool = new HashSet<>();

    public String generateToken(User user) {
        if (user == null) return null;
        logger.debug("generate a token for user: " + user.email);
        cleanToken();
        String res = user.email + '-' + UUID.randomUUID().toString().replaceAll("-", "")
                + '-' + System.currentTimeMillis();
        String token = Base64.getEncoder().encodeToString(res.getBytes());
        tokenPool.add(token);
        return token;
    }

    public boolean isTokenActive(String token) {
        cleanToken();
        return tokenPool.contains(token);
    }

    private void cleanToken() {
        Iterator<String> iterator = tokenPool.iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            String res = new String(Base64.getDecoder().decode(token.getBytes()));
            String[] arr = res.split("-");
            long millis = Long.parseLong(arr[2]);
            if (System.currentTimeMillis() - millis > INTERVAL) {
                logger.debug("token over time, remove it, email " + arr[0]);
                iterator.remove();
            }
        }
    }
}
