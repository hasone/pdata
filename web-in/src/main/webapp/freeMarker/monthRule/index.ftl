<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-包月赠送</title>
    <meta name="keywords" content="流量平台 包月赠送列表"/>
    <meta name="description" content="流量平台 包月赠送列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
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
        <h3>包月赠送</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/monthRule/addOrEdit.html" class="btn btn-sm btn-danger">
                <i class="fa fa-plus mr-5"></i>新建包月赠送
            </a>
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">状态：</label>
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; height: 0; opacity: 0" class="form-control searchItem" name="status"
                               id="status" value="">
                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                        	<li data-value=""><a href="#">全部</a></li>
                        	<#list presentStatus?keys as item>
                    				<li data-value="${item}"><a href="#">${presentStatus[item]}</a></li>
                			</#list>
	                    </ul>                       
                    </div>
                </div>

                <div class="form-group form-group-sm" id="search-time-range">
                    <label>创建时间：</label>&nbsp
                    <input type="text" class="form-control search-startTime searchItem" name="startTime"
                           value="" id="startTime" placeholder="">&nbsp;~&nbsp;
                    <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                           value="" placeholder="">
                </div>

                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
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
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

	var presentStatus = {};
    <#list presentStatus?keys as item>
    	presentStatus["${item}"] = "${presentStatus[item]}";
    </#list>

    var buttonsFormat = function (value, column, row) {
        return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/monthRule/detail.html?id=" + row.id + "'>详情</a>"];
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

    var statusFormator = function (value, column, row) {
 		return presentStatus[value];
    };
    
    var creatorNameFormat = function (value, column, row) {
        if (typeof(row.activityCreator) == "undefined") { 
            return "-";
        } else{
            return row.activityCreator.userName;
        } 
    };
    
    var creatorMobileFormat = function (value, column, row) {
        if (typeof(row.activityCreator) == "undefined") { 
            return "-";
        } else{
            return row.activityCreator.mobilePhone;
        } 
    };

    var columns = [
    	{name: "activityName", text: "活动名称", tip: true},
        {name: "productName", text: "产品名称", tip: true},
        {name: "total", text: "被赠送人总数"},
        {name: "monthCount", text: "赠送月数"},
        {name: "status", text: "状态", format: statusFormator},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        <#if isShandong?? && isShandong == "true">
        {name: "creatorName", text: "创建者",format: creatorNameFormat},
        {name: "creatorMobile", text: "创建者账号",format: creatorMobileFormat},
         </#if>
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/monthRule/search.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "page/list", "daterangepicker"], function () {
        initSearchDateRangePicker();
    });

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