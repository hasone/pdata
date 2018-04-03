<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>广东移动集团流量卡</title>
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <style>
        .wrap {
            padding: 50px;
        }

        img {
            width: 35%;
            margin-top: 20px;
            margin-bottom: 40px;
        }

        p{
            margin-bottom: 50px;
        }

        .btn-primary:focus, .btn-primary:hover {
            background-color: #337ab7;
            border-color: #2e6da4;
        }
    </style>
</head>
<body>
<div class="wrap text-center">
    <img src="${contextPath}/assets/img/receiveSuccess.png">
    <p>您的流量领用申请已成功提交,流量到账时间以10086短信为准！</p>
    <a class="btn btn-primary" style="width: 100%;margin-bottom: 10px" href="${contextPath}/manage/mdrc/cardinfo/chargeIndex.html">确 定</a>
</div>
</body>
</html>