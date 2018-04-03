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
    
    <meta name="format-detection" content="telephone=no">
    <!--<meta http-equiv="x-rim-auto-match" content="none">-->
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>签到</title>
    <style>
        body {
            background-color: #edf1f3;
        }

        img {
            display: inline-block;
        }

        .modal-body {
            padding-top: 140px;
        }

        .modal-body img {
            position: absolute;
            top: -25%;
            left: 0;
            width: 100%;
            height: 68%;
        }

        .content {
            top: 0;
            background: url(${contextPath}/assets/imgs/bg-mySign.png) no-repeat center bottom;
            background-size: contain;
        }

        .info-wrap {
            padding: 30px 0 30px 12px;
            color: #fff;
            font-size: 1.5rem;
        }

        .row {
            margin: 0;
        }

        .exchange-wrap {
            padding: 12px 0;
        }

        .exchange-wrap img {
            height: 1.6rem;
        }

        .exchange-wrap div div:nth-child(2) {
            margin-top: 0.5rem;
            font-size: 1.2rem;
        }

        .exchange-wrap div div:nth-child(3) {
            margin-top: 0.2rem;
            font-size: 1.5rem;
        }

        .col-xs-4 {
            padding: 0;
        }

        #totalMoney{
            position: absolute;
            right: 0;
            top: 50%;
            margin-top: -1rem;
            padding: 0 2rem;
            line-height: 2rem;
            height: 2rem;
            font-size: 1.2rem;
            background-color: #ffc51d;
            border-top-left-radius: 15px;
            border-bottom-left-radius: 15px;
        }

        #totalMoney img{
            height: 1.3rem;
            margin-top: -0.2rem;
            margin-right: 0.5rem;
        }

    </style>
</head>
<body>
<div class="content">
    <div class="module_content">
        <div class="row info-wrap">
            <img src="${headImgurl!}" style="width: 13%;"/>
            <span class="ml-5">${currentMobile!}</span>
            <span id="totalMoney">
                <img src="${contextPath}/assets/imgs/icon-totalMoney.png">总流量币:${score!}
            </span>
        </div>
        <div class="row exchange-wrap">
            <div class="col-xs-4 text-center">
                <img src="${contextPath}/assets/imgs/icon-clock.png">
                <div>起床时间</div>
                <div>${signTime!}</div>
            </div>
            <div class="col-xs-4 text-center">
                <img src="${contextPath}/assets/imgs/icon-continue.png">
                <div>本月早起</div>
                <div>${serialSignCount!}天</div>
            </div>
            <div class="col-xs-4 text-center">
                <img src="${contextPath}/assets/imgs/icon-money.png">
                <div>今日流量币</div>
                <div>${coinCount!}</div>
            </div>
        </div>
        <div class="text-center" style="font-size: 14px;padding-top: 5px;">共有${totalSignCount!}人参加早起计划</div>
    </div>
</div>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<!--<script type="text/javascript">-->
    <!--var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");-->
    <!--document.write(unescape("%3Cspan  style='display:none;' id='cnzz_stat_icon_1256913214'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256913214' type='text/javascript'%3E%3C/script%3E"));-->
<!--</script>-->
<script>
	<#--
    wx.config({
        debug: false,
        appId: '${wxconfig.appid!}',
        timestamp: '${wxconfig.timestamp!}',
        nonceStr: '${wxconfig.nonceStr!}',
        signature: '${wxconfig.signature!}',
        jsApiList: ['onMenuShareAppMessage', 'onMenuShareTimeline']
    });

    /**
     * config信息验证后会执行ready方法，
     * 所有接口调用都必须在config接口获得结果之后，
     * config是一个客户端的异步操作，
     * 所以如果需要在 页面加载时就调用相关接口，
     * 则须把相关接口放在ready函数中调用来确保正确执行。
     * 对于用户触发时才调用的接口，则可以直接调用，
     * 不需要放在ready 函数中。
     */
    wx.ready(function () {
        // 获取“分享给朋友”按钮点击状态及自定义分享内容接口
        wx.onMenuShareAppMessage({
            title: '快来广东移动公众号签到领流量币吧！', // 分享标题
            desc: '快来广东移动公众号签到领流量币吧！', // 分享描述
            link: "http://wx.10086.cn/fujian/lottery/game/index.html?openid=${openid!}",
            imgUrl: "${contextPathIMG}/assets/imgs/wx_logo.png",
            type: 'link', // 分享类型,music、video或link，不填默认为link
            success: function (res) {
                $.ajax({
                    url: ctx + "/lottery/addCount.html",
                    type: "GET",
                    async: false,
                    dataType: "json",
                    data: {type: 0},
                    success: function (data) {
                    }
                });
            }
        });

        wx.onMenuShareTimeline({
            title: '快来广东移动公众号签到领流量币吧！', // 分享标题
            desc: '快来广东移动公众号签到领流量币吧！', // 分享描述
            link: "http://wx.10086.cn/fujian/lottery/game/index.html?openid=${openid!}",
            imgUrl: "${contextPathIMG}/assets/imgs/wx_logo.png",
            type: 'link', // 分享类型,music、video或link，不填默认为link
            success: function (res) {
                $.ajax({
                    url: ctx + "/lottery/addCount.html",
                    type: "GET",
                    async: false,
                    dataType: "json",
                    data: {type: 1},
                    success: function (data) {

                    }
                });
            }
        });
    });
    -->
</script>
</body>
</html>