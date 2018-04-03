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
    <title>流量平台-用户修改记录</title>
    <meta name="keywords" content="流量平台 用户修改记录"/>
    <meta name="description" content="流量平台 用户修改记录"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>
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
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<script src="${contextPath}/assets/lib/respond.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>用户修改记录
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tools row">
        <div class="col-sm-12 dataTables_filter text-right">
           
            <form class="form-inline" action="${contextPath}/manage/user/index.html" method="GET">
               
                <span class="search-separator mt-10"></span>
                <div class="form-group form-group-sm" id="search-time-range">
                    <label>提交时间：</label>&nbsp
                    <input type="text" class="form-control search-startTime searchItem" name="startTime"
                           value="${(pageResult.queryObject.queryCriterias.startTime)!}" id="startTime" placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                           value="${(pageResult.queryObject.queryCriterias.endTime)!}" placeholder="">
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:history.go(-1)">返回</a>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>

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
    require(["common", "bootstrap",  "page/list","daterangepicker"], function (common, bootstrap, list,daterangepicker) {
        initSearchDateRangePicker(); 
      
       
    });

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
        if(row.virifyStatus == 0 ){
            return "审批中";
        }else if(row.virifyStatus == 1 ){
            return "已通过";
        }else if(row.virifyStatus == 2 ){
            return "已驳回";
        }   
    }
    
    var opFormat = function (value, column, row) {
        return ["<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/adminChange/historyDetail.html?${_csrf.parameterName}=${_csrf.token}&id=" + row.requestId  + "'>查看</a>"];
    };

    var columns = [{name: "requestTime", text: "提交时间", format: "DateTimeFormat"},
                    <#if needVirify??>
                    {name: "virifyStatus", text: "审批状态", format: statusFormat},
                    </#if>
                    {name: "op", text: "<#if needVirify??>审批操作<#else>详情</#if>", format: opFormat}];


    var action = "${contextPath}/manage/adminChange/searchHistory.html?${_csrf.parameterName}=${_csrf.token}&adminId=${adminId!}";
    

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range');

        var startEle = $('#startTime');
        var endEle = $('#endTime');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
            beforeShowDay: function (t) {
                var valid = t.getTime() < new Date().getTime();
                return [valid, '', ''];
            },
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
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }


</script>
</body>
</html>