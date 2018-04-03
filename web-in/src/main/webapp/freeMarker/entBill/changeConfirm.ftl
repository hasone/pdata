<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-申请调账</title>
    <meta name="keywords" content="流量平台 申请调账" />
    <meta name="description" content="流量平台 申请调账" />

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/DateTime.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .nav>li>a {
            padding: 5px 15px;
        }
        .nav-tabs{
            border-bottom: 2px solid #60a3d2;
        }
        .nav-tabs>li.active>a, .nav-tabs>li.active>a:focus, .nav-tabs>li.active>a:hover{
            border-color: #60a3d2;
        }
        .nav-tabs>li>a, .nav-tabs>li>a:focus, .nav-tabs>li>a:hover{
            color: #60a3d2;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>申请调账
        	 <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile">
        <div class="tile-header">调账企业</div>
        <div class="tile-content">
        	<form id="data-form" action="${contextPath}/manage/entBill/submitChangeApproval.html" method="post">
	            <div class="form-inline text-right">
	                <div class="form-group form-group-sm">
	                    <label>企业名称: </label>
	                    <div class="btn-group btn-group-sm">
	                        <input name="enterId" class="searchItem" type="hidden" value="${enterprise.id!}">
	                        <span>${enterprise.id!}   </span>
	                    </div>
	                </div>
	                <div class="form-group form-group-sm ml-5">
	                    <label>企业编码: </label>
	                    <span id="code">${enterprise.code!}</span>
	                </div>
	                <div class="form-group form-group-sm ml-5">
	                    <label>账单时间: </label>
	                    <input type="hidden" id="chargeTime" name="chargeTime" value="${chargeTime!}">
	                    <span style="text-align: left;">${chargeTime!}</span>
	                </div>					
					
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<input name="recordSystemNums" class="searchItem" type="hidden" value="${recordSystemNums!}">
					
	            </div>
            </form>
        </div>
    </div>


    <div class="tile mt-30" id="detail-wrap">
        <div class="tile-header">调账记录</div>        
            <div class="mt-20"><label>调账类型: </label><span id="type"></span>
            <label>总计: </label><span id="price"></span>元,
            <span id="count"></span>条</div>
            
            <a class="btn btn-sm btn-danger dialog-btn ml-5" data-toggle="modal" data-target="#sq-dialog">确认提交</a>

            <div class="mt-20">
                <div role="table"></div>
                <div role="pagination"></div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="sq-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">确认提交将进入调账审批流程，审批中的记录不可被调账。</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                <button class="btn btn-warning btn-sm" id="sp-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>


<div class="modal fade dialog-sm" id="tip-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
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

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    var ctx = "";
    var action = "${contextPath}/manage/entBill/confirmSearch.html?${_csrf.parameterName}=${_csrf.token}";
    var row_index = 0;
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
    }
    
    var priceFormat = function (value, column, row) {
        if(row.price!=null){
        	return row.price/100.00;
        }else{
        	return 0;
        }
    };
    
    var intervalFormat = function (value, column, row) {
        if(row.intervalDate==null){
        	return "-";
        }else{
        	var date = "";
        	var time = parseInt(row.intervalDate);
        	var hour = parseInt(time/3600);
        	var minute = parseInt((time%3600)/60);
        	var second = parseInt((time%3600)%60);
        	date = hour.toString() + "时" + minute.toString() + "分" 
        		+ second.toString() + "秒";
        	return date;
        }
    };
    
    var statusFormat = function (value, column, row) {
        if (row.status == "1") {
            return "待充值";
        }
        if (row.status == "2") {
            return "已发送充值请求";
        }
        if (row.status == "3") {
            return "充值成功";
        }
        if (row.status == "4") {
            return "充值失败";
        }
        //return "";
    }
    
    var columns = [        
        {name: "type", text: "活动类型"},
        {name: "productName", text: "产品名称", tip:true},
        {name: "phone", text: "手机号码"},
        {name: "status", text: "充值状态", format: statusFormat},
        {name: "chargeTime", text: "充值时间", format: "DateTimeFormat"},
        {name: "intervalDate", text: "请求时长", format: intervalFormat},
        {name: "price", text: "金额(元)", format: priceFormat}
    ];

	/**
	 * 查询结束回调
	 */
	window.searchCallback= function(ret){
		$("#price").html(ret.price);
		$("#count").html(ret.total);
		$("#type").html(ret.type);
    }
    
    require(["react","react-dom","DateTime","moment","common", "bootstrap","page/list", "daterangepicker"], function(React, ReactDOM, DateTime, moment, c, b, list){
        //initDateRangePicker2();

        window.moment = moment;

        $("#sp-btn").on("click", function(){
            $("#sq-dialog").modal("hide");
            $("#data-form").submit();
        });

    });

</script>
</body>
</html>