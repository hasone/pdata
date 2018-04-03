<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-编辑二维码</title>
    <meta name="keywords" content="流量平台 编辑二维码"/>
    <meta name="description" content="流量平台 编辑二维码"/>

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
        .modal-dialog {
            width: 800px !important;
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
    </style>

</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>编辑二维码
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/qrcode/index.html'">返回</a>
        </h3>
    </div>

    <form id="dataForm" class="form"
          action="${contextPath}/manage/qrcode/preView.html?${_csrf.parameterName}=${_csrf.token}"
          method="post"
          enctype="multipart/form-data">
        <div class="tile mt-30">
            <div class="tile-header">
                活动信息
            </div>
            <div class="tile-content">
                <div class="row form">

                    <div class="col-md-6">
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
                        
                        <input type="hidden" class="form-control" name="cmPrdSize" id="cmPrdSize">
                        <input type="hidden" class="form-control" name="cuPrdSize" id="cuPrdSize">
                        <input type="hidden" class="form-control" name="ctPrdSize" id="ctPrdSize">
                                                
                        <input type="hidden" class="form-control" name="cmProductType" id="cmProductType">
                        <input type="hidden" class="form-control" name="cuProductType" id="cuProductType">
                        <input type="hidden" class="form-control" name="ctProductType" id="ctProductType">

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

                        <div class="form-group">
                            <label>产品总数：</label>
                            <input type="text" name="prizeCount" id="prizeCount"
                                   value="${activityInfo.prizeCount!}"
                                   required>
                            <span class="promot">产品总数支持8位正整数</span>
                        </div>

                        <div class="form-group">
                            <label style="font-weight:700 ">名单类型：</label>
                            <div class="btn-group btn-group-sm phoneList-select">
                                <input style="width: 0; font-size: 0; height: 0; padding: 0; opacity: 0"
                                       class="form-control searchItem"
                                       name="hasWhiteOrBlack" id="hasWhiteOrBlack"
                                       value="${activityInfo.hasWhiteOrBlack!}" required>
                                <button type="button" class="btn btn-default" style="width: 299px">
                                    &nbsp;</button>
                                <button type="button" class="btn btn-default dropdown-toggle"
                                        data-toggle="dropdown"
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

                    <#if cqFlag??&&cqFlag==0>
                        <div class="form-group" style="line-height: 32px;">
                            <label>企业余额：</label>
                            <span id="balance"></span>
                        </div>
                    </#if>

                        <div class="form-group">
                            <label>活动时间：</label>
                                <span id="activeTime" class="daterange-wrap">
                                    <input type="text" name="startTime" id="startTime"
                                           value="${(activities.startTime?date)!}"
                                           readonly="readonly"
                                           style="width: 152px">&nbsp;至
                                    <input type="text" id="endTime" name="endTime"
                                           value="${(activities.endTime?date)!}"
                                           readonly="readonly" required style="width: 152px">
                                    <label class="error error-tip" id="time_tip"></label>
                                </span>
                        </div>

                        <div class="form-group">
                            <label>选择尺寸：</label>
                            <div class="btn-group btn-group-sm qrcodeSize-select">
                                <input style="width: 0; font-size: 0; height: 0; padding: 0; opacity: 0"
                                       class="form-control searchItem"
                                       name="qrcodeSize" id="qrcodeSize"
                                       value="${activityInfo.qrcodeSize!}" required>
                                <button type="button" class="btn btn-default" style="width: 299px">
                                    &nbsp;</button>
                                <button type="button" class="btn btn-default dropdown-toggle"
                                        data-toggle="dropdown"
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

                    </div>
                </div>
            </div>
        </div>

        <div class="tile mt-30">
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

                            <#if cqFlag??&&cqFlag==0>
                                <th width="10%">售出价格</th>
                            </#if>

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
                                    <div id="cmPackageAccount">
                                    <span class="cmProdSize" name="cmProdSize" id="cmProdSize"
                                          style="width:80px;"></span>
                                    </div>
                                    
                                    <div id="cmFlowAccount" style="display:none">
                                        <input class="cmProdSize" name="cmProdSize_flowaccount"
                                               id="cmProdSize_flowaccount"
                                               style="width:80px;">MB
                                    </div>
                                    <div id="cmFeeAccount" class="form-group" style="display:none">
                                        <input class="cmProdSize" name="cmProdSize_feeAccount"
                                               id="cmProdSize_feeAccount"
                                               style="width:80px;">元
                                    </div>
                                </td>

                            <#if cqFlag??&&cqFlag==0>
                                <td>
                                    <span class="cmProdPrice" name="cmProdPrice" id="cmProdPrice"
                                          style="width:80px;"></span>
                                </td>
                            </#if>

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
                                    <div id="cuPackageAccount">
                                    <span class="cuProdSize" name="cuProdSize" id="cuProdSize"
                                          style="width:80px;"></span>
                                    </div>
                                    
                                    <div id="cuFlowAccount" style="display:none">
                                        <input class="cuProdSize" name="cuProdSize_flowaccount"
                                               id="cuProdSize_flowaccount"
                                               style="width:80px;">MB
                                    </div>
                                    <div id="cuFeeAccount" class="form-group" style="display:none">
                                        <input class="cuProdSize" name="cuProdSize_feeAccount"
                                               id="cuProdSize_feeAccount"
                                               style="width:80px;">元
                                    </div>
                                </td>


                                <#if cqFlag??&&cqFlag==0>
                                    <td>
                                    <span class="cuProdPrice" name="cuProdPrice" id="cuProdPrice"
                                          style="width:80px;"></span>
                                    </td>
                                </#if>

                                <td>
                                    <span class="cOwnership" name="cuOwnership" id="cuOwnership"
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
                                    <div id="ctPackageAccount">
                                    <span class="ctProdSize" name="ctProdSize" id="ctProdSize"
                                          style="width:80px;"></span>
                                    </div>
                                    
                                    <div id="ctFlowAccount" style="display:none">
                                        <input class="ctProdSize" name="ctProdSize_flowaccount"
                                               id="ctProdSize_flowaccount"
                                               style="width:80px;">MB
                                    </div>
                                    <div id="ctFeeAccount" class="form-group" style="display:none">
                                        <input class="ctProdSize" name="ctProdSize_feeAccount"
                                               id="ctProdSize_feeAccount"
                                               style="width:80px;">元
                                    </div>
                                    </td>

                            	<#if cqFlag??&&cqFlag==0>
                                	<td>
                                    <span class="ctProdPrice" name="ctProdPrice" id="ctProdPrice"
                                          style="width:80px;"></span>
                                </td>
                            	</#if>
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

    <div class="tile mt-30" style="display:none" id="upload-dialog">
        <div class="tile-header">
            黑白名单
        </div>
        <div class="tile-content">
            <div class="">
                <label style="width: 50px;">名单：</label>
                    <span id="download">
                        <span class="file-box">
                            <a class='btn-icon icon-detail mr-5'
                               href='${contextPath}/manage/flowcard/downloadPhones.html?activitiesId=${activities.activityId!}&activityType=6'>下载原手机号码</a>
                        </span>
                    </span>
                    <span>
                        <input type="hidden" id="phones" name="phones" value="" readOnly="true">
                        <span class="file-box">
                            <input type="file" name="file" id="file" class="file-helper">
                            <a id="upload-name">重新上传</a>
                        </span>
                        <span>上传txt格式文件，每行一个手机号码，号码换行操作</span>
                    </span>
                <p class="red text-left" style="padding-left: 83px;" id="modal_error_msg"></p>
            </div>
        </div>
    </div>

    <div class="mt-30 text-center">
        <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn save-btn"
           id="save-btn">保存</a>
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
                <div role="table" style="max-height: 200px; overflow: auto;"></div>
            </div>
        </div>
    </div>
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
    //用于判断显示那个上传框，如果进入编辑页，初始时黑白名单，则显示和新建一样的上传框
    var oldHasBlackAndWhite = ${activityInfo.hasWhiteOrBlack!};

    var lastHasBlackAndWhite = ${activityInfo.hasWhiteOrBlack!};

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
        var cqFlag = "${cqFlag}";
        $("#products-dialog").modal("hide");
        
        //渲染对应的运营商数据
        if (type == "M") {//移动运营商
            $("#cmProductId").val(product.id);
            $("#cmProdName").html(product.name);
            $("#cmProdCode").html(product.productCode);
        	$("#cmProdSize_flowaccount").val('');         	

            //记录下产品类型
            $("#cmProductType").val(product.type);
            //流量池产品，设置为输入框
            if (product.type == '1') {//流量池产品
                $("#cmFlowAccount").show();
                $("#cmPackageAccount").hide();
                $("#cmFeeAccount").hide();

                $("#cmProdPrice").html('-');
            } else if (product.type == '3') {//话费产品
                $("#cmFlowAccount").hide();
                $("#cmPackageAccount").hide();
                $("#cmFeeAccount").show();
                $("#cmProdPrice").html('-');
                $("#cmFeeAccount").children("input").on("input", function () {
                    $("#cmProdPrice").html(pricefun($(this).val() * 100))
                });
            } else {//流量包产品
                $("#cmFlowAccount").hide();
                $("#cmPackageAccount").show();
                $("#cmFeeAccount").hide();

                var size = sizefun(product.productSize);
                $("#cmProdSize").html(size);

                if (cqFlag == 0) {
                    var price = pricefun(product.price);
                    $("#cmProdPrice").html(price);
                }
            }

            $("#cmOwnership").html(product.ownershipRegion);
            $("#cmRoaming").html(product.roamingRegion);
        }

        if (type == "T") {//电信运营商
            $("#ctProductId").val(product.id);
            $("#ctProdName").html(product.name);
            $("#ctProdCode").html(product.productCode);
            $("#ctProdSize_flowaccount").val('');         	
                        
            //记录下产品类型
            $("#ctProductType").val(product.type);
            //流量池产品，设置为输入框
            if (product.type == '1') {//流量池产品
                $("#ctFlowAccount").show();//流量池产品大小输入框
                $("#ctPackageAccount").hide();//流量包数量输入框
                $("#ctFeeAccount").hide();//话费产品大小输入框
                $("#ctProdPrice").html('-');
            } else if (product.type == '3') {//话费产品
                $("#ctFlowAccount").hide();
                $("#ctPackageAccount").hide();
                $("#ctFeeAccount").show();
                $("#ctProdPrice").html('-');
                $("#ctFeeAccount").children("input").on("input", function () {
                $("#ctProdPrice").html('-');
                });
            } else {//流量包产品
                $("#ctFlowAccount").hide();
                $("#ctPackageAccount").show();
                $("#ctFeeAccount").hide();

                var size = sizefun(product.productSize);
                $("#ctProdSize").html(size);

                if (cqFlag == 0) {
                    var price = pricefun(product.price);
                    $("#ctProdPrice").html(price);
                }else {
                	$("#ctProdPrice").html(product.count);
            	}
            }

            $("#ctOwnership").html(product.ownershipRegion);
            $("#ctRoaming").html(product.roamingRegion);
            
            
            
 /*           var size = sizefun(product.productSize);
            
            $("#ctProdSize").html(size);
            if (cqFlag == 0) {
                var price = pricefun(product.price);
                $("#ctProdPrice").html(price);
            } else {
                $("#ctProdPrice").html(product.count);
            }
            $("#ctOwnership").html(product.ownershipRegion);
            $("#ctRoaming").html(product.roamingRegion);
*/            
        }

        if (type == "U") {//联通运营商
            $("#cuProductId").val(product.id);
            $("#cuProdName").html(product.name);
            $("#cuProdCode").html(product.productCode);
            $("#cuProdSize_flowaccount").val('');         	
            
             //记录下产品类型
            $("#cuProductType").val(product.type);
            //流量池产品，设置为输入框
            if (product.type == '1') {//流量池产品
                $("#cuFlowAccount").show();//流量池产品大小输入框
                $("#cuPackageAccount").hide();//流量包数量输入框
                $("#cuFeeAccount").hide();//话费产品大小输入框
                $("#cuProdPrice").html('-');
            } else if (product.type == '3') {//话费产品
                $("#cuFlowAccount").hide();
                $("#cuPackageAccount").hide();
                $("#cuFeeAccount").show();
                $("#cuProdPrice").html('-');
                $("#cuFeeAccount").children("input").on("input", function () {
                    $("#cuProdPrice").html(pricefun($(this).val() * 100))
                });
            } else {//流量包产品
                $("#cuFlowAccount").hide();
                $("#cuPackageAccount").show();
                $("#cuFeeAccount").hide();

                var size = sizefun(product.productSize);
                $("#cuProdSize").html(size);

                if (cqFlag == 0) {
                    var price = pricefun(product.price);
                    $("#cuProdPrice").html(price);
                }else {
                	$("#cuProdPrice").html(product.count);
            	}
            }

            $("#cuOwnership").html(product.ownershipRegion);
            $("#cuRoaming").html(product.roamingRegion);
            
/*            var size = sizefun(product.productSize);
            $("#cuProdSize").html(size);
            if (cqFlag == 0) {
                var price = pricefun(product.price);
                $("#cuProdPrice").html(price);
            } else {
                $("#cuProdPrice").html(product.count);
            }
            $("#cuOwnership").html(product.ownershipRegion);
            $("#cuRoaming").html(product.roamingRegion);
*/
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
        if (row.type == '1') {
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
        if (row.type == '1') {
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
        else if(row.type == 6) {
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


    <#if cqFlag??&&cqFlag==0>
        {name: "price", text: "售出价格(元)", format: priceFormat},
    </#if>

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
                        window.location.href = "${contextPath}/manage/qrcode/index.html";
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

                        var parent = $(".phoneList-select");
                        var val = $("input", parent).val();

                        var correctMobiles = $("#correctMobiles").val();

						//判断是否选择产品
                		var cmProdId = $("#cmProductId").val();
                		var cuProdId = $("#cuProductId").val();
               			var ctProdId = $("#ctProductId").val();              

		                if (cmProdId == '' && cuProdId == '' && ctProdId == '') {
		                    showTip("请至少选择一个产品！");
		                    return false;
		                }
						
                        //移动
                        var cmProductType = $("#cmProductType").val();
                        if (!isEmpty(cmProductType) && cmProductType == 1) {//流量池产品
                            var cmPrdSize = $("#cmProdSize_flowaccount").val() * 1024;
                            $("#cmPrdSize").val(cmPrdSize);
                        } else if(!isEmpty(cmProductType) && cmProductType == 2) {//流量包产品
                            var cmPrdSize = $("#cmProdSize_flowaccount").val() * 1024;
                            $("#cmPrdSize").val(cmPrdSize);
                        } else if (!isEmpty(cmProductType) && cmProductType == 3) {//话费产品
                            var cmPrdSize = $("#cmProdSize_flowaccount").val() * 100;
                            $("#cmPrdSize").val(cmPrdSize);
                        }
                                     
                        //联通
                        var cuProductType = $("#cuProductType").val();

                        if (!isEmpty(cuProductType) && cuProductType == '1') {//流量池产品
                            var cuPrdSize = $("#cuProdSize_flowaccount").val() * 1024;
                            $("#cuPrdSize").val(cuPrdSize);
                        } else if(!isEmpty(cuProductType) && cuProductType == '2') {//流量包产品
                            var cuPrdSize = $("#cuProdSize_flowaccount").val() * 1024;
                            $("#cuPrdSize").val(cuPrdSize);
                        } else if (!isEmpty(cuProductType) && cuProductType == '3') {//话费产品
                            var cuPrdSize = $("#cuProdSize_flowaccount").val() * 100;
                            $("#cuPrdSize").val(cuPrdSize);
                        }
                     
                        //电信
                        var ctProductType = $("#ctProductType").val();
                        if (!isEmpty(ctProductType) && ctProductType == 1) {//流量池产品
                            var ctPrdSize = $("#ctProdSize_flowaccount").val() * 1024;
                            $("#ctPrdSize").val(ctPrdSize);
                        } else if(!isEmpty(ctProductType) && ctProductType == '2') {//流量包产品
                            var ctPrdSize = $("#ctProdSize_flowaccount").val() * 1024;
                            $("#ctPrdSize").val(ctPrdSize);
                        } else if (!isEmpty(ctProductType) && ctProductType == '3') {//话费产品
                            var ctPrdSize = $("#ctProdSize_flowaccount").val() * 100;
                            $("#ctPrdSize").val(ctPrdSize);
                        }
						
		                if (val != 0 && val != oldHasBlackAndWhite) {
                            if (correctMobiles == "") {
                                showTip("已选择了启用黑白名单，请上传手机号!");
                                return false;
                            }
                        }

                        //开始保存
                        var activity = {
                            activityId: "${activities.activityId!}",
                            name: $("input[name='name']").val(),
                            entId: $("#entId").val(),
                            startTime: $("input[name='startTime']").val(),
                            endTime: $("input[name='endTime']").val()
                        };

                        var activityInfo = {
                            prizeCount : $("#prizeCount").val(),
                            hasWhiteOrBlack : $("#hasWhiteOrBlack").val(),
                            qrcodeSize : $("#qrcodeSize").val()
                        }

                        //mb转kb
                        var productSize = $("#prdSize").val();
                        if(productSize && productSize!=''){
                            productSize = parseInt(productSize)*1024;
                        }

                        var formData = {
                            activity: JSON.stringify(activity),
                            activityInfo: JSON.stringify(activityInfo),
                            cmProductId: cmProdId,
                            cuProductId: cuProdId,
                            ctProductId: ctProdId,
                            correctMobiles: correctMobiles,
                            cmPrdSize: $("#cmPrdSize").val(),
                            cuPrdSize: $("#cuPrdSize").val(),
                            ctPrdSize: $("#ctPrdSize").val()
                        };

                        $("#save-btn").addClass("disabled");

                        ajaxData("${contextPath}/manage/qrcode/saveAjax.html", formData,
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

                initPhoneListSelect();

                initQrcodeSizeSelect();

            });

    function convert(str) {
        if (!str || str.length < 2) {
            return;
        }

        var suffix = str.substring(str.length - 2);
        var value = str.substring(0, str.length - 2);
        if (suffix === "KB") {
            return value;
        } else if (suffix == "MB") {
            return value * 1024;
        } else if (suffix == "GB") {
            return value * 1024 * 1024;
        }
    }

    function initEnterpriseSelect() {
        $(function () {
            var parent = $(".enterprise-select");
            var val = $("input", parent).val();
            $("li[data-value='" + val + "']", parent).children("a").click();
            getBalance(val);
        });
    }

    function initPhoneListSelect() {
        $(function () {
            var parent = $(".phoneList-select");
            var val = $("input", parent).val();
            $("li[data-value='" + val + "']", parent).children("a").click();
        });
    }

    function initQrcodeSizeSelect() {
        $(function () {
            var parent = $(".qrcodeSize-select");
            var val = $("input", parent).val();
            $("li[data-value='" + val + "']", parent).children("a").click();
        });
    }

    /**
     * 获取产品信息
     * */
    function init() {
        $("#product").show();
        $("#cuProdPrice").empty();
        $("#ctProdPrice").empty();
       	$("#cmProdPrice").empty();
        
        if ($("#cmProductType").val() == '1') {//移动流量池产品     
            $("#cmFlowAccount").show();
            $("#cmPackageAccount").hide();
            $("#cmFeeAccount").hide();
            $("#cmProdPrice").html('-');
        } else if ($("#cmProductType").val() == '3') {//移动话费产品
            $("#cmFlowAccount").hide();
            $("#cmPackageAccount").hide();
            $("#cmFeeAccount").show();
            $("#cmProdPrice").html('-');
        } else {
            $("#cmFlowAccount").hide();
            $("#cmPackageAccount").show();
            $("#cmFeeAccount").hide();
        }
        
        if ($("#cuProductType").val() == '1') {//联通流量池产品     
            $("#cuFlowAccount").show();
            $("#cuPackageAccount").hide();
            $("#cuFeeAccount").hide();
            $("#cuProdPrice").html('-');
        } else if ($("#cuProductType").val() == '3') {//联通话费产品
            $("#cuFlowAccount").hide();
            $("#cuPackageAccount").hide();
            $("#cuFeeAccount").show();
            $("#cuProdPrice").html('-');
        } else {
            $("#cuFlowAccount").hide();
            $("#cuPackageAccount").show();
            $("#cuFeeAccount").hide();
        }

        if ($("#ctProductType").val() == '1') {//电信流量池产品
            $("#ctFlowAccount").show();
            $("#ctPackageAccount").hide();
            $("#ctFeeAccount").hide();
             $("#ctProdPrice").html('-');
        } else if ($("#ctProductType").val() == '3') {//电信话费产品
            $("#ctFlowAccount").hide();
            $("#ctPackageAccount").hide();
            $("#ctFeeAccount").show();
            $("#ctProdPrice").html('-');
        } else {
            $("#ctFlowAccount").hide();
            $("#ctPackageAccount").show();
            $("#ctFeeAccount").hide();
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
                   url: "${contextPath}/manage/qrcode/getProductAjax.html?${_csrf.parameterName}=${_csrf.token}",
                   dataType: "json", //指定服务器的数据返回类型，
                   success: function (res) {
                       if (res.success && res.success == "success") {
                           if (res.isp == "M") {
                               $("#cmProductId").val(res.productId);
                               $("#cmProdName").html(res.productName);
                               $("#cmProdCode").html(res.productCode);
                               $("#cmProdSize").html(res.productSize);
                               $("#cmProdPrice").html(res.productPrice)
                               $("#cmPrdPrice").html(res.productPrice);
                               $("#cmProdSize_flowaccount").val(res.size);
                               if (res.type == 1 || res.type == 3) {               
                                   $("#cmProdPrice").html('-');
                               }
                               $("#cmOwnership").html(res.ownership);
                               $("#cmRoaming").html(res.roaming);
                               $("#cmProductType").val(res.type);
                           }

                           if (res.isp == "T") {
                               $("#ctProductId").val(res.productId);
                               $("#ctProdName").html(res.productName);
                               $("#ctProdCode").html(res.productCode);
                               $("#ctProdSize").html(res.productSize);
                               $("#ctProdPrice").html(res.productPrice);
                               $("#ctPrdPrice").html(res.productPrice);
                               $("#ctProdSize_flowaccount").val(res.size);
                               if (res.type == '1' || res.type == '3') {
                                   $("#ctProdPrice").html('-');
                                  
                               }
                               $("#ctOwnership").html(res.ownership);
                               $("#ctRoaming").html(res.roaming);
                               $("#ctProductType").val(res.type);
                           }

                           if (res.isp == "U") {
                               $("#cuProductId").val(res.productId);
                               $("#cuProdName").html(res.productName);
                               $("#cuProdCode").html(res.productCode);
                               $("#cuProdSize").html(res.productSize);
                               $("#cuProdPrice").html(res.productPrice);                               
                               $("#cuPrdPrice").html(res.productPrice);
                               $("#cuProdSize_flowaccount").val(res.size);                               
                               if (res.type == '1' || res.type == '3') {
                                   $("#cuProdPrice").html('-');
                               }
                               $("#cuOwnership").html(res.ownership);
                               $("#cuRoaming").html(res.roaming);
                               $("#cuProductType").val(res.type );
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

        //监听黑白名单类型
        $(".phoneList-select a").on("click", function () {
            var hasWhiteOrBlack = $(this).parent().attr("data-value");

            if (lastHasBlackAndWhite != hasWhiteOrBlack) {
                //切换名单类型，原上传手机号不保存
                $("#correctMobiles").val("");
                $("#invalidMobiles").val("");
                $("#cmMobileList").val("");
                $("#cuMobileList").val("");
                $("#ctMobileList").val("");
                $("#phones").val("");
                lastHasBlackAndWhite = hasWhiteOrBlack;
            }

            if (hasWhiteOrBlack == 0) {
                $("#upload-dialog").hide();
            }
            else {
                if (oldHasBlackAndWhite == 0) {
                    $("#download").hide();
                    $("#upload-name").html("批量上传");
                    $("#phones").attr('type', 'test');
                }
                else {
                    $("#download").show();
                }
                $("#upload-dialog").show();
            }
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
                           $("#balance").html("0");
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
                                        if ($(element).attr("id") == "cmProdSize_flowaccount" || $(element).attr("id") == "ctProdSize_flowaccount" || $(element).attr("id") == "cuProdSize_flowaccount") {
                                            error.addClass("error-tip-flowaccount");
                                        } else {
                                            error.addClass("error-tip");
                                        }
                                        if ($(element).siblings('div').hasClass('prompt')) {
                                            $(element).siblings('div.prompt').before(error);
                                        } else {
                                            $(element).parent().append(error);
                                        }
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
                                        prizeCount: {
                                            required: true,
                                            max: 100000000,
                                            min: 1,
                                            positive: true

                                        },
                                        qrcodeSize: {
                                            required: true
                                        },
                                      	cmProdSize_flowaccount: {
                                            required: true,
                                            max: ${maxProductSize},
                                            min: 1,
                                            positive: true
                                        },
                                        ctProdSize_flowaccount: {
                                            required: true,
                                            max: ${maxProductSize},
                                            min: 1,
                                            positive: true
                                        },
                                        cuProdSize_flowaccount: {
                                            required: true,
                                            max: ${maxProductSize},
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
                                        prizeCount: {
                                            required: "请填写产品总数!",
                                            max: "请输入1-99999999的正整数",
                                            min: "请输入1-99999999的正整数",
                                            positive: "请输入1-99999999的正整数"
                                        },
                                        qrcodeSize: {
                                            required: "请选择二维码尺寸!"
                                        },
                                        cmProdSize_flowaccount: {
                                            required: "请填写流量大小!",
                                            max: "请输入1-" + ${maxProductSize} +"的正整数",
                                            min: "请输入1-" + ${maxProductSize} +"的正整数",
                                            positive: "请输入1-" + ${maxProductSize} +"的正整数"
                                        },
                                        cuProdSize_flowaccount: {
                                            required: "请填写流量大小!",
                                            max: "请输入1-" + ${maxProductSize} +"的正整数",
                                            min: "请输入1-" + ${maxProductSize} +"的正整数",
                                            positive: "请输入1-" + ${maxProductSize} +"的正整数"
                                        },
                                        ctProdSize_flowaccount: {
                                            required: "请填写流量大小!",
                                            max: "请输入1-" + ${maxProductSize} +"的正整数",
                                            min: "请输入1-" + ${maxProductSize} +"的正整数",
                                            positive: "请输入1-" + ${maxProductSize} +"的正整数"
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
                                 }
                             },
                             error: function (data) {
                                 $("#modal_error_msg").html("上传文件失败");
                             }
                         });
        return false;
    }
    
    function isEmpty(value){
    	if (value == null || value == undefined || value == '') { 
			return true; 
		}else{
			return false;
		}
	} 

</script>
</body>
</html>
