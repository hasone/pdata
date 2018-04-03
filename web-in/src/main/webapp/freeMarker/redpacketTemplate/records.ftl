<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-营销管理</title>
    <meta name="keywords" content="流量平台 营销管理"/>
    <meta name="description" content="流量平台 营销管理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">

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

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>用户中奖详情</h3>
    </div>

    <div class="tools row ">
        <div class="col-sm-2">
            <a href="">
                <a class="btn btn-primary btn-sm btn-icon icon-back" onclick="history.go(-1)">返回</a>
            </a>
        </div>

        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">

                <div class="form-group mr-10">
                    <label for="mobile">手机号码：</label>
                    <input type="text" class="form-control searchItem" id="mobile" name="mobile" placeholder="">
                </div>

                <div class="form-group mr-10">
                    <label>状态：</label>
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="form-control searchItem"
                               name="status" value="0,1,2,3,4">
                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value=""><a href="#">全部</a></li>
                            <li data-value="1"><a href="#">待充值</a></li>
                            <li data-value="2"><a href="#">已发送充值请求</a></li>
                            <li data-value="3"><a href="#">充值成功</a></li>
                            <li data-value="4"><a href="#">充值失败</a></li>
                        </ul>
                    </div>
                </div>

                <input type="hidden" class="searchItem" name="activityId" value="${activityId}" id="activityId">

                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>


                <!--<a onclick="createFile()" class="btn btn-sm btn-warning">报表导出</a>-->
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

    <!--错误提示弹窗-->
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
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
</div>

<!--[if lt IE 9]-->
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<!--[endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var statusFormat = function (value, column, row) {
        if (row.status == 1) {
            return "待充值";
        }
        if (row.status == 2) {
            return "已发送充值请求";
        }
        if (row.status == 3) {
            return "充值成功";
        }
        if (row.status == 4) {
            return "充值失败";
        }
    };

    var buttonsFormat = function (value, column, row) {
        var msg = row.reason;
        if (msg != null) {
            msg = msg.replace("\"", "&quot;");
        }
        if (row.status == 4) {
            return '<a class="btn-icon icon-detail mr-5" onclick="showTip(this)" data-msg="' + msg + '">查看失败原因</a>';
        }
    }

    function showTip(ele) {
        showTipDialog($(ele).data("msg"));
    }

    var columns = [{name: "ownMobile", text: "手机号码"},
        {name: "productName", text: "中奖产品", tip: true},
        {name: "winTime", text: "中奖时间", format: "DateTimeFormat"},
        {name: "status", text: "充值状态", format: statusFormat},
        {name: "op", text: "操作", format: buttonsFormat}];


    var action = "${contextPath}/manage/redpacket/searchRecord.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "daterangepicker", "easyui", "page/list"], function () {
        //initSearchDateRangePicker();
    });

    /*function initSearchDateRangePicker(){
        var ele = $('#search-time-range');

        var startEle = $('#startTime');
        var endEle = $('#endTime');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
            beforeShowDay: function(t) {
                var valid = t.getTime() < new Date().getTime();
                return [valid,'',''];
            },
            customShortcuts: [
                {
                    name: '半年内',
                    dates : function()
                    {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate()-182);
                        return [start,end];
                    }
                },
                {
                    name: '一年内',
                    dates : function()
                    {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate()-360);
                        return [start,end];
                    }
                }
            ],
            getValue: function () {
                if (startEle.val() && endEle.val() )
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }*/


</script>

</body>
</html>