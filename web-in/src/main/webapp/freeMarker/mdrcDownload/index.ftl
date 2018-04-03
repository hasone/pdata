<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-营销卡记录列表</title>
    <meta name="keywords" content="流量平台营销卡记录列表"/>
    <meta name="description" content="流量平台营销卡记录列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">

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
        
        .icon-download {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-download.png);
        }
        
        .icon-syxg {
            color: #708090;
            background-image: url(${contextPath}/assets/imgs/icon-syxg.png);
        }

        .levelChart canvas {
            position: absolute;
            top: 0;
            left: 0;
        }

    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/Js/artDialog/skins/default.css">
    <script src="${contextPath}/manage2/Js/artDialog/artDialog.js"></script>
    <script src="${contextPath}/manage2/Js/artDialog/jquery.artDialog.js"></script>


</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>卡数据列表</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">

        </div>

        <div class="col-lg-10 dataTables_filter text-right">
            <div class="form-inline">
                <div class="form-group mr-10 form-group-sm">
                    <div class="form-group mr-10 form-group-sm">
                        <label for="name">卡批次号：</label>
                        <input type="text" class="form-control searchItem" id="serialNumber" name="serialNumber">
                    </div>

                    <div class="form-group mr-20 form-group-sm">
                        <label>状态：</label>
                        <div class="btn-group btn-group-sm">
                            <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="searchItem"
                                   name="status">
                            <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="#">全部</a></li>
                                <li data-value="1"><a href="#">新制卡</a></li>
                                <li data-value="2"><a href="#">制卡中</a></li>
                                <li data-value="5"><a href="#">已邮寄</a></li>
                                <li data-value="3"><a href="#">已签收</a></li>
                                <li data-value="6"><a href="#">已失效</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <button id="search-btn" class="btn btn-sm btn-warning">确定</button>
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
    
    <div class="modal fade dialog-sm" id="tips-dialog">
    	<div class="modal-dialog">
        	<div class="modal-content bd-muted">
	            <div class="modal-header bg-muted bd-muted">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
	                        aria-hidden="true">&times;</span></button>
	                <h5 class="modal-title">提示</h5>
	            </div>
	            <div class="modal-body">
	                <span class="message-icon message-icon-info"></span>
	                <span class="message-content" id="tips"></span>
	            </div>
	            <div class="modal-footer">
	                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
	            </div>
        	</div><!-- /.modal-content -->
    	</div><!-- /.modal-dialog -->
	</div>
	
	<div class="modal fade dialog-sm" id="download-fail-dialog">
    	<div class="modal-dialog">
        	<div class="modal-content bd-muted">
	            <div class="modal-header bg-muted bd-muted">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
	                        aria-hidden="true">&times;</span></button>
	                <h5 class="modal-title">下载失败提示</h5>
	            </div>
	            <div class="modal-body">
	                <span class="message-icon message-icon-info"></span>
	                <span class="message-content" id="downloadFailTip">
						若确定下载失败，卡数据将失效，确定操作？
	                </span>
	            </div>
	            <div class="modal-footer">
	            	<button class="btn btn-warning btn-sm" data-dismiss="modal" id="downloadFail">确 定</button>
	                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
	            </div>
        	</div><!-- /.modal-content -->
    	</div><!-- /.modal-dialog -->
	</div>
	
	<div class="modal fade dialog-sm" id="tracking-number-dialog">
    	<div class="modal-dialog">
        	<div class="modal-content bd-muted">
	            <div class="modal-header bg-muted bd-muted">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
	                        aria-hidden="true">&times;</span></button>
	                <h5 class="modal-title">物流信息</h5>
	            </div>
	            <div class="modal-body">
	                <span class="message-icon message-icon-info"></span>
	                <span class="message-content" id="trackingNumberTip" style="word-break: break-all;"></span>
	            </div>
	            <div class="modal-footer">
	                <button class="btn btn-warning btn-sm" data-dismiss="modal">确  定</button>
	            </div>
        	</div><!-- /.modal-content -->
    	</div><!-- /.modal-dialog -->
	</div>
	
	
	<div class="modal fade dialog-sm" id="download-dialog">
    	<div class="modal-dialog">
        	<div class="modal-content bd-muted">
	            <div class="modal-header bg-muted bd-muted">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
	                        aria-hidden="true">&times;</span></button>
	                <h5 class="modal-title">提示</h5>
	            </div>
	            <div class="modal-body">
	                <span class="message-icon message-icon-info"></span>
	                <span class="message-content">
	                	1、下载成功：请将下载数据解密后制卡，制卡完毕可进行发货；</br>
						2、下载失败：请及时与卡平台管理员联系。
					</span>
	            </div>
	            <div class="modal-footer text-center">
	            	<button class="btn btn-warning btn-sm" data-dismiss="modal" id="download">确 定</button>
	                <#-- <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button> -->
	            </div>
        	</div><!-- /.modal-content -->
    	</div><!-- /.modal-dialog -->
	</div>

    <div id="dialogContent" style="display:none;width:600px;">
        <div>
            <font style="font-weight:bold;font-size:14px;">正在为您准备数据，请稍候。。。</font>
            <img src="${contextPath}/manage2/assets/images/load-16-16.gif"/>
        </div>
    </div>
</div>


<!-- loading -->
<div id="loadingToast" class="weui_loading_toast">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <div class="weui_loading">
            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div><!-- loading end -->

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", "page/list"], function () {
    	 $("#download").on("click", function () {
    	 	window.location.reload();
    	 });
    	 
    	 $("#downloadFail").on("click", function () {
    	 	downloadFail();
    	 });
    });
    
    //下载失败
    function downloadFail(){
    	var configId = $("#downloadFailTip").val();
    	$.ajax({
            beforeSend: function (request) {
                showToast();
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "post",
            data: {
                configId: configId
            },
            dataType: "json", //指定服务器的数据返回类型，
            url: '${contextPath}/manage/mdrc/download/downloadFailAjax.html',
            success: function (data) {
            	if(data.result && data.result == "true"){
            		window.location.reload();
            	}
            },
            complete: function(){
                hideToast();
            }
        });
    }

    function showToast() {
        $('#loadingToast').modal('show');
    }

    function hideToast() {
        window.setTimeout(function(){
            $('#loadingToast').modal('hide');
        }, 500);
    }
    
    //物流信息弹框
    function trackingNumberInfo(id){
    	$.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "post",
            data: {
                configId: id
            },
            dataType: "json", //指定服务器的数据返回类型，
            url: '${contextPath}/manage/mdrc/download/getTrackingNumberAjax.html',
            success: function (data) {
            	if(data.result && data.result=="true"){
            		var msg = "公司名称：顺丰速运</br>快递单号："+ data.trackingNumber;
            		$("#trackingNumberTip").html(msg);
            	}else{
            		$("#trackingNumberTip").html("未查询到物流信息!");
            	}
            	$("#tracking-number-dialog").modal("show");
            }
        });
    }
    
    function handleDownloadFail(id){
    	$("#downloadFailTip").val(id);
    	$("#download-fail-dialog").modal("show");
    }
    
    function doDownload(id){
    	//首先弹框 哈哈哈哈
    	$("#download-dialog").modal("show");
    	
    	location.href = "${contextPath}/manage/mdrc/download/file.html?configId="+id;
    	
    	<#--
	    $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "post",
            data: {
                configId: id
            },
            dataType: "json", //指定服务器的数据返回类型，
            url: '${contextPath}/manage/mdrc/download/file.html',
            success: function (data) {
             	if(data.result && data.result=="false"){
             		$("#tips").html("下载异常，请稍后再试!");
             		$("#tips-dialog").modal("show");
             	}
            }
        });
        -->
    }

    function downloadFile(ele) {
        var dialog = art.dialog({
            lock: true,
            title: '下载数据',
            content: $('#dialogContent')[0],
            drag: false,
            resize: false,
            cancel: function () {
                location.reload();
                return true;
            },
            cancelVal: "关闭"
        });


        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: '${contextPath}/manage/mdrc/download/listFile.html?configId=' + $(ele).attr('configid') + '&templateId=' + $(ele).attr('templateId'),
            timeout: 600000,
            success: function (data) {
                $('#dialogContent').html(data);
            },
            cache: false
        });
    }

    function Delete() {
        var i = window.confirm("确定要删除吗？");
        return i;
    }

    var buttonsFormat = function (value, column, row) {
        if (row.cardmakeStatus == 2 && row.status == 1) {
            return [
                "<a class='btn-icon mr-5 icon-download' onclick = 'doDownload("+ row.id +")'>下载卡数据</a>"
            ];
        } 
        if(row.status == 2){
        	return [
                "<a class='btn-icon mr-5 icon-syxg' href='${contextPath}/manage/mdrc/download/fillInTrackingNumber.html?configId=" + row.id + "'>发货</a>",
                "<a class='btn-icon mr-5 icon-download' onclick = 'handleDownloadFail("+ row.id +")' >下载失败</a>"
            ];
        }  
        if(row.status == 6){
        	return "";
        }  
        if(row.status == 3 || row.status == 5){
        	return [
                "<a class='btn-icon mr-5 icon-detail' onclick = 'trackingNumberInfo("+ row.id +")' >物流信息</a>"
            ];
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
    };

    var statusFormator = function (value, column, row) {
        if (row.status == 1) {
            return "新制卡";
        }
        if (row.status == 2) {
            return "制卡中";
        }
        if (row.status == 5) {
            return "已邮寄";
        }
        if (row.status == 3) {
            return "已签收";
        }
        if (row.status == 6) {
            return "已失效";
        }
    };
	
	var serialNumberFormat = function (value, column, row) {
        if (row.id != null) {
        	return "<a href='${contextPath}/manage/mdrc/download/getConfigDetail.html?configId="
                   + row.id + "'>" + row.serialNumber + "</a>";
        } else {
            return "";
        }
    };
    
    var columns = [
    	{name: "configName", text: "卡名称", tip: true},
        {name: "serialNumber", text: "卡批次号", format: serialNumberFormat},
        {name: "amount", text: "制卡数量"},
        {name: "status", text: "状态", format: statusFormator},
        {name: "op", text: "操作", format: buttonsFormat},
        {name: "downloadTime", text: "下载时间", format: dateFormator},
        {name: "createTime", text: "创建时间", format: dateFormator}
    ];
    var action = "${contextPath}/manage/mdrc/download/search.html?${_csrf.parameterName}=${_csrf.token}";
</script>
</body>
</html>