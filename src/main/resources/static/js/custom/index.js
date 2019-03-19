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
        ajaxPostJson(
            "/api/user/login",
            parseJson($('#login-form')),
            function (jsonResult) {
                if (jsonResult.status === 200) {
                    $.cookie("token", jsonResult.body, {path: '/'});
                    window.location.href = "home.html";
                } else {
                    overhang("error", "登陆失败，用户名或密码错误");
                }
            });
        return false;
    });

    $('#register-form').submit(function () {
        if ($("#password").val() !== $('#password-repeat').val()) {
            overhang("error", "两次输入的密码不一致");
            return false;
        }
        ajaxPostJson(
            "/api/user/register",
            parseJson($('#register-form')),
            function (jsonResult) {
                if (jsonResult.status === 200) {
                    $.cookie("token", jsonResult.body, {path: '/'});
                    window.location.href = "home.html";
                } else {
                    overhang("error", "注册失败，邮箱地址已被使用");
                }
            });
        return false;
    });

    $('#register').on('show.bs.modal', function (e) {
        $(this).css('display', 'block');
        const modalHeight = $(window).height() / 2 - $('#register .modal-dialog').height() / 2;
        $(this).find('.modal-dialog').css({
            'margin-top': modalHeight
        });
    });
});