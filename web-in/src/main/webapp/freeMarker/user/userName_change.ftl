<!DOCTYPE html>
﻿<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-姓名修改</title>
    <meta name="keywords" content="流量平台 姓名修改"/>
    <meta name="description" content="流量平台 姓名修改"/>

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
            width: 1000px;
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
            width: 450px;
        }

        .form .form-group label {
            width: 500px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 255px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .btn-save {
            margin-left: 650px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>账号信息</h3>
    </div>

    <form action="${contextPath}/manage/userInfo/saveUserName.html" method="post">
        <div class="tile mt-30">
            <div class="tile-header">
                姓名修改
            </div>
            <div class="tile-content">
                <div class="row form">
                    <input type="hidden" name="id" id="id" value="${administer.id!}"/>
                    <div class="form-group">
                        <label>姓名：</label>
                        <input type="text" name="userName" id="userName" class="hasPrompt" required/>
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
            errorElement: "span",
            rules: {
                userName: {
                    required: true,
                    maxlength: 64,
                }
            },
            messages: {
                userName: {
                    required: "姓名不能为空！",
                    maxlength: "姓名不能超过64个字符!"
                }
            }
        });
    }

</script>
</body>
</html>