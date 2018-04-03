<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>四川流量平台-新增产品</title>
    <meta name="keywords" content="四川流量平台 新增产品"/>
    <meta name="description" content="四川流量平台 新增产品"/>

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
        .text-center th {
            text-align: center;
        }
        .count{
            width: 50%;
        }
        .discount-tip {
            color: red;
            margin: 0 auto;
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>
            变更产品
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="window.location.href='${contextPath}/manage/enterprise/productChange/index.html'">返回</a>
        </h3>
    </div>
    <div>
        <div class="mt-30 mb-20">
        	<#--
            <div class="form-group form-group-sm form-inline">
                <label>产品模板：</label>
                <div class="btn-group btn-group-sm">
                    <input type="text" style="width: 0; height:0; opacity: 0;" class="" name="productTemplate"
                           id="productTemplate">
                    <button type="button" class="btn btn-default">请选择</button>
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        <span class="caret"></span>
                        <span class="sr-only">Toggle Dropdown</span>
                    </button>
                    <ul class="dropdown-menu templateType">
                        <li><a href="#">全部</a></li>
                        <li data-value="1"><a href="#">模板1</a></li>
                        <li data-value="2"><a href="#">模板2</a></li>
                        <li data-value="3"><a href="#">模板3</a></li>
                        <li data-value="4"><a href="#">模板4</a></li>
                    </ul>
                </div>
            </div>
            -->
        </div>
        <div class="mt-30">
            <h5 class="h5">企业产品列表</h5>
            <div class="dataTables_filter1 text-left">
                <form class="form-inline">
                    <input type="text" class="form-control hidden" id="entId" name="entId" value="${enterprise.id!}"
                           autocomplete="off" maxlength="255"/>
                    <div class="form-group hidden">
                        <a tpye="submit" class="btn btn-warning btn-sm" id="search-btn1">确定</a>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
            </div>
            <div class="tile mt-30">
                <!--放入列表-->
                <div id="listData1" class="text-center">
                    <div>
                        <div class="tile">
                            <div class="tile-content" style="padding:0;">
                                <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                                    <div class="table-responsive">
                                        <div class="table-responsive">
                                            <table class="cm-table table table table-indent text-center table-bordered-noheader mb-0">
                                                <thead>
                                                <tr>
                                                    <th>运营商</th>
                                                    <th>产品名称</th>
                                                    <th>产品编码</th>
                                                    <th>产品大小</th>
                                                    <th>售出价格</th>
                                                    <th>状态</th>
                                                    <th>使用范围</th>
                                                    <th>漫游范围</th>
                                                    <#if provinceFlag == "ziying">
                                                    	<th>折扣</th>
                                                    </#if>
                                                    <th>操作</th>
                                                </tr>
                                                </thead>
                                                <tbody></tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-sm-12">
        <div class="tools row">
            <h5 class="h5 mt-40">选择产品</h5>
            <div class="dataTables_filter2 text-left">
                <form>
                    <div class="form-inline">
                        <input type="text" class="form-control hidden searchItem" id="enterId" name="enterId"
                               value=""
                               autocomplete="off" maxlength="255"/>

                        <div class="form-group form-group-sm">
                            <label for="">产品名称: </label>&nbsp;
                            <input type="text" class="form-control searchItem" name="name" autocomplete="off"
                                   maxlength="255" data-type="like"/>
                        </div>
                        <div class="form-group form-group-sm">
                            <label for="">产品编码: </label>&nbsp;
                            <input type="text" class="form-control searchItem" name="productCode" autocomplete="off"
                                   maxlength="255" data-type="like"/>
                        </div>
                        <div class="form-group form-group-sm">
                            <label for="">产品大小: </label>&nbsp;
                            <input type="text" class="form-control searchItem" name="productSize"/>
                            <div class="btn-group btn-group-sm">
                                <input type="text" style="width: 0; height:0; opacity: 0;" class="searchItem"
                                       name="productUnit">
                                <button type="button" class="btn btn-default">请选择</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li data-value=""><a href="#">请选择</a></li>
                                    <li data-value="1"><a href="#">KB</a></li>
                                    <li data-value="2"><a href="#">MB</a></li>
                                    <li data-value="3"><a href="#">GB</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="form-inline mt-10">
                        <div class="form-group">
                            <label for="">运营商：</label>&nbsp;
                            <div class="btn-group btn-group-sm">
                                <input type="text" style="width: 0; height:0; opacity: 0;" class="searchItem" name="isp"
                                       data-type="like">
                                <button type="button" class="btn btn-default">请选择</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a href="#">请选择</a></li>
                                    <li data-value="A"><a href="#">三网</a></li>
                                    <li data-value="M"><a href="#">移动</a></li>
                                    <li data-value="U"><a href="#">联通</a></li>
                                    <li data-value="T"><a href="#">电信</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="">使用范围：</label>&nbsp;
                            <div class="btn-group btn-group-sm">
                                <input type="text" style="width: 0; height:0; opacity: 0;" class="searchItem"
                                       name="ownershipRegion">
                                <button type="button" class="btn btn-default">请选择</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu province-menu">

                                </ul>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="">漫游范围：</label>&nbsp;
                            <div class="btn-group btn-group-sm">
                                <input type="text" style="width: 0; height:0; opacity: 0;" class="searchItem"
                                       name="roamingRegion">
                                <button type="button" class="btn btn-default">请选择</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu province-menu">

                                </ul>
                            </div>
                        </div>
                        <div class="form-group">
                            <a type="submit" class="btn btn-warning btn-sm" id="search-btn2">确定</a>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
        <div class="mt-30 row">
            <div id="listData2" class="text-center"></div>
        </div>

        <div class="mt-30 text-center">
            <a class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn">保存</a>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="tips-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="tips"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="tips-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

</body>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var tableData = {}, filterTable, filterTable2;
	//是否自营标识
	var provinceFlag = "${provinceFlag}";
	
    var statusFormat = function (value, column, row) {

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
    	if (row.productSize == null) {
        	return "-";
    	}
        	
    	if(row.type && row.type==4){
    		return row.productSize + "个";
    	}else{
    		if (row.productSize < 1024) {
	            return row.productSize + "KB";
	        }
	        if (row.productSize >= 1024 && row.productSize < 1024 * 1024) {
	            return row.productSize * 1.0 / 1024 + "MB";
	        }
	        if (row.productSize >= 1024 * 1024) {
	            return row.productSize * 1.0 / 1024 / 1024 + "GB";
	        }
        	return row.productSize * 1.0 / 1024 + "MB";
    	}
    };

    var priceFormat = function (value, column, row) {
        if (row.price == null) {
            return "-";
        }
        return (row.price / 100.0).toFixed(2) + "元";
    };

    var opFormat1 = function (value, cell, row) {
        tableData[row.id] = row;
        return ['<a class="icon-del mr-5 remove-btn" data-id="' + row.id + '" href="#">删除</a>',
            '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/enterprise/showEnterpriseDetail.html?id=' + row.id + '">查看</a>'];
    };

    var discountFormat = function (value, column, row) {
        var select = '<select name="discount" id="type" data-value="' + value + '" class="select-discount" data-id="' + row.id + '" onchange="chooseDiscount(this)">' +
                discountOptions +
                '</select>';
        var s = $(select);
        s.val(value);
        return select;
    }

    var productStatusFormat = function (value, column, row){
        if(row.status==0){
            return "下架";
        }
        if(row.status==1){
            return "上架";
        }
    };

    //折扣显示转化
    function discountFun(discount){
        if(discount==""){
            return "";
        }
        return (discount / 10.0).toFixed(1);
    }

    var opFormat2 = function (value, cell, row) {
        tableData[row.id] = row;
        return ['<a class="icon-del mr-5 add-btn" data-id="' + row.id + '" href="#">新增</a>',
            '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/supplierProduct/showDetail.html?id=' + row.id + '">查看</a>'];
    };

    var columns2 = [{name: "isp", text: "运营商", format: ispFormat},
        {name: "name", text: "产品名称"},
        {name: "productCode", text: "产品编码"},
        {name: "productSize", text: "产品大小", format: sizeFormat},
        {name: "price", text: "售出价格", format: priceFormat},
        {name: "status", text: "状态", format: productStatusFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {name: "", text: "操作", format: opFormat2}];
    var action2 = "${contextPath}/manage/enterProductChange/getProducts.html?${_csrf.parameterName}=${_csrf.token}";

    require(["react", "react-dom", "page/listDate", "common", "bootstrap"], function (React, ReactDOM, ListData) {

        initParams();
        ReactDOM.render(React.createElement(ListData, {
            columns: columns2,
            searchClass: "searchItem",
            searchBtn: $("#search-btn2")[0],
            action: action2
        }), $("#listData2")[0]);


        $(".dropdown-menu.templateType").on("click", "a", function () {
            allId.splice(0,allId.length);
            $('#listData1 tbody tr').remove();
            $('#enterId').val("");
            $("#search-btn2").click();
        });

        $("#listData2").on("click", ".add-btn", function () {
            addProduct($(this));
        });

        $("#listData1").on("click", ".remove-btn", function () {
//            filterTable2.addRow(tableData[id]);
            deleteProduct($(this));
        });

        $("#listData1").on("click",".mod-btn",function(){
            $(this).data('flag',!$(this).data('flag'));
            if($(this).data('flag')){
                $(this).html('确定');
                var discount = $(this).parent().find(".discount").html();
                $(this).parent().find(".discount").html('<input class="count" value="'+discount+'">');
            }else{
                $(this).html('修改');
                var discount = $(this).parent().find(".count").val();
                $(this).parent().find(".discount").html(discount);
            }
        });

        $("#save-btn").on("click", function () {
            saveForm();
        });
        
        $("#tips-btn").on("click", function () {
        	var result = $("#tips").val();
        	if(result == "true"){
        		window.location.href = "${contextPath}/manage/enterProductChange/index.html";
        	}
        });
    });

	function productsChosenByEnterprise(productList){
		if(provinceFlag == "ziying"){
			//自营
        	if(productList && productList.length>0){
	            for(var i = 0; i < productList.length; i++){
	                var data = productList[i];
	                allId.push(data.id);
	                tableData[data.id] = data;	 
	                appendProduct({
	                    id: data.id,
	                    isp: data.isp,
	                    name: data.name,
	                    productCode: data.productCode,
	                    productSize: data.productSize,
	                    price: data.price,
	                    status: data.status,
	                    ownershipRegion: data.ownershipRegion,
	                    roamingRegion: data.roamingRegion,
	                    discount: data.discount
	                });
	            }
        	}
		}else{
			if(productList && productList.length>0){
	            for(var i = 0; i < productList.length; i++){
	                var data = productList[i];
	                allId.push(data.id);
					tableData[data.id] = data;	                
	                appendProduct({
	                    id: data.id,
	                    isp: data.isp,
	                    name: data.name,
	                    productCode: data.productCode,
	                    productSize: data.productSize,
	                    price: data.price,
	                    status: data.status,
	                    ownershipRegion: data.ownershipRegion,
	                    roamingRegion: data.roamingRegion
	                });
	            }
        	}
		}
		$("#enterId").val(allId);
	}

    var allId = [];

    function initParams() {
    	var productList = ${products!};
        productsChosenByEnterprise(productList);
    }

    function addProduct(btnEle) {
        var spId = btnEle.data("id");
        allId.push(spId);
        var row = tableData[spId];
        appendProduct(row);
        $("#enterId").val(allId);
        $("#search-btn2").click();
    }
    function appendProduct(data) {
        var parent = $("#listData1 tbody");
        var discount = isNaN(discountFun(data["discount"]))?"":discountFun(data["discount"]);
        var trs;
        if(provinceFlag == "ziying"){
        	//自营
        	trs = $('<tr data-id="' + data["id"] + '">' +
                '<td>' + ispFormat(0, 0, data) + '</td>' +
                '<td>' + data["name"] +
                '</td><td>' + data["productCode"] +
                '</td><td>' + sizeFormat(0, 0, data) +
                '</td><td>' + priceFormat(0, 0, data) +
                '</td><td>' + productStatusFormat(0, 0, data) +
                '</td><td>' + data["ownershipRegion"] +
                '</td><td>' + data["roamingRegion"] +
                '<td><span class="discount">'+ discount+'</span>折' +
                '<a class="btn-icon icon-del mr-5 mod-btn" data-flag="false" data-id="' + data["id"] + '" href="#">修改</a>'+
                '<div class="discount-tip">' +
                '</div>' +
                '</td>' +
                '<td>' + '<a class="btn-icon icon-del mr-5 remove-btn" data-id="' + data["id"] + '" href="#">删除</a>' +
                '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/supplierProduct/showDetail.html?id=' + data["id"] + '">查看</a></td>' +
                '</tr>');
        }else{
        	//省公司
        	trs = $('<tr data-id="' + data["id"] + '">' +
                '<td>' + ispFormat(0, 0, data) + '</td>' +
                '<td>' + data["name"] +
                '</td><td>' + data["productCode"] +
                '</td><td>' + sizeFormat(0, 0, data) +
                '</td><td>' + priceFormat(0, 0, data) +
                '</td><td>' + productStatusFormat(0, 0, data) +
                '</td><td>' + data["ownershipRegion"] +
                '</td><td>' + data["roamingRegion"] +
                '<td>' + '<a class="btn-icon icon-del mr-5 remove-btn" data-id="' + data["id"] + '" href="#">删除</a>' +
                '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/supplierProduct/showDetail.html?id=' + data["id"] + '">查看</a></td>' +
                '</tr>');
        }
        parent.append(trs);
    }

    function deleteProduct(btnEle) {
        var spId = btnEle.data("id");
        allId.splice($.inArray(spId, allId), 1);
        $("#enterId").val(allId);
        btnEle.parent().parent().remove();
        $("#search-btn2").click();
    }

    function tableList(trs) {
        var listData = [];
        trs.each(function () {
            var proId = $(this).data("id");
            var trData = tableData[proId];
            trData["discount"] = $('.discount',this).html();
            listData.push(trData);
        });
        return listData;
    }

    function saveForm() {
        var trs = $("#listData1 tbody tr");
        var valid = true;
        
        if(provinceFlag == "ziying"){
        	//自营，需要判断折扣
        	trs.each(function () {
	            var id = $(this).data("id");
	            if($(this).find(".count").length !== 0){
	                if (!$(this).find(".count").val()) {
	                    $("#tips").html("您还有未填写的折扣");
	                    $("#tips-dialog").modal('show');
	                    valid = false;
	                    return false;
	                } else if (!/^(0\.[1-9]|[1-9](\.[0-9])?|10(\.[0])?)$/.test($(this).find(".count").val())) {
	                    $(this).find(".discount-tip").html('请输入正确折扣格式');
	                    $("#tips").html("您填写了错误的折扣");
	                    $("#tips-dialog").modal('show');
	                    valid = false;
	                    return false;
	                } else {
	                    $(this).find(".discount-tip").html('');
	                    $("#tips").html("您还有未保存的折扣");
	                    $("#tips-dialog").modal('show');
	                    valid = false;
	                    return false;
	                }
	            }else{
	                if(!$(this).find(".discount").html()){
	                    $("#tips").html("您还有未填写的折扣");
	                    $("#tips-dialog").modal('show');
	                    valid = false;
	                    return false;
	                }else if (!/^(0\.[1-9]|[1-9](\.[0-9])?|10(\.[0])?)$/.test($(this).find(".discount").html())) {
	                    $(this).find(".discount-tip").html('请填写正确折扣格式');
	                    $("#tips").html("您填写了错误的折扣");
	                    $("#tips-dialog").modal('show');
	                    valid = false;
	                    return false;
	                }
	                else{
	                    $(this).find(".discount-tip").html('');
	                    valid = true;
	                }
	            }
	        });
        }

        if(valid){
            var list = tableList($('#listData1 tbody tr'));

            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                type: "POST",
                url: "${contextPath}/manage/enterProductChange/saveProductChange.html",
                data: {
                    enterpriseId: ${enterprise.id},
                    products: JSON.stringify(list)
                },
                dataType: "json", //指定服务器的数据返回类型，
                success: function (data) {
                    $("#tips").html(data.msg);
                	$("#tips").val(data.result);
        			$("#tips-dialog").modal("show");
                },
                error: function () {
                    $("#tips").html("网络出错，请重新尝试");
                	$("#tips").val("false");
        			$("#tips-dialog").modal("show");
                }
            });
        }
    }

    function chooseDiscount(ele) {
        var id = $(ele).data("id");
        var value = $(ele).val();
        var row = tableData[id];

        row.discount = value;
    }

</script>
</html>