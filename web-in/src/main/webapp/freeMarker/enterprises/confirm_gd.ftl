<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量礼品卡平台-企业详情</title>
    <meta name="keywords" content="流量礼品卡平台 企业详情"/>
    <meta name="description" content="流量礼品卡平台 企业详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form-group label{
            width: 40%;
            text-align: right;
        }
        .form-group .prompt{
            color: #a7a7a7;
        }
        
        input[type='text']{
		    background: transparent;
		    border: 1px solid #ccc;
		    outline: none;
		    border-radius: 3px;
		    padding: 4px 5px;
		    width: 350px;
		}
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>企业详情
         	<#if needConfirm?? && needConfirm == "true">
				<a class="btn btn-primary btn-sm pull-right btn-icon icon-back" 
            		onclick="javascript:window.location.href='${contextPath}/manage/enterprise/createEnterpriseIndex.html'">返回</a>
 			</#if> 
			<#if needConfirm?? && needConfirm == "false">
				<a class="btn btn-primary btn-sm pull-right btn-icon icon-back" 
            		onclick="javascript:window.location.href='${contextPath}/manage/enterprise/index.html'">返回</a>
 			</#if> 
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            产品订购信息
        </div>
        <div class="tile-content">
            <div class="form-group">
                <label>企业名称：</label>
                <span>${(enterprise.name)!}</span>
            </div>
            <div class="form-group">
                <label>集团客户编码：</label>
                <span>${(enterprisesExtInfo.ecCode)!}</span>
            </div>
            <div class="form-group">
                <label>所属地市：</label>
                <span>${(enterprise.cmManagerName)!}</span>
            </div>
            <div class="form-group">
                <label>集团产品号码：</label>
                <span>${(enterprisesExtInfo.ecPrdCode)!}</span><a class="ml-10" href="${contextPath}/manage/product/gd/qryProduct.html?entCode=${enterprise.code!}&&entId=${enterprise.id!}&&entPrdCode=${enterprisesExtInfo.ecPrdCode!}">查看订购套餐详情</a>
            </div>
            <div class="form-group">
                <label>产品状态：</label>
 				<#if enterprise.deleteFlag==0>
 					<span>正常</span>
 				</#if>
 				<#if enterprise.deleteFlag==2>
 					<span>已暂停</span>
 				</#if>
 				<#if enterprise.deleteFlag==3>
 					<span>已下线</span>
 				</#if>
 				<#if enterprise.deleteFlag==4>
 					<span>等待市级管理员审批</span>
 				</#if>
 				<#if enterprise.deleteFlag==5>
 					<span>等待省级管理员审批</span>
 				</#if>
 				<#if enterprise.deleteFlag==6>
 					<span>等待省级管理员审批</span>
 				</#if>
 				<#if enterprise.deleteFlag==7>
 					<span>已保存</span>
 				</#if>
 				<#if enterprise.deleteFlag==8>
 					<span>已驳回</span>
 				</#if>
 				<#if enterprise.deleteFlag==10>
 					<span>认证审批中</span>
 				</#if>
 				<#if enterprise.deleteFlag==14>
 					<span>待确认</span>
 				</#if>
            </div>
            <div class="form-group">
                <label>折扣：</label>
                <span>${((enterprise.discount)! / 10.0)?string("#.##")}</span>
            </div>
            <div class="form-group">
                <label>客户经理姓名：</label>
                <span>${(cmAdminister.userName)!}</span>
            </div>
            <div class="form-group">
                <label>客户经理手机号码：</label>
                <span>${(cmAdminister.mobilePhone)!}</span>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            集团信息
        </div>
        <div class="tile-content">
            <div class="form-group">
                <label>企业法人：</label>
                <span>${(ecSyncInfo.legalPerson)!}</span> <a class="ml-30 btn btn-warning" href="${contextPath}/manage/enterprise/syncEcInfo.html?entCode=${enterprise.code!}&&entId=${enterprise.id!}&&entPrdCode=${enterprisesExtInfo.ecPrdCode!}&&needConfirm=${needConfirm!}">同步信息</a>
            </div>
            <div class="form-group">
                <label>企业营业执照号码：</label>
                <span>${(ecSyncInfo.entPermit)!}</span>
            </div>
            <div class="form-group">
                <label>集团联系人姓名：</label>
                <span>${(ecSyncInfo.userName)!}</span>
            </div>
            <div class="form-group">
                <label>集团联系人手机号码：</label>
                <span>${(ecSyncInfo.mobile)!}</span>
            </div>
            <div class="form-group">
                <label>集团状态：</label>
 				<#if ecSyncInfo?? && ecSyncInfo.deleteFlag?? && ecSyncInfo.deleteFlag==0>
 					<span>正常</span>
 				</#if>
 				<#if ecSyncInfo?? && ecSyncInfo.deleteFlag?? && ecSyncInfo.deleteFlag==1>
 					<span>注销</span>
 				</#if>
            </div>
            <div class="form-group">
                <label>集团状态更新时间：</label>
                <span>${(vipTypeStateDate)!}</span>
            </div>
            <div class="form-group">
                <label>集团开户时间：</label>
                <span>${(innetDate)!}</span>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
 	<#if needConfirm?? && needConfirm == "true">
            需确认企业产品管理员信息
 	</#if>
 	<#if needConfirm?? && needConfirm == "false">
            企业产品管理员信息
 	</#if>
        </div>
        <div class="tile-content">
        	<#if needConfirm?? && needConfirm == "true">
        	 <form id="dataForm" action="${contextPath}/manage/enterprise/confirmCreateEnterprise.html?${_csrf.parameterName}=${_csrf.token}&entId=${enterprise.id!}&ecCode=${enterprise.code!}&ecPrdCode=${enterprisesExtInfo.ecPrdCode!}"
        	 	method="post">
				<div class="form-group form-inline">
 					<label>姓名：</label>
 					<input type="text" name="name" id="name" value="${(entAdminister.userName)!}"
                               class="form-control" maxlength="64">
            	</div>
            	<div class="form-group form-inline">
              	  <label>手机号码：</label>
              	  <input type="text" name="phone" id="phone" value="${(entAdminister.mobilePhone)!}"
                               class="form-control" maxlength="11">
            	</div>
           		<div class="form-group form-inline">
                	<label>联系邮箱：</label>
                	<input type="text" name="email" id="email" value="${(entAdminister.email)!}"
                               class="form-control" maxlength="64">                
            	</div>
 			
            	<div class="form-group">
                	<label></label>
                	<span class="prompt">请认真确认手机号码及联系邮箱，填写错误将影响正常使用</span>
            	</div>
            	</form>
 			</#if>
            <#if needConfirm?? && needConfirm == "false">
				<div class="form-group">
 					<label>姓名：</label>
                 	<span>${(entAdminister.userName)!}</span>
            	</div>
            	<div class="form-group">
              	  <label>手机号码：</label>
              	  <span>${(entAdminister.mobilePhone)!}</span>
            	</div>
           		<div class="form-group">
                	<label>联系邮箱：</label>
                	<span>${(entAdminister.email)!}</span>
            	</div>
 			</#if>
        </div>
    </div>
    <#if needConfirm?? && needConfirm == "true">
    	<div class="text-center">
    		<div class="btn btn-warning mt-30" style="width: 150px;" id="confirm-btn">确认开户</div>
    		<span style="color:red" id="error_msg">${errorMsg!}</span>
    	</div>
    </#if>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script type="text/javascript">
require(["common", "bootstrap"], function () {
	initFormValidate();
	listeners();
});

function listeners(){
	/* $(".client-edit-btn").on("click", function () {
	    $(this).addClass("hidden");
	    $(this).next(".client-save-btn").removeClass("hidden");
	    var editableEle = $(this).parent().find(".editable-text");
	    editableEle.hide();
	    var input = editableEle.next(".edit-input");
	    input.removeClass("hidden");
	    input.val(editableEle.text());

	    input.blur();
	});

	$(".client-save-btn").on("click", function () {
	    var input = $(this).parent().find(".edit-input");
	    var editableEle = input.prev(".editable-text");
	    var edit_ele = input;
	    var text = input.val();

	    if (validElement(input[0])) {
	        $(this).addClass("hidden");
	        $(this).prev(".client-edit-btn").removeClass("hidden");

	        editableEle.html(text);
	        if (edit_ele.hasClass("btn-group")) {
	            editableEle.data("value", input.val());
	        }
	        editableEle.show();
	        edit_ele.addClass("hidden");
	    }
	}); */
	$("#confirm-btn").on("click", function () {
		if ($("#dataForm").validate().form()) {
			$("#dataForm").submit();
		}
	});
}

function validElement(input) {
    var validator = $("#dataForm").validate();
    validator.form();
    return validator.check(input);
}

function initFormValidate() {
    $("#dataForm").validate({
                                onfocusout: function(element){
                                	$(element).valid();
                                },
    							errorPlacement: function (error, element) {
                                    error.addClass("error-tip");
                                   	element.closest(".form-group").append(error);
                                },
                                errorElement: "span",
                                rules: {
                                    name: {
                                        required: true,
                                        noSpecial: true
                                    },
                                    phone: {
                                        required: true,
                                        mobile: true
                                    },
                                    email: {
                                        required: true,
                                        email: true
                                    }
                                },
                                messages: {
                                    name: {
                                        required: "请正确输入管理员姓名！",
                                        noSpecial: "管理员姓名只能由汉字、英文字符、下划线或数字组成!"
                                    },
                                   
                                    email: {
                                        required: "请正确填写电子邮箱！",
                                        email: "请正确填写电子邮箱！"
                                    },
                                    phone: {
                                        required: "请填写管理员联系方式！",
                                        mobile: "请正确填写管理员联系方式！"
                                    }
                                }
                            });
}
</script>
</body>
</html>