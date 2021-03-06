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
    <link rel="stylesheet" href="${contextPath}/assets/css/cloudMessage.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>订单确认</title>
</head>
<body>
<img src="${contextPath}/assets/imgs/failure.png" class="img-20 justify">
<div class="pd-20p mt-40">
    <p class="intro mt-10">对不起，受理失败！<br>返回“我的订购”可查看订单详情</p>
</div>
<script>
    setTimeout(function () {
        window.location.href = "${contextPath}/yqx/order/index.html?autoBack=true";
    }, 3000);
</script>
</body>
</html>