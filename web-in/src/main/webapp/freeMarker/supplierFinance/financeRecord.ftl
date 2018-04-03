<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-财务记录</title>
    <meta name="keywords" content="流量平台 财务记录"/>
    <meta name="description" content="流量平台 财务记录"/>

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
        <h3>财务记录</h3>
    </div>
    
    <div class="tools row">
        
        <div class="col-sm-12 dataTables_filter text-right">
            <div class="form-inline" id="table_validate">
                <div class="form-group form-group-sm">
	            	<label for="name">供应商名称：</label>
	                <input type="text" class="form-control searchItem" id="supplierName" name="supplierName">
	            </div>
                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">查询</a>
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
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var totalMoneyFormat = function (value, column, row) {
    	if(row.totalMoney && row.totalMoney > 0){
    		return (row.totalMoney/1000000).toFixed(4);
    	}
        return "0.0000";
    };
    
    var usedMoneyFormat = function (value, column, row) {
    	if(row.usedMoney && row.usedMoney > 0){
    		return (row.usedMoney/1000000).toFixed(2);
    	}
        return "0.00";
    };
    
    var balanceFormat = function (value, column, row) {
    	if(row.balance){
    		return (row.balance/1000000).toFixed(0);
    	}
    	return "0";
    <#--
    	if(row.usedMoney && row.totalMoney){
    		if(row.totalMoney>=row.usedMoney){
    			var balance = ((row.totalMoney - row.usedMoney)/1000000).toFixed(4);
				return balance;
    		}
    		
    		if(row.totalMoney<row.usedMoney){
    			var balance = ((row.usedMoney - row.totalMoney)/1000000).toFixed(4);
				return "-"+balance;
    		}
    	}
    	
    	if(row.usedMoney && (!row.totalMoney)){
    		return "-"+row.usedMoney.toFixed(4);
    	}
    	
    	if((!row.usedMoney) && row.totalMoney){
    		return row.totalMoney.toFixed(4);
    	}
    	-->
    };

    var buttonsFormat = function (value, column, row) {
    	return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/supplierFinance/payRecord.html?supplierId=" + row.supplierId + "'>查看明细</a>"];
    };
    
    var columns = [{name: "supplierName", text: "供应商", tip: true},
        {name: "totalMoney", text: "付款总金额(万元)", format: totalMoneyFormat},
        {name: "usedMoney", text: "使用总金额(万元)", format: usedMoneyFormat},
        {name: "balance", text: "余额(万元)", format: balanceFormat},
        {name: "operateTime", text: "修改时间", format: "DateTimeFormat"},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/supplierFinance/search.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "page/list"], function () {

    });

</script>
</body>
</html>