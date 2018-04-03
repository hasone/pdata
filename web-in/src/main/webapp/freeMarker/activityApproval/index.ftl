<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-活动审批列表</title>
    <meta name="keywords" content="流量平台 活动审批列表"/>
    <meta name="description" content="流量平台 活动审批列表"/>

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
        <h3>活动审批列表</h3>
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
                <!--
                <div class="form-group mr-10 form-group-sm">
                    <label for="code">企业编码：</label>
                    <input type="text" name="code" class="form-control searchItem" id="code" placeholder="">
                </div>

                <span class="search-separator mt-10"></span>
                <div class="form-group form-group-sm" id="search-time-range">
                    <label>请求审批时间：</label>&nbsp;
                    <input type="text" class="form-control search-startTime searchItem" name="startTime" id="startTime"
                           placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                           placeholder="">
                </div>
				-->
				<#if approvalType ?? && approvalType == 3>
				<div class="form-group mr-10 form-group-sm">
                    <label>审批状态：</label>
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="searchItem"
                               name="result" value="" id="result">
                        <button type="button" class="btn btn-default" style="width: 110px" id="selectedItem">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                        	<li data-value=""><a href="#">全部</a></li>
                            <li data-value="0"><a href="#">审批中</a></li>
                            <li data-value="1"><a href="#">已通过</a></li>
                            <li data-value="2"><a href="#">已驳回</a></li>
                        </ul>
                    </div>
				</div>
				</#if>
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

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }

    var opFormat = function (value, column, row) {
        return ["<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/approval/accountChangeDetail.html?${_csrf.parameterName}=${_csrf.token}&id=" + row.id + "&approvalType=3" + "'>审批充值</a>"];
    };

    var activityTypeFormat = function (value, column, row) {
        if (row.activityType == 2) {
            return "红包";
        }
        if (row.activityType == 3) {
            return "转盘";
        }
        if (row.activityType == 4) {
            return "砸金蛋";
        }
        if (row.activityType == 5) {
            return "流量卡";
        }
        if (row.activityType == 6) {
            return "二维码";
        }
    }
    
    var resultFormat = function (value, column, row) {
        if (row.result == 0) {
            return "审批中";
        }
        if (row.result == 1) {
            return "已通过";
        }
        if (row.result == 2) {
            return "已驳回";
        }
    }

    var buttonsFormat = function (value, column, row) {
        if(row.canOperate == "1"){
    		return ["<a class='btn-icon icon-syxg mr-5' href='${contextPath}/manage/approval/activityApprovalDetail.html?id=" + row.id + "&canOperate=" + row.canOperate + "'>审批</a>"];  				
    	}else{
    		return ["<a class='btn-icon icon-syxg mr-5' href='${contextPath}/manage/approval/activityApprovalDetail.html?id=" + row.id + "&canOperate=" + row.canOperate + "'>详情</a>"];
    	}
    }

    var action = "${contextPath}/manage/approval/search.html?${_csrf.parameterName}=${_csrf.token}&approvalType=3&back=${(back)!0}";
    var columns = [{name: "activityName", text: "活动名称", tip: true},
        {name: "createTime", text: "请求审批时间", format: "DateTimeFormat"},
        {name: "managerName", text: "企业管理员"},
        {name: "managerPhone", text: "企业管理员手机号"},
        {name: "entName", text: "企业名称", tip: true},
        {name: "entCode", text: "企业编码", tip: true},
        {name: "districtName", text: "所属地区", tip: true},
        {name: "result", text: "审批状态", format: resultFormat},
        {name: "op", text: "审批操作", format: buttonsFormat}];

    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

    require(["common", "bootstrap", "daterangepicker", "page/list"], function () {
       // initSearchDateRangePicker();
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
    
    //条件回填
    function searchCallback(data){
    	$("#name").val(data.queryObject.name);
    	var result = data.queryObject.result;
    	$("#result").val(result);
    	var item = "请选择";
    	if(result == 0){
    		item = "审批中";    		
    	}else if(result == 1){
    		item = "已通过";    	
    	}else if(result == 2){
    		item = "已驳回";    	
    	}
    	$("#selectedItem").html(item);
    	$("#pageSize").val(data.queryObject.pageSize);
    	$("#pageNum").val(data.queryObject.pageNum);
    	action = "${contextPath}/manage/approval/search.html?${_csrf.parameterName}=${_csrf.token}&approvalType=3";
    }	 
</script>
</body>
</html>