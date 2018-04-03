<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业充值</title>
    <meta name="keywords" content="流量平台 企业充值"/>
    <meta name="description" content="流量平台 企业充值"/>

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
        <h3>
        	<#if approvalType?? && approvalType == 2>
        		企业充值提交审批列表
        	<#elseif approvalType?? && approvalType == 9>
        		企业最小额度变更审批列表
        	<#elseif approvalType?? && approvalType == 10>
        		企业预警值变更审批列表
        	<#elseif approvalType?? && approvalType == 11>
        		企业暂停值变更审批列表
        	</#if>
        	<a class="btn btn-primary btn-sm pull-right btn-icon icon-back"  onclick="javascript:window.location.href='${contextPath}/manage/accountChange/index.html'">返回</a>                    	
        </h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">
                <span class="search-separator mt-10"></span>
                <div class="form-group form-group-sm" id="search-time-range">
                    <label>创建时间：</label>&nbsp;
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


<div class="modal fade dialog-sm" id="remove-dialog" data-backdrop="static">
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

<div class="modal fade dialog-sm" id="offline-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">下架成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="submit-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">请确认是否提交审批，一旦提交不可修改！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="tip-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">

                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">提交审批成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->s
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
	var approvalType = ${approvalType!};
    var statusFormat = function (value, column, row) {
        return value == 1 ? "上架" : "下架";
    };

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }
    
   var moneyFormator = function (value, column, row) {
    	if(1 == row.productType){
    		value = value / 1024.0
    		return value + "MB"
    	}else{
    		value = value / 100.0;
    		return value.toFixed(2) + "元";
    	}       
    }

    var opFormat = function (value, column, row) {
        return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/accountChange/changeEdit.html?accountChangeRequestId=" + row.id + "&entId=" + row.entId + "&prdId=" + row.prdId +  "&edit=true&approvalType=${approvalType!}' +  ${approvalType!} + '>编辑</a>",
            "<a class='btn-icon' href='javascript:submitAccountChange(" + row.id + "," + row.changeType + ")'>提交审批</a></span>"];
    };
    
    var moneyFormator = function (value, column, row) {
        if(1 == row.productType){
            return value + "MB"
        }else{
            return value.toFixed(2) + "元";
        }       
    }

    var productTypeFormator = function (value, column, row) {
        if(0 == row.productType){
            return "现金产品";
        }else if(1 == row.productType){
            return "流量池产品";
        }else if(2 == row.productType){
            return "流量包产品";
        }else if(3 == row.productType){
            return "话费产品";
        }else if(4 == row.productType){
            return "虚拟币产品";
        }else{
        	return "未知类型";
        }         
    }

    function submitAccountChange(id, approvalType) {
        $("#submit-dialog").modal("show");
        $("#sure").data("accountId", id);
        $("#sure").data("approvalType", approvalType);
    }
    ;

    var entId =${entId};
    var action = "${contextPath}/manage/accountChange/searchSubmitIndex.html?${_csrf.parameterName}=${_csrf.token}&entId=" + entId +"&approvalType=" + approvalType;

    var columns = [{name: "entName", text: "企业名称", tip: true},
        {name: "entCode", text: "企业编码", tip: true}];
    <#if approvalType?? && approvalType == 2>
		columns.push({name: "count", text: "充值额度",format: moneyFormator});
    <#elseif approvalType?? && approvalType == 9>
		columns.push({name: "count", text: "最小额度",format: moneyFormator});
    <#elseif approvalType?? && approvalType == 10>
		columns.push({name: "count", text: "预警值",format: moneyFormator});
    <#elseif approvalType?? && approvalType == 11>
		columns.push({name: "count", text: "暂停值",format: moneyFormator});
    </#if>
    columns.push({name: "productType", text: "产品类型", format: productTypeFormator});
    columns.push({name: "createTime", text: "创建时间", format: "DateTimeFormat"});
    columns.push({name: "updateTime", text: "更新时间", format: "DateTimeFormat"});
    columns.push({name: "op", text: "操作", format: opFormat});

    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

    require(["common", "bootstrap", "daterangepicker", "page/list"], function () {
        initSearchDateRangePicker();

        $("#subject-btn").on("click", function () {
            window.location.reload();
        });

        init();
    });

    function init() {

        $("#sure").on("click", function () {
            var accountId = $("#sure").data("accountId");

            var entId = "${entId}";
            var currentId = "${currentId}";
            var roleId = "${roleId}";
			var approvalType = $("#sure").data("approvalType");
			
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/approval/submitAccountAjax.html",
                data: {
                    accountId: accountId,
                    entId: entId,
                    currentId: currentId,
                    roleId: roleId,
                    approvalType : approvalType
                },
                type: "post",
                dataType: "json"
            }).success(function (ret) {
                if (ret) {
                    if (ret.submitRes == "success") {
                        showTipDialog("提交审批成功！");
                        window.location.href = "${contextPath}/manage/accountChange/record.html?approvalType=" + approvalType;                       
                    } else if(ret.submitRes == "fail"){
                        showTipDialog("提交审批失败！");
                    }else{
                        showTipDialog(ret.submitRes);
                    }                    
                }
            });
        });
    }

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
