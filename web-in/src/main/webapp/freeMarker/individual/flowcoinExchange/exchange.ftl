<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>兑换</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets_individual/theme/default/theme.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/main.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets_individual/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets_individual/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form-group label {
            width: 80px;
        }

    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mb-20">
        <h3>业务办理-兑换</h3>
    </div>

    <div class="tile mt-30">
        <div class="form-group">
            <label>流量币余额:</label>
            <span class="green-text-lg">10000</span>&nbsp;&nbsp;个
        </div>
        <hr>

        <div class="form-group">
            <label>兑换流量:</label>
            <ul id="flowList">
            <#if products??>
                <#list products as product>
                    <#if product_index == 0>
                    <li class="flowItem active" id="${product.id}">
                    <#else>
                    <li class="flowItem" id="${product.id}">
                    </#if>
                    <span>
								<#if (product.productSize<1024)>
                                ${(product.productSize)?string('#.##')}KB
                                </#if>
                        <#if (product.productSize>=1024) && (product.productSize<1024*1024)>
                        ${(product.productSize/1024.0)?string('#.##')}MB
                        </#if>
                        <#if (product.productSize>=1024*1024)>
                        ${(product.productSize/1024.0/1024.0)?string('#.##')}GB
                        </#if>
            				</span>
                </li>
                </#list>
            </#if>
            </ul>
        </div>
        <hr>
        <form id="table_validate">
            <div class="form-group">
                <label>消耗流量币:</label>
                <span class="red-text-lg" id="flowcoinCount">${flowcoinCount!}</span>&nbsp;&nbsp;个
                <input type="text" style="width: 0; height:0; opacity: 0;" name="count" id="count"
                       value="${flowcoinCount!}">
                <input type="text" style="width: 0; height:0; opacity: 0;" name="productId" id="productId"
                       value="${productId!}">
            </div>

            <div class="form-group form-group-sm form-inline">
                <label>手机号码:</label>
                <input class="form-control mobileOnly" maxlength="11" id="mobile" name="mobile">

                <div class="promote">流量币将兑换至填写手机号码通信账户</div>
            </div>
        </form>

        <div class="tile tile-sm orange-tile orange-border">
            流量币兑换须知：<br>
            1、流量币兑换规则是什么？<br>
            答：1个流量币价值0.1元，根据标准流量包的资费30个兑换10M流量、50个兑换30M流量、500个兑换1G流量，以此类推。<br>
            2、兑换的流量是否具有有限期？<br>
            答：兑换成功至通信账户的流量有效期为当月月底。<br>
        </div>
    </div>

    <div class="mt-20 text-center">
        <a class="btn btn-primary btn-sm btn-width" id="exchange-btn">兑 换</a>
        <span style="color:red" id="error_msg">${errorMsg!}</span>
    </div>


    <div class="mt-30"><h3>历史订单</h3></div>
    <hr>

    <div class="mt-20">
        <div class="form-inline text-right">
            <div class="form-group form-group-sm">
                <span class="ml-20">手机号码:</span>
                <input name="mobile" class="searchItem form-control mobileOnly" maxlength="11">
            </div>
            <a class="btn btn-sm btn-default btn-width ml-20" id="search-btn">查询</a>
        </div>
        <div id="table_wrap" class="mt-10 table-wrap text-center"></div>
    </div>

    <div id="messageBox"></div>
</div>

<!-- loading -->
<div id="loadingToast" class="weui_loading_toast">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <div class="weui_loading">
            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div><!-- loading end -->

<!--[if lt IE 9]>
<script src="${contextPath}/assets_individual/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets_individual/lib/es5-sham.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script>
    Object.getPrototypeOf = function getPrototypeOf(object) {
        return object.__proto__;
    };
</script>
<![endif]-->

<script>
    if (window.ActiveXObject) {
        var reg = /10\.0/;
        var str = navigator.userAgent;
        if (reg.test(str)) {
            Object.getPrototypeOf = function getPrototypeOf(object) {
                return object.__proto__;
            };
        }
    }
</script>


<script src="${contextPath}/assets_individual/lib/require.js"></script>
<script src="${contextPath}/assets_individual/dist/config.js"></script>
<script>
    var messageBox;
    require(["react", "react-dom", "../js/listData", "MessageBox", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData, MessageBox) {
        renderTable(React, ReactDOM, ListData);
        messageBox = renderWidget(React, ReactDOM, MessageBox, {
            grid: 0.35,
            confirm: function () {
                window.location.href = "${contextPath}/individual/flowcoin/exchangeIndex.html";
                return true;
            }
        }, $("#messageBox")[0]);

        listeners();

        checkFormValidate();
    });

    function checkFormValidate() {
        $("#table_validate").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                $(element).parent().append(error);
            },
            errorElement: "span",
            rules: {
                mobile: {
                    required: true,
                    mobile: true
                },
                count: {
                    required: true
                }
            },
            messages: {
                mobile: {
                    required: "请输入正确的手机号码",
                    mobile: "请输入正确的手机号码"
                },
                count: {
                    required: "请选择兑换流量包"
                }
            }
        });
    }

    var ctx = "${contextPath}/individual/flowcoin";

    var STATUS = {
        "0": "兑换中",
        "1": "兑换成功",
        "2": "失败退回中",
        "3": "失败已退回",
        "4": "失败退回中"
    };
    var COLORS = {
        "0": "#EA6500",
        "1": "#8FC31C",
        "2": "#377BC4",
        "3": "#377BC4",
        "4": "#377BC4"
    };
    var renderTable = function (React, ReactDOM, ListData) {
        var ele = React.createElement(ListData, {
            action: ctx + "/exchangeHistoryOrders.html?${_csrf.parameterName}=${_csrf.token}",
            columns: [
                {name: "systemSerial", text: "流水号"},
                {name: "createTime", text: "兑换时间", format: "DateTimeFormat"},
                {name: "mobile", text: "兑换手机"},
                {name: "productSize", text: "兑换流量(M)"},
                {
                    name: "status", text: "状态", format: function (value, column, row) {
                    var color = COLORS[value];
                    return "<span style='color: " + color + "'>" + STATUS[value] + "</span>"
                }
                }
            ]
        });
        ReactDOM.render(ele, $("#table_wrap")[0]);
    };

    /**
     * 监听
     */
    function listeners() {
        $("#exchange-btn").on("click", function () {
            if ($("#table_validate").validate().form()) {
                var productId = $("#productId").val();
                var count = $("#count").val();
                var mobile = $("#mobile").val();
                showToast();
                $.ajax({
                    type: "POST",
                    url: "${contextPath}/individual/flowcoin/exchange.html?${_csrf.parameterName}=${_csrf.token}",
                    data: {
                        productId: productId,
                        count: count,
                        mobile: mobile
                    },
                    dataType: "json", //指定服务器的数据返回类型，
                    success: function (res) {
                        if (res.result && res.result == "success") {
                            //逻辑完成后
                            hideToast();
                            //弹框提示
                            messageBox.show('<div class="modal-body text-center">' +
                                    '<div class="">流量币兑换已成功受理，具体兑换情况您可在下方历史记录或流量币余额收支明细中查询，谢谢！</div>' +
                                    '</div>');
                        }
                        else {
                            hideToast();
                            messageBox.show('<div class="modal-body text-center">' +
                                    '<div class="">流量币兑换失败！</div>' +
                                    '</div>');
                        }
                    },
                    error: function () {
                        hideToast();
                        messageBox.show('<div class="modal-body text-center">' +
                                '<div class="">流量币兑换失败！</div>' +
                                '</div>');
                    }
                });

            }

        });

        $(".flowItem").on("click", function () {
            if ($(this).hasClass("disabled")) {
                return false;
            }

            if ($(this).hasClass("active")) {
                $(this).removeClass("active");
                $("#flowcoinCount").html("0");
                $("#count").val("");
            } else {
                $(".flowItem.active").removeClass("active");
                $(this).addClass("active");

                getFlowcoinCount(this);

                $("#count").val($("#flowcoinCount").text());
                $("#productId").val($(this).attr("id"));
            }

        });

    }

    function getFlowcoinCount(obj) {
        var productId = $(obj).attr("id");
        $.ajax({
            type: "POST",
            url: "${contextPath}/individual/flowcoin/getFlowcoinCount.html?${_csrf.parameterName}=${_csrf.token}",
            data: {
                productId: productId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (res) {
                if (res.result) {
                    $("#flowcoinCount").html(res.result);
                    $("#count").val(res.result);
                }
                else {
                    $("#flowcoinCount").html("");
                    $("#count").val("0");
                }
            },
            error: function () {
                $("#flowcoinCount").html("");
                $("#count").val("0");
            }
        });
        return false;
    }
</script>
</body>
</html>