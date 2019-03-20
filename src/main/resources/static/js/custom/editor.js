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

const editor = new SimpleMDE({
    element: document.getElementById("editor"),
    spellChecker: false,
    status: false,
});

$(document).ready(function () {
    resize();
    $('#tab-create').addClass('active');
    $('.editor-toolbar').append(
        "<div class=\"btn-group pull-right\" style='margin-right: 10px'>\n" +
        "   <button type=\"button\" class=\"btn btn-success\" onclick='submit()'>发布</button>\n" +
        "   <button type=\"button\" class=\"btn btn-primary\" onclick='save()'>保存</button>\n" +
        "</div>");
});

editor.codemirror.on("refresh", function () {
    if (editor.isFullscreenActive()) {
        $('#header').hide();
        $('#sidebar').hide();
    } else {
        $('#header').show();
        $('#sidebar').show();
    }
    resize();
});

function resize() {
    $('.CodeMirror-wrap').css("min-height", $('.content-wrapper').height() -
        $('.editor-toolbar').height() - $('#footer').height() - $('#title').height());
}

function submit() {
    alert("发布")
}

function save() {
    alert("保存")
}