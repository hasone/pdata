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
    <title>流量平台-卡数据列表</title>
    <meta name="keywords" content="流量平台卡数据列表"/>
    <meta name="description" content="流量平台卡数据列表"/>

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
        <h3>卡数据列表</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-4">
            <a href="${contextPath}/manage/mdrc/statistics/pieStatistics.html?configId=${configId!}&year=${year!}"
               class="btn btn-sm btn-danger mr-10">
                <i class="fa fa-plus mr-5"></i>查看卡状态统计
            </a>
        </div>

        <div class="col-sm-8 dataTables_filter text-right">
            <form class="form-inline" action="${contextPath}/manage/mdrc/statistics/index.html" method="POST">

                <div class="form-group mr-10 form-group-sm">
                    <label for="cardNumber">卡号：</label>
                    <input type="text" class="form-control searchItem" id="cardNumber" name="cardNumber"
                           value="${cardNumber!}" style="width:215px;">
                </div>

                <div class="form-group mr-10 form-group-sm">
                    <label for="statusSelect">状态：</label>
                    <select id="statusSelect" name="status" class="form-control searchItem">
                        <option value="">全部</option>
                    <#list cardStatus?keys as item>
                        <option value="${item}"
                                <#if status?? && item == status>selected</#if>>${cardStatus[item]}</option>
                    </#list>
                    </select>
                </div>
                <button type="submit" class="btn btn-sm btn-warning">确定</button>
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


    <div class="modal fade dialog-sm" id="remove-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">删除成功</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

    <div id="dialogContent" style="display:none;width:600px;">
        <div>
            <font style="font-weight:bold;font-size:14px;">短信发送中。。。</font>
            <img src="${contextPath}/manage2/assets/images/load-16-16.gif"/>
        </div>
    </div>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var cardStatus = {};
    <#list cardStatus?keys as item>
    cardStatus["${item}"] = "${cardStatus[item]}";
    </#list>

    require(["common", "bootstrap", "page/list"], function () {
    });


</script>


<script>


    var statusFormat = function (value, column, row) {
        if (row.opStatus > 7) {
            return cardStatus[row.opStatus];
        } else {
            return cardStatus[row.status];
        }
    };

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

    var stringValue = function (value) {
        if (value) {
            return value.toString();
        } else {
            return "";
        }

    }
    var columns = [
        {name: "cardNumber", text: "卡号", tip: true},
        {name: "status", text: "状态", format: statusFormat},
        {name: "createTime", text: "创建时间", format: dateFormator},
        {name: "op", text: "操作"}
    ];
    var action = "${contextPath}/manage/mdrc/statistics/search.html?${_csrf.parameterName}=${_csrf.token}";
</script>
</body>
</html>