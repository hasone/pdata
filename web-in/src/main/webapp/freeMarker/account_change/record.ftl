<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-充值记录</title>
    <meta name="keywords" content="流量平台 充值记录"/>
    <meta name="description" content="流量平台 充值记录"/>

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

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>
        	<#if approvalType?? && approvalType == 2>
        		充值记录
        	<#elseif approvalType?? && approvalType == 9>
        		企业最小额度变更记录
        	<#elseif approvalType?? && approvalType == 10>
        		企业预警值变更记录
        	<#elseif approvalType?? && approvalType == 11>
        		企业暂停值变更记录
        	</#if>      
        </h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <div class="form-inline">
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
                    <label>
                    <#if approvalType?? && approvalType == 2>
		        		充值时间：
		        	<#elseif approvalType?? && approvalType == 9>
		        		最小额度修改时间：
		        	<#elseif approvalType?? && approvalType == 10>
		        		预警值修改时间
		        	<#elseif approvalType?? && approvalType == 11>
		        		暂停值修改时间
		        	</#if>                    
                    </label>&nbsp;
                    <input type="text" class="form-control search-startTime searchItem" name="startTime" id="startTime"
                           placeholder="">~
                    <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime"
                           placeholder="">
                </div>
                <a type="submit" class="btn btn-sm btn-warning" id="search-btn">确定</a>
                <a onclick="createFile()" class="btn btn-sm btn-warning">报表导出</a>
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
	var approvalType = "${approvalType!}";
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
            return value + "MB"
        }else{
            return value.toFixed(2) + "元";
        }       
    }

    var opFormat = function (value, column, row) {
    	if(row.productType == 5){
    		return "";
    	}
        return ["<a class='btn-icon icon-detail mr-5' href='${contextPath}/manage/accountChange/recordDetial.html?requestId=" + row.id + "&approvalType=" + approvalType + "'>详情</a>"];
    };
    var discountTypeFormator = function (value, column, row) {
        if(1 == row.discountType){
            return "存送比例"
        }else{
            return "无";
        } 
    };
    var discountValueFormator = function (value, column, row) {
        if(1 == row.discountType){
            return value + "%"
        }else{
            return "无";
        } 
    };
    var productTypeFormator = function (value, column, row) {
        if(0 == row.productType){
            return "现金产品"
        } else if(1 == row.productType){
            return "流量池产品";
        } else if(2 == row.productType){
            return "流量包产品";
        } else if(3 == row.productType){
            return "话费产品";
        } else if(4 == row.productType){
            return "虚拟币产品";
        } else if(5 == row.productType){
            return "预付费资金产品";
        }
    };
    
   var descriptionFormat = function (value, column, row) {
        if(row.productType == 5){
        	return "上游同步完成";
        }else{
        	return row.description;
        }
    };
    

    var managerId = "${managerId}";
    var adminId = "${adminId}";
    var action = "${contextPath}/manage/accountChange/searchRecord.html?${_csrf.parameterName}=${_csrf.token}&managerId=" + managerId + "&adminId=" + adminId + "&approvalType="+approvalType;
	
    var columns = [];
    <#if approvalType?? && approvalType == 2>
    columns.push({name: "createTime", text: "充值时间", format: "DateTimeFormat"});
    <#elseif approvalType?? && approvalType == 9>
    columns.push({name: "createTime", text: "最小额度修改时间", format: "DateTimeFormat"});
    <#elseif approvalType?? && approvalType == 10>
    columns.push({name: "createTime", text: "预警值修改时间", format: "DateTimeFormat"});
    <#elseif approvalType?? && approvalType == 11>
    columns.push({name: "createTime", text: "暂停值修改时间", format: "DateTimeFormat"});
    </#if>    
     <#if provinceFlag?? && provinceFlag == "sd">
    	columns.push({name: "productTypeName", text: "账户名称", tip: true});
 	 </#if>
 	     
    <#if provinceFlag?? && provinceFlag == "gansu">
    	columns.push({name: "discountType", text: "优惠类型", tip: true,format: discountTypeFormator});
    	columns.push({name: "discountValue", text: "优惠值", tip: true,format: discountValueFormator});
    	columns.push({name: "productType", text: "账户名称", tip: true,format: productTypeFormator});
 	</#if>
    <#if approvalType?? && approvalType == 2>
		columns.push({name: "count", text: "充值额度",format: moneyFormator});
    <#elseif approvalType?? && approvalType == 9>
		columns.push({name: "count", text: "最小额度",format: moneyFormator});
    <#elseif approvalType?? && approvalType == 10>
		columns.push({name: "count", text: "预警值",format: moneyFormator});
    <#elseif approvalType?? && approvalType == 11>
		columns.push({name: "count", text: "暂停值",format: moneyFormator});
    </#if>
    columns.push({name: "entName", text: "企业名称", tip: true});

        	
    <#if isShowPhone?? && isShowPhone == true>
       columns.push({name: "entPhone", text: "集团编码", tip: true});
    <#else>
		columns.push({name: "entCode", text: "企业编码", tip: true});
    </#if>
    
	columns.push({name: "districtName", text: "所属地区"});
	columns.push({name: "description", text: "状态", format : descriptionFormat});
	columns.push({name: "op", text: "操作", format: opFormat});
    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

    require(["common", "bootstrap", "daterangepicker", "page/list"], function () {
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
    
     function createFile() {

        var name = document.getElementById('name').value;
        name = name.replace("%", "%25");
        var phone='';
        <#if isShowPhone?? && isShowPhone == true>
        	phone = document.getElementById('phone').value;
        	phone = phone.replace("%", "%25");
        <#else>
    		phone = document.getElementById('code').value;
    		phone = phone.replace("%", "%25");
		</#if>

        var startTime = document.getElementById('startTime').value;
        var endTime = document.getElementById('endTime').value;
        <#if isShowPhone?? && isShowPhone == true>
        	window.open(
                "${contextPath}/manage/accountChange/exportSdChargeRecords.html?managerId=" + managerId + "&&adminId=" + adminId + "&&name=" + name
                + "&&phone=" + phone + "&&startTime=" + startTime + "&&endTime="
                + endTime);
   		<#else>
        	window.open(
                "${contextPath}/manage/accountChange/exportChargeRecords.html?managerId=" + managerId + "&&adminId=" + adminId + "&&name=" + name
                + "&&phone=" + phone + "&&startTime=" + startTime + "&&endTime="
                + endTime);
		</#if>
    }
</script>
</body>
</html>