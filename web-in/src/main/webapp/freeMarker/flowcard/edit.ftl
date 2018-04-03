<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-编辑流量券</title>
    <meta name="keywords" content="流量平台 编辑流量券"/>
    <meta name="description" content="流量平台 编辑流量券"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>

        .form input[type='text'], .form input[type='number'], .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form-group > label {
            width: 82px;
            text-align: right;
        }

        .error-tip {
            display: block;
            padding-left: 86px;
        }

        .error-tip-flowaccount {
            display: block;
            color: red;
        }

        table .btn-sm {
            padding: 2px 10px !important;
        }

        th, td {
            vertical-align: middle !important;
        }

        .table-responsive {
            max-height: 200px;
            overflow: auto;
        }

        @media (min-width: 768px){
            .modal-dialog {
                width: 800px;
            }
        }
    </style>

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑流量券
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/flowcard/index.html'">返回</a>
        </h3>
    </div>

    <form id="dataForm" class="form"
          action="${contextPath}/manage/flowcard/preView.html?${_csrf.parameterName}=${_csrf.token}"
          method="post"
          enctype="multipart/form-data">
        <div class="tile mt-30">
            <div class="tile-header">
                活动信息
            </div>
            <div class="tile-content">
                <div class="row form">

                    <div class="col-md-6">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <input type="hidden" class="form-control" name="cmProductId"
                               id="cmProductId"
                               value="${cmProductId!}">
                        <input type="hidden" class="form-control" name="cuProductId"
                               id="cuProductId"
                               value="${cuProductId!}">
                        <input type="hidden" class="form-control" name="ctProductId"
                               id="ctProductId"
                               value="${ctProductId!}">

                        <input type="hidden" class="form-control" name="correctMobiles"
                               id="correctMobiles">
                        <input type="hidden" class="form-control" name="invalidMobiles"
                               id="invalidMobiles">

                        <input type="hidden" class="form-control" name="cmMobileList"
                               id="cmMobileList">
                        <input type="hidden" class="form-control" name="cuMobileList"
                               id="cuMobileList">
                        <input type="hidden" class="form-control" name="ctMobileList"
                               id="ctMobileList">

                        <input type="hidden" class="form-control" name="flowSize" id="flowSize">
                        <input type="hidden" class="form-control" name="productType"
                               id="productType">

                        <input type="hidden" class="form-control" name="cmUserSet" id="cmUserSet">
                        <input type="hidden" class="form-control" name="cuUserSet" id="cuUserSet">
                        <input type="hidden" class="form-control" name="ctUserSet" id="ctUserSet">

                        <input type="hidden" class="form-control" name="activityId" id="activityId"
                               value="${activities.activityId!}">

                        <div class="form-group">
                            <label style="font-weight:700 ">企业名称：</label>
                            <div class="btn-group btn-group-sm enterprise-select">
                                <input style="width: 0; font-size: 0; height: 0; padding: 0; opacity: 0"
                                       class="form-control searchItem"
                                       name="entId" id="entId" value="${enterprise.id!}" required>
                                <button type="button" class="btn btn-default" style="width: 299px">
                                    &nbsp;</button>
                                <button type="button" class="btn btn-default dropdown-toggle"
                                        data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                <#if enterpriseList?? && enterpriseList?size!=0>
                                    <#list enterpriseList as e>
                                        <li data-value="${e.id!}"><a href="#">${e.name!}</a></li>
                                    </#list>
                                <#else>
                                    <li data-value=""><a href="#">没有可选的企业</a></li>
                                </#if>
                                </ul>
                            </div>
                        </div>
					<#if showOrderList??&&showOrderList==1>
						<div class="form-group">
                            <label style="font-weight:700 ">订购组：</label>

                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; font-size: 0; height: 0; padding: 0; opacity: 0"
                                       class="form-control searchItem"
                                       name="orderId" id="orderId" value="">
                                <button type="button" class="btn btn-default" style="width: 299px">
                                    &nbsp;</button>
                                <button type="button" class="btn btn-default dropdown-toggle"
                                        data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                <#if orderList?? && orderList?size!=0>
                                    <#list orderList as e>
                                        <li data-value="${e.id!}"><a href="#">${e.orderName!}</a></li>
                                    </#list>
                                <#else>
                                    <li data-value=""><a href="#">没有可选的订购组</a></li>
                                </#if>
                                </ul>
                            </div>
                        </div>
					</#if>
                        <div class="form-group">
                            <label>活动名称：</label>
                            <input type="text" name="name" id="name" value="${activities.name!}"
                                   required>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="form-group" style="height: 32px; line-height: 32px;">
                            <label>企业余额：</label>
                            <span id="balance"></span>
                        </div>

                        <div class="form-group">
                            <label>活动时间：</label>
                                <span id="activeTime" class="daterange-wrap">
                                    <input type="text" name="startTime" id="startTime"
                                           value="${(activities.startTime?date)!}"
                                           readonly="readonly"
                                           style="width: 152px;">&nbsp;至
                                    <input type="text" id="endTime" name="endTime"
                                           value="${(activities.endTime?date)!}"
                                           readonly="readonly" required style="width: 152px;">
                                    <label class="error error-tip" id="time_tip"></label>
                                </span>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <div class="tile mt-30">
            <div class="tile-header">
                产品信息
            </div>
            <div class="tile-content">
                <div class="">
                    <div class="table-responsive mt-20">
                        <table class="table table-bordered text-center award-table">
                            <thead>
                            <tr>
                                <th width="10%">选择产品</th>
                                <th width="20%">产品名称</th>
                                <th width="20%">产品编码</th>
                                <th width="10%">产品大小</th>
                                <th width="10%">售出价格(元)</th>
                                <th width="15%">使用范围</th>
                                <th width="15%">漫游范围</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                    <a class="btn btn-sm btn-info openProductsDialog-btn"
                                       data-type="M">移动</a>
                                </td>
                                <td>
                                    <span class="cmProdName" name="cmProdName" id="cmProdName"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="cmProdCode" name="cmProdCode" id="cmProdCode"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <div id="packageAccount">
	                                    <span class="cmProdSize" name="cmProdSize" id="cmProdSize"
                                              style="width:80px;"></span>
                                    </div>
                                    <div id="flowAccount" style="display:none">
                                        <input class="cmProdSize" name="cmProdSize_flowaccount"
                                               id="cmProdSize_flowaccount"
                                               style="width:80px;">MB
                                    </div>
                                </td>
                                <td>
                                    <span class="cmProdPrice" name="cmProdPrice" id="cmProdPrice"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="cmOwnership" name="cmOwnership" id="cmOwnership"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="cmRoaming" name="cmRoaming" id="cmRoaming"
                                          style="width:80px;"></span>
                                </td>
                            </tr>

                            <#if allowAllPlatformProduct?? && allowAllPlatformProduct>
                            <tr>
                                <td>
                                    <a class="btn btn-sm btn-info openProductsDialog-btn"
                                       data-type="U">联通</a>
                                </td>
                                <td>
                                    <span class="cuProdName" name="cuProdName" id="cuProdName"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="cuProdCode" name="cuProdCode" id="cuProdCode"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="cuProdSize" name="cuProdSize" id="cuProdSize"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="cuProdPrice" name="cuProdPrice" id="cuProdPrice"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="cuOwnership" name="cuOwnership" id="cuOwnership"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="cuRoaming" name="cuRoaming" id="cuRoaming"
                                          style="width:80px;"></span>
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <a class="btn btn-sm btn-info openProductsDialog-btn"
                                       data-type="T">电信</a>
                                </td>
                                <td>
                                    <span class="ctProdName" name="ctProdName" id="ctProdName"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="ctProdCode" name="ctProdCode" id="ctProdCode"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="ctProdSize" name="ctProdSize" id="ctProdSize"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="ctProdPrice" name="ctProdPrice" id="ctProdPrice"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="ctOwnership" name="ctOwnership" id="ctOwnership"
                                          style="width:80px;"></span>
                                </td>
                                <td>
                                    <span class="ctRoaming" name="ctRoaming" id="ctRoaming"
                                          style="width:80px;"></span>
                                </td>
                            </tr>
                            </#if>
                            </tbody>
                        </table>
                        <span class="error-tip" id="award-error"></span>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <div class="tile mt-30">
        <div class="tile-header">
            赠送手机号
        </div>
        <div class="tile-content">
            <div class="form-group">
                <label>赠送手机号:</label>
                    <span class="file-box">
                        <a class='btn-icon icon-detail mr-5'
                           href='${contextPath}/manage/flowcard/downloadPhones.html?activitiesId=${activities.activityId!}'>下载原手机号</a>
                    </span>
                <input type="hidden" id="phones" name="phones" value="" readOnly="true">
                &nbsp;&nbsp;
                    <span class="file-box">
                        <input type="file" name="file" id="file" class="file-helper">
                        <a>重新上传</a>
                    </span>
                <span>上传txt格式文件，每行一个手机号码，号码换行操作</span>
                <p class="red text-left" style="padding-left: 115px;" id="modal_error_msg"></p>
            </div>
        </div>
    </div>

    <div class="mt-30 text-center">
        <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn save-btn"
           id="save-btn">保 存</a>
        <span style='color:red' id="error_msg">${errorMsg!}</span>
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


<div class="modal fade dialog-lg" id="products-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-center">产品列表</h5>
            </div>
            <div class="modal-body" style="padding: 40px 10px;">
                <input type="hidden" class="searchItem" name="type" id="ispType">
                <input type="hidden" class="searchItem" name="enterId" id="enterId">
                <a id="search-btn" hidden></a>
                <div role="table"></div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="sure-dialog" data-backdrop="static">
    <div class="modal-dialog" style="width: 300px !important;">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="tips"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="sure-btn">确 定</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>

    var productmap = {};
    var selectedProductFormat = function (value, cloumn, row) {
        productmap[row.id] = row;
        return "<a href='javascript:void(0)' class='btn btn-sm btn-success' onClick='selectedProduct(" + row.id + ")'>选择</a>";
    };

    /**
     * 清空奖品信息
     * */
    function emptyAwardProducts() {

        $("#cmProductId").empty();
        $("#ctProductId").empty();
        $("#cuProductId").empty();

        $("#cmProdName").empty();
        $("#cmProdCode").empty();
        $("#cmProdSize").empty();
        $("#cmProdPrice").empty();
        $("#cmOwnership").empty();
        $("#cmRoaming").empty();

        $("#ctProdName").empty();
        $("#ctProdCode").empty();
        $("#ctProdSize").empty();
        $("#ctProdPrice").empty();
        $("#ctOwnership").empty();
        $("#ctRoaming").empty();

        $("#cuProdName").empty();
        $("#cuProdCode").empty();
        $("#cuProdSize").empty();
        $("#cuProdPrice").empty();
        $("#cuOwnership").empty();
        $("#cuRoaming").empty();
    }

    /**
     * 渲染选中的产品信息
     */
    function selectedProduct(id) {
        var product = productmap[id];
        var type = $("#ispType").val();
        var tr = $("a[data-type='" + type + "']").parent().parent();

        $("#products-dialog").modal("hide");
        //渲染对应的运营商数据
        if (type == "M") {
            $("#cmProductId").val(product.id);

            $("#cmProdName").html(product.name);
            $("#cmProdCode").html(product.productCode);

            //记录下产品类型
            $("#productType").val(product.type);
            //流量池产品，设置为输入框
            if (product.type == '1') {
                $("#flowAccount").show();
                $("#packageAccount").hide();

                $("#cmProdPrice").html('-');
            } else {
                $("#flowAccount").hide();
                $("#packageAccount").show();
                var size = sizefun(product.productSize);
                $("#cmProdSize").html(size);

                var price = pricefun(product.price);
                $("#cmProdPrice").html(price);
            }

            $("#cmOwnership").html(product.ownershipRegion);
            $("#cmRoaming").html(product.roamingRegion);
        }

        if (type == "T") {
            $("#ctProductId").val(product.id);

            $("#ctProdName").html(product.name);
            $("#ctProdCode").html(product.productCode);
            var size = sizefun(product.productSize);
            $("#ctProdSize").html(size);
            var price = pricefun(product.price);
            $("#ctProdPrice").html(price);
            $("#ctOwnership").html(product.ownershipRegion);
            $("#ctRoaming").html(product.roamingRegion);
        }

        if (type == "U") {
            $("#cuProductId").val(product.id);

            $("#cuProdName").html(product.name);
            $("#cuProdCode").html(product.productCode);
            var size = sizefun(product.productSize);
            $("#cuProdSize").html(size);
            var price = pricefun(product.price);
            $("#cuProdPrice").html(price);
            $("#cuOwnership").html(product.ownershipRegion);
            $("#cuRoaming").html(product.roamingRegion);
        }

        //更新活动规模
        updateActiveInfo();
    }

    /**
     * 更新活动规模
     */
    function updateActiveInfo() {

    }

    /**
     * 运营商
     * */
    function ispNamefun(isp) {
        if (isp == "M") {
            return "移动";
        }
        if (isp == "T") {
            return "电信";
        }
        if (isp == "U") {
            return "联通";
        }
    }
    ;
    var ispNameFormat = function (value, cloumn, row) {
        var isp = row.isp;
        if (isp == "M") {
            return "移动";
        }
        if (isp == "T") {
            return "电信";
        }
        if (isp == "U") {
            return "联通";
        }
    };

    /**
     * 价格
     * */
    function pricefun(price) {
        if (price != 0) {
            return (price / 100.00).toFixed(2) + "";
        }
        return "0.00";
    }
    ;
    var priceFormat = function (value, cloumn, row) {
        if (row.type == '1'
            || row.type == '3') {
            return "-";
        }
        var price = row.price;
        if (price != 0) {
            return (price / 100.00).toFixed(2) + "";
        }
        return "0.00";
    };
    /**
     * 流量包大小
     * */
    function sizefun(size) {
        if (size == null) {
            return "-";
        }
        if (size < 1024) {
            return size + "KB";
        }
        if (size >= 1024 && size < 1024 * 1024) {
            return (size * 1.0 / 1024) + "MB";
        }
        if (size >= 1024 * 1024) {
            return (size * 1.0 / 1024 / 1024) + "GB";
        }

        return size * 1.0 / 1024 + "MB";
    }
    ;
    var productSizeFormat = function (value, coloumn, row) {
        if (row.type == '1'
            || row.type == '3') {
            return "-";
        }
        if (row.productSize == null) {
            return "-";
        }
        if (row.productSize < 1024) {
            return row.productSize + "KB";
        }
        if (row.productSize >= 1024 && row.productSize < 1024 * 1024) {
            return (row.productSize * 1.0 / 1024) + "MB";
        }
        if (row.productSize >= 1024 * 1024) {
            return (row.productSize * 1.0 / 1024 / 1024) + "GB";
        }

        return row.productSize * 1.0 / 1024 + "MB";
    };

    var countFormat = function (value, coloumn, row) {
        if (row.type == 2) {
            return row.count;
        }
        else {
            return "-";
        }
    };
    var columns = [{name: "isp", text: "运营商", format: ispNameFormat},
        {name: "name", text: "产品名称", tip: true},
        {name: "productCode", text: "产品编码", tip: true},
        {name: "productSize", text: "流量包大小", format: productSizeFormat},
        {name: "price", text: "售出价格(元)", format: priceFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {name: "count", text: "数量", format: countFormat},
        {name: "op2", text: "操作", format: selectedProductFormat}
    ];

    var action = "${contextPath}/manage/flowcard/searchProduct.html?${_csrf.parameterName}=${_csrf.token}";

    require(["moment", "common", "bootstrap", "daterangepicker", "page/list", "upload"],
            function (mm) {
                initEnterpriseSelect();

                window.moment = mm;

                //初始化日期组件
                initDateRangePicker2();

                listeners();

                //文件上传
                fileListener();

                formValidate();
                
                $("#sure-btn").on("click", function () {
		        	var flag = $("#tips").val();
		        	if(flag && flag=="true"){
		        		window.location.href = "${contextPath}/manage/flowcard/index.html";
		        	}else{
		        		$("#save-btn").removeClass("disabled");
		        		$("#sure-dialog").modal("hide");
		        	}
		        });

                //保存 startEle.val() &&
                // moment(startEle.val()).isBefore(today) ||
                $("#save-btn").on("click", function () {
                    var startEle = $('#startTime');
                    var endEle = $('#endTime');
                    var today = moment();

                    if (endEle.val()) {
                        if (moment(endEle.val() + " 23:59:59").isBefore(today)) {
                            var validator = $("#dataForm").validate();
                            if (validator.check(endEle[0])) {
                                var err = validator.errorsFor(endEle[0]);
                                if (err && err.length) {
                                    err.show().html("请选择正确的时间!");
                                } else {
                                    $("#activeTime").parent()
                                            .append('<span id="endTime-error" class="error error-tip">请选择正确的时间!</span>');
                                    endEle.attr("aria-describedby", "endTime-error");
                                }
                            }
                            return;
                        }
                    }

                    if ($("#dataForm").validate().form()) {
                        showToast();

                        //判断上传手机号和产品是否相符合
                        var cmMobileList = $("#cmMobileList").val();
                        var cuMobileList = $("#cuMobileList").val();
                        var ctMobileList = $("#ctMobileList").val();

                        var cmProdId = $("#cmProductId").val();
                        var cuProdId = $("#cuProductId").val();
                        var ctProdId = $("#ctProductId").val();

                        //流量池传递产品大小
                        var flowSize = $("#cmProdSize_flowaccount").val();
                        $("#flowSize").val(flowSize);
                        
                        //待传参数
                        var formData = {};
                        
                        var activity = {
		                	id: "${activities.id!}", 
		                	activityId: "${activities.activityId!}",
		                	name: $("input[name='name']").val(),
		                	entId: $("#entId").val(),
		                	startTime: $("input[name='startTime']").val(),
		                	endTime: $("input[name='endTime']").val()
		            	};

                        if (cmMobileList == '' && cuMobileList == '' && ctMobileList == '') {
                            //没有更新手机号
                            //$("#dataForm").submit();
                            
                            var prdSize = $("#flowSize").val();
				        	if(prdSize && prdSize!=''){
				        		prdSize = parseInt(prdSize)*1024;
				        	}
				        	
                            formData = {
			                	activity: JSON.stringify(activity),
			                	cmProductId: cmProdId,
			                	cuProductId: cuProdId,
			                	ctProductId: ctProdId,
			                	prdSize: prdSize
			            	};
                        }
                        else {
                            //更新手机号，需要校验
                            if ((cmProdId != '' && cuProdId == '' && ctProdId == '')
                                &&                                   ( cmMobileList == '' && cuMobileList == '' && ctMobileList == ''
                                     || cmMobileList == '' && cuMobileList != '' && ctMobileList
                                                                                    != ''
                                     || cmMobileList == '' && cuMobileList == '' && ctMobileList
                                                                                    != ''
                                     || cmMobileList == '' && cuMobileList != '' && ctMobileList
                                                                                    == '')) {
                                showTip("至少上传一个合法的移动手机号码！");
                                return false;
                            }
                            if ((cmProdId == '' && cuProdId != '' && ctProdId == '')
                                &&                                   ( cmMobileList == '' && cuMobileList == '' && ctMobileList == ''
                                     || cmMobileList != '' && cuMobileList == '' && ctMobileList
                                                                                    != ''
                                     || cmMobileList != '' && cuMobileList == '' && ctMobileList
                                                                                    == ''
                                     || cmMobileList == '' && cuMobileList == '' && ctMobileList
                                                                                    != '')) {
                                showTip("至少上传一个合法的联通手机号码！");
                                return false;
                            }
                            if ((cmProdId == '' && cuProdId == '' && ctProdId != '')
                                &&                                   ( cmMobileList == '' && cuMobileList == '' && ctMobileList == ''
                                     || cmMobileList != '' && cuMobileList == '' && ctMobileList
                                                                                    == ''
                                     || cmMobileList == '' && cuMobileList != '' && ctMobileList
                                                                                    == ''
                                     || cmMobileList != '' && cuMobileList != '' && ctMobileList
                                                                                    == '')) {
                                showTip("至少上传一个合法的电信手机号码！");
                                return false;
                            }
                            if ((cmProdId == '' && cuProdId != '' && ctProdId != '')
                                &&                                   ( cmMobileList == '' && cuMobileList == '' && ctMobileList == ''
                                     || cmMobileList != '' && cuMobileList == '' && ctMobileList
                                                                                    == '')) {
                                showTip("至少上传一个合法的联通或者电信手机号码！");
                                return false;
                            }
                            if ((cmProdId != '' && cuProdId == '' && ctProdId != '')
                                &&                                   ( cmMobileList == '' && cuMobileList == '' && ctMobileList == ''
                                     || cmMobileList == '' && cuMobileList != '' && ctMobileList
                                                                                    == '')) {
                                showTip("至少上传一个合法的移动或者电信手机号码！");
                                return false;
                            }
                            if ((cmProdId != '' && cuProdId != '' && ctProdId == '')
                                &&                                   ( cmMobileList == '' && cuMobileList == '' && ctMobileList == ''
                                     || cmMobileList == '' && cuMobileList == '' && ctMobileList
                                                                                    != '')) {
                                showTip("至少上传一个合法的移动或者联通手机号码！");
                                return false;
                            }
                            if (                                                 (cmProdId != '' && cuProdId != ''                && ctProdId != '')
                                && ( cmMobileList == '' && cuMobileList == '' && ctMobileList
                                                                                 == '')) {
                                showTip("至少上传一个合法手机号码！");
                                return false;
                            }
                            
                            //mb转kb
				        	var productSize = $("#flowSize").val();
				        	if(productSize && productSize!=''){
				        		productSize = parseInt(productSize)*1024;
				        	}
            	
                            //构造数据
			            	formData = {
			                	activity: JSON.stringify(activity),
			                	cmProductId: cmProdId,
			                	cuProductId: cuProdId,
			                	ctProductId: ctProdId,
			                	cmMobileList: cmMobileList,
			                	cuMobileList: cuMobileList,
			                	ctMobileList: ctMobileList,
			                	cmUserSet: $("#cmUserSet").val(),
			                	cuUserSet: $("#cuUserSet").val(),
			                	ctUserSet: $("#ctUserSet").val(),
			                	prdSize: productSize
			            	};
                            //$("#dataForm").submit();
                        }
                        
                        $("#save-btn").addClass("disabled");
                        
                        //开始保存
                        ajaxData("${contextPath}/manage/flowcard/saveAjax.html", formData, 
		                	function (ret) {
			                    if(ret && ret.result){
			                		$("#tips").html(ret.msg);
			                		$("#tips").val(ret.result);
			                		$("#sure-dialog").modal("show");
			                	}else{
			                		$("#tips").val("false");
			                    	$("#tips").html("保存失败!");
			                    	$("#sure-dialog").modal("show");
			                	}
		                });
                    }
                    return false;
                });

                init();
            });

    function initEnterpriseSelect() {
        $(function () {
            var parent = $(".enterprise-select");
            var val = $("input", parent).val();
            $("li[data-value='" + val + "']", parent).children("a").click();
            getBalance(val);
        });
    }

    /**
     * 获取产品信息
     * */
    function init() {
        //产品类型为流量池
        if ($("#productType").val() == '1') {
            $("#flowAccount").show();
            $("#packageAccount").hide();
        } else {
            $("#flowAccount").hide();
            $("#packageAccount").show();
        }

        //移动产品
        var cmProductId = $("#cmProductId").val();
        if (cmProductId != "") {
            getProduct(cmProductId);
        }
        else {
            $("#cmProdName").empty();
            $("#cmProdCode").empty();
            $("#cmProdSize").empty();
            $("#cmProdPrice").empty();
            $("#cmOwnership").empty();
            $("#cmRoaming").empty();
        }

        //联通产品
        var cuProductId = $("#cuProductId").val();
        if (cuProductId != "") {
            getProduct(cuProductId);
        }
        else {
            $("#cuProdName").empty();
            $("#cuProdCode").empty();
            $("#cuProdSize").empty();
            $("#cuProdPrice").empty();
            $("#cuOwnership").empty();
            $("#cuRoaming").empty();
        }

        //电信产品
        var ctProductId = $("#ctProductId").val();
        if (ctProductId != "") {
            getProduct(ctProductId);
        }
        else {
            $("#ctProdName").empty();
            $("#ctProdCode").empty();
            $("#ctProdSize").empty();
            $("#ctProdPrice").empty();
            $("#ctOwnership").empty();
            $("#ctRoaming").empty();
        }

    }

    function getProduct(productId) {
        if (productId == "") {
            return;
        }

        $.ajax({
                   type: "POST",
                   data: {
                       productId: productId
                   },
                   url: "${contextPath}/manage/flowcard/getProductAjax.html?${_csrf.parameterName}=${_csrf.token}",
                   dataType: "json", //指定服务器的数据返回类型，
                   success: function (res) {
                       if (res.success && res.success == "success") {
                           if (res.isp == "M") {
                               $("#cmProductId").val(res.productId);

                               $("#cmProdName").html(res.productName);
                               $("#cmProdCode").html(res.productCode);
                               $("#cmProdSize").html(res.productSize);
                               $("#cmProdPrice").html(res.productPrice);
                               if (res.type == '1') {
                                   $("#cmProdPrice").html('-');
                               }
                               $("#cmOwnership").html(res.ownership);
                               $("#cmRoaming").html(res.roaming);
                           }

                           if (res.isp == "T") {
                               $("#ctProductId").val(res.productId);

                               $("#ctProdName").html(res.productName);
                               $("#ctProdCode").html(res.productCode);
                               $("#ctProdSize").html(res.productSize);
                               $("#ctProdPrice").html(res.productPrice);
                               $("#ctOwnership").html(res.ownership);
                               $("#ctRoaming").html(res.roaming);
                           }

                           if (res.isp == "U") {
                               $("#cuProductId").val(res.productId);

                               $("#cuProdName").html(res.productName);
                               $("#cuProdCode").html(res.productCode);
                               $("#cuProdSize").html(res.productSize);
                               $("#cuProdPrice").html(res.productPrice);
                               $("#cuOwnership").html(res.ownership);
                               $("#cuRoaming").html(res.roaming);
                           }
                       }
                   }
               });
    }

    /**
     * 文件上传
     */
    function fileListener() {
        $("#file").on("change", function () {
            upload();

            var clone = $(this).clone();
            $(this).parent().append(clone);
            clone.remove();

            fileListener();
        });
    }

    function listeners() {
        $(function () {
            //企业余额
            $(".enterprise-select a").on("click", function () {
                var entId = $(this).parent().attr("data-value");
                getBalance(entId);
                //emptyAwardProducts();
            });

            $(".openProductsDialog-btn").on("click", function () {
                var type = $(this).data("type");
                var enterId = $(this).parent().attr("data-value");
                $("#ispType").val(type);
                $("#enterId").val(enterId);
                //
                $("#products-dialog").modal("show");
                $("#search-btn").click();
            });
        });
    }

    //获取企业余额
    function getBalance() {
        var entId = $("#entId").val();
        if (entId == "") {
            $("#balance").html("");
            return;
        }

        $.ajax({
                   type: "POST",
                   data: {
                       entId: entId
                   },
                   url: "${contextPath}/manage/flowcard/getBalanceAjax.html?${_csrf.parameterName}=${_csrf.token}",
                   dataType: "json", //指定服务器的数据返回类型，
                   success: function (res) {
                       if (res.success && res.success == "success" && res.balance) {
                           $("#balance").html(res.balance);
                       }
                       else {
                           $("#balance").html("0元");
                       }
                   }
               });
    }

    /**
     * 模拟文件选择器
     */
    function fileBoxHelper() {
        $(".file-helper").on("change", function () {
            var path = this.value;
            var lastsep = path.lastIndexOf("\\");
            var filename = path.substring(lastsep + 1);
            $(this).parent().children(".file-input").val(filename);
        });
    }

    /**
     *
     * @param msg
     */
    function showTip(msg) {
        $("#tip-dialog .message-content").html(msg);
        $("#tip-dialog").modal("show");
    }

    /**
     * 表单验证
     */
    function formValidate() {
        $("#dataForm").validate({
                                    errorPlacement: function (error, element) {
                                        if ($(element).attr("id") == "cmProdSize_flowaccount") {
                                            error.addClass("error-tip-flowaccount");
                                        } else {
                                            error.addClass("error-tip");
                                        }
                                        $(element).parents(".form-group").append(error);
                                    },

                                    errorElement: "span",
                                    rules: {
                                        entId: {
                                            required: true
                                        },
                                        name: {
                                            required: true,
                                            cmaxLength: 64,
                                            space: true,
                                            remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="
                                                    + $('#name').val()
                                        },
                                        startTime: {
                                            required: true
                                        },
                                        cmProdSize_flowaccount: {
                                            required: true,
                                            max: 11264,
                                            min: 1,
                                            positive: true
                                        }
                                    },
                                    messages: {
                                        entId: {
                                            required: "请选择企业!"
                                        },
                                        name: {
                                            required: "请填写活动名称!",
                                            cmaxLength: "活动名称不允许超过64个字符!",
                                            space: "活动名称不允许含有空格!",
                                            remote: "文案中有不良信息，请删除后再次提交"
                                        },
                                        startTime: {
                                            required: "请选择活动时间!"
                                        },
                                        cmProdSize_flowaccount: {
                                            required: "请填写流量大小!",
                                            max: "请输入1-11264的正整数",
                                            min: "请输入1-11264的正整数",
                                            positive: "请输入11264的正整数"
                                        }
                                    }
                                });
    }

    /**
     * 初始化日期选择器
     */
    function initDateRangePicker2() {
        var ele = $('#activeTime');

        var startEle = $('#startTime');
        var endEle = $('#endTime');
        ele.dateRangePicker({
                                startDate: new Date(),
                                separator: ' ~ ',
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
                                    var validator = $("#dataForm").validate();
                                    if (validator.check(startEle[0])) {
                                        var err = validator.errorsFor(startEle[0]);
                                        if (err && err.length) {
                                            err.hide();
                                        }
                                    }

                                    if (validator.check(endEle[0])) {
                                        var err = validator.errorsFor(endEle[0]);
                                        if (err && err.length) {
                                            err.hide();
                                        }
                                    }

                                    $("#time_tip").hide();
                                    $("#time_tip").html("");
                                }
                            });
    }

    function checkTimeInput() {
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        if (startTime == '' || endTime == '') {
            $("#time_tip").show();
            $("#time_tip").html("请输入起止时间");
            return false;
        }
        return true;
    }

    /**
     * 批量上传
     */
    function upload() {
        $.ajaxFileUpload({
                             url: '${contextPath}/manage/flowcard/uploadPhones.html?${_csrf.parameterName}=${_csrf.token}',
                             secureuri: false,
                             fileElementId: 'file',
                             dataType: 'json',
                             success: function (data) {
                                 if (data) {
                                     if (data.successMsg == "success") {
                                         $("#cmMobileList").val(data.cmMobileList);
                                         $("#cuMobileList").val(data.cuMobileList);
                                         $("#ctMobileList").val(data.ctMobileList);

                                         $("#correctMobiles").val(data.correctMobiles);
                                         $("#invalidMobiles").val(data.invalidMobiles);

                                         $("#cmUserSet").val(data.cmUserSet);
                                         $("#cuUserSet").val(data.cuUserSet);
                                         $("#ctUserSet").val(data.ctUserSet);

                                         $("#modal_error_msg").html("上传手机号成功!");
                                         $("#phones").val(data.correctMobiles);
                                     }

                                     if (data.successMsg == "fail") {
                                         $("#invalidMobiles").val(data.invalidMobiles);
                                         $("#modal_error_msg").html("请至少上传一个合法的手机号!");
                                     }

                                     if (data.errorMsg) {
                                         $("#modal_error_msg").html(data.errorMsg);
                                     }
                                 }
                             },
                             error: function (data) {
                                 $("#modal_error_msg").html("上传文件失败");
                             }
                         });
        return false;
    }

</script>
</body>
</html>