<#assign admin = rowData>

<td>
${admin.name}
</td>

<td>
${admin.createTime?datetime}
</td>

<td>
${admin.updateTime?datetime}
</td>

<td>
    <a href="${contextPath}/manage/role/delete.html?roleId=${admin.roleId}">${params.pageResult.pages}</a>
</td>