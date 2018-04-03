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
    <title>流量平台-用户列表</title>
    <meta name="keywords" content="流量平台 用户列表"/>
    <meta name="description" content="流量平台 用户列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>
    <style>
        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-sync {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-sync.png);
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
        }

        .icon-online {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-online.png);
        }

        .icon-search {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-search.png);
        }

        .form {
            width: 350px;
            text-align: left;
        }

        .form label {
            width: 70px;
            text-align: right;
            margin-right: 10px;
        }

        .form .form-control {
            width: 230px;
        }

        .lg-btn-input {
            width: 205px;
            text-align: left;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        .form .form-group .prompt {
            padding-left: 86px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .search-separator {
            display: none;
        }

        @media (max-width: 1300px) {
            .search-separator {
                display: block;
            }
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
        <h3>用户管理</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <#if isCustomManager?? && isCustomManager=="false">
                <#if tag??&&tag = "true">
                    <a href="${contextPath}/manage/user/setManager.html" class="btn btn-sm btn-danger">
                        <i class="fa fa-plus mr-5"></i>
                        <#if sc??&&sc=="true">
                            角色分配
                        <#else>
                            分配职位
                        </#if>
                    </a>
                </#if>
            </#if>
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <form class="form-inline" action="${contextPath}/manage/user/index.html" method="GET">
                <div class="form-group form-group-sm">
                    <label>地区：</label>
                    <div class="btn-group btn-group-sm" id="tree" style="text-align: left;"></div>
                    <span id="area_error" style='color:red'></span>
                    <input id="managerId" name="managerId" class="searchItem" value="${managerId!}"
                           style="width: 0; height: 0; opacity: 0">
                </div>
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">姓名：</label>
                    <input type="text" class="form-control searchItem" id="userName" name="userName" value=""
                           placeholder="">
                </div>
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">手机号码：</label>
                    <input type="text" class="form-control searchItem" id="mobilePhone" name="mobilePhone" value=""
                           placeholder="">
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
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


    <div class="modal fade dialog-sm" id="remove-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">删除成功</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var comboTree;
    require(["common", "bootstrap", "daterangepicker", "page/list", "ComboTree", "react",
             "react-dom"],
                function (common, bootstrap, daterangepicker, list, ComboTree, React, ReactDOM) {
                     //筛选树形接口
                    comboTree = ReactDOM.render(React.createElement(ComboTree, {
                        name: "managerId",
                        url: "${contextPath}/manage/tree/getRoot.html"
                    }), $("#tree")[0]);
                    var tree = comboTree.getReference();
                    tree.on("open", function (item) {
                        if (item.open) {
                            tree.deleteChildItems(item);
                            ajaxData("${contextPath}/manage/tree/getChildNode.html",
                                     {"parentId": item.id}, function (ret) {
                                        if (ret) {
                                            tree.loadDynamicJSON(item, ret);
                                        }
                                    });
    
                        }
                    });
                    //选中区域
                    tree.on("select", function (item) {
                        $("#managerId").val(item.id);
                });
            });

</script>
<script>
    function Delete() {
        var i = window.confirm("确定要删除吗？");
        return i;
    }
</script>

<script>
    var currentManagerId = "${currentManagerId!}";
    var sc = "${sc!}"

    var buttonsFormat = function (value, column, row) {
        var isCustomManager = "${isCustomManager!}";

        if (row.parentManagerId == currentManagerId && row.parentManagerId != row.id && sc != "true") {
             if(isCustomManager=="false"){
                 return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/user/showDetails.html?administerId=" + row.id + "'>详情</a>",
                     "<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/user/editManager.html?administerId=" + row.id + "'>修改职位</a>"];
             }
             if(isCustomManager=="true"){
                 return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/user/showDetails.html?administerId=" + row.id + "'>详情</a>"];
             }
        }
        else {
            return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/user/showDetails.html?administerId=" + row.id + "'>详情</a>"];
        }
    }

    function dateFormat(date, fmt) {
        var o = {
            "M+": date.getMonth() + 1,                 //月份
            "d+": date.getDate(),                    //日
            "h+": date.getHours(),                   //小时
            "m+": date.getMinutes(),                 //分
            "s+": date.getSeconds(),                 //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }

    var nameFormat = function (value, column, row) {
        return row.managerName + row.roleName;
    }

    if (sc == "true") {
        var columns = [{name: "userName", text: "姓名", tip: true},
            {name: "mobilePhone", text: "手机", tip: true},
            {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
            {name: "updateTime", text: "修改时间", format: "DateTimeFormat"},
            {name: "managerName", text: "用户", format: nameFormat},
            {name: "roleName", text: "角色身份"},
            {name: "op", text: "操作", format: buttonsFormat}];
    }
    else {
        var columns = [{name: "userName", text: "姓名", tip: true},
            {name: "mobilePhone", text: "手机", tip: true},
            {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
            {name: "updateTime", text: "修改时间", format: "DateTimeFormat"},
            //{name: "managerName", text: "职位名称", format: nameFormat},
            {name: "districtName", text: "所属地区", tip: true},
            {name: "roleName", text: "角色身份"},
            {name: "op", text: "操作", format: buttonsFormat}];
    }


    var action = "${contextPath}/manage/user/search.html?${_csrf.parameterName}=${_csrf.token}&back=${(back)!0}";
    
    function searchCallback(data){
    	$("#userName").val(data.queryObject.userName);
    	$("#mobilePhone").val(data.queryObject.mobilePhone);
    	$("#pageSize").val(data.queryObject.pageSize);
    	$("#pageNum").val(data.queryObject.pageNum);
    	action = "${contextPath}/manage/user/search.html?${_csrf.parameterName}=${_csrf.token}";
    }

</script>
</body>
</html>