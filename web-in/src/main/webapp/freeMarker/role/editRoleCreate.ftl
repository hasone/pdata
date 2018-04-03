<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-角色创建权限</title>
    <meta name="keywords" content="流量平台 角色创建权限"/>
    <meta name="description" content="流量平台 角色创建权限"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 900px;
        }

        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
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
            display: block;
            padding-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .form .form-group ul {
            margin: -25px 0 0 305px;
        }

        .btn-save {
            margin-left: 300px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>角色创建权限
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/role/index.html?back=1'">返回</a>
        </h3>
    </div>

    <form id="table_validate" action="${contextPath}/manage/role/saveRolesCreate.html" method="post" role="form">
        <div class="tile mt-30">

            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>角色名称：</label>
                        <span type="text" name="name" id="name">${(role.name)!}</span>
                        <input type="hidden" name="roleId" id="roleId" class="hasPrompt" value="${(role.roleId)!}"/>
                    </div>

                    <div class="form-group">
                        <label>权限：</label>
                        <ul>
                        <#list roles as role>

                            <li>
                                        <span class="input-checkbox">
                                            <input type="checkbox" name="roleIds" id="${role.roleId}"
                                                   value="${role.roleId}"
                                                <#if roleIds??>
                                                    <#list roleIds as roleId>
                                                        <#if roleId == role.roleId>
                                                   checked='checked'
                                                            <#break>
                                                        </#if>
                                                    </#list>
                                                </#if>
                                            >
                                            
		                                	<label style="width:30px" for="${role.roleId}" class="c-checkbox mr-15 rt-1
		                                	 	<#if roleIds??>
                                                    <#list roleIds as roleId>
                                                        <#if roleId == role.roleId>
                                                            checked
                                                            <#break>
                                                        </#if>
                                                    </#list>
                                                </#if>
                                                 "
                                            ></label>
                                                 <span>${(role.name)!}</span>
                                        </span>
                            </li>

                        </#list>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-save mt-30">
            <input type="submit" class="btn btn-sm btn-warning dialog-btn" value="保存"/>&nbsp;&nbsp;&nbsp;
            <span style="color:red" id="error_msg">${errorMsg!}</span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        checkFormValidate();
    });
</script>

<script>
    function checkFormValidate() {
        $("#table_validate").packValidate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if ($(element).siblings('span').hasClass('prompt')) {
                    $(element).siblings('span.prompt').before(error);
                } else {
                    $(element).parent().append(error);
                }
            },

            errorElement: "span",
        });
    }
</script>

</body>
</html>