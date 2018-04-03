<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-平台产品</title>
    <meta name="keywords" content="流量平台 查看平台产品"/>
    <meta name="description" content="流量平台 查看平台产品"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .form-group label {
            width: 180px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 186px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }
    </style>
</head>
<body>

<div class="main-container">


    <div class="module-header mt-30 mb-20">
        <h3>查看平台产品
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="${contextPath}/manage/platformProduct/index.html?back=1">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-content">
            <div class="row form">
                <input type="hidden" id="platformProductId" name="platformProductId" value="${product.id!}"/>
                
                <div class="col-md-6">
                    <div class="form-group">
                        <label>平台产品名称：</label>
                        <span>${product.name!}</span>
                    </div>
                    <div class="form-group">
                        <label>平台产品编码：</label>
                        <span>${product.productCode!}</span>
                    </div>
                    <div class="form-group">
                        <label>售出价格：</label>
                            <span>${(product.price/100.0)?string('0.00')}元<span>
                    </div>
                    <div class="form-group">
                        <label>状态：</label>
                        <span>
                        	<#if product.status==0>
                        		下架
                        	</#if>
                        	<#if product.status==1>
                        		上架
                        	</#if>
                        <span>
                    </div>
                    <div class="form-group">
                        <label>说明：</label>
                        <span style="word-break: break-all;">
                        	${product.illustration!}
                        <span>
                    </div>
                                        
                    <div class="form-group">
                        <label>产品分类：</label>
                        <div class="btn-group btn-group-sm">
                            <span>${product.productCustomerTypeName!}</span>
                        </div>
                    </div>                    
                    
                    <#if ziying!="true">
                        <div class="form-group">
                            <label>是否默认关联：</label>
                            <#if product.defaultvalue??&&product.defaultvalue==0>
                                <span>否</span>
                            </#if>
                            <#if product.defaultvalue??&&product.defaultvalue==1>
                                <span>是</span>
                            </#if>
                        </div>
                    </#if>
                    
                </div>
                
                <div class="col-md-6">  
                    <div class="form-group">
                        <label>产品大小：</label>
                        <#if product.type?? && product.type==4>
                        	<span>${product.productSize!}个</span>
		                <#else>
		                	<#if (product.productSize<1024)>
                        		<span>${(product.productSize)?string('#.##')}KB</span>
                    		</#if>
		                    <#if (product.productSize>=1024) && (product.productSize<1024*1024)>
		                        <span>${(product.productSize/1024.0)?string('#.##')}MB</span>
		                    </#if>
		                    <#if (product.productSize>=1024*1024)>
		                        <span>${(product.productSize/1024.0/1024.0)?string('#.##')}GB</span>
		                    </#if>
                        </#if>
                    </div>

                    <div class="form-group">
                        <label>运营商：</label>
                        <div class="btn-group btn-group-sm">
                        <#if product.isp=="M">
                            <span>移动</span>
                        </#if>
                        <#if product.isp=="U">
                            <span>联通</span>
                        </#if>
                        <#if product.isp=="T">
                            <span>电信</span>
                        </#if>
                        <#if product.isp=="A">
                            <span>三网</span>
                        </#if>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label>使用范围：</label>
                        <span>${product.ownershipRegion!}</span>
                    </div>
                    <div class="form-group">
                        <label>漫游范围：</label>
                        <span>${product.roamingRegion!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>创建时间：</label>
                        <span>${(product.createTime?datetime)!}</span>
                    </div>
                    
                    <div class="form-group">
                        <label>更新时间：</label>
                        <span>${(product.updateTime?datetime)!}</span>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <div class="mt-30">
        <h5 class="h5">已关联的boss产品</h5>
        <#if ziying=="true">
	        <div class="form-group form-inline">
		        <label style="font-size: 14px;">优先选择：</label>
		        <select id="choice" name="choice" class="form-control">
		           <option value="0" <#if priorSupplier?? && priorSupplier.id == 0>selected</#if>>价格优先</option>
		              <#if suppliers?? && (suppliers?size) gt 0>	                  
		                  <#list suppliers as item>
		                      <option value="${item.id}" <#if priorSupplier?? && item.id == priorSupplier.id>selected</#if>>${item.name!}</option>
		                  </#list>
		              </#if>
		        </select>
		        <label class="h5">(注释：当选择某供应商后，充值时优先走该渠道)</label>
	        </div>
        </#if>
        <form>
            <input type="text" class="form-control searchItem1 hidden" id="platformProductId" name="platformProductId"
                   value="${product.id!}" autocomplete="off" maxlength="255"/>
            <a class="btn btn-warning hidden" id="search-btn1">确定</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        <div class="tile mt-30">
            <!--放入列表-->
            <div id="listData1"></div>
        </div>
    </div>

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

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var opFormat1 = function (value, cell, row) {
        return ['<a href="${contextPath}/manage/supplierProduct/showDetail.html?id=' + row.id + '">详情</a>'];
    };
    var ispFormat = function (value, column, row) {
        if (row.isp == "M") {
            return "移动";
        }
        if (row.isp == "A") {
            return "三网";
        }
        if (row.isp == "U") {
            return "联通";
        }
        if (row.isp == "T") {
            return "电信";
        }
        return "-";
    };
    var sizeFormat = function (value, column, row) {
    	if(row.productType==null){
    		return "-";
    	}
    	if(row.productType!=4){
    		if (row.size == null) {
            	return "-";
        	}
	        if (row.size < 1024) {
	            return row.size + "KB";
	        }
	        if (row.size >= 1024 && row.size < 1024 * 1024) {
	            return (row.size * 1.0 / 1024).toFixed(2) + "MB";
	        }
	        if (row.size >= 1024 * 1024) {
	            return (row.size * 1.0 / 1024 / 1024).toFixed(2) + "GB"
	        }
        	return row.size + "kB";
    	}else{
			return row.size + "个";
		}
        
    };
    
    var statusFormat = function (value, column, row){
        if(row.supplierStatus == 0){
            return "下架";
        }else if(row.supplierStatus == 1){
            if(row.status==0){
                return "下架";
            }
            if(row.status==1){
                return "上架";
            }
        }
        return "下架";
    }

    var priceFormat = function (value, column, row) {
        if (row.price == null) {
            return "-";
        }
        return (row.price / 100.0).toFixed(2) + "元";
    };

    var action1 = "${contextPath}/manage/platformProduct/getRelation.html?${_csrf.parameterName}=${_csrf.token}";

    var columns1 = [{name: "supplierName", text: "供应商"},
        {name: "isp", text: "运营商", format: ispFormat},
        {name: "name", text: "产品名称"},
        {name: "code", text: "产品编码"},
        {name: "size", text: "产品大小", format: sizeFormat},
        {name: "price", text: "采购价格", format: priceFormat},
        {name: "status", text: "状态", format: statusFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {name: "", text: "操作", format: opFormat1}];

    require(["react", "react-dom", "page/listDate","common", "bootstrap"], function (React, ReactDOM, ListData) {

        ReactDOM.render(React.createElement(ListData, {
            columns: columns1,
            searchClass: "searchItem1",
            searchBtn: $("#search-btn1")[0],
            action: action1
        }), $("#listData1")[0]);
        
        listeners();
    });
    
    function listeners(){
    	$('#choice').on('change',function(){
    		var supplierId = $(this).val();
    		var prdId = ${product.id!};
    		$.ajax({
                type: 'post',
                data: {
                	supplierId:supplierId,
                	prdId:prdId
                },
                url: "${contextPath}/manage/platformProduct/selectSupplierAjax.html?${_csrf.parameterName}=${_csrf.token}",
                dataType: 'json',
                success: function (data) {
                    if (data && data.msg === "success") {
                    	showTipDialog("选择成功！");
                    } else {
                    	showTipDialog(data.msg);
                    }
                },
                error: function () {
                	showTipDialog("网络错误！");
                }
            });
    	})
    }

</script>

</body>
</html>