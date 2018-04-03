<form class="form-inline definewidth m20" id="table_validate"
      action="${formModel.action}"
      method="${formModel.method}">

<#list formModel.queryModels as qm>
    <#if qm.type = "text">
    ${qm.title}:<input type="text" name="${qm.name}" class="abc input-default"
                       placeholder="" value="${qm.value}" maxlength="255">
    </#if>

    <#if qm.type = "select">
    ${qm.title}:<select name="${qm.name}" style="width: 70px">
        <#list qm.option?keys as itemKey>
            <option value="${qm.option[itemKey]}" <#if qm.option[itemKey] = qm.value>
                    selected </#if>>${itemKey}</option>
        </#list>
    </select>
    </#if>

    <#if qm.type = "submit">
        <button type="submit" class="btn btn-primary" name="${qm.name}">${qm.title}</button>
    </#if>

    <#if qm.type = "button">
        <button type="button" class="btn btn-success" name="${qm.name}">${qm.title}</button>
    </#if>
</#list>

    <span class='empty hide'>查询字符串长度不能超过64</span>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
