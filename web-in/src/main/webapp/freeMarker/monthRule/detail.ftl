<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-包月赠送详情</title>
    <meta name="keywords" content="流量平台 包月赠送详情"/>
    <meta name="description" content="流量平台 包月赠送详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
        }

        textarea {
            width: 323px;
            resize: none;
            height: 75px;
            outline: none;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>包月赠送详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-header">
            活动信息
        </div>
        <div class="tile-content">
            <div class="row form">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>企业名称：</label>
                        <span>${(rule.entName)!}</span>
                    </div>
                    <div class="form-group">
                        <label>企业编码：</label>
                        <span>${(rule.entCode)!}</span>
                    </div>
                    <div class="form-group">
                        <label>活动名称：</label>
                        <span>${(rule.activityName)!}</span>
                    </div>
                    <div class="form-group">
                        <label>赠送月数：</label>
                        <span>${(rule.monthCount)!}</span>
                    </div>
                    <div class="form-group">
                        <label>赠送总数：</label>
                        <span>${(rule.total)!}</span>
                    </div>
                    <div class="form-group">
                        <label>产品名称：</label>
                        <span>${(rule.productName)!}</span>
                    </div>
                    <div class="form-group">
                        <label>赠送值：</label>
                        <span>${(rule.productSize/1024.0)?string('#.##')}MB</span>
                    </div>
                    <#if provinceFlag??&&provinceFlag!='sd'>
                        <div class="form-group">
                            <label>首次赠送时间：</label>
                            <span>${rule.startTime?date}</span>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn"
           href="${contextPath}/manage/monthRecord/index.html?ruleId=${(rule.id)!}">查看赠送用户信息</a>
    </div>

</div>

</body>
</html>