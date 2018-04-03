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
    <title>流量平台-产品管理</title>
    <meta name="keywords" content="流量平台 产品管理"/>
    <meta name="description" content="流量平台 产品管理"/>

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
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>订购信息查询</h3>
    </div>
    <div class="col-sm-12 dataTables_filter text-right">
        <div class="form-inline" id="table_validate">
            <div class="form-group mr-10 form-group-sm">
                <label for="exampleInputName2">产品名称：</label>
                <input type="text" class="form-control searchItem" name="productName" id="productName"
                       autocomplete="off"
                       placeholder="" value="" maxlength="255"> &nbsp;

                <label for="exampleInputName2">产品编码：</label>
                <input type="text" class="form-control searchItem" name="productCode" id="productCode"
                       autocomplete="off"
                       placeholder="" value="" maxlength="255"> &nbsp;

                <label for="exampleInputName2">流量包大小：</label>
                <input type="text" class="form-control searchItem" name="size" id="size" autocomplete="off"
                       placeholder="" value="" maxlength="255"> &nbsp;

                <input type="hidden" class="form-control searchItem" name="status" id="status" autocomplete="off"
                       placeholder="" value="on" maxlength="255"> &nbsp;

                <input type="hidden" class="form-control searchItem" name="enterId" id="enterId" autocomplete="off"
                       placeholder="" value="${enterprise.id!}" maxlength="255"> &nbsp;

            </div>
            <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>
        </div>
    </div>
    <div class="tools row">
    <#if enterprise?exists>
        <form>
            <div class="tile mt-30">
                <div class="tile-content">
                    <div class="row form">
                        <div class="form-group">
                            <label>企业名称：</label>
                            <span>${enterprise.name!}</span>
                        </div>
                        <div class="form-group">
                            <label>合作开始时间：</label>
                            <#if enterprise.startTime??>
                                <span>${enterprise.startTime?datetime}</span>
                            </#if>
                        </div>

                        <div class="form-group">
                            <label>合作结束时间：</label>
                            <#if enterprise.endTime??>
                                <span>${enterprise.endTime?datetime}</span>
                            </#if>
                        </div>
                        <#if discount?exists && countFlag=="true">
                            <div class="form-group">
                                <label>折扣：</label>
                                <span>${discount.name!}</span>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </#if>

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
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var priceFormat = function (value, column, row) {
        if (row.price == null) {
            return "-";
        }
        return (row.price * 1.0 / 100).toFixed(2);
    };

    var sizeFormat = function (value, column, row) {
        if (row.productSize == null) {
            return "-";
        }

        if (row.productSize < 1024) {
            return row.productSize + "KB";
        }
        if (row.productSize >= 1024 && row.productSize < 1024 * 1024) {
            return (row.productSize * 1.0 / 1024) + "MB";
        }
        if (row.productSize >= 1024 * 1024) {
            return (row.productSize * 1.0 / 1024 / 1024) + "GB";
        }

        return (row.productSize * 1.0 / 1024) + "MB";

    };

    var discountFormat = function (value, column, row) {
        if (row.discount == 100) {
            return "无折扣";
        }
        return row.discount * 1.0 / 10 + "折";
    };

    var flag = "${countFlag!}";

    var columns = [{name: "name", text: "产品名称", tip: true},
        {name: "productCode", text: "产品编码"},
        {name: "productSize", text: "流量包大小", format: sizeFormat},
        {name: "price", text: "售出价格(元)", format: priceFormat},
    ];
    if (flag == "true") {
        columns.push({name: "count", text: "剩余个数"});
    }
    if (flag == "false") {
        columns.push({name: "discount", text: "产品折扣", format: discountFormat});
    }

    var action = "${contextPath}/manage/platformProduct/searchOrderList.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "page/list"], function () {

    });
</script>


</body>
</html>