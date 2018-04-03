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
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

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
            width: 120px;
        }
        
        .form input[type='text']:read-only{
        	background-color: #e0e0e0;
        }
        
        .form label{
	        width: 80px;
		    text-align: right;
	    }

        span.error {
            color: red;
            margin-left: 10px;
        }

        .form .form-group .prompt {
            margin-left: 84px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>企业充值
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"  onclick="javascript:window.location.href='${contextPath}/manage/accountChange/index.html'">返回</a>            
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-header">
            企业信息
        </div>
        <div class="tile-content">
            <div class="row form">
                <form id="dataForm" action="${contextPath}/manage/accountChange/saveChange.html" method="post">
                    <div class="col-md-6 col-md-offset-4">
                    	<input type="hidden" id="approvalType" name="approvalType" value="${approvalType!}"/>
                        <input type="hidden" id="entCode" name="entCode" value="${enterprise.code!}"/>
						<input type="hidden" id="prdId" name="prdId" value="${(enterprise.productId)!}"/>

                        <div class="form-group">
                            <label>企业名称：</label>
                            <span>${enterprise.name}</span>
                        </div>
                        <div class="form-group">
                            <label>企业简称：</label>
                            <span>${enterprise.entName}</span>
                        </div>
                        <div class="form-group">
                            <label>企业编码：</label>
                                <span>
                                ${enterprise.code!}
                                </span>
                        </div>
                        <div class="form-group">
                            <label>创建时间：</label>
                            <span>${enterprise.createTime?datetime}</span>
                        </div>
                        <div class="form-group">
                            <label>账户名称：</label>
                            <span>${(enterprise.productName)!}</span>
                        </div>                      
                        <div class="form-group">
                            <label>账户类型：</label>
                            <span>${(enterprise.productTypeName)!}</span>
                        </div>
                        <div class="form-group">
                            <label>余额：</label>
                            <span><#if enterprise.productType?? && enterprise.productType == 1>${(enterprise.currencyCount)!} MB<#else>${(enterprise.currencyCount)?string("0.00")} 元</#if></span>
                        </div>
                        
                        <div class="form-group">
                            <label>充值额度：</label>
                            <#if enterprise.productType?? && enterprise.productType == 1>
                            	<input type="text" name="deltaCount" id="deltaCount" class="hasPrompt" required />MB
                            	<div class="prompt">支持数字输入，请输入正整数</div>
                            <#else>
                            	<input type="text" name="deltaCount" id="deltaCount" class="hasPrompt" required /> 元                          	                           	
                            	<div class="prompt">支持数字输入，最多保留2位小数</div>
                            </#if>	
                        </div>
						<#if provinceFlag?? && provinceFlag == "gansu">
							<div class="form-group">
                            	<label>优惠类型：</label>
 								<select name="discountType" id="discountType" class="hasPrompt" value='0' required>
	 								<option value='0'>无</option>
	 								<option value='1'>存送比例</option>
 								</select>                          	                           	
                        	</div>
							<div class="form-group">
                            	<label>优惠值：</label>
 								<input type="text" name="discountValue" id="discountValue" class="hasPrompt mobileOnly" value='0' required readonly/> %                         	                           	
 								<div class="prompt">支持0-100的整数</div>
                        	</div>
						</#if>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
            </div>

        </div>
    </div>


    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="save-btn">保存</a>
        <span style='color:red'>${errorMsg!}</span>
    </div>
</div>

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
                    <span class="message-content">是否提交给市级管理员审批？</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
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

        saveForm();

        listeners();
        //submitApproval();
    });

	function listeners(){
		$("#discountType").on("change",function(){
			if($(this).val() == '0'){
				$("#discountValue").attr('readonly',true);
				$("#discountValue").val('0');
			}else{
				$("#discountValue").removeAttr('readonly');
			}
		});
	}
    /**
     * 表单验证
     */
    function initFormValidate() {
        var maxValue = 1.01;
        var solde = <#if enterprise.productType?? && enterprise.productType == 1>${(enterprise.currencyCount)!}<#else>${(enterprise.currencyCount)?string("0.00")}</#if>;
        var minPermit =  solde * -1;

        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if ($(element).siblings('div').hasClass('prompt')) {
                    $(element).siblings('div.prompt').before(error);
                } else {
                    $(element).parent().append(error);
                }
            },
            errorElement: "span",
            rules: {
                deltaCount: {
                    required: true,
                    maxlength: 10,
                    min: minPermit,
                    <#if enterprise.productType?? && enterprise.productType == 1>
                    	positive:true 
                    	
                    <#else>
                    	<#if isAllowMinusCharge?? && isAllowMinusCharge == true>
                        payLimitNega: true
                        <#else>
                        payLimit: true
                        </#if>    
                    </#if>
                },
                discountValue: {
                	required: true,
                	maxlength: 3,
                	range:[0,100]
                }
            },

            messages: {
                deltaCount: {
                    required: "请输入充值额度",
                    maxlength: "长度最大为10位",
                    min:"请输入正确的金额",
                   <#if enterprise.productType?? && enterprise.productType == 1>                   	
                    	positive:"充值额度格式错误"
                    <#else>
                        <#if isAllowMinusCharge?? && isAllowMinusCharge == true>
                        payLimitNega: "充值额度格式错误" 
                        </#if>    
                    </#if>
                },
                discountValue: {
                	required: "请输入优惠值",
                	maxlength: "长度最大为3位",
                	range:"请输入正确的存送比例"
                }
            }
        });
    }


    function saveForm() {
        $("#save-btn").on("click", function () {
if($("#dataForm").validate().form()){
 showToast();
            
   
            $("#dataForm").submit();
}
           

        });
    }

    function submitApproval() {
        $("#submit_approval").on("click", function () {
            var entId = "${enterprise.id}";
            console.log(entId);
            window.location.href = "${contextPath}/manage/accountChange/submitIndex.html?entId=" + entId;
        });
    }


</script>
</body>
</html>
