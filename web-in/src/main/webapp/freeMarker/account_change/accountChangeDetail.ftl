<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-审批充值</title>
    <meta name="keywords" content="流量平台 审批充值"/>
    <meta name="description" content="流量平台 审批充值"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 220px;
        }

        span.error {
            color: red;
            margin-left: 10px;
        }

        #comment {
            width: 200px;
            resize: none;
            height: 80px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>     
	        <#if approvalType?? && approvalType == 2>
				审批充值
	    	<#elseif approvalType?? && approvalType == 9>
				审批最小额度
	    	<#elseif approvalType?? && approvalType == 10>
				审批预警值
	    	<#elseif approvalType?? && approvalType == 11>
				审批暂停值
	    	</#if>
	    	<a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
    	</h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-header">
            审批意见
        </div>
        <div class="tile-content">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>审批状态</th>
                            <th>审批用户</th>
                            <th>用户职位</th>
                            <th>审批时间</th>
                            <th>审批意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if approvalRecords??>
                            <#list approvalRecords as record>
                            <tr>
                                <td>${record.description!}</td>
                                <td>${record.userName!}</td>
                                <td>${record.managerName!}${record.roleName!}</td>
                                <td>${(record.updateTime?datetime)!}</td>
                                <td title="${record.comment!}">${record.comment!}</td>
                            </tr>
                            </#list>
                        </#if>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            企业信息
        </div>
        <div class="tile-content">
            <div class="row form">
                <form id="dataForm" action="${contextPath}/manage/approval/saveAccountChangeApproval.html"
                      method="post">
                    <div class="col-md-6 col-md-offset-4">
                        <div class="form-group">                       
                            <label>
				           <#if approvalType?? && approvalType == 2>
								申请充值时间：
					    	<#elseif approvalType?? && approvalType == 9>
								修改最小额度时间
					    	<#elseif approvalType?? && approvalType == 10>
								修改预警值时间
					    	<#elseif approvalType?? && approvalType == 11>
								修改暂停值时间
					    	</#if></label>
                            <span>${approvalRequest.createTime?datetime}</span>
                        </div>
                        <div class="form-group">
                        	<label>账户名称：</label>
                            <span>${(account.productName)!}</span>
                        </div>
                        <div class="form-group">
                            <label>账户类型：</label>
                            <span>${(account.productTypeName)!}</span>
                        </div>
                        <div class="form-group">
                            <label>
					            <#if approvalType?? && approvalType == 2>
									充值额度：
						    	<#elseif approvalType?? && approvalType == 9>
									最小额度：
						    	<#elseif approvalType?? && approvalType == 10>
									预警值：
						    	<#elseif approvalType?? && approvalType == 11>
									暂停值：
						    	</#if>
						    </label>	
                            <span><#if account.productType?? && account.productType == 1>${(requestCount)!} MB<#else>${(requestCount?string('0.##'))!} 元</#if></span>
                        </div>
						<#if provinceFlag ?? && provinceFlag == "gansu">
							<div class="form-group">
                            	<label>优惠类型：</label>
                            	<span>${discountType!}</span>
                        	</div>
							<div class="form-group">
                            	<label>优惠值：</label>
                            	<span><#if discountType ?? && discountType != "无">${discountValue!}<#else>无</#if></span>
                        	</div>
                        </#if>
                        <div class="form-group">
                            <label>客户经理：</label>
                            <span>${customerManager.userName!}</span>
                        </div>
                        <div class="form-group">
                            <label>客户经理手机号：</label>
                            <span>${customerManager.mobilePhone!}</span>
                        </div>
                        <div class="form-group">
                            <label>企业名称：</label>
                            <span>${Enterprises.name!}</span>
                        </div>
                        <div class="form-group">
                            <label>企业编码：</label>
                            <span>${Enterprises.code!}</span>
                        </div>
                        <div class="form-group">
                            <label>所属地区：</label>
                            <span>${Enterprises.cmManagerName!}</span>
                        </div>

                        <div class="form-group">
                            <label>创建时间：</label>
                            <span>${Enterprises.createTime?datetime}</span>
                        </div>
                       <#if approvalType?? && approvalType == 2>									
                        <div class="form-group">
                            <label>余额：</label>
                            <span><#if account.productType?? && account.productType == 1> ${(account.count/1024)?string("0")} MB<#else>${(account.count/100.0)?string("0.##")} 元</#if></span>
                        </div>
                       </#if>

                        <div class="form-group">
                            <input type="hidden" name="enterId" id="enterId" value="${(Enterprises.id)!}">
                            <input type="hidden" name="approvalRecordId" id="approvalRecordId"
                                   value="${approvalRecordId!}">
                            <input type="hidden" name="requestId" id="requestId" value="${requestId!}">
                            <input type="hidden" name="processId" id="processId" value="${processId!}">
                            <input type="hidden" name="approvalStatus" id="approvalStatus">
                    		<input type="hidden" id="approvalType" name="approvalType" value="${approvalType!}"/>                   	
                            <label style="vertical-align: top">审批意见：</label>
                            <textarea name="comment" id="comment" maxlength="300"></textarea>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
            </div>

        </div>
    </div>


    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="reject-btn">驳回</a>
        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">提交审批</a>
        <span style="color:red" id="error_msg"></span>
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
        <p class="weui_toast_content">提交中</p>
    </div>
</div>
<!-- loading end -->

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    require(["common", "bootstrap"], function () {

        initFormValidate();

        submitForm();
    });

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#dataForm").validate({
            rules: {
                comment: {
                    required: true,
                    maxlength: 300
                }
            },
            errorElement: "span",
            messages: {
                comment: {
                    required: "请填写审批意见",
                    maxlength: "审批意见不超过300个字符"
                }
            }
        });
    }

    function submitForm() {
        $("#submit-btn").on("click", function () {
            $("#error_msg").empty();
            var currentSolde = <#if account.productType?? && account.productType == 1> ${(account.count/1024)?string("0")}<#else>${(account.count/100.0)?string("0.##")}</#if>; 
            var minusSolde = <#if account.productType?? && account.productType == 1>${(requestCount)!}<#else>${(requestCount?string('0.##'))!}</#if>
            if(currentSolde + minusSolde <  0){
                $("#error_msg").append("账户余额小于退款金额，审批失败！");
                return false;
            }
            if ($("#dataForm").validate().form()) {
                $("#approvalStatus").val(1);
                showToast();
                $("#dataForm").submit();
            }
            return false;
        });

        $("#reject-btn").on("click", function () {
            if ($("#dataForm").validate().form()) {
                $("#approvalStatus").val(0);
                $("#dataForm").submit();
            }
            return false;
        });
    }
</script>
</body>
</html>