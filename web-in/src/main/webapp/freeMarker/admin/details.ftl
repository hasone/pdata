<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-用户管理-用户信息详情</title>
    <meta name="keywords" content="流量平台 营销管理"/>
    <meta name="description" content="流量平台 营销管理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .table > thead > tr > th {
            line-height: initial;
        }

        .table > thead > tr > th, .table > tbody > tr > td {
            vertical-align: middle;
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
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
        <h3>用户管理
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="${contextPath}/manage/user/index.html?back=1">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            用户信息详情
        </div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-8">
                <#if roleName?exists>
                    <div class="form-group">
                        <span class="detail-label">用户角色:</span>
                        <span class="detail-value">${(roleName)!}</span>
                    </div>
                </#if>
                <#if enterpriseName?exists>
                    <div class="form-group">
                        <span class="detail-label">企业名称:</span>
                        <span class="detail-value">${enterpriseName!}</span>
                    </div>
                </#if>
                <#if districtName?exists>
                    <div class="form-group">
                        <span class="detail-label">所属区域:</span>
                        <span class="detail-value">${(districtName)!}</span>
                    </div>
                </#if>
                <#if cardmarkerName?exists>
                    <div class="form-group">
                        <span class="detail-label">制卡商名称:</span>
                        <span class="detail-value">${cardmarkerName!}</span>
                    </div>
                </#if>
                    <div class="form-group">
                        <span class="detail-label">姓名:</span>
                        <span class="detail-value">${administer.userName!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">手机号码:</span>
                        <span class="detail-value">${administer.mobilePhone}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">邮箱:</span>
                        <span class="detail-value">${administer.email!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">创建时间:</span>
                        <span class="detail-value">${administer.createTime?datetime}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">修改时间:</span>
                        <span class="detail-value">${administer.updateTime?datetime}</span>
                    </div>


                </div>
            </div>
        </div>
    </div>


</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
    });
</script>
</body>
</html>