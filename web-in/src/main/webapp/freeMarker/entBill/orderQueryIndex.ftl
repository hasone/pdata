<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-运营管理</title>
    <meta name="keywords" content="流量平台 运营管理"/>
    <meta name="description" content="流量平台 运营管理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

    <style>
        .icon-sync {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-sync.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-online {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-online.png);
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
        }

        .form label {
            width: 70px;
            text-align: right;
            margin-right: 10px;
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
        <h3>报表查询</h3>
    </div>

    <div class="tools row ">

        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">

                <div class="form-group mr-10 form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="text" class="form-control searchItem" id="entName" name="entName"
                           placeholder="" maxlength="64">
                </div>

                <div class="form-group mr-10 form-group-sm">
                    <label for="name">手机号码：</label>
                    <input type="text" class="form-control searchItem" id="mobile" name="mobile" placeholder=""
                           maxlength="11">
                </div>

                <div class="form-group mr-10 form-group-sm" id="search-time-range">
                    <label>充值时间：</label>
                    <input type="text" style="width:110px" class="form-control search-startTime searchItem" name="startTime"
                           value="${(pageResult.queryObject.queryCriterias.beginTime)!}" id="startTime" placeholder="">&nbsp;~&nbsp;
                    <input type="text" style="width:110px" class="form-control search-endTime searchItem" name="endTime"
                           id="endTime" value="${(pageResult.queryObject.queryCriterias.endTime)!}" placeholder="">
                </div>

                <div class="form-group mr-10 form-group-sm">
                    <label for="name">请求时长大于：</label>
                    <input type="text" class="form-control searchItem" id="requestTimeRange" name="requestTimeRange" placeholder=""><label>时</label>
                </div>

                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">查询</a>&nbsp;&nbsp;
                <a class="btn btn-sm btn-warning" id="boss-search-btn" href="javascript:void(0)">BOSS查询</a>
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
                <span class="message-content" id="tips">提交审批成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<!--[if lt IE 9]-->
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>

<!--[endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var buttonsFormat = function (value, column, row) {
        return ["<a href='javascript:void(0)' class='btn-icon icon-online mr-5' onclick=queryOrder('" + row.systemNum + "')>BOSS查询</a>"];
    };

    var columns = [{name: "entName", text: "企业名称", tip: true},
        {name: "type", text: "活动类型", tip: true},
        {name: "productName", text: "产品名称", tip: true},
        {name: "phone", text: "手机号码"},
        {name: "district", text: "归属地"},
        {name: "chargeTime", text: "充值时间", format: "DateTimeFormat"},
        {name: "timeRange", text: "请求时长"},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/chargeOrder/search.html?${_csrf.parameterName}=${_csrf.token}";
    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";
    require(["common", "bootstrap", "daterangepicker", "easyui", "page/list"], function () {
        initSearchDateRangePicker();
        //initTree();
        init();

        $("#subject-btn").on("click", function () {
            window.location.reload();
        });
    });

    function init() {
        $("#sure").on("click", function () {
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
        var ele = $('#search-time-range');

        var startEle = $('#startTime');
        var endEle = $('#endTime');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
            beforeShowDay: function (t) {
                var valid = t.getTime() < new Date().getTime();
                return [valid, '', ''];
            },
            customShortcuts: [
                {
                    name: '半年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 182);
                        return [start, end];
                    }
                },
                {
                    name: '一年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 360);
                        return [start, end];
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

    function queryOrder(systemNum) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "post",
            url: "${contextPath}/manage/chargeOrder/queryOrderAjax.html",
            data: {
                systemNum: systemNum
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var message = data.message;
                $("#tips").html(message);
                $("#subject-dialog").modal("show");
            },
            error: function () {
                var message = "网络出错";
                $("#tips").html(message);
                $("#subject-dialog").modal("show");
            }
        });
    }
</script>

</body>
</html>