<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量币购买</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets_individual/theme/default/theme.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/main.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets_individual/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets_individual/lib/respond.min.js"></script>
    <![endif]-->
    <style>
        .form-group label {
            width: 80px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mb-20">
        <h3>业务办理-流量币购买</h3>
    </div>

    <div class="tile gray-border mt-30">
        <form class="form" id="dataForm"
              action="${contextPath}/individual/flowcoin/savePurchase.html?${_csrf.parameterName}=${_csrf.token}">
            <input type="hidden" name="payFlag" id="payFlag" value="false">
            <div class="form-group form-group-sm form-inline">
                <label>流量币余额:</label>
                <span class="green-text-lg">10000</span>&nbsp;&nbsp;个
            </div>
            <div class="form-group form-group-sm form-inline">
                <label>购买数量:</label>
                <input class="form-control mobileOnly" maxlength="6" name="coins" id="coins"
                       placeholder="支持1-100000的正整数" onblur="getMoney()"> 个流量币
            </div>
            <div class="form-group form-group-sm form-inline">
                <label>支付金额:</label>
                <span class="red-text-lg" id="money"></span>
            </div>

            <div class="form-group form-group-sm form-inline">
                <label>支付方式:</label>
                    <span class="radio-wrap">
                        <input type="radio" name="type" id="type_radio1" checked>
                        <label class="radio-input" for="type_radio1"></label>
                        <span class="blue-border ml-10">
                            <i class="menu-icon fa iconfont icon-zhifu"></i>
                            <span>话费支付</span>
                        </span>
                    </span>
            </div>
        </form>
        <div class="tile tile-sm orange-tile orange-border mt-30">
            <p>流量币购买须知：</p>
            <p>1、流量币单价是多少？</p>
            <p>答：0.1元可购买1个流量币。</p>
            <p>2、流量币是否具有有限期？</p>
            <p>答：您购买的流量币将在购买时间的次年年底失效，通过赠送、红包、营销活动等形式获得的流量币将在获赠时间的次月月底失。</p>
        </div>
    </div>
    <div class="text-center mt-20">
        <a class="btn btn-success btn-sm btn-width" id="save-btn">保存</a>
        <a class="btn btn-primary btn-sm btn-width ml-20" id="pay-btn">支付</a>
        <span style="color:red" id="error_msg">${errorMsg!}</span>
    </div>

    <div class="mt-20">
        <label style="float:left">历史订单</label> 
        <div class="form-inline text-right">
            <span>创建日期:</span>
            <div class="form-group form-group-sm form-inline" style="position: relative;">
                <span id="search-time-range" class="date-range-wrap">
                    <input name="startDate" id="startDate" class="form-control searchItem">-
                    <input name="endDate" id="endDate" class="form-control searchItem">
                </span>
            </div>
            <span class="ml-20">状态:</span>
            <div class="btn-group btn-group-sm">
                <input name="status" class="searchItem" style="width: 0; height: 0;padding: 0; opacity: 0;">
                <button type="button" class="btn btn-default">请选择</button>
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                    <span class="caret"></span>
                    <span class="sr-only">Toggle Dropdown</span>
                </button>
                <ul class="dropdown-menu">
                    <li data-value=""><a href="#">全部</a></li>
                    <li data-value="0,3"><a href="#">待支付</a></li>
                    <li data-value="1"><a href="#">支付中</a></li>
                    <li data-value="2"><a href="#">支付成功</a></li>
                    <li data-value="4"><a href="#">已取消</a></li>
                </ul>
            </div>
            <a class="btn btn-sm btn-default btn-width ml-20" id="search-btn">查询</a>
        </div>
        <div id="table_wrap" class="mt-10 table-wrap text-center"></div>
    </div>

    <div id="confirmBox"></div>
</div>
<!--[if lt IE 9]>
<script src="${contextPath}/assets_individual/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets_individual/lib/es5-sham.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script>
    Object.getPrototypeOf = function getPrototypeOf(object) {
        return object.__proto__;
    };
</script>
<![endif]-->

<script>
    if (window.ActiveXObject) {
        var reg = /10\.0/;
        var str = navigator.userAgent;
        if (reg.test(str)) {
            Object.getPrototypeOf = function getPrototypeOf(object) {
                return object.__proto__;
            };
        }
    }
</script>


<script src="${contextPath}/assets_individual/lib/require.js"></script>
<script src="${contextPath}/assets_individual/dist/config.js"></script>
<script>
    var confirmBox;
    require(["react", "react-dom", "../js/listData", "MessageBox", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData, MessageBox) {
        renderTable(React, ReactDOM, ListData);
        confirmBox = renderWidget(React, ReactDOM, MessageBox, {
            type: "confirm",
            grid: 0.3,
            confirm: function (flag) {
                if (flag) {
                    cancelOrder();
                }
                return true;
            }
        }, $("#confirmBox")[0]);
        initSearchDateRangePicker();

        listeners();

        submitForm();

        checkFormValidate();
    });

    var ctx = "${contextPath}/individual/flowcoin";

    var STATUS = {
        "0": "待支付",
        "1": "支付中",
        "2": "支付成功",
        "3": "待支付",
        "4": "已取消"
    };
    var COLORS = {
        "1": "#377BC4",
        "2": "#EA6500",
        "4": "#8FC31C"
    };
    var renderTable = function (React, ReactDOM, ListData) {
        var ele = React.createElement(ListData, {
            action: ctx + "/historyOrders.html?${_csrf.parameterName}=${_csrf.token}",
            columns: [
                {name: "systemSerial", text: "订单号"},
                {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
                {name: "count", text: "数量"},
                {
                    name: "price", text: "金额", format: function (value, column, row) {
                    return row.price / 100;
                }
                },
                {name: "expireTime", text: "流量币有效期", format: "DateTimeFormat"},
                {
                    name: "status", text: "状态", format: function (value, column, row) {
                    var color = COLORS[value];
                    return "<span style='color: " + color + "'>" + STATUS[value] + "</span>"
                }
                },
                {
                    name: "op", text: "操作", format: function (value, column, row) {
                    if (row.status === 0 || row.status === 3) {
                        return "<a href='${contextPath}/individual/flowcoin/showPay.html?systemSerial=" + row.systemSerial + "' style='color: " + COLORS["2"] + "'><i class='iconfont icon-detail'></i>支付</a>" +
                                "<a data-id='" + row.systemSerial + "' href='#' class='ml-10 cancel-order' style='color: " + COLORS["4"] + "'><i class='iconfont icon-close'></i>取消订单</a>"
                    } else {
                        return "<a href='${contextPath}/individual/flowcoin/detail.html?systemSerial=" + row.systemSerial + "' style='color: " + COLORS["1"] + "'><i class='iconfont icon-detail'></i>详情</a>"
                    }
                }
                }
            ]
        });
        ReactDOM.render(ele, $("#table_wrap")[0]);
    };

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range');
        var startEle = $('#startDate');
        var endEle = $('#endDate');
        ele.dateRangePicker({
            separator: '至',
            container: ele.parent()[0],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + '至' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

    function listeners() {
        $("#table_wrap").on("click", ".cancel-order", function () {
            confirmBox.show('<div class="modal-body text-center">' +
                    '<i class="iconfont icon-iconfontclose2"></i>' +
                    '<div class="">确认取消本次订单?</div>' +
                    '</div>');
            confirmBox.setData($(this).data("id"));
        });
    }

    /**
     * 取消订单
     */
    function cancelOrder() {
        var orderId = confirmBox.getData();
        window.location.href = "${contextPath}/individual/flowcoin/cancelPurchase.html?systemSerial=" + orderId;
    }

	function verifyCount(v) {
        var a = /^[1-9][0-9]*$/;
        if (!v.match(a)) {
            return false;
        }
        return true;
    }
    
    /**
     * 根据购买数量获取金额
     */
    function getMoney() {
    	$("#money").html("");
        var count = $("#coins").val();
        if(count>0&&count<=100000&&verifyCount(count)){
	        $.ajax({
	            type: "POST",
	            url: "${contextPath}/individual/flowcoin/getMoney.html?${_csrf.parameterName}=${_csrf.token}",
	            data: {
	                count: count
	            },
	            dataType: "json", //指定服务器的数据返回类型，
	            success: function (res) {
	                if (res.result) {
	                    $("#money").html("￥" + res.result);
	                }
	                else {
	                    $("#money").html("");
	                }
	            },
	            error: function () {
	                $("#money").html("");
	            }
	        });
        }
    }
    ;

    function checkFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                $(element).parent().append(error);
            },
            errorElement: "span",
            rules: {
                coins: {
                    required: true,
                    digits: true,
                    notStartWithZero: true,
                    min: 1,
                    max: 100000
                }
            },
            messages: {
                coins: {
                    required: "请填写正确的数量值",
                    digits: "请填写正确的数量值",
                    notStartWithZero: "请填写正确的数量值",
                    min: "请填写正确的数量值",
                    max: "请填写正确的数量值"
                }
            }
        });
    }

    function submitForm() {
        $("#save-btn").on("click", function () {
        	$("#payFlag").val("false");
            showToast();
            if ($("#dataForm").validate().form()) {
                $("#dataForm").submit();
            }
        });

        $("#pay-btn").on("click", function () {
            $("#payFlag").val("true");
            showToast();
            if ($("#dataForm").validate().form()) {
                $("#dataForm").submit();
            }
        });
    }
</script>
</body>
</html>