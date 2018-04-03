<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品关联</title>
    <meta name="keywords" content="产品关联"/>
    <meta name="description" content="产品关联"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
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
        .modal-dialog {
            width: 850px !important;
        }
    
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
        }

        #startTime {
            width: 150px;
        }

        #endTime {
            width: 150px;
        }

        textarea {
            width: 323px;
            resize: none;
            height: 75px;
            outline: none;
            border: 1px solid #ccc;
        }

        .preview {
            width: 190px;
            height: 336px;
            border: 1px solid #ccc;
            margin: 0 auto;
        }

        .award-table td {
            position: relative;
        }

        .table > tbody > tr > td {
            padding: 8px 0 20px;
        }

        .award-table .error-tip {
            position: absolute;
            top: 35px;
            left: 0;
            right: 0;
            display: block;
            text-align: center;
            max-width: 100%;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>产品关联
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <form action="#" method="post" id="table_validate"
          enctype="multipart/form-data">
    
    <div class="tile mt-30"><!--  新增禁止关系start -->

            <div class="tile-header">
                                                               产品关联
            </div>
            
            <div class="tile-content">
                <div class="row form">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>产品：</label>
                            <span id="srcPrdName"></span>
                            <a class="btn btn-sm btn-info openProductsDialog-btn"
                                       style="width:70px;">选择</a>
                            <span>产品不能同时关联多个钱账户或池账户</span>
                        </div>
                        <div class="form-group">
                            <label>账户：</label>
                            <select name="destPrdAccount" id="destPrdAccount" onchange="chooseDestPrdAccount()">
                                <option value="">---请选择---</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <a class="btn btn-primary btn-sm" id="addItem">添 加</a>
                            <span id="addErrMsg" style="color:red;"></span>
                        </div>
                    </div>
                </div>
            </div>
    </div><!--  新增禁止关系end -->
    
    <!-- 配置列表 -->
    <div class="tile mt-30"><!--  配置列表start -->
        <div class="tile-header">
                                            配置列表
        </div>
        <div class="tile-content">
                <div class="">
                    <div class="table-responsive mt-20">
                        <table class="table table-bordered text-center award-table">
                            <thead>
                            <tr>
                                <th>供应商</th>
                                <th>产品名称</th>
                                <th>产品编码</th>
                                <th>产品大小</th>
                                <th>售出价格</th>
                                <th>关联账户</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody data-bind="foreach: prdItems">
                            <tr>
                                <td>
                                    <span id="sourceIsp" data-bind="text:sourceIsp"></span>
                                </td>
                                <td>
                                    <input type="hidden" id="sourcePrdId" data-bind="value: sourcePrdId">
                                    <span id="sourcePrdName" data-bind="text:sourcePrdName"></span>
                                </td>
                                <td>
                                    <span id="sourcePrdCode" data-bind="text:sourcePrdCode"></span>
                                </td>
                                <td>
                                    <span id="sourcePrdSize" data-bind="text:sourcePrdSize"></span>
                                </td>
                                <td>
                                    <span id="sourcePrdSize" data-bind="text:sourcePrdPrice"></span>
                                </td>
                                <td>
                                    <input type="hidden" id="destPrdId" data-bind="value: destPrdId">
                                    <span id="destPrdName" data-bind="text:destPrdName"></span>
                                </td>
                                <td>
                                    <a class="btn btn-sm btn-link" data-bind="click: $parent.removeItem">删除</a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <span class="error-tip hidden" id="award-error"></span>
                    </div>
                </div>
            </div>
    </div><!--  配置列表end -->
    
    <div class="mt-30 text-center">
            <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn">提交</a>
            <span style='color:red' id="error_msg"></span>
    </div>
    
    </from>
</div>


<div class="modal fade dialog-lg" id="products-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">产品列表</h5>
            </div>
            <div class="modal-body">
                <div class="tools row text-right">
                    <div class="col-lg-12 dataTables_filter text-right">
                        <div id="searchForm" class="form-inline searchForm" id="table_validate">
                            <div class="form-group form-group-sm" >
                               <label for="name">产品名称：</label>
                               <input type="text" name="productName" id="productName" class="form-control searchItem" autocomplete="off"
                                      placeholder="" value=""
                                      maxlength="255">
                            </div>         
                            <a class="btn btn-sm" id="search-btn" href="javascript:void(0)">查询</a>
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
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
    
<script>
    var productmap = {};
    var viewModel;
    var globalSourcePrd;//保存已选择的产品

    require(["common", "bootstrap", "daterangepicker", "knockout","page/list"], function (a, b, c, ko) {
        initViewModel(ko);
        addAwardListener();//增加addButton监听 
        listeners(); //增加saveButton监听
        initDataBase();//初始化数据
        
    });
    
    function initViewModel(ko) {
        viewModel = {
            probabilityType: ko.observable("0"),
            prdItems: ko.observableArray([]),
            removeItem: function () {
                viewModel.prdItems.remove(this);
            }
        };

        viewModel.probabilityDisplay = ko.dependentObservable({
            read: function () {
                return this.probabilityType() == "1";
            },
            owner: viewModel
        });

        ko.applyBindings(viewModel);
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
     * 选择关联账户
     */
    function chooseDestPrdAccount(){
        $("#addErrMsg").empty();
    }
    
    /**
     * 添加配置列表
     */
    function addAwardListener() {
        //点击的时候添加一行
        $("#addItem").on("click", function () {
            if(globalSourcePrd == null){
                $("#addErrMsg").empty();
                $("#addErrMsg").append("请选择产品名称");
                return false;
            }
          
            var body = $(".award-table tbody");

            var prdItems = $(".award-table tbody tr");
            
            var sourcePrdNameText = globalSourcePrd.name; 
            var destPrdNameText = $("#destPrdAccount").find("option:selected").text(); 

            var sourcePrdIdText = globalSourcePrd.id;
            var destPrdIdText = $("#destPrdAccount").val();


            if(destPrdIdText == ""){
                $("#addErrMsg").empty();
                $("#addErrMsg").append("请选择关联账户");
                return false;
            }


			var sourceIsp = globalSourcePrd.isp;            
            
            //判断是否已添加
            var award_ele = $(".award-table tbody tr");
            
            var exist = 0;
            award_ele.each(function (index) {
                
                var itemsourcePrdId = $("#sourcePrdId", this).val();
                var itemdestPrdId = $("#destPrdId", this).val();
                
                if(sourcePrdIdText == itemsourcePrdId 
                    && destPrdIdText == itemdestPrdId){
                    
                    $("#addErrMsg").empty();
                    $("#addErrMsg").append("产品已经与该账户关联");
                    exist = 1;
                    return;
                }
                
            });
            
            if(exist==0){
                console.log(globalSourcePrd);
                viewModel.prdItems.push({
                  	sourceIsp: convertIspName(globalSourcePrd.isp),      
                    sourcePrdId: sourcePrdIdText,
                    sourcePrdCode: globalSourcePrd.productCode,
                    sourcePrdSize: formatProductSize(globalSourcePrd.name,globalSourcePrd.productSize),
                    sourcePrdPrice: formatPrice(globalSourcePrd.price),
                    destPrdId: destPrdIdText,
                    sourcePrdName: sourcePrdNameText,
                    destPrdName:  destPrdNameText       
                });
            }
            
        });
    }

    function listeners() {

        $("#save-btn").on("click", function () {
            var prdItems = $(".award-table tbody tr");
            
            if ($("#table_validate").validate().form()) {
                var params = {
                    convertLists: []
                };
                
                var award_ele = $(".award-table tbody tr");
                var obj;
                if (!award_ele.length) {

                }
                else{
                    award_ele.each(function (index) {
                        obj = {
                            sourcePrdId: $("#sourcePrdId", this).val(),
                            destPrdId: $("#destPrdId", this).val(),
                        };
                        
                        params.convertLists.push(obj);
                    });   
                }
                
                
                var paramurl = JSON.stringify(params);
                //alert(paramurl);
                
                $.ajax({
                    type: "GET",
                    url: "${contextPath}/manage/productConverter/editSubmitAjax.html",
                    data: {
                        converterPage: paramurl
                    },
                    dataType: "json",
                    asyc: true,
                    success: function (ret) {
                        if(ret.success == "true"){
                            //跳转或刷新
                            window.location.href = "${contextPath}/manage/productConverter/index.html";
                        }
                        else{
                            alert(ret.message);
                        }
                        
                    },
                    error: function () {
                        alert("网络错误");
                    }
                });
                
                
            }
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
    }


     function initDataBase(){
        $.ajax({
            type: "GET",
            url: "${contextPath}/manage/productConverter/initConverterData.html",
            dataType: "json", //指定服务器的数据返回类型，
            asyc: true,
            success: function (data) {
                var convertLists= data.convertLists;
                for(var i =0;i<convertLists.length ; i++){
                    var param = convertLists[i];
                    
                    viewModel.prdItems.push({
                        sourceIsp: convertIspName(param.sourceIsp),
                        sourcePrdId: param.sourcePrdId,
                        sourcePrdCode: param.sourcePrdCode,
                        sourcePrdSize: formatProductSize(param.sourcePrdName,param.sourcePrdSize),
                        sourcePrdPrice: formatPrice(param.sourcePrdPrice),
                        destPrdId:  param.destPrdId,
                        sourcePrdName: param.sourcePrdName,
                        destPrdName:  param.destPrdName      
                    });
                }
            },
            error: function () {
                alert("网络错误");
            }
        });
    }


    function getAvailDestPrds(){
        $("#srcPrdName").empty();
        $("#srcPrdName").append(globalSourcePrd.name);
        //var sourceId = $("#sourcePrdAccount").val();
        var sourceId = globalSourcePrd.id;
        $.ajax({
            type: "GET",
            url: "${contextPath}/manage/productConverter/getAvailDestPrds.html",
            data:{
                sourcePrdId : sourceId
            },
            dataType: "json", //指定服务器的数据返回类型，
            asyc: true,
            success: function (data) {
               if(data.success == "true"){
                    $("#destPrdAccount").empty();
               
                    var destPrds = data.result;
                    $("#destPrdAccount").append("<option value=''>---请选择---</option>");
                    if(destPrds.length==0){
                         
                    }
                    else{
                         for(var i=0 ; i<destPrds.length ;i++){
                            $("#destPrdAccount").append("<option value='"+destPrds[i].id+"'>"+destPrds[i].name+"</option>");
                         } 
                    }
               }
               else{
                    alert("选择的源产品错误");
               }
            },
            error: function () {
                alert("网络错误");
            }
        });
    }
    
    var selectedProductFormat = function (value, cloumn, row) {
        $("#addErrMsg").empty();
        productmap[row.id] = row;
        return "<a href='javascript:void(0)' onClick='selectedProduct(" + row.id + ")'>选择</a>";
    };
    
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

    var productSizeFormat = function (value, coloumn, row) {
        if (row.type == '1') {
            return "-";
        }
        
        if (row.type == '3') {
            return "-";
        }
        
        if (row.productSize == null) {
            return "-";
        }
        
        var name = row.name;
        if(name.indexOf("流量池") >= 0){
            return "-";
        }
        
        if(name.indexOf("话费") >= 0){
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
    
    function formatProductSize(name,size){
        if (size == null) {
            return "-";
        }
        if(name.indexOf("流量池") >= 0){
            return "-";
        }
        if(name.indexOf("话费") >= 0){
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
    }
    
    var priceFormat = function (value, cloumn, row) {

        var price = row.price;
        if (price != 0) {
            return (price / 100.00).toFixed(2) + "";
        }
        return "-";
    };

    function formatPrice(price){
        if (price != 0) {
            return (price / 100.00).toFixed(2) + "";
        }
        return "-";
    }
    
     var countFormat = function (value, coloumn, row) {
        if (row.type == 2) {
            return row.count;
        }
        else {
            return "-";
        }
    };
    
    /**
     * 渲染选中的产品信息
     */
    function selectedProduct(id) {
        var product = productmap[id];
        globalSourcePrd = product;
        
        $("#sourcePrdAccount").empty();
        $("#sourcePrdAccount").append();
        $("#sourcePrdAccount").append("<option value='"+product.id+"'>"+product.name+"</option>");
        getAvailDestPrds();
        
        $("#products-dialog").modal("hide");
        
    }
    
    var columns = [{name: "isp", text: "运营商", format: ispNameFormat},
        {name: "name", text: "产品名称", tip: true},
        {name: "productCode", text: "产品编码", tip: true},
        {name: "productSize", text: "产品大小", format: productSizeFormat},
        {name: "price", text: "售出价格", format: priceFormat},
        {name: "ownershipRegion", text: "使用范围"},
        {name: "roamingRegion", text: "漫游范围"},
        {name: "count", text: "数量", format: countFormat},
        {name: "op2", text: "操作", format: selectedProductFormat}
    ];

    var action = "${contextPath}/manage/productConverter/searchProduct.html?${_csrf.parameterName}=${_csrf.token}";

    
    function convertIspName(isp){
        if (isp == "M") {
            return "移动";
        }
        if (isp == "T") {
            return "电信";
        }
        if (isp == "U") {
            return "联通";
        }
        
        return isp;
    }
</script>

</body>
</html>