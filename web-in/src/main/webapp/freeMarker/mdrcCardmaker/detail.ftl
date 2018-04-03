<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-营销卡管理-制卡商详情</title>
    <meta name="keywords" content="流量平台制卡商详情"/>
    <meta name="description" content="流量平台制卡商详情"/>

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
        <h3>制卡商详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">

        <div class="tile-content">
            <div class="row">
                <div class="col-md-8">
                    <div class="form-group">
                        <span class="detail-label">制卡商名称:</span>
                    <#if record.name??>
                        <span class="detail-value">${record.name}</span>
                    </#if>
                    </div>


                    <div class="form-group">
                        <span class="detail-label">编号:</span>
                    <#if record.serialNumber??>
                        <span class="detail-value">${record.serialNumber}</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">创建时间:</span>
                    <#if record.createTime??>
                        <span class="detail-value">${record.createTime?datetime}</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">专员姓名:</span>
                    <#if record.operatorName??>
                        <span class="detail-value">${record.operatorName}</span>
                    </#if>
                    </div>


                    <div class="form-group">
                        <span class="detail-label">电话号码:</span>
                    <#if record.operatorMobile??>
                        <span class="detail-value">${record.operatorMobile}</span>
                    </#if>
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