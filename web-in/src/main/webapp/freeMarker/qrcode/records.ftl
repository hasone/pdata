<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-赠送用户信息</title>
    <meta name="keywords" content="流量平台 赠送用户信息"/>
    <meta name="description" content="流量平台 赠送用户信息"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

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
        <h3>赠送用户信息</h3>
    </div>

    <div class="tools row ">
        <div class="col-sm-2">
            <a href="">
                <a class="btn btn-primary btn-sm btn-icon icon-back" onclick="history.go(-1)">返回</a>
                <!--<button class="btn btn-danger"><i class="fa fa-plus mr-5"></i>导出全部中奖记录</button>-->
            </a>
        </div>

        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <div class="form-group mr-10">
                    <label for="mobile">手机号码：</label>
                    <input type="text" class="form-control searchItem" id="mobile" name="mobile" placeholder="">
                </div>
                <div class="form-group mr-10">
                    <label for="productName">产品名称：</label>
                    <input type="text" class="form-control searchItem" id="productName" name="productName"
                           placeholder="">
                </div>
                <div class="form-group mr-10">
                    <label>运营商：</label>
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="form-control searchItem"
                               name="isp">
                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value=""><a href="#">全部</a></li>
                            <li data-value="M"><a href="#">移动</a></li>
                            <li data-value="U"><a href="#">联通</a></li>
                            <li data-value="T"><a href="#">电信</a></li>
                        </ul>
                    </div>
                </div>

                <div class="form-group mr-10">
                    <label>状态：</label>
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="form-control searchItem"
                               name="status" value="1,2,3,4">
                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value="1,2,3,4"><a href="#">全部</a></li>
                            <li data-value="1"><a href="#">待充值</a></li>
                            <li data-value="2"><a href="#">已发送充值请求</a></li>
                            <li data-value="3"><a href="#">充值成功</a></li>
                            <li data-value="4"><a href="#">充值失败</a></li>
                        </ul>
                    </div>
                </div>

                <input type="hidden" class="searchItem" name="activityId" value="${activityId!}" id="activityId">

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

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
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

    /**
     *
     */
    function showTip(ele) {
        showTipDialog($(ele).data("msg"));
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

    var ispFormat = function (value, column, row) {
        if (row.isp == "M") {
            return "移动";
        }
        if (row.isp == "A") {
            return "三网";
        }
        if (row.isp == "U") {
            return "联通";
        }
        if (row.isp == "T") {
            return "电信";
        }
        return "-";
    };

    var columns = [{name: "ownMobile", text: "手机号码", tip: true},
        {name: "isp", text: "产品运营商", format: ispFormat},
        {name: "productName", text: "产品名称", tip: true},
        {name: "status", text: "状态", format: statusFormat},
        {name: "op", text: "操作", format: buttonsFormat}];


    var action = "${contextPath}/manage/qrcode/searchRecords.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "easyui", "page/list"], function () {

    });


</script>


</body>
</html>