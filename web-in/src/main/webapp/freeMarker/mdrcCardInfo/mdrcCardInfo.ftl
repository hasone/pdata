<#assign contextPath = rc.contextPath >
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>手机流量充值</title>
    <link href="${contextPath}/assets/css/bootstrap.min.css" rel="stylesheet">

    <style type="text/css">

        html {
            font-size: 62.5%;
        }

        body {
            padding-top: 56px;
            color: #646464;
            background-color: #f5f5f5;
            font-size: 14px;
            font-size: 1.4rem;
            font-family: "黑体", "Helvetica Neue", Helvetica, Arial, sans-serif;
        }

        a, a:visited, a:hover, a:link {
            text-decoration: none;
        }

        img {
            max-width: 100%;
        }

        .font-grayish {
            color: #a0a0a0;
        }

        .strong {
            font-weight: 700;
        }

        /*顶部悬浮导航条*/
        #flow-nav h3 {
            margin: 15px 10px;
        }

        #flow-nav .btn-back {
            position: absolute;
        }

        #flow-nav .btn-back a {
            color: #fff;
        }

        #flow-nav .btn-back img {
            max-height: 22px;
            margin-left: 10px;
            vertical-align: bottom;
        }

        .btn-primary {
            background-color: #007fce;
        }

        .btn-warning {
            background-color: #ff9800;
        }

        .btn-success {
            background-color: #9cc813;
            border-color: #9cc813;
        }

        .btn-gray, .btn-gray:focus, .btn-gray:hover, .btn-gray:active {
            background-color: #ededf2;
            color: #a0a0a0
        }

        /* flexslider */
        .flexslider {
            position: relative; /*height:400px;*/
            overflow: hidden;
            background: url(../img/loading.gif) 50% no-repeat;
        }

        .slides {
            position: relative;
            z-index: 1;
            padding: 0
        }

        .slides .img img {
            width: 100%
        }

        .flex-control-nav {
            position: absolute;
            bottom: 5px;
            z-index: 2;
            margin-bottom: 3%;
            width: 100%;
            text-align: center;
        }

        .flex-control-nav li {
            display: inline-block;
            width: 14px;
            height: 14px;
            margin: 0 5px;
            *display: inline;
            zoom: 1;
        }

        .flex-control-nav a {
            display: inline-block;
            width: 14px;
            height: 14px;
            line-height: 40px;
            overflow: hidden;
            background: url(../img/dot.png) right 0 no-repeat;
            cursor: pointer;
            font-size: 14px
        }

        .flex-control-nav .flex-active {
            background-position: 0 0;
        }

        .flex-direction-nav {
            position: absolute;
            z-index: 3;
            width: 100%;
            top: 45%;
        }

        .flex-direction-nav li a {
            display: block;
            width: 50px;
            height: 50px;
            overflow: hidden;
            cursor: pointer;
            position: absolute;
        }

        .flex-direction-nav li a.flex-prev {
            left: 40px;
            background: url(../img/prev.png) center center no-repeat;
        }

        .flex-direction-nav li a.flex-next {
            right: 40px;
            background: url(../img/next.png) center center no-repeat;
        }

        @media (min-width: 321px) and (max-width: 369px) {
            html {
                font-size: 62.5%;
            }
        }

        @media screen and (min-width: 370px) and (max-width: 479px) {
            html {
                font-size: 70.5%;
            }
        }

        @media screen and (min-width: 480px) and (max-width: 539px) {
            html {
                font-size: 80.5%;
            }
        }

        @media screen and (min-width: 540px) and (max-width: 639px) {
            html {
                font-size: 90.5%;
            }
        }

        @media screen and (min-width: 640px) and (max-width: 800px) {
            html {
                font-size: 100%;
            }
        }

        @media screen and (min-width: 801px) {
            html {
                font-size: 120%;
            }
        }

        body {
            background-color: #fff;
        }

        hr {
            margin: 5px 0;
        }

        .main {
            margin: 50px 15px;
        }

        .main input {
            height: 60px;
            font-size: 20px;
        }

        .btn, .btn:hover, .btn:active {
            margin: 20px auto;
            width: 100%;
            display: inline-block;
            border-radius: 0;
            border-color: #0085cf;
            background-color: #0085cf;
        }

        label.prompt {
            color: red;
            font-weight: 400;
        }

        .phone-prompt, .card-prompt, .psw-prompt {
            visibility: hidden;
        }
    </style>
</head>

<body>

<nav id="flow-nav" class="navbar bg-primary navbar-fixed-top">
    <h3 class="text-center">流量卡充值界面</h3>
</nav>

<div class="main">
    <form method="POST" action="${contextPath}/manage/mdrc/cardinfo/submit.html">
        <input type="tel" style="font-weight:bold" id="phone" name="chargeMobile" value="${chargeMobile!}"
               maxlength="11" class="form-control" placeholder="请输入手机号码"
               onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
               onafterpaste="this.value=this.value.replace(/[^\d]/g,'')"/>
        <label class="prompt phone-prompt">请输入正确的手机号码</label>

        <input type="tel" style="font-weight:bold" id="cardnumber" name="cardnumber" value="${cardnumber!}"
               class="form-control" placeholder="请输入充值卡卡号" maxlength="30" autocomplete="off"
               onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
               onafterpaste="this.value=this.value.replace(/[^\d]/g,'')"/>
        <label class="prompt card-prompt">请输入充值卡卡号</label>

        <input type="password" style="font-weight:bold" id="cardpsw" name="cardpsw" class="form-control"
               placeholder="请输入充值卡密码" maxlength="30">
        <label class="prompt psw-prompt">请输入充值卡密码</label>

        <button type="submit" class="btn btn-default btn-lg btn-danger" role="button" onclick="return doSubmit();"
                style="background-color: #0085cf;">确认充值
        </button>

        <div class="text-center">
            <label class="prompt text-center" id="submitErrMsg">${errMsg!}</label>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<script src="${contextPath}/assets/lib/jquery-1.11.3.min.js"></script>
<script type="text/javascript">

    if (top != self) {
        top.location = "${contextPath}" + "/manage/mdrc/cardinfo/chargeIndex.html";
    }

    $('#phone').focus(function () {
        $('#submitErrMsg').html('');
        $('.phone-prompt').css("visibility", "hidden");
    });

    $('#phone').blur(function () {
        if (!this.value) {
            $('.phone-prompt').css("visibility", "visible");
            return;
        }
        if (checkMobile(this.value)) {
            $('.phone-prompt').css("visibility", "visible");
            return;
        }

        $('.phone-prompt').css("visibility", "hidden");
    });


    $('#cardnumber').focus(function () {
        $('#submitErrMsg').html('');
        $('.card-prompt').css("visibility", "hidden");
    });

    $('#cardnumber').blur(function () {
        if (!this.value) {
            $('.card-prompt').css("visibility", "visible");
            return;
        }
        $('.card-prompt').css("visibility", "hidden");
    });

    $('#cardpsw').focus(function () {
        $('#submitErrMsg').html('');
        $('.psw-prompt').css("visibility", "hidden");
    });

    $('#cardpsw').blur(function () {
        if (!this.value) {
            $('.psw-prompt').css("visibility", "visible");
            return;
        }
        $('.psw-prompt').css("visibility", "hidden");
    });


    function checkMobile(mobile) {
        return !/^1[3-8][0-9]{9}$/.test(mobile);
    }
    function checkStr(str, length) {
        if (str != null && (str.length == length)) {
            return false;
        }
        return true;
    }

    //确认充值
    function doSubmit() {
        if (checkMobile($("#phone").val())) {
            $('.phone-prompt').css("visibility", "visible");
            return false;
        }

        if ($("#cardnumber").val() == '') {
            $('.card-prompt').css("visibility", "visible");
            if ($("#cardpsw").val() == '') {
                $('.psw-prompt').css("visibility", "visible");
            }
            return false;
        }

        if ($("#cardpsw").val() == '') {
            $('.psw-prompt').css("visibility", "visible");
            return false;
        }

        return true;
    }

    function checkCQMobile(obj) {
        if (checkMobile($("#phone").val())) {

            return 1;
        }
        else {
            return 0;
        }

    }

</script>
</body>
</html>
