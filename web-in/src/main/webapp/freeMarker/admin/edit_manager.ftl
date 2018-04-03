<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-用户管理</title>
    <meta name="keywords" content="流量平台 修改职位"/>
    <meta name="description" content="流量平台 修改职位"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/icon.css">

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 950px;
        }

        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 5px;
            width: 350px;
        }

        .form .form-group label {
            width: 300px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .btn-save {
            margin-left: 300px;
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>用户管理
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/user/index.html'">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/user/editUserManager.html" method="post" name="adminForm" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-header">
                修改职位
            </div>

            <div class="tile-content">

                <input id="administerId" name="administerId" type="hidden" value="${admin.id!}">
                <div class="row form">
                    <div class="form-group">
                        <label>职位名称：</label>
                        <div class="btn-group btn-group-sm">
                            <input style="width: 0; height: 0; opacity: 0" name="managerId" id="managerId"
                                   value="${currentManager.id!}" required>
                            <button type="button" class="btn btn-default"
                                    style="width:325px">${currentManager.extendName!}</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                                <li data-value="-1"><a href="#">无职位</a></li>
                            <#if managers??>
                                <#list managers as manager>
                                    <li data-value="${(manager.id)! }"><a
                                            onclick="showRole(${(manager.id)! })">${manager.extendName!}</a></li>
                                </#list>
                            </#if>
                            </ul>
                        </div>
                    </div>

                    <div class="form-group" id="role" style="display:none">
                        <label>角色：</label>
                        <span id="roleName"></span>
                    </div>

                    <div class="form-group">
                        <label>用户手机号码：</label>
                        <span>${admin.mobilePhone!}</span>
                    </div>


                    <div class="form-group">
                        <label>用户姓名：</label>
                        <span>${admin.userName!}</span>
                    </div>

                </div>
            </div>

            <div class="btn-save mt-30">
                <input type="submit" class="btn btn-sm btn-warning dialog-btn" value="保存"/>&nbsp;&nbsp;&nbsp;
                <span style="color:red" id="error_msg">${errorMsg!}</span>
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", "easyui"], function () {
        checkFormValidate();
    });


    function onSubmit() {
        $("#table_validate").submit();
        return true;
    }


    function checkFormValidate() {
        $("#table_validate").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if ($(element).siblings('div').hasClass('prompt')) {
                    $(element).siblings('div.prompt').before(error);
                } else {
                    $(element).parent().append(error);
                }
            },
            errorElement: "span",
            rules: {
                managerId: {
                    required: true
                },
                userName: {
                    required: true,
                    cmaxLength: 32,
                },
                mobilePhone: {
                    required: true,
                    mobile: true,
                    remote: {
                        url: "checkMobileDistrict.html",
                        type: "get",
                        dataType: "text"
                    }
                }
            },
            messages: {
                managerId: {
                    required: "管理员身份不能为空!"
                },
                userName: {
                    required: "用户姓名不能为空!",
                    cmaxLength: "姓名长度最大为32位"
                },
                mobilePhone: {
                    required: "手机号码不能为空!",
                    remote: "该号码已存在！"
                }
            }
        });

    }

    function getUserName() {
        var phone = $("#mobilePhone").val();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/user/getNameByPhone.html",
            data: {
                phone: phone
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (res) {
                if (res.exist == "yes") {
                    $("#userName").empty();
                    $("#userName").val(res.name);
                    $("#userName").attr("readonly", "readonly");
                }
                else {
                    $("#userName").removeAttr("readonly");
                }
            },
            error: function () {
            }
        });
    }
    ;

    function showRole(managerId) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/user/getRoleNameByManagerId.html",
            data: {
                managerId: managerId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (res) {
                if (res) {
                    $("#role").show();
                    $("#roleName").html(res.roleName);
                }
                else {
                    $("#role").hide();
                }
            },
            error: function () {
            }
        });
    }
    ;
</script>
</body>
</html>