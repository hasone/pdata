<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>统一流量平台-活动管理-流量众筹</title>
    <meta name="keywords" content="统一流量平台 活动管理 流量众筹" />
    <meta name="description" content="统一流量平台 活动管理 流量众筹" />

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <style>
        .table>thead>tr>th {
             line-height: initial;
        }
        .table>thead>tr>th,.table>tbody>tr>td {
            vertical-align: middle;
        }

        .table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th {
            padding: 8px 0;
        }
        .table-indent th:first-child, .table-indent tr>td:first-child {
            text-indent: 0;
        }

        .icon-sync {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-sync.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-online {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-online.png);
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
        }

        .table tbody > tr > td:last-child, .table tbody > tr > td:last-child *, .table thead > tr > th:last-child{
            max-width: 10000px;
            overflow: visible;
        }

        @media (min-width: 768px) {
            .form-inline .form-control {
                display: inline-block;
                width: 120px;
            }
        }
    </style>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->
</head>
<body>

    <div class="main-container">
        <div class="module-header mt-30 mb-20">
            <h3>流量众筹</h3>
        </div>

        <div class="tools row">
            <div class="col-sm-2">
                <a href="${contextPath}/manage/crowdFunding/create.html?${_csrf.parameterName}=${_csrf.token}">
                    <button class="btn btn-danger"><i class="fa fa-plus mr-5"></i>新建流量众筹</button>
                </a>
            </div>
            
            <div class="col-sm-10 dataTables_filter text-right">
                <div class="form-inline">
                
                  	<div class="form-group mr-10">
                    	<label for="name">活动名称：</label>
                    	<input type="text" class="form-control searchItem" id="name" name="name" placeholder="">
                	</div>

                	<div class="form-group mr-10 form-group-sm">
                    	<label>状态：</label>
                    	<div class="btn-group btn-group-sm">
                        	<input style="width: 0; font-size: 0; height: 0; opacity: 0" class="form-control searchItem"
                               	name="status" value="">
                        	<button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        	<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            	<span class="caret"></span>
                            	<span class="sr-only">Toggle Dropdown</span>
                        	</button>
                    		<ul class="dropdown-menu">
                        		<li data-value=""><a href="#">全部</a></li>
                        		<li data-value="0,7"><a href="#">已保存</a></li>
                        		<li data-value="5"><a href="#">审批中</a></li>
                        		<li data-value="6"><a href="#">审批结束</a></li>
                        		<li data-value="1,2"><a href="#">进行中</a></li>
                        		<li data-value="3,4"><a href="#">已结束</a></li>
                    		</ul>
                    	</div>
                	</div>
                
                	<div class="form-group" id="search-time-range">
                    	<label>创建时间：</label>
                    	<input type="text" style="width:110px" class="form-control search-startTime searchItem" name="startTime"
                           	value="${(pageResult.queryObject.queryCriterias.beginTime)!}" id="startTime" placeholder="">&nbsp;~&nbsp;
                    	<input type="text" style="width:110px" class="form-control search-endTime searchItem" name="endTime"
                           	id="endTime" value="${(pageResult.queryObject.queryCriterias.endTime)!}" placeholder="">
                	</div>
                
                    <a type="submit" class="btn btn-warning" id="search-btn">确定</a>
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

    <div class="modal fade dialog-sm" id="subject-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content" id="tips">提交审批成功</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
    <script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
    <!--[endif]-->
    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>
    <script>
        var statusFormat = function(value, column, row){
            if (row.status == 0 || row.status == 7) {
                return "已保存";
            }
            if (row.status == 1 || row.status == 2) {
                return "进行中";
            }
            if (row.status == 3 || row.status == 4) {
                return "已结束";
            }
            if (row.status == 5) {
                return "审批中";
            }
            if (row.status == 6) {
                return "审批结束";
            }
        };

        var successFormat = function(value, column, row){
            if(row.crowdFundingResult==0){
                //return "众筹中";
                return "否";
            }
            if(row.crowdFundingResult==1){
                //return "成功";
                return "是";
            }
            if(row.crowdFundingResult==2){
                //return "失败";
                return "否";
            }
        };

        var opFormat = function(value, column, row){
        	var approvalFlag = "${approvalFlag!}";
        	
        	if (row.status == 0 || row.status == 7) {
	            if(approvalFlag=="true"){
	                return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/crowdFunding/edit.html?activityId="+row.activityId+"'>编辑</a>",
	                    "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/crowdFunding/detail.html?activityId="+row.activityId+"'>详情</a>",
	                    '<a class="btn-icon icon-detail mr-5" href="javascript:submitActivityApproval(\''+row.activityId+'\')">提交审批</a>'];
	            }
	            else{
	                return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/crowdFunding/edit.html?activityId="+row.activityId+"'>编辑</a>",
	                    "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/crowdFunding/detail.html?activityId="+row.activityId+"'>详情</a>",
	                    "<a href='javascript:void(0)' class='btn-icon icon-online mr-5' onclick=onShelf('" + row.activityId + "')>上架</a>"];
	            }
	        }
	        else if (row.status == 6) {
	            return ["<a href='javascript:void(0)' class='btn-icon icon-online mr-5' onclick=onShelf('" + row.activityId + "')>上架</a>",
	                "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/crowdFunding/detail.html?activityId="+row.activityId+"'>详情</a>"];
	        }
	        else if (row.status == 1 || row.status == 2) {
	            return ["<a href='javascript:void(0)' class='btn-icon icon-down mr-5' onclick=offShelf('" + row.activityId + "')>下架</a>",
	                "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/crowdFunding/detail.html?activityId="+row.activityId+"'>详情</a>"];
	        }
	        else {
	            return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/crowdFunding/detail.html?activityId="+row.activityId+"'>详情</a>"];
	        }
        	
        	<#--
            return ['<a class="btn-icon icon-detail mr-5" href="javascript:submitActivityApproval(\''+row.activityId+'\')">提交审批</a>',
                "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/crowdFunding/detail.html?activityId="+row.activityId+"'>详情</a>",
                "<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/crowdFunding/edit.html?activityId="+row.activityId+"'>编辑</a>",

                "<a href='javascript:void(0)' class='btn-icon icon-down mr-5' onclick=offShelf('" + row.activityId + "')>下架</a>"];
                -->
        };

        /**
         * 提交审批请求
         * */
        function submitActivityApproval(activityId) {
            $("#submit-dialog").modal("show");
            $("#sure").data("activityId", activityId);
        };
        
        function onShelf(activityId) {
	        $.ajax({
	            beforeSend: function (request) {
	                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
	                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
	                request.setRequestHeader(header1, token1);
	            },
	            type: "post",
	            url: "${contextPath}/manage/crowdFunding/onshelf.html",
	            data: {
	                activityId: activityId
	            },
	            dataType: "json", //指定服务器的数据返回类型，
	            success: function (data) {
	                var message = data.message;
	                var result = data.result;
	                var errorMsg = data.errorMsg;
	                if(errorMsg!=null && errorMsg!=""){//企业同一时间只能上架一个众筹活动的判断
	                    showSubjectDialog(data.errorMsg);
	                }else{
    	                $("#tips").html(message);
    	                $("#subject-dialog").modal("show");
	                }
	            },
	            error: function () {
	                var message = "网络出错，请重新尝试";
	                $("#tips").html(message);
	                $("#subject-dialog").modal("show");
	            }
	        });
    	}
        
        function offShelf(activityId) {
	        $.ajax({
	            beforeSend: function (request) {
	                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
	                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
	                request.setRequestHeader(header1, token1);
	            },
	            type: "post",
	            url: "${contextPath}/manage/crowdFunding/offshelf.html",
	            data: {
	                activityId: activityId
	            },
	            dataType: "json", //指定服务器的数据返回类型，
	            success: function (data) {
	                var result = data.message;
	                if (result == 'true') {
	                    $("#tips").html("下架成功!");
	                }
	                else {
	                    $("#tips").html("下架失败!");
	                }
	                $("#subject-dialog").modal("show");
	            },
	            error: function () {
	                var message = "网络出错，请重新尝试";
	                $("#tips").html(message);
	                $("#subject-dialog").modal("show");
	            }
	        });
    	}

        function init() {
            $("#sure").on("click", function () {
                var activityId = $("#sure").data("activityId");

                var currentId = "${currUserId}";
                var roleId = "${roleId}";

                $.ajax({
                    beforeSend: function (request) {
                        var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                        var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                        request.setRequestHeader(header1, token1);
                    },
                    url: "${contextPath}/manage/crowdFunding/submitActivityApprovalAjax.html",
                    data: {
                        activityId: activityId,
                        currentId: currentId,
                        roleId: roleId
                    },
                    type: "post",
                    dataType: "json"
                }).success(function (ret) {
                    if(ret){
                        if(ret.submitRes == "success"){
                            $("#subject-dialog").modal("show");
                        }
                        else{
                            if(ret.errorMsg){
                                showSubjectDialog(ret.errorMsg);
                            }
                            else{
                                showSubjectDialog("提交审批失败!");
                            }
                        }
                    }
                });
            });
        }

        /**
         * 显示提示信息
         */
        function showSubjectDialog(msg) {
            $("#subject-dialog .message-content").html(msg);
            $("#subject-dialog").modal("show");
        }

        var action = "${contextPath}/manage/crowdFunding/search.html?${_csrf.parameterName}=${_csrf.token}";
        var columns = [{name: "name", text: "活动名称",tip: true},
            <#--{name: "entName", text: "企业名称",tip: true}, -->
            {name: "startTime", text: "开始时间", format: "DateTimeFormat"}, 
            {name: "endTime", text: "结束时间", format: "DateTimeFormat"}, 
            {name: "status", text: "状态", format: statusFormat},
            {name: "currentCount", text: "众筹成功人数"},
            {name: "crowdFundingResult", text: "众筹是否成功", format: successFormat},
            {name: "createTime", text: "创建时间", format: "DateTimeFormat"}, 
            {name: "op", text: "操作", format: opFormat}];

        var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

        require(["common", "bootstrap", "page/list", "daterangepicker"], function() {
            initSearchDateRangePicker();

            init();

            $("#subject-btn").on("click", function () {
                window.location.reload();
            });
        });

        function initSearchDateRangePicker(){
            var ele = $('#search-time-range');

            var startEle = $('#startTime');
            var endEle = $('#endTime');

            ele.dateRangePicker({
                separator: ' ~ ',
                format: 'YYYY-MM-DD HH:mm:ss',
                endDate: new Date(),
                time: {
                    enabled: true
                },
                getValue: function () {
                    if (startEle.val() && endEle.val() )
                        return startEle.val() + ' ~ ' + endEle.val();
                    else
                        return '';
                },
                setValue: function (s, s1, s2) {
                    startEle.val(s1);
                    endEle.val(s2);
                }


            });
            console.log($('#startTime').val());

        }
    </script>
</body>
</html>
