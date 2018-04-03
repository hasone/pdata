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
        .col-xs-3, .col-xs-9 {
            padding: 0;
        }

        .row {
            margin: 0;
        }
    </style>
</head>
<body>
<div class="content-wrap" style="border: 0;">
    <div class="content-item">
        <div class="p-title">产品说明</div>
        <div class="p-content">
            <div class="p-intro font-ml-gray">
                <div class="row">
                    <div class="col-xs-3">适用用户：</div>
                    <div class="col-xs-9">重庆移动用户</div>
                </div>
                <div class="row">
                    <div class="col-xs-3">适用范围：</div>
                    <div class="col-xs-9">全国范围（不含港澳台地区），适用2G/3G/4G网络</div>
                </div>
                <div class="row">
                    <div class="col-xs-3">使用优先级：</div>
                    <div class="col-xs-9">优先于已办理的套餐流量</div>
                </div>
                <div class="row">
                    <div class="col-xs-3">有效期：</div>
                    <div class="col-xs-9">至次月月底</div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="content-wrap mt-10" style="border: 0;">
    <div class="content-item">
        <div class="p-title">购买说明</div>
        <div class="p-content">
            <div class="p-intro font-ml-gray">
                <div class="row">
                    <div class="col-xs-3">到账时间：</div>
                    <div class="col-xs-9">支付成功后48小时内到账，如未到账，请致电4001008686</div>
                </div>
                <div class="row">
                    <div class="col-xs-3">购买规则：</div>
                    <div class="col-xs-9">
                        （1）每月单个号码流量充值次数最多20次<br>
                        （2）请确认用户手机状态正常，欠费、套餐互斥、办理次数达到上限等可能造成流量无法到账
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-3">温馨提示：</div>
                    <div class="col-xs-9">请避免在每月最后${startDay!}天${startTime!}至次月第${endDay!}天${endTime!}订购，在此期间流量可能无法到账。</div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="content-wrap mt-10" style="border: 0;">
    <div class="content-item">
        <div class="p-title">产品优惠规则</div>
        <div class="p-content">
            <div class="p-intro font-ml-gray">
                所有用户可享受9.2折折扣优惠
            </div>
        </div>
    </div>
</div>
</body>
</html>