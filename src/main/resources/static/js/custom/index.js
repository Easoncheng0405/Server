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
    $('#login-form').submit(function () {
        $.ajax({
            type: "POST",
            url: "/api/user/login",
            contentType: "application/json",
            dataType: "json",
            data: parseJson($('#login-form')),
            success: function (jsonResult) {
                console.log(jsonResult);
                if (jsonResult.status === 200) {
                    alert("登陆成功！")
                } else {
                    $('#login-result').show();
                }
            }
        });
        return false;
    });

    $('#register-form').submit(function () {
        var div = $('#register-result');
        $.ajax({
            type: "POST",
            url: "/api/user/register",
            contentType: "application/json",
            dataType: "json",
            data: parseJson($('#register-form')),
            beforeSend: function () {
                if ($("#password").val() !== $('#password-repeat').val()) {
                    $('#register-text').text("两次输入的密码不一致");
                    div.show();
                    return false;
                }
            },
            success: function (jsonResult) {
                console.log(jsonResult);
                if (jsonResult.status === 200) {
                    alert("注册成功")
                } else {
                    $('#register-text').text("注册失败，邮箱地址已被使用。");
                    div.show();
                }
            }
        });
        return false;
    });

    function parseJson(form) {
        var formObject = {};
        var formArray = form.serializeArray();
        $.each(formArray, function (i, item) {
            formObject[item.name] = item.value;
        });
        return JSON.stringify(formObject);
    }
});