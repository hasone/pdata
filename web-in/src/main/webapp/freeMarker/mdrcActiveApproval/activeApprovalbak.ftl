<!DOCTYPE html>
<#assign  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-创建激活申请</title>
    <meta name="keywords" content="流量平台 激活申请"/>
    <meta name="description" content="流量平台 激活申请"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form .form-control {
            width: 220px;

        }

        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 220px;
        }

        .file-box {
            display: inline-block;
        }

        .form label {
            width: 80px;
            text-align: right
            margin-right: 10px;
        }

        span.error {
            color: red;
            margin-left: 10px;
        }

        .form .form-group .prompt {
            margin-left: 83px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>

    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/Js/artDialog/skins/default.css">
    <script src="${contextPath}/manage2/Js/artDialog/artDialog.js"></script>
    <script src="${contextPath}/manage2/Js/artDialog/jquery.artDialog.js"></script>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>激活申请
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header"></div>
        <div class="tile-content">
            <div class="row form">
                <form id="dataForm" action="${contextPath}/manage/mdrc/active/save.html?${_csrf.parameterName}=${_csrf.token}"
                      method="post"  enctype="multipart/form-data">
                    <div class="col-md-12 col-md-offset-1">

                        <div class="form-group">
                            <label>企业名称：</label>
                            <div class="btn-group btn-group-sm enterprise-select">
                                <input name="entId" id="entId" style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                                <button type="button" class="btn btn-default" style="width: 298px;">请选择</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <#if enterprises?? && enterprises?size!=0>
                                        <#list enterprises as e>
                                            <li data-value="${e.id!}"><a href="#">${e.name!}</a></li>
                                        </#list>
                                    <#else>
                                        <li data-value=""><a href="#">没有可选的企业</a></li>
                                    </#if>
                                </ul>
                            </div>
                        </div>                                        
                        
						<div class="form-group">
                            <label>批次号：</label>
                            <div class="btn-group btn-group-sm configId-select">
                                <input name="configId" id="configId" style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                                <button type="button" class="btn btn-default" style="width: 298px;">请选择</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                	<li data-value=""><a href="#">请选择</a></li>
                                    <#if mdrcBatchConfigList?? && mdrcBatchConfigList?size!=0>
                                        <#list mdrcBatchConfigList as m>
                                            <li data-value="${m.id!}"><a href="#">${m.serialNumber!}</a></li>
                                        </#list>
                                    <#else>
                                        <li data-value=""><a href="#">没有可选的批次号</a></li>
                                    </#if>
                                </ul>
                            </div>
                        </div>
                        
                        <div class="form-group ">
                            <label>产品名称：</label>
                            <span id="productName"></span>
                        </div>
                        
                        <div class="form-group">
                            <label>产品大小：</label>
                            <span id="productSize"></span>
                        </div>

						<div class="form-group">
                            <label>可激活数量：</label>
                            <span id="canBeActivatedCount"></span>
                        </div>	
						
						
						<div class="form-group">
                            <label>激活方式：</label>
                            <input type="radio" name="agree"  value='0' onclick="activateStyle(this);" checked required/><span>全量激活</span>
                            <input type="radio" name="agree"  value='1' onclick="activateStyle(this);"/><span>区间激活</span>
                        </div>
						
                        <div class="form-group">
                            <label>激活数量：</label>
                            <div class="btn-group btn-group-sm">
                                <input class="form-control" id="count" name="count" style="width:322px" required>
                            </div>
                        </div>

                        <div class="form-group ">
                            <label>起始序列：</label>
                            <input type="hidden" id="startCardNumber" name="startCardNumber"/>                           
                            <span id="startCardNum"></span>
                        </div>
                        
                        <div class="form-group">
                            <label>结束序列：</label>
                            <input type="hidden" id="endCardNumber" name="endCardNumber"/>
                            <span id="endCardNum"></span>
                        </div>

                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>


    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">提交审批</a>
        &nbsp;&nbsp;&nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
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
                <span class="message-content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="success-btn">确 定</button>
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
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div>
<!-- loading end -->

<!--[if lt IE 9]-->
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<!--[endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var templateList = [];
    require(["moment", "common", "bootstrap"], function (mm) {

        window["moment"] = mm;

        initFormValidate();

        submitForm();

        listeners();

        init();
    });

    function init() {

        //提交表单
        $("#sure").on("click", function () {
            $("#dataForm").submit();
        });

    }

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#dataForm").validate({
            rules: {
                count: {
                    required: true,
                    min: 1,
                    max: 9999999,
                    positive: true
                }
            },
            errorElement: "span",
            messages: {
                count: {
                    required: "请填写激活数量",
                    positive: "请正确填写激活数量",
                    min: "激活数量为1-9999999范围之间",
                    max: "激活数量为1-9999999范围之间"
                }
            }
        });
    }

    function submitForm() {
        $("#submit-btn").on("click", function () {
            if ($("#dataForm").validate().form()) {
                $("#submit-dialog").modal("show");
            }
            return false;
        });
    }

    function listeners() {
        //监听企业变更
        $("a",".enterprise-select li").on("click", function () {
            var entId = $(this).parent('li').data('value');
            getConfigsAjax(entId);
        });
        
        //监听批次号变更
        $("a",".configId-select li").on("click", function () {
          	var configId = $(this).parent('li').data('value');
            getConfigDetailsAjax(configId);
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
    
    //根据企业id卡批次信息
    function getConfigsAjax(entId) {
        if(entId){
            $.ajax({
                type: "POST",
                data: {
                    entId: entId
                },
                url: "${contextPath}/manage/mdrc/active/getConfigsAjax.html?${_csrf.parameterName}=${_csrf.token}",
                dataType: "json", //指定服务器的数据返回类型，
                success: function (res) {
                	if (res.result && res.result == "success") {
                		setConfigs(res.configs);        	              	
                	}
                }
            });
        }
    }
    
    
    //设置批次号
	function setConfigs(configs) {
        var parent = $(".configId-select ul");
		parent.empty();
		$(".btn", parent.parent()).eq(0).html("请选择批次号");
		if(configs && configs.length>0){
			for(var i=0; i< configs.length; i++){
				parent.append('<li data-value="'+configs[i].id+'"><a href="javascript:void(0)">'+configs[i].serialNumber+'</a></li>');
			}
		}else{
			parent.append('<li data-value=""><a href="javascript:void(0)">该企业没有可选的批次号</a></li>');
		}
		listeners();
	}		
    
    
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
	                    $("#productName").html(res.mdrcBatchConfig.productName);//产品名称
	                    $("#productSize").html(res.mdrcBatchConfig.productSize);//产品大小                    
	                    $("#canBeActivatedCount").html(res.mdrcBatchConfig.canBeActivatedCount);//可激活数量
	                    $("#startCardNum").html(res.mdrcBatchConfig.startCardNumer);//起始序列号	
	                    $("#startCardNumber").val(res.mdrcBatchConfig.startCardNumer);                    			
	                    $("#endCardNum").html(res.mdrcBatchConfig.endCardNumber);//终止序列号
              			$("#endCardNumber").val(res.mdrcBatchConfig.endCardNumber);
              		
	                	
                	}                
                }
            });
        }
    }
    
   //校验可激活的卡数量
   function validateActivateCount(configId,count) {
        if(configId){
            $.ajax({
                type: "POST",
                data: {
                    configId: configId,
                    startCardNumber:startCardNumber,
                    count:count
                },
                url: "${contextPath}/manage/mdrc/active/getConfigDetailsAjax.html?${_csrf.parameterName}=${_csrf.token}",
                dataType: "json", //指定服务器的数据返回类型，
                success: function (res) {
                	if (res.result && res.result == "success") {
	                    $("#productName").html(res.mdrcBatchConfig.productName);//产品名称
	                    $("#productSize").html(res.mdrcBatchConfig.productSize);//产品大小                    
	                    $("#canBeActivatedCount").html(res.mdrcBatchConfig.canBeActivatedCount);//可激活数量
	                    $("#startCardNum").html(res.mdrcBatchConfig.startCardNumer);//起始序列号	
	                    $("#startCardNumber").val(res.mdrcBatchConfig.startCardNumer);                    			
	                    $("#endCardNum").html(res.mdrcBatchConfig.endCardNumber);//终止序列号
              			$("#endCardNumber").val(res.mdrcBatchConfig.endCardNumber);             		                	
                	}                
                }
            });
        }
    }
</script>
</body>
</html>