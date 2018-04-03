<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />


<html>
<head>
    <meta charset="utf-8"/>
    <title>
        企业报表
    </title>
</head>

<body>
<div class="page-header">
    <h1>企业报表</h1>
</div><!-- /.page-header -->
<div class="row">
    <div class="col-xs-12">
        <div class="table-search clearfix">
            <form class="form-inline definewidth m20" id="table_validate"
                  action="${contextPath}/manage/entReport/index.html"
                  method="POST">
                <div class="col-sm-2">

                </div>
                <div class="col-sm-10 dataTables_filter">


                    <label>地区：</label>
                    <input type="text" name="entBossID" class="abc input-default"
                           placeholder="" autocomplete="off"
                           value="${(pageResult.queryObject.queryCriterias.city)!}"
                           maxlength="64"> &nbsp;


                    <label>企业名称：</label>
                    <input type="text" name="entName" class="abc input-default"
                           placeholder="" autocomplete="off"
                           value="${(pageResult.queryObject.queryCriterias.entName)!}"
                           maxlength="64"> &nbsp;

                    <label>企业BOSSID：</label>
                    <input type="text" name="entBossID" class="abc input-default"
                           placeholder="" autocomplete="off"
                           value="${(pageResult.queryObject.queryCriterias.entBossID)!}"
                           maxlength="64"> &nbsp;

                    <label>状态：</label>
                    <select name="status">
                        <option value='0'>未删除</option>
                        <option value='1'>已删除</option>
                        <option value=''>全部</option>
                    </select>
                    <button type="submit" class="btn btn-bg-white">查询</button>
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
    "fullName": r"${(row.entName)!}"
    },
    {
    "name": "企业端企业编码",
    "template": r"${(row.entBOSSID)!}"
    },
    {
    "name": "地市",
    "template": r"${(row.city)!}"
    },
    {
    "name": "全省产品数量",
    "template":  r"${(row.provinceTotalRecord)!}"
    },
    {
    "name": "全省产品折扣前价值",
    "template":  r"${(row.provinceTotalValueBeforeDiscount)!}"
    },
    {
    "name": "全省产品折扣后价值",
    "template":  r"${(row.provinceTotalValueAfterDiscount)!}"
    },
    {
    "name": "本地产品数量",
    "template":  r"${(row.cityTotalRecord)!}"
    },
    {
    "name": "本地产品折扣前价值",
    "template":  r"${(row.cityTotalValueBeforeDiscount)!}"
    },
    {
    "name": "本地产品折扣后价值",
    "template":  r"${(row.cityTotalValueAfterDiscount)!}"
    },
    {
    "name": "合计数量",
    "template":  r"${(row.provinceTotalRecord)! + (row.cityTotalRecord)! }"
    },

    {
    "name": "合计折前价值",
    "template":  r"${(row.cityTotalValueBeforeDiscount)! + (row.provinceTotalValueBeforeDiscount)! }"
    },
    {
    "name": "合计折后价值",
    "template":  r"${(row.cityTotalValueAfterDiscount)! + (row.provinceTotalValueAfterDiscount)! }"
    }

    </body>
</html>
