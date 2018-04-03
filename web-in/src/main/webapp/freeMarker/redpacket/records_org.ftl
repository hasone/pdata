<#import "../Util.ftl" as Util>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>抢红包记录</title>
    <script>
        <#global  contextPath = rc.contextPath />

        $(function () {
            //返回详情
            $("#retunDetails").click(function () {
                window.location.href = "${contextPath}/manage/entRedpacket/getDetail.html?id=${(pageResult.queryObject.queryCriterias.redpacketID)!}"
            });

            //返回列表
            $("#returnRuleList").click(function () {
                window.location.href = "${contextPath}/manage/entRedpacket/index.html"
            });
        });
    </script>
</head>

<body>
<div class="page-header">
    <h1>抢红包记录
        <button class="btn btn-white pull-right"
                onclick="javascript:window.location.href='${contextPath}/manage/entRedpacket/getDetail.html?id=${redpacketId}'">
            返回
        </button>
    </h1>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <table class="table no-border table-hover table-left">
            <tr>
                <td>
                    抢红包地址:<a href="${(redpacketUrl)!}" target="_blank">${(redpacketUrl)!}</a>
                </td>
            </tr>
        </table>

    <#-- tableBody -->
    <#assign fieldsConfig= [
    {
    "name": "序号",
    "template": r"${row_index + 1}"
    },

    {
    "name": "系统流水号",
    "template": r"${row.sysSerialNum!}"
    },

    {
    "name": "用户手机",
    "template": r"${row.mobile}"
    },

    {
    "name": "产品名称",
    "template": r"${row.productName}"
    },
    {
    "name": "状态",
    "template": r"
				<#list recordStatus?keys as key>
					<#if row.status?? && row.status?c==key>${recordStatus[key]}</#if>
				</#list>
                &nbsp;&nbsp;&nbsp;<#if row.status?? && row.status==4> <a href='#' id='${row.id}' class='btnShow' onclick='return false;'>查看原因</a></#if>
                "
    },

    {
    "name": "操作时间",
    "template": r"${(row.createTime)?datetime}"
    },

    {
    "name": "操作",
    "template": r"<#if row.status?? && row.status==4><a href='${contextPath}/manage/entRedpacket/chargeAgain.html?id=${row.id}&ruleId=${row.ruleId}'>再次充值 </a></#if>"
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
    </div>
</div>
<link rel="stylesheet" type="text/css" href="${contextPath}/manage/Js/artDialog/skins/default.css">
<script src="${contextPath}/manage/Js/artDialog/artDialog.js"></script>
<script src="${contextPath}/manage/Js/artDialog/jquery.artDialog.js"></script>
<script type="text/javascript">

    $(function () {

        $(".btnShow").click(function () {
            var dialog = art.dialog({
                lock: true,
                title: '失败原因',
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
                url: '${contextPath}/manage/entRedpacket/getRecordInfoAjax.html?id=' + $(this).attr('id'),
                success: function (data) {
                    $('#dialogContent').html(data);
                },
                cache: false
            });

        });
    });
</script>

</body>
</html>