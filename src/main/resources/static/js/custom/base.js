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

$('#header').load('http://localhost/header.html');
$('#sidebar').load('http://localhost/sidebar.html');
$('#footer').load('http://localhost/footer.html');

$(document).ready(function () {
    if (!document.URL.endWith("index.html")) {
        ajaxGetJson("http://localhost/api/user/tokenActive?token=" + $.cookie("token"), function (jsonResult) {
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
            "http://localhost/api/question/create",
            JSON.stringify(data),
            function (jsonResult) {
                console.log(jsonResult);
                if (jsonResult.status === 200) {
                    window.location.href = "http://localhost/content.html?type=question&page=0&id=" + jsonResult.body.id;
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

(function ($) {
    $.getUrlParam = function (name, def) {
        let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        let r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return def;
    }
})(jQuery);

function switchTab(tab) {
    switch (tab.innerText) {
        case " 推荐":
            window.location.href = "http://localhost/home.html?tab=recommend&page=0";
            break;
        case " 问题":
            window.location.href = "http://localhost/home.html?tab=question&page=0";
            break;
        case " 想法":
            window.location.href = "http://localhost/home.html?tab=idea&page=0";
            break;
        case " 文章":
            window.location.href = "http://localhost/home.html?tab=article&page=0";
            break;
    }
}

let currentCreateAnswerBtn;
let currentEditor;

function createAnswer(e) {
    if (e === currentCreateAnswerBtn && currentEditor != null) return;
    removeEditor();
    $(e).parents('.box-body').append("<textarea id='editor'></textarea>");
    currentCreateAnswerBtn = e;
    currentEditor = new SimpleMDE({
        element: document.getElementById("editor"),
        spellChecker: false,
        status: false,
        toolbar: ["bold", "italic", "heading", "|", "quote",
            "unordered-list", "ordered-list", "link", "image",
            "table", "|", "preview",
            {
                name: "submit",
                action: function (editor) {
                    submitAnswer(editor.value(), $(e).parents('.box-solid').attr('id'));
                },
                className: "fa fa-check",
                title: "发布",
            },
            {
                name: "cancel",
                action: removeEditor,
                className: "fa fa-times",
                title: "取消",
            }],

    });
}

function removeEditor() {
    if (currentEditor != null) {
        currentEditor.toTextArea();
        currentEditor = null;
    }
    $('#editor').remove();
}

function submitAnswer(content, qid) {
    const request = {
        "qid": parseInt(qid),
        "author": currentUser,
        "content": content
    };

    ajaxPostJson(
        "http://localhost/api/answer/create",
        JSON.stringify(request),
        function (response) {
            window.location.href = "http://localhost/content.html?type=answer&id=" + response.body.id;
        }
    )
}


/**
 * @return {string}
 */
function GetUrlRelativePath() {
    let url = document.location.toString();
    let arrUrl = url.split("//");

    let start = arrUrl[1].indexOf("/");
    let relUrl = arrUrl[1].substring(start);

    if (relUrl.indexOf("?") !== -1) {
        relUrl = relUrl.split("?")[0];
    }
    return relUrl;
}