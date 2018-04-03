<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业EC操作记录</title>
    <meta name="keywords" content="流量平台 企业EC操作记录"/>
    <meta name="description" content="流量平台 企业EC操作记录"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
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
        .dialog-sm .modal-dialog {
		    width: auto;
		    max-width: 600px;
		}
		
		.message-content{
			word-break: break-all;
		}
		
				
		.table tbody > tr > td:last-child{
			max-width: 247px;
			width: 181px;
		}
		td div.title{ 
			text-decoration:none; 
		}
		
		.table tbody > tr > td div.title:hover span{
			//display:block;
		}
		
		.table tbody > tr > td div.title span{
			display:none; 
			border:1px solid #000; 
			text-decoration:none;
			color:#000; 
			position:fixed; 
			z-index:100;
			background-color: #fffcf0;
		    text-overflow: initial;
		    word-wrap: break-word;
		    white-space: normal;
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
        <h3>企业EC操作记录</h3>
    </div>

    <div class="tools row text-right">
        <div class="col-lg-12 dataTables_filter text-right">
        	<a class="btn btn-primary btn-sm pull-right btn-icon icon-back"  onclick="javascript:window.location.href='${contextPath}/manage/enterprise/showEcInfo.html?entId=${entId!}'">返回</a>          	
            <div id="searchForm" class="form-inline searchForm" id="table_validate">
                                
                <div class="form-group mr-10 form-group-sm">
                    <label for="status">状态：</label>					
					<select name="status" id="status" class="form-control searchItem">
						<option value="">全部</option>
						<option value="0">关闭</option>
						<option value="1">开通</option>						
					</select>
                </div>
                
                <div class="form-group form-group-sm" id="search-time-range">
                    <label>操作时间：</label>&nbsp
                    <input type="text" class="form-control search-startTime searchItem"
                           name="startTime"
                           value="${(pageResult.queryObject.queryCriterias.startTime)!}"
                           id="startTime" placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime"
                           id="endTime"
                           value="${(pageResult.queryObject.queryCriterias.endTime)!}"
                           placeholder="">
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
    
    <div class="modal fade dialog-sm" id="tip-dialog" data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content bd-muted">
	            <div class="modal-header bg-muted bd-muted">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
	                        aria-hidden="true">&times;</span></button>
	                <h5 class="modal-title">备注信息</h5>
	            </div>
	            <div class="modal-body">
	                <span class="message-content"></span>
	            </div>
	            <div class="modal-footer">
	                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
	            </div>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal-dialog -->
	</div>

    <div role="pagination"></div>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/es5-shim.min.js"></script>
    <script src="../../assets/lib/es5-sham.min.js"></script>
    <![endif]-->
    <script src="${contextPath}/assets/lib/require.js"></script>
    <script src="${contextPath}/assets/js/config.js"></script>
    <script>
    	var opTypeFormat = function (value, column, row) {
           return row.nowStatus == 1 ? "开通" : "关闭";
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
        
       var buttonsFormat = function (value, column, row) {
       		return "<a href='javascript:void(0)' class='btn-icon icon-detail mr-5' onclick='showECChangeDescAjax(" + row.id + ")'>查看</a>";
        }
        
        var opDescFormat = function(value, column, row){
        	return "<div class='title' onmouseover='showDescInfo(this)' onmouseout='hideDescInfo(this)'>"+value+"<span>"+value+"</span></div>";
        }

        var columns = [
        	{name: "createTime", text: "EC操作时间", format: "DateTimeFormat"},        	
            {name: "operatorName", text: "操作用户", tip: true},
            {name: "operatorRole", text: "角色身份", tip: true},
            {name: "opType", text: "状态", tip: true, format:opTypeFormat},
            {name: "opDesc", text: "备注信息", format:opDescFormat}];
		
		var action = "${contextPath}/manage/enterprise/ecSearch.html?${_csrf.parameterName}=${_csrf.token}&&entId=${entId!}";
	
        require(["common", "bootstrap", "daterangepicker","page/list","react","react-dom"], function (common, bootstrap, daterangepicker, list,React, ReactDOM) {
        	initSearchDateRangePicker();
        	//listeners();
        });
        
		function showDescInfo(event){
         	var e = window.event || arguments.callee.caller.arguments[0];
			var width = e.pageX;
			var height = e.pageY + 10;
            var contentWidth = $("span",event).width();
            var contentHeight = $("span",event).height();
            var clientWidth = $("div.main-container").width();
            var clientHeight = $("div.main-container").height();
            if(width+contentWidth > clientWidth && height+contentHeight > clientHeight){
                $("span",event).css({"left":width - contentWidth,"top":height - contentHeight < 0 ? 0 : height - contentHeight}).show();
            }else if(width+contentWidth > clientWidth && height+contentHeight < clientHeight){
                $("span",event).css({"left":width - contentWidth,"top":height}).show();
            }else if(width+contentWidth < clientWidth && height+contentHeight > clientHeight){
                $("span",event).css({"left":width,"top":height - contentHeight < 0 ? 0 : height - contentHeight}).show();
            }else{
                $("span",event).css({"left":width,"top":height}).show();
            }
        }
        
        function hideDescInfo(event){
        	$("span",event).hide();
        }
        
        function listeners(){
        	$(".table tbody > tr > td div.title").on("mouseover",function(e){
        		var e = e||window.event;
        		var width = e.pageX;
        		var height = e.pageY + 10;
        		$("span",this).css({"left":width,"top":height}).show();
        	});
        	$(".table tbody > tr > td div.title").on("mouseout",function(){
        		$("span",this).hide();
        	});
        }
        
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
	                                    if (startEle.val() && endEle.val()) {
	                                        return startEle.val() + ' ~ ' + endEle.val();
	                                    } else {
	                                        return '';
	                                    }
	                                },
	                                setValue: function (s, s1, s2) {
	                                    startEle.val(s1);
	                                    endEle.val(s2);
	                                }
	                            });
    	}
     
    function showECChangeDescAjax(id){
     	$.ajax({
                type: "POST",
                url: "${contextPath}/manage/enterprise/showECChangeDescAjax.html?${_csrf.parameterName}=${_csrf.token}",
                dataType: "json", //指定服务器的数据返回类型,
                data: {
                    id: id
                },
                success: function (res) {
                    if (res.msg && res.msg == "fail") {
                    	showTipDialog("操作失败!");              	                         
                    }else{
                    	showTipDialog(res.msg );
                    }                       
                },
                error: function () {
                    showTipDialog("操作失败!");
                }
            });			
    }   
    </script>
</body>
</html>