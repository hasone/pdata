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


    <div class="modal fade dialog-sm" id="submit-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">请确认是否提交审批，一旦提交不可修改！</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

    <div class="modal fade dialog-sm" id="subject-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">提交审批成功</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", , "page/list"], function () {
        init();
        $("#subject-btn").on("click", function () {
            window.location.reload();
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
    
    /**
     * 提交审批请求
     * */
    function submitProductChange(id) {
        $("#submit-dialog").modal("show");
        $("#sure").data("adminId", id);
    };
    
    /**
     * 显示提示信息
     */
    function showSubjectDialog(msg) {
        $("#subject-dialog .message-content").html(msg);
        $("#subject-dialog").modal("show");
    }

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
    
     var buttonsFormat1 = function (value, column, row) {
        var authFlag = "${authFlag}";
        var isUseEnterList = "${isUseEnterList}";

        var links = ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/adminChange/updateAdmin.html?adminId=" + row.id + "'>修改</a>",
            "<a class='btn-icon' href='javascript:submitProductChange(" + row.id + ")'>提交审批</a></span>",
            "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/adminChange/detail.html?adminId=" + row.id + "'>详情</a>"];

        if(row.roleId != 3){
            return [links[2]];
        }

        if (row.adminChangeStatus == 0 || row.adminChangeStatus == 4) {
            if(isUseEnterList == "false"){
                return [links[2],links[0]];
            }
            else{
                return [links[2]];
            }
        }

        if (row.adminChangeStatus == 1) {
            if (authFlag == "true") {
                if(isUseEnterList == "false"){
                    return [links[2],links[0],links[1]];
                }
                else{
                    return [links[2]];
                }
            }
            else {
                return [links[2]];
            }
        }
        
        if (row.adminChangeStatus == 2) {
            return [links[2]];
        }
        
        if (row.adminChangeStatus == 5) {
            return [links[2]];
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
            //{name: "description", text: "状态"},
            {name: "op", text: "操作", format: buttonsFormat1}];
    }
    else {
        var columns = [{name: "userName", text: "姓名", tip: true},
            {name: "mobilePhone", text: "手机", tip: true},
            {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
            {name: "updateTime", text: "修改时间", format: "DateTimeFormat"},
            {name: "roleName", text: "角色身份"},
            //{name: "description", text: "状态"},
            {name: "op", text: "操作", format: buttonsFormat1}];
    }


    var action = "${contextPath}/manage/adminChange/search.html?${_csrf.parameterName}=${_csrf.token}";
    
    function init() {
        $("#sure").on("click", function () {
            var adminId = $("#sure").data("adminId");
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/approval/submitAdminChangeAjax.html",
                data: {
                    adminId: adminId
                },
                type: "post",
                dataType: "json"
            }).success(function (ret) {
                if (ret) {
                    if (ret.submitRes == "success") {
                        $("#subject-dialog").modal("show");
                    }
                    else if (ret.submitRes == "mobileConflict") {
                        showSubjectDialog("您修改的手机号系统已存在，请重新修改！");
                    }
                    else {
                        showSubjectDialog("已存在提交的审批记录，请勿重复提交。");
                    }
                }
            });
        });
    }


</script>
</body>
</html>