<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-营销卡管理-卡批次详情</title>
    <meta name="keywords" content="流量平台 卡批次详情"/>
    <meta name="description" content="流量平台 卡批次详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .form-group label {
            width: 40%;
            text-align: right;
        }
    </style>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>卡批次详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">制卡信息</div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-12">
                	<div class="form-group">
                        <label>卡名称：</label>
                        <span class="detail-value">${mdrcBatchConfig.configName!}</span>
                    </div>
                	
                	<div class="form-group">
                        <label>卡批次号：</label>
                        <span class="detail-value">${mdrcBatchConfig.serialNumber!}</span>
                    </div>

                    <div class="form-group">
                        <label>制卡数量：</label>
                        <span class="detail-value">${mdrcBatchConfig.amount!}&nbsp;张</span>
                    </div>
                    
                    <div class="form-group">
                        <label>产品大小：</label>
                        <span class="detail-value" id="productSize"></span>
                    </div>
                    
                    <div class="form-group">
                        <label>起始卡序列号：</label>
                        <span class="detail-value">${startCardNum!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>终止卡序列号：</label>
                        <span class="detail-value">${endCardNum!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>正面预览：</label>
	                    <#if mdrcTemplate.frontImage??>
	                        <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.frontImage}"
	                             style="max-width:250px;"/>
	                    <#else>
	                        -
	                    </#if>
                    </div>

                    <div class="form-group">
                        <label>背面预览：</label>
	                    <#if mdrcTemplate.rearImage??>
	                        <img src="${contextPath}/manage/mdrc/template/getFileS3.html?id=${mdrcTemplate.id}&filename=${mdrcTemplate.rearImage}"
	                             style="max-width:250px;"/>
	                    <#else>
	                        -
	                    </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    
    <div class="tile mt-30">
        <div class="tile-header">卡收货人信息</div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-12">
                	<div class="form-group">
                        <label>邮寄方式：</label>
                        <span class="detail-value">顺丰到付</span>
                    </div>

                    <div class="form-group">
                        <label>收件人姓名：</label>
                        <span class="detail-value">${bacthConfigInfo.name!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>联系电话：</label>
                        <span class="detail-value">${bacthConfigInfo.mobile!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>详细地址：</label>
                        <span class="detail-value">${bacthConfigInfo.address!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>邮编：</label>
                        <span class="detail-value">${bacthConfigInfo.postcode!}</span>
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
        init();
    });

    /**
     * 初始化
     */
    function init() {
        getProductSize();
    }

    function getProductSize() {
        var productSize = ${product.productSize!};
        $("#productSize").html(sizeFun(productSize));
    }

    function sizeFun(size) {
        if (size == null) {
            return "-";
        }
        if (size < 1024) {
            return size + "KB";
        }
        if (size >= 1024 && size < 1024 * 1024) {
            return (size * 1.0 / 1024) + "MB";
        }
        if (size >= 1024 * 1024) {
            return (size * 1.0 / 1024 / 1024) + "GB";
        }

        return size * 1.0 / 1024 + "MB";
    }
</script>
</body>
</html>