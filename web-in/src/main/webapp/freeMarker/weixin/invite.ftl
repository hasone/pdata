<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>邀请好友</title>
    <style>
        body {
            background: url(${contextPath}/assets/imgs/picall.png) no-repeat;
            background-size: cover;
            overflow: auto;
        }

        .module_content{
            padding: 0 15px 0 10px;
        }

        .item_content{
            padding-top: 6%;
            padding-bottom: 33%;
            width: 100%;
            height: 0;
            background-repeat: no-repeat;
            background-size: 100% 100%;
        }

        .item_1{
            margin-top: 54%;
            background-image: url(${contextPath}/assets/imgs/pic1.png);
        }

        .item_2{
            background-image: url(${contextPath}/assets/imgs/pic2.png);
        }

        .item_3{
            background-image: url(${contextPath}/assets/imgs/pic3.png);
        }

        .arrow{
            margin: 0 auto;
            width: 15px;
            height: 15px;
            background: url(${contextPath}/assets/imgs/arrow.png) no-repeat;
            background-size: 100% 100%;
        }
        .QRCode{
            float: left;
            margin-left: 13.2%;
            width: 27%;
        }

        .font-black {
            width: 59%;
            font-size: 13px;
            line-height: 20px;
        }

        .emphasize {
            font-size: 15px;
            font-weight: 700;
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="module_content">
    <div class="item_content item_1">
        <img src="${contextPath}/wx/common/qrCode.html?qrcodeId=${qrcodeId}" class="QRCode">
        <div class="font-black text-center pull-right">我是${nickName!}，<br><span class="emphasize">长按识别</span>我的专属二维码，<br>关注公众号<br>帮助我获得流量奖励！</div>
    </div>
    <div class="arrow"></div>
    <div class="item_content item_2">
        <div class="font-black text-center" style="margin-top: 3%;margin-left: 6%;"><span class="emphasize">关注并成功绑定</span><br>手机号码，<br>你也可获得流量奖励！</div>
    </div>
    <div class="arrow"></div>
    <div class="item_content item_3 mb-15">
        <div class="font-black text-center pull-right" style="margin-top: -3%;">点击微信公众号菜单栏<br>【玩转流量】-【赚流量】<br>生成属于自己的二维码海报<br><span class="emphasize">分享此链接给好友</span><br>和我一样赚流量</div>
    </div>
</div>
</body>
</html>