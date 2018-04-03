<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业账单</title>
    <meta name="keywords" content="流量平台 企业账单" />
    <meta name="description" content="流量平台 企业账单" />

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
        <h3>企业账单
        </h3>
    </div>

    <div class="tile">
        <div class="tile-header">出账企业</div>
        <div class="tile-content">
            <div class="form-inline text-right">
                <div class="form-group form-group-sm">
                    <label>企业名称: </label>
                    <div class="btn-group btn-group-sm">
                        <input name="enterId" id="enterId" class="searchItem" style="width: 0; height: 0;padding: 0; opacity: 0;" value="${enterId!}">
                        
                        <#if enterName??>
                        	<button type="button" class="btn btn-default" style="width: 200px;">${enterName!}</button>
                        <#else>
                        	<button type="button" class="btn btn-default" style="width: 200px;">请选择</button>
                        </#if>
                        
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu" style="width: 100%">
                        <#if enters??>
	                        <#list enters as e>
	                        	<li data-value="${e.id!}"><a onclick="showEnterCode('${(e.code)!}')">${e.name!}</a></li>
	                        </#list>
                        </#if>
                        </ul>
                    </div>
                </div>
                <div class="form-group form-group-sm ml-5">
                    <label>企业编码: </label>
                    <span id="code"></span>
                </div>
                <div class="form-group form-group-sm ml-5">
                    <label>账单时间: </label>
                    <!--<input class="form-control searchItem" id="dateTime" name="month">-->
                    <span id="dateTime" style="text-align: left;"></span>
                </div>

                <div class="btn-group btn-group-sm">
                    <a class="btn btn-sm btn-warning dialog-btn ml-5" id="search-btn">查 询</a>
                    <a class="btn btn-sm btn-danger dialog-btn ml-5" data-toggle="modal" id="fund-btn" data-target="#funded-dialog">结 账</a>
                </div>
            </div>
        </div>
    </div>


    <div class="tile mt-30 hidden" id="detail-wrap">
        <div class="tile-header">账单详情</div>
        <div class="tile-content">
            <ul class="nav nav-tabs" role="tablist">
                <input name="type" class="searchItem" type="hidden" id="type" value="cz">
                <li role="presentation" class="active"><a href="#cz" data-type="cz" role="tab" data-toggle="tab">出账</a></li>
                <li role="presentation"><a href="#wcz" role="tab" data-type="wcz" data-toggle="tab">未出账</a></li>
                <li role="presentation"><a href="#dzcz" role="tab" data-type="dzcz" data-toggle="tab">调账出账</a></li>
                <li role="presentation"><a href="#dzwcz" role="tab" data-type="dzwcz" data-toggle="tab">调账未出账</a></li>
            </ul>
            <div class="tile" style="padding: 20px; border: 1px solid #ccc;">
                <div class="form-inline">
                    <div class="form-group form-group-sm">
                        <label>手机号码:</label>
                        <input class="form-control mobileOnly searchItem" name="phone" maxlength="11">
                        <a class="btn btn-sm btn-warning dialog-btn" id="search-mobile-btn">查 询</a>
                    </div>
                    <div class="pull-right">
                        <label>账单操作:</label>
                        <a class="btn btn-sm btn-danger dialog-btn" id="dz-btn">申请调账</a>
                        <a class="btn btn-sm btn-danger dialog-btn">导 出</a>
                    </div>
                </div>
            </div>

            <div class="mt-20"><label>总计: </label><span id="price"></span>元,<span id="count"></span>条</div>

            <div class="mt-20" style="overflow: hidden">
                <div role="table"></div>
                <div role="pagination"></div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="funded-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">结账后账单记录不可被调账，即将进入账单下载页面。</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                <button class="btn btn-warning btn-sm" id="download-btn">确 定</button>
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
    var action = "${contextPath}/manage/entBill/search.html?${_csrf.parameterName}=${_csrf.token}";
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
        {name: "ck", text: "", format: function(value, column, row){
            if(row.changeAccountStatus==0 && parseInt(row.intervalDate)>=48*60*60) {
                row_index++;
                return '<span class="input-checkbox">' +
                        '<input type="checkbox" class="check_input" id="check_' + row_index + '" value="' + row.systemNum + '">' +
                        '<label class="c-checkbox" for="check_' + row_index + '"></label>' +
                        '</span>';
            }else{
                return "";
            }
        }},
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
    }
    
    require(["react","react-dom","DateTime","moment","common", "bootstrap","page/list", "daterangepicker"], function(React, ReactDOM, DateTime, moment, c, b, list){
        //initDateRangePicker2();

        window.moment = moment;
        
        init();
        
        $("#search-mobile-btn").on("click", function(){
            $("#search-btn").click();
        });
        
        $("#search-btn").on("click", function(){       
        	if($("#code").html() != ""){
        		$("#detail-wrap").removeClass("hidden");
        	}
        	
        });

        $('a[data-toggle="tab"]').on("show.tab.bs", function(){
            if($("#type").val() != $(this).data("type")){
            	$("#type").val($(this).data("type"));
            	list.refresh();
            }
        }); 

        $("#download-btn").on("click", function(){
            window.open("download.html");
        });

        var timeEle = null;
        var obj = renderWidget(React, ReactDOM, DateTime, {
            monthOnly: true,
            format: "YYYY-MM",
            name: "chargeTime",
            value: "${chargeTime!}",
            endDate: moment().format("YYYY-MM-DD"),
            onSelectDate: function(value, date){
                timeEle.val(value);
                isFunded(value);
            }
        }, $("#dateTime")[0]);

        timeEle = $("input", ReactDOM.findDOMNode(obj)).addClass("searchItem");

        $("#dz-btn").on("click", function(){
            if(!$(".check_input:checked").length){
                showTipDialog("请选择需要调帐的记录");
                return;
            }
            var check_value = [];
			$(".check_input:checked").each(function(){ 
				check_value.push($(this).val()); 
			});
			var recordSystemNums = check_value.join(",");
			var enterId = $("#enterId").val();
			var chargeTime = $("input[name='chargeTime']").val();	
			window.location.href = "${contextPath}/manage/entBill/changeConfirm.html?recordSystemNums=" 
				+ recordSystemNums + "&&enterId=" + enterId + "&&chargeTime=" + chargeTime;
			return;
        });
        
    });

    /**
     * 判断这个月是不是已经结账了,该月份已结账则隐藏结账按钮和调账按钮
     * @param monthTime
     */
    function isFunded(monthTime){
        //ajax 请求

        //模拟逻辑 11月份已经结账
        if(monthTime == "2016-11"){
            $("#fund-btn").hide();
            $("#dz-btn").hide();
        }else{
            $("#fund-btn").show();
            $("#dz-btn").show();
        }
    }

    function initDateRangePicker2(){
        var ele = $('#dateTime');
        ele.dateRangePicker({
            autoClose: true,
            singleDate : true,
            format: "YYYY-MM",
            singleMonth: true
        });
    }
    
    function showEnterCode(code){
    	$("#code").html(code);
    }
    
    function init(){
    	if($("#enterId").val()!=null && $("#enterId").val()!=""){
    		$("#detail-wrap").removeClass("hidden");
    	}
    }
    
</script>
</body>
</html>