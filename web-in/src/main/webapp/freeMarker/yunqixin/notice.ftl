<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/amazeui.slick.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cloudMessage.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>购买说明</title>
    <style>
        body {
            overflow: auto;
        }
        .btn-notice{
            display: block;
            width: 13rem;
            margin: 0 auto;
            margin-top: 5rem;
            background-color: #c9302c;
            border-color: #ac2925;
        }
    </style>
</head>
<body>
<div class="content-wrap" style="border: 0;">
    <div class="content-item">
        <div class="p-title">用户须知</div>
        <div class="p-content">
            <div class="p-intro font-ml-gray">
                （1）本次政企专享流量尊享包包含的流量均为四川省内流量，收费不区分上下半月，流量仅限当月有效，次月清零。次月若需继续参与本活动，请登录云企信客户端再次订购。<br>
                （2）云企信用户的手机主资费套餐三个月内有降档记录，则暂不能参加本次专享流量尊享包优惠活动。<br>
                （3）云企信用户在参加本次专享流量尊享包优惠活动当月，手机主资费套餐不能降档，不能取消流量附加套餐。<br>
                （4）V网用户享受85折优惠，每月最后${startDay!}天${startTime!}至次月第${endDay!}天${endTime!}无法订购，使用优先于已办理的套餐流量。<br>
                （5）请确保用户手机状态正常，欠费、套餐互斥、办理次数达到上限等可能造成流量无法到账。<br>
                （6）本次活动订购的流量预计到账时间为48小时，若流量到账延时，请联系本次活动客户经理：13438098668。<br>
                （7）本活动解释权归四川移动公司所有。
            </div>
        </div>
    </div>
</div>
<a class="btn btn-danger btn-notice" href="${contextPath}/yqx/order/index.html">我知道了</a>

</body>
</html>