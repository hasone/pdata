<!DOCTYPE html>
<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-短信黑名单</title>
    <meta name="keywords" content="流量平台 短信黑名单"/>
    <meta name="description" content="流量平台 短信黑名单"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        a:focus, a:hover {
            text-decoration: none;
        }

        .btn-disabled {
            pointer-events: none;
            cursor: not-allowed;
        }
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>短信黑名单</h3>
    </div>

    <div class="tools">
        <div class="dataTables_filter">
            <div class="form-inline">
                <div class="form-group mr-10 form-group-sm">
                    <label for="name">手机号码</label>
                    <input type="text" class="form-control searchItem mobileOnly" id="tel" name="tel" value=""
                           maxlength="11">
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
                <span class="error-tip" id="search-error"></span>
            </div>
        </div>
    </div>

    <div class="tile form-inline mt-20" style="padding: 15px;line-height: 30px;">
        <div class="form-group mr-10 form-group-sm">
            <label for="name">手机号码</label>
            <input type="text" class="form-control mobileOnly" id="mobile" name="mobile" value="" maxlength="11">
        </div>

        <div class="form-group mr-10 form-group-sm">
            <label for="name">类型</label>
            <div class="btn-group btn-group-sm">
                <input type="text" style="width: 0; height: 0; opacity: 0; padding: 0;" class="form-control" name="type"
                       id="type">
                <button type="button" class="btn btn-default">请选择</button>
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="caret"></span>
                    <span class="sr-only">Toggle Dropdown</span>
                </button>
                <ul class="dropdown-menu">
                    <li data-value=""><a>请选择</a></li>
                    <li data-value="1"><a>开户审批提醒</a></li>
                    <li data-value="2"><a>EC审批提醒</a></li>
                    <li data-value="3"><a>平台短信验证码</a></li>
                    <li data-value="4"><a>充值到账提醒</a></li>
                    <li data-value="5"><a>预警值提醒</a></li>
                    <li data-value="6"><a>暂停值提醒</a></li>
                    <li data-value="7"><a>营销活动</a></li>
                    <li data-value="8"><a>初始化静态密码</a></li>
                <#--<#if roles??>-->
                <#--<#list roles as role>-->
                <#--<li data-value="${(role.roleId)! }"><a href="#">${role.name!}</a></li>-->
                <#--</#list>-->
                <#--</#if>-->
                </ul>
            </div>
        </div>
        <a id="add-btn" href="javascript:void(0)" style="font-size: 14px;" class="mr-10">添加</a>
        <span class="error-tip" id="add-error"></span>
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
</div>

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
        </div>
    </div>
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
    var typeList = ["", "开户审批提醒", "EC审批提醒", "平台短信验证码", "充值到账提醒", "预警值提醒", "暂停值提醒", "营销活动", "初始化静态密码"];
    var typeFormat = function (value, column, row) {
        return typeList[value];
    };
    var buttonsFormat = function (value, column, row) {
        return ["<a class='btn-icon icon-del mr-5' href='javascript:deleteList(" + row.id + ")'>删除</a>"];
    };
    var columns = [{name: "mobile", text: "手机号码"},
        {name: "type", text: "类型", format: typeFormat},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/sms/blackList/search.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "page/list"], function () {
        listeners();
    });

    function listeners() {
        $("#tel").on("blur", function () {
            var val = $(this).val();
            if (val && !(/^1[3-8][0-9]{9}$/.test(val))) {
                $("#search-error").html("请输入正确格式的手机号码");
                $("#search-btn").addClass("btn-disabled");
                return false;
            } else {
                $("#search-btn").removeClass("btn-disabled");
                $("#search-error").html("");
            }
        });

        $("#mobile").on("blur", function () {
            var val = $(this).val();
            if (!(/^1[3-8][0-9]{9}$/.test(val))) {
                $("#add-error").html("请输入正确格式的手机号码");
                return false;
            } else {
                $("#add-error").html("");
            }
        });

        $("#add-btn").on("click", function () {
            $("#tel").val('');
            $("#add-error").html('');
            var mobile = $("#mobile").val();
            var type = $("#type").val();
            if (!mobile) {
                $("#add-error").html("请输入手机号码");
                return false;
            } else if (!(/^1[3-8][0-9]{9}$/.test(mobile))) {
                $("#add-error").html("请输入正确格式的手机号码");
                return false;
            } else if (!type) {
                $("#add-error").html("请选择黑名单类型");
                return false;
            } else {
                $.ajax({
                    beforeSend: function (request) {
                        var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                        var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                        request.setRequestHeader(header1, token1);
                    },
                    url: "${contextPath}/sms/blackList/add.html",
                    type: "POST",
                    data: {
                        mobile: mobile,
                        type: type
                    },
                    dataType: "JSON",
                    success: function (res) {
                        if (res && res.isDup) {
                            $("#add-error").html('同类型手机号码重复添加');
                        } else {
                            $("#search-btn").click();
                        }
                    }
                });
            }
        });
    }

    function deleteList(id) {
        $("#tip-dialog").modal("show");
        $("#ok").on("click", function () {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/sms/blackList/delete.html",
                type: "POST",
                data: {
                    id: id
                },
                dataType: "JSON",
                success: function (res) {
                    if (res.result != true) {
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