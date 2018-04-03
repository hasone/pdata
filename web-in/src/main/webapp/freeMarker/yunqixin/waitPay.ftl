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
    <title>订购页面</title>
</head>
<body>
<div class="result-wrap">
    <div class="content-wrap">
        <div class="bottom">
            <img src="${contextPath}/assets/imgs/pay-wait.png" class="img-10 justify">
            <div class="pd-20p">
                <p class="intro mt-10">待支付</p>
            </div>
        </div>
        <div class="order-detail">
            <p>订单编号：${yqxOrderRecord.serialNum!}</p>
            <input type="hidden" id="serialNum" value="${yqxOrderRecord.serialNum!}">
            <p>购买产品：${productName!}</p>
            <p>支付金额：￥${(yqxOrderRecord.payPrice/100)?string("##.##")}</p>
            <p>创建时间：${(yqxOrderRecord.createTime?datetime)!}</p>
            <p>支付时间：</p>
            <p>流量充值：</p>
        </div>
    </div>
</div>
<div class="pd-10p mt-30 mb-20">
    <a class="btn btn-lg btn-confirm" id="btn-submit">确认支付</a>
    <div id="error-tip" class="text-center"></div>
</div>
<!-- loading -->
<div id="loadingToast" class="weui_loading_toast">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <div class="weui_loading">
            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div><!-- loading end -->
<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script>
    $('#btn-submit').on('click', function () {
        window.location.href = "orderConfirm.html?serialNum=" + $("#serialNum").val();//跳转到支付页面
    });


    function showToast() {
        $('#loadingToast').modal('show');
    }

    function hideToast() {
        window.setTimeout(function () {
            $('#loadingToast').modal('hide');
        }, 300);
    }
</script>
</body>
</html>