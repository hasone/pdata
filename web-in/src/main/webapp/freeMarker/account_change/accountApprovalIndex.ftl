<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-充值审批列表</title>
    <meta name="keywords" content="流量平台 充值审批列表"/>
    <meta name="description" content="流量平台 充值审批列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>


    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <style>
        .table > thead > tr > th {
            line-height: initial;
        }

        .table > thead > tr > th, .table > tbody > tr > td {
            vertical-align: middle;
        }
        
        .icon-syxg {
            color: #708090;
            background-image: url(${contextPath}/assets/imgs/icon-syxg.png);
        }

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

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
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
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>充值审批列表</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">

                <input name="currentUserId" id="currentUserId" class="searchItem" type="hidden" value="${currUserId!}">
                <input name="roleId" id="roleId" class="searchItem" type="hidden" value="${roleId!}">
                <input name="managerId" id="managerId" class="searchItem" type="hidden" value="${managerId!}">

                <div class="form-group mr-10 form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="text" name="name" class="form-control searchItem" id="name" placeholder="">
                </div>
                <div class="form-group mr-10 form-group-sm">
                    <label for="code">企业编码：</label>
                    <input type="text" name="code" class="form-control searchItem" id="code" placeholder="">
                </div>

                <span class="search-separator mt-10"></span>
                <div class="form-group form-group-sm" id="search-time-range">
                    <label>充值时间：</label>&nbsp
                    <input type="text" class="form-control search-startTime searchItem" name="startTime" id="startTime"
                           placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                           placeholder="">
                </div>

                <a type="submit" class="btn btn-sm btn-warning" id="search-btn">确定</a>
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
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var statusFormat = function (value, column, row) {
        return value == 1 ? "上架" : "下架";
    };

    var statusFormat = function (value, column, row) {
        if (row.status == 1) {
            return "已提交";
        }
        if (row.status == 2) {
            return "审批中";
        }
        if (row.status == 3) {
            return "审批通过";
        }
        if (row.status == 4) {
            return "审批驳回";
        }
    };
    
        
    var countFormator = function (value, column, row) {
    	if(1 == row.productType){
    		return value + "MB"
    	}else {
    		return value.toFixed(2) + "元";
    	}       
    }

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }
    
    var opFormat = function (value, column, row) {
    	if(row.canOperate == 1){
    	   return ["<a class='btn-icon icon-syxg mr-5' href='${contextPath}/manage/approval/accountChangeDetail.html?${_csrf.parameterName}=${_csrf.token}&id=" + row.id + "&approvalType=2" + "'>审批</a>"];
    	}else{
    	   return ["<a class='btn-icon icon-syxg mr-5' href='${contextPath}/manage/accountChange/recordDetial.html?${_csrf.parameterName}=${_csrf.token}&requestId=" + row.id + "&approvalType=2" + "'>详情</a>"];   		
    	}
    };

    var action = "${contextPath}/manage/approval/search.html?${_csrf.parameterName}=${_csrf.token}&approvalType=2&result=0";
    var columns = [{name: "createTime", text: "申请充值时间", format: "DateTimeFormat"},
        {name: "productName", text: "账户名称"},
    	{name: "productTypeName", text: "账户类型"},
        {name: "count", text: "充值额度",format: countFormator},
        {name: "managerName", text: "客户经理"},
        {name: "managerPhone", text: "客户经理手机号"},
        {name: "entName", text: "企业名称"},
        {name: "entCode", text: "企业编码"},
        {name: "districtName", text: "所属地区"},
        {name: "op", text: "操作", format: opFormat}];

    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

    require(["common", "bootstrap", "daterangepicker", "page/list"], function () {
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