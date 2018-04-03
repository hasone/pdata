<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-修改密码</title>
    <meta name="keywords" content="流量平台 修改密码"/>
    <meta name="description" content="流量平台 修改密码"/>

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
        .tile-content {
            padding-bottom: 60px;
        }

        .module-header {
            padding: 15px;
            background-color: #475b75;
        }
    </style>
</head>
<body>

<div class="module-header mb-20">
    <img src="${contextPath}/assets/imgs/login_logo.png" style="width: 125px;">
</div>

<div class="main-container">
    <div class="tile mt-30">
        <div class="tile-header">
            修改密码
        </div>
        <div class="tile-content text-center mt-40">
            <img src="${contextPath}/assets/imgs/icon-check.png">修改密码成功，<span id="time"></span>秒跳转至登录页
        </div>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        time($('#time'));
    });

    var timer;
    var wait = 3;
    function time(ele) {
        if (wait === 0) {
            window.location.href="${contextPath}/manage/user/login.html";
        } else {
            ele.html(wait);
            wait--;
            timer = setTimeout(function () {
                time(ele);
            }, 1000);
        }
    }
</script>
</body>
</html>