<!DOCTYPE html>

<#global  contextPath = rc.contextPath />

<html>

<head>
    <title>

    </title>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="keywords" content="流量平台 全局配置"/>
    <meta name="description" content="流量平台 全局配置"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

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
            display: block;
        }

        span.error {
            color: red;
            margin-left: 10px;
        }

    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>
<div class="main-container">
    <div>
        <h3>
        <#if authority.authorityId??>
            编辑权限
        <#else>
            新建权限
        </#if>

            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="javascript:history.go(-1);">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/authorityManage/saveAuthority.html?${_csrf.parameterName}=${_csrf.token}"
          method="post" name="authorityForm" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">
                    <input type="hidden" name="authorityId" value="${(authority.authorityId)! }"/>
                    <input type="hidden" name="isModifyChanged" id="isModifyChanged" value="false"/>
                    <input type="hidden" name="deleteFlag" value="0"/>

                    <div class="form-group">
                        <label>权限名称:</label>
                        <input type="text" id="name" name="name" maxlength="64" required
                               data-bind="value: userName, valueUpdate: 'afterkeydown'"
                               class="hasPrompt" <#if authority.authorityId?? > readonly="readonly" </#if>
                               value="${(authority.name)!}""/>
                        <span class="prompt">配置名称由汉字、英文字符及数字组成，长度不超过20个字符</span>
                    </div>

                    <div class="form-group">
                        <label>权限access:</label>
                        <input type="text" id="authorityName" name="authorityName" maxlength="64" required
                               data-bind="value: userName, valueUpdate: 'afterkeydown'"
                               class="hasPrompt" value="${(authority.authorityName)!}""/>
                        <span class="prompt">权限access由英文字符及数字组成，以ROLE_开头，长度不超过50个字符</span>
                    </div>

                    <div class="form-group">
                        <label>权限code:</label>
                        <input type="text" name="code" required
                               value="${(authority.code)!}"/>
                        <span class="prompt">Key值由数字，长度不超过6个字符</span>
                    </div>

                </div>
            </div>
        </div>

        <div class="btn-save mt-30 text-center">
            <input type="submit" class="btn btn-sm btn-warning dialog-btn" value="保存"/>&nbsp;&nbsp;&nbsp;
            <span style="color:red" id="error_msg">${errorMsg!}</span>
        </div>
    </form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        $("#table_validate").validate({
            errorElement: "span",
            rules: {
                name: {
                    required: true,
                    maxlength: 20,
                    searchBox: true
                },
                authorityName: {
                    required: true,
                    maxlength: 50
                },
                code: {
                    required: true,
                    maxlength: 6,
                    noSpecial: true
                },
            },
            messages: {
                name: {
                    required: "权限名称不能为空!",
                    maxlength: "权限名称不能超过20个字符!"
                },
                authorityName: {
                    required: "权限access不能为空!",
                    maxlength: "权限access不能超过50个字符!"
                },
                code: {
                    required: "权限编码不能为空!",
                    maxlength: "权限编码不能超过6个字符!"
                }
            }
        });

        $(function () {
            $('#backid').click(function () {
                window.location.location = "${contextPath}/manage/authorityManage/index.html";
            });

            String.prototype.trim = function () {
                return this.replace(/(^\s*)|(\s*$)/g, '');
            }

        });
    });


    function goback() {
        if ($('#isModifyChanged').val() == 'true') {
            return confirm("是否确定不保存记录退出？");
        }
        else {
            return true;
        }
    }
</script>

</body>
</html>