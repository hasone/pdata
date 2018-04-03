<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业流控列表</title>
    <meta name="keywords" content="流量平台 企业流控列表"/>
    <meta name="description" content="流量平台 企业流控列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

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
        <h3>企业流控列表</h3>
    </div>

    <div class="tools row text-right">


        <div class="col-lg-12 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <div class="form-group form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="hidden" name="name" id="name" class="form-control searchItem enterprise_autoComplete" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                <div class="form-group form-group-sm">
                    <label for="code">企业编码：</label>
                    <input type="text" name="code" id="code" class="form-control searchItem" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
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


    <div class="modal fade dialog-sm" id="openSms-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">短信开启则企业发包达到设置金额90%，平台将发送企业管理员及客户经理告警短信，确认开启？</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

    <div class="modal fade dialog-sm" id="closeSms-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">短信关闭则不再发送告警短信，确认关闭？</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure1">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
</div>

<div class="modal fade dialog-sm" id="subject-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">

                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">短信提醒更新成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var currentUserRoleId = "${currentUserRoleId}";
    var currentUserId = "${currentUserId}";

    var nameFormat = function (value, column, row) {
        return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?id=" + row.enterId + "'>" + row.name + "</a>";
    };

    var codeFormat = function (value, column, row) {
        if (row.code != null) {
            return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?id=" + row.enterId + "'>" + row.code + "</a>";
        } else {
            return "";
        }

    };

    var countUpperFormat = function (value, column, row) {
        if (row.countUpper == null) {
            return "-";
        } else if (row.countUpper >= 0) {
            return (row.countUpper / 100.0).toFixed(2) + "元";
        } else {
            return row.countUpper;
        }

    };

    var countNowFormat = function (value, column, row) {
        if (row.countNow == null) {
            return "-";
        }
        return (row.countNow / 100.0).toFixed(2) + "元";
    };

    var countAdditionFormat = function (value, column, row) {
        if (row.countAddition == null) {
            return "-";
        }
        return (row.countAddition / 100.0).toFixed(2) + "元";
    };

    var opFormat = function (value, column, row) {
     //   if (row.countUpper > 0 && row.countNow >= row.countUpper) {
    	if (row.countUpper > 0 ) {
            return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/entFlowControl/setEntFlowControl.html?enterId=" + row.enterId + "'>设置</a>",
                "<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/entFlowControl/setEntFlowContorlAddition.html?enterId=" + row.enterId + "'>额度追加</a>"];
        } else {
            return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/entFlowControl/setEntFlowControl.html?enterId=" + row.enterId + "'>设置</a>"];
        }

    };

    var hsFormat = function (value, column, row) {
        return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/entFlowControl/queryEntFlowControlHistory.html?enterId=" + row.enterId + "'>查看</a>"];

    };

    var smsFormat = function (value, column, row) {

        if (row.countUpper == -1) {
            return "-";
        } else {

            if (row.fcSmsFlag == 0) {
                return ["<a class='btn-icon icon-online mr-5' href='javascript:openSmsAlert(" + row.enterId + ")'>开启</a>"];
            }
            if (row.fcSmsFlag == 1) {
                return ["<a class='btn-icon icon-online mr-5' href='javascript:closeSmsAlert(" + row.enterId + ")'>关闭</a>"];
            }
        }
    };

    /**
     * 开启短信提醒
     * */
    function openSmsAlert(id) {
        $("#openSms-dialog").modal("show");
        $("#sure").data("entId", id);
        $("#sure").data("fcSmsFlag", 1);
    }
    ;

    /**
     * 关闭短信提醒
     * */
    function closeSmsAlert(id) {
        $("#closeSms-dialog").modal("show");
        $("#sure1").data("entId", id);
        $("#sure1").data("fcSmsFlag", 0);
    }
    ;

    var columns = [{name: "name", text: "企业名称", tip: true, format: nameFormat},
        {name: "code", text: "企业编码", tip: true, format: codeFormat},
        {name: "cmManagerName", text: "所属地区", tip: true},
        {name: "countUpper", text: "日上限金额(元)", format: countUpperFormat},
        {name: "countNow", text: "日发送金额(元)", format: countNowFormat}
    ];

    if (currentUserRoleId == 6 || currentUserRoleId == 7) {
        columns.push({name: "countAddition", text: "当日追加金额(元)", format: countAdditionFormat});
        columns.push({name: "op2", text: "历史记录", format: hsFormat});
    } else {
        columns.push({name: "op", text: "操作", format: opFormat});
        columns.push({name: "fcSmsFlag", text: "短信提醒", format: smsFormat});
        columns.push({name: "op2", text: "历史记录", format: hsFormat});
    }

    var action = "${contextPath}/manage/entFlowControl/search.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "daterangepicker", "easyui", "page/list"], function () {

        $("#subject-btn").on("click", function () {
            window.location.reload();
        });
        init();
    });

    function init() {
        $("#sure").on("click", function () {

            var entId = $("#sure").data("entId");
            var fcSmsFlag = $("#sure").data("fcSmsFlag");

            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/entFlowControl/updateSmsAlertAjax.html",
                data: {
                    entId: entId,
                    fcSmsFlag: fcSmsFlag
                },
                type: "post",
                dataType: "json"
            }).success(function (ret) {
                if (ret) {
                    if (ret.res == "success") {
                        $("#subject-dialog").modal("show");
                    } else {
                        showTipDialog("更改短信提醒失败！");
                    }
                }
            });
        });

        $("#sure1").on("click", function () {

            var entId = $("#sure1").data("entId");
            var fcSmsFlag = $("#sure1").data("fcSmsFlag");

            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/entFlowControl/updateSmsAlertAjax.html",
                data: {
                    entId: entId,
                    fcSmsFlag: fcSmsFlag
                },
                type: "post",
                dataType: "json"
            }).success(function (ret) {
                if (ret) {
                    if (ret.res == "success") {
                        $("#subject-dialog").modal("show");
                    } else {
                        showTipDialog("更改短信提醒失败！");
                    }
                }
            });
        });
    }

</script>

<script type="text/javascript">
    function queryAccount(enterId) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/enterprise/queryAccountFromBoss.html",
            data: {
                id: enterId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                alert(data.msg);
            },
            error: function () {
                alert("网络错误");
            }
        });
    }
</script>

</body>
</html>