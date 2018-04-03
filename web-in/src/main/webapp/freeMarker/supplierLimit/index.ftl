<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-限额管理</title>
    <meta name="keywords" content="流量平台 限额管理"/>
    <meta name="description" content="流量平台 限额管理"/>

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
        <h3>限额管理</h3>
    </div>
    
    <div class="tools row">
        
        <div class="col-sm-12 dataTables_filter text-right">
            <div class="form-inline" id="table_validate">
                <div class="form-group form-group-sm">
	            	<label for="name">供应商：</label>
	                <input type="text" class="form-control searchItem" id="supplierName" name="supplierName">
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
                    <span class="message-content" id="msg"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
    
    <div class="modal fade dialog-sm" id="del-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content" id="delMsg"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="delOk">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
    
    <div class="modal fade dialog-sm" id="subject-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content" id="tips"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
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
    var limitMoneyFormat = function (value, column, row) {
    	if(row.limitMoneyFlag && row.limitMoneyFlag == 1){
    		if(row.limitMoney && row.limitMoney>0){
        		return (row.limitMoney/1000000).toFixed(4);
	        }else{
	        	return "0.0000";
	        }
    	}else{
    		return "-1";
    	}
    };
    
    var nowUsedMoneyFormat = function (value, column, row) {
	    if(row.limitMoneyFlag && row.limitMoneyFlag == 1){
	    	if(row.nowUsedMoney && row.nowUsedMoney>0){
    			return (row.nowUsedMoney/1000000).toFixed(4);
	        }else{
	        	return "0.0000";
	        }
	    }else{
			return "-1";    
	    }
    };
    
    var limitMoneyFlagFormat = function (value, column, row) {
    	if(row.limitMoneyFlag && row.limitMoneyFlag == 1){
	        return "是";
    	}else{
    		return "否";
    	}
    };
    
    var productLimitMoneyFlagFormat = function (value, column, row) {
    	if(row.productLimitMoneyFlag && row.productLimitMoneyFlag == 1){
	        return "是";
    	}else{
    		return "否";
    	}
    };

    var buttonsFormat = function (value, column, row) {
    	return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/supplierLimit/showDetail.html?id=" + row.id + "'>详情</a>"];
    }

    var columns = [{name: "name", text: "供应商", tip: true},
        {name: "limitMoney", text: "全量每日上限(万元)", format: limitMoneyFormat},
        {name: "nowUsedMoney", text: "今日充值金额(万元)", format: nowUsedMoneyFormat},
        {name: "limitMoneyFlag", text: "是否全量控制", format: limitMoneyFlagFormat},
        {name: "productLimitMoneyFlag", text: "是否有产品控制", format: productLimitMoneyFlagFormat},
        {name: "limitUpdateTime", text: "编辑时间", format: "DateTimeFormat"},
        {name: "op", text: "操作", format: buttonsFormat}];

    var action = "${contextPath}/manage/supplierLimit/search.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "page/list"], function () {
    	$("#subject-btn").on("click", function () {
        	window.location.reload();
        });
    });


</script>
</body>
</html>