<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-订单详情</title>
    <meta name="keywords" content="流量平台 订单详情"/>
    <meta name="description" content="流量平台 订单详情"/>

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
            margin-top: -26px;
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
        <h3>订单详情
        <a class="btn btn-primary btn-sm btn-icon icon-back pull-right" onclick="history.go(-1)" >返回</a>
        </h3>
    </div>
    <div class="mt-30">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#orderDetail" role="tab" data-toggle="tab">订单详情</a></li>
            <li role="presentation"><a href="#payRecord" role="tab" data-toggle="tab">支付记录</a></li>
        </ul>
        <input type="hidden" name="orderSerialNum" class="form-control searchItem" value="${record.serialNum!}">
        <a class="btn btn-sm btn-primary btn-width ml-10" id="search-btn"
           style="width: 0;height: 0; padding: 0;opacity: 0;"></a>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="orderDetail">
                <div class="tile gray-border tile-noTopBorder">
                    <div class="mt-10 tile-content">
                        <div class="row">
                            <label class="col-xs-2 text-right">订单号</label>
                            <span class="col-xs-10">${record.serialNum!}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-2 text-right">订购创建时间</label>
                            <span class="col-xs-10">${record.createTime?datetime}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-2 text-right">订单状态</label>
                            <#if record.tradeStatus == 0>
                            <span class="col-xs-10">交易中</span>
                            </#if>
                            <#if record.tradeStatus == 1>
                            <span class="col-xs-10">交易关闭</span>
                            </#if>
                            <#if record.tradeStatus == 2>
                            <span class="col-xs-10">交易成功</span>
                            </#if>
                            <#if record.tradeStatus == 3>
                            <span class="col-xs-10">交易失败</span>
                            </#if>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-2 text-right">手机号码</label>
                            <span class="col-xs-10">${record.mobile!}</span>
                        </div>
                        <#if record.vpmnTime??>
                            <div class="row mt-10">
                                <label class="col-xs-2 text-right">V网网龄</label>
                                <span class="col-xs-10">${record.vpmnTime?datetime}</span>
                            </div>
                        </#if>
                        <div class="row mt-10">
                            <label class="col-xs-2 text-right">购买产品</label>
                            <span class="col-xs-10">${productName!}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-2 text-right">支付金额</label>
                            <span class="col-xs-10">￥${(record.payPrice/100.0)?string("#0.00")}</span>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-2 text-right">支付状态</label>
                            <#if record.payStatus??&&record.payStatus == 0>
                            <span class="col-xs-10">待支付</span>
                            </#if>
                            <#if record.payStatus??&&record.payStatus == 1>
                            <span class="col-xs-10">未支付</span>
                            </#if>
                            <#if record.payStatus??&&record.payStatus == 2>
                            <span class="col-xs-10">支付成功</span>
                            </#if>
                            <#if record.payStatus??&&record.payStatus == 3>
                            <span class="col-xs-10">支付失败</span>
                            </#if>
                        </div>
                        <div class="row mt-10">
                            <label class="col-xs-2 text-right">充值状态</label>
                            <#if !record.chargeStatus??>
                            <span class="col-xs-10">未充值</span>
                            </#if>
                            <#if record.chargeStatus??&&record.chargeStatus == 1>
                            <span class="col-xs-10">待充值</span>
                            </#if>
                            <#if record.chargeStatus??&&record.chargeStatus == 2>
                            <span class="col-xs-10">已发送充值请求</span>
                            </#if>
                            <#if record.chargeStatus??&&record.chargeStatus == 3>
                            <span class="col-xs-10">充值成功</span>
                            </#if>
                            <#if record.chargeStatus??&&record.chargeStatus == 4>
                            <span class="col-xs-10">充值失败</span>
                            </#if>
                        </div>                      
                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" id="payRecord">
                <div class="tile gray-border tile-noTopBorder">
                    <div id="listData2" class="mt-10 table-wrap text-center">
                        <div class="table-responsive">
                            <div role="table"></div>
                            <div role="pagination" hidden></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="sync-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">是否进行成功?</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sync-ok-btn">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
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

    var statusFormat = function (value, column, row) {
        if (value == null) {
            return "未充值";
        }
        if (value == 1) {
            return "待充值";
        }
        if (value == 2) {
            return "已发送充值请求";
        }
        if (value == 3) {
            return "充值成功";
        }
        if (value == 4) {
            var errMsg = row.chargeMsg;
            if(errMsg==null){
                errMsg = "无该条充值记录";
            }
            return '充值失败&nbsp;&nbsp;<a onclick="showTip(this)" data-msg="' + errMsg + '">查看失败原因</a>';
        }
        return "";
    };

    var payFormat = function (value, column, row) {
        if (value == 0) {
            return "支付成功";
        }
        if (value == 1) {
            return "支付失败";
        }
        if (value == 2) {
            return "等待支付(支付平台返回)";
        }
        if (value == 3) {
            return "等待支付平台返回";
        }
        return "";
    };
    
    var priceFormat = function (value, column, row) {        
        return "￥" + value/100.0;
    };
    
    var opFormat = function (value, column, row) {
        if (value) {
            return "<a href='${contextPath}/manage/yqx/order/orderService.html?doneCode=" + row.doneCode + "'>售后处理</a>";
        }
        return "";
    };

    var action = "${contextPath}/manage/yqx/order/searchPayRecord.html?${_csrf.parameterName}=${_csrf.token}";

    var columns = [
        {name: "orderSerialNum", text: "订单号"},
        {name: "doneCode", text: "支付流水号"},
        {name: "resultReturnTime", text: "支付完成时间", format: "DateTimeFormat"},
        {name: "status", text: "支付状态", format: payFormat},
        {name: "payPrice", text: "支付金额", format: priceFormat},
        {name: "chargeTime", text: "充值时间", format: "DateTimeFormat"},
        {name: "chargeStatus", text: "充值状态", format: statusFormat},
        {name: "canRefund", text: "操作", format: opFormat}];

    require(["react", "react-dom", "page/list", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData) {
        listeners();
    });

    function listeners() {
        $("#search-btn").click();
    }
</script>
</body>
</html>