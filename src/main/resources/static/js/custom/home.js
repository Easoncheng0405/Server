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

let currentTab;
const contentWrapper = $('#content-wrapper');
const pager = $('#page');


function loadData() {
    $('#home-menu').addClass('menu-open active');
    currentPage = parseInt($.getUrlParam("page", 0));
    currentTab = $.getUrlParam("tab", "recommend");
    switch (currentTab) {
        case "question":
            $('#tab-question').addClass('active');
            loadNormalQuestion();
            break;
        case "idea":
            alert("想法");
            break;
        case "article":
            alert("文章");
            break;
        default:
            $('#tab-recommend').addClass('active');
            loadRecommend();
            break;
    }
}

function loadRecommend() {
    contentWrapper.empty();
    ajaxGetJson(
        "http://localhost/api/recommend/all?page=" + currentPage,
        function (data) {
            for (let i = 0; i < data.body.length; i++) {
                ajaxPostJson(
                    "/api/render/recommend",
                    JSON.stringify(data.body[i]),
                    function (response) {
                        contentWrapper.append(response.body);
                    });
            }
        }
    );
    setPage("/api/recommend/count");
}

function loadNormalQuestion() {
    contentWrapper.empty();
    ajaxGetJson(
        "http://localhost/api/question/all?page=" + currentPage,
        function (data) {
            for (let i = 0; i < data.body.length; i++) {
                ajaxPostJson(
                    "/api/render/question",
                    JSON.stringify(data.body[i]),
                    function (response) {
                        contentWrapper.append(response.body);
                    });
            }
        }
    );
    setPage("/api/question/count");
}

function loadPage(i) {
    if (currentPage === i || i < 0) return;
    window.location.href = "http://localhost/home.html?tab=" + currentTab + "&page=" + i;
}