<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-营销卡管理-营销卡详情</title>
    <meta name="keywords" content="流量平台 营销卡详情"/>
    <meta name="description" content="流量平台 营销卡详情"/>

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
        <h3>营销卡详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">

        <div class="tile-content">
            <div class="row">
                <div class="col-md-8">
                    <div class="form-group">
                        <span class="detail-label">卡号:</span>
                    <#if record.cardNumber??>
                        <span class="detail-value">${record.cardNumber}</span>
                    </#if>
                    </div>


                    <div class="form-group">
                        <span class="detail-label">状态:</span>
                    <#if record.status??>
                        <span class="detail-value">
                            <#if (record.opStatus > 7)>
		                            		${cardStatus[(record.opStatus)?string]}	 
		                            	<#else>
                            ${cardStatus[(record.status)?string]}
                            </#if>
		                            </span>
                    </#if>
                    </div>


                    <div class="form-group">
                        <span class="detail-label">用户手机号码 :</span>
                    <#if record.userMobile??>
                        <span class="detail-value">${record.userMobile}</span>
                    </#if>
                    </div>

					<!--
                    <div class="form-group">
                        <span class="detail-label">用户IP地址:</span>
	                    <#if record.userIp??>
	                        <span class="detail-value">${record.userIp!}</span>
	                    </#if>
                    </div>
					-->
                    <div class="form-group">
                        <span class="detail-label">充值结果:</span>
                        <span class="detail-value">${record.chargeMsg!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">创建时间:</span>
                    <#if record.createTime??>
                        <span class="detail-value">${record.createTime?datetime}</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">签收时间:</span>
                    <#if record.storedTime??>
                        <span class="detail-value">${record.storedTime?datetime}</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">激活时间:</span>
                    <#if record.activatedTime??>
                        <span class="detail-value">${record.activatedTime?datetime}</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">使用时间:</span>
                    <#if record.usedTime??>
                        <span class="detail-value">${record.usedTime?datetime}</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">生效时间:</span>
                    <#if record.startTime??>
                        <span class="detail-value">${record.startTime?datetime}</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">截止时间:</span>
                    <#if record.deadline??>
                        <span class="detail-value">${record.deadline?datetime}</span>
                    </#if>
                    </div>
					<!--
                    <div class="form-group">
                        <span class="detail-label">去激活时间:</span>
                    <#if record.deactivateTime??>
                        <span class="detail-value">${record.deactivateTime?datetime}</span>
                    </#if>
                    </div>
					-->
                    <div class="form-group">
                        <span class="detail-label">锁定时间:</span>
                    <#if record.lockedTime??>
                        <span class="detail-value">${record.lockedTime?datetime}</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">解锁时间:</span>
                    <#if record.unlockTime??>
                        <span class="detail-value">${record.unlockTime?datetime}</span>
                    </#if>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">销卡时间:</span>
                    <#if record.deleteTime??>
                        <span class="detail-value">${record.deleteTime?datetime}</span>
                    </#if>
                    </div>
					<!--
                    <div class="form-group">
                        <span class="detail-label">延期时间:</span>
                    <#if record.extendTime??>
                        <span class="detail-value">${record.extendTime?datetime}</span>
                    </#if>
                    </div>-->
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