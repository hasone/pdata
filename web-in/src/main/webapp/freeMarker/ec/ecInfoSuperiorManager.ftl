<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-EC信息</title>
    <meta name="keywords" content="流量平台 EC信息"/>
    <meta name="description" content="流量平台 EC信息"/>

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
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>EC信息
        	<a class="btn btn-primary btn-sm pull-right btn-icon icon-back"  onclick="javascript:window.location.href='${contextPath}/manage/enterprise/index.html?back=1'">返回</a>
        </h3>
    </div>
    <div class="tile mt-30">
        <div class="tile-header">
            EC信息
        </div>

        <div class="tile-content">
            <div class="row form">
                <div class="form-group">
                    <label>企业名称：</label>
                    <span>${enterprise.name!}</span>                   
                </div>
            	<div class="form-group">
                    <label>企业编码：</label>
                    <span>${enterprise.code!}</span>                   
                </div>
                <#if entWhiteLists?size==0>
                	<div class="form-group">
                        <label>企业IP白名单地址：</label>                 
                    </div>
                </#if>
                <#list entWhiteLists as whiteList>
                    <div class="form-group">
                        <label>企业IP白名单地址：</label>
                        <span>${whiteList.ipAddress!}</span>                    
                    </div>
                </#list>
                <div class="form-group" style="word-break: break-all;">
                    <label>EC回调地址：</label>
                    <span style="word-break: break-all; display: inline-block;">${callbackAddr!}</span>                   
                </div>                   
                <#if currentUserRoleId?? && (currentUserRoleId==6 || currentUserRoleId==7)>
                    <div class="form-group">
                    	<intput type="hidden" id="status"/>
                        <label>EC状态：</label>
                        <#if enterprise.interfaceFlag == 1>
                        	<span>开通</span>
                        	<#if currentUserRoleId==6 || (currentUserRoleId==7 && closeAndRecordButton ?? && closeAndRecordButton == 'true')>
            					<a class="btn btn-sm btn-warning" id="updateInterface" data-toggle="modal" status="0">关闭</a>
            				</#if>	
                        </#if>
                        <#if enterprise.interfaceFlag == 0 >
                        	<span>关闭</span>
                        	<#if currentUserRoleId==6>
	                          	<a class="btn btn-sm btn-warning" id="updateInterface" data-toggle="modal" status="1">开通</a>
                        	</#if>
                        </#if>
                        <#if currentUserRoleId==6>
	                          <a href="javascript:void(0)" class="btn btn-sm btn-warning" id="ecHistory-btn">操作记录</a>
                        </#if>
                        <#if currentUserRoleId==7 && closeAndRecordButton ?? && closeAndRecordButton == 'true' >
            				<a href="javascript:void(0)" class="btn btn-sm btn-warning" id="ecHistory-btn">操作记录</a>
            			</#if>	
                    </div>
                </#if>
               
                <div class="form-group">
                    <label>企业appkey：</label>
                    <span>${expire!}</span>	                        	                       
                </div>
                <div class="form-group">
                    <label>企业appsecret：</label>
                    <span>${expire!}</span>
                </div>
                
                <div class="btn-save mt-30 text-center" id="buttons2">
            		<a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="history-btn">历史记录</a>
        		</div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="note-dialog" data-backdrop="static">
    <div class="modal-dialog" style="width:525px;">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">备注信息</h5>
            </div>
            <div class="modal-body" style="padding-top:20px;">
                <textarea rows="6" cols="60" maxlength="300" id="opDesc" name="opDesc" class="hasPrompt" style="width: 100%;"></textarea>
                <span class="help-block"><span style="color:red;">*</span>必填：备注信息长度不超过300个字符。</span>
                <span id="open-tip"></span>
             </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="open-ok">确 定</button>
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
<script>
	function init(){
		$("#updateInterface").on("click",function(){
			$("#opDesc").val("");
			$("#open-tip").html("");
			$("#note-dialog").modal('show');
			$("#status").val($(this).attr("status")) ;
		});	
	
		$("#open-ok").on("click", function () {
			$("#open-tip").html("");
			var status = $("#status").val();
			var opDesc = $("#opDesc").val();
			if(isBlank(opDesc)){
				$("#open-tip").html("请输入备注信息").css("color","red");
			}else{
				$("#open-tip").html("");
				$("#note-dialog").modal('hide');
				var entId = "${enterprise.id!}";
				var opDesc = $('#opDesc').val();
	            $.ajax({
	                type: "POST",
	                url: "${contextPath}/manage/enterprise/changeECStatus.html?${_csrf.parameterName}=${_csrf.token}",
	                data: {
	                    id: entId,
	                    status:status,
	                    opDesc : opDesc
	                },
	                dataType: "json", //指定服务器的数据返回类型，
	                success: function (res) {
	                    if (res.msg && res.msg == "fail") {
	                    	showTipDialog("操作失败!");              	                         
	                    }else{
	                    	showTipDialog(res.msg);
	                    }                       
	                },
	                error: function () {
	                    showTipDialog("操作失败!");
	                }
	            });
			}
			
        });

		
        
        $("#ok").on("click",function(){
        	var entId = "${enterprise.id!}";
        	window.location.href = "${contextPath}/manage/enterprise/showEcInfo.html?entId=" + entId;
        });
        
        //历史记录
        $("#history-btn").on("click", function () {
            window.location.href="${contextPath}/manage/entecinfochangehistory/index.html?entId=${enterprise.id!}";
        });
        
        //EC操作记录
        $("#ecHistory-btn").on("click", function () {
            window.location.href="${contextPath}/manage/enterprise/ecRecord.html?entId=${enterprise.id!}";
        });
        
        function isBlank(str){
        	if(str == null){
        		return true;
        	}
        	var temp = str.replace(/\ +/g, ""); //去掉空格
        	temp = temp.replace(/[ ]/g, "");    //去掉空格
        	temp = temp.replace(/[\r\n]/g, ""); //去掉回车换行
        	if(temp.length ==0){
        		return true;
        	}else{
        		return false;
        	}
    	}
		
        
     }
</script>
<script>
    if(window.name != "bencalie"){
        location.reload();
        window.name = "bencalie";
    }
    else{
        window.name = "";
    }
</script>
</body>
</html>