<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>
    <title>短信详情</title>
    <meta charset="UTF-8">

</head>

<body>
<div class="page-header">
    <h1>短信详情<a class="btn btn-white pull-right" href="${contextPath}/manage/sms/index.html">返回</a></h1>
</div>
<div class="row">
    <div class="col-xs-12">
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-3 control-label">名称</label>
                <div class="col-sm-9">
                    <label><#if smsTemplate.name??>${smsTemplate.name}</#if></label>
                </div>
            </div>
            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">内容</label>
                <div class="col-sm-9">
                    <label><#if smsTemplate.content??>${smsTemplate.content}</#if></label>
                </div>
            </div>
            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">创建时间</label>
                <div class="col-sm-9">
                    <label><#if smsTemplate.createTime??>${smsTemplate.createTime?datetime}</#if></label>
                </div>
            </div>
            <div class="hr hr-24"></div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>
</div>
</body>
</html>