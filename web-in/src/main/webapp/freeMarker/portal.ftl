<#global  contextPath = rc.contextPath />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/main.min.css">

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        body {
            background: #fff;
        }

        .dialog-sm .modal-dialog {
            width: 300px
        }
    </style>
</head>
<body>
<div class="headTop">
    <div class="center clearfix">
        <#-- <a class="fr" id="enterLogin" href="${contextPath}/manage/portal/enterpriselogin.html">政企登录</a> -->
        <a class="fr" id="userLogin" href="${contextPath}/manage/portal/personallogin.html">个人登录</a>
        <span class="userIcon fr"></span>
    </div>
</div>
<div class="head">
    <div class="center clearfix">
        <a id="logo" href="#"><img src="${contextPath}/assets/imgs/portal-logo.png" alt=""></a>
        <span class="province">四川</span>
    </div>
</div>
<div class="indexBannerBox">
    <div class="center">
        <div class="warp">
            <ul class="allImgs clearfix">
                <li><img src="${contextPath}/assets/imgs/indexbanner1.png" alt=""></li>
                <li><img src="${contextPath}/assets/imgs/indexbanner2.png" alt=""></li>
                <li><img src="${contextPath}/assets/imgs/indexbanner3.png" alt=""></li>
            </ul>
            <div class="arrL"></div>
            <div class="arrR"></div>
            <ul class="items">
                <li class="current"></li>
                <li></li>
                <li></li>
            </ul>
        </div>
    </div>
</div>
<div class="commonlyFunction">
    <div class="center clearfix">
        <div class="flow fl">
            <a href="#" data-target="#tip-dialog" data-toggle="modal">
                <img src="${contextPath}/assets/imgs/flow.png" alt="">
            </a>
        </div>
        <div class="enquiry fl">
            <a href="#" data-target="#tip-dialog" data-toggle="modal">
                <img src="${contextPath}/assets/imgs/enquiry.png" alt="">
            </a>
        </div>
    </div>
</div>
<div class="copyRight mt20">
    <div class="center">
        <p>Copyright 1999-2016 中国移动 版权所有 京ICP备05002571号</p>
    </div>
</div>

<div class="modal fade dialog-sm" id="tip-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-muted bd-muted">
                <a type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></a>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-content">敬请期待</span>
            </div>
            <div class="modal-footer">
                <a class="btn btn-warning btn-sm" data-dismiss="modal">确 定</a>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<script src="${contextPath}/assets/lib/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/lib/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/portal.js"></script>

</body>
</html>