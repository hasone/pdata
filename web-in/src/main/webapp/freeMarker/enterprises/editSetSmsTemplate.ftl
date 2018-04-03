<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-设置短信模板</title>
    <meta name="keywords" content="流量平台 设置短信模板"/>
    <meta name="description" content="流量平台 设置短信模板"/>

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

        .form .form-group .prompt {
            padding-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .form .form-group ul {
            margin: -25px 0 0 305px;
        }

        .btn-save {
            margin-left: 300px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>设置短信模板
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <form id="table_validate" action="${contextPath}/manage/enterprise/saveSetSmsTemplate.html" method="post"
          role="form">
        <div class="tile mt-30">
            <input type="hidden" name="enterId" id="enterId" value="${enterId!}">
            <div class="tile-content">
                <div class="row form">

                    <div class="form-group">
                        <label>短信模板：</label>
                        <ul>                 
                            <span class="input-checkbox">

                            <#if nowTemplates??>
                                <#list nowTemplates as nowTemplate>
                                    <#if nowTemplate.status == 1>
                                        <li>
                                            <input type="radio" value="${nowTemplate.smsTemplateId!}" name="sms"
                                                   id="${nowTemplate.smsTemplateId!}" checked="checked" class="hidden">
                                            <label style="width:30px" for="${nowTemplate.smsTemplateId!}"
                                                   class="c-checkbox mr-15 rt-1 checked"></label>
                                            <span>${nowTemplate.content}</span>
                                        </li>
                                    <#else>
                                        <li>
                                            <input type="radio" value="${nowTemplate.smsTemplateId!}" name="sms"
                                                   id="${nowTemplate.smsTemplateId!}" class="hidden">
                                            <label style="width:30px" for="${nowTemplate.smsTemplateId!}"
                                                   class="c-checkbox mr-15 rt-1"></label>
                                            <span>${nowTemplate.content}</span>
                                        </li>
                                    </#if>

                                <li>


                                </#list>

                                <#if openSms??>
                                    <li>
                                        <input type="radio" value="0" name="sms" id="0" class="hidden">
                                        <label style="width:30px" for="0" class="c-checkbox mr-15 rt-1"></label>
                                        <span>关闭短信</span>
                                    </li>
                                <#else>
                                    <li>
                                        <input type="radio" value="0" name="sms" id="0" checked="checked"
                                               class="hidden">
                                        <label style="width:30px" for="0" class="c-checkbox mr-15 rt-1 checked"></label>
                                        <span>关闭短信</span>
                                    </li>
                                </#if>
                            </#if>
                        	</span>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-save mt-30">
            <input type="submit" class="btn btn-sm btn-warning dialog-btn" value="保存"/>&nbsp;&nbsp;&nbsp;
            <span style="color:red" id="error_msg">${errorMsg!}</span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {

    });
</script>


</body>
</html>