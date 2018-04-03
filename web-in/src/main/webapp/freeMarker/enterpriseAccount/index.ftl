<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />


<html>
<head>
    <meta charset="utf-8"/>
    <title>
        新建企业账户
    </title>
</head>

<body>
<div class="page-header">
    <h1>企业账户管理</h1>
</div><!-- /.page-header -->
<div class="row">
    <div class="col-xs-12">
        <div class="table-search clearfix">
            <form class="form-inline definewidth m20" id="table_validate"
                  action="${contextPath}/manage/enterpriseAccount/index.html"
                  method="GET">
                <div class="col-sm-2">
                    <a class="btn btn-info" id="addnew"
                       href="${contextPath}/manage/enterpriseAccount/addOrEditShow.html">新建企业账户</a>
                </div>
                <div class="col-sm-10 dataTables_filter">
                    <label>企业名称：</label>
                    <input type="hidden" name="userName" class="abc input-default enterprise_autoComplete"
                           placeholder="" autocomplete="off"
                           value="${(pageResult.queryObject.queryCriterias.userName)!}"
                           maxlength="64"> &nbsp;
                    <label>状态：</label>
                    <select name="deleteFlag">
                        <option  <#if deleteFlag == "0"> selected </#if> value='0'>未删除</option>
                        <option  <#if deleteFlag == "1"> selected </#if> value='1'>已删除</option>
                        <option  <#if deleteFlag == "2"> selected </#if> value='2'>全部</option>
                    </select>
                    <button type="submit" class="btn btn-bg-white">查询</button>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>

    <#-- tableBody -->
    <#assign fieldsConfig= [
    {
    "name": "企业名称",
    "template": r"<#if row.eName?length lte 10>
							${row.eName}
						  <#else>
						  	${(row.eName?substring(0,10))!}...
						  </#if>",
    "fullName": r"${row.eName}"
    },
    {
    "name": "剩余流量",
    "template": r"${row.volume!}"
    },
    {
    "name": "创建时间",
    "template": r"${(row.createTime)?datetime}"
    },
    {
    "name": "修改时间",
    "template":  r"${(row.updateTime)?datetime}"
    },
    {
    "name": "状态",
    "template": r"<#if row.deleteFlag == 1>
										已删除
									<#else>
										未删除
									</#if>"
    },
    {

    "name": "操作",
    "template": r" <#if row.deleteFlag?? && row.deleteFlag == 0>
										<a href='${contextPath}/manage/enterpriseAccount/addOrEditShow.html?id=${row.id}'>编辑</a>&nbsp;
						 				<a href='${contextPath}/manage/enterpriseAccount/delete.html?id=${row.id} & pageNum=${pageResult.queryObject.pageNum}&
									<@mapToQueryString pageResult.queryObject.queryCriterias />' onclick='return Delete();'>删除</a> 
									 </#if>"
    }] />

    <@Util.getListView pageResult.list fieldsConfig />
    <#include "../pageInfo.ftl" encoding="utf-8" >
    </div>
</div>

<script>
    function Delete() {
        var i = window.confirm("确定要删除吗？");
        return i;
    }
</script>

</body>
</html>
