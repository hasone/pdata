<#-- 从Map对象生成queryString, 生成的queryString以“?”开始 -->
<#macro mapToQueryString mapObject>
    <#t>
    <#list mapObject?keys as key>
    ${key}=${mapObject[key]}<#if key_has_next>&</#if><#t>
    </#list>
</#macro>

<#-- 根据表格对象生成表格 -->
<#macro getTable tableObject>
    <#if tableObject??>
    <table border="1">
        <@getTableHeader tableObject.headerTemplate />
			<@getContentBody tableObject.listData tableObject.rowTemplate />
    </table>
    </#if>
</#macro>


<#-- 利用List生成表头信息 -->
<#macro getTableHeader template>
    <#if template??>
    <thead>
    <tr>
        <#assign params=(template.getParams())!>
				<#include template.getPatternPath() encoding="utf-8">
    </tr>
    </thead>
    </#if>
</#macro>

<#-- 得到表格体内容 -->
<#macro getContentBody dataList template>
    <#if template??>
        <#list dataList as rowData>
        <tr>
            <#assign params=(template.getParams())!>
				<#include template.getPatternPath() encoding="utf-8">
        </tr>
        </#list>
    </#if>
</#macro>

<#-- 列表模板, dataList为数据， fieldsConfig为配置信息 -->
<#macro getListView dataList fieldsConfig detailUrl>
<div class="table-responsive">
    <table class="table table-hover">

        <tbody>
            <#list fieldsConfig as field>
            <th>
            ${field.name}
            </th>
            </#list>


            <#list dataList as row>
            <tr>
                <#list fieldsConfig as field>
                    <td style="cursor:pointer"
                        onclick="javascript:window.location.href='${detailUrl!}?recordId=${row.id}'">
                        <@field.template?interpret />
                    </td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#macro>

