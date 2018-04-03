<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-营销卡模板管理</title>
    <meta name="keywords" content="流量平台 营销卡模板管理"/>
    <meta name="description" content="流量平台 营销卡模板管理"/>

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

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>营销卡模板列表</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/mdrc/template/create.html" class="btn btn-sm btn-danger">
                <i class="fa fa-plus mr-5"></i>新建卡模板
            </a>
        </div>

        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm">
                <div class="form-group mr-10 form-group-sm">
                    <label for="name">模板名称：</label>
                    <input type="text" class="form-control searchItem" id="name" name="name">
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

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", "page/list"], function () {
    });

</script>
<script>
    function Delete() {
        var i = window.confirm("确定要删除吗？");
        return i;
    }
</script>

<script>

    var buttonsFormat = function (value, column, row) {
        return ["<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/mdrc/template/edit.html?id=" + row.id + "'>编辑</a>"];
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
    ;


    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    };

    var sizeFormat = function (value, column, row) {
        if (row.productSize == null) {
            return "-";
        }
        if (row.productSize < 1024) {
            return row.productSize + "KB";
        }
        if (row.productSize >= 1024 && row.productSize < 1024 * 1024) {
            return (row.productSize * 1.0 / 1024).toFixed(2) + "MB";
        }
        if (row.productSize >= 1024 * 1024) {
            return (row.productSize * 1.0 / 1024 / 1024).toFixed(2) + "GB";
        }
        return (row.productSize * 1.0 / 1024).toFixed(2) + "MB";
    };

    var columns = [{name: "name", text: "模板名称", tip: true},
        {name: "productSize", text: "流量包大小", format: sizeFormat},
        {name: "theme", text: "卡面主题", tip: true},
        <#-- {name: "resourcesCount", text: "资源个数"}, -->
        {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
        {name: "op", text: "操作", format: buttonsFormat}
    ];


    var action = "${contextPath}/manage/mdrc/template/search.html?${_csrf.parameterName}=${_csrf.token}";


</script>
</body>
</html>
