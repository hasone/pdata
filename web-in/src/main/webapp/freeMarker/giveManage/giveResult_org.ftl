<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />

<html>

<head>
    <meta charset="utf-8"/>
    <title>普通赠送记录</title>
</head>

<body>
<#-- queryBar -->

<#assign status=pageResult.queryObject.queryCriterias.status!>
<#assign mobile=pageResult.queryObject.queryCriterias.mobile!>
<#assign entName=pageResult.queryObject.queryCriterias.entName!>
<#assign prdName=pageResult.queryObject.queryCriterias.prdName!>
<#assign ruleId=pageResult.queryObject.queryCriterias.ruleId!>
<div class="page-header">
    <h1普通赠送记录
    <a href="${contextPath}/manage/giveRuleManager/index.html" class="btn btn-white pull-right">返回</a>
    </h1>
</div>

<div class="col-xs-12">
    <div class="table-search clearfix">
        <form id="searchForm" class="form-inline definewidth m20"
              id="table_validate"
              action="${contextPath}/manage/giveRecordManager/giveResult.html"
              method="POST">
            <div class="col-sm-2"></div>
            <div class="col-sm-10 dataTables_filter">
                <input type="hidden" name="ruleId" id="ruleId" value="${ruleId!}"/>


                电话号码：<input type="text"
                            name="mobile"
                            class="abc input-default"
                            maxlength="64"
                            autocomplete="off"
                            style="width:150px"
                            value="${mobile!}">&nbsp;&nbsp;


                赠送结果：<select style="width:140px" name="status">
                <option value="">全部</option>

            <#list recordStatus?keys as item>
                <option value="${item}"
                        <#if status?? && item == status>selected</#if>>${recordStatus[item]}</option>
            </#list>
            </select>&nbsp;&nbsp;
                <button type="submit" class="btn btn-info">查询</button>
                &nbsp;&nbsp;

            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>

<#-- tableBody -->
<#assign fieldsConfig= [
{
"name": "系统流水号",
"template": r"${row.sysSerialNum!}"
},
{
"name": "被赠送人号码",
"template": r"${row.mobile}"
},

{
"name": "产品名称",
"template": r"${row.prdName}"
},
{
"name": "赠送时间",
"template": r"<#if row.operateTime??> ${row.operateTime?datetime} </#if>"
},
{
"name": "赠送结果",
"template": r"
				<#list recordStatus?keys as key>
					<#if row.status?? && row.status?c==key>${recordStatus[key]}</#if>
				</#list>
                &nbsp;&nbsp;&nbsp;<#if row.status?? && row.status==4> <a href='#' id='${row.id}' class='btnShow' onclick='return false;'>查看原因</a></#if>
			 "
},
{
"name": "操作",
"template": r"<#if row.status?? && row.status==4 && row.creatorId==adminId>
							<a href='${contextPath}/manage/giveRecordManager/chargeAgain.html?id=${row.id!}&ruleId=${ruleId!}&pageNum=${pageNum}'>再次充值 </a>
						  <#else>
						     -
						  </#if>"
}
] />

<@Util.getListView pageResult.list fieldsConfig />

<#-- pageInfo -->
<#include "../pageInfo.ftl" encoding="utf-8" >


    <div id="dialogContent" style="display:none;width:600px;">
        <div>
            <font style="font-weight:bold;font-size:14px;">正在为您数查找据，请稍候。。。</font>
            <img src="${contextPath}/manage/assets/img/load-16-16.gif"/>
        </div>
    </div>

    <link rel="stylesheet" type="text/css" href="${contextPath}/manage/Js/artDialog/skins/default.css">
    <script src="${contextPath}/manage/Js/artDialog/artDialog.js"></script>
    <script src="${contextPath}/manage/Js/artDialog/jquery.artDialog.js"></script>
    <script type="text/javascript">

        $("#searchForm").packValidate({
            rules: {
                mobile: {
                    digits: true,
                    maxlength: 11
                }

            }
        });

        $(function () {

            $(".btnShow").click(function () {
                var dialog = art.dialog({
                    lock: true,
                    title: '查找数据',
                    content: $('#dialogContent')[0],
                    drag: false,
                    resize: false,
                    cancel: function () {
                        return true;
                    },
                    cancelVal: "关闭"
                });


                $.ajax({
                    beforeSend: function (request) {
                        var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                        var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                        request.setRequestHeader(header1, token1);
                    },
                    url: '${contextPath}/manage/giveRecordManager/getRecordInfoAjax.html?id=' + $(this).attr('id'),
                    success: function (data) {
                        $('#dialogContent').html(data);
                    },
                    cache: false
                });

            });
        });
    </script>

</div>
</body>
</html>