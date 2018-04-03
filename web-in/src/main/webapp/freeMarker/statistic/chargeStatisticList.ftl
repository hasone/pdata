<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-充值报表</title>
    <meta name="keywords" content="流量平台 充值报表"/>
    <meta name="description" content="流量平台 充值报表"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>

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

        .form-group label {
            width: 77px;
            text-align: right;
        }

        .search-separator {
            display: none;
        }

        @media (max-width: 1300px) {
            .search-separator {
                display: block;
            }
        }

        #autoComplete {
            display: inline-block;
        }

        #moreSearch {
            position: relative;
            cursor: pointer;
        }

        #moreSearch .font-more {
            font-size: 14px;
            color: #428bca;
        }

        #more-wrap {
            position: absolute;
            left: -256px;
            top: 37px;
            z-index: 100;
            width: 580px;
            background-color: #f2f2f2;
            padding: 34px;
            border: 1px solid #a8a8a8;
            box-shadow: 0px 0px 5px #a8a8a8;
        }

        #more-wrap:before {
            position: absolute;
            content: "";
            width: 0;
            height: 0;
            left: 279px;
            top: -11px;
            border-bottom: 10px solid #a8a8a8;
            border-left: 10px solid transparent;
            border-right: 10px solid transparent;
            z-index: 102;
        }

        #more-wrap:after {
            position: absolute;
            content: "";
            width: 0;
            height: 0;
            left: 280px;
            top: -9px;
            border-bottom: 9px solid #f2f2f2;
            border-left: 9px solid transparent;
            border-right: 9px solid transparent;
            z-index: 102;
        }

        .btn-wrap {
            width: 150px;
            margin: 0 auto;
            margin-top: 15px;
        }
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>充值报表</h3>
    </div>
    <div class="tools row">
        <div class="col-lg-12 dataTables_filter">
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                <div class="form-group" id="base-wrap">
                    <div class="form-group form-group-sm">
                        <label for="searchTime">充值时间：</label>
                        <div class="btn-group btn-group-sm">
                            <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                                   name="startTime"
                                   id="startTime" value="${startTime!}">
                            <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                                   name="endTime"
                                   id="endTime" value="${endTime!}">
                            <button type="button" class="btn btn-default">
                                <span id="startTime"> ${startTime!} </span>~<span
                                    id="endTime">${endTime!}</span>
                            </button>
                            <button type="button" class="btn btn-default dropdown-toggle"
                                    data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu timeList">
                                <li data-value="1"><a href="#">近7天</a></li>
                                <li data-value="2"><a href="#">近30天</a></li>
                                <li data-value="3" id="search-time-range"><a id="customRange"
                                                                             href="#">自定义</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group form-group-sm">
                        <label for="status">充值状态：</label>
                        <div class="btn-group btn-group-sm">
                            <input style="width: 0; height: 0; opacity: 0" class="searchItem"
                                   name="status"
                                   id="status">
                            <button type="button" class="btn btn-default">全部</button>
                            <button type="button" class="btn btn-default dropdown-toggle"
                                    data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu statusList">
                                <li data-value=""><a href="#">全部</a></li>
                                <li data-value="1"><a href="#">待充值</a></li>
                                <li data-value="2"><a href="#">已发送充值请求</a></li>
                                <li data-value="3"><a href="#">充值成功</a></li>
                                <li data-value="4"><a href="#">充值失败</a></li>
                            </ul>
                        </div>
                    </div>
                    
                    <#if crowdfunding>
                        <input style="width: 0; height: 0; opacity: 0" name="type" id="type" class="searchItem" value="13">
                    </#if>    
                                    
                </div>
                <div class="form-group form-group-sm" id="moreSearch">
                    <span class="font-more ml-10">更多查询</span>
                    <div class="form-group" id="more-wrap" style="display: none">
                        <div class="form-inline">
                            <div class="form-group form-group-sm">
                                <label for="name">手机号码：</label>
                                <input type="text" name="mobile" id="mobile" class="form-control"
                                       style="width: 150px;"
                                       autocomplete="off"
                                       placeholder="" value=""
                                       maxlength="255">
                            </div>
                            <div class="form-group form-group-sm">
                                <label for="name">企业名称：</label>
                                <input type="hidden" name="eName" id="eName"
                                       class="form-control enterprise_autoComplete"
                                       autocomplete="off"
                                       placeholder="" value=""
                                       maxlength="255">
                            </div>
                        </div>
                        <div class="form-inline mt-10">
                            <div class="form-group form-group-sm">
                                <label>地区：</label>
                                <div class="btn-group btn-group-sm text-left" id="tree"></div>
                                <span id="area_error" style='color:red'></span>
                                <input hidden id="managerId" name="managerId"
                                       value="${managerId!}"/>
                            </div>
                            <div class="form-group form-group-sm">
                                <label for="type">活动类型：</label>
                                <#if !crowdfunding>
                                    <div class="btn-group btn-group-sm">
                                        <input style="width: 0; height: 0; opacity: 0" name="type" 
                                               id="type">
                                        <button type="button" class="btn btn-default"
                                                style="width: 110px">选择类型
                                        </button>
                                        <button type="button" class="btn btn-default dropdown-toggle"
                                                data-toggle="dropdown"
                                                aria-haspopup="true" aria-expanded="false">
                                            <span class="caret"></span>
                                            <span class="sr-only">Toggle Dropdown</span>
                                        </button>
                                        <ul class="dropdown-menu typeList">
                                            <li data-value=""><a href="#">全部</a></li>
                                            <li data-value="0"><a href="#">接口调用</a></li>
                                            <li data-value="1"><a href="#">赠送</a></li>
                                            <li data-value="2,8"><a href="#">红包</a></li>
                                            <li data-value="3"><a href="#">转盘</a></li>
                                            <li data-value="4"><a href="#">砸金蛋</a></li>
                                            <li data-value="5"><a href="#">流量券</a></li>
                                            <li data-value="6"><a href="#">二维码</a></li>
                                            <li data-value="18"><a href="#">包月赠送</a></li>
                                        </ul>
                                    </div>
                                <#else>
                                    <div class="btn-group btn-group-sm">
                                        <input style="width: 0; height: 0; opacity: 0" name="type" value="13">
                                        <button type="button" class="btn btn-default"
                                                style="width: 110px">众筹活动
                                        </button>
                                        <button type="button" class="btn btn-default dropdown-toggle"
                                                data-toggle="dropdown"
                                                aria-haspopup="true" aria-expanded="false">
                                            <span class="caret"></span>
                                            <span class="sr-only">Toggle Dropdown</span>
                                        </button>
                                        <ul class="dropdown-menu typeList">
                                            <li data-value="13"><a href="#">众筹活动</a></li>
                                        </ul>
                                    </div>
                                </#if>
                            </div>
                        </div>
                        <#if isSdEnvironment??>
                        <div class="form-inline mt-10">
                            <div class="form-group form-group-sm">
                                <label>产品类型：</label>
                                 <div class="btn-group btn-group-sm">
                                        <input style="width: 0; height: 0; opacity: 0" name="sdPrdType" 
                                               id="sdPrdType">
                                        <button type="button" class="btn btn-default"
                                                style="width: 110px">选择类型
                                        </button>
                                        <button type="button" class="btn btn-default dropdown-toggle"
                                                data-toggle="dropdown"
                                                aria-haspopup="true" aria-expanded="false">
                                            <span class="caret"></span>
                                            <span class="sr-only">Toggle Dropdown</span>
                                        </button>
                                        <ul class="dropdown-menu typeList">
                                            <li data-value=""><a href="#">全部</a></li>
                                            <li data-value="1087"><a href="#">1087</a></li>
                                            <li data-value="1092"><a href="#">1092</a></li>
                                            <li data-value="1099"><a href="#">1099</a></li>
                                            <li data-value="1105"><a href="#">1105</a></li>
                                        </ul>
                                </div>
                            </div>
                        </div>
                        </#if>
                        <div class="btn-wrap">
                            <a class="btn btn-sm btn-warning" id="search-btn">查询</a>
                            <a class="btn btn-sm btn-warning" id="clear-btn">清空</a>
                            <a class="btn btn-sm btn-warning" id="close-btn">关闭</a>
                        </div>
                    </div>
                </div>
                <div class="pull-right">
                    <a onclick="createFile()" class="btn btn-sm btn-warning">报表导出</a>
                </div>
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
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close"><span
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
            </div>
        </div>
    </div>
    
    <div class="modal fade dialog-sm" id="result-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                </div>
            </div>
        </div>
    </div>
</div>

<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var comboTree;
    require(["moment", "common", "page/list", "bootstrap", "daterangepicker", "ComboTree", "react",
             "react-dom"],
            function (mm, common, list, bootstrap, daterangepicker, ComboTree, React, ReactDOM) {
                window.moment = mm;
                initSearchDateRangePicker();
                listeners();

                //筛选树形接口
                comboTree = ReactDOM.render(React.createElement(ComboTree, {
                    name: "managerId",
                    url: "${contextPath}/manage/tree/getRoot.html"
                }), $("#tree")[0]);
                var tree = comboTree.getReference();
                tree.on("open", function (item) {
                    if (item.open) {
                        tree.deleteChildItems(item);
                        ajaxData("${contextPath}/manage/tree/getChildNode.html",
                                 {"parentId": item.id}, function (ret) {
                                    if (ret) {
                                        tree.loadDynamicJSON(item, ret);
                                    }
                                });

                    }
                });
                //选中区域
                tree.on("select", function (item) {
                    $("#managerId").val(item.id);
//            console.log($("#managerId").val());
                });

//        $('a','.dropdown-menu.timeList li[data-value="1"]').click();

            });

    var buttonsFormat = function (value, column, row) {
        var msg = row.chargeMsg;
        var queryTime = row.queryTime;
        if (msg != null) {
            msg = msg.replace("\"", "&quot;");
        }
        if (row.chargeStatus == 4) {
            return '<a class="btn-icon icon-detail mr-5" onclick="showTip(this)" data-msg="' + msg
                   + '">查看失败原因</a>';
        }
    	if (row.chargeStatus == 2 && row.isQueryChargeResult == 1 
        		&& new Date(row.chargeTime).getTime() < (new Date().getTime()- 1800000)) {
        	return '<a class="btn-icon icon-detail mr-5" data-systemnum = "' + row.systemNum + '" data-fingerprint = "' + row.fingerprint + '" onclick="searchResult(this)" data-msg="' + msg + '">查询结果</a>';
        }
        /* if(!queryTime){
        	if (row.chargeStatus == 2 && row.isQueryChargeResult == 1 
            		&& new Date(row.chargeTime).getTime() < (new Date().getTime()- 1800000)) {
            	return '<a class="btn-icon icon-detail mr-5" data-systemnum = "' + row.systemNum + '" data-fingerprint = "' + row.fingerprint + '" onclick="searchResult(this)" data-msg="' + msg + '">查询结果</a>';
            }
        }else{
        	if (row.chargeStatus == 2 && row.isQueryChargeResult == 1){
        		var time = new Date().getTime() - new Date(queryTime).getTime();
            	if (time >= 30 * 60 * 1000) {
        			return '<a class="btn-icon icon-detail mr-5" data-systemnum = "' + row.systemNum + '" data-fingerprint = "' + row.fingerprint + '" onclick="searchResult(this)" data-msg="' + msg + '">查询结果</a>';    
                } else {
                	var leftTime = 30 * 60 * 1000 - time;
                	var minute = parseInt(leftTime/1000/60);
                	var second = parseInt((leftTime - minute * 60 *1000)/1000);
                	return minute + '分'+ second + '秒后查询';
                }
        	}
        }   */
    }
    
    function initTime(time) {
    	if (time == 0) {
			return '<a class="btn-icon icon-detail mr-5" data-systemnum = "' + row.systemNum + '" data-fingerprint = "' + row.fingerprint + '" onclick="searchResult(this)" data-msg="' + msg + '">查询结果</a>';    
        } else {
        	var minute = time/1000/60;
        	var second = (time/1000)%60;
        	return minute + '分'+ second + '秒';
        }
    }
   /* 
    function wait(ele,time){
    	if (time == 0) {
			$(ele).html('<a class="btn-icon icon-detail mr-5" data-systemnum = "' + row.systemNum + '" data-fingerprint = "' + row.fingerprint + '" onclick="searchResult(this)" data-msg="' + msg + '">查询结果</a>');    
        } else {
        	var minute = time/1000/60;
        	var second = (time/1000)%60;
        	$(ele).html( minute + '分'+ second + '秒');
        	time--;
            setTimeout(function () {
            	wait(time)
            },1000)
        }
    	
    }*/
    
    function searchResult(ele){
    	var fingerprint = $(ele).data("fingerprint");
    	var systemNum = $(ele).data("systemnum");
    	$.ajax({
    		url:"${contextPath}/manage/statisticCharge/queryChargeResult.html?${_csrf.parameterName}=${_csrf.token}",
    		type:"POST",
    		data:{
    			systemNum : systemNum,
    			fingerprint : fingerprint
    		},
    		dataType:"JSON",
    		success: function(ret){
    			if(ret && ret.success){
    				showResultDialog("充值成功");
    			} else if(ret && ret.failed) {
    				showResultDialog("充值失败");
    			} else if(ret && ret.process) {
    				showResultDialog("请稍后再查");
    			} else if(ret && ret.time) {
    				showResultDialog(ret.time);
 				}else {
    				showResultDialog("查询异常");
    			}
    		}
    	});
    }
    
    function showResultDialog(msg){
    	$("#result-dialog .message-content").html(msg);
    	$("#result-dialog").modal("show");
    }

    /**
     *
     */
    function showTip(ele) {
        showTipDialog($(ele).data("msg"));
    }

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
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt =
                        fmt.replace(RegExp.$1,
                                    (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(
                                            ("" + o[k]).length)));
            }
        }
        return fmt;
    }

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(moment(value).toDate(), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }
    var statusFormat = function (value, column, row) {
        if (row.chargeStatus == 1) {
            return "待充值";
        }
        if (row.chargeStatus == 2) {
            return "已发送充值请求";
        }
        if (row.chargeStatus == 3) {
            return "充值成功";
        }
        if (row.chargeStatus == 4) {
            return "充值失败";
        }
        return "";
    }
    
    var activityTypeFormat = function (value, column, row){
    	if(row.chargeType && row.chargeType == "拼手气红包"){
    		return "红包";
    	}
    	return row.chargeType;
    }

    var columns = [{name: "eName", text: "企业名称", tip: true},
        {name: "eCode", text: "企业编码", tip: true},
        {name: "fullDistrictName", text: "地区", tip: true},
        {name: "aName", text: "活动名称", tip: true},
        {name: "chargeType", text: "活动类型", format: activityTypeFormat},
        {name: "pName", text: "产品名称", tip: true},
        {name: "mobile", text: "手机号码"},
        {name: "chargeTime", text: "充值时间", format: "DateTimeFormat"},
        {name: "chargeStatus", text: "充值状态", format: statusFormat}

        <#if showStatus?? && showStatus>
            ,{name: "op", text: "操作", format: buttonsFormat}
        </#if>
    ];

    var action = "${contextPath}/manage/statisticCharge/listSearch.html?${_csrf.parameterName}=${_csrf.token}";

    function listeners() {
        $("#search-btn").on('click',function(){
            $("#more-wrap").hide();
        });
        $('#close-btn').on('click', function (event) {
            $('#clear-btn').click();
            removeSearchItem();
            $('#more-wrap').hide();
            event.stopPropagation();
        });

        $('#clear-btn').on('click', function () {
            $('#mobile').val("");
            $('a', '.dropdown-menu.typeList li[data-value=""]').click();
            var itemId = comboTree.getReference().getSelectedItem();
            comboTree.getReference().unSelectItem(itemId);
            comboTree.setValue("");
            $("input[name='managerId']").val("");
            $("#eName").val("");
            $(".cm-select-value.cm-form-control").val("");

        });

        $('#moreSearch').on('click', function () {
            addSearchItem();
            $('#more-wrap').show();
        });

        $('.dropdown-menu.timeList').on('click', 'a:not(#customRange)', function () {
            var now = new Date();
            var nowadays = now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate();
            var before7days = new Date(now);
            before7days.setDate(now.getDate() - 6);
            before7days =
                    before7days.getFullYear() + "-" + (before7days.getMonth() + 1) + "-"
                    + before7days.getDate();
            var before30days = new Date(now);
            before30days.setDate(now.getDate() - 29);
            before30days =
                    before30days.getFullYear() + "-" + (before30days.getMonth() + 1) + "-"
                    + before30days.getDate();

            var finalDays;
            if ($(this).parents('li').attr("data-value") == "1") {
                finalDays = before7days;
            } else if ($(this).parents('li').attr("data-value") == "2") {
                finalDays = before30days;
            }

            $(".btn", $(this).parents(".btn-group")).eq(0)
                    .html('<span id="startTime">' + finalDays + '</span>~<span id="endTime">'
                          +                                     nowadays                            +                          '</span>');
            $("input", $(this).parents(".btn-group")).eq(0).val(finalDays).blur();
            $("input", $(this).parents(".btn-group")).eq(1).val(nowadays).blur();

            removeSearchItem();
            $('#search-btn').click();
        });

        $('.dropdown-menu.statusList').on('click', 'a', function () {
            removeSearchItem();
            $('#search-btn').click();
        });

        $('.dropdown-menu.timeList').on('click', 'a#customRange', function (){
            $("#more-wrap").hide();
        });
        
        $('#result-dialog .close, #ok').on('click',function(){
        	$('#search-btn').click();
        });
    }
    
    function addSearchItem() {
        var inputs = $('input', '#more-wrap');
        inputs.each(function () {
            if ($(this).attr('name') == undefined) {
                $(this).removeClass('searchItem');
            } else {
                $(this).addClass('searchItem');
            }
        });
    }

    function removeSearchItem() {
        var inputs = $('input', '#more-wrap');
        inputs.each(function () {
            $(this).removeClass('searchItem');
        });
    }

    function initSearchDateRangePicker() {
        var ele = $('#search-time-range');

        ele.dateRangePicker({
            separator: ' ~ ',
            showShortcuts: true,
            shortcuts: {
                'prev': ['week', 'month']
            },
            maxDays: 90,
            beforeShowDay: function (t) {
                var valid = t.getTime() < new Date().getTime();
                return [valid, '', ''];
            },
            setValue: function (s, s1, s2) {
                $(".btn", ele.parents(".btn-group")).eq(0).html('<span id="startTime">' + s1 + '</span>~<span id="endTime">' + s2 + '</span>');
                $("input", $(this).parents(".btn-group")).eq(0).val(s1).blur();
                $("input", $(this).parents(".btn-group")).eq(1).val(s2).blur();
            }
        }).bind('datepicker-closed', function () {
            /* This event will be triggered after date range picker close animation */
            $('#search-btn').click();
        });
    }

</script>

<script>
    //获取元素的值,这里需要判断更多查询是否显示
    function getValue(elem) {
        return $(elem).hasClass("searchItem") ? $(elem).val() : "";
    }

    function createFile() {
        var status = document.getElementById('status').value;
        var startTime = document.getElementById('startTime').value;
        var endTime = document.getElementById('endTime').value;

        var eName = getValue(document.getElementById('eName'));
        eName = eName ? eName.replace("%", "%25") : "";

        var mobile = getValue(document.getElementById('mobile'));
        mobile = mobile ? mobile.replace("%", "%25") : "";

        var type = getValue(document.getElementById("type"));

        var managerId = getValue(document.getElementById('managerId'));

        var sdPrdType = getValue(document.getElementById("sdPrdType"));

        //检查导出的时间是否超过六个月，超过六个月的给出提醒
        if (startTime != null && endTime != null && startTime != "" && endTime != "") {
            var start = new Date(startTime.replace(/-/g, "/"));
            var end = new Date(endTime.replace(/-/g, "/"));
            var s = (end.getYear() - start.getYear()) * 12 + end.getMonth() - start.getMonth();
            if (s > 6) {
                showTipDialog("无法导出时间超过6个月的数据！");
            }
            else {
                window.open(
                        "${contextPath}/manage/statisticCharge/creatCSVfile.html?mobile=" + mobile
                        + "&&type=" + type + "&&eName=" + eName + "&&managerId=" + managerId
                        + "&&startTime=" + startTime + "&&endTime=" + endTime + "&&status="
                        + status + "&&sdPrdType=" + sdPrdType);
            }
        }
        else {
            showTipDialog("导出最近6个月的数据！");
            window.open("${contextPath}/manage/statisticCharge/creatCSVfile.html?mobile=" + mobile
                        + "&&type=" + type + "&&eName=" + eName + "&&managerId=" + managerId
                        + "&&startTime=" + startTime + "&&endTime=" + endTime + "&&status="
                        + status + "&&sdPrdType=" + sdPrdType);
        }

    }

</script>
</body>
</html>
