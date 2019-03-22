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

$('#header').load('header.html');
$('#sidebar').load('sidebar.html');
$('#footer').load('footer.html');

$(document).ready(function () {
    if (!document.URL.endWith("index.html")) {
        ajaxGetJson("/api/user/tokenActive?token=" + $.cookie("token"), function (jsonResult) {
            if (jsonResult.body == null)
                window.location.href = "index.html";
            else
                initUserData(jsonResult.body);
        })
    }
    $('#create-question-form').submit(function () {
        let data = JSON.parse(parseJson($('#create-question-form')));
        data.author = currentUser;
        $('#create-question').modal('hide');
        ajaxPostJson(
            "/api/question/create",
            JSON.stringify(data),
            function (jsonResult) {
                console.log(jsonResult);
                if (jsonResult.status === 200) {
                    overhang("success", "成功发布问题！")
                } else {
                    overhang("error", "创建问题失败，请重试。");
                }
            });
    });
});

String.prototype.endWith = function (s) {
    if (s == null || s === "" || this.length === 0 || s.length > this.length)
        return false;
    return this.substr(this.length - s.length) === s;
};

function ajaxPostJson(url, data, callback) {
    $.ajax({
        headers: {
            "token": $.cookie("token")
        },
        type: "POST",
        url: url,
        contentType: "application/json",
        dataType: "json",
        data: data,
        success: callback,
    });
}

function ajaxGetJson(url, callback) {
    $.ajax({
        headers: {
            "token": $.cookie("token")
        },
        type: "GET",
        url: url,
        dataType: "json",
        success: callback,
    });
}

function parseJson(form) {
    const formObject = {};
    const formArray = form.serializeArray();
    $.each(formArray, function (i, item) {
        formObject[item.name] = item.value;
    });
    return JSON.stringify(formObject);
}

function overhang(type, msg) {
    $("body").overhang({
        type: type,
        message: msg,
        duration: 5
    });
}

let currentUser;

function initUserData(user) {
    currentUser = user;
    $('#sidebar-avatar').attr("src", user.image);
    loadData();
}