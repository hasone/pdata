<!DOCTYPE html>
<#global contextPath = rc.contextPath />

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台</title>
    <meta name="keywords" content="四川流量平台"/>
    <meta name="description" content="四川流量平台"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
</head>
<body>

<div class="container">
    <div class="tile" style="width: 260px; margin: 0 auto;">
        <div class="tile-content">
            <div class="form-group form-group-sm form-inline">
                <label class="mr-10">手机号:</label>
                <input class="form-control" name="mobile" id="mobile">
            </div>
            <div class="form-group form-group-sm form-inline">
                <label class="mr-10">动态码:</label>
                <span id="verify_code"></span>
            </div>

            <div class="form-group form-group-sm" style="padding: 0 10px;">
                <a class="btn btn-sm btn-success" id="loginVerifyCode"
                   data-url="${contextPath}/manage/query/login.html">登录验证码</a>
                <a class="btn btn-sm btn-info pull-right" id="updatePwdVerifyCode"
                   data-url="${contextPath}/manage/query/updatePwd.html">更新密码验证码</a>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/lib/jquery-1.11.3.min.js"></script>
<script>

    $('#loginVerifyCode,#updatePwdVerifyCode').on("click", function () {
        getVerifyCode(this);
    });

    function getVerifyCode(ele) {
        var url = $(ele).data("url");
        var mobile = $("#mobile").val();
        $.getJSON(url, {mobile: mobile}, function (ret) {
            $("#verify_code").html(ret.code);
        });
    }
</script>
</body>
</html>