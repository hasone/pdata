<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-编辑产品</title>
    <meta name="keywords" content="流量平台 编辑产品"/>
    <meta name="description" content="流量平台 编辑产品"/>

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
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }
        
        textarea {
            width: 323px;
            resize: none;
            height: 150px;
            outline: none;
            border: 1px solid #ccc;
        }

        .form .form-group label {
            width: 140px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 146px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
        
        .error-tip {
            display: block;
            margin-left: 146px;
            color: red;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑产品
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="${contextPath}/manage/supplierProduct/index.html">返回</a>
        </h3>
    </div>

    <form class="form-horizontal" action="${contextPath}/manage/supplierProduct/saveEdit.html" method="post"
          name="table_validate" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">
                	<input type="hidden" name="id" class='hasPrompt' value='${supplierProduct.id!}'/>
                	<input type="hidden" name="supplierId" class='hasPrompt' value='${supplierProduct.supplierId!}'/>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>供应商：</label>
                            <span>${supplierProduct.supplierName!}</span>
                        </div>

                        <div class="form-group">
                            <label>运营商：</label>
							<span>
								<#if supplierProduct.isp == "M">
									移动
								</#if>
								<#if supplierProduct.isp == "U">
									联通
								</#if>
								<#if supplierProduct.isp == "T">
									电信
								</#if>
								<#if supplierProduct.isp == "A">
									三网
								</#if>
							</span>
                        </div>
                        
                        <div class="form-group">
                            <label>产品名称：</label>
                            <input type="text" name="name" id="name" class='hasPrompt' maxLength="64" value='${supplierProduct.name!}'
                                   required/>
                            <div class="prompt">产品名称支持汉字、英文字符、数字及特殊字符组成，区分大小写，长度不超过64个字符</div>
                        </div>
                        <div class="form-group">
                            <label>产品编码：</label>
                            <input type="text" name="code" id="code" class='hasPrompt' maxLength="64" value='${supplierProduct.code!}'
                                   required/>

                            <div class="prompt">产品编码由英文字符、数字、下划线、特殊字符组成，区分大小写，长度不超过64个字符</div>
                        </div>
                        <div class="form-group">
                            <label>产品大小：</label>
                            <span> 
                            	<#if supplierProduct.size?? && supplierProduct.size lt 1024>
                            		${(supplierProduct.size)?string("#.##")} KB
                            	</#if>
                            	<#if supplierProduct.size?? && supplierProduct.size gt 1024 && supplierProduct.size lt 1024*1024>
                            		${(supplierProduct.size/1024)?string("#.##")} MB
                            	</#if>
                            	<#if supplierProduct.size?? && supplierProduct.size gt 1024*1024 && supplierProduct.size lt 1024*1024*1024>
                            		${(supplierProduct.size/1024/1024)?string("#.##")} GB
                            	</#if>
                            </span>
                        </div>
                        <div class="form-group">
                            <label>采购价格：</label>
                            <input type="text" name="priceStr" id="priceStr" maxLength="6" class='hasPrompt'
                                   required value="${money?string("#.##")}" />
                            <div class="prompt">采购价格最多支持两位小数，长度不超过6个字符，单位元</div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                    	<div class="form-group">
                            <label>状态：</label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="status" id="status" 
                                	value="${supplierProduct.status!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">
                                	<#if supplierProduct.status?? && supplierProduct.status==0>下架</#if>
                                	<#if supplierProduct.status?? && supplierProduct.status==1>上架</#if>
                                </button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li data-value="0"><a href="#">下架</a></li>
                                    <li data-value="1"><a href="#">上架</a></li>
                                </ul>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label>使用范围：</label>

                            <div class="btn-group btn-group-sm">

                                <input style="width: 0; height: 0; opacity: 0" name="ownershipRegion"
                                       id="ownershipRegion" value="${supplierProduct.ownershipRegion!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择地区</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu province-menu">

                                </ul>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>漫游范围：</label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="roamingRegion" id="roamingRegion"
                                       value="${supplierProduct.roamingRegion!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择地区</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu province-menu">

                                </ul>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>特征参数：</label>
                            <input type="text" name="feature" id="feature" maxlength="256" class='hasPrompt'
                            	value='${(supplierProduct.feature)!}'/>
                            <div class="prompt">特征参数长度不超过1024个字符</div>
                        </div>
                        
                        <div class="form-group">
	                        <label>说明：</label>
	                        <textarea rows="10" cols="100" maxlength="300" id="illustration" 
	                        	name="illustration" placeholder="" class='hasPrompt'
	                            style="vertical-align: top">${supplierProduct.illustration!}</textarea>
                        	<div class="prompt">说明长度不超过300个字符</div>
                    	</div>
                    
                    </div>
                </div>

            </div>
        </div>

        <div class="mt-30 text-center">
            <input type="submit" class="btn btn-warning btn-sm mr-10 dialog-btn" style="width: 114px;" value="保存"/>&nbsp;&nbsp;
            &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
		init();
        checkFormValidate();
    });

    function checkFormValidate() {
        $("#table_validate").packValidate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.closest(".form-group").append(error);
            },
            
            errorElement: "span",
            rules: {
                name: {
                    required: true,
                    maxlength: 64,
                    space: true
                },
                code: {
                    required: true,
                    maxlength: 64,
                    ZHspace: true
                },
                priceStr: {
                    required: true,
                    maxlength: 6,
                    price: true
                },
                roamingRegion: {
                    required: true
                },
                ownershipRegion: {
                    required: true
                },
                feature: {
                    maxlength: 1024
                },
                status: {
                	required: true
                },
                illustration: {
                	maxlength: 300
                }
            },
            messages: {
                name: {
                    required: "请输入产品名称",
                    maxlength: "产品名称格式错误",
                    space: "产品名称格式错误"
                },
                code: {
                    required: "请输入产品编码",
                    maxlength: "产品编码格式错误",
                    ZHspace: "产品编码格式错误"
                },
                priceStr: {
                    required: "请输入采购价格",
                    maxlength: "采购价格格式错误"
                },
                roamingRegion: {
                    required: "请选择漫游范围"
                },
                ownershipRegion: {
                    required: "请选择使用范围"
                },
                feature: {
                    maxlength: "特征参数最长不超过1024个字符"
                },
                status: {
                	required: "请选择BOSS产品状态"
                },
                illustration: {
                	maxlength: "说明长度不能超过300个字符"
                }
            }
        });
    }
    function init(){
    	var roamingRegion = $('input[name="roamingRegion"]').val();
    	var ownershipRegion = $('input[name="ownershipRegion"]').val();
    	$('a','.dropdown-menu li[data-value='+roamingRegion+']').click();
    	$('a','.dropdown-menu li[data-value='+ownershipRegion+']').click();
    }
</script>

</body>
</html>