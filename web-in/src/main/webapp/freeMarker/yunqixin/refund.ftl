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
            <img src="${contextPath}/assets/imgs/pay-success.png" class="img-10 justify">
            <div class="pd-20p">
                <p class="intro mt-10">支付成功</p>
            </div>
        </div>
        <div class="order-detail">
            <p>订单编号：${yqxOrderRecord.serialNum!}</p>
            <p>购买产品：${productName!}</p>
            <p>支付金额：￥${(yqxOrderRecord.payPrice/100.0)?string("##.##")}</p>
            <p>创建时间：${(yqxOrderRecord.createTime?datetime)!}</p>
            <p>支付时间：${(yqxPayRecord.createTime?datetime)!}</p>
            <p>流量充值：充值失败</p>
            <input type="hidden" id="serialNum" value="${yqxOrderRecord.serialNum!}">
        </div>
    </div>
</div>
<div class="pd-10p mt-30 mb-20">
    <a class="btn btn-lg btn-confirm" id="btn-refund">申请退款</a>
    <div id="error-tip" class="text-center"></div>
</div>
<div id="success-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">退款申请已成功受理！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a id="ok" style=" display: block; " data-dismiss="modal">确 定</a></p>
            </div>
        </div>
    </div>
</div>
<div id="fail-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">退款申请受理失败！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style=" display: block; " data-dismiss="modal">再试一次</a></p>
            </div>
        </div>
    </div>
</div>
<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script>
    
    $('#btn-refund').on('click', function () {
        var serialNum = $("#serialNum").val();
        $.ajax({
            url: "${contextPath}/yqx/order/submitRefund.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            data: {serialNum: serialNum},
            beforeSend: function () {
                showToast();
            },
            success: function (data) {
                hideToast();
                if (data.success) {                    
                    $("#success-dialog").modal('show');
                }else{
                    $("#fail-dialog").modal('show');
                }
            },
            error: function () {
                $("#fail-dialog").modal('show');
            },
            complete: function () {
                hideToast();
            }
        });
    });

    $("#ok").on("click",function(){
		var serialNum = $("#serialNum").val();
        window.location.href = "showDetail.html?serialNum=" + serialNum;//跳转到订购详情页
    });

</script>
</body>
</html>