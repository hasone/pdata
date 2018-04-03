<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-审批流程详情</title>
    <meta name="keywords" content="流量平台 审批流程详情"/>
    <meta name="description" content="流量平台 审批流程详情"/>

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
        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .form-group label {
            width: 180px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 186px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>
</head>
<body>

<div class="main-container">


    <div class="module-header mt-30 mb-20">
        <h3>审批流程详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-content">
            <div class="row form">

                <div class="col-md-6 col-md-offset-3">
                    <div class="form-group">
                        <label>审批流程发起角色：</label>
                        <span>${approvalProcess.roleName!}</span>
                    </div>

                    <div class="form-group">
                        <label>审批级数：</label>
                            <span>
                                <#if approvalProcess.stage == 0>
                                    无审批流程
                                </#if>
                                <#if approvalProcess.stage == 1>
                                    一级审批流程
                                </#if>
                                <#if approvalProcess.stage == 2>
                                    二级审批流程
                                </#if>
                                <#if approvalProcess.stage == 3>
                                    三级审批流程
                                </#if>
                                <#if approvalProcess.stage == 4>
                                    四级审批流程
                                </#if>
                                <#if approvalProcess.stage == 5>
                                    五级审批流程
                                </#if>
                            </span>
                    </div>

                    <div class="form-group">
                        <label>创建时间：</label>
                        <span>${approvalProcess.createTime?datetime}</span>
                    </div>

                    <div class="form-group">
                        <label>修改时间：</label>
                        <span>${approvalProcess.updateTime?datetime}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>是否给审批流程的发起人发送提醒短信：</label>
                        <span><#if approvalProcess.msg?? && approvalProcess.msg == 1>发送<#else>不发送</#if></span>
                    </div>

                    <div class="form-group">
                        <label>是否给审批管理员发送代办任务的提醒：</label>
                        <span><#if approvalProcess.recvmsg?? && approvalProcess.recvmsg == 1>发送<#else>不发送</#if></span>
                    </div>

                    <div class="form-group">
                        <label>审批流程：</label>
                    <#if approvalDetailList??>
                        <#list approvalDetailList as approvalDetail>
                            <span>${approvalDetail.roleName!}</span>
                        </#list>
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