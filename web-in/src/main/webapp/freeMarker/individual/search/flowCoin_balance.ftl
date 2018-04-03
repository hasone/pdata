<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量币余额</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets_individual/theme/default/theme.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/main.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets_individual/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets_individual/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mb-20">
        <h3>业务查询-流量币余额</h3>
    </div>
    <div class="form-group">
        <span>当前账户:</span>
        <span>${mobile!}</span>
    </div>
    <div class="form-group">
        <span>流量币余额:</span>
        <span class="green-text-lg">&nbsp;&nbsp;1000</span>&nbsp;&nbsp;个
    </div>
    <hr>
    <h4>流量币收支明细</h4>
    <div class="mt-30">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#incomeList" role="tab" data-toggle="tab">收入</a></li>
            <li role="presentation"><a href="#costList" role="tab" data-toggle="tab">支出</a></li>
        </ul>

        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="incomeList">
                <div class="tile gray-border tile-noTopBorder">
                    <div class="mt-20">
                        <div class="form-inline text-right">
                            <span>收入时间:</span>
                            <div class="form-group form-group-sm form-inline" style="position: relative;">
                                <span id="search-time-range" class="date-range-wrap valign-middle">
                                    <input name="startDate" id="search-startDate" class="form-control searchItem"
                                           readonly="readonly">-
                                    <input name="endDate" id="search-endDate" class="form-control searchItem"
                                           readonly="readonly">
                                </span>
                            </div>
                            <a class="btn btn-sm btn-default btn-width ml-20" id="search-btn">查询</a>
                        </div>
                        <div id="table_wrap" class="mt-10 table-wrap text-center"></div>
                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" id="costList">
                <div class="tile gray-border tile-noTopBorder">
                    <div class="mt-20">
                        <div class="form-inline text-right">
                        	<span>手机号码:</span>
				            <input name="mobile" id="mobile" class="form-control searchItem1"/>           
                            <span>支出时间:</span>
                            <div class="form-group form-group-sm form-inline" style="position: relative;">
                                <span id="search-time-range1" class="date-range-wrap valign-middle">
                                    <input name="startDate" id="search-startDate1" class="form-control searchItem1"
                                           readonly="readonly">-
                                    <input name="endDate" id="search-endDate1" class="form-control searchItem1"
                                           readonly="readonly">
                                </span>
                            </div>
                            <a class="btn btn-sm btn-default btn-width ml-20" id="search-btn1">查询</a>
                        </div>
                        <div id="table_wrap1" class="mt-10 table-wrap text-center"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="messageBox"></div>
    <div id="closeConfirmBox"></div>
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
    require(["react", "react-dom", "../js/listData", "MessageBox", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData, MessageBox) {
        renderTable(React, ReactDOM, ListData);
        renderTable1(React, ReactDOM, ListData);
        initSearchDateRangePicker();
        initSearchDateRangePicker1();
        listeners();
    });

    var ctx = "${contextPath}/individual/query";

    var STATUS = {
        "1": "领取成功",
        "2": "领取中",
        "3": "领取失败已退回"
    };
    var COLORS = {
        "1": "#377BC4",
        "2": "#EA6500"
    };
    var renderTable = function (React, ReactDOM, ListData) {
        var ele = React.createElement(ListData, {
            action: ctx + "/inRecord.html?${_csrf.parameterName}=${_csrf.token}",
            columns: [
                {name: "createTime", text: "收入时间", format: "DateTimeFormat"},
                {name: "count", text: "个数"},
                {name: "description", text: "来源"},
                {name: "expireTime", text: "有效期", format: "DateTimeFormat"}
            ]
        });
        ReactDOM.render(ele, $("#table_wrap")[0]);
    };

    var renderTable1 = function (React, ReactDOM, ListData) {
        var ele = React.createElement(ListData, {
            action: ctx + "/outRecord.html?${_csrf.parameterName}=${_csrf.token}",
            searchClass: "searchItem1",
            searchBtn: $("#search-btn1"),
            columns: [
                {name: "createTime", text: "支出时间", format: "DateTimeFormat"},
                {name: "mobile", text: "支出对象"},
                {name: "description", text: "支出类型"},
                {name: "count", text: "支出数量"}
            ]
        });
        ReactDOM.render(ele, $("#table_wrap1")[0]);
    };

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range');
        var startEle = $('#search-startDate');
        var endEle = $('#search-endDate');
        ele.dateRangePicker({
            separator: '~',
            container: ele.parent()[0],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + '~' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

    function initSearchDateRangePicker1() {
        var ele = $('#search-time-range1');
        var startEle = $('#search-startDate1');
        var endEle = $('#search-endDate1');
        ele.dateRangePicker({
            separator: '~',
            container: ele.parent()[0],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + '~' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }
    /**
     * 监听
     */
    function listeners() {
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $("#nav-text").html($(this).text());
        });
    }
    function closeActivity(ele) {
        var id = $(ele).data("id");
        closeConfirmBox.show('<div class="modal-body text-center">' +
                '<div class="">活动一旦关闭则无法继续，请确认是否关闭？</div>' +
                '</div>');
        closeConfirmBox.setData(id);
    }

</script>

</body>
</html>