<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品管理</title>
    <meta name="keywords" content="流量平台 产品管理" />
    <meta name="description" content="流量平台 产品管理" />

    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="../../assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="../../assets/css/base.min.css"/>
    <link rel="stylesheet" href="../../assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="../../assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <style>
        .icon-detail { color: #ca95db;background-image: url(../../assets/imgs/icon-detail.png); }
        .icon-edit { color: #5faee3;background-image: url(../../assets/imgs/icon-edit.png); }
        .icon-sync { color: #aec785;background-image: url(../../assets/imgs/icon-sync.png); }
        .form-control[readonly] {  background-color: #fff;  }
    </style>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

    <div class="main-container">
        <div class="module-header mt-20">
        <#if entManageFlag??&&entManageFlag!=1>
            <h3>详情
                	<a class="btn btn-sm btn-primary pull-right btn-icon icon-back" href="${contextPath}/sh/product/index.html">返回</a>
            </h3>
		</#if>
		<#if entManageFlag??&&entManageFlag==1>
            <h3>订购信息查询
            </h3>
		</#if>
        </div>

        <div class="tools row">
			<div class="col-sm-10 dataTables_filter text-left">
                <div class="form-inline">
                
                    <div class="form-group mr-10 form-group-sm">
                        <label>订购组名称：</label>
                        <input type="text" class="form-control searchItem" placeholder="" name="orderName"  id="orderName">
                    </div>
                    <#if entManageFlag??&&entManageFlag==1>
					<div class="form-group form-group-sm" id="search-time-range">
						<label>创建时间：</label>&nbsp
 						<input type="text" class="form-control search-startTime searchItem"
  							name="startTime"
							value="${(pageResult.queryObject.queryCriterias.beginTime)!}"
							id="startTime" placeholder="" readonly>~
 						<input type="text" class="form-control search-endTime searchItem" name="endTime"
                           	id="endTime"
                           	value="${(pageResult.queryObject.queryCriterias.endTime)!}"
                           	placeholder="" readonly>

                	</div>
                </#if>
                    <a class="btn btn-sm btn-warning" id="search-btn">确定</a>
                    <a onclick="createFile()" class="btn btn-sm btn-warning">报表导出</a>
                </div>
            </div>
        </div>
    <div class="tools row">
    <#if enterprise?exists&&entManageFlag??&&entManageFlag==1>
        <form>
            <div class="tile mt-30">
                <div class="tile-content">
                    <div class="row form">
                        <div class="form-group">
                            <label>企业名称：</label>
                            <span>${enterprise.name!}</span>
                        </div>
						<div class="form-group">
                            <label>企业编码：</label>
                            <span>${enterprise.code!}</span>
                        </div>
                    </div>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </#if>

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
    </div>

    <div class="modal fade dialog-lg" id="sync-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">同步信息</h5>
                </div>
                <div class="modal-body">
                    <div>集团产品号码：<span id="dialog_productCode"></span></div>
                    <div class="error-tip">查询同步结果如下，请确认无误后，点击提交</div>
                    <div class="table-responsive mt-10" role="table">
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="text-center">
                        <button class="btn btn-gray btn-sm dialog-btn" data-dismiss="modal">取 消</button>
                        <button class="btn btn-warning btn-sm dialog-btn" id="submit-btn">提 交</button>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
    
    
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
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>


    <!-- loading -->
    <div id="loadingToast" class="weui_loading_toast">
        <div class="weui_mask_transparent"></div>
        <div class="weui_toast">
            <div class="weui_loading">
                <div class="weui_loading_leaf weui_loading_leaf_0"></div>
                <div class="weui_loading_leaf weui_loading_leaf_1"></div>
                <div class="weui_loading_leaf weui_loading_leaf_2"></div>
                <div class="weui_loading_leaf weui_loading_leaf_3"></div>
                <div class="weui_loading_leaf weui_loading_leaf_4"></div>
                <div class="weui_loading_leaf weui_loading_leaf_5"></div>
                <div class="weui_loading_leaf weui_loading_leaf_6"></div>
                <div class="weui_loading_leaf weui_loading_leaf_7"></div>
                <div class="weui_loading_leaf weui_loading_leaf_8"></div>
                <div class="weui_loading_leaf weui_loading_leaf_9"></div>
                <div class="weui_loading_leaf weui_loading_leaf_10"></div>
                <div class="weui_loading_leaf weui_loading_leaf_11"></div>
            </div>
            <p class="weui_toast_content">数据加载中</p>
        </div>
    </div><!-- loading end -->



    <script src="../../assets/lib/require.js"></script>
    <script src="../../assets/js/config.js"></script>
    <script>

        function orderTypeFormat(value, column, row){
            if(value == '01'){
                return "全网订购";
            }else{
                return "本地订购";
            }
        };
        var accountFormat = function (value, column, row) {
        	if(row.orderType == '01') {
        		return "-";
        	} else if(value != null){
        	    return "<span>" + value / 100. + "元</span>";
        	}else{
        		return "<span>0元</span>";
        	}
        };

        var action = "${contextPath}/sh/product/orderList.html?${_csrf.parameterName}=${_csrf.token}&entId=${enterprise.id}";
        var columns = [
            {name: "orderName", text: "订购组名称"},
            {name: "orderType", text: "订购组类型", format: orderTypeFormat},
            {name: "count", text: "订购组余额", format: accountFormat},
            {name: "alertCount", text: "预警值", format: accountFormat},
            {name: "stopCount", text: "暂停值", format: accountFormat},
            {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
            {name: "op", text: "操作", format: function(value, column, row){
            	if(${entManageFlag} == 1
            			|| row.orderType == "01"){
            		return "<a href='${contextPath}/sh/product/orderListInfo.html?orderId=" + row.id + "'>详情</a>";
            	}else{
            		return ["<a href='${contextPath}/sh/product/orderListInfo.html?orderId=" + row.id + "'>详情</a>",
                    		"<a href='${contextPath}/sh/product/orderListEdit.html?orderId=" + row.id + "&entId=${enterprise.id}' class='btn-icon icon-sync mr-5 sync-btn'>编辑</a>"];
            	}
            }}
        ];
        require(["page/list","common", "bootstrap", "daterangepicker"], function() {
        	if(${entManageFlag} == 1){
        		initSearchDateRangePicker();
        	}
        });
        function createFile() {
            var orderName = document.getElementById('orderName').value;
            orderName = orderName.replace("%", "%25");
            var startTime = '';
            var endTime = '';
        	if(${entManageFlag} == 1){
                var startTime = document.getElementById('startTime').value;
                var endTime = document.getElementById('endTime').value;
        	}
            window.open(
                    "${contextPath}/sh/product/creatOrderNameCSVfile.html?orderName=" + orderName + "&entId=${enterprise.id}"
                    + "&&startTime=" + startTime + "&&endTime="
                    + endTime);
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
    </script>
</body>
</html>