<!DOCTYPE html>

<#global  contextPath = rc.contextPath />

<html>

<head>
    <title>
    <#if globalConfig.id??>
        编辑配置项
    <#else>
        新建配置项
    </#if>
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
        <#if globalConfig.id??>
            编辑配置项
        <#else>
            新建配置项
        </#if>

            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               href="${contextPath}/manage/globalConfig/index.html?back=1">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/globalConfig/addOrEditProduct.html"
          method="post" name="globalConfigForm" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">
                    <input type="hidden" name="id" value="${(globalConfig.id)! }"/>
                    <input type="hidden" name="isModifyChanged" id="isModifyChanged" value="false"/>
                    <input type="hidden" name="deleteFlag" value="0"/>

                    <div class="form-group">
                        <label>配置名称:</label>
                        <input type="text" id="name" name="name" maxlength="64" required
                               data-bind="value: userName, valueUpdate: 'afterkeydown'"
                               class="hasPrompt" <#if globalConfig.id?? > readonly="readonly" </#if>
                               value="${(globalConfig.name)!}""/>
                        <span class="prompt">配置名称由汉字、英文字符及数字组成，长度不超过64个字符</span>
                    </div>

                    <div class="form-group">
                        <label style="vertical-align: top;">描述: </label>
                            <textarea rows="10" cols="55" name="description" class="hasPrompt"
                                      maxlength="100" required
                                      onfocus="$('#isModifyChanged').val('true')">${(globalConfig.description)!}</textarea>
                        <br/><span class="prompt">配置描述长度不超过100个字符</span>
                    </div>

                    <div class="form-group">
                        <label>Key值:</label>
                        <input type="text" name="configKey" required
                               value="${(globalConfig.configKey)!}"
                               onfocus="$('#isModifyChanged').val('true')"/>
                        <span class="prompt">Key值由汉字、英文字符、数字及下划线组成，长度不超过50个字符</span>
                    </div>

                    <div class="form-group">
                        <label>Value值:</label>
                        <input type="text" name="configValue" required
                               value="${(globalConfig.configValue)!?html}"
                               onfocus="$('#isModifyChanged').val('true')"/>
                        <br/><span class="prompt">Value值长度不超过1024个字符</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-save mt-30 text-center">
            <input type="submit" class="btn btn-sm btn-warning dialog-btn" value="保存"/>&nbsp;&nbsp;&nbsp;
            <span style="color:red" id="error_msg">${errorMsg!}</span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
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
                                                  maxlength: 64,
                                                  remote: "check.html?id=${globalConfig.id!}"
                                              },
                                              description: {
                                                  required: true,
                                                  maxlength: 300
                                              },
                                              configKey: {
                                                  required: true,
                                                  maxlength: 50,
                                                  remote: "check.html?id=${globalConfig.id!}",
                                                  noSpecial: true
                                              },
                                              configValue: {
                                                  required: true,
                                                  maxlength: 1024
                                              }
                                          },
                                          messages: {
                                              name: {
                                                  remote: "配置名称已经存在!"
                                              },
                                              configKey: {
                                                  remote: "Key值已经存在!"
                                              }
                                          }
                                      });

        $(function () {
            $('#backid').click(function () {
                window.location.location = "${contextPath}/manage/globalConfig/index.html";
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