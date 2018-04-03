<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-账号管理</title>
    <meta name="keywords" content="流量平台 账号管理"/>
    <meta name="description" content="流量平台 账号管理"/>

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
        .form .form-group label {
            width: 70px;
            text-align: right;
            margin-left: 15px;
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>账号信息</h3>
    </div>
    <form>
        <div class="tile mt-30">
            <div class="tile-header">
                账号信息
            </div>

            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>姓名：</label>
                    <#if administer.userName?exists && administer.userName!="">
                        <span>${administer.userName}</span>
                    <#else>
                        <a href="${contextPath}/manage/userInfo/changeUserName.html?adminId=${administer.id}"
                           style="color: red;">填写姓名</a>
                    </#if>
                    </div>
                <#if roleName?exists>
                    <div class="form-group">
                        <label>用户角色：</label>
                        <span>${(roleName)!}</span>
                    </div>
                </#if>

                <#if enterpriseName?exists>
                    <div class="form-group">
                        <label>企业名称：</label>
                        <span>${enterpriseName!}</span>
                    </div>
                </#if>

                <#if districtName?exists>
                    <div class="form-group">
                        <label>所属区域：</label>
                        <span>${(districtName)!}</span>
                    </div>
                </#if>

                    <div class="form-group">
                        <label>手机号码：</label>
                        <span>${administer.mobilePhone}</span>
                    </div>

                    <div class="form-group">
                        <label>邮箱：</label>
                        <span>${administer.email!}</span>
                        <a href="${contextPath}/manage/userInfo/changeEmail.html" style="color: red;">修改</a>
                    </div>

                    <div class="form-group">
                        <label>创建时间：</label>
                        <span>${administer.createTime?datetime}</span>
                    </div>

                    <div class="form-group">
                        <label>修改时间：</label>
                        <span>${administer.updateTime?datetime}</span>
                    </div>

                </div>
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    <#if closeChangePass?exists && closeChangePass=="true">
    <#else>
    <div class="mt-30">
        <a class="btn btn-sm btn-danger dialog-btn" href="${contextPath}/manage/userInfo/updatePassword.html">修改密码</a>
    </div>
    </#if>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        init();
    });
</script>
<script>
    function init() {

        $("#userName", top.document.body).html("${administer.userName!}");

        String.prototype.trim = function () {
            return this.replace(/(^\s*)|(\s*$)/g, '');
        }
    }
</script>
</body>
</html>