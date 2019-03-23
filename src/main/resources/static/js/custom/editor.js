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
    toolbar: ["bold", "italic", "heading", "|", "quote",
        "unordered-list", "ordered-list", "link", "image",
        "table", "preview", "|", "side-by-side", "fullscreen"],
});

function loadData() {
    resize();
    $('#tab-create').addClass('active');
    $('.editor-toolbar').append(
        "<div class=\"btn-group pull-right\" style='margin-right: 10px'>\n" +
        "   <button type=\"button\" class=\"btn btn-success\" onclick='submit()'>发布</button>\n" +
        "   <button type=\"button\" class=\"btn btn-primary\" onclick='save()'>保存</button>\n" +
        "</div>");
    $('#title').val("未命名 " + new Date().toLocaleString())
}

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
    let title = $('#title').val();
    let content = editor.value();
    if (title.length < 10 || title.length > 50) {
        overhang("error", "标题的长度在10到50个字符之间");
        return;
    }
    if (content.length < 10 || content.length > 10000) {
        overhang("error", "内容的长度在100到10000个字符之间");
        return;
    }
    let mdContent = editor.markdown(content);
    alert(mdContent);
}

function save() {
    alert("保存")
}

window.onbeforeunload = function () {
    return confirm("确定离开此页面吗？未保存的改动将会丢失。");
};