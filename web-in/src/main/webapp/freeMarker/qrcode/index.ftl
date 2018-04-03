<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-流量券列表</title>
    <meta name="keywords" content="流量平台流量券列表"/>
    <meta name="description" content="流量平台 流量券列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">
    
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

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

        .table tbody > tr > td:last-child, .table tbody > tr > td:last-child *, .table thead > tr > th:last-child{
            max-width: 10000px;
            overflow: visible;
        }

    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>二维码
        </h3>
    </div>

    <div class="tools row text-right">

        <div class="col-sm-2 text-left">
            <a href="${contextPath}/manage/qrcode/create.html">
                <button class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>新增二维码</button>
            </a>
        </div>

        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <input type="hidden" class="form-control searchItem" id="managerId" name="managerId"
                       value="${managerId!}">

                <div class="form-group mr-20 form-group-sm">
                    <label>活动名称：</label>
                    <input type="text" class="form-control searchItem" id="name" name="name" maxlength="255">
                </div>

                <div class="form-group mr-20 form-group-sm">
                    <label>状态：</label>
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="form-control searchItem"
                               name="status" value="">
                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value=""><a href="#">全部</a></li>
                            <li data-value="0,7"><a href="#">已保存</a></li>
                            <li data-value="5"><a href="#">审批中</a></li>
                            <li data-value="6"><a href="#">审批结束</a></li>
                            <li data-value="1,2"><a href="#">进行中</a></li>
                            <li data-value="3,4"><a href="#">已结束</a></li>
                        </ul>
                    </div>
                </div>

                <div class="form-group mr-20 form-group-sm">
                    <label>创建时间：</label>
                    <span id="search-time-range1" class="daterange-wrap">
                        <input type="text" class="form-control search-startTime searchItem" name="startTime"
                               id="startTime" placeholder="">&nbsp;~&nbsp;
                        <input type="text" class="form-control search-endTime searchItem" name="endTime"
                               id="endTime" placeholder="">
                    </span>
                </div>

                <#--<button id="search-btn" class="btn btn-sm btn-warning">确定</button>-->
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

<!-- loading -->
<div id="loadingToast" class="weui_loading_toast">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <div class="weui_loading">
            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">审批提交中</p>
    </div>
</div>
<!-- loading end -->

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var statusFormat = function (value, column, row) {
        if (row.status == 0 || row.status == 7) {
            return "已保存";
        }
        if (row.status == 1 || row.status == 2) {
            return "进行中";
        }
        if (row.status == 3 || row.status == 4) {
            return "已结束";
        }
        if (row.status == 5) {
            return "审批中";
        }
        if (row.status == 6) {
            return "审批结束";
        }

    };

    var buttons1Format = function (value, column, row) {

        var approvalFlag = "${approvalFlag!}";
        if (row.status == 0 || row.status == 7) {

            if(approvalFlag=="true"){
                <#--需要审批-->
                if(row.download == 1){
                    return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/qrcode/detail.html?activityId=" + row.activityId + "'>详情</a>",
                        '<a class="btn-icon icon-detail mr-5" href="javascript:submitActivityApproval(\'' + row.activityId + '\')">提交审批</a>'];
                }else{
                    return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/qrcode/edit.html?activityId=" + row.activityId + "'>编辑</a>",
                        "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/qrcode/detail.html?activityId=" + row.activityId + "'>详情</a>",
                        '<a class="btn-icon icon-detail mr-5" href="javascript:submitActivityApproval(\'' + row.activityId + '\')">提交审批</a>'];
                }
            }
            else{
                <#--不需要审批-->
                if(row.download == 1){
                    return ["<a href='javascript:void(0)' class='btn-icon icon-online mr-5' onclick=onShelf('" + row.activityId + "')>上架</a>",
                            "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/qrcode/detail.html?activityId=" + row.activityId + "'>详情</a>"];
                }else{
                    return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/qrcode/edit.html?activityId=" + row.activityId + "'>编辑</a>",
                        "<a href='javascript:void(0)' class='btn-icon icon-online mr-5' onclick=onShelf('" + row.activityId + "')>上架</a>",
                        "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/qrcode/detail.html?activityId=" + row.activityId + "'>详情</a>"];
                }
            }
        }
        else if (row.status == 1 || row.status == 2) {
            return ["<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/qrcode/offShelf.html?activityId=" + row.activityId + "'>下架</a>",
                    "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/qrcode/detail.html?activityId=" + row.activityId + "'>详情</a>"];
        }
        else if (row.status == 6) {
            return ["<a href='javascript:void(0)' class='btn-icon icon-online mr-5' onclick=onShelf('" + row.activityId + "')>上架</a>",
                    "<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/qrcode/detail.html?activityId=" + row.activityId + "'>详情</a>"];
        }
        else {
            return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/qrcode/detail.html?activityId=" + row.activityId + "'>详情</a>"];
        }
    }

    /**
     * 提交审批请求
     * */
    function submitActivityApproval(activityId) {
        $("#submit-dialog").modal("show");
        $("#sure").data("activityId", activityId);
    }
    ;

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
    
    var creatorNameFormat = function (value, column, row) {
        if (typeof(row.activityCreator) == "undefined") { 
            return "-";
        } else{
            return row.activityCreator.userName;
        } 
    };
    
    var creatorMobileFormat = function (value, column, row) {
        if (typeof(row.activityCreator) == "undefined") { 
            return "-";
        } else{
            return row.activityCreator.mobilePhone;
        } 
    };

    var columns = [{name: "name", text: "活动名称", tip: true},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "startTime", text: "活动开始时间", format: "DateTimeFormat"},
        {name: "endTime", text: "活动结束时间", format: "DateTimeFormat"},
        {name: "status", text: "活动状态", format: statusFormat},
        <#if isShandong?? && isShandong == "true">
        {name: "creatorName", text: "创建者",format: creatorNameFormat},
        {name: "creatorMobile", text: "创建者账号",format: creatorMobileFormat},
         </#if>
        {name: "op1", text: "审批操作", format: buttons1Format}];

    var action = "${contextPath}/manage/qrcode/search.html?${_csrf.parameterName}=${_csrf.token}";

    require(["moment", "common", "bootstrap", "daterangepicker", "easyui", "page/list"], function (mm) {
        window.moment = mm;
        initSearchDateRangePicker();

        init();

        $("#subject-btn").on("click", function () {
            window.location.reload();
        });
    });

    function init() {
        $("#sure").on("click", function () {

            var activityId = $("#sure").data("activityId");

            var currentId = "${currUserId}";
            var roleId = "${roleId}";
            showToast();
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/qrcode/submitActivityApprovalAjax.html",
                data: {
                    activityId: activityId,
                    currentId: currentId,
                    roleId: roleId
                },
                type: "post",
                dataType: "json"
            }).success(function (ret) {
                if (ret) {
                    hideToast();
                    if (ret.submitRes == "success") {
                        $("#subject-dialog").modal("show");
                    }
                    else {
                        if(ret.errorMsg){
                            showSubjectDialog(ret.errorMsg);
                        }
                        else{
                            showSubjectDialog("提交审批失败!");
                        }
                    }
                }
            });
        });
    }

    /**
     * 显示提示信息
     */
    function showSubjectDialog(msg) {
        $("#subject-dialog .message-content").html(msg);
        $("#subject-dialog").modal("show");
    }

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range1');

        var startEle = $('#startTime');
        var endEle = $('#endTime');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            endDate: new Date(),
            customShortcuts: [
                {
                    name: '半年内',
                    dates: function () {
                        var end = moment();
                        var start = moment().add(-6, "month");
                        return [start.toDate(), end.toDate()];
                    }
                },
                {
                    name: '一年内',
                    dates: function () {
                        var end = moment();
                        var start = moment().add(-1, "year");
                        return [start.toDate(), end.toDate()];
                    }
                }
            ],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

    function onShelf(activityId) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "post",
            url: "${contextPath}/manage/qrcode/onShelf.html",
            data: {
                activityId: activityId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var message = data.message;
                var result = data.result;
                if (result != 'true') {
                    alert(message);
                }
                else {
                }
                location.reload();
            },
            error: function () {
                var message = "网络出错，请重新尝试";
                alert(message);
            }
        });
    }

</script>

</body>
</html>