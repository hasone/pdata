<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-</title>
    <meta name="keywords" content="流量平台 信息修改记录"/>
    <meta name="description" content="流量平台 信息修改记录"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <style>
        .table > thead > tr > th {
            line-height: initial;
        }

        .table > thead > tr > th, .table > tbody > tr > td {
            vertical-align: middle;
        }

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

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
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
        <h3>信息修改记录
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">
                <input name="currUserId" id="currUserId" class="searchItem" type="hidden" value="${currUserId!}">
                <input name="roleId" id="roleId" class="searchItem" type="hidden" value="">
                <input name="managerId" id="managerId" class="searchItem" type="hidden" value="${managerId!}">
                <input name="approvalType" id="approvalType" class="searchItem" type="hidden" value="5">
                <input name="entId" id="entId" class="searchItem" type="hidden" value="${entId}">
                <#--<a type="submit" class="btn btn-sm btn-warning" id="search-btn">确定</a>-->
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

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var statusFormat = function (value, column, row) {
        if (row.entStatus == 6) {
            return "审批中";
        }
        if (row.entStatus == 0) {
            return "已通过";
        }
        if (row.entStatus == 8) {
            return "已驳回";
        }

    };

    var buttonsFormat = function (value, column, row) {
        return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/historyEnterprise/historyDetail.html?${_csrf.parameterName}=${_csrf.token}&requestId=" + row.id + "'>查看</a>"];
    }

    var action = "${contextPath}/manage/historyEnterprise/search.html?${_csrf.parameterName}=${_csrf.token}&entId=${entId}";
    var columns = [{name: "createTime", text: "提交审批时间", format: "DateTimeFormat"},
        {name: "entStatus", text: "审批状态", format: statusFormat},
        {name: "op", text: "审批操作", format: buttonsFormat}];

    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

    require(["common", "bootstrap", "daterangepicker", "page/list"], function () {

    });

</script>
</body>
</html>