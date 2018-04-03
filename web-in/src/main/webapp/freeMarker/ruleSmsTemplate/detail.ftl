<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>
    <title>短信模板详情</title>
    <meta charset="UTF-8">

</head>

<body>
<div class="page-header">
    <h1>短信模板详情<a class="btn btn-white pull-right"
                 href="${contextPath}/manage/rule_sms_template/index.html?pageNum=${pageNum}">返回</a></h1>
</div>
<div class="row">
    <div class="col-xs-12">
        <form class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-3 control-label">模板名称</label>
                <div class="col-sm-9">
                    <label><#if ruleSmsTemplate.name??>${ruleSmsTemplate.name}</#if></label>
                </div>
            </div>
            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">模板类型</label>
                <div class="col-sm-9">
                    <label><#if ruleSmsTemplate.typeName??>${ruleSmsTemplate.typeName}</#if></label>
                </div>
            </div>
            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">创建用户</label>
                <div class="col-sm-9">
                    <label>${creatorName!}</label>
                </div>
            </div>
            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">创建时间</label>
                <div class="col-sm-9">
                    <label><#if ruleSmsTemplate.createTime??>${ruleSmsTemplate.createTime?datetime}</#if></label>
                </div>
            </div>
            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">更新时间</label>
                <div class="col-sm-9">
                    <label><#if ruleSmsTemplate.updateTime??>${ruleSmsTemplate.updateTime?datetime}</#if></label>
                </div>
            </div>
            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">模板内容</label>
                <div class="col-sm-9">
                    <label><#if ruleSmsTemplate.content??>${ruleSmsTemplate.content}</#if></label>
                </div>
            </div>
            <div class="hr hr-24"></div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>
</div>
</body>
</html>