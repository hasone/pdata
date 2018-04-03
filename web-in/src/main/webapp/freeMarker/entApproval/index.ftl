<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业审批列表</title>
    <meta name="keywords" content="流量平台 待审批列表列表"/>
    <meta name="description" content="流量平台 待审批列表列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

    <style>
    
    	.icon-syxg {
            color: #708090;
            background-image: url(${contextPath}/assets/imgs/icon-syxg.png);
        }
    	
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

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>企业审批列表
        </h3>
    </div>

    <div class="tools row text-right">

        <div class="col-lg-12 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <input name="currentUserId" id="currentUserId" class="searchItem" type="hidden" value="${currUserId!}">
                <input name="roleId" id="roleId" class="searchItem" type="hidden" value="${roleId!}">
                <input name="managerId" id="managerId" class="searchItem" type="hidden" value="${managerId!}">

                <div class="form-group form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="text" name="name" id="name" class="form-control searchItem " autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                <div class="form-group form-group-sm">
                    <label for="code">企业编码：</label>
                    <input type="text" name="code" id="code" class="form-control searchItem" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
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

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var currUserId = "${currUserId}";
    var roleName = "${roleName!}";
    var statusFormat = function (value, column, row) {
        return "待" + roleName + "审批";
    };

    var nameFormat = function (value, column, row) {
        <#-- return "<a href='${contextPath}
        /manage/enterprise/showEnterpriseDetail.html?id=" + row.entId + "'>" + row.name + "</a>";-->
        return row.entName;
    };

    var codeFormat = function (value, column, row) {
        <#-- if(row.code != null){
            return "<a href='${contextPath}
        /manage/enterprise/showEnterpriseDetail.html?id=" + row.entId + "'>" + row.code + "</a>";
                    }-->
        return row.entCode;
    };


    var buttons1Format = function (value, column, row) {
        return ["<a class='btn-icon icon-syxg mr-5' href='${contextPath}/manage/approval/entApprovalDetail.html?id=" + row.id + "'>审批</a>"];
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

    var columns = [{name: "entName", text: "企业名称", tip: true, format: nameFormat},
        {name: "entCode", text: "企业编码", format: codeFormat},
        {name: "districtName", text: "所属地区", tip: true},
        {name: "entCreateTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "status", text: "企业状态", format: statusFormat},
        {name: "op1", text: "审批操作", format: buttons1Format}];

    var action = "${contextPath}/manage/approval/search.html?${_csrf.parameterName}=${_csrf.token}&approvalType=0";

    require(["common", "bootstrap", "daterangepicker", "easyui", "page/list"], function () {
        initSearchDateRangePicker();
    });

    function initSearchDateRangePicker() {

    }

</script>

</body>
</html>