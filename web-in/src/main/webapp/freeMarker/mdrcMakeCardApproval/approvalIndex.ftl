<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-制卡审批列表</title>
    <meta name="keywords" content="流量平台 制卡审批列表"/>
    <meta name="description" content="流量平台 制卡审批列表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <style>
        .table > thead > tr > th {
            line-height: initial;
        }

        .table > thead > tr > th, .table > tbody > tr > td {
            vertical-align: middle;
        }
        
        .icon-syxg {
            color: #708090;
            background-image: url(${contextPath}/assets/imgs/icon-syxg.png);
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

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>制卡审批列表</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">
                <div class="form-group mr-20 form-group-sm">
                	<label for="entName">企业名称：</label>
	                <input type="text" name="entName" id="entName"
	                       class="form-control searchItem"
	                       autocomplete="off"
	                       placeholder="" 
	                       value=""
	                       maxlength="255">
	            </div>
	            
	            <div class="form-group mr-20 form-group-sm">
	                <label for="entCode">集团客户编码：</label>
	                <input type="text" name="entCode" id="entCode" 
	               		class="form-control searchItem"
	                    autocomplete="off"
	                    placeholder="" 
	                    value=""
	                    maxlength="255">
	            </div>
	            
	            <div class="form-group mr-20 form-group-sm">
                    <label>状态：</label>
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="searchItem"
                               name="result" value="">
                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value=""><a href="#">全部</a></li>
                            <li data-value="0"><a href="#">审批中</a></li>
                            <li data-value="1"><a href="#">已通过</a></li>
                            <li data-value="2"><a href="#">已驳回</a></li>
                        </ul>
                    </div>
                </div>

                <a type="submit" class="btn btn-sm btn-warning" id="search-btn">确定</a>
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

<!--[if lt IE 9]-->
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<!--[endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var buttonsFormat = function (value, column, row) {
    	if(row.canOperate == "0"){
    		return ["<a class='btn-icon mr-5 icon-detail' href='${contextPath}/manage/approval/requestDetail.html?id=" + row.id + "&canOperate="+ row.canOperate +"'>详情</a>"];
    	}else{
    		return ["<a class='btn-icon mr-5 icon-syxg' href='${contextPath}/manage/approval/operateMakecardApproval.html?id=" + row.id + "&canOperate="+ row.canOperate +"'>审批</a>"];
    	}
    }
    
    var resultFormat = function(value, column, row){
		if(value == 0){
			return "审批中";
		}else if(value == 1){
			return "已通过";
		}else{
			return "已驳回";
		}
    }
    
    var productSizeFormat = function(value, column, row){
    	if(value){
    		if(value < 1024){
    			return value + "KB";
    		}else if(value>1024 && value < 1024*1024){
    			return value/1024 + "MB";
    		}else{
				return value/1024/1024 + "GB";    		
    		} 
    	}else{
    		return "-";
    	}
    }

    var action = "${contextPath}/manage/approval/searchMakecardRecords.html?${_csrf.parameterName}=${_csrf.token}";
    
	 var columns = [{name: "entName", text: "企业名称", tip: true},
	    {name: "entCode", text: "集团客户编码", tip: true},
	    {name: "configName", text: "卡名称",tip:true},
	    {name: "amount", text: "制卡数量"},
	    {name: "productSize", text: "流量大小", format: productSizeFormat},
	    {name: "result", text: "状态", format: resultFormat},
	    {name: "op", text: "操作", format: buttonsFormat},
	    {name: "createTime", text: "创建时间", format: "DateTimeFormat"}];
    

    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

    require(["common", "bootstrap", "page/list"], function () {
        
    });

    
</script>
</body>
</html>