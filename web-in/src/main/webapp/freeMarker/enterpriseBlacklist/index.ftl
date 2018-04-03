<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>统一流量平台-企业黑名单管理</title>
    <meta name="keywords" content="统一流量平台 企业黑名单管理"/>
    <meta name="description" content="统一流量平台 企业黑名单管理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <style>
        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        #add-btn {
            font-size: 14px;
        }

        a#add-btn:hover, a#add-btn:focus {
            text-decoration: none;
        }

        @media (min-width: 768px) {
            .form-inline .form-control {
                display: inline-block;
                width: 135px;
            }
        }
    </style>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>企业黑名单管理</h3>
    </div>

    <div class="tools">
        <div class="dataTables_filter">
            <div class="form-inline">
                <div class="form-group mr-10">
                    <label for="keyword">关键词</label>
                    <input type="text" name="keyword" class="form-control searchItem" id="keyword" placeholder="">
                </div>

                <div class="form-group mr-10">
                    <label for="eName">企业名称</label>
                    <input type="text" name="eName" class="form-control searchItem" id="eName" placeholder="">
                </div>
                <a type="submit" class="btn btn-warning" id="search-btn">确定</a>
            </div>
        </div>
    </div>

    <div class="tile form-inline mt-20" style="padding: 15px;">
        <div class="form-group mr-10 form-group-sm">
            <a id="add-btn" href="./add.html"><i class="fa fa-plus mr-5"></i>添加黑名单企业</a>
        </div>
    </div>
    <div class="tile">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <div role="table"></div>
                </div>
            </div>
        </div>
    </div>
    <div role="pagination"></div>
</div>


<div class="modal fade dialog-sm" id="remove-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="error-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">删除失败！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var opFormat = function (value, column, row) {
        return ['<a class="btn-icon icon-edit mr-10" href="${contextPath}/manage/blacklist/edit.html?id='+row.id+'">编辑</a>',
            '<a class="btn-icon icon-del" data-id="'+row.id+'" onclick="deleteItem(this)">删除</a>'];
    };
    var action = "${contextPath}/manage/blacklist/search.html";
    var columns = [{name: "enterpriseName", text: "企业名称", tip: true},
        {name: "keyName", text: "关键词", tip: true},
        {name: "createrTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "updateTime", text: "修改时间", format: "DateTimeFormat"},
        {name: "op", text: "操作", format: opFormat}];

    require(["common", "bootstrap", "page/list"], function () {

    });

    function deleteItem(ele) {
        var eName = $(ele).parent().parent().find("td:first-child").html();
        var id = $(ele).data("id");
        $("#remove-dialog .message-content").html('确认将"' + eName + '"从黑名单中删除？');
        $("#remove-dialog").modal("show");
        $("#ok").on("click", function () {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/blacklist/delete.html",
                type: "POST",
                data: {
                	id: id,
                    eName: eName
                },
                dataType: "JSON",
                success: function (res) {
                    if (res.status != "OK") {
                        $("#error-dialog").modal("show");
                    } else {
                        $("#search-btn").click();
                    }
                },
                error: function () {
                    $("#error-dialog").modal("show");
                }
            });
        });
    }
</script>
</body>
</html>
