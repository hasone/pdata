<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新增制卡商</title>
    <meta name="keywords" content="流量平台 新增制卡商"/>
    <meta name="description" content="流量平台 新增制卡商"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/icon.css">

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

        .form .form-group .prompt {
            padding-left: 305px;
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
        <h3>新建制卡商
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/mdrc/cardmaker/index.html'">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/mdrc/cardmaker/save.html" method="post" name="adminForm" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-header">
                新建制卡商
            </div>

            <div class="tile-content">
                <div class="row form">

                    <div class="form-group">
                        <label for="name">制卡商名称：</label>
                        <input type="text" name="name" maxlength="64" id="name" value="${name!}" class="hasPrompt"/>
                    </div>

                    <div class="form-group">
                        <label for="operatorSelect">请设定专员：</label>
                    <#if admins?? && admins?size != 0>
                        <select name="operatorId" id="operatorSelect">
                            <#list admins as admin>
                                <option value="${admin.id}">${(admin.userName)!}</option>
                            </#list>
                        </select>
                    <#else>
                        <select name="operatorId" id="operatorSelect">
                            <option value="">---没有可设定的专员---</option>
                        </select>
                    </#if>
                    </div>

                </div>
                <div class="btn-save mt-30">
                    <input type="submit" class="btn btn-sm btn-warning dialog-btn" value="保存"/>&nbsp;&nbsp;&nbsp;
                    <span style="color:red" id="error_msg">${errorMessage!}</span>
                </div>

            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", "easyui"], function () {
        //验证
        checkFormValidate();
    });

    function onSubmit() {
        $("#table_validate").submit();
        return true;
    }

    function checkFormValidate() {
        jQuery.validator.addMethod("searchBox1", function (value, element, param) {
            return this.optional(element) || (/^[\u4E00-\u9FA5A-Za-z0-9]+$/.test(value));
        }, "只能输入汉字、字母和数字");
        $("#table_validate").packValidate({
            errorPlacement: function (error, element) {
                $(element).parent().find('.error-tip').remove();
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
                    searchBox1: true,
                    remote: {
                        url: "checkName.html",
                        data: {
                            name: function(){
                            	return $('#name').val();
                            }
                        }
                    }
                },
                operatorId: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "请输入制卡商名称",
                    maxlength: "长度不能大于64！",
                    searchBox1: "只能输入数字、字母和汉字！",
                    remote: "名称已存在！"
                },
                operatorId: {
                    required: "请选择制卡员"
                }
            }
        });
    }

</script>
</body>
</html>