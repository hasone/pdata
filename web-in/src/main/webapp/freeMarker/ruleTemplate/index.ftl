<#import "../Util.ftl" as Util>
<#global contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>
    <title>活动模板列表</title>
    <meta charset="UTF-8">
</head>
<body>

<#-- queryBar -->
<#assign name = (pageResult.queryObject.queryCriterias.name)! >

<div class="page-header">
    <h1>活动模板列表</h1>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-search clearfix">
            <form class="form-inline definewidth m20" id="table_validate"
                  action="${contextPath}/manage/rule_template/index.html" method="POST">
                <div class="col-sm-2">
                    <div class="form-group">
                        <a href="${contextPath}/manage/rule_template/create.html" class="btn btn-info">新建活动模板</a>
                    </div>
                </div>
                <div class="col-sm-10 form-inline dataTables_filter">
                    <div class="form-group">
                        <label for="name">模板名称：</label>
                        <input type="text" name="name" id="name" autocomplete="off" placeholder=""
                               value="${taskTemplateName!}" maxlength="255"/>
                    </div>
                    &nbsp;&nbsp;

                    <div class="form-group">
                        <label>模板状态：</label>
                        <select name="status" id="status">
                            <option value="">全部</option>
                            <option value="1" <#if queryStatus??&&queryStatus=='1'>selected</#if>>上架</option>
                            <option value="0" <#if queryStatus??&&queryStatus=='0'>selected</#if>>下架</option>
                        </select>
                    </div>
                    &nbsp;&nbsp;

                    <div class="form-group">
                        <button class="btn btn-bg-white" type="submit">查询</button>
                    </div>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>
    <#-- tableBody -->
    <#assign fieldsConfig= [
    {
    "name": "模板名称",
    "template": r"<#if row.name?length lte 10>
							${row.name}
						  <#else>
						    ${(row.name?substring(0,10))!}...
						  </#if>",
    "fullName": r"${row.name!}"
    },
    {
    "name": "创建时间",
    "template": r"<#if row.createTime??>
                              ${row.createTime?string('yyyy-MM-dd HH:mm:ss')}
                          </#if>"
    },
    {
    "name": "红包主题",
    "template": r"${row.title!}"
    },
    {
    "name": "资源个数",
    "template": r"${row.imageCnt!}"
    },
    {
    "name": "操作",
    "template": r"<#if row.deleteFlag != 1>
								<#if row.status ==  0>
									<a href='${contextPath}/manage/rule_template/changeStatus.html?id=${row.id}&pageNum=${pageResult.queryObject.pageNum}&
							    		<@mapToQueryString pageResult.queryObject.queryCriterias />'>上架</a>&nbsp;&nbsp;
			
							    	<a href='${contextPath}/manage/rule_template/uploadImage.html?id=${row.id}&pageNum=${pageResult.queryObject.pageNum}&
										<@mapToQueryString pageResult.queryObject.queryCriterias />'>上传图片</a>&nbsp;&nbsp;

								<#else>
									<a href='${contextPath}/manage/rule_template/changeStatus.html?id=${row.id}&pageNum=${pageResult.queryObject.pageNum}&
										<@mapToQueryString pageResult.queryObject.queryCriterias />'>下架</a>&nbsp;&nbsp;
									
									<a href='${contextPath}/manage/rule_template/detail.html?id=${row.id}&pageNum=${pageResult.queryObject.pageNum}&
										<@mapToQueryString pageResult.queryObject.queryCriterias />'>详情</a>
								</#if>
						 </#if>&nbsp"
    }
    ] />

    <@Util.getListView pageResult.list fieldsConfig/>

    <#include "../pageInfo.ftl" encoding="utf-8" >

        <script>

            function Delete() {
                var i = window.confirm("确定要删除吗？");
                return i;
            }

        </script>


    </div>
</div>
</body>
</html>