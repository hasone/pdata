<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-退款详情</title>
    <meta name="keywords" content="流量平台 退款详情"/>
    <meta name="description" content="流量平台 退款详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <style>
        .nav-tabs > li > a {
            border: 1px solid #ddd;
            background-color: #ddd;
            color: #787878;
            padding: 10px 25px;
        }

        .nav > li > a:focus, .nav > li > a:hover {
            background-color: #ddd;
            color: #787878;
        }

        .tile {
            box-shadow: 0px 0px 1px rgba(0, 0, 0, .2);
        }
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>退款详情
         <a class="btn btn-primary btn-sm btn-icon icon-back pull-right" onclick="history.go(-1)" >返回</a>
        </h3>
    </div>
    <div class="mt-30">
        <div class="tile gray-border tile-noTopBorder">
            <div class="mt-10 tile-content">
                <div class="row">
                    <div class="col-xs-6">
                        <div class="row">
                            <label class="col-xs-3 text-right">订单号</label>
                            <span class="col-xs-9">${orderRecord.serialNum!}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">订购创建时间</label>
                            <span class="col-xs-9">${orderRecord.createTime?datetime}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">订单状态</label>
                            <span class="col-xs-9">
                            <#if orderRecord.tradeStatus == 0>
                            交易中
                            </#if>
                            <#if orderRecord.tradeStatus == 1>
                            交易关闭
                            </#if>
                            <#if orderRecord.tradeStatus == 2>
                            交易成功
                            </#if>
                            <#if orderRecord.tradeStatus == 3>
                            交易失败
                            </#if>
                            </span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">手机号码</label>
                            <span class="col-xs-9">${orderRecord.mobile}</span>
                        </div>                        
                        <#if orderRecord.vpmnTime??>
                            <div class="row mt-10">
                                <label class="col-xs-3 text-right">V网网龄</label>
                                <span class="col-xs-9">${orderRecord.vpmnTime?datetime}</span>
                            </div>
                        </#if>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">购买产品</label>
                            <span class="col-xs-9">${productName!}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">支付金额</label>
                            <span class="col-xs-9">￥${(orderRecord.payPrice/100.0)?string("#0.00")}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">支付流水号</label>
                            <span class="col-xs-9">${refundRecord.doneCode!}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">支付状态</label>
                            <span class="col-xs-9">
                            <#if payRecord.status??&&payRecord.status == 0>
                            成功
                            </#if>
                            <#if payRecord.status??&&payRecord.status == 1>
                            失败
                            </#if>
                            <#if payRecord.status??&&payRecord.status == 2>
                            等待支付(支付平台返回)
                            </#if>
                            <#if payRecord.status??&&payRecord.status == 3>
                            等待支付平台返回
                            </#if>
                            </span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">支付时间</label>
                            <span class="col-xs-9">${payRecord.createTime?datetime}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">充值时间</label>
                            <#if payRecord?? && payRecord.chargeTime??>
                            <span class="col-xs-9">${payRecord.chargeTime?datetime}</span>
                            </#if>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">充值状态</label>
                            <span class="col-xs-9">
                            <#if !payRecord?? || !payRecord.chargeStatus??>
                            未充值
                            </#if>
                            <#if payRecord?? && payRecord.chargeStatus??&&payRecord.chargeStatus == 1>
                            待充值
                            </#if>
                            <#if payRecord??&&payRecord.chargeStatus??&&payRecord.chargeStatus == 2>
                            已发送充值请求
                            </#if>
                            <#if payRecord??&&payRecord.chargeStatus??&&payRecord.chargeStatus == 3>
                            充值成功
                            </#if>
                            <#if payRecord??&&payRecord.chargeStatus??&&payRecord.chargeStatus == 4>
                            充值失败
                            </#if>
                            </span>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="row">
                            <label class="col-xs-3 text-right">退款流水号</label>
                            <span class="col-xs-9">${refundRecord.refundSerialNum}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">退款发起时间</label>
                            <span class="col-xs-9">${refundRecord.createTime?datetime}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">退款金额</label>
                            <span class="col-xs-9">￥${(orderRecord.payPrice/100.0)?string("#0.00")}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">退款状态</label>
                            <span class="col-xs-9">
                            <#if refundRecord.status == 0>
                            待发送
                            </#if>
                            <#if refundRecord.status == 1>
                            受理成功
                            </#if>
                            <#if refundRecord.status == 2>
                            退款失败&nbsp;&nbsp;<a onclick="showTip(this)" data-msg="${refundRecord.msg!}">查看失败原因</a>
                            </#if>
                            <#if refundRecord.status == 3>
                            退款成功
                            </#if>
                            <#if refundRecord.status == 4>
                            退款失败&nbsp;&nbsp;<a onclick="showTip(this)" data-msg="${refundRecord.msg!}">查看失败原因</a>
                            </#if>
                            </span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">退款返回时间</label>
                            <#if refundRecord.resultReturnTime??>
                            <span class="col-xs-9">${refundRecord.resultReturnTime?datetime}</span>
                            </#if>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">处理原因</label>
                            <span class="col-xs-9">${refundRecord.reason!}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">处理人姓名</label>
                            <span class="col-xs-9">${refundRecord.operatorName!}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-3 text-right">处理人手机号码</label>
                            <span class="col-xs-9">${refundRecord.operatorMobile!}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--错误提示弹窗-->
<div class="modal fade dialog-sm" id="tip-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
function showTip(ele) {
    showTipDialog($(ele).data("msg"));
}
require(["common", "bootstrap"], function () {

});



</script>
</body>
</html>