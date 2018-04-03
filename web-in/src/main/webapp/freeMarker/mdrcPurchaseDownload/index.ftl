<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<!DOCTYPE html>
<html lang="en">
	<head>
	    <meta charset="UTF-8">
	    <meta name="renderer" content="webkit">
	    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
	    <title>流量平台-营销卡记录列表</title>
	    <meta name="keywords" content="流量平台营销卡记录列表" />
	    <meta name="description" content="流量平台营销卡记录列表" />
	
	    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
	    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
	    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
	    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
	    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
	    <style>
	        .icon-detail { color: #ca95db;background-image: url(${contextPath}/assets/imgs/icon-detail.png); }
	        .icon-edit { color: #5faee3;background-image: url(${contextPath}/assets/imgs/icon-edit.png); }
	        .icon-del { color: #fa8564;background-image: url(${contextPath}/assets/imgs/icon-del.png); }

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
            	<h3>营销卡记录列表</h3>
        	</div>
		
			<div class="tools row">
	            <div class="col-sm-2">	            

	            </div>
	            
	          	<div class="col-lg-10 dataTables_filter text-right">
	                <div class="form-inline" >
	                    <div class="form-group mr-10 form-group-sm">
	               			<div class="form-group mr-10 form-group-sm">
	                        	<label for="name">卡名称：</label>
	                        	<input type="text" class="form-control searchItem" id="configName" name="configName">
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
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
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
	    
	<div id="dialogContent" style="display:none;width:600px;">
		<div>
			<font style="font-weight:bold;font-size:14px;">正在为您准备数据，请稍候。。。</font>
			<img src="${contextPath}/manage2/assets/images/load-16-16.gif" />
		</div>
	</div>
				
	</div>
	
	<script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>
    <script>        
        require(["common", "bootstrap","page/list"], function() {
        });                       
    </script>
    <script>
    
    var  configStatus = {};
   	<#list mdrcBatchConfigStatus?keys as item>
    	configStatus["${item}"] = "${mdrcBatchConfigStatus[item]}";
	</#list>
    
    function downloadFile(ele){
		var dialog = art.dialog({
			lock: true,
		    title: '下载数据',
		    content: $('#dialogContent')[0],
		    drag: false,
		    resize: false,
		    cancel: function() {
		    	location.reload();
		    	return true;
		    },
		    cancelVal: "关闭"
		});
		
		
		
		$.ajax({beforeSend: function(request) {var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");request.setRequestHeader(header1, token1);},
			url:'${contextPath}/manage/mdrc/purchaseDownload/listFile.html?configId=' + $(ele).attr('configid') + '&templateId=' + $(ele).attr('templateId'),
            timeout: 300000,
            success: function(data) {
				$('#dialogContent').html(data);
			},
			cache: false
		});   
    }
        
	function Delete(){
		var i=window.confirm("确定要删除吗？"); 
		return i;
	}
	</script>
	
	<script>		
        var buttonsFormat = function(value, column, row){
        	
	        	return [			
					"<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/purchaseDownload/listFile.html' configid=" + row.id + " templateId=" + row.templateId + " onclick='downloadFile(this);return false;'>下载</a>"
				];	       														
        }
       
        function dateFormat( date, fmt) {
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

        var dateFormator = function (value){
        	if(value){
        		return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        	}
        	return "";
        }
        
      var stringValue = function (value){
        	if(value){
        		return value.toString();
        	}else{
        		return "";
        	}
        	
        }
        		
    	var columns = [
    		{name: "configName", text: "卡名称", tip: true},
    		{name: "amount", text: "记录条数",format: stringValue},
    		{name: "createTime", text: "创建时间", format: dateFormator},          
            {name: "op", text: "操作", format: buttonsFormat}   
        ];            
		var action = "${contextPath}/manage/mdrc/purchaseDownload/search.html";
    </script>		
	</body>
</html>