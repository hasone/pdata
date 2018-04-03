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
    <title>流量平台-营销卡制卡申请列表</title>
    <meta name="keywords" content="流量平台 营销卡制卡申请列表"/>
    <meta name="description" content="流量平台 营销卡制卡申请列表"/>

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
    </style>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>制卡申请列表</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/mdrc/cardmake/create.html" class="btn btn-sm btn-danger">
                <i class="fa fa-plus mr-5"></i>新增制卡申请
            </a>
        </div>

        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">
                <div class="form-group mr-10 form-group-sm">
                
                	<div class="form-group mr-10 form-group-sm">
                    	<label>企业名称：</label>
                    	<input type="text" name="enterpriseName" class="form-control searchItem " id="enterpriseName" placeholder="">
                	</div>
                	
                	<div class="form-group mr-10 form-group-sm">
                    	<label>集团客户编码：</label>
                    	<input type="text" name="enterpriseCode" class="form-control searchItem " id="enterpriseCode" placeholder="">
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
                </div>
                <button id="search-btn" class="btn btn-sm btn-warning">确定</button>
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

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    require(["common", "bootstrap", "page/list"], function () {
    });

    var buttonsFormat = function (value, column, row) {
    	if(row.result==2){
    		return [
            	"<a class='btn-icon icon-edit mr-5' href='${contextPath}/manage/mdrc/cardmake/edit.html?requestId=" + row.id + "'>编辑</a>",
            	"<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/cardmake/requestDetail.html?requestId=" + row.id + "'>详情</a>"
        	];
    	}else{
    		return [
            	"<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/mdrc/cardmake/requestDetail.html?requestId=" + row.id + "'>详情</a>"
        	];
    	}
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

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    };

    var resultFormat = function (value, column, row) {
        if(row.result==0){
            return "审批中";
        }
        if(row.result==1){
            return "已通过";
        }
        if(row.result==2){
            return "已驳回";
        }
    };
    
    var productSizeFormat = function (value, column, row) {
    	if(value){
    		if(value < 1024){
    			return value + "KB";
    		}else if( value>1024 && value < 1024*1024 ){
    			return value/1024 + "MB"
    		}else{
    			return value/(1024*1024) + "GB";
    		}
    	}else{
    		return "-"
    	}
    };

    var columns = [
        {name: "entName", text: "企业名称", tip:true},
        {name: "entCode", text: "集团客户编码",tip:true},
        {name: "configName", text: "卡名称",tip:true},
        {name: "amount", text: "制卡数量"},
        {name: "productSize", text: "流量大小", format: productSizeFormat},
        {name: "result", text: "状态", format: resultFormat},
        {name: "op", text: "操作", format: buttonsFormat},
        {name: "createTime", text: "创建时间", format: dateFormator}
    ];
    var action = "${contextPath}/manage/mdrc/cardmake/search.html?${_csrf.parameterName}=${_csrf.token}";
</script>
</body>
</html>
