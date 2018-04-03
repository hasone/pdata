<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品变更</title>
    <meta name="keywords" content="流量平台 产品变更"/>
    <meta name="description" content="流量平台 产品变更"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 350px;
            text-align: left;
        }

        .form label {
            width: 70px;
            text-align: right;
            margin-right: 10px;
        }

        .form .form-control {
            width: 230px;
        }

        .lg-btn-input {
            width: 205px;
            text-align: left;
        }

        .dropdown-menu {
            left: 0;
            right: auto;
            width: 100%;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>产品变更编辑
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-content">
            <div class="row">
                <div class="col-md-6 col-md-offset-1">
                    <div class="form-group">
                        <span class="detail-label">企业名称:</span>
                        <span class="detail-value">${enterprise.name!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">企业编码:</span>
                        <span class="detail-value">${enterprise.code}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">所属地区:</span>
                        <span class="detail-value">${enterprise.provinceName!}${enterprise.cityName!}${enterprise.districtName!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">创建时间:</span>
                        <span class="detail-value">${enterprise.createTime?datetime}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            审批意见
        </div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-8 col-md-offset-2">
                    <table class="table text-center table-striped">
                        <thead>
                        <tr>
                            <th>时间</th>
                            <th>角色</th>
                            <th>用户名</th>
                            <th>意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if approvalRecords??>
                            <#list approvalRecords as record>
                            <tr>
                                <td>${record.createTime?datetime}</td>
                                <td>${record.roleName!}</td>
                                <td>${record.userName!}</td>
                                <td title="${record.operatorComment!}">${record.operatorComment!}</td>
                            </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            企业产品列表
        </div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-8 col-md-offset-2">
                    <table class="table text-center table-striped" id="productTable">
                        <thead>
                        <tr>
                            <th>产品名称</th>
                            <th>产品编码</th>
                            <th>售出价格</th>
                            <th>折扣</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody data-bind="foreach: products">
                        <tr>
                            <td>
                                <select class="productName" name="productName" data-bind="value: productName">
                                </select>
                            </td>
                            <td data-bind="text: productCode" class="productCode" name="productCode"></td>
                            <td data-bind="text: productPrize" class="productPrize" name="productPrize"></td>
                            <td>
                                <input data-bind="value: discount" class="discount" name="discount">
                            </td>
                            <td>
                                <a class="btn-icon mr-5 delete-btn" href="javascript:void(0)"
                                   data-bind="click: $parent.removeRecord">删除</a>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="col-md-12 text-center">
                    <a class="btn btn-sm btn-warning dialog-btn" id="add_btn">新 增</a>
                </div>
            </div>
        </div>
    </div>

    <div class="text-center mt-30">
        <a class="btn btn-sm btn-warning dialog-btn" id="save_btn">提 交</a><span style='color:red'
                                                                                id="errorMsg">${errorMsg!}</span>
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

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var viewModel;

    var productList = [];
    <#if nowProductList??>
        <#list nowProductList as product>
        productList.push({
            productName: "${product.name}",
            productCode: "${product.productCode}",
            productPrize: "${(product.price/100)?string("#.##")}",
            discount: "${product.discount}"
        });
        </#list>
    </#if>

    var allProducts = [];
    <#if allProducts??>
        <#list allProducts as product>
        allProducts.push({
            productName: "${product.name}",
            productCode: "${product.productCode}",
            productPrize: "${(product.price/100)?string("#.##")}"
        });
        </#list>
    </#if>

    //是否自营标识
    var provinceFlag = "${provinceFlag}" === "ziying";
    var defaultDiscount = "${defaultDiscount}";

    var hasAppended = {};

    require(["knockout", "common", "bootstrap"], function (ko) {
        initViewModel(ko);

        if (!provinceFlag) {
            $("#productTable thead th").eq(3).hide();
            $("#productTable tbody td").eq(3).hide();
        }

        $("#add_btn").on("click", function () {
            addProduct();
        });

        $("#save_btn").on("click", function () {
            saveForm();
        });
    });

    function appendProductNames(product) {
        var parent = $(".productName:last");
        if (product) {
            hasAppended[product.productCode] = true;
            parent.attr("disabled", "disabled");
            parent.append('<option value="' + product.productCode + '" data-prize="' + product.productPrize + '">' + product.productName + '</option>');
        } else {
            parent.append('<option value="">---请选择---</option>');
            for (var i in allProducts) {
                product = allProducts[i];
                if (!hasAppended[product.productCode]) {
                    parent.append('<option value="' + product.productCode + '" data-prize="' + product.productPrize + '">' + product.productName + '</option>');
                }
            }

            productSelectListener(parent);
        }
    }

    function updateProductNames(product) {
        var parent = $(".productName:not(:disabled)");
        parent.empty();
        parent.each(function () {
            $(this).append('<option value="">---请选择---</option>');
            var lastCode = $(this).data("lastCode");
            for (var i in allProducts) {
                product = allProducts[i];
                if (!hasAppended[product.productCode]) {
                    var option = $('<option value="' + product.productCode + '" data-prize="' + product.productPrize + '">' + product.productName + '</option>');
                    if (lastCode == product.productCode) {
                        option.attr("selected", true);
                    }
                    $(this).append(option);
                }
            }
        });
    }

    /**
     *
     * @param ele
     */
    function productSelectListener(ele) {
        ele.on("change", function () {
            var code = ele.val();
            var prize = ele.find("option:selected").data("prize");
            ele.parent().parent().find(".productCode").html(code);
            ele.parent().parent().find(".productPrize").html(prize);

            ele.data("lastCode", code);
        });
    }

    function initViewModel(ko) {
        viewModel = {
            products: ko.observableArray([]),
            removeRecord: function () {
                delete hasAppended[this.productCode];
                updateProductNames();
                viewModel.products.remove(this);
            }
        };
        ko.applyBindings(viewModel);

        initProductData();
    }

    function initProductData() {
        for (var i in productList) {
            var product = productList[i];
            addProduct(product);
        }
    }

    function addProduct(product) {
        if (product) {
            var productName = product.productName;
            viewModel.products.push(product);
            product.productName = productName;
            appendProductNames(product);
        } else {
            product = {
                productName: "",
                productCode: "",
                productPrize: "",
                discount: ""
            };

            viewModel.products.push(product);
            appendProductNames();
            var discountEle = $(".discount:last");
            discountEle.on("input", function () {
                var val = $(this).val();
                val = val.replace(/[^0-9]+/g, "");
                $(this).val(val);
            });

            if (!provinceFlag) {
                $("#productTable tbody td").eq(3).hide();
                discountEle.val(defaultDiscount);
            }
        }
    }

    function saveForm() {
        var productList = [];

        var mytr = $("#productTable").find("tbody").find("tr");
        var validate = true;
        mytr.each(function () {
            var productCode = $(this).find("[name='productCode']").text();
            var productName = $(this).find("[name='productName']").find("option:selected").text();
            var productPrize = $(this).find("[name='productPrize']").text();
            var discount = $(this).find("[name='discount']").val();
            if (productCode == "") {
                validate = false;
                showTipDialog("请选择变更的具体产品！");
                return false;
            }
            if (discount == "") {
                validate = false;
                showTipDialog("请填写折扣信息！");
                return false;
            }
            if (parseInt(discount) > 100) {
                validate = false;
                showTipDialog("请输入小于等于100的折扣");
                return false;
            }
            productList.push({
                productName: productName,
                productCode: productCode,
                productPrize: productPrize,
                discount: discount
            });

        });


        if (!validate) {
            return;
        }

        var products = JSON.stringify(productList);

        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/enterprise/productChange/editProductChange.html",
            data: {
                enterpriseId: ${enterprise.id},
                products: products,
                requestId: ${requestId}
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                if (data.result == "success") {
                    showTipDialog("提交成功！");
                    window.location.href = "${contextPath}/manage/enterprise/productChange/index.html";
                }
                else if (data.result == "approving") {
                    showTipDialog("存在审批中的产品变更申请！");
                }
                else if (data.result == "repeat") {
                    showTipDialog("提交了重复的产品变更！");
                }
                else if (data.result == "nochange") {
                    showTipDialog("产品无变更！");
                }
                else
                    showTipDialog("提交失败！");
            },
            error: function () {
                showTipDialog("提交失败！");
            }
        });
    }
</script>
</body>
</html>