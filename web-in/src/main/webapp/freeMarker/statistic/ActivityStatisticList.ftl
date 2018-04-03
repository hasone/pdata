<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-活动记录详情</title>
    <meta name="keywords" content="流量平台 活动记录详情"/>
    <meta name="description" content="流量平台 活动记录详情"/>

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
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>活动记录详情</h3>
    </div>

    <div class="tools row text-right">

        <div class="col-lg-12 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <!--企业管理员要带上adminId进行活动记录的搜索-->
                <input class="searchItem" id="adminId" name="adminId" type="hidden" value="${adminId!}">

                <div class="form-group form-group-sm">
                    <label for="name">地区：</label>
                    <!--combotree-->
                    <select id="districtId" name="" style="width:150px" class="btn btn-default btn-sm searchItem">
                    </select>
                    <input class="searchItem" id="districtIdSelect" name="districtId" type="hidden" value="">
                </div>
                <div class="form-group form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="hidden" name="eName" id="eName" class="form-control searchItem enterprise_autoComplete" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>
                <div class="form-group form-group-sm">
                    <label for="name">手机号码：</label>
                    <input type="text" name="mobile" id="mobile" class="form-control searchItem" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                </div>

                <div class="form-group form-group-sm" id="search-time-range">
                    <label>活动时间：</label>&nbsp
                    <input type="text" class="form-control search-startTime searchItem" name="startTime" value=""
                           id="startTime" placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                           value="" placeholder="">
                </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
                <a onclick="createFile()" class="btn btn-sm btn-warning">报表导出</a>
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
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

</div>

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var buttonsFormat = function (value, column, row) {
        var msg = row.chargeMsg;
        if (row.chargeStatus == 4) {
            return "<a class='btn-icon icon-detail mr-5' onclick= showTipDialog('" + msg + "');>查看失败原因</a>";
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
    var statusFormat = function (value, column, row) {
        if (row.chargeStatus == 1) {
            return "待活动";
        }
        if (row.chargeStatus == 2) {
            return "已发送活动请求";
        }
        if (row.chargeStatus == 3) {
            return "活动成功";
        }
        if (row.chargeStatus == 4) {
            return "活动失败";
        }
        return "";
    }

    var columns = [{name: "eName", text: "企业名称", tip: true},
        {name: "eCode", text: "企业编码", tip: true},
        {name: "fullDistrictName", text: "地区", tip: true},
        {name: "aName", text: "活动名称", tip: true},
        {name: "chargeType", text: "活动类型"},
        {name: "chargeStatus", text: "活动状态", format: statusFormat},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/statisticCharge/listSearch.html?${_csrf.parameterName}=${_csrf.token}";


    require(["common", "bootstrap", "daterangepicker", "easyui", "page/list"], function () {
        initSearchDateRangePicker();
        initTree();
    });

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

    /**
     * combotree构建
     */
    function initTree() {
        $('#districtId').combotree({
            url: "${contextPath}/manage/enterprise/getDistrictAjax.html?${_csrf.parameterName}=${_csrf.token}&districtId=${(districtId)!}",
            onBeforeExpand: function (node) {
                $('#districtId').combotree("tree").tree("options").url = "${contextPath}/manage/enterprise/getDistrictAjax.html?${_csrf.parameterName}=${_csrf.token}&parentId=" + node.id;
            },
            onSelect: function (node) {
                $("#districtIdSelect").val(node.id);
            }
        });
    }
</script>

<script>
    function createFile() {
        var eName = document.getElementById('eName').value;
        eName = eName.replace("%", "%25");
        var mobile = document.getElementById('mobile').value;
        mobile = mobile.replace("%", "%25");
        var districtId = document.getElementById('districtIdSelect').value;
        var startTime = document.getElementById('startTime').value;
        var endTime = document.getElementById('endTime').value;
        //检查导出的时间是否超过六个月，超过六个月的给出提醒
        if (startTime != null && endTime != null && startTime != "" && endTime != "") {
            var start = new Date(startTime.replace(/-/g, "/"));
            var end = new Date(endTime.replace(/-/g, "/"));
            var s = (end.getYear() - start.getYear()) * 12 + end.getMonth() - start.getMonth();
            if (s > 6) {
                showTipDialog("无法导出时间超过6个月的数据！");
            }
            else {
                window.open("${contextPath}/manage/statisticCharge/creatCSVfile.html?mobile=" + mobile + "&&eName=" + eName + "&&districtId=" + districtId + "&&startTime=" + startTime + "&&endTime=" + endTime);
            }
        }
        else {
            showTipDialog("导出最近6个月的数据！");
            window.open("${contextPath}/manage/statisticCharge/creatCSVfile.html?mobile=" + mobile + "&&eName=" + eName + "&&districtId=" + districtId + "&&startTime=" + startTime + "&&endTime=" + endTime);
        }

    }
</script>
</body>
</html>