<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品管理</title>
    <meta name="keywords" content="流量平台 产品管理"/>
    <meta name="description" content="流量平台 产品管理"/>

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
        .form {
            width: 900px;
        }

        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 5px;
            width: 350px;
        }

        .form .form-group label {
            width: 300px;
            text-align: right;
        }

        #productSize {
            width: 330px;
        }

        #price {
            width: 330px;
        }

        .form .form-group .prompt1,
        .form .form-group .prompt2,
        .form .form-group .prompt3 {
            margin-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .btn-save {
            margin-left: 300px;
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>产品信息编辑
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/product/saveEdit.html" method="POST" name="enterprisesForm" id="table_validate">
        <input type="hidden" id="id" name="id" value="${(product.id)!}"/>

        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>产品名称:</label>
                        <input type="text" id="name" name="name" value="${(product.name)!}" class='hasPrompt'
                               onfocus="$('#isModifyChanged').val('true')"/>
                    <#--&lt;#&ndash;<span style="color:red" id="tip_name" ></span>&ndash;&gt;<br />-->
                        <span class="prompt1">产品名称由汉字、英文字符及数字组成，长度不超过64个字符</span>
                    </div>

                    <div class="form-group">
                        <label>产品编码:</label>
                        <input type="text" id="productCode" name="productCode" value="${(product.productCode)!}"
                               class='hasPrompt' onfocus="$('#isModifyChanged').val('true')"/>
                    <#--<span style="color:red" id="tip_productCode" ></span><br />-->
                        <span class="prompt2">产品编码由英文字符、数字及下划线组成，长度不超过20个字符</span>
                    </div>

                    <div class="form-group">
                        <label>流量包大小:</label>
                        <input type="text" name="productSize" id="productSize" value="${(product.productSize)!}"
                               class='hasPromt' onfocus="$('#isModifyChanged').val('true')"/>
                        <span style="width:65px">KB   </span>
                        <!--单位
                            <select name="unit" style="width:65px">
                                <option value = "TB" <#if product.unit??&&product.unit=="TB">selected</#if> > TB </option>
                                <option value = "GB" <#if product.unit??&&product.unit=="GB">selected</#if> > GB </option>
                                <option value = "MB" <#if product.unit??&&product.unit=="MB">selected</#if> > MB </option>
                                <!--<option value = "KB" <#if product.unit??&&product.unit=="KB">selected</#if> > KB </option>
                            </select><br/>
                            -->
                        <span class="prompt3">流量包大小由数字组成,长度不超过8字符。</span>
                    </div>
                    <div class="form-group">
                        <label>产品价格:</label>
                        <input type="text" name="price" id="price" value="${(product.price)!}" class='hasPromt'
                               onfocus="$('#isModifyChanged').val('true')"/>
                        <span style="width:65px">分   </span>
                        <span class="prompt3">产品价格由数字组成，单位为分，长度不超过12个字符。</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-save mt-30">
            <input type="submit" class="btn btn-sm btn-warning dialog-btn" value="保存"/>
        <#--<span style="color:red" id="error_msg">${errorMsg!}</span>-->
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        check();
    });
</script>

<script>
    function check() {
        $("#table_validate").packValidate({

            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if ($(element).attr("id") == "productSize" || $(element).attr("id") == "price") {
                    $(element).next().after(error);
                } else {
                    element.after(error);
                }
            },

            errorElement: "span",
            rules: {
                name: {
                    required: true,
                    maxlength: 64,
                    searchBox: true,
                    remote: "checkName.html?id=${(product.id)!}"

                },
                productCode: {
                    required: true,
                    maxlength: 20,
                    format1: true,
                    remote: "checkProductCode.html?id=${(product.id)!}"
                },
                productSize: {
                    required: true,
                    maxlength: 8,
                    positive: true
                },
                price: {
                    required: true,
                    maxlength: 12
                }
            },
            messages: {
                name: {
                    required: "产品名称不能为空!",
                    maxlength: "字符长度最大为64位!",
                    remote: "该产品名称已存在!"
                },
                productCode: {
                    required: "产品编码不能为空!",
                    maxlength: "字符长度最大为20位!",
                    remote: "该产品编码已存在!"
                },
                productSize: {
                    required: "流量包大小不能为空!",
                    maxlength: "字符长度最大为8位!",
                    positive: "只能输入正整数!"
                },
                price: {
                    maxlength: "产品价格长度不超过12个字符!",
                    required: "产品价格不能为空!"
                }
            }
        });
    }
</script>
</body>
</html>