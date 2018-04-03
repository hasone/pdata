<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />

<html>

<head>
    <meta charset="utf-8"/>
    <title>普通赠送列表</title>
</head>

<body>
<div class="page-header">
    <h1>普通赠送列表</h1>
</div>
<#-- queryBar -->

<div class="col-xs-12">
    <div class="table-search clearfix">
        <form class="form-inline definewidth m20" id="table_validate"
              action="${contextPath}/manage/giveRuleManager/index.html">
            <div class="col-sm-2">
                <div class="form-group">
                    <a class="btn btn-info" id="addnew" href="${contextPath}/manage/giveRuleManager/add.html">新建普通赠送</a>
                </div>
            </div>

            <div class="col-sm-10 dataTables_filter">
                <div class="form-group">

                <#if role =="ENTERPRISE_CONTACTOR">    <!--企业关键人 -->
                    企业名称：
                    <#list enterprises as e>
                        <span style="display: inline-block;padding: 4px 0 4px;font-size: 13px;line-height: 19px;">${e.name}</span>
                    </#list>
                <#else>          <!--客户经理 -->
                    企业名称：
                    <input type="hidden" name="entName" class="abc input-default enterprise_autoComplete" autocomplete="off"
                           placeholder="" value="${(pageResult.queryObject.queryCriterias.entName)!}" maxlength="64">
                </#if>

                </div>
                &nbsp;&nbsp;
                <div class="form-group">
                    产品名称：
                    <input type="text" name="pName" class="abc input-default" autocomplete="off"
                           placeholder="" value="${(pageResult.queryObject.queryCriterias.pName)!}" maxlength="64">&nbsp;&nbsp;
                </div>
                <div class="form-group">
                    状态：<select name="status">
                    <option value="-1">全部</option>
                    <option <#if status == "0"> selected </#if>
                                                value="0">待赠送
                    </option>
                    <option <#if status == "1"> selected </#if>
                                                value="1">进行中
                    </option>
                    <option <#if status == "2"> selected </#if>
                                                value="2">已完成
                    </option>
                    <option <#if status == "3"> selected </#if>
                                                value="3">已删除
                    </option>
                </select>&nbsp;&nbsp;
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-bg-white">查询</button>
                    &nbsp;&nbsp;
                </div>
                &nbsp;&nbsp; <span class='empty hide'>查询字符串长度不能超过64</span>
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
"name": "产品名称",
"template": r"${row.pName}"
},
{
"name": "被赠送人总数",
"template": r"${row.total!}"
},
{
"name": "状态",
"template": r"<#if row.deleteFlag?? && row.deleteFlag == 1> 已删除
			<#else>
			  <#if row.status?? && row.status==0>待赠送</#if>
			  <#if row.status?? && row.status==1>进行中</#if>
			  <#if row.status?? && row.status==2>已完成</#if>
			</#if>"
},
{
"name": "创建时间",
"template": r"${row.createTime?datetime}"
},

{
"name": "修改时间",
"template": r"${row.updateTime?datetime}"
},

{
"name": "操作",
"template": r"<#if row.deleteFlagVo?? && row.deleteFlagVo=='0' >
							<#if row.creatorId == adminId && row.status==0>
								<#if row.total?? && row.total gte 1>
									<a href='${contextPath}/manage/giveRuleManager/giveStart.html?ruleId=${row.id}&type=3' onclick='return goback();'> 赠送</a>
								</#if>
								<a href='${contextPath}/manage/giveRuleManager/editRule.html?ruleId=${row.id}'>编辑</a>
						 		<a href='${contextPath}/manage/giveRuleManager/delete.html?ruleId=${row.id}&pageNum=${pageResult.queryObject.pageNum}&
									<@mapToQueryString pageResult.queryObject.queryCriterias />' onclick='return Delete();'>删除</a>
							<#else>
								<a href='${contextPath}/manage/giveRuleManager/detail.html?id=${row.id}'>详情</a>
							</#if>
						<#else>
							<a href='${contextPath}/manage/giveRuleManager/detail.html?id=${row.id}'>详情</a>
						</#if>"
}] />

<@Util.getListView pageResult.list fieldsConfig />

<#-- pageInfo -->
<#include "../pageInfo.ftl" encoding="utf-8" >

    <script>
        function goback() {
            return confirm("是否确定赠送？");
        }
        ;
    </script>
</div>

<script>
    function Delete() {
        var i = window.confirm("确定要删除吗？");
        return i;
    }
</script>

</body>
</html>