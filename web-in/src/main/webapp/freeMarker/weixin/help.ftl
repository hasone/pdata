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
    <title>帮助</title>
    <style>
        body {
            background-color: #edf1f3;
        }

        .list-group .list-item {
            padding: 0 12px 12px;
            overflow: hidden;
        }

        .activity-content {
            padding: 8px 0 0;
        }

        .left-wrap {
            width: 35%;
            height: 100px;
            padding: 0 5px 0 0;
        }

        .left-wrap img {
            max-height: 100%;
        }

        .right-wrap {
            overflow: hidden;
            padding: 0 0 0 5px;
        }

        .activity-title {
            font-size: 15px;
        }

        .line-3 {
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
        }

        .list-group .list-item img {
            margin: 0;
        }
        
        .content {
            position: absolute;
            top: 0;
            bottom: 0;
            overflow: auto;
            width: 100%;
        }

    </style>
</head>
<body>
<div class="content">
    <div class="module_content">
        <ul class="list-group">
            <li class="list-item mb-10">
                <a href="#" style="display: block;">
                    <div class="activity-content">
                        <div class="left-wrap pull-left text-center">
                            <img src="${contextPath}/assets/imgs/help-score.png">
                        </div>
                        <div class="right-wrap">
                            <div class="activity-title font-black">如何获得流量币？</div>
                            <a href="#">
                                <div class="font-gray">参加活动赚流量币</div>
                            </a>
                            <a href="#">
                                <div class="font-gray mt-5">签到赚流量币</div>
                            </a>
                            <a href="#">
                                <div class="font-gray mt-5">邀请好友关注并绑定</div>
                            </a>
                        </div>
                    </div>
                </a>
            </li>
            <li class="list-item mb-10">
                <a href="#" style="display: block;">
                    <div class="activity-content">
                        <div class="left-wrap pull-left text-center">
                            <img src="${contextPath}/assets/imgs/help-vip.png">
                        </div>
                        <div class="right-wrap">
                            <div class="activity-title font-black">如何升级会员？</div>
                            <a href="#">
                                <div class="font-gray line-3">关注并绑定即可成为会员，当累积流量币达到200、800、2000时即可升级为青铜、白银、黄金会员。</div>
                            </a>
                        </div>
                    </div>
                </a>
            </li>
            <li class="list-item mb-10">
                <a href="#" style="display: block;">
                    <div class="activity-content">
                        <div class="left-wrap pull-left text-center">
                            <img src="${contextPath}/assets/imgs/help-use.png">
                        </div>
                        <div class="right-wrap">
                            <div class="activity-title font-black">流量币有什么用？</div>
                            <a href="#">
                                <div class="font-gray">用户获得的流量币可以兑换流量为手机号充值。<br>流量币每年12月31日23:59:59清零，请及时使用。</div>
                            </a>
                        </div>
                    </div>
                </a>
            </li>
        </ul>
    </div>
</div>

<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
</body>
</html>