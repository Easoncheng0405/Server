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

$(document).ready(function () {
    if (!document.URL.endWith("index.html") && $.cookie("token") == null) {
        window.location.href = "index.html";
    }
});

String.prototype.endWith = function (s) {
    if (s == null || s === "" || this.length === 0 || s.length > this.length)
        return false;
    return this.substr(this.length - s.length) === s;
};

function ajaxPostJson(url, data, successCallBack) {
    $.ajax({
        type: "POST",
        url: url,
        contentType: "application/json",
        dataType: "json",
        data: data,
        success: successCallBack,
    });
    return false;
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