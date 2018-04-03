<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-异常订单</title>
    <meta name="keywords" content="流量平台 异常订单"/>
    <meta name="description" content="流量平台 异常订单"/>

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

        .tile{
            margin-top: -1px;
            box-shadow: 0px 0px 1px rgba(0,0,0,.2);
        }

        .date-picker-wrapper .month-wrapper table {
            width: 188px;
        }

        .date-picker-wrapper .month-wrapper table.month2 {
            width: 188px;
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
        <h3>异常订单</h3>
    </div>
    <div class="mt-30">
        <ul class="nav nav-tabs pull-left" role="tablist">
            <li role="presentation" class="active"><a href="#orderList" role="tab" data-toggle="tab">退款申请</a></li>
            <li role="presentation"><a href="#refundList" role="tab" data-toggle="tab">重复支付</a></li>
        </ul>

        <div class="form-inline text-right">
            <span>订单创建时间</span>
            <div class="form-group form-group-sm form-inline ml-5" style="position: relative;">
                <span id="search-time-range" class="date-range-wrap valign-middle">
                    <input class="form-control searchItem" name="searchTime" id="searchTime"
                           style="width: 21rem;">
                </span>
            </div>
            <span class="ml-10">订单号</span>
            <div class="form-group form-group-sm form-inline ml-5" style="position: relative;">
                <input class="form-control searchItem" name="orderSerialNum" id="orderNo">
            </div>
            <span class="ml-10">退款状态</span>
            <div class="btn-group btn-group-sm ml-5">
                <input name="refundStatus" class="searchItem" id="status"
                       style="width: 0; height: 0;padding: 0; opacity: 0;" value="1,2,3,4">
                <button type="button" class="btn btn-default">全部</button>
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="caret"></span>
                    <span class="sr-only">Toggle Dropdown</span>
                </button>
                <ul class="dropdown-menu">
                    <li data-value="1,2,3,4"><a href="#">全部</a></li>
                    <li data-value="1"><a href="#">受理中</a></li>
                    <li data-value="2"><a href="#">退款中</a></li>
                    <li data-value="3"><a href="#">退款成功</a></li>
                    <li data-value="4"><a href="#">退款失败</a></li>
                </ul>
            </div>
            <a class="btn btn-sm btn-primary btn-width ml-10" id="search-btn">确定</a>
            <a class="btn btn-sm btn-warning btn-width ml-10" onclick="createFile()">报表导出</a>
        </div>

        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="orderList">
                <div class="tile gray-border tile-noTopBorder">
                    <div id="listData1" class="mt-10 table-wrap text-center"></div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" id="refundList">

                <div class="tile gray-border tile-noTopBorder">
                    <div id="listData2" class="mt-10 table-wrap text-center"></div>
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
    var refundFormat = function (value, column, row) {
        if (value == 1) {
            return "受理中";
        }
        if (value == 2) {
            return "退款中";
        }
        if (value == 3) {
            return "退款成功";
        }
        if (value == 4) {
            return "退款失败";
        }
        return "";
    };
    
    var sizeFormat = function (value, column, row) {
        if (value <1024) {
            return value + "K";
        }
        if (value >=1024 && value <1024*1024) {
            return value/1024.0 + "M";
        }
        if (value >=1024*1024) {
            return value/1024.0/1024.0 + "G";
        }
        return "";
    };

    var statusFormat = function (value, column, row) {
        if (value == null){
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
            return '充值失败&nbsp;&nbsp;<a onclick="showTip(this)" data-msg="' + row.chargeMsg + '">查看失败原因</a>';
        }
        return "";
    };
    
    var action1 = "${contextPath}/manage/yqx/order/searchRefundlist.html?${_csrf.parameterName}=${_csrf.token}";
    var columns1 = [
        {name: "serialNum", text: "订单号", tip: true},
        {name: "createTime", text: "订购创建时间", format: "DateTimeFormat"},
        {name: "mobile", text: "手机号码"},        
        {name: "productSize", text: "购买产品", format: sizeFormat},
        {name: "doneCode", text: "支付宝流水号"},
        {name: "chargeStatus", text: "充值状态", format: statusFormat},
        {name: "refundApprovalTime", text: "申请时间", format: "DateTimeFormat"},
        {name: "refundStatus", text: "退款状态", format: refundFormat}];

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
    
    function showTip(ele) {
        showTipDialog($(ele).data("msg"));
    }
  
    var action2 = "${contextPath}/manage/yqx/order/searchRepeatPay.html?${_csrf.parameterName}=${_csrf.token}";

    var columns2 = [
        {name: "orderSerialNum", text: "订单号", tip: true},
        {name: "doneCode", text: "支付流水号"},
        {name: "resultReturnTime", text: "支付完成时间", format: "DateTimeFormat"},
        {name: "status", text: "支付状态", format: payFormat},
        {name: "chargeTime", text: "充值时间", format: "DateTimeFormat"},
        {name: "chargeStatus", text: "充值状态", format: statusFormat}];

    require(["react", "react-dom", "page/listDate", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData) {
        initSearchDateRangePicker('#search-time-range', '#searchTime');

        ReactDOM.render(React.createElement(ListData, {
            columns: columns1,
            searchClass: "searchItem",
            searchBtn: $("#search-btn")[0],
            action: action1
        }), $("#listData1")[0]);

        ReactDOM.render(React.createElement(ListData, {
            columns: columns2,
            searchClass: "searchItem",
            searchBtn: $("#search-btn")[0],
            action: action2
        }), $("#listData2")[0]);

        listeners();
    });

    function listeners() {

    }

    function initSearchDateRangePicker(searchTimeRange, searchTime) {
        var ele = $(searchTimeRange);

        var timeEle = $(searchTime);

        ele.dateRangePicker({
            format: 'YYYY-MM-DD HH:mm',
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
            time: {
                enabled: true
            },
            endDate: new Date(),
            customShortcuts: [
                {
                    name: '半年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 182);
                        return [start, end];
                    }
                },
                {
                    name: '一年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 360);
                        return [start, end];
                    }
                }
            ],
            getValue: function () {
                if (timeEle.val())
                    return timeEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                timeEle.val(s);
            }
        });
    }

    function createFile() {
        var searchTime = $("#searchTime").val();
        var orderNo = $("#orderNo").val();
        var status = $("#status").val();

        var target = $(".nav-tabs>li.active>a").attr("href");
        if(target == "#orderList"){
            location.href = "${contextPath}/manage/yqx/order/downloadRefundList.html?searchTime="+searchTime+
                    "&orderSerialNum="+orderNo+"&refundStatus="+status;
        }else{
            location.href = "${contextPath}/manage/yqx/order/downloadRepeatPayList.html?searchTime="+searchTime+
                    "&orderSerialNum="+orderNo;
        }
    }
</script>
</body>
</html>