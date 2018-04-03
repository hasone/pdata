<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-订单管理</title>
    <meta name="keywords" content="流量平台 订单管理"/>
    <meta name="description" content="流量平台 订单管理"/>

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
        <h3>订单管理</h3>
    </div>
    <div class="mt-30">
        <ul class="nav nav-tabs pull-left" role="tablist">
            <li role="presentation" class="active"><a href="#orderList" role="tab" data-toggle="tab">订单列表</a></li>
            <li role="presentation"><a href="#refundList" role="tab" data-toggle="tab">退款列表</a></li>
        </ul>

        <div class="form-inline text-right">
            <span>订单创建时间</span>
            <div class="form-group form-group-sm form-inline ml-5" style="position: relative;">
                <span id="search-time-range" class="date-range-wrap valign-middle">
                    <input class="form-control searchItem" name="searchTime" id="searchTime"
                           style="width: 21rem;">
                </span>
            </div>
            <span class="ml-10">手机号码</span>
            <div class="form-group form-group-sm form-inline ml-5" style="position: relative;">
                <input class="form-control searchItem" name="mobile" id="telephone">
            </div>
            <span class="ml-10">订单号</span>
            <div class="form-group form-group-sm form-inline ml-5" style="position: relative;">
                <input class="form-control searchItem" name="orderSerialNum" id="orderNo">
            </div>
            <a class="btn btn-sm btn-primary btn-width ml-10" id="search-btn">确定</a>
            <#--<a class="btn btn-sm btn-warning btn-width ml-10" onclick="createFile()">报表导出</a>-->
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

    //0.待发送 1.受理成功 2.受理失败 3.退款成功 4.退款失败
    var refundFormat = function (value, column, row) {
        if (value == 0) {
            return "待发送";
        }
        if (value == 1) {
            return "受理成功";
        }
        if (value == 2) {
            return '退款失败&nbsp;&nbsp;<a onclick="showTip(this)" data-msg="' + row.msg + '">查看失败原因</a>';
        }
        if (value == 3) {
            return "退款成功";
        }
        if (value == 4) {
            return '退款失败&nbsp;&nbsp;<a onclick="showTip(this)" data-msg="' + row.msg + '">查看失败原因</a>';
        }
        return "";
    };

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
            return '充值失败&nbsp;&nbsp;<a onclick="showTip(this)" data-msg="' + row.chargeMsg + '">查看失败原因</a>';
        }
        return "";
    };

    var payFormat = function (value, column, row) {
        if (value == 1) {
            return "支付成功";
        }
        return "";
    };

    var action1 = "${contextPath}/manage/yqx/order/searchOrderlist.html?${_csrf.parameterName}=${_csrf.token}";
    var columns1 = [
         {name: "serialNum", text: "订单号", tip:true},
         {name: "createTime", text: "订购创建时间", format: "DateTimeFormat"},
         {name: "mobile", text: "手机号码"},
         {name: "chargeStatus", text: "充值状态", format: statusFormat},
         {name: "", text: "操作", format: function(value, column, row){
            return "<a href='${contextPath}/manage/yqx/order/showOrderDetail.html?orderSerialNum="+row.serialNum+"'>详情</a>"
         }}
     ];
     
    var action2 = "${contextPath}/manage/yqx/order/searchRefundRecord.html?${_csrf.parameterName}=${_csrf.token}";
    var columns2 = [
        {name: "orderSerialNum", text: "订单号", tip:true},
        {name: "orderCreateTime", text: "订购创建时间", format: "DateTimeFormat"},
        {name: "refundSerialNum", text: "退款流水号", tip:true},
        {name: "mobile", text: "手机号码"},
        {name: "status", text: "退款状态", format: refundFormat},
        {name: "", text: "详情", format: function (value, column, row) {
            return "<a href='${contextPath}/manage/yqx/order/showRefundRecordDetail.html?refundSerialNum="+row.refundSerialNum+"'>查看</a>"
        }}];

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
            location.href = "${contextPath}/manage/yqx/order/downloadOrderList.html?searchTime="+searchTime+
                    "&orderSerialNum="+orderNo+"&mobile="+mobile;
        }else{
            location.href = "${contextPath}/manage/yqx/order/downloadOrderList.html?searchTime="+searchTime+
                    "&orderNo="+orderNo;
        }
    }
</script>
</body>
</html>