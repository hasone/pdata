<!DOCTYPE html>
<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品管理</title>
    <meta name="keywords" content="流量平台 产品管理" />
    <meta name="description" content="流量平台 产品管理" />

    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="../../assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="../../assets/css/base.min.css"/>
    <link rel="stylesheet" href="../../assets/css/innerbase.min.css"/>
    <style>
        .icon-detail { color: #ca95db;background-image: url(../../assets/imgs/icon-detail.png); }
        .icon-edit { color: #5faee3;background-image: url(../../assets/imgs/icon-edit.png); }
        .icon-del { color: #fa8564;background-image: url(../../assets/imgs/icon-del.png); }
        .icon-down { color: #aec785;background-image: url(../../assets/imgs/icon-down.png); }
        .icon-online{color: #aec785;background-image: url(../../assets/imgs/icon-online.png);}
        .icon-search{color: #ca95db;background-image: url(../../assets/imgs/icon-search.png);}
    </style>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

    <div class="main-container">
        <div class="module-header mt-20">
            <h3>集团产品详情
                <a class="btn btn-sm btn-primary pull-right btn-icon icon-back" href="javascript: history.go(-1)">返回</a>
            </h3>
        </div>

        <div class="tile mt-30">
            <div class="tile-header">
                集团产品号码余额
            </div>
            <div class="tile-content">
                <div>集团产品号码：${entProCode}  <a href= "${contextPath}/cq/product/updateBalance.html?${_csrf.parameterName}=${_csrf.token}&entProCode=${entProCode}&entId=${entId}" class="btn btn-sm btn-danger ml-20"><i class="fa fa-refresh mr-5"></i>同步余额</a></div>
                <div class="mt-20" role="table"></div>
            </div>
        </div>
    </div>

    <script src="../../assets/lib/require.js"></script>
    <script src="../../assets/js/config.js"></script>
    <script>

        var action = "${contextPath}/cq/product/orderDetailsList.html?${_csrf.parameterName}=${_csrf.token}&entProCode=${entProCode}&entId=${entId}";
        var columns = [
            {name: "proName", text: "产品名称"},
            {name: "discount", text: "折扣"},
            {name: "count", text: "余额"}
        ];

        require(["page/table","common", "bootstrap"], function(tableWidget) {
            tableWidget.search();
        });
    </script>
</body>
</html>