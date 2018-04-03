<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-编辑供应商</title>
    <meta name="keywords" content="流量平台 编辑供应商"/>
    <meta name="description" content="流量平台 编辑供应商"/>

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

        .form .form-group label {
            width: 180px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 186px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
        
        .error-tip{
        	display: block;
        	margin-left: 183px;
        }
        #status-error{
        	margin-left:0;
        }
    </style>
</head>
<body>

<div class="main-container">


    <div class="module-header mt-30 mb-20">
        <h3>编辑供应商
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="${contextPath}/manage/supplier/index.html">返回</a>
        </h3>
    </div>

    <form class="form-horizontal" action="${contextPath}/manage/supplier/saveEdit.html" method="post"
          name="table_validate" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">
					
                    <div class="col-md-8 col-md-offset-2">
                    	<input type="hidden" name="id" class='hasPrompt' value="${supplier.id!}"/>
                        <div class="form-group">
                            <label>供应商：</label>
                            <input type="text" name="name" id="name" class='hasPrompt' maxLength="64" value="${supplier.name!}"
                                   required/>
                            <#--<div class="prompt">产品名称支持汉字、英文字符、数字及特殊字符组成，区分大小写，长度不超过64个字符</div>-->
                        </div>
                        
                        <div class="form-group">
                            <label>供应商指纹：</label>
                            <input type="text" name="fingerprint" id="fingerprint" class='hasPrompt' maxLength="200" value="${supplier.fingerprint!}"
                                   required/>

                            <#--<div class="prompt">产品编码由英文字符、数字、下划线、特殊字符组成，区分大小写，长度不超过64个字符</div>-->
                        </div>

                        <div class="form-group">
                            <label>状态：</label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="status" id="status" 
                                	value="${supplier.status!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">
                                	<#if supplier.status?? && supplier.status==0>下架</#if>
                                	<#if supplier.status?? && supplier.status==1>上架</#if>
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
                            <label>是否有充值查询接口：</label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="isQueryChargeResult" id="isQueryChargeResult" 
                                	value="${supplier.isQueryChargeResult!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">
                                	<#if supplier.isQueryChargeResult?? && supplier.isQueryChargeResult==0>无</#if>
                                	<#if supplier.isQueryChargeResult?? && supplier.isQueryChargeResult==1>有</#if>
                                </button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li data-value="0"><a href="#">无</a></li>
                                    <li data-value="1"><a href="#">有</a></li>
                                </ul>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label>付款方式：</label>
                            <input type="text" name="payType" id="payType" value="${supplier.payType!}" class='hasPrompt' maxLength="32"/>
                        </div>
                        
                        <div class="form-group">
                            <label>合同编号：</label>
                            <input type="text" name="contractCode" id="contractCode" value="${supplier.contractCode!}" class='hasPrompt' maxLength="32"/>
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

        checkFormValidate();
    });

    function checkFormValidate() {
        $("#table_validate").packValidate({
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
                name: {
                    required: true,
                    maxlength: 64,
                    space: true,
                    remote: {
                        url: "checkNameUnique.html",
                        data: {
                            name: function () {
                                return $('#name').val()
                            },
                            supplierId : function () {
                                return "${supplier.id!}"
                            }
                        }
                    }
                },
                fingerprint: {
                    required: true,
                    maxlength: 200,
                    ZHspace: true,
                    remote: {
                        url: "checkFingerprintUnique.html",
                        data: {
                            name: function () {
                                return $('#fingerprint').val()
                            },
                            supplierId : function () {
                                return "${supplier.id!}"
                            }
                        }
                    }
                },
                payType: {
                	maxlength: 32,
                	space: true,
                	payType: true
                },
                contractCode: {
                	maxlength: 32,
                	space: true,
                	contract: true
                }
            },
            messages: {
                name: {
                    required: "请正确填写供应商名称",
                    maxlength: "供应商名称长度不超过64个字符",
                    remote: "供应商名称已经存在"
                },
                fingerprint: {
                    required: "请正确填写供应商指纹",
                    maxlength: "供应商指纹长度不超过200个字符",
                    remote: "供应商指纹已经存在"
                },
                payType: {
                	maxlength: "付款方式长度不超过32个字符",
                	space: "不能输入空格",
                	payType: "付款方式只能输入汉字、字母或数字"
                },
                contractCode: {
                	maxlength: "合同编码长度不超过32个字符",
                	space: "不能输入空格",
                	contract: "支持字母、数字、()、-、[]、长度不超过32个字符，其中特殊字符支持中英文输入"
                }
            }
        });
    }
</script>

</body>
</html>