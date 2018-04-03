<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>红包详情</title>

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
        <h3>业务办理-红包-活动详情
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
            <span>活动时间:</span>
            <span>${activities.startTime?datetime} 至 ${activities.endTime?datetime}</span>
        </div>

    <#if activityInfo.url??>
        <div class="form-group">
            <span>活动链接及二维码:</span>
            <span>${activityInfo.url!}</span>
            <div>
                <img src="${contextPath}/manage/individual/redpacket/qcode.html?activityId=${activityInfo.activityId}"
                     style="margin-left: 95px; width: 128px;">
            </div>
        </div>
    </#if>

        <div class="form-group">
            <span>红包类型:</span>
            <span>
            <#if activities??&&activities.type??&&activities.type==7>
                普通红包
            <#else>
                拼手气红包
            </#if>
            </span>
        </div>
        <div class="form-group">
            <span>
                <#if activities??&&activities.type??&&activities.type==7>
                    每个红包含:
                <#else>
                    总数额:
                </#if>
            </span>
            <span>${activityInfo.totalProductSize!} 个流量币</span>
        </div>
        <div class="form-group">
            <span>红包个数:</span>
            <span>${activityInfo.prizeCount!} 个</span>
        </div>
    </div>

    <div class="mt-30"><h3>中奖用户信息</h3></div>
    <hr>

    <div class="mt-20">
        <div class="form-inline text-right">
            <div class="form-group form-group-sm">
                <span class="ml-20">手机号码:</span>
                <input name="mobile" class="searchItem form-control mobileOnly" maxlength="11">
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

    var ctx = "${contextPath}/manage/individual/redpacket/";

    var STATUS = {
        "1": "等待领取",
        "2": "领取中",
        "3": "领取成功",
        "4": "领取失败已退回"
    };
    var COLORS = {
        "1": "#377BC4",
        "2": "#EA6500"
    };
    var renderTable = function (React, ReactDOM, ListData) {
        var ele = React.createElement(ListData, {
            action: ctx + "records.html?activityId=${activities.activityId!}&${_csrf.parameterName}=${_csrf.token}",
            columns: [
                {name: "recordId", text: "流水号"},
                {name: "ownMobile", text: "中奖人号码"},
                {name: "winTime", text: "中奖时间", format: "DateTimeFormat"},
                {name: "size", text: "中奖流量币(个)"},
                {name: "status", text: "状态", format: function (value, column, row) {
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