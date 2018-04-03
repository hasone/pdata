<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>订单支付</title>
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    
    <style>
        .footer-color a, .footer-color a:active, .footer-color a:focus, .footer-color a:hover {
            color: #000;
            font-size: 14px;
        }
    </style>
</head>
<body>
<#--<div class="module_top">-->
    <#--<div class="header">-->
        <#--<span>订单支付</span>-->
    <#--</div>-->
<#--</div>-->

<div class="content">
    <div class="item">
        <div class="col-xs-8">
            <p>商品名称</p>
        </div>
        <div class="col-xs-4 text-right all_price">${prize.prizeName!}</div>
    </div>
    <div class="item">
        <div class="col-xs-8">
            <p>数量</p>
        </div>
        <div class="col-xs-4 text-right all_price">1</div>
        <!--<div class="col-xs-4 count" style="padding-right: 13px;">-->
            <!--<div class="col-xs-3 text-center plus">+</div>-->
            <!--<input type="text" value="1" class="col-xs-5 text-center" style="float: right;" readOnly="true">-->
            <!--<div class="col-xs-3 text-center minus">-</div>-->
        <!--</div>-->
    </div>
    <div class="item">
        <div class="col-xs-7">
            <p>付款方式</p>
        </div>
        <div class="col-xs-5 text-right all_price">微信支付&nbsp;&nbsp;></div>
    </div>
    <div class="price text-center">￥${price!}</div>

        <div class="btn-pay text-center" id="pay">确认支付</div>

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

<div id="tip-dialog-repeat" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-failure">
            <div class="modal-body">
                <img src="${contextPath}/assets/imgs/sorry.png" alt=""/>
                <p class="body-font">无法重复支付</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a href="${contextPath}/wx/zhongchou/list.html" 
                    style="display: block;">确 定</a>
                </p>
            </div>
        </div>
    </div>
</div>
<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>

<script>
    $("#pay").on("click", function(){
        $.ajax({
            url: "${contextPath}/wx/zhongchou/submitPay.html?${_csrf.parameterName}=${_csrf.token}&date="+ new Date(),
            data: {
                activityWinRecordId: "${activityWinRecordId!}",
            },
            beforeSend: function () {
                showToast();
            },
            type: "post",
            dataType: "json",
            success: function (ret) {
                if (ret) {                    
                    if (!ret.repeat) {
                        window.location.href = "${contextPath}/wx/zhongchou/callForPay.html?activityWinRecordId=" + "${activityWinRecordId!}";
                    }
                    else {
                        hideToast();
                        $("#tip-dialog-repeat").modal("show");
                    }
                }
            }
        });
        
    });
    
    /**
     * 显示
     */
    function showToast() {
        $("#loadingToast").show();
    }

    /**
     * 隐藏
     */
    function hideToast() {
        $("#loadingToast").hide();
    }
</script>
</body>
</html>