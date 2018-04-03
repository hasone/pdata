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
    <title>流量平台-审批流程管理</title>
    <meta name="keywords" content="流量平台 审批流程管理"/>
    <meta name="description" content="流量平台 审批流程管理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        .icon-new-register {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-new-register.png);
        }

        .levels {
            overflow: hidden;
        }

        .level {
            width: 20%;
            float: left;
            text-align: center;
        }

        .levelChart {
            height: 130px;
            width: 130px;
            margin: 0 auto;
            position: relative;
            text-align: center;
        }

        .levelChart canvas {
            position: absolute;
            top: 0;
            left: 0;
        }

        .percent {
            display: inline-block;
            line-height: 130px;
            z-index: 2;
            color: black;
            font-size: 22px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>审批流程</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/approvalDefinition/create.html">
                <button class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>新增审批流程</button>
            </a>
        </div>

        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <div class="form-group mr-20 form-group-sm">
                    <!--
                                        <label>企业名称：</label>
                                        <input type="text" class="form-control searchItem" id="name" name="name">
                    -->
                </div>

                <div class="form-group mr-20 form-group-sm">
                    <!--
                                        <label>状态：</label>
                                        <div class="btn-group btn-group-sm">
                                            <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="form-control searchItem"
                                                   name="deleteFlag">
                                            <button type="button" class="btn btn-default" style="width: 110px">&nbsp;</button>
                                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                                    aria-haspopup="true" aria-expanded="false">
                                                <span class="caret"></span>
                                                <span class="sr-only">Toggle Dropdown</span>
                                            </button>
                                            <ul class="dropdown-menu">
                                                <li data-value=""><a href="#">全部</a></li>
                                                <li data-value="9"><a href="#">未认证</a></li>
                                                <li data-value="10"><a href="#">待客户经理审批</a></li>
                                                <li data-value="4"><a href="#">待市级管理员审批</a></li>
                                                <li data-value="5,6"><a href="#">待省级管理员审批</a></li>
                                                <li data-value="8,12"><a href="#">已驳回</a></li>
                                            </ul>
                                        </div>
                    -->
                    <!--  <input type="text" class="form-control searchItem" id="deleteFlag" name="deleteFlag"> -->
                </div>

                <!--   <button id="search-btn" class="btn btn-sm btn-warning">确定</button>-->
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

    <div class="modal fade dialog-sm" id="tip-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">请确认是否删除该条审批记录</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", "easypiechart", "easyui", "page/list"], function () {
        renderLevelCharts();


    });


    var buttonsFormat = function (value, column, row) {
    <#--   return ["<a class='btn-icon icon-search' href='${contextPath}/manage/approvalDefinition/detail.html?id=" + row.id + "'>详情</a>"];-->
<#--<a class='btn-icon icon-edit' href='${contextPath}/manage/approvalDefinition/edit.html?id=" + row.id + "'>编辑</a> -->
        return ["<a class='btn-icon icon-detail' href='${contextPath}/manage/approvalDefinition/detail.html?id=" + row.id + "'>详情</a>",
            "<a class='btn-icon icon-del' href='javascript:deleteApprovalProcess(" + row.id + ")'>删除</a>"];
    }

    var stageFormat = function (value, column, row) {
        if (row.stage == 0) {
            return "无审批流程";
        }
        if (row.stage == 1) {
            return "一级审批流程";
        }
        if (row.stage == 2) {
            return "两级审批流程";
        }
        if (row.stage == 3) {
            return "三级审批流程";
        }
        if (row.stage == 4) {
            return "四级审批流程";
        }
        if (row.stage == 5) {
            return "五级审批流程";
        }
    }

    var typeFormat = function (value, column, row) {
        if (row.type == 0) {
            return "企业开户审批流程";
        }
        if (row.type == 1) {
            return "产品变更审批流程";
        }
        if (row.type == 2) {
            return "账户变更审批流程";
        }
        if (row.type == 3) {
            return "营销活动审批流程";
        }
        if (row.type == 4) {
            return "信息变更审批流程";
        }
        if (row.type == 5) {
            return "企业EC审批流程";
        }
        if (row.type == 6) {
            return "营销卡激活审批流程";
        }
        if (row.type == 7) {
            return "营销卡制卡审批流程";
        }
        if (row.type == 8) {
            return "企业管理员信息审批流程";
        }
        if (row.type == 9) {
            return "企业最小额度变更审批";
        }
        if (row.type == 10) {
            return "企业预警值变更审批";
        }
        if (row.type == 11) {
            return "企业暂停值变更审批";
        }
    }

    var buttonsDetailFormat = function (value, column, row) {
        return "<a href = '${contextPath}/manage/potentialCustomer/potentailDetail.html?enterpriseId=" + row.id + "'>" + row.name + "</a>";
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


    var columns = [{name: "roleName", text: "流程发起角色", tip: true},
        {name: "stage", text: "审批级数", tip: true, format: stageFormat},
        {name: "type", text: "审批流程类型", tip: true, format: typeFormat},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/approvalDefinition/search.html?${_csrf.parameterName}=${_csrf.token}";


    function renderLevelCharts(echarts) {
        $('.level-vh').easyPieChart({
            lineWidth: 15,
            size: 130,
            barColor: '#FFC200',
            trackColor: '#ccc',
            scaleColor: false,
            onStep: function (from, to, percent) {
                $(this.el).find('.percent').text(Math.round(percent) + "%");
            }
        }).data('easyPieChart');


        $('.level-h').easyPieChart({
            lineWidth: 15,
            size: 130,
            barColor: '#FF784F',
            trackColor: '#ccc',
            scaleColor: false,
            onStep: function (from, to, percent) {
                $(this.el).find('.percent').text(Math.round(percent) + "%");
            }
        }).data('easyPieChart');

        $('.level-jh').easyPieChart({
            lineWidth: 15,
            size: 130,
            barColor: '#00C794',
            trackColor: '#ccc',
            scaleColor: false,
            onStep: function (from, to, percent) {
                $(this.el).find('.percent').text(Math.round(percent) + "%");
            }
        }).data('easyPieChart');

        $('.level-normal').easyPieChart({
            lineWidth: 15,
            size: 130,
            barColor: '#85C0EB',
            trackColor: '#ccc',
            scaleColor: false,
            onStep: function (from, to, percent) {
                $(this.el).find('.percent').text(Math.round(percent) + "%");
            }
        }).data('easyPieChart');


        $('.level-low').easyPieChart({
            lineWidth: 15,
            size: 130,
            barColor: '#CB93DD',
            trackColor: '#ccc',
            scaleColor: false,
            onStep: function (from, to, percent) {
                $(this.el).find('.percent').text(Math.round(percent) + "%");
            }
        }).data('easyPieChart');
    }


    function deleteApprovalProcess(id) {
        $("#tip-dialog").modal("show");
        init(id);
    }
    ;
    function init(id) {
        $("#ok").on("click", function () {
            window.location.href = "${contextPath}/manage/approvalDefinition/delete.html?id=" + id;
        });
    }

</script>

</body>
</html>