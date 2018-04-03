﻿<#global  contextPath = rc.contextPath />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${contextPath}/assets/css/flowCoupon.min.css"/>
    <title>领取二维码</title>
    <style>
        body {
            max-width: 800px;
            margin: 0 auto;
        }
    </style>
</head>

<body>

<img data-src="logo.png" class="justify logo">

<div class="pd-5p mt-40">

    <div class="ent-text mb-5 mt-10 double-arrow"><span>${record.entName!}</span></div>
    <div class="coupon">
        <div class="leftHoles"></div>
        <div class="rightHoles"></div>
        <div class="coupon-content">
            <div class="coupon-left">
                <span>恭喜您获得了</span>
                <div class="import-text">
                <#if (record.productSize<1024)>
                    <span class="primary-text">${(record.productSize)?string('#.##')}</span>KB
                </#if>
                <#if (record.productSize>=1024) && (record.productSize<1024*1024)>
                    <span class="primary-text">${(record.productSize/1024.0)?string('#.##')}</span>MB
                </#if>
                <#if (record.productSize>=1024*1024)>
                    <span class="primary-text">${(record.productSize/1024.0/1024.0)?string('#.##')}</span>GB
                </#if>

                </div>
            </div>
            <div class="coupon-right">
                <div class="phoneFont">充值号码${record.chargeMobile!}</div>
                <div>充值结果以充值短信为准。</div>
            </div>
        </div>
    </div>
</div>

<script>
    var baseUrl = "${contextPath}/assets/imgs/";
</script>
<script src="${contextPath}/assets/js/zepto.min.js"></script>
<script src="${contextPath}/assets/js/flowcard.common.min.js"></script>
<script>

</script>
</body>
</html>