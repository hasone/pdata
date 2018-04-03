<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-EC信息编辑</title>
    <meta name="keywords" content="流量平台 EC信息编辑"/>
    <meta name="description" content="流量平台 EC信息编辑"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form .form-group label {
            width: 150px;
            text-align: right;
            margin-left: 15px;
            vertical-align:top;
        }
        .form .form-group select {
           border: 1px #E3E9EF solid;
           margin-left: 20px;
           height: 35px;
           vertical-align:top;
        }
        .form .form-group textarea {
           border: 1px #E3E9EF solid;
        }
        .ipInput-border {
            display: inline-block;
            border: 1px #E3E9EF solid;
            width: 172px;
            font-size: 9pt;
            margin-left: 20px;
            border-radius: 2px;
        }

        .ipInput-cell {
            width: 40px;
            height: 34px;
            border-width: 0px;
            border-style: none;
            text-align: center;
        }
        .url-head {
        	display:inline-block;
        	vertical-align:top;
        	text-align: middle;
        	line-height:35px;
        	height:35px;
        }

    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>EC信息编辑
        <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:history.go(-1)">返回</a>
        </h3>
    </div>
    <form>
        <div class="tile mt-30">
            <div class="tile-header">
                EC信息编辑
            </div>

            <div class="tile-content">
            	<div class="form-group" style="height:50px">
			        <span style="color:blue;text-align: right;margin-left: 15px; ">请将企业的EC信息填写至下方并提交审批</span>
			    </div>
            	
                <div class="row form">
                    <input type="hidden" name="entId" id="entId" value="${enterprise.id!}"/>
			        <div class="form-group">
			        	<label>企业IP白名单地址：</label>
			            <div class="ip_div" required></div>
			        </div>
			        <div class="form-group">
			        	<label></label>
			            <div class="ip_div"></div>
			            <span>(可选)</span>			            
			        </div>
			        <div class="form-group">
			        	<label></label>
			            <div class="ip_div"></div>
			            <span>(可选)</span>		            
			        </div>
			        

                    <div class="form-group">
                        <label>企业回调地址：</label>
                        <select id="head">
                        	<option <#if urlHead?? && urlHead == "http">selected</#if>>http</option>
                        	<option <#if urlHead?? && urlHead == "https">selected</#if>>https</option>
                        </select>
                        <span class="url-head">://</span><input value="${urlBody!}" maxlength="500" name="callbackUrl" id="callbackUrl" style="height: 32px; width: 500px; border: 1px #E3E9EF solid;">
                    </div>
                    
                    <div class="form-group">
				        <span style="color:blue;text-align: right;margin-left: 270px;">请填写EC接口对接使用的正式环境回调URL地址</span>
				        <span> (不超过500个字符)</span>
			        </div>
			        
			        <div class="btn-save mt-30" id="buttons2">
			            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="refuse-btn">取消</a>
			            <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn">提交审批</a>&nbsp;&nbsp;&nbsp;&nbsp;
			            <span id="error" style='color:red'>${errorMsg!}</span>
        			</div>
                </div>
            </div>
        </div>
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
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
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
        <p class="weui_toast_content">更新中</p>
    </div>
</div>
<!-- loading end -->

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
		init();
    });
</script>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/lib/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/lib/newInput_ip.js"></script>
<script src="${contextPath}/assets/lib/inputSelection.js"></script>
<script type="text/javascript">
	var ips;
    $(function () {
        $('.ip_div').ipInput("IP");  
    });
    
    //ip地址回填
    function init(){
    	var ip1 = "${ip1!}";
    	var ip2 = "${ip2!}";
    	var ip3 = "${ip3!}";
    	var index = 1;
    	$('.ip_div').each(function(){
    		if(index==1 && (ip1!=null || ip1!="")){
	    		var s = ip1.split(".");
	    		if (s.length == 4 ) {
	    			var i = 0;		    		
			     	$("input", this).each(function(){ 		  	     		
			     		$(this).val(s[i]);
			     		i++;     		
			     	});
			     }
			 }   	
			if(index==2 && (ip2!=null || ip2!="")){
	    		var s = ip2.split(".");
	    		if (s.length == 4 ) {
	    			var i = 0;		    		
			     	$("input", this).each(function(){ 		  	     		
			     		$(this).val(s[i]);
			     		i++;     		
			     	});
			     }
			 }
			 if(index==3 && (ip3!=null || ip3!="")){
	    		var s = ip3.split(".");
	    		if (s.length == 4 ) {
	    			var i = 0;		    		
			     	$("input", this).each(function(){ 		  	     		
			     		$(this).val(s[i]);
			     		i++;     		
			     	});
			     }
			 }
			 index++;
    	 });
    }
    
    $("#refuse-btn").on("click", function (){  
    	window.location.href = "${contextPath}/manage/enterprise/index.html";
    })

    $("#save-btn").on("click",function(){
    	$("#error").html("");
		if(checkIps()){
    		$("#submit-dialog").modal("show");
    	}else{
    		$("#error").html("请填写合法的IP白名单地址!");
    	}
    });
    
    function checkIps(){
    	var result = true;
    	ips = new Array();
    	var i = 1;
    	$('.ip_div').each(function(){    		
	     	var ip = [];
	     	var ipStr;
	     	$("input", this).each(function(){	     		  	     		
	     		ip.push($(this).val());
	     		ipStr = ip.join(".");	     		
	     	});	
     		if(i == 1){     			
     			if(ipStr == "..." || !chkIP(ipStr)){     				
     				result = false;
     				return false;
     				$("#error").html("请填写合法的IP白名单地址！");
     			}
     		}else{
     			if(ipStr!="..." && !chkIP(ipStr)){
     				$("#error").html("请填写合法的IP白名单地址！");
     				result = false;
     				return false;
     			}
     		}
     		ips.push(ipStr);
     		i++;
	     	
     	});
     	return result;
    }
    
    
    function chkIP(ipStr){
		//参数格式校验   成功继续,失败返回
		ipStr = ipStr.replace(/\s/g, " ");
		var reg = /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;
		if(ipStr.match(reg) == false){
			return false;
		}
		//ip地址合法性校验   成功继续   ,失败返回
		var arr = ipStr.split( ".");
		for(var i=0;i <4;i++){
			if(arr[i] == null || arr[i] == ""){
				return false;
			}
			var str = arr[i];
			if(i == 0 && str.charAt(0) == '0'){
				return false;
			}
			if(str.length>1 && str.charAt(0) == '0'){
				return false;
			}
			arr[i] =  parseInt(arr[i],10);
			if(arr[i] == null || parseInt(arr[i],10) > 255){
				return false;
			}			
		}
		return true;
	}
    
    $("#sure").on("click", function (){    	
		var ipString = JSON.stringify(ips);
		var entId = $("#entId").val();
		var urlBody = $("#callbackUrl").val();
		var callbackUrl = "";
		if(urlBody!=null && urlBody.trim().length>0){
			callbackUrl = callbackUrl + $('#head option:selected') .val() + "://" + urlBody;
		}
 		$.ajax({
            type: "POST",
            url: "${contextPath}/manage/approval/submitEcInfoAjax.html?${_csrf.parameterName}=${_csrf.token}",
            data: {
                ipString: ipString,
                entId: entId,
                callbackUrl: callbackUrl,
                interfaceFlag: ${enterprise.interfaceFlag!}             
            },
            timeout: 300000, //超时时间：30秒
            dataType: "json", //指定服务器的数据返回类型，
            success: function (res) {
                if (res.msg && res.msg == "success") {
                	hideToast();
                	showTipDialog("企业EC信息提交审批成功，请耐心等待审批，谢谢!");	                         
                }else{
                	hideToast();
                	showTipDialog(res.msg);            	
                }                       
            },
            error: function () {
            	hideToast();
            	showTipDialog("提交失败!");
            }
        });
    });

	$("#ok").on("click",function(){
    	window.location.href = "${contextPath}/manage/enterprise/index.html";   	
    });
    
    
	
</script>
</body>
</html>