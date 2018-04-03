<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-标签配置</title>
    <meta name="keywords" content="流量平台 标签配置"/>
    <meta name="description" content="流量平台 标签配置"/>

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

        .icon-sync {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-sync.png);
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
        }

        .icon-online {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-online.png);
        }

        .icon-search {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-search.png);
        }

        .form {
            width: 350px;
            text-align: left;
        }

        .form label {
            width: 70px;
            text-align: right;
            margin-right: 10px;
        }

        .form .form-control {
            width: 230px;
        }

        .lg-btn-input {
            width: 205px;
            text-align: left;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        .form .form-group .prompt {
            padding-left: 86px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .search-separator {
            display: none;
        }

        @media (max-width: 1300px) {
            .search-separator {
                display: block;
            }
        }

    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>标签配置</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/labelConfig/showAddOrUpdateLabelConfig.html" class="btn btn-sm btn-danger">
                <i class="fa fa-plus mr-5"></i>新建标签
            </a>
        </div>

        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <div class="form-group form-group-sm">
                    <label for="name">Key值名称：</label>
                    <input type="text" class="form-control searchItem" id="configName" name="configName">
                </div>
                <div class="form-group form-group-sm">
                    <label for="name">标签显示值：</label>
                    <input type="text" class="form-control searchItem" id="defaultValue" name="defaultValue">
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <div role="table"></div>
                </div>
            </div>
        </div>
    </div>

    <div role="pagination"></div>

    <div class="modal fade dialog-sm" id="tip-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">请确认是否删除</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

    <div class="modal fade dialog-sm" id="error-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    删除标签配置项失败！
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

</div>

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var statusFormat = function (value, column, row) {
        return row.deleteFlag == 0 ? "未删除" : "已删除";
    };

    var buttonsFormat = function (value, column, row) {
        if (row.deleteFlag == 0) {
            return ["<a class='btn-icon icon-down mr-5' href='javascript:deleteLabelConfig(" + row.id + ")'>删除</a>",
                "<a class='btn-icon icon-search mr-5' class='btn-icon icon-down mr-5' href='${contextPath}/manage/labelConfig/showAddOrUpdateLabelConfig.html?id=" + row.id + "'>编辑</a>"];
        }
    }

    var columns = [{name: "name", text: "Key值名称", tip: true},
        {name: "defaultValue", text: "标签显示值", tip: true},
        {name: "description", text: "描述", tip: true},
        {name: "deleteFlag", text: "状态", format: statusFormat},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "updateTime", text: "修改时间", format: "DateTimeFormat"},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/labelConfig/search.html";

    require(["common", "bootstrap", "page/list"], function () {
    });


</script>

<script>
    function deleteLabelConfig(id) {
        $("#tip-dialog").modal("show");
        init(id);
    }

    function init(id) {
        $("#ok").on("click", function () {
            $.ajax({beforeSend: function(request) {var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");request.setRequestHeader(header1, token1);},
                url: "${contextPath}/manage/labelConfig/delete.html",
                data: {
                    id: id
                },
                dataType: "json", //指定服务器的数据返回类型，
                success: function (res) {
                    if (res.result != true) {
                        $("#error-dialog").modal("show");
                    }
                    else {
                        window.location.reload();
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