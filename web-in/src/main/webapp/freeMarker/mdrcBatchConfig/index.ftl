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
    <title>流量平台-卡数据列表</title>
    <meta name="keywords" content="流量平台卡数据列表"/>
    <meta name="description" content="流量平台卡数据列表	"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-active {
            color: #2aa515;
            background-image: url(${contextPath}/assets/imgs/icon-active.png);
        }

        .icon-list {
            color: #f89e52;
            background-image: url(${contextPath}/assets/imgs/icon-list.png);
        }

        .table tbody > tr > td:nth-child(7), .table tbody > tr > td:nth-child(7) *, .table thead > tr > th:nth-child(7){
            max-width: 10000px;
            overflow: visible;
        }

        .table tbody > tr > td:last-child {
            max-width: 100%;
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
   <#--
       <#if isCustomer?? && isCustomer!="true">
           <a href="${contextPath}/manage/mdrc/batchconfig/addOrEdit.html" class="btn btn-sm btn-danger">
               <i class="fa fa-plus mr-5"></i>新建卡数据
           </a>
       </#if>
        -->
   </div>


        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">
                <div class="form-group mr-10 form-group-sm">
                    <input type="hidden" name="configId" class="searchItem" value="${configId!}"/>
                    <input type="hidden" name="year" class="searchItem" value="${year!}"/>

                    <div class="form-group mr-10 form-group-sm">
                        <label for="name">企业名称：</label>
                        <input type="text" class="form-control searchItem" id="enterpriseName" name="enterpriseName"
                               value="">
                    </div>
                    
                    <div class="form-group mr-10 form-group-sm">
                        <label for="name">卡批次号：</label>
                        <input type="text" class="form-control searchItem" id="serialNumber" name="serialNumber"
                               value="">
                    </div>

                    <div class="form-group mr-20 form-group-sm">
                        <label>状态：</label>
                        <div class="btn-group btn-group-sm">
                            <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="searchItem"
                                   name="status" value="">
                            <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                            	<li data-value=""><a href="#">全部</a></li>
                            	<li data-value="1"><a href="#">新制卡</a></li>
                            	<li data-value="2"><a href="#">制卡中</a></li>
                            	<li data-value="3"><a href="#">已签收</a></li>
                            	<li data-value="5"><a href="#">已邮寄</a></li>
                            	<li data-value="6"><a href="#">已失效</a></li>

                            	<#--
                            	<#if mdrcBatchConfigStatus?? && mdrcBatchConfigStatus?size!=0>
                                	<#list mdrcBatchConfigStatus?keys as key>
                                    	<li data-value="${key!}"><a href="#">${(mdrcBatchConfigStatus[key])!}</a></li>
                                    </#list>
                                </#if>
                                -->
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
                    <span class="message-content" id="dialog-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

    <div id="dialogContent" style="display:none;width:600px;">
        <div>
            <font style="font-weight:bold;font-size:14px;">短信发送中。。。</font>
            <img src="${contextPath}/manage2/assets/images/load-16-16.gif"/>
        </div>
    </div>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
	var hasMdrcActiveAuth ="${(MdrcActiveAuth)!'FALSE'}";
	var hasMdrcReceiveAuth = "${(MdrcReceiveAuth)!'FALSE'}";
    var configStatus = {};//规则状态
    <#list mdrcBatchConfigStatus?keys as item>
    	configStatus["${item}"] = "${mdrcBatchConfigStatus[item]}";
    </#list>

    require(["common", "bootstrap", , "page/list"], function () {
    	//事件监听
    });

    var buttonsFormat = function (value, column, row) {
        if (row.status && row.status == 5 && hasMdrcReceiveAuth == "true") {//卡批次是已邮寄且当前用户有签收权限，则显示操作“签收”
            return [
            	"<a class='btn-icon icon-edit mr-5' href = 'javascript:void(0);' allowClick='true' configId= '" + row.id + "' onclick ='receive(this)' title=''>签收</a>",
                "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/batchconfig/detail.html?id=" + row.id + "' title=''>批次详情</a>",
                "<a class='btn-icon icon-list mr-5' href='${contextPath}/manage/mdrc/cardinfo/index.html?configId=" + row.id + "&year=" + row.thisYear.substring(2, 4) + "&currentCardStatus=" + row.status + "'>卡列表</a>"
            ];
        } else if(row.status && row.status == 3 && (hasMdrcActiveAuth == "true")) {//卡批次是已签收，且当前用户有激活申请权限，则显示操作“激活申请”
            return [
            	"<a href='javascript:void(0)' class='btn-icon icon-active mr-5' configId=" + row.id + " id='active-btn' onclick='getConfigDetailsAjax(" + row.id + ")'>激活申请</a>",
                "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/batchconfig/detail.html?id=" + row.id + "' title=''>批次详情</a>",
                "<a class='btn-icon icon-list mr-5' href='${contextPath}/manage/mdrc/cardinfo/index.html?configId=" + row.id + "&year=" + row.thisYear.substring(2, 4) + "&currentCardStatus=" + row.status + "'>卡列表</a>"
            ];  
        } else {
            return [
                "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/batchconfig/detail.html?id=" + row.id + "' title=''>批次详情</a>",
                "<a class='btn-icon icon-list mr-5' href='${contextPath}/manage/mdrc/cardinfo/index.html?configId=" + row.id + "&year=" + row.thisYear.substring(2, 4) + "&currentCardStatus=" + row.status + "'>卡列表</a>"
            ];
        }
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
    };

    var stringValue = function (value) {
        if (value) {
            return value.toString();
        }
        return "";
    };

    var statusFormat = function (value, column, row) {
        return configStatus[row.status];
    };
    
    var productSizeFormator = function (value, column, row){
    	if(value){
    		if(value < 1024){
    			return value + "KB";
    		}else if(value>=1024 && value<1024*1024){
    			return value/1024 + "MB";
    		}else{
    			return value/1024/1024 + "GB";
    		}
    	}else{
    		return "-";
    	}
    };

    var columns = [
        {name: "enterpriseName", text: "企业名称", tip: true},
        {name: "configName", text: "卡名称", tip: true},
        {name: "serialNumber", text: "卡批次号", tip: true},
        {name: "amount", text: "制卡数量", format: stringValue},
        {name: "productSize", text: "流量大小", format: productSizeFormator},
        {name: "status", text: "批次状态", format: statusFormat},
        {name: "op", text: "操作", format: buttonsFormat},
        {name: "createTime", text: "创建时间", format: dateFormator}
    ];
    var action = "${contextPath}/manage/mdrc/batchconfig/search.html?${_csrf.parameterName}=${_csrf.token}";
   
    
    //根据规则id获取详细信息
    function getConfigDetailsAjax(configId) {
        if(configId){
            $.ajax({
                type: "POST",
                data: {
                    configId: configId
                },
                url: "${contextPath}/manage/mdrc/active/getConfigDetailsAjax.html?${_csrf.parameterName}=${_csrf.token}",
                dataType: "json", //指定服务器的数据返回类型，
                success: function (res) {
                	if (res.result && res.result == "success") {
	  					window.location.href = "${contextPath}/manage/mdrc/active/create.html?configId=" + configId;              	
                	}else{
                		$("#dialog-content").html(res.errorMsg);
                		$("#tip-dialog").modal("show");
                	}                
                }
            });
        }
    }
    
    
   function receive(ele){
    	var reviceButton = $(ele).attr("allowClick");    	
    	if(reviceButton == "true"){
    		$(ele).attr("allowClick","false"); 
    		window.location.href = "${contextPath}/manage/mdrc/batchconfig/receive.html?id=" + $(ele).attr("configId"); 		
    	}    	 	
    }
</script>
</body>
</html>