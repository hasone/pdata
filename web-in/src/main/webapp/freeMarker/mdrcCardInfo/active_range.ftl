<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>区间激活</title>
    <meta name="keywords" content="区间激活"/>
    <meta name="description" content="区间激活"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form .form-control {
            width: 220px;

        }

        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 220px;
        }

        .form label {
            width: 80px;
            text-align: right
            margin-right: 10px;
        }

        span.error {
            color: red;
            margin-left: 10px;
        }

        #comment {
            width: 200px;
            resize: none;
            height: 80px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>卡激活
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            区间激活
        </div>
        <div class="tile-content">
            <div class="row form">
                <form id="dataForm">
                    <div class="col-md-12 col-md-offset-1">
                        <div class="form-group">
                            <label>卡批次：</label>
                            <span>${mdrcBatchConfig.serialNumber!}</span>
                        </div>
                        <div class="form-group ">
                            <label>起始序列：</label>
                            <input type="hidden" class="form-control" id="startSerial" name="startSerial"
                                   value="${startSerial!}">
                            <span>${startSerial!}</span>
                        </div>
                        <div class="form-group ">
                            <label>激活数量：</label>
                            <div class="btn-group btn-group-sm">
                                <input class="form-control" id="activeCount" name="activeCount">
                            </div>
                            <div>
                                <label style="visibility: hidden">激活数量：</label>
                                <span style="font-size: 12px">该批次卡数量为${totalCount!}张，还剩余${notActiveCount!}未激活。</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>结束序列：</label>
                            <input name="endSerial" id="finishSerial" type="hidden">
                            <span id="endSerial"></span>
                        </div>
                        <div class="form-group">
                            <label>卡面值：</label>
                            <span>
                                <#if mdrcBatchConfig.productSize??&&(mdrcBatchConfig.productSize<1024)>
                                    <span>${( mdrcBatchConfig.productSize)?string("#.##")}KB</span>
                                </#if>
                                <#if  mdrcBatchConfig.productSize?? && ( mdrcBatchConfig.productSize>=1024) && ( mdrcBatchConfig.productSize<1024*1024)>
                                    <span>${( mdrcBatchConfig.productSize/1024.0)?string("#.##")}MB</span>
                                </#if>
                                <#if  mdrcBatchConfig.productSize?? && ( mdrcBatchConfig.productSize>=1024*1024)>
                                    <span>${( mdrcBatchConfig.productSize/1024.0/1024.0)?string("#.##")}GB</span>
                                </#if>
                            </span>
                        </div>
                        <div class="form-group">
                            <label>绑定企业：</label>
                            <div class="btn-group btn-group-sm enterprise-select">
                                <input style="width:0;height:0;opacity:0" id="enterprise" name="enterprises">
                                <button type="button" class="btn btn-default" style="width: 120px">&nbsp;</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu" style="right: auto; left: 0;">
                                <#if enterprises?? && enterprises?size != 0>
                                    <#list enterprises as item>
                                        <li data-value="${item.id}"><a href="#">${item.name}</a></li>
                                    </#list>
                                <#else>
                                    <li data-value=""><a href="#">没有可选的企业</a></li>
                                </#if>
                                </ul>
                            </div>
                        </div>
                        <div class="form-group ">
                            <label>绑定产品：</label>
                            <div class="btn-group btn-group-sm product-select">
                                <input style="width:0;height:0;opacity:0" id="product" name="product">
                                <button type="button" class="btn btn-default" style="width: 120px">&nbsp;</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu" style="right: auto; left: 0;">

                                </ul>
                            </div>
                            <div>
                                <label style="visibility: hidden">绑定产品：</label>
                                <span style="font-size: 12px">(请与卡印刷流量面值保持一致)</span>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
            </div>

        </div>
    </div>


    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">确认激活</a>
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
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="success-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    require(["common", "bootstrap"], function () {

        initFormValidate();

        submitForm();

        listeners();
    });

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#dataForm").validate({
            rules: {

                activeCount: {
                    required: true,
                    min: 1,
                    max: ${notActiveCount}
                },
                enterprise: {
                    required: true
                },
                product: {
                    required: true
                }
            },
            errorElement: "span",
            messages: {

                activeCount: {
                    required: "请填写激活数量",
                    min: "激活数量在1~${notActiveCount}之间",
                    max: "激活数量在1~${notActiveCount}之间"
                },
                enterprise: {
                    required: "请选择企业"
                },
                product: {
                    required: "请选择产品"
                }
            }
        });
    }

    //选择企业
    function loadProducts(id) {
        var enterId = id;
        var proSize = "${(mdrcBatchConfig.productSize)!}";
        //console.log(proSize);
        var option = "";
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            data: {
                enterId: enterId,
                proSize: proSize
            },
            url: "${contextPath}/manage/product/getProductsAjaxBySizeEnterId.html",
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var parent = $(".product-select .dropdown-menu");
                parent.empty();

                parent.append('<li data-value="" ><a href="#">请选择产品</a></li>');
                $.each(data, function (k, v) {
                    parent.append('<li data-value="' + v['id'] + '" ><a href="#">' + v['name'] + '</a></li>');
                });
            }
        });
    }

    function submitForm() {
        $("#submit-btn").on("click", function () {
            if ($("#dataForm").validate().form()) {
                saveForm();
            }
            return false;
        });
    }

    function listeners() {
        $("#startSerial").on("change", function () {
            updateEnd();
        });
        $("#activeCount").on("input", function () {
            var val = $(this).val();
            if (val === "0") {
                val = "";
            }
            val = val.replace(/[^0-9]+/g, "");
            $(this).val(val);
        });
        $("#activeCount").on("change", function () {
            updateEnd();
        });

        $(".enterprise-select a").on("click", function () {
            var entId = $(this).parent().attr("data-value");
            loadProducts(entId);
        });
    }


    function updateEnd() {
        var start = $("#startSerial").val();
        var count = $("#activeCount").val();
        var leftCount =${notActiveCount!};

        //判断start是否存在
        if (!start) {
            return;
        }
        //判断start是否为正整数
        var te = /(^[1-9]([0-9]*)$|^[0-9]$)/;
        if (!te.test(count)) {
            return;
        }

        if (count > leftCount) {
            return;
        }


        count = count != "" ? parseInt(count) : 1;

        var length = 7;

        var pref = start.substring(0, start.length - length);
        var numStr = start.substr(start.length - length);
        var sum = parseInt(numStr) + count - 1;
        var sumStr = sum + "";
        var sumLength = sumStr.length;

        for (var i = 0; i < length - sumLength; i++) {
            sumStr = "0" + sumStr;
        }

        var ret = pref + sumStr;
        $("#endSerial").html(ret);
    }

    /**
     * 提交表单
     */
    function saveForm() {
        var serialNumber = "${mdrcBatchConfig.serialNumber!}";
        var configId = "${configId!}";

        var params = {
            configId: configId,
            serialNumber: serialNumber,
            startSerial: $("#startSerial").val(),
            endSerial: $("#endSerial").html(),
            enterpriseId: $("#enterprise").val(),
            product: $("#product").val()
        };

        var url = "${contextPath}/manage/mdrc/cardinfo/activeRange.html";
        ajaxData(url, params, function (ret) {
            if (ret && ret.success) {
                //激活成功
                showTipDialog("激活成功");

                $("#success-btn").on("click", function () {
                    //成功后跳转
                    //window.history.go(-1);
                    window.location.href = "${contextPath}/manage/mdrc/cardinfo/index.html?configId=${configId!}&year=${year}&currentCardStatus=${currentCardStatus}";
                });
            } else {
                showTipDialog("激活失败");
            }
        }, null, "post");
    }
</script>
</body>
</html>