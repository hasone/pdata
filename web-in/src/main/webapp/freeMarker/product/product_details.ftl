<!DOCTYPE html>
<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新增产品</title>
    <meta name="keywords" content="流量平台 新增产品" />
    <meta name="description" content="流量平台 新增产品" />

    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="../../assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="../../assets/css/base.min.css"/>
    <link rel="stylesheet" href="../../assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="../../assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form select{
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 200px;
        }
        .form .form-group label{width: 100px;text-align: right;}
        .form .promote{
            margin-left: 105px;
            color: #999;
            font-size: 12px;
        }
    </style>
</head>
<body>

    <div class="main-container">
        <div class="module-header mt-30 mb-20">
            <h3>产品添加
                <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
            </h3>
        </div>


        <div class="tile mt-30">
            <div class="tile-header">
                详情
            </div>
            <div class="tile-content">
                <div>集团产品号码: <span>${entProCode}</span><a href= "${contextPath}/cq/product/updateBalance1.html?${_csrf.parameterName}=${_csrf.token}&entProCode=${entProCode}&entId=${entId}" class="btn btn-sm btn-danger ml-20"><i class="fa fa-refresh mr-5"></i>同步余额</a></div>
                <div class="table-responsive mt-10" role="table"></div>
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