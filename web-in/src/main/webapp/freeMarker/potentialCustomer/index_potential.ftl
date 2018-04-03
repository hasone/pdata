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
    <title>流量平台-潜在客户管理</title>
    <meta name="keywords" content="流量平台 潜在客户管理"/>
    <meta name="description" content="流量平台 潜在客户管理"/>

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

        .table tbody > tr > td, .table tbody > tr > td *, .table thead > tr > th {

        }

        .table tbody > tr > td:last-child {
            max-width: 100%;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>潜在客户</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/potentialCustomer/addPotential.html">
                <button class="btn btn-sm btn-danger"><i class="fa fa-plus mr-5"></i>新增潜在客户</button>
            </a>
        </div>

        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <div class="form-group mr-20 form-group-sm">
                    <label>企业名称：</label>
                    <input type="hidden" data-url="/manage/potentialCustomer/query.html" class="form-control searchItem enterprise_autoComplete" id="name" name="name">
                </div>

                <div class="form-group mr-20 form-group-sm">
                    <label>状态：</label>

                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="form-control searchItem"
                               name="deleteFlag">
                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value=""><a href="#">全部</a></li>
                            <li data-value="9,10,5,4"><a href="#">未提交审批</a></li>
                            <li data-value="6"><a href="#">审批中</a></li>
                            <li data-value="8"><a href="#">已驳回</a></li>
                        </ul>
                    </div>
                    <!--  <input type="text" class="form-control searchItem" id="deleteFlag" name="deleteFlag"> -->
                </div>

                <button id="search-btn" class="btn btn-sm btn-warning">确定</button>
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
                <span class="message-content">请确认是否提交企业基本信息，一旦提交不可修改!</span>
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
                <span class="message-content">企业基本信息提交成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", "page/list"], function () {
        //renderLevelCharts();

//        initTree();
        $("#subject-btn").on("click", function () {
            window.location.reload();
        });

        init();
    });

    function init() {

        $("#sure").on("click", function () {
            var entId = $("#sure").data("entId");

            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/approval/submitBasicEnt.html",
                data: {
                    entId: entId
                },
                type: "post",
                dataType: "json"
            }).success(function (ret) {
                if (ret) {
                    if (ret.submitRes == "success") {
                        $("#subject-dialog").modal("show");
                    } else {
                        showTipDialog("提交审批失败！");
                    }
                }
            });
        });

    }

    function submitBasicEnt(id) {
        $("#submit-dialog").modal("show");
        $("#sure").data("entId", id);
    };


    var buttonsFormat = function (value, column, row) {
        /**
         *submitApprovalFlag: 提交审批权限， true: 有权限，可以提交审批
         *provinceFlag：是否是省公司标示，1：是自营平台，企业信息填写链接有“认证信息”和“合作信息”；0：是省公司，只有“认证合作信息”
         * quafilicationFlag：填写"认证信息"标示，仅用于自营，true有权限。
         * cooperationFlag：填写"合作信息"标示，仅用于自营，true有权限。
         * entInfoFlag：填写"认证合作信息"标示，仅用于省公司，true有权限。
         * */

        var submitApprovalFlag = "${submitApprovalFlag!}";
        var provinceFlag = "${flag!}";

        var quafilicationFlag = "${quafilicationFlag!}";
        var cooperationFlag = "${cooperationFlag!}";
        var entInfoFlag = "${entInfoFlag!}";

        var editFlag = "${editFlag!}";

        var links = ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/potentialCustomer/editPotential.html?enterpriseId=" + row.id + "'>编辑</a>",
            "<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/enterprise/qualification.html?entId=" + row.id + "'>认证信息</a>",
            "<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/enterprise/qualificationApproval.html?entId=" + row.id + "'>合作信息</a>",
            "<a class='btn-icon mr-5' href='${contextPath}/manage/approval/submitEntApproval.html?entId=" + row.id + "&type=0" + "'>提交审批</a>",
            "<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/enterprise/addQualificationCooperation.html?entId=" + row.id + "'>企业开户信息</a>",
            "<a class='btn-icon' href='javascript:submitBasicEnt(" + row.id + ")'>确认提交</a></span>"];

        if (submitApprovalFlag == "true") {
            if (row.deleteFlag == 4 || row.deleteFlag == 5 || row.deleteFlag == 10) {
                if (provinceFlag == 1) {
                    if (quafilicationFlag == "true" && cooperationFlag == "true") {
                        if (row.deleteFlag == 4) {
                            return [links[1]];
                        }
                        else {
                            return [links[1], links[2], links[3]];
                        }
                    }
                    else if (quafilicationFlag == "false" && cooperationFlag == "false") {
                        if (row.deleteFlag == 5 || row.deleteFlag == 10) {
                            return [links[3]];
                        }
                    }
                    else if (quafilicationFlag == "false" && cooperationFlag == "true") {
                        if (row.deleteFlag == 10) {
                            return [links[2], links[3]];
                        }
                    }
                    else {
                        if (row.deleteFlag == 4) {
                            return [links[1]];
                        }
                        else {
                            return [links[1], links[3]];
                        }
                    }

                }
                else {
                    if (entInfoFlag == "true") {
                        if (row.deleteFlag == 4) {
                            return [links[4]];
                        }
                        else if (row.deleteFlag == 5) {
                            return [links[4], links[3]];
                        }
                    }
                    else {
                        if (row.deleteFlag == 5) {
                            return [links[3]];
                        }
                    }
                }
            }
        }
        //企业在潜在客户或者驳回状态
        if (row.deleteFlag == 9 || row.deleteFlag == 8) {
            if (editFlag == "true") {
                return [links[0], links[5]];
            }
        }

        if (row.deleteFlag == 4 || row.deleteFlag == 5 || row.deleteFlag == 10) {
            if (provinceFlag == 1) {
                if (quafilicationFlag == "true" && cooperationFlag == "true") {
                    if (row.deleteFlag == 4) {
                        return [links[1]];
                    }
                    else {
                        return [links[1], links[2]];
                    }
                }

            } else {
                if (entInfoFlag == "true") {
                    return [links[4]];
                }
            }
        }

    }

    var buttonsDetailFormat = function (value, column, row) {
        return "<a href = '${contextPath}/manage/potentialCustomer/potentailDetail.html?enterpriseId=" + row.id + "'>" + row.name + "</a>";
    }

    var statusFormat = function (value, column, row) {
        if (row.deleteFlag == 9 || row.deleteFlag == 10 || row.deleteFlag == 5 || row.deleteFlag == 4) {
            return "未提交审批";
        }
        if (row.deleteFlag == 12 || row.deleteFlag == 8) {
            return "<a href = '${contextPath}/manage/enterprise/showApprovalDetail.html?entId=" + row.id + "'>已驳回 </a>";
        }
        if (row.deleteFlag == 6) {
            return "<a href = '${contextPath}/manage/enterprise/showApprovalDetail.html?entId=" + row.id + "'>审批中</a>";
        }
    };

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


    var columns = [{name: "name", text: "企业名称", tip: true, format: buttonsDetailFormat},
        {name: "code", text: "企业编码", tip: true},
        {name: "enterpriseManagerName", text: "企业管理员", tip: true},
        {name: "enterpriseManagerPhone", text: "企业管理员手机号码"},
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "status", text: "状态", format: statusFormat},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/potentialCustomer/searchPotential.html?status=1&?${_csrf.parameterName}=${_csrf.token}";

    /**
     * combotree构建
     */
    <#--function initTree() {-->
    <#--$('#districtId').combotree({-->
    <#--url: "${contextPath}/manage/enterprise/getDistrictAjax.html?districtId=${(districtId)!}",-->
    <#--onBeforeExpand: function (node) {-->
    <#--$('#districtId').combotree("tree").tree("options").url = "${contextPath}/manage/enterprise/getDistrictAjax.html?parentId=" + node.id;-->
    <#--},-->
    <#--onSelect: function (node) {-->
    <#--$("#districtIdSelect").val(node.id);-->
    <#--}-->
    <#--});-->
    <#--}-->


    //    function renderLevelCharts(echarts) {
    //        $('.level-vh').easyPieChart({
    //            lineWidth: 15,
    //            size: 130,
    //            barColor: '#FFC200',
    //            trackColor: '#ccc',
    //            scaleColor: false,
    //            onStep: function (from, to, percent) {
    //                $(this.el).find('.percent').text(Math.round(percent) + "%");
    //            }
    //        }).data('easyPieChart');
    //
    //
    //        $('.level-h').easyPieChart({
    //            lineWidth: 15,
    //            size: 130,
    //            barColor: '#FF784F',
    //            trackColor: '#ccc',
    //            scaleColor: false,
    //            onStep: function (from, to, percent) {
    //                $(this.el).find('.percent').text(Math.round(percent) + "%");
    //            }
    //        }).data('easyPieChart');
    //
    //        $('.level-jh').easyPieChart({
    //            lineWidth: 15,
    //            size: 130,
    //            barColor: '#00C794',
    //            trackColor: '#ccc',
    //            scaleColor: false,
    //            onStep: function (from, to, percent) {
    //                $(this.el).find('.percent').text(Math.round(percent) + "%");
    //            }
    //        }).data('easyPieChart');
    //
    //        $('.level-normal').easyPieChart({
    //            lineWidth: 15,
    //            size: 130,
    //            barColor: '#85C0EB',
    //            trackColor: '#ccc',
    //            scaleColor: false,
    //            onStep: function (from, to, percent) {
    //                $(this.el).find('.percent').text(Math.round(percent) + "%");
    //            }
    //        }).data('easyPieChart');
    //
    //
    //        $('.level-low').easyPieChart({
    //            lineWidth: 15,
    //            size: 130,
    //            barColor: '#CB93DD',
    //            trackColor: '#ccc',
    //            scaleColor: false,
    //            onStep: function (from, to, percent) {
    //                $(this.el).find('.percent').text(Math.round(percent) + "%");
    //            }
    //        }).data('easyPieChart');
    //    }
</script>

</body>
</html>