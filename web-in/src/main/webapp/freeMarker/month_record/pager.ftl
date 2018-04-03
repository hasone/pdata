<#import "../Util.ftl" as Util>
<#global contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>
    <title>包月赠送记录</title>
    <meta charset="UTF-8">
    <script>
        function doback() {
            window.location.href = "${contextPath}/manage/month_rule/detail.html?id=${rule.id}"
        }
    </script>
</head>
<div class="page-header">
    <h1>包月赠送记录
        <button type="button" class="btn btn-white pull-right" onclick="doback()">返回</button>
    </h1>
</div>
<div class="col-xs-12">
    <div class="table-search clearfix">
        <form id="searchForm" action="${contextPath}/manage/month_record/pager.html" method="post">
            <div class="col-sm-12 dataTables_filter">

                <input type="hidden" name="ruleId" value="${rule.id}"/>
                共赠送${rule.monthCount}期,首次赠送日期${rule.startTime?date},末次赠送日期${rule.endTime?date}

                <th width="60" class="tableleft">当前期数</th>

            <#assign serialNumber=queryObject.queryCriterias.serialNumber!>
            <#assign mobile=queryObject.queryCriterias.mobile!>
            <#assign status=queryObject.queryCriterias.status!>
                <select style="width:80px" name="serialNumber">
                <#list 1..rule.monthCount as x>
                    <option value="${x}" <#if serialNumber?? && x?c == serialNumber>selected</#if>>${x}</option>
                </#list>
                </select>
                电话号码：<input type="text"
                            name="mobile"
                            class="abc input-default"
                            maxlength="64"
                            autocomplete="off"
                            style="width:100px"
                            value="${mobile!}">
                赠送结果：<select style="width:130px" name="status">
                <option value="">全部</option>
            <#list monthRecordStatus?keys as item>
                <option value="${item}"
                        <#if status?? && item == status>selected</#if>>${monthRecordStatus[item]}</option>
            </#list>
            </select>
                <button type="submit" class="btn btn-bg-white">查询</button>

            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>
<#assign columName = "赠送日期" >
<#if rule.status==3>
    <#assign columName = "删除日期" >
</#if>
<#-- tableBody -->
<#assign fieldsConfig= [
{
"name": "序号",
"template": r"${row_index+1}"
},
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
"template": r"${rule.prdName}"
}
,
{
"name": "当前状态",
"template": r"${monthRecordStatus[row.status?c]}
			&nbsp;&nbsp;&nbsp;<#if row.status?? && row.status==4> <a href='#' id='${row.id}' class='btnShow' onclick='return false;'>查看原因</a></#if>"
},

{
"name": "${columName}",
"template": r"<#if rule.status==3>${rule.updateTime?datetime }<#else>${row.operateTime?datetime }</#if>"
},

{
"name": "操作",
"template": r"<#if row.status?? && row.status==4 && rule.creatorId==adminId>
							<a href='${contextPath}/manage/month_record/chargeAgain.html?id=${row.id!}&ruleId=${ruleId!}'>再次充值 </a>
						<#else>-</#if>"
} ] />

<@Util.getListView pageResult.list fieldsConfig />

<#include "../pageInfo.ftl" encoding="utf-8" >
</div>
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
                url: '${contextPath}/manage/month_record/getRecordInfoAjax.html?id=' + $(this).attr('id'),
                success: function (data) {
                    $('#dialogContent').html(data);
                },
                cache: false
            });

        });
    });
</script>