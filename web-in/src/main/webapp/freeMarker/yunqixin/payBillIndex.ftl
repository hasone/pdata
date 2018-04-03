<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-支付对账</title>
    <meta name="keywords" content="流量平台 账单记录"/>
    <meta name="description" content="流量平台 账单记录"/>

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
            margin-top: -1px;
            box-shadow: 0px 0px 1px rgba(0, 0, 0, .2);
        }
        
        .btn-icon {
            padding-left: 0px;
            background-repeat: no-repeat;
            background-position: 6px center;
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
        <h3>支付对账</h3>
    </div>
    <div class="mt-30">
        <ul class="nav nav-tabs pull-left" role="tablist">
            <li role="presentation" class="active"><a href="#orderList" role="tab" data-toggle="tab">账单记录</a></li>
            <!--<li role="presentation"><a href="#refundList" role="tab" data-toggle="tab">退款对账</a></li>-->
        </ul>

        <div class="form-inline text-right">
            <span>订单创建时间</span>
            <div class="form-group form-group-sm form-inline ml-5" style="position: relative;">
                <span id="search-time-range" class="date-range-wrap valign-middle">
                    <input class="form-control searchItem" name="searchTime" id="searchTime"
                           style="width: 21rem;">
                </span>
            </div>
            <span id="orderNum">
                <span class="ml-10">订单号</span>
                <div class="form-group form-group-sm form-inline ml-5" style="position: relative;">
                    <input class="form-control searchItem" name="orderSerialNum" id="orderSerialNum">
                </div>
            </span>
            <span class="ml-10">手机号码</span>
            <div class="form-group form-group-sm form-inline ml-5" style="position: relative;">
                <input class="form-control searchItem" name="mobile" id="mobile">
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

    var moneyFormator = function (value, column, row) {
        if(value == null || value==""){
            return "";
        }
        value = value / 100.0;
        return value.toFixed(2) + "元";          
    }
    
    var dateTimeFormator2 = function (value, column, row) {
        if(value == null  || value.length != 14){
            return value;
        }
        
        return value.substring(0,4) + "-" + value.substring(4,6)+"-" +value.substring(6,8) +" " +value.substring(8,10) +":"
        +value.substring(10,12)+":"+value.substring(12,14);
    }

    var action1 = "${contextPath}/manage/yqx/order/payBillSearch.html?${_csrf.parameterName}=${_csrf.token}";
    var columns1 = [
        {name: "orderSerialNum", text: "订单号", tip:true},
        {name: "payCreateTime", text: "订购创建时间", format: "DateTimeFormat"},
        {name: "doneCode", text: "第三方支付流水号", tip:true},
        {name: "price", text: "支付金额", format: moneyFormator},
        {name: "payTime", text: "支付时间",format: dateTimeFormator2},
        {name: "mobile", text: "手机号码"},
        {name: "prdName", text: "购买产品"},
        
    ];


    require(["react", "react-dom", "page/listDate", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData) {
        initSearchDateRangePicker('#search-time-range', '#searchTime');
    
        ReactDOM.render(React.createElement(ListData, {
            columns: columns1,
            searchClass: "searchItem",
            searchBtn: $("#search-btn")[0],
            action: action1
        }), $("#listData1")[0]);

        listeners();
    });

    function listeners() {
        $('a[data-toggle="tab"]').on("click", function(){
            var target = $(this).attr("href");
            if(target == "#orderList") {
                $("#refundSerialCode").hide();
                $("#orderNum").show();
            }else{
                $("#orderNum").hide();
                $("#refundSerialCode").show();
            }
        });
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
        var orderSerialNum = $("#orderSerialNum").val();
        var mobile = $("#mobile").val();
        var reconcileStatus = $("#reconcileStatus").val();
        var searchTime = $("#searchTime").val();
        var target = $(".nav-tabs>li.active>a").attr("href");
        if (target == "#orderList") {
            location.href = "${contextPath}/manage/yqx/order/downloadBillData.html?orderSerialNum=" + orderSerialNum +
                    "&mobile=" + mobile + "&searchTime="+searchTime;
        }
    }
</script>
</body>
</html>