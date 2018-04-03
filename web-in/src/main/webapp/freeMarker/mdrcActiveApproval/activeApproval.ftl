<!DOCTYPE html>
<#assign  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量礼品卡平台-激活申请</title>
    <meta name="keywords" content="流量礼品卡平台 激活申请"/>
    <meta name="description" content="流量礼品卡平台 激活申请"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

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
            width: 323px;
        }

        .form input[type='radio'] {
            vertical-align: text-bottom;
            margin-bottom: 2px;
        }

        .form .btn-default[disabled] {
            background-color: #ccc;
        }

        .form .btn-default[disabled]:hover {
            background-color: #ccc;
        }

        .form .form-group label {
            width: 40%;
            text-align: right;
            margin-right: 10px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>新增激活申请
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-content">
            <form class="form" id="dataForm">
            	<div class="form-group">
                	<label>卡名称：</label>
                	<span>${(mdrcBatchConfig.configName)!}</span>
            	</div>           
                <div class="form-group">
                    <label>批次号：</label>
                    <span id="productName">${(mdrcBatchConfig.serialNumber)!}</span>
                </div>
                <div class="form-group">
                    <label>流量套餐：</label>
                    <span id="productName">${(mdrcBatchConfig.productName)!}</span>
                </div>
                <div class="form-group">
                    <label>流量值：</label>
                    <span id="productSize">${(mdrcBatchConfig.productSize/1024.0)?string("#.##")} MB</span>
                </div>
                <div class="form-group">
                    <label>可激活数量：</label>
                    <span id="canBeActivatedCount">${(mdrcBatchConfig.canBeActivatedCount)!}</span>
                </div>
                <div class="form-group">
                    <label>激活方式：</label>
                    <input type="radio" name="activeType" value="1" checked><span class="mr-20" required>全量激活</span>
                    <input type="radio" name="activeType" value="2" class="ml-30"><span class="mr-10">区间激活</span>
                </div>
                <div id="intervalActive" class="form-group" hidden>
                    <label>激活数量：</label>
                    <input type="text" name="activeCount" id="activeCount" class="mobileOnly" required>
                    <span id="over-error" class="error-tip"></span>
                </div>
                <div class="form-group">
                    <label>起始卡序列号：</label>
                    <input type="hidden" id="startCardNumber" name="startCardNumber" value="${(mdrcBatchConfig.startCardNumer)!}"/>                           
                    <span id="startCardNum">${(mdrcBatchConfig.startCardNumer)!}</span>
                </div>
                <div class="form-group">
                    <label>终止卡序列号：</label>
                    <input type="hidden" id="endCardNumber" name="endCardNumber"/>
                    <span id="endCardNum">${(mdrcBatchConfig.endCardNumber)!}</span>
                </div>                   
               
            </form>
             <input type="hidden" name="count" id="count" value="${(mdrcBatchConfig.canBeActivatedCount)!}"/>
             <input type="hidden" name="configId" id="configId" value="${(mdrcBatchConfig.id)!}"/>
        </div>
    </div>

    <div class="mt-30 text-center" style="position: relative;">
        <a class="btn btn-primary btn-sm mr-10" style="width: 114px;" id="submit-btn">提交审批</a>
        <span id="submit-error" class="error-tip" style="position: absolute;line-height: 30px;"></span>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    require(["common", "bootstrap"], function () {
        initFormValidate();
        listeners();

    });

    function listeners() {
        $("input[name='activeType']").on("click", function () {
            if ($("input[name='activeType']:checked").val() == '1') {//全量激活
                $("#intervalActive").hide();
                $("#endCardNum").html('${(mdrcBatchConfig.endCardNumber)!}');
                var total = parseInt($("#canBeActivatedCount").html());
                $("#count").val(total);
            } else {//区间激活
                $("#intervalActive").show();
                $("#activeCount").val("");
                $("#endCardNum").html("");
            }
        });
       
        
        $("#activeCount").on("blur", function () {
        	 	//后台根据起始卡号和激活数量计算终止卡号
                var configId = $("#configId").val(); 
    			var startCardNumber = $("#startCardNumber").val();
       			      			
       			var total = parseInt($("#canBeActivatedCount").html());
            	var activeCount = parseInt($(this).val());
            	if (activeCount > total) {
            		return false;
            	}else{
            		$("#count").val(activeCount);
            	}
            	var count = $("#count").val();    
                $.ajax({
		            url: "${contextPath}/manage/mdrc/active/calEndCardNumberAjax.html?${_csrf.parameterName}=${_csrf.token}",
		            type: "POST",
		            data: {
		            	configId: configId,
		            	startCardNumber: startCardNumber,
		                count: count
		            },
		            dataType: "JSON",
		            success: function (res) {
		            	if (res.result && res.result == "success") {
		                    $("#endCardNum").html(res.endCardNumber);
						} 
		             }
	        	});                 
        });

        $("#submit-btn").off().on("click", function () {
            if ($("#dataForm").validate().form()) {         
	                submitFormAjax();
	                return false;
            }
        });
    }

    function initFormValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.closest(".form-group").append(error);
            },
            rules: {
                activeCount: {
                	positive: true,               	
                    required: true,
                    max : ${(mdrcBatchConfig.canBeActivatedCount)!}
                }
            },
            errorElement: "span",
            messages: {
                activeCount: {
                	positive: "请输入正整数",
                    required: "请输入激活数量",
                    max : "激活数量不可超过可激活总数"
                }
            }
        });
    }
    
      function sizeFun(size) {
        if (size == null) {
            return "-";
        }
        if (size < 1024) {
            return size + "KB";
        }
        if (size >= 1024 && size < 1024 * 1024) {
            return (size * 1.0 / 1024) + "MB";
        }
        if (size >= 1024 * 1024) {
            return (size * 1.0 / 1024 / 1024) + "GB";
        }

        return size * 1.0 / 1024 + "MB";
    }		
    
    //提交请求
    function submitFormAjax(){
        var configId = $("#configId").val(); 
    	var startCardNumber = $("#startCardNumber").val();
        var count = $("#count").val();
        if(startCardNumber && configId && count){
	        $.ajax({
	            url: "${contextPath}/manage/mdrc/active/save.html?${_csrf.parameterName}=${_csrf.token}",
	            type: "POST",
	            data: {
	            	configId: configId,
	            	startCardNumber: startCardNumber,
	                count: count
	            },
	            dataType: "JSON",
	            success: function (res) {
	            	if (res.result && res.result == "success") {
	                	$("#submit-error").html("");
	                    window.location.href = "index.html";
					} else {
	                    $("#submit-error").html(res.errorMsg);
	                    return false;
	                }
	             }
	        });               
        } 
    }
    
    function cleanInput(){
         $("#submit-btn").removeAttr("disabled");
         $("input[type='radio']").removeAttr("disabled");
         $("#over-error").html("");
         $("#submit-error").html("");
    }  
</script>
</body>
</html>