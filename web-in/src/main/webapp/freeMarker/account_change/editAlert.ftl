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

        span.error {
            color: red;
            margin-left: 10px;
        }

        .form .form-group .prompt {
            margin-left: 0px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3><#if provinceFlag ?? && provinceFlag == 'hun'>设置暂停值<#else>设置预警值</#if>
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"  onclick="javascript:window.location.href='${contextPath}/manage/accountChange/index.html'">返回</a>            
        </h3>
    </div>


    <div class="tile mt-30">
        <div class="tile-header">
            企业信息
        </div>
        <div class="tile-content">
            <div class="row form">
            	<form id="dataForm" action="${contextPath}/manage/accountChange/saveAlert.html" method="post">
                <!--<form id="dataForm" action="${contextPath}/manage/accountChange/saveChange.html" method="post">-->
                    <div class="col-md-6 col-md-offset-4">
                    	<input type="hidden" id="approvalType" name="approvalType" value="${approvalType!2}"/>
                        <input type="hidden" id="entCode" name="entCode" value="${enterprise.code!}"/>
                        <input type="hidden" id="prdId" name="prdId" value="${(enterprise.productId)!}"/>
                        
                        
                        
                        <input type="hidden" id="id" name="id" value="${enterprise.id!}"/>
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
                        <#if provinceFlag ?? && provinceFlag != 'hun'>
                        <div class="form-group">
                            <label>预警值：</label>
                            <input type="text" name="alertCount" id="alertCount" class="hasPrompt"  <#if enterprise.productType ?? && enterprise.productType == 1> value="${(enterprise.alertCount)!}" <#else> value="${(enterprise.alertCount)?string('0.00')}" </#if>  required/>
                            <#if enterprise.productType ?? && enterprise.productType == 1> MB<#else> 元</#if>
                        </div>
                        </#if>
                        <div class="form-group">
                            <label>暂停值：</label>
                            <input type="text" name="stopCount" id="stopCount" class="hasPrompt" <#if enterprise.productType ?? && enterprise.productType == 1> value="${(enterprise.stopCount)!}" <#else> value="${(enterprise.stopCount)?string('0.00')}" </#if> required/>
                            <#if enterprise.productType ?? && enterprise.productType == 1> MB<#else> 元</#if>
                        </div>
                        <div class="form-group">
                            <label>企业余额：</label>
                            <span name="currencyCount" id="currencyCount">
                            	<#if enterprise.productType ?? && enterprise.productType == 1>${(enterprise.currencyCount)!} MB<#else>${(enterprise.currencyCount)?string('0.00')} 元</#if>
                            </span>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
            </div>

        </div>
    </div>


    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="save-btn">保存</a>
        <span style='color:red' id="errorMsg">${errorMsg!}</span>
    </div>


</div>

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
    });


    function initFormValidate() {
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
                alertCount: {
                    required: true,
                    maxlength: 11,
                    <#if enterprise.productType ?? && enterprise.productType == 1>
                    digits: true
                    <#else>
                    solde: true
                    </#if>
                },
                stopCount: {
                    required: true,
                    maxlength: 11,
                    <#if enterprise.productType ?? && enterprise.productType == 1>
                    digits: true
                    <#else>
                    solde: true
                    </#if>
                }
            },

            messages: {
                alertCount: {
                    required: "请输入预警值",
                    maxlength: "最大不超过11个字符"
                },
                stopCount: {
                    required: "请输入暂停值",
                    maxlength: "最大不超过11个字符"
                }
            }
        });
    }

    function saveForm() {
        $("#save-btn").on("click", function () {
        $('#error_msg').html("");
        if($('#dataForm').validate().form() == false){
            return;
        }
	
    var overageAmount =${(enterprise.currencyCount)!'0.00'};
    var alertAmount = $('#alertCount').val();
    var stopAmount = $('#stopCount').val();

    var oA = toDecimal(overageAmount);
    var aA = toDecimal(alertAmount);
    var sA = toDecimal(stopAmount);

	var provinceFlag = "${provinceFlag!}";

    oA=oA*1;
    aA=aA*1;
    sA=sA*1;
    
    if(alertAmount==0.00 && stopAmount==0.00){
    	$("#dataForm").submit();
	    return false;
    }
    
    if(provinceFlag != 'hun'){
        if(aA<sA){
        	$('#errorMsg').html("预警值不能小于暂停值");
    		return false;
    	}
    
    	if(oA<aA){
        	$('#errorMsg').html("企业余额不能小于预警值");
    		return false;
    	}   
    }
       
    if(!confirm("是否确定保存记录？")){
			return false;
	}
    $("#dataForm").submit();
    });
}
    
function verifySoldeValTrue(str){
	var reg =/^([1-9][\d]{0,10}|0)(\.[\d]{2})+$/ ;
    var r = str.match(reg);  
    if(r==null){
    	return false;
    }
    else{
    	return true;
    }       
}

 function toDecimal(x) {  
        var f = parseFloat(x);  
        if (isNaN(f)) {  
            return false;  
        }  
        var f = Math.round(x*100)/100;  
        var s = f.toString();  
        var rs = s.indexOf('.');  
        if (rs < 0) {  
            rs = s.length;  
            s += '.';  
        }  
        while (s.length <= rs + 2) {  
            s += '0';  
        }  
        return s;  
    } 
</script>
</body>
</html>
