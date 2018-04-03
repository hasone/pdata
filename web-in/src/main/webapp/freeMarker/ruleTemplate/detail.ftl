<#global contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>
    <title>活动模板详情</title>
    <meta charset="UTF-8">
</head>
<body>
<div class="page-header">
    <h1>活动模板详情<a class="btn btn-white pull-right"
                 href="${contextPath}/manage/rule_template/index.html?pageNum=${pageNum}">返回</a></h1>
</div>
<div class="row">
    <div class="col-xs-12">
        <form class="form-horizontal" role="form">

            <div class="form-group">
                <label class="col-sm-3 control-label">模板名称</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.name!}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">红包主题</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.title!}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">红包描述</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.description!}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">活动用户</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.people!}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">活动描述</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.activityDes!}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">创建日期</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.createTime?string('yyyy-MM-dd HH:mm:ss')}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">图片个数</label>
                <div class="col-sm-9">
                    <label>${ruleTemplate.imageCnt!}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">图片预览</label>
                <div class="col-sm-9">
                <#if ruleTemplate.image??>
                    <img src="${contextPath}/manage/rule_template/getFile.html?id=${ruleTemplate.id}&filename=${ruleTemplate.image}"
                         style="max-width:360px;"/>
                <#else>
                    -
                </#if>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>
</div>

</body>
</html>