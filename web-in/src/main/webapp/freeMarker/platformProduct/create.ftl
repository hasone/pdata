<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新建平台产品</title>
    <meta name="keywords" content="流量平台 新建平台产品"/>
    <meta name="description" content="流量平台 新建平台产品"/>

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
            width: 140px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 146px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .province-menu {
            max-height: 250px;
            overflow: auto;
        }
        
        textarea {
            width: 323px;
            resize: none;
            height: 150px;
            outline: none;
            border: 1px solid #ccc;
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
        <h3>新建平台产品
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="${contextPath}/manage/platformProduct/index.html?back=1">返回</a>
        </h3>
    </div>

    <form class="form-horizontal" action="${contextPath}/manage/platformProduct/save.html" method="post"
          name="table_validate" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">

                    <div class="col-md-6">
                        <div class="form-group">
                            <label>平台产品名称：</label>
                            <input type="text" name="name" id="name" required class='hasPrompt' maxlength="64"/>

                            <div class="prompt">产品名称支持汉字、英文字符、数字及特殊字符组成，区分大小写，长度不超过64个字符</div>
                        </div>
                        <div class="form-group">
                            <label>平台产品编码：</label>
                            <input type="text" name="productCode" required id="productCode" class='hasPrompt' maxlength="64"/>

                            <div class="prompt">产品编码由英文字符、数字、下划线、特殊字符组成，区分大小写，长度不超过64个字符</div>
                        </div>
                        <div class="form-group">
                            <label>售出价格：</label>
                            <input type="text" name="priceStr" required id="priceStr" class='hasPrompt numberOnly' maxlength="6"/>

                            <div class="prompt">售出价格最多支持两位小数，长度不超过6个字符，单位元</div>
                        </div>
                        <div class="form-group">
                            <label>产品大小：</label>
                            <input type="text" style="width:240px" required name="sizeStr" id="sizeStr"
                                   class='hasPrompt numberOnly' maxlength="8"/>
                            <select style="width:80px" id="unit" name="unit">
                                <option value="MB">MB</option>
                                <option value="GB">GB</option>
                            </select>

                            <div class="prompt">产品大小最多支持两位小数，长度不超过8字符</div>
                        </div>

                        <div class="form-group">
                            <label>运营商：</label>
                            <div class="btn-group btn-group-sm">

                                <input style="width: 0; height: 0; opacity: 0" name="isp" id="isp" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择运营商</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li data-value="M"><a href="#">移动</a></li>
                                    <li data-value="U"><a href="#">联通</a></li>
                                    <li data-value="T"><a href="#">电信</a></li>
                                    <li data-value="A"><a href="#">三网</a></li>
                                </ul>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label>产品分类：</label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="productCustomerType" id="productCustomerType" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择产品分类</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <#list productCustomerType as pct>
                                        <li data-value="${pct.id}"><a href="#">${pct.name}</a></li>
                                    </#list>
                                </ul>
                            </div>
                        </div>                        
                    </div>

                   <div class="col-md-6">
                        <div class="form-group">
                            <label>状态：</label>
                            <select id="status" name="status" required>
                                <option value="0" selected>下架</option>
                                <option value="1">上架</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label>是否默认关联：</label>
                            <select id="defaultvalue" name="defaultvalue" required>
                                <option value="0" selected>否</option>
                                <option value="1">是</option>
                            </select>
                            <div class="prompt">创建企业时是否默认关联该产品</div>
                        </div>

                        <div class="form-group">
                            <label>使用范围：</label>

                            <div class="btn-group btn-group-sm">

                                <input style="width: 0; height: 0; opacity: 0" name="ownershipRegion"
                                       id="ownershipRegion" required>
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
                                       required>
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
	                        <label>说明：</label>
	                        <textarea rows="10" cols="100" maxlength="300" id="illustration" 
	                        	name="illustration" placeholder="" class='hasPrompt'
	                            style="vertical-align: top"></textarea>
                        	<div class="prompt">说明长度不超过300个字符</div>
                    	</div>
                    	
                    </div>
                </div>

            </div>
        </div>

        <div class="mt-30 text-center">
            <input type="submit" class="btn btn-warning btn-sm mr-10 dialog-btn" style="width: 114px;" value="下一步"/>&nbsp;&nbsp;
            &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {

        checkFormValidate();
        listeners();
    });

    function listeners(){
        $("input.numberOnly").on("input", function(){
            var val = $(this).val();
            val = val.replace(/[^0-9.]+/g, "");
            $(this).val(val);

            if (val.length == $(this).attr("maxlength")) {
                $(this).blur();
            }
        });
    }

    function checkFormValidate() {
        $("#table_validate").packValidate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.closest(".form-group").append(error);
            },
            errorElement: "span",
            rules: {
                supplierId: {
                    required: true
                },
                isp: {
                    required: true
                },
                name: {
                    required: true,
                    maxlength: 64,
                    space: true
                },
                productCode: {
                    required: true,
                    maxlength: 64,
                    ZHspace: true,
                    remote: {
                        url: "checkProductCodeUnique.html",
                        data: {
                            productCode: function () {
                                return $('#productCode').val();
                            }
                        }
                    }
                },
                sizeStr: {
                    required: true,
                    maxlength: 8,
                    price: true
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
                illustration:{
                	maxlength: 300
                }
            },
            messages: {
                supplierId: {
                    required: "请选择供应商"
                },
                isp: {
                    required: "请选择运营商"
                },
                name: {
                    required: "请输入产品名称",
                    maxlength: "产品名称格式错误",
                    space: "产品名称格式错误"
                },
                productCode: {
                    required: "请输入产品编码",
                    maxlength: "产品编码格式错误",
                    ZHspace: "产品编码格式错误",
                    remote: "系统中已存在该产品编码的平台产品"
                },
                sizeStr: {
                    required: "请输入产品大小",
                    maxlength: "产品大小格式错误",
                    price: "产品大小格式错误"
                },
                priceStr: {
                    required: "请输入售出价格",
                    maxlength: "售出价格格式错误",
                    price: "售出价格格式错误"
                },
                roamingRegion: {
                    required: "请选择漫游范围"
                },
                ownershipRegion: {
                    required: "请选择使用范围"
                },
                illustration:{
                	maxlength: "说明长度不能超过300字符"
                },
                productCustomerType: {
                    required: "请选择产品分类"
                }
            }
        });
    }
</script>

</body>
</html>