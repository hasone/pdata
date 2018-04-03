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
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>
            新建产品管理
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="window.location.href='${contextPath}/manage/platformProduct/index.html'">返回</a>
        </h3>
    </div>
    <div>
        <div class="mt-30">
            <h5 class="h5">已关联的boss产品</h5>
            <form>
                <input type="text" class="form-control searchItem1 hidden" id="platformProductId"
                       name="platformProductId" value="${platformProductId!}" autocomplete="off" maxlength="255"/>
                <a class="btn btn-warning hidden" id="search-btn1">确定</a>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
            <div class="tile mt-30">
                <!--放入列表-->
                <div id="listData1"></div>
            </div>
        </div>

    </div>
    <div class="col-sm-12">
        <div class=" tools row">
            <h5 class="h5">选择Boss产品</h5>
            <div class="dataTables_filter text-left">
                <form class="form-inline">
                    <input type="text" class="form-control searchItem hidden" id="platformProductId"
                           name="platformProductId" value="${platformProductId!}" autocomplete="off" maxlength="255"/>

                    <input type="text" class="form-control searchItem hidden" name="size" value="${size!}"
                           autocomplete="off" maxlength="255"/>

                    <div class="form-group form-group-sm">
                        <label for="">供应商: </label>&nbsp;
                        <input type="text" class="form-control searchItem" name="supplierName" autocomplete="off"
                               maxlength="255"/>
                    </div>
                    <div class="form-group form-group-sm">
                        <label for="">产品名称: </label>&nbsp;
                        <input type="text" class="form-control searchItem" name="name" autocomplete="off"
                               maxlength="255"/>
                    </div>
                    <div class="form-group form-group-sm">
                        <label for="">产品编码: </label>&nbsp;
                        <input type="text" class="form-control searchItem" name="productCode" class="abc input-default"
                               autocomplete="off"
                               placeholder="" value="" maxlength="255"/>
                    </div>
                    <div class="form-group">
                        <label for="">运营商：</label>&nbsp;
                        <div class="btn-group btn-group-sm">
                            <input type="text" style="width: 0; height:0; opacity: 0;" class="searchItem" name="isp">
                            <button type="button" class="btn btn-default">请选择</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="#">全部</a></li>
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
                        <label for="">状态：</label>&nbsp;
                        <div class="btn-group btn-group-sm">
                            <input type="text" style="width: 0; height:0; opacity: 0;" class="searchItem" name="status">
                            <button type="button" class="btn btn-default">请选择</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                                <li data-value=""><a href="#">全部</a></li>
	                            <li data-value="ON"><a href="#">上架</a></li>
	                            <li data-value="OFF"><a href="#">下架</a></li>
                            </ul>
                        </div>
                    </div>
                   
                    <div class="form-group">
                        <a tpye="submit" class="btn btn-warning" id="search-btn2">确定</a>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
            </div>

        </div>
        <div class="mt-30 row">
            <div id="listData2"></div>
        </div>

        <div class="mt-30 text-center">
            <a class="btn btn-warning btn-sm mr-10 dialog-btn"
               onclick="javascript:window.location.href='${contextPath}/manage/platformProduct/index.html'">保存</a>

        </div>


    </div>
</div>

</body>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var statusFormat = function (value, column, row) {

    };

    var opFormat1 = function (value, cell, row) {
        return ['<a class="btn-icon icon-del mr-5 delete-btn" data-id="' + row.id + '" href="#">删除</a>',
            '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/supplierProduct/showDetail.html?id=' + row.id + '">查看</a>'];
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
    	if(row.productType == null){
    		return "-";
    	}
    	
    	if(row.productType==4){
    		return row.size + "个";
    	}else{
    		if (row.size == null) {
            	return "-";
        	}
	        if (row.size < 1024) {
	            return row.size + "KB";
	        }
	        if (row.size >= 1024 && row.size < 1024 * 1024) {
	            return (row.size * 1.0 / 1024) + "MB";
	        }
	        if (row.size >= 1024 * 1024) {
	            return (row.size * 1.0 / 1024 / 1024) + "GB";
	        }
        	return row.size * 1.0 / 1024 + "MB";
    	}
        
    };

    var priceFormat = function (value, column, row) {
        if (row.price == null) {
            return "-";
        }
        return (row.price / 100.0).toFixed(2) + "元";
    };
    
    var supplierProductStatusFormat = function (value, column, row){
    	if(row.supplierStatus==0 || row.status==0){
    		return "下架";
    	}
    	if(row.status==1 && row.supplierStatus==1){
    		return "上架";
    	}
    }

    var action1 = "${contextPath}/manage/platformProduct/getRelation.html?${_csrf.parameterName}=${_csrf.token}";

    var columns1 = [{name: "supplierName", text: "供应商"},
        {name: "isp", text: "运营商", format: ispFormat},
        {name: "name", text: "产品名称"},
        {name: "code", text: "产品编码"},
        {name: "size", text: "流量包大小", format: sizeFormat},
        {name: "price", text: "采购价格", format: priceFormat},
        {name: "status", text: "状态", format: supplierProductStatusFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {name: "", text: "操作", format: opFormat1}];


    var opFormat2 = function (value, cell, row) {
        return ['<a class="btn-icon icon-del mr-5 add-btn" data-id="' + row.id + '" href="#">新增</a>', '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/supplierProduct/showDetail.html?id=' + row.id + '">查看</a>'];
    };

    var columns2 = [{name: "supplierName", text: "供应商"},
        {name: "isp", text: "运营商", format: ispFormat},
        {name: "name", text: "产品名称", tip: true},
        {name: "code", text: "产品编码", tip: true},
        {name: "size", text: "产品大小", format: sizeFormat},
        {name: "price", text: "采购价格(元)", format: priceFormat},
        {name: "status", text: "状态", format: supplierProductStatusFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {name: "op", text: "操作", format: opFormat2}];

    var action2 = "${contextPath}/manage/platformProduct/getBossProduct.html?${_csrf.parameterName}=${_csrf.token}";


    require(["react", "react-dom", "page/listDate", "common", "bootstrap"], function (React, ReactDOM, ListData) {

        ReactDOM.render(React.createElement(ListData, {
            columns: columns1,
            searchClass: "searchItem1",
            searchBtn: $("#search-btn1")[0],
            action: action1
        }), $("#listData1")[0]);

        ReactDOM.render(React.createElement(ListData, {
            columns: columns2,
            searchClass: "searchItem",
            searchBtn: $("#search-btn2")[0],
            action: action2
        }), $("#listData2")[0]);


        $("#listData2").on("click", ".add-btn", function () {
            addProduct($(this));
        });
        $("#listData1").on("click", ".delete-btn", function () {
            deleteProduct($(this));
        });
    });

    function addProduct(btnEle) {

        var spId = btnEle.data("id");
        var pltProductId = $("#platformProductId").val();

        var url = "${contextPath}/manage/platformProduct/addRelation.html.html";

        ajaxData(url, {"spId": spId, "pltProductId": pltProductId}, function (ret) {
            if (ret && ret.success) {
                $("#search-btn1").click();
                $("#search-btn2").click();
            }
        });
    }

    function deleteProduct(btnEle) {
        var spId = btnEle.data("id");
        var pltProductId = $("#platformProductId").val();
        var url = "${contextPath}/manage/platformProduct/deleteRelation.html";
        ajaxData(url, {"spId": spId, "pltProductId": pltProductId}, function (ret) {
            if (ret && ret.success) {
                $("#search-btn1").click();
                $("#search-btn2").click();
            }
        });
    }
</script>
</html>