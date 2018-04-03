<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量币购买订单详情</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>

    <link rel="stylesheet" href="${contextPath}/assets_individual/theme/default/theme.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/main.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets_individual/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets_individual/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form-group label {
            width: 80px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mb-20">
        <h3>业务办理-流量币购买-订单详情
            <a href="${contextPath}/individual/flowcoin/purchaseIndex.html" class="btn btn-sm btn-success pull-right"
               style="color: white;">
                <i class="fa fa-mail-reply mr-5"></i>返 回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="form-group">
            <span>订单号:</span>
            <span>${record.systemSerial!}</span>
        </div>
        <div class="form-group">
            <span>创建时间:</span>
            <span>${record.createTime?datetime}</span>
        </div>
        <div class="form-group">
            <span>数量:</span>
            <span>${record.count!}</span>
        </div>
        <div class="form-group">
            <span>金额:</span>
            <span>${(record.price/100.0)?string("#.##")}元</span>
        </div>
        <div class="form-group">
            <span>选择支付方式:</span>
            <span>话费支付</span>
        </div>
        <div class="form-group">
            <span>订单状态:</span>
	        <span>
	        	<#if record.status == 0 || record.status == 3>
                    待支付
                </#if>
                <#if record.status == 1>
                    支付中
                </#if>
                <#if record.status == 2>
                    支付成功
                </#if>
                <#if record.status == 4>
                    已取消
                </#if>
	        </span>
        </div>
        <div class="tile tile-sm orange-tile orange-border mt-30">
            <p>流量币购买须知：</p>
            <p>1、流量币单价是多少？</p>
            <p>答：0.1元可购买1个流量币。</p>
            <p>2、流量币是否具有有限期？</p>
            <p>答：您购买的流量币将在购买时间的次年年底失效，通过赠送、红包、营销活动等形式获得的流量币将在获赠时间的次月月底失。</p>
        </div>
    </div>
</div>

<!--[if lt IE 9]>
<script src="${contextPath}/assets_individual/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets_individual/lib/es5-sham.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script>
    Object.getPrototypeOf = function getPrototypeOf(object) {
        return object.__proto__;
    };
</script>
<![endif]-->

<script>
    if (window.ActiveXObject) {
        var reg = /10\.0/;
        var str = navigator.userAgent;
        if (reg.test(str)) {
            Object.getPrototypeOf = function getPrototypeOf(object) {
                return object.__proto__;
            };
        }
    }
</script>


<script src="${contextPath}/assets_individual/lib/require.js"></script>
<script src="${contextPath}/assets_individual/dist/config.js"></script>
<script>
    require(["react", "react-dom", "MessageBox", "common", "bootstrap"], function (React, ReactDOM, ListData, MessageBox) {
        listeners();
    });

    /**
     * 监听
     */
    function listeners() {

    }
</script>
</body>
</html>