<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-权限管理</title>
    <meta name="keywords" content="流量平台 编辑职位"/>
    <meta name="description" content="流量平台 编辑职位"/>

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
            width: 250px;
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
        <h3>职位管理
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/manager/index.html?back=1'">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/manager/saveEditManager.html" method="post" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-header">
                编辑职位
            </div>

            <input name="id" id="id" value="${manager.id!}" type="hidden"/>
            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>职位角色：</label>
                        <span>${manager.roleName!}</span>
                    </div>

                    <input type="hidden" name="name" id="name">

                    <div class="form-group" id="user_select">
                        <label>职位名称：</label>
                        <div class="btn-group btn-group-sm">
                            <input style="width: 0; height: 0; opacity: 0" name="name_select" id="name_select" required>
                            <button type="button" class="btn btn-default" style="width:225px" id="btn_select">选择名称
                            </button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                            <#if districts??>
                                <#list districts as district>
                                    <li data-value="${(district.name)!}"><a href="#">${district.name!}</a></li>
                                </#list>
                            </#if>
                            </ul>
                        </div>
                        <span>${manager.roleName!}</span>
                        <a href="javascript:void(0);" onclick="showUserDefine()">自定义</a>

                    </div>

                    <div class="form-group" id="user_define" style="display:none">
                        <label>职位名称：</label>
                        <input type="text" name="name_define" id="name_define" maxLength="64" class="hasPrompt" style="display:none"
                               required/>
                        <span>${manager.roleName!}</span>
                        <a href="javascript:void(0);" onclick="showUserSelect()">选择名称</a>
                    </div>

                </div>
            </div>

            <div class="btn-save mt-30">
                <a href="javascript:void(0);" onclick="onSubmit()" class="btn btn-sm btn-warning dialog-btn">保存</a>
                <span style="color:red" id="error_msg">${errorMsg!}</span>
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var select = "${select!}";
    var managerName = "${manager.name}";
    require(["common", "bootstrap", "easyui"], function () {
        checkFormValidate();
        init();
    });

    function init() {
        if (select == "true") {

            $("#user_define").hide();
            $("#name_define").hide();
            $("#user_select").show();
            $("#name_select").show();

            $("#name_select").val(managerName);
            $("#btn_select").html(managerName);
        }
        else {
            $("#user_define").show();
            $("#name_define").show();
            $("#user_select").hide();
            $("#name_select").hide();

            $("#name_define").val(managerName);
        }

    }

    function onSubmit() {
        if ($("#user_define").is(":hidden")) {
            $("#name").val($("#name_select").val());
        }
        if ($("#user_select").is(":hidden")) {
            $("#name").val($("#name_define").val());
        }

        $("#table_validate").submit();

        return true;
    }


    function checkFormValidate() {
        $("#table_validate").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if($(element).attr("id") == "name_define"){
            		$(element).parent().append(error);
            	}else{
            		 $(element).parent().parent().append(error);
            	}
            },
            errorElement: "span",
            rules: {
                roleId: {
                    required: true
                },
                name_define: {
                    required: true,
                    cmaxLength: 64,
                },
                name_select: {
                    required: true,
                    cmaxLength: 64,
                }
            },
            messages: {
                roleId: {
                    required: "角色不能为空!"
                },
                name_define: {
                    required: "职位范围不能为空!",
                    cmaxLength: "职位范围长度最大为64位"
                },
                name_select: {
                    required: "职位范围不能为空!",
                    cmaxLength: "职位范围长度最大为64位"
                }
            }
        });

    }

    function showUserDefine() {
        $("#user_define").show();
        $("#name_define").show();
        $("#user_select").hide();
        $("#name_select").hide();
    }

    function showUserSelect() {
        $("#user_define").hide();
        $("#name_define").hide();
        $("#user_select").show();
        $("#name_select").show();
    }

</script>


</body>
</html>