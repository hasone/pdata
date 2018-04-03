<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />

<html>

<head>
    <meta charset="utf-8"/>
    <title>AppInfo列表</title>
</head>

<body>
<div class="page-header">
    <h1>AppInfo列表</h1>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->


    <#-- tableBody -->
    <#assign fieldsConfig= [
    {
    "name": "App编码",
    "template": r"<#if row.id?length lte 10>
										${row.id}
						  			  <#else>
						  				${(row.id?substring(0,10))!}...
						  			  </#if>",
    "fullName": r"${row.id}"
    },

    {
    "name": "企业编码",
    "template": r"${row.enterpriseCode}"
    },

    {
    "name": "AppKey",
    "template": r"<#if row.appKey?length lte 10>
										${row.appKey}
						  			  <#else>
						  				${(row.appKey?substring(0,10))!}...
						  			  </#if>"
    },

    {
    "name": "AppSecret",
    "template": r"<#if row.appSecret?length lte 10>
										${row.appSecret}
						  			  <#else>
						  				${(row.appSecret?substring(0,10))!}...
						  			  </#if>"
    },

    {
    "name": "创建时间",
    "template": r"${row.createTime?datetime}"
    },

    {
    "name": "修改时间",
    "template": r"${row.updateTime?datetime}"
    }

    ] />

    <@Util.getListView pageResult.list fieldsConfig />

    <#-- pageInfo -->
    <#include "../pageInfo.ftl" encoding="utf-8" >


    </div>
</div>
</body>
</html>