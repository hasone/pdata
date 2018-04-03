<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />

<form class="form-inline definewidth m20" id="table_validate"
      action="${contextPath}/manage/whiteList/index.html"
      method="GET">

    手机号：
    <span>
    	<input type="text"
               name="mobile"
               class="abc input-default" placeholder=""
               autocomplete="off"
               value="${(pageResult.queryObject.queryCriterias.mobile)!}" maxlength="64">
    </span>&nbsp;&nbsp;
    <button type="submit"
            class="btn btn-primary">
        查询
    </button>
    &nbsp;&nbsp;
    <a class="btn btn-success" id="addnew" href="${contextPath}/manage/whiteList/add.html">新增白名单</a>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

<#-- tableBody -->
<#assign fieldsConfig= [
{
"name": "手机号",
"template": r"${row.mobile}"
},
{
"name": "创建时间",
"template": r"${row.createTime?datetime}"
} ,
{
"name": "修改时间",
"template": r"<#if row.updateTime??>
									${row.updateTime?datetime}
								</#if>"
},

{

"name": "操作",
"template": r"
			              <#if row.deleteFlag?? && row.deleteFlag!=1 >
						  <a href='${contextPath}/manage/whiteList/delete.html
									?id=${row.id}&pageNum=${pageResult.queryObject.pageNum}&
									<@mapToQueryString pageResult.queryObject.queryCriterias />' onclick='return Delete();'>删除</a>
                          </#if>
						"
}] />

<@Util.getListView pageResult.list fieldsConfig />

<#include "../pageInfo.ftl" encoding="utf-8" >

<script>
    function Delete() {
        var i = window.confirm("确定要删除吗？");
        return i;
    }
</script>