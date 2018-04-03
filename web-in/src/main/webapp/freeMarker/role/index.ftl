<!DOCTYPE html>
<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-角色管理</title>
    <meta name="keywords" content="流量平台 企业列表"/>
    <meta name="description" content="流量平台 企业列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>角色管理</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/role/addOrEdit.html" class="btn btn-sm btn-danger">
                <i class="fa fa-plus mr-5"></i>新建角色
            </a>
        </div>
        
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">
                <div class="form-group mr-10 form-group-sm">
                    <label for="name">角色名称：</label>
                    <input type="text" class="form-control searchItem" id="name" name="name" value="" placeholder="">
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
            </div>
        </div>
    </div>


    <div class="tile mt-30">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <div role="table"></div>
                </div>
            </div>
        </div>
    </div>

    <div role="pagination"></div>
    
</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", "page/list"], function () {
    });

</script>

<script>
    var buttonsFormat = function (value, column, row) {        				
        if(row.canBeDeleted !=0){
	        return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/role/addOrEdit.html?roleId=" + row.roleId + "'>编辑</a>",
	        	"<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/role/editRoleCreate.html?roleId=" + row.roleId + "'>编辑角色创建权限</a>",
	        	"<a class='btn-icon icon-del mr-5' href='${contextPath}/manage/role/delete.html?roleId=" + row.roleId + "' onclick='return Delete();'>删除</a>"];
    	}else{
    		return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/role/addOrEdit.html?roleId=" + row.roleId + "'>编辑</a>",
        	"<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/role/editRoleCreate.html?roleId=" + row.roleId + "'>编辑角色创建权限</a>"];				
    	}    
    }
    var columns = [{name: "name", text: "角色名称", tip: true},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "updateTime", text: "修改时间", format: "DateTimeFormat"},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/role/search.html?${_csrf.parameterName}=${_csrf.token}&back=${(back)!0}";
    
    function Delete() {
        var i = window.confirm("确定要删除吗？");
        return i;
    }
    
    function searchCallback(data){
    	$("#name").val(data.queryObject.name);
    	$("#pageSize").val(data.queryObject.pageSize);
    	$("#pageNum").val(data.queryObject.pageNum);
    	action = "${contextPath}/manage/role/search.html?${_csrf.parameterName}=${_csrf.token}";
    }

</script>


</body>
</html>