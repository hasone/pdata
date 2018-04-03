<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>四川流量平台-编辑产品模板</title>
    <meta name="keywords" content="四川流量平台 编辑产品模板"/>
    <meta name="description" content="四川流量平台 编辑产品模板"/>

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
                                编辑	
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="window.location.href='${contextPath}/manage/productTemplate/index.html?back=1'">返回</a>
        </h3>
    </div>

    <div class="mt-30 mb-20">
        <div class="form-group form-group-sm form-inline">
            <label>产品模板名称：</label>
            <input type="text" class="form-control" id="name"
                   name="name" maxlength="64" value="${productTemplate.name!}"/>
        </div>
    </div>
    <div>
        <div class="mt-30">
            <h5 class="h5">已包含平台产品列表</h5>
            <form>
                <input type="text" class="form-control searchItem1 hidden" id="templateId"
                       name="templateId" value="${productTemplate.id!}" autocomplete="off" maxlength="255"/>
                <a class="btn btn-warning hidden" id="search-btn1">确定</a>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            <div class="tile mt-30">
                <!--放入列表-->
                <div id="listData1">
                    <div>
                        <div class="tile">
                            <div class="tile-content" style="padding:0;">
                                <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                                    <div class="table-responsive">
                                        <div class="table-responsive">
                                            <table class="cm-table table table table-indent text-center table-bordered-noheader mb-0">
                                                <thead>
                                                <tr>
                                                    <th name="isp">运营商</th>
                                                    <th name="name">产品名称</th>
                                                    <th name="productCode">产品编码</th>
                                                    <th name="size">产品大小</th>
                                                    <th name="price">售出价格</th>
                                                    <th name="status">状态</th>
                                                    <th name="ownershipRegion">使用范围</th>
                                                    <th name="roamingRegion">漫游范围</th>
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
            <h5 class="h5 mt-40">选择平台产品</h5>
            <div class="dataTables_filter text-left">
                <form>
                    <div class="form-inline">
                        <input type="text" class="form-control searchItem hidden" id="productIds"
                               name="productIds"/>

                        <input type="text" class="form-control searchItem hidden" id="productTemplateId"
                               name="productTemplateId" value="${productTemplate.id!}"/>

                        <div class="form-group form-group-sm">
                            <label for="">产品名称: </label>&nbsp;
                            <input type="text" class="form-control searchItem" name="productName" autocomplete="off"
                                   maxlength="255"/>
                        </div>
                        <div class="form-group form-group-sm">
                            <label for="">产品编码: </label>&nbsp;
                            <input type="text" class="form-control searchItem abc input-default" name="productCode"
                                   autocomplete="off"
                                   placeholder="" value="" maxlength="255"/>
                        </div>
                        
                        <div class="form-group mr-10 form-group-sm">
		                    <label for="exampleInputName2">产品大小：</label>
		                    <input type="text" class="form-control searchItem abc input-default mobileOnly" name="size"
		                           autocomplete="off"
		                           placeholder="" value="" maxlength="255"> 
	                	</div>
		                <div class="form-group">
		                    <div class="btn-group btn-group-sm">
		                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="searchItem"
		                               name="unit" value="MB">
		                        <button type="button" class="btn btn-default" style="width: 35px">MB</button>
		                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
		                                aria-haspopup="true" aria-expanded="false">
		                            <span class="caret"></span>
		                            <span class="sr-only">Toggle Dropdown</span>
		                        </button>
		                        <ul class="dropdown-menu">
		                            <li data-value="MB"><a href="#">MB</a></li>
		                            <li data-value="GB"><a href="#">GB</a></li>
		                        </ul>
		                    </div>
		                </div>
                        
                    </div>
                    <div class="form-inline mt-10">
                        <div class="form-group">
                            <label for="">运营商：</label>&nbsp;
                            <div class="btn-group btn-group-sm">
                                <input type="text" style="width: 0; height:0; opacity: 0;" class="searchItem"
                                       name="isp">
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
                                <input type="text" style="width: 0; height:0; opacity: 0;" class="searchItem"
                                       name="status">
                                <button type="button" class="btn btn-default">请选择</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li data-value=""><a href="#">全部</a></li>
                                    <li data-value="on"><a href="#">上架</a></li>
                                    <li data-value="off"><a href="#">下架</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="form-group">
                            <a tpye="submit" class="btn btn-warning" id="search-btn2">确定</a>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
        <div class="mt-30 row">
            <div id="listData2"></div>
        </div>

        <div class="mt-30 text-center">
            <a class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn">保存</a>
        </div>
    </div>
    
    <div class="modal fade dialog-sm" id="subject-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content" id="msg"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
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
                    <button class="btn btn-warning btn-sm" id="subject-btn">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

    <div class="modal fade dialog-sm" id="valid-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content" id="validTips"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
    
</div>

</body>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var opFormat1 = function (value, cell, row) {
        return ['<a class="btn-icon icon-del mr-5 delete-btn" data-id="' + row.id + '" href="#">删除</a>',
            '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/supplierProduct/platFormProductDetail.html?productId=' + row.id + '">查看</a>'];
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

    var priceFormat = function (value, column, row) {
        if (row.price == null) {
            return "-";
        }
        return (row.price / 100.0).toFixed(2) + "元";
    };
    
    var productStatusFormat = function (value, column, row){
    	if(row.status==0){
    		return "下架";
    	}
    	if(row.status==1){
    		return "上架";
    	}
    };

    var tableData = {};
    var action2 = "${contextPath}/manage/productTemplate/getProducts.html?${_csrf.parameterName}=${_csrf.token}";

    var opFormat2 = function (value, cell, row) {
        tableData[row.id] = row;
        return ['<a class="btn-icon icon-del mr-5 add-btn" data-id="' + row.id + '" href="#">新增</a>', '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/supplierProduct/platFormProductDetail.html?productId=' + row.id + '">查看</a>'];
    };

    var columns2 = [{name: "isp", text: "运营商", format: ispFormat},
        {name: "name", text: "产品名称", tip: true},
        {name: "productCode", text: "产品编码", tip: true},
        {name: "productSize", text: "产品大小", format: sizeFormat},
        {name: "price", text: "采购价格(元)", format: priceFormat},
        {name: "status", text: "状态", format: productStatusFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {name: "op", text: "操作", format: opFormat2}];


    require(["react", "react-dom", "page/listDate", "common", "bootstrap"], function (React, ReactDOM, ListData) {

        initParams();

        ReactDOM.render(React.createElement(ListData, {
            columns: columns2,
            searchClass: "searchItem",
            searchBtn: $("#search-btn2")[0],
            action: action2
        }), $("#listData2")[0]);

        <#--$("#search-btn2").click();-->

        $("#listData2").on("click", ".add-btn", function () {
            addProduct($(this));
        });
        $("#listData1").on("click", ".delete-btn", function () {
            deleteProduct($(this));
        });
        
        //监听
        listeners();
    });

    var allId = [];

    function initParams() {
        var products = ${products!};
        if(products && products.length>0){
            for(var i = 0; i < products.length; i++){
                var data = products[i];
                allId.push(data.id);
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
        $("#productIds").val(allId);
    }

    function appendProduct(data) {
        var parent = $("#listData1 tbody");
        var trs = $('<tr data-id="' + data["id"] + '">' +
                '<td>' + ispFormat(0, 0, data) + '</td>' +
                '<td>' + data["name"] +
                '</td><td>' + data["productCode"] +
                '</td><td>' + sizeFormat(0, 0, data) +
                '</td><td>' + priceFormat(0, 0, data) +
                '</td><td>' + productStatusFormat(0, 0, data) +
                '</td><td>' + data["ownershipRegion"] +
                '</td><td>' + data["roamingRegion"] +
                '<td>' + '<a class="btn-icon icon-del mr-5 delete-btn" data-id="' + data["id"] + '" href="#">删除</a>' +
                '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/supplierProduct/platFormProductDetail.html?productId=' + data["id"] + '">查看</a></td>' +
                '</tr>');
        parent.append(trs);
    }

    function listeners(){
    	$("#save-btn").on("click", function () {
            if (!$("input[name='name']").val()) {
                $("#validTips").html('请输入产品模板名称');
                $("#valid-dialog").modal('show');
                return
            }

            var name = $("input[name='name']").val();

            if(name == "无产品模板"){
                $("#validTips").html("产品模板名称不能命名为'无产品模板'");
                $("#valid-dialog").modal('show');
                return
            }

            var productTemplateId = $("input[name='templateId']").val();
            $.ajax({
                type: 'post',
                secureuri: false,
                data: {
                    name : name,
                    productTemplateId : productTemplateId
                },
                url: "${contextPath}/manage/productTemplate/checkNameUnique.html?${_csrf.parameterName}=${_csrf.token}",
                dataType: 'json',
                success: function (ret) {
                    if(ret.result && ret.result == "false"){
                        $("#validTips").html('已经存在该模板名称');
                        $("#valid-dialog").modal('show');
                        return;
                    }else{
                        if ($("#listData1 tbody tr").length === 0) {
                            $("#validTips").html('产品模版至少需要包含一个平台产品');
                            $("#valid-dialog").modal('show');
                            return
                        }
                        $("#msg").html("模版保存后即生效，请再次确认？");
                        $("#subject-dialog").modal("show");
                        //saveRelation();
                    }

                },
                error: function () {
                    $("#validTips").html('网络异常，请重新再试');
                    $("#valid-dialog").modal('show');
                    return;
                }
            });

        });
        
        $("#subject-btn").on("click", function () {
            var result = $("#tips").val();
            if(result == "true"){
                window.location.href = "${contextPath}/manage/productTemplate/index.html";
            }
        });

        $("#ok").on("click", function () {

            if(allId.length<1){
                showTipDialog("产品模版至少需要包含一个平台产品");
                return;
            }

            var productTemplateId = $("input[name='templateId']").val();
            var productTemplate = {
                id: productTemplateId,
                name: $("input[name='name']").val()
            };

            var platformProductTemplateMaps = [];

            for(var i=0; i<allId.length; i++){
                var item = {};
                item["productTemplateId"] = productTemplateId;
                item["platformProductId"] = allId[i];
                platformProductTemplateMaps.push(item);
            }

            var formData = {
                productTemplate: JSON.stringify(productTemplate),
                platformProductTemplateMaps: JSON.stringify(platformProductTemplateMaps)
            };
            $.ajax({
                type: 'post',
                secureuri: false,
                data: formData,
                url: "${contextPath}/manage/productTemplate/saveAjax.html?${_csrf.parameterName}=${_csrf.token}",
                dataType: 'json',
                success: function (ret) {
                    $("#tips").val(ret.result);
                    $("#tips").html(ret.msg);
                    $("#tips-dialog").modal("show");
                },
                error: function () {
                    $("#tips").val("false");
                    $("#tips").html("网络出错，请重新尝试");
                    $("#tips-dialog").modal("show");
                }
            });
        });
    }

    function addProduct(btnEle) {

        var spId = btnEle.data("id");
        allId.push(spId);
        var row = tableData[spId];
        appendProduct(row);
        $("#productIds").val(allId);
        $("#search-btn2").click();
    }

    function appendProduct(data) {
        var parent = $("#listData1 tbody");
        var trs = $('<tr data-id="' + data["id"] + '">' +
                '<td>' + ispFormat(0, 0, data) + '</td>' +
                '<td>' + data["name"] +
                '</td><td>' + data["productCode"] +
                '</td><td>' + sizeFormat(0, 0, data) +
                '</td><td>' + priceFormat(0, 0, data) +
                '</td><td>' + productStatusFormat(0, 0, data) +
                '</td><td>' + data["ownershipRegion"] +
                '</td><td>' + data["roamingRegion"] +
                '<td>' + '<a class="btn-icon icon-del mr-5 delete-btn" data-id="' + data["id"] + '" href="#">删除</a>' +
                '<a class="btn-icon icon-check mr-5" href="${contextPath}/manage/supplierProduct/platFormProductDetail.html?productId=' + data["id"] + '">查看</a></td>' +
                '</tr>');
        parent.append(trs);
    }


    function deleteProduct(btnEle) {
        var spId = btnEle.data("id");
        allId.splice($.inArray(spId, allId), 1);
        $("#productIds").val(allId);
        btnEle.parent().parent().remove();
        $("#search-btn2").click();
    }

    function tableList(trs) {
        var listData = [];
        trs.each(function () {
            var proId = $(this).data("id");
            listData.push(tableData[proId]);
        });
        return listData;
    }
    
    function saveRelation(){

    }
</script>
</html>