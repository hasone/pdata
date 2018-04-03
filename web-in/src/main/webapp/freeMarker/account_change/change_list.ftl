<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-企业充值</title>
    <meta name="keywords" content="流量平台 企业充值"/>
    <meta name="description" content="流量平台 企业充值"/>



    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

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

        .search-separator {
            display: none;
        }

        @media (max-width: 1300px) {
            .search-separator {
                display: block;
            }
        }
        <#--
		.table tbody > tr > td:last-child, .table tbody > tr > td:last-child *, .table thead > tr > th:last-child{
            overflow: visible;
        }

        .table > thead > tr > th {
            line-height: initial;
        }

        .table > thead > tr > th, .table > tbody > tr > td {
            vertical-align: middle;
        }
        -->

        .table tbody > tr > td, .table tbody > tr > td *, .table thead > tr > th {

        }

        .table tbody > tr > td:last-child {
            max-width: 100%;
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
        <h3>企业充值</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">
            	
           		<div class="form-group mr-10 form-group-sm">
                    <label for="productType">账户类型：</label>					
					<select name="productType" id="productType" class="form-control searchItem">
						<option value="" >全部</option>
						<option value="0" <#if productType ?? && 0 == productType>selected</#if>>现金产品</option>
						<option value="1" <#if productType ?? && 1 == productType>selected</#if>>流量池产品</option>
						<option value="5" <#if productType ?? && 5 == productType>selected</#if>>预付费产品</option>
						
					</select>
                </div>
                          
                <div class="form-group mr-10 form-group-sm">
                    <label for="name">企业名称：</label>
                    <input type="hidden" name="name" class="form-control searchItem enterprise_autoComplete" id="name" placeholder="">
                </div>

				<#if isShowPhone?? && isShowPhone == true>
                    <div class="form-group mr-10 form-group-sm">
                    <label for="code">集团编码：</label>
                    <input type="text" name="phone" class="form-control searchItem" id="phone" placeholder="">
                </div>
                <#else>
                <div class="form-group mr-10 form-group-sm">
                    <label for="code">企业编码：</label>
                    <input type="text" name="code" class="form-control searchItem" id="code" placeholder="">
                </div>
                </#if>

                <span class="search-separator mt-10"></span>
                <div class="form-group form-group-sm" id="search-time-range">
                    <label>创建时间：</label>
                    <input type="text" class="form-control search-startTime searchItem" name="startTime" id="startTime"
                           placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                           placeholder="">
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


<div class="modal fade dialog-sm" id="remove-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">删除成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="offline-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">下架成功</span>
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
    var flag = ${flag}
    var statusFormat = function (value, column, row) {
        return value == 1 ? "上架" : "下架";
    };

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }

    var moneyFormator = function (value, column, row) {
    	if(1 == row.productType){
    		value = value / 1024.0
    		return value + "MB"
    	}else{
    		value = value / 100.0;
    		return value.toFixed(2) + "元";
    	}       
    }

    var opFormat = function (value, column, row) {
        var submitApprovalAuth = "${submitApprovalAuth}";
        if (submitApprovalAuth == "true") {
            if (flag == 1) {
            	if(row.productType == 5){
            		 return ["<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/editAlert.html?entId=" + row.id + "&prdId=" + row.productId +  "'>设置预警</a>"];
            	}
                return ["<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/change.html?entId=" + row.id + "&prdId=" + row.productId +  "&approvalType=2'>充值</a>",
                    "<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/submitIndex.html?entId=" + row.id + "&prdId=" + row.productId +  "&approvalType=2'>提交审批</a>",
                    <#if provinceFlag ?? && provinceFlag == "hun">
                    "<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/change.html?entId=" + row.id + "&prdId=" + row.productId +  "&approvalType=11'>设置暂停</a>"                
                    <#else>
                    "<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/editAlert.html?entId=" + row.id + "&prdId=" + row.productId +  "'>设置预警</a>"
                    </#if>];
                    
                <#-- "<a class='btn-icon icon-edit mr-5' href='${contextPath}
                /manage/accountChange/accountSyncronizing.html?id=" + row.id + "'>同步账户余额</a>"-->
            }
            else {
                if(row.productType == 5){             
            		 return [
            		       <#if provinceFlag ?? && provinceFlag == "hun">
                 			"<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/change.html?entId=" + row.id + "&prdId=" + row.productId +  "&approvalType=11'>设置暂停</a>"               
                 			<#else>
            		 		"<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/editAlert.html?entId=" + row.id + "&prdId=" + row.productId +  "'>设置预警</a>"
                 			</#if>];
            	}
                return ["<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/submitIndex.html?entId=" + row.id +  "&prdId=" + row.productId + "'>提交审批</a>"];
            }

        } else {
            if (flag == 1) {
                return ["<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/change.html?entId=" + row.id + "&prdId=" + row.productId + "'>充值</a>",
                 <#if provinceFlag ?? && provinceFlag == "hun">
                 "<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/change.html?entId=" + row.id + "&prdId=" + row.productId +  "&approvalType=11'>设置暂停</a>"                
                 <#else>
                "<a class='btn-icon icon-down mr-5' href='${contextPath}/manage/accountChange/editAlert.html?entId=" + row.id + "&prdId=" + row.productId + "'>设置预警</a>"
                 </#if>];
                <#--"<a class='btn-icon icon-edit mr-5' href='${contextPath}
                /manage/accountChange/accountSyncronizing.html?id=" + row.id + "'>同步账户余额</a>"-->
            }
        }
    };
    var action = "${contextPath}/manage/accountChange/search.html?${_csrf.parameterName}=${_csrf.token}";

    var columns = [
    	{name: "name", text: "企业名称", tip: true},
    	{name: "code", text: "企业编码", tip: true}
    ];
               
    <#if isShowPhone?? && isShowPhone == true>
       columns.push({name: "phone", text: "集团编码", tip: true});		
    </#if>
    
	columns.push({name: "productName", text: "账户名称", tip: true});
	columns.push({name: "productTypeName", text: "账户类型", tip: true});
	columns.push({name: "currencyCount", text: "企业余额", format: moneyFormator});
	<#if provinceFlag ?? && provinceFlag != "hun">
		columns.push({name: "alertCount", text: "预警值", format: moneyFormator});
	</#if>
	columns.push({name: "stopCount", text: "暂停值", format: moneyFormator});
	columns.push({name: "cmManagerName", text: "所属地区", tip: true});
	columns.push({name: "createTime", text: "创建时间", format: "DateTimeFormat"});           
        
   	<#if isShowOperator?? && (submitApprovalAuth?? && submitApprovalAuth == "true"  || flag?? && flag != '0')>
   		columns.push({name: "op", text: "操作", format: opFormat});
   	</#if>     

    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

    require(["common", "bootstrap", "daterangepicker", "page/list"], function () {
        initSearchDateRangePicker();
        
        $("#productType").on("change", function(){
        	$("#search-btn").click();
        });
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
</script>
</body>
</html>
