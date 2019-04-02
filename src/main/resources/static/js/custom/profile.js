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


function loadData() {
    const uid = $.getUrlParam("id", currentUser.id + "");
    const isCurrentUser = uid === currentUser.id + "";
    ajaxGetJson(
        "/api/user/metadata/" + uid,
        function (response) {
            ajaxPostJson(
                "/api/render/profile",
                JSON.stringify(response.body),
                function (html) {
                    $('#content-wrapper').append(html.body);
                    datatable($("#answer"));
                    datatable($("#question"));
                    datatable($("#article"));
                    datatable($("#collect"));
                    if (!isCurrentUser) $('#settings').remove();
                    $('#user-form').submit(function () {
                        ajaxPostJson(
                            "http://47.94.134.55/api/user/modify/" + uid,
                            parseJson($('#user-form')),
                            function (jsonResult) {
                                if (jsonResult.status === 200) {
                                    window.location.reload();
                                } else {
                                    overhang("error", "修改个人设置失败");
                                }
                            });
                    });
                }
            )
        });
}

function datatable(e) {
    e.dataTable(
        {
            language: {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            }
        }
    )
}