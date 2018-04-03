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
    <meta name="keywords" content="流量平台 用户管理"/>
    <meta name="description" content="流量平台 用户管理"/>

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
        <h3>用户管理
        <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="history.go(-1)">返回</a>
        </h3>
    </div>
    <#if isUseEnterList?exists && isUseEnterList=="false" && roleId?exists && roleId == 3>
    <button class="btn btn-primary" id="history-btn">修改历史</button>
    </#if>
    <#if verifyingCount?exists && verifyingCount gt 0>
    <br/>
        修改审批中，如需查看具体信息请点击上方修改历史
    </#if>
    <form>
        <div class="tile mt-30">
            <div class="tile-header">
               用户信息详情
            </div>

            <div class="tile-content">
                <div class="row form">
                
                
                <#if roleName?exists>
                    <div class="form-group">
                        <label>用户角色：</label>
                        <span>${(roleName)!}</span>
                    </div>
                </#if>                

                    <div class="form-group">
                        <label>姓名：</label>
                    <#if administer.userName?exists && administer.userName!="">
                        <span>${administer.userName!}</span>
                    </#if>
                    </div>
                    
                    <div class="form-group">
                        <label>手机号码：</label>
                        <span>${administer.mobilePhone!}</span>
                    </div>

                <#if enterName?exists>
                    <div class="form-group">
                        <label>企业名称：</label>
                        <span>${(enterName)!}</span>
                    </div>
                </#if>       

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
        
        $("#history-btn").on("click", function () {
            window.location.href = "${contextPath}/manage/adminChange/historyList.html?adminId=" + ${adminId!};
        });

        String.prototype.trim = function () {
            return this.replace(/(^\s*)|(\s*$)/g, '');
        }
    }
</script>
</body>
</html>