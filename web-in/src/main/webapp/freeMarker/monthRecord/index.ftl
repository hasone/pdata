<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-赠送用户信息</title>
    <meta name="keywords" content="流量平台 赠送用户信息"/>
    <meta name="description" content="流量平台 赠送用户信息"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
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

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>赠送用户信息</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                
                <div class="form-group mr-10 form-group-sm">
                    <label for="mobile">手机号码：</label>
                    <input type="text" class="form-control searchItem mobileOnly" name="mobile" id="mobile" placeholder="" maxlength="11"/>                   
                </div>
                
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">赠送结果：</label>
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; height: 0; opacity: 0; padding: 0;" class="form-control searchItem" name="status" id="status" value="">
                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                        	<li data-value=""><a href="#">全部</a></li>
                        	<li data-value="1"><a href="#">待充值</a></li>
                        	<li data-value="2"><a href="#">已发送充值请求</a></li>
                        	<li data-value="3"><a href="#">充值成功</a></li>
                        	<li data-value="4"><a href="#">充值失败</a></li>
	                    </ul>
                    </div>
                </div>
                <#if provinceFlag??&&provinceFlag!='sd'>
	                <div class="form-group mr-10 form-group-sm">
	                    <label for="exampleInputName2">赠送月份：</label>
	                    <div class="btn-group btn-group-sm">
	                        <input style="width: 0; height: 0; opacity: 0; padding: 0;" class="form-control searchItem" name="giveMonth" id="giveMonth" value="">
	                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
	                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
	                                aria-haspopup="true" aria-expanded="false">
	                            <span class="caret"></span>
	                            <span class="sr-only">Toggle Dropdown</span>
	                        </button>
	                        <ul class="dropdown-menu">
	                            <li data-value=""><a href="#">全部</a></li>
	                            <li data-value="1"><a href="#">第1月</a></li>
	                            <li data-value="2"><a href="#">第2月</a></li>
	                            <li data-value="3"><a href="#">第3月</a></li>
	                            <li data-value="4"><a href="#">第4月</a></li>
	                            <li data-value="5"><a href="#">第5月</a></li>
	                            <li data-value="6"><a href="#">第6月</a></li>
	                            <li data-value="7"><a href="#">第7月</a></li>
	                            <li data-value="8"><a href="#">第8月</a></li>
	                            <li data-value="9"><a href="#">第9月</a></li>
	                            <li data-value="10"><a href="#">第10月</a></li>
	                            <li data-value="11"><a href="#">第11月</a></li>
	                            <li data-value="12"><a href="#">第12月</a></li>
	                        </ul>
	                    </div>
	                </div>
                </#if>
                <a class="btn btn-sm btn-warning mr-10" id="search-btn" href="javascript:void(0)">确定</a>
                <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
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
    
    <div class="modal fade dialog-sm" id="tip-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
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

</div>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var provinceFlag = "${provinceFlag!}";
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

	var chargeRecordStatus = {};
    <#list chargeRecordStatus?keys as item>
    	chargeRecordStatus["${item}"] = "${chargeRecordStatus[item]}";
    </#list>

	var statusFormator = function (value, column, row) {
        var msg = row.errorMessage;
		if(value == 4){
			if (msg != null) {
                msg = msg.replace("\"", "&quot;");
            }
            return ['充值失败', '<a class="btn-icon icon-detail mr-5" onclick="showTip(this)" data-msg="' + msg + '">失败原因</a>'];
		}else{
			return chargeRecordStatus[value];     
		}
    };
    var giveMonthFormator = function (value) {
        if (value) {
            return "第" + value + "月";
        }
        return "";
    };
    var sizeFormat = function (value) {
    	if (value) {
    		if (value == null) {
                return "-";
            }
            if (value < 1024) {
                return row.productSize + "KB";
            }
            if (value >= 1024 && value < 1024 * 1024) {
                return (value * 1.0 / 1024) + "MB";
            }
            if (value >= 1024 * 1024) {
                return (value * 1.0 / 1024 / 1024) + "GB";
            }
            return (value * 1.0 / 1024) + "MB";
        }
    	return "-";
    };
    var columns = [
    	{name: "sysSerialNum", text: "系统流水号", tip: true},
        {name: "mobile", text: "被赠送人号码"},
        {name: "productName", text: "产品名称"},
        {name: "productSize", text: "赠送值", format: sizeFormat},
        {name: "monthCount", text: "赠送月数"}];

    if (provinceFlag != 'sd') {
        columns.push({name: "giveMonth", text: "赠送月份", format: giveMonthFormator});
    }
    columns.push({name: "createTime", text: "赠送时间", format: "DateTimeFormat"});
    columns.push({name: "status", text: "赠送结果", format: statusFormator});

    var action = "${contextPath}/manage/monthRecord/search.html?ruleId=#{ruleId!}&${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "page/list", "daterangepicker"], function () {
        initSearchDateRangePicker();
    });

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range');

        var startEle = $('#startTime');
        var endEle = $('#endTime');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
            beforeShowDay: function (t) {
                var valid = t.getTime() < new Date().getTime();
                return [valid, '', ''];
            },
            customShortcuts: [
                {
                    name: '半年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 182);
                        return [start, end];
                    }
                },
                {
                    name: '一年内',
                    dates: function () {
                        var end = new Date();
                        var start = new Date();
                        start.setDate(start.getDate() - 360);
                        return [start, end];
                    }
                }
            ],
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);
            }
        });
    }

    function showTip(ele) {
        showTipDialog($(ele).data("msg"));
    }
</script>

</body>
</html>