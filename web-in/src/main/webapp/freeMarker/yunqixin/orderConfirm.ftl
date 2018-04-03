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
    <title>订单确认</title>
    <style>
        body {
            overflow: auto;
        }
    </style>
</head>
<body>
<div class="content-wrap" style="border: 0;">
    <div class="content-item">
        <div class="p-title">订单确认</div>
        <div class="p-content">
            <div class="p-item">
                <span class="col-xs-6 font-ml-gray">订购充值号码</span>
                <span class="col-xs-6 text-right">${yqxOrderRecord.mobile!}</span>
            </div>
            <#if vpmn>
                <div class="p-item">
                    <span class="col-xs-6 font-ml-gray">四川移动V网网龄</span>
                    <span class="col-xs-6 text-right">${vpmnYear!}</span>
                </div>
            </#if>
            <div class="p-item">
                <span class="col-xs-6 font-ml-gray">购买产品</span>
                <span class="col-xs-6 text-right">${productName!}</span>
            </div>
            <div class="p-item">
                <span class="col-xs-6 font-ml-gray">产品价格</span>
                <span class="col-xs-6 text-right">￥${productPrice!}</span>
            </div>
            <div class="p-item">
                <span class="col-xs-6 font-ml-gray">实付金额</span>
                <span class="col-xs-6 text-right">￥${(yqxOrderRecord.payPrice/100.0)?string("##.##")}</span>
            </div>
            <input type="hidden" id="serialNum" value="${yqxOrderRecord.serialNum!}">
        </div>
    </div>
</div>
<div class="content-wrap mt-10">
    <div class="content-item" style="border: 0;">
        <div class="p-title">选择支付方式</div>
        <div class="pay-group">
            
            <#--<div class="pay-item">
                <span class="col-xs-6"><img src="${contextPath}/assets/imgs/wxpay.png" style="margin-right: 12px;">微信支付</span>
                    <span class="col-xs-6 text-right">
                        <input type="radio" name="pay-item" id="radio_1" value="1">
                        <label class="radio-item" for="radio_1"></label>
                    </span>
            </div>-->
            <div class="pay-item">
                <span class="col-xs-6"><img src="${contextPath}/assets/imgs/alipay.png">支付宝支付</span>
                    <span class="col-xs-6 text-right">
                        <input type="radio" name="pay-item" id="radio_2" value="2">
                        <label class="radio-item" for="radio_2"></label>
                    </span>
            </div>
        </div>
    </div>
</div>
<#--<div class="mt-10 ml-10 font-xs-gray">
    <img src="${contextPath}/assets/imgs/icon-info.png" class="img-info">
    请尽快完成支付。5分钟后逾期交易关闭。
</div> -->

<div class="pd-10p mt-30">
    <a class="btn btn-lg btn-confirm create-disabled" id="btn-submit" data-toggle="modal">确认支付</a>
</div>


<div id="busy-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-busy hide">
            <div class="modal-body">
                <p class="body-font">网络繁忙！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style=" display: block; " data-dismiss="modal">再试一次</a></p>
            </div>
        </div>
    </div>
</div>

<div id="fail-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <p class="body-font">非常抱歉，目前暂不支持订购！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal">确 定</a></p>
            </div>
        </div>
    </div>
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

    if ($("input[name='pay-item']:checked").val()) {
        $('#btn-submit').removeClass('create-disabled');
    }

    $('.radio-item').on('click', function () {
        $('#btn-submit').removeClass('create-disabled');
    });


    $('#btn-submit').on('click', function () {
        var payType = $("input[name='pay-item']:checked").val();
        var serialNum = $("#serialNum").val();
        showToast();
        $.ajax({
            url: "${contextPath}/yqx/order/checkPay.html?${_csrf.parameterName}=${_csrf.token}",
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                hideToast();
                if (data.success) {
                    window.location.href = "${contextPath}/yqx/order/submitPay.html?payType="+payType+"&serialNum="+serialNum;                    
                }else{
                    $("#fail-dialog").modal('show');
                }
            },
            error: function () {
                $("#busy-dialog").modal('show');
            }
        });
        
        
        
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