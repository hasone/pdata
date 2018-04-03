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
<#-- queryBar -->
<#assign productName = (pageResult.queryObject.queryCriterias.productName)! >
<#assign productCode = (pageResult.queryObject.queryCriterias.productCode)! >
<#assign status = (pageResult.queryObject.queryCriterias.status)! >

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>订购信息查询</h3>
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
                            <span>${startTime!}</span>
                        </div>

                        <div class="form-group">
                            <label>合作结束时间：</label>
                            <span>${endTime!}</span>
                        </div>

                        <div class="form-group">
                            <label>流量池余额：</label>
                            <span>${flowCount!}</span>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </#if>

    </div>


</div>


<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>


</body>
</html>