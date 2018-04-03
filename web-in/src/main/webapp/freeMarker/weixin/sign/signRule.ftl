<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    
    <!-- 防止csrf攻击 -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>签到</title>
    <style>
        body{
            background-color: #edf1f3
        }
        .rule-wrap{
            width: 88%;
            margin: 0 auto;
            margin-top: 20%;
        }
        .ribbon {
            margin: 0 auto;
            width: 160px;
            height: 0;
            line-height: 0;
            border-top: 20px solid #90c31f;
            border-right: 15px solid transparent;
            border-bottom: 20px solid #90c31f;
            border-left: 15px solid transparent;
            font-size: 15px;
            color: #fff;
        }

        .rules {
            position: relative;
            z-index: -1;
            margin-top: -20px;
            padding: 55px 13px;
            background-color: #fff;
            border-radius: 5px;
            font-size: 14px;
            line-height: 27px;
            text-align: justify;
        }
    </style>
</head>
<body>
<div class="rule-wrap">
    <div class="ribbon text-center">签到详细规则</div>
    <div class="rules">
    	1、每天早上7点开放签到，前${maxAwardUserCount!}名签到者每人可获得${signGiveCount!}个流量币，每日流量币数量有限，先到先得；</br>
		2、${maxAwardUserCount!}名后签到者没有流量币奖励，可累积签到次数；每个用户每个自然月累积签到${maxSerialSign!}次， 在第${maxSerialSign!}次签到成功时可额外获得${signAwardCount!}个流量币奖励，当月后续签到将不再发放流量币奖励。
    </div>
</div>

</body>
</html>