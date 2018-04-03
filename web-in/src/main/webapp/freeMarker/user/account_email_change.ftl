<!DOCTYPE html>
﻿<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-邮箱修改</title>
    <meta name="keywords" content="流量平台 邮箱修改"/>
    <meta name="description" content="流量平台 邮箱修改"/>

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
            width: 1200px;
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
            margin-left: 305px;
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
        <h3>账号信息
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/userInfo/saveEmail.html" method="post" name="emailForm" id="emailForm">
        <div class="tile mt-30">
            <div class="tile-header">
                邮箱修改
            </div>
            <div class="tile-content">
                <div class="row form">
                    <input type="hidden" name="id" id="id" value="${administer.id!}"/>

                    <div class="form-group">
                        <label>邮箱：</label>
                        <input type="text" readonly='readonly' value="${administer.email!}"/>
                    </div>

                    <div class="form-group">
                        <label>新邮箱：</label>
                        <input type="text" name="email" id="email" required/>
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
        $("#emailForm").packValidate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.parent().append(error);
            },
            errorElement: "span",
            rules: {
                email: {
                    required: true,
                    email: true,
                    maxlength: 64
                }
            },
            messages: {
                email: {
                    required: "电子邮箱不能为空！",
                    email: "请输入正确格式的电子邮件!"

                }
            }
        });
    }

</script>
</body>
</html>