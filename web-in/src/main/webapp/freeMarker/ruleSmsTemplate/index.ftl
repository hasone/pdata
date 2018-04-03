<#assign contextPath = rc.contextPath >
<#import "../Util.ftl" as Util>
<!DOCTYPE html>
<html>

<head>
    <title>短信模板管理</title>

    <meta charset="UTF-8">

</head>

<body>

<div class="page-header">
    <h1>短信模板管理</h1>
</div><!-- /.page-header -->

<#assign name = (pageResult.queryObject.queryCriterias.name)! >

<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->

        <div class="table-search clearfix">
            <form class="form-inline definewidth m20" id="table_validate"
                  action="${contextPath}/manage/rule_sms_template/index.html" method="GET">

                <div class="col-sm-2">
                    <a class="btn btn-info" id="addnew"
                       url="${rc.contextPath}/manage/rule_sms_template/add.html">新增短信模板</a>
                </div>

                <div class="col-sm-10 dataTables_filter">
                    <label> 短信模板名称：</label>
                    <input type="text" name="name" class="abc input-default"
                           autocomplete="off" value="${templateName!}" maxlength="64">&nbsp;&nbsp;

                    <button type="submit" class="btn btn-bg-white"> 查询</button>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>

    <#assign fieldsConfig= [
    {
    "name": "名称",
    "template": r"<#if row.name?length lte 10>
							${row.name}
						  <#else>
						  	${(row.name?substring(0,10))!}...
						  </#if>",
    "fullName": r"${row.name}"
    },


    {
    "name": "模板类型",
    "template": r"${row.typeName!}"
    },

    {
    "name": "创建时间",
    "template": r"${row.createTime?datetime}"
    },

    {
    "name": "更新时间",
    "template": r"${row.updateTime?datetime}"
    },

    {
    "name": "操作",
    "template": r"<a href='${rc.contextPath}/manage/rule_sms_template/edit.html?id=${row.id}&pageNum=${pageNum}'>编辑</a>&nbsp;
							<a href='${rc.contextPath}/manage/rule_sms_template/detail.html?id=${row.id}&pageNum=${pageNum}'>详情</a>
							"

    }] />

    <@Util.getListView pageResult.list fieldsConfig />
        <script>
            $(function () {
                $('#addnew').click(function () {
                    window.location.href = "${rc.contextPath}/manage/rule_sms_template/add.html";
                });
            });
        </script>

        <script>
            function Delete() {
                var i = window.confirm("确定要删除吗？");
                return i;
            }
        </script>

    <#-- pageInfo -->
    <#include "../pageInfo.ftl" encoding="utf-8" >
</body>
</html>