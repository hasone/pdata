<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-抢红包列表</title>
    <meta name="keywords" content="流量平台 抢红包列表"/>
    <meta name="description" content="流量平台 抢红包列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

    <style>
        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-sync {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-sync.png);
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
        }

        .icon-online {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-online.png);
        }

        .icon-search {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-search.png);
        }

        .form {
            width: 350px;
            text-align: left;
        }

        .form label {
            width: 70px;
            text-align: right;
            margin-right: 10px;
        }

        .form .form-control {
            width: 230px;
        }

        .lg-btn-input {
            width: 205px;
            text-align: left;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        .form .form-group .prompt {
            padding-left: 86px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .search-separator {
            display: none;
        }

        @media (max-width: 1300px) {
            .search-separator {
                display: block;
            }
        }

    </style>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>抢红包列表</h3>
    </div>

    <div class="tools row ">
        <div class="col-sm-12 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">

                <div class="form-group mr-10">
                    <label for="mobile">抢红包手机号：</label>
                    <input type="text" class="form-control searchItem" id="chargeMobile" name="chargeMobile" placeholder="">
                </div>

                <div class="form-group" id="search-time-range">
                    <label>抢红包时间：</label>&nbsp
                    <input type="text" style="width:110px" class="form-control search-startTime searchItem" name="startTime" id="startTime" placeholder="">~
                    <input type="text" style="width:110px" class="form-control search-endTime" id="endTime" placeholder="">
                    <input type="hidden" style="width:110px" class="form-control search-endTime searchItem" name="endTime" id="endTime1" placeholder="">
                </div>

                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>


                <a onclick="createFile()" class="btn btn-sm btn-warning">报表导出</a>
            </div>
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
    
</div>

<!--[if lt IE 9]-->
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<!--[endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
var statusFormat = function (value, column, row) {
        if (row.status == 1) {
            return "待充值";
        }
        if (row.status == 2) {
            return "已发送充值请求";
        }
        if (row.status == 3) {
            var reason = row.reason;
            return reason;
        }
        if (row.status == 4) {
            var reason = row.reason;
            return reason;
        }
        return "";        
    }
    
    var sizeFormat = function (value, column, row) {
        if(row.size!=null){
            return row.size + "M"
        };        
    }

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

    var columns = [{name: "createTime", text: "抢红包时间", format: "DateTimeFormat"},
        {name: "orderMobile", text: "发红包手机号"},
        {name: "chargeMobile", text: "抢红包手机号"},
        {name: "size", text: "产品大小", format: sizeFormat},
        {name: "status", text: "状态", tip:true, format: statusFormat}];


    var action = "${contextPath}/manage/scredpacket/searchGrabList.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "daterangepicker", "easyui", "page/list"], function () {
        initSearchDateRangePicker();
    });

    function initSearchDateRangePicker(){
        var ele = $('#search-time-range');

        var startEle = $('#startTime');
        var endEle = $('#endTime');
        var endEle1 = $('#endTime1');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
            beforeShowDay: function(t) {
                var valid = t.getTime() < new Date().getTime();
                return [valid,'',''];
            },
            customShortcuts: [
                {
                    name: '半年内',
                    dates : function()
                    {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate()-182);
                        return [start,end];
                    }
                },
                {
                    name: '一年内',
                    dates : function()
                    {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate()-360);
                        return [start,end];
                    }
                }
            ],
            getValue: function () {
                if (startEle.val() && endEle.val() )
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
                endEle1.val(s2 + " 23:59:59");
            }
        });
    }

    function createFile() {
        var chargeMobile = document.getElementById('chargeMobile').value;
        var orderSystemNum = document.getElementById('orderSystemNum').value;
        var startTime = document.getElementById('startTime').value;
        var endTime = document.getElementById('endTime1').value;
        window.open(
                "${contextPath}/manage/scredpacket/createGrabListFile.html?orderSystemNum=" + orderSystemNum 
                + "&&chargeMobile" + chargeMobile + "&&startTime=" + startTime + "&&endTime=" + endTime);
    }

</script>

</body>
</html>