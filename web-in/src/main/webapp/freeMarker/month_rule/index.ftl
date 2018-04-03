<#import "../Util.ftl" as Util>
<#global contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>
    <title>包月流量赠送列表</title>
    <meta charset="UTF-8">

</head>

<#assign status=pageResult.queryObject.queryCriterias.status!>
<#assign entName=pageResult.queryObject.queryCriterias.entName!>
<#assign prdName=pageResult.queryObject.queryCriterias.prdName!>
<div class="page-header">
    <h1>包月流量赠送列表
    </h1>
</div>
<div class="col-xs-12">
    <div class="table-search clearfix">

        <form class="form-inline definewidth m20"
              id="table_validate"
              action="${contextPath}/manage/month_rule/index.html"
              method="POST">
            <div class="col-sm-2">
                <div class="form-group">
                    <button type="button" class="btn btn-info" onclick="add()">新建包月流量赠送</button>
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
                    <input type="hidden"
                           name="entName"
                           class="abc input-default enterprise_autoComplete"
                           maxlength="64"
                           autocomplete="off"
                           style="width:150px"
                           value="${entName!}">
                </#if>

                </div>
                &nbsp;&nbsp;
                <div class="form-group">
                    产品名称：<input type="text"
                                name="prdName"
                                class="abc input-default"
                                maxlength="64"
                                autocomplete="off"
                                style="width:150px"
                                value="${productName!}">
                </div>
                &nbsp;&nbsp;
                <div class="form-group">
                    状态：<select name="status">
                    <option value="">全部</option>
                <#list monthRuleStatus?keys as item>
                    <option value="${item}"
                            <#if status?? && item == status>selected</#if>>${monthRuleStatus[item]}</option>
                </#list>
                </select>
                </div>
                &nbsp;&nbsp;
                <div class="form-group">
                    <button type="submit" class="btn btn-bg-white">查询</button>
                </div>
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

    </div>

<#-- tableBody -->
<#assign fieldsConfig= [
{
"name": "企业名称",
"template": r"<#if row.entName?length lte 10>
							${row.entName}
						  <#else>
						  	${(row.entName?substring(0,10))!}...
						  </#if>",
"fullName": r"${row.entName}"
},
{
"name": "产品名称",
"template": r"${row.prdName}"
},

{
"name": "被赠送人总数",
"template": r"${row.total}"
}
,
{
"name": "状态",
"template": r"${monthRuleStatus[row.status?c]}"
},

{
"name": "赠送总月数",
"template": r"${row.monthCount}"
},

{
"name": "首次赠送日期",
"template": r"${row.startTime?date}"
},

{
"name": "末次赠送日期",
"template": r"${row.endTime?date}"
},

{
"name": "创建时间",
"template": r"${row.createTime?datetime}"
} ,
{
"name": "修改时间",
"template": r"${row.updateTime?datetime}"
},

{
"name": "操作",
"template": r"<#if row.status == 0 && row.creatorId == adminId>
							<a href='${contextPath}/manage/month_rule/addOrEdit.html?id=${row.id}'>修改</a>
							<a href='${contextPath}/manage/month_rule/delete.html?ruleId=${row.id}&pageNum=${pageResult.queryObject.pageNum}&
								<@mapToQueryString pageResult.queryObject.queryCriterias/>' onclick='return Delete();'>删除</a>
							<a href='${contextPath}/manage/month_rule/give.html?ruleId=${row.id}&pageNum=${pageResult.queryObject.pageNum}&
								<@mapToQueryString pageResult.queryObject.queryCriterias/>' class='giveConfirm' >赠送</a>
						  </#if>
						<a href='${contextPath}/manage/month_rule/detail.html?id=${row.id}'>详情</a>"
}

] />

<@Util.getListView pageResult.list fieldsConfig />

<#include "../pageInfo.ftl" encoding="utf-8" >

    <script>
        //新增
        function add() {
            window.location.href = "${contextPath}/manage/month_rule/addOrEdit.html";
        }

        $(".giveConfirm").click(function () {
            if (confirm("确定要赠送吗？")) {
                window.location.href = $(this).attr("href");
            }
            else {
                return false;
            }
            return false;
        })

    </script>

    <script>
        function Delete() {
            var i = window.confirm("确定要删除吗？");
            return i;
        }
    </script>

</div>