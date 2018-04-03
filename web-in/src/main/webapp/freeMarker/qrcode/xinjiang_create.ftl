<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新增流量券</title>
    <meta name="keywords" content="流量平台 新增流量券"/>
    <meta name="description" content="流量平台 新增流量券"/>

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
            width: 750px !important;
        }

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

        .form .promot {
            display: block;
            padding-left: 86px;
            font-size: 12px;
        }

        #balance {
            padding: 6px 0;
        }

        .error-tip {
            display: block;
            padding-left: 86px;
        }
        
        .error-tip-cmProdSize {
            display: block;
            color: red;
        }
    </style>

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>新增二维码
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/qrcode/index.html'">返回</a>
        </h3>
    </div>

    <form id="dataForm" class="form"
          action="${contextPath}/manage/qrcode/preView.html?${_csrf.parameterName}=${_csrf.token}" method="post"
          enctype="multipart/form-data">
        <div class="tile mt-30">
            <div class="tile-header">
                活动信息
            </div>
            <div class="tile-content">
                <div class="row form">

                    <div class="col-md-6">
                        <input type="hidden" class="form-control" name="cmProductId" id="cmProductId">
                        <input type="hidden" class="form-control" name="cuProductId" id="cuProductId">
                        <input type="hidden" class="form-control" name="ctProductId" id="ctProductId">

                        <input type="hidden" class="form-control" name="cmMobileList" id="cmMobileList">
                        <input type="hidden" class="form-control" name="cuMobileList" id="cuMobileList">
                        <input type="hidden" class="form-control" name="ctMobileList" id="ctMobileList">
                        
                        <input type="hidden" class="form-control" name="flowSize" id="flowSize">

                        <input type="hidden" class="form-control" name="invalidMobiles" id="invalidMobiles">

                        <div class="form-group">
                            <label style="font-weight:700 ">企业名称：</label>
                            <div class="btn-group btn-group-sm enterprise-select">
                                <input style="width: 0px; font-size: 0; height: 0px; padding: 0;  opacity: 0"
                                       class="form-control searchItem"
                                       name="entId" id="entId" value="" required>
                                <button type="button" class="btn btn-default" style="width: 299px">请选择企业</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                <#if enterprises?? && enterprises?size!=0>
                                    <#list enterprises as e>
                                        <li data-value="${e.id!}"><a href="#">${e.name!}</a></li>
                                    </#list>
                                <#else>
                                    <li data-value=""><a href="#">没有可选的企业</a></li>
                                </#if>
                                </ul>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>活动名称：</label>
                            <input type="text" name="name" id="name" required>
                        </div>

                        <div class="form-group">
                            <label>产品总数：</label>
                            <input type="text" name="prizeCount" id="prizeCount" required>
                            <span class="promot">产品总数支持8位正整数</span>
                        </div>

                        <div class="form-group">
                            <label style="font-weight:700 ">名单类型：</label>
                            <div class="btn-group btn-group-sm phoneList-select">
                                <input style="width: 0; font-size: 0; height: 0; padding: 0; opacity: 0"
                                       class="form-control searchItem"
                                       name="hasWhiteOrBlack" id="hasWhiteOrBlack" value="0" required>
                                <button type="button" class="btn btn-default" style="width: 299px">无黑白名单</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li data-value="0"><a href="#">无黑白名单</a></li>
                                    <li data-value="1"><a href="#">白名单</a></li>
                                    <li data-value="2"><a href="#">黑名单</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="form-group" style="height: 32px; line-height: 32px;">
                            <label>企业余额：</label>
                            <span id="balance">&nbsp;</span>
                        </div>

                        <div class="form-group">
                            <label>活动时间：</label>
                                <span id="activeTime" class="daterange-wrap">
                                    <input type="text" name="startTime" id="startTime" value="" readonly="readonly"
                                           style="width: 152px;">&nbsp;至
                                    <input type="text" id="endTime" name="endTime" value="" readonly="readonly" required
                                           style="width: 152px;">
                                    <label class="error error-tip" id="time_tip"></label>
                                </span>
                        </div>

                        <div class="form-group">
                            <label>选择尺寸：</label>
                            <div class="btn-group btn-group-sm qrcodeSize-select">
                                <input style="width: 0; font-size: 0; height: 0; padding: 0; opacity: 0"
                                       class="form-control searchItem"
                                       name="qrcodeSize" id="qrcodeSize" value="" required>
                                <button type="button" class="btn btn-default" style="width: 299px">选择二维码边长</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li data-value=""><a href="#">选择二维码边长</a></li>
                                    <li data-value="1"><a href="#">8cm</a></li>
                                    <li data-value="2"><a href="#">12cm</a></li>
                                    <li data-value="3"><a href="#">15cm</a></li>
                                    <li data-value="4"><a href="#">30cm</a></li>
                                    <li data-value="5"><a href="#">50cm</a></li>
                                </ul>
                            </div>
                        </div>

                        <div class="form-group" style="display: none" id="phoneList">
                            <label>黑白名单：</label>
                            <input type="text" id="phones" name="phones" value="" readOnly="true" style="width: 268px;">
                                <span class="file-box">
                                    <input type="file" name="file" id="file" class="file-helper">
                                    <a>批量上传</a>
                                </span>
                            <span class="promot">上传txt格式文件，每行一个手机号码，号码换行操作</span>
                            <p class="red text-left" style="padding-left: 86px;" id="modal_error_msg"></p>
                        </div>

                    </div>
                </div>
            </div>
        </div>

        <div class="tile mt-20" id="product">
            <div class="tile-header">
                奖品信息
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
                                <th width="10%">流量包大小</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                    <a class="btn btn-sm btn-info openProductsDialog-btn" data-type="M"
                                       style="width:70px;">移动</a>
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
                                	<div class="form-group">
                                    <input class="cmProdSize" name="cmProdSize" id="cmProdSize"
                                          style="width:80px;">MB
                                    </div>
                                </td>
                            </tr>

                           
                            </tbody>
                        </table>
                        <span class="error-tip" id="award-error"></span>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <div class="mt-30 text-center">
        <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn save-btn" id="save-btn">下一步</a>
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
                <h5 class="modal-title">产品列表</h5>
            </div>
            <div class="modal-body">
                <div class="tools row text-right">
                    <div class="col-lg-12 dataTables_filter text-right">
                        <div id="searchForm" class="form-inline searchForm" id="table_validate">
                            <input type="hidden" class="searchItem" name="type" id="ispType">
                            <input type="hidden" class="searchItem" name="enterId" id="enterId">
                            <a class="btn btn-sm " id="search-btn" href="javascript:void(0)"></a>
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
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>

    var lastHasBlackAndWhite = '';

    var productmap = {};
    var selectedProductFormat = function (value, cloumn, row) {
        productmap[row.id] = row;
        return "<a href='javascript:void(0)' onClick='selectedProduct(" + row.id + ")'>选择</a>";
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
            var size = sizefun(product.productSize);
            
            var price = pricefun(product.price);
            $("#cmProdPrice").html(price);
            $("#cmOwnership").html(product.ownershipRegion);
            $("#cmRoaming").html(product.roamingRegion);
            
            console.info(product.type);
            //产品类型是流量池，要增加产品包大小            
            if(product.type != "1"){
            	$("#cmProdSize").val(size);
            }
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


    var columns = [{name: "isp", text: "运营商", format: ispNameFormat},
        {name: "name", text: "产品名称", tip: true},
        {name: "productCode", text: "产品编码", tip: true},
        {name: "op2", text: "操作", format: selectedProductFormat}
    ];

    var action = "${contextPath}/manage/flowcard/searchProduct.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "daterangepicker", "page/list", "upload"], function () {

        //初始化日期组件
        initDateRangePicker2();

        listeners();

        //文件上传
        fileListener();

        formValidate();

        init();

        //保存
        $("#save-btn").on("click", function () {
            if ($("#dataForm").validate().form()) {
                showToast();
                //判断是否选择产品
                var cmProdId = $("#cmProductId").val();
                var cuProdId = $("#cuProductId").val();
                var ctProdId = $("#ctProductId").val();
                //流量池传递产品大小                
                var flowSize = $("#cmProdSize").val();
                $("#flowSize").val(flowSize);

                if (cmProdId == '' && cuProdId == '' && ctProdId == '') {
                    showTip("请至少选择一个产品！");
                    return false;
                }

                var cmMobiles = $("#cmMobileList").val();
                var cuMobiles = $("#cuMobileList").val();
                var ctMobiles = $("#ctMobileList").val();
                var hasWhiteOrBlack = $("#hasWhiteOrBlack").val();

                if (hasWhiteOrBlack != 0 && (cmMobiles == '' && cuMobiles == '' && ctMobiles == '')) {
                    showTip("已经选择黑白名单标示，请上传手机号");
                    return false;
                }

                $("#dataForm").submit();
            }
            return false;
        });

        initEnterpriseSelect();

        initPhoneListSelect();

        initQrcodeSizeSelect();
    });

    function initEnterpriseSelect() {
        var parent = $(".enterprise-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
    }

    function initPhoneListSelect() {
        var parent = $(".phoneList-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
    }

    function initQrcodeSizeSelect() {
        var parent = $(".qrcodeSize-select");
        var val = $("input", parent).val();
        $("li[data-value='" + val + "']", parent).children("a").click();
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

    function init() {
        $("#product").show();
        
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

    function listeners() {
        //监听企业
        $(".enterprise-select a").on("click", function () {
            var entId = $(this).parent().attr("data-value");
            getBalance(entId);
            emptyAwardProducts();
        });

        //监听流量类型选择
        $(".phoneList-select a").on("click", function () {
            var phoneListType = $(this).parent().attr("data-value");

            if (lastHasBlackAndWhite == '') {
                //第一次，初始化
                lastHasBlackAndWhite = phoneListType;
            } else {
                //切换名单类型，原上传手机号不保存
                $("#correctMobiles").val("");
                $("#invalidMobiles").val("");
                $("#cmMobileList").val("");
                $("#cuMobileList").val("");
                $("#ctMobileList").val("");
                $("#phones").val("");
                lastHasBlackAndWhite = phoneListType;
            }


            if (phoneListType == 0) {
                $("#phoneList").hide();
            }
            if (phoneListType == 1 || phoneListType == 2) {
                $("#phoneList").show();
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

    //获取企业余额
    function getBalance(entId) {
        var entId = entId;
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
                    $("#balance").html("0");
                }
            }
        });
    }
    /**
     * 获取产品信息
     * */
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
            	if($(element).attr("id") == "cmProdSize"){
            		error.addClass("error-tip-cmProdSize");
            	}else{
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
                    space: true
                },
                startTime: {
                    required: true
                },
                prizeCount: {
                    required: true,
                    max: 100000000,
                    min: 1,
                    positive: true

                },
                qrcodeSize: {
                    required: true
                },
                cmProdSize: {
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
                    space: "活动名称不允许含有空格!"
                },
                startTime: {
                    required: "请选择活动时间!"
                },
                prizeCount: {
                    required: "请填写产品总数!",
                    max: "请输入1-99999999的正整数",
                    min: "请输入1-99999999的正整数",
                    positive: "请输入1-99999999的正整数"
                },
                qrcodeSize: {
                    required: "请选择二维码尺寸!"
                },
                cmProdSize: {
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
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
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