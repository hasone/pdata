<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-会员列表</title>
    <meta name="keywords" content="流量平台 会员列表"/>
    <meta name="description" content="流量平台 会员列表"/>

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
        <h3>会员列表</h3>
    </div>

    <div class="tools row ">
        <div class="col-sm-12 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">

                <div class="form-group mr-10">
                    <label for="mobile">手机号码：</label>
                    <input type="text" class="form-control searchItem" id="mobile" name="mobile" placeholder="">
                </div>
                
                <#--<div class="form-group mr-10">
                    <label for="openid">OPENID：</label>
                    <input type="text" class="form-control searchItem" id="openid" name="openid" placeholder="">
                </div>
                
                <div class="form-group mr-10">
                    <label for="nickname">昵称：</label>
                    <input type="text" class="form-control searchItem" id="nickname" name="nickname" placeholder="">
                </div>-->
                
                <div class="form-group" id="search-time-range">
                    <label>时间：</label>&nbsp
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
    
</div>

<!--[if lt IE 9]-->
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<!--[endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var buttonsFormat = function (value, column, row) {
        return '<a class="btn-icon icon-detail mr-5" href="${contextPath}/manage/membership/detail.html?mobile=' + row.mobile + '">详情</a>';
    }

    /**
     *
     */
    function showTip(ele) {
        showTipDialog($(ele).data("msg"));
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

    var columns = [{name: "mobile", text: "手机号"},
        {name: "count", text: "流量币", tip: true},
        {name: "accumulateCount", text: "累积流量币"},
        {name: "grade", text: "等级"},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"}, 
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/membership/search.html?${_csrf.parameterName}=${_csrf.token}";

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
                endEle1.val(s2+" 23:59:59");
            }
        });
    }

    function createFile() {
        var nickname = "";//document.getElementById('nickname').value;
        nickname = nickname.replace("%", "%25");
        var mobile = document.getElementById('mobile').value;
        var openid = "";//document.getElementById('openid').value;
        var startTime = document.getElementById('startTime').value;
        var endTime = document.getElementById('endTime1').value;
        window.open(
                "${contextPath}/manage/membership/createMembershipListFile.html?mobile=" + mobile + "&&nickname=" + nickname
                + "&&openid=" + openid + "&&startTime=" + startTime + "&&endTime=" + endTime);
    }

</script>

</body>
</html>