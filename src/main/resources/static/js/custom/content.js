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

const type = $.getUrlParam("type", "question");
const page = parseInt($.getUrlParam("page", "0"));
const contentWrapper = $('#content-wrapper');
const id = $.getUrlParam("id", "1");

function loadData() {
    switch (type) {
        case "question":
            loadQuestion(id);
            loadMore();
            break;
        case "answer":
            loadAnswer();
            break;
    }
}

function loadQuestion(_id) {
    ajaxGetJson(
        function (response) {
            ajaxPostJson(
                "/api/render/question",
                JSON.stringify(response.body),
                function (response) {
                    $('#title').innerText = response.body.title;
                    contentWrapper.prepend(response.body);
                });
        }
    );
}

function loadAnswer() {
    ajaxGetJson(
        "http://localhost/api/answer/" + id,
        function (response) {
            loadQuestion(response.body.qid);
            const content = response.body.content;
            ajaxPostJson(
                "/api/render/answer",
                JSON.stringify(response.body),
                function (response) {
                    contentWrapper.append(response.body);
                    loadAnswerBody(content)
                });
        }
    );
}

function loadAnswerBody(answerBody) {
    const body = $('#answer-body');
    body.empty();
    ajaxPostJson(
        "/api/render/md",
        "" + answerBody,
        function (response) {
            console.log(JSON.stringify(response));
            body.append(response.body);
        }
    );
}

function loadMore() {

}