<!DOCTYPE html>
<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-云企信退款列表</title>
    <meta name="keywords" content="流量平台云企信退款列表"/>
    <meta name="description" content="流量平台 云企信退款列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
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
        <h3>云企信退款管理</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/payplatform/yqxRefundPage.html" class="btn btn-sm btn-danger">
                        <i class="fa fa-plus mr-5"></i>
                                                                   退款处理
                    </a>
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <form class="form-inline" action="${contextPath}/manage/user/index.html" method="GET">
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">流水号：</label>
                    <input type="text" class="form-control searchItem" id="doneCode" name="doneCode" value=""
                           placeholder="">
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>
    </div>


    <div class="tile mt-30">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <div role="table"></div>
                </div>
            </div>
        </div>
    </div>

    <div role="pagination"></div>


</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", , "page/list"], function () {
        init();
        $("#subject-btn").on("click", function () {
            window.location.reload();
        });
    });

</script>
<script>
    function Delete() {
        var i = window.confirm("确定要删除吗？");
        return i;
    }
</script>

<script>

    function dateFormat(date, fmt) {
        var o = {
            "M+": date.getMonth() + 1,                 //月份
            "d+": date.getDate(),                    //日
            "h+": date.getHours(),                   //小时
            "m+": date.getMinutes(),                 //分
            "s+": date.getSeconds(),                 //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }
    
    var statusFormat = function (value, column, row) {
        if (row.status == 0) {
            return "待发送";
        }
        if (row.status == 1) {
            return "受理成功";
        }
        if (row.status == 2) {
            return "受理失败";
        }
        if (row.status == 3) {
            return "退款成功";
        }
        if (row.status == 4) {
            return "退款失败";
        }
    };

    var nameFormat = function (value, column, row) {
        return row.managerName + row.roleName;
    }

    var columns = [{name: "doneCode", text: "支付平台返回流水号", tip: true},
                    {name: "reason", text: "退款原因", tip: true},
                    {name: "refundSerialNum", text: "发起退款流水号", tip: true},
                    {name: "status", text: "状态", format:statusFormat},
                    {name: "msg", text: "相关信息", tip: true},
                    {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
                    {name: "updateTime", text: "更新时间", format: "DateTimeFormat"},
                    {name: "resultReturnTime", text: "异步返回时间", format: "DateTimeFormat"}
                    ];


    var action = "${contextPath}/manage/payplatform/yqxRefundIndexSearch.html?${_csrf.parameterName}=${_csrf.token}";
    
    function init() {
        
    }


</script>
</body>
</html>