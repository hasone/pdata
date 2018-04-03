<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>赠送详情</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>

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
        <h3>业务办理-赠送-活动详情
        <a href="javascript:history.go(-1)" class="btn btn-sm btn-success pull-right" style="color: white;">
                <i class="fa fa-mail-reply mr-5"></i>返 回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="form-group">
            <span>活动名称:</span>
            <span>${activities.name!}</span>
        </div>
        <div class="form-group">
            <span>赠送时间:</span>
            <span>${activities.startTime?datetime}</span>
        </div>
        <div class="form-group">
            <span>赠送总号码数:</span>
            <span>${activityInfo.prizeCount!}</span>
        </div>
        <div class="form-group">
            <span>单个号码赠送:</span>
            <span>${activityPrize.size!}个流量币</span>
        </div>
        <div class="form-group">
            <span>赠送流量币总个数:</span>
            <span>${activityInfo.totalProductSize!}个</span>
        </div>
    </div>

    <div class="mt-30"><h3>中奖用户信息</h3></div>
    <hr>

    <div class="mt-20">
        <div class="form-inline text-right">
            <div class="form-group form-group-sm">
                <span class="ml-20">手机号码:</span>
                <input name="chargeMobile" id="chargeMobile" class="searchItem form-control mobileOnly" maxlength="11">
                <input type="hidden" name="activityId" id="activityId" value="${activityId!}"
                       class="searchItem form-control">
            </div>
            <a class="btn btn-sm btn-default btn-width ml-20" id="search-btn">查询</a>
        </div>
        <div id="table_wrap" class="mt-10 table-wrap text-center"></div>
    </div>
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
        listeners();
    });

    var ctx = "http://192.168.170.16:8415/mock/prov/";

    var STATUS = {
        "0": "赠送中",
        "1": "赠送成功",
        "2": "赠送失败退回中",
        "3": "赠送失败已退回",
        "4": "赠送失败退回中"
    };
    var COLORS = {
        "1": "#377BC4",
        "2": "#EA6500"
    };
    var renderTable = function (React, ReactDOM, ListData) {
        var ele = React.createElement(ListData, {
            action: "${contextPath}/individual/flowcoinpresent/searchRecords.html?${_csrf.parameterName}=${_csrf.token}",
            columns: [
                {name: "recordId", text: "流水号"},
                {name: "winTime", text: "赠送时间", format: "DateTimeFormat"},
                {name: "chargeMobile", text: "被赠送人号码"},
                {name: "productSize", text: "赠送流量币(个)"},
                {
                    name: "status", text: "状态", format: function (value, column, row) {
                    var color = COLORS[value];
                    return "<span style='color: " + color + "'>" + STATUS[value] + "</span>"
                }
                }
            ]
        });
        ReactDOM.render(ele, $("#table_wrap")[0]);
    };


    /**
     * 监听
     */
    function listeners() {
    }
</script>
</body>
</html>