<!DOCTYPE html>
<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新增产品</title>
    <meta name="keywords" content="流量平台 新增产品" />
    <meta name="description" content="流量平台 新增产品" />

    <link rel="stylesheet" href="../../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../../assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="../../assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="../../assets/css/base.min.css"/>
    <link rel="stylesheet" href="../../assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="../../assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form select{
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 200px;
        }
        .form .form-group label{width: 100px;text-align: right;}
        .form .promote{
            margin-left: 105px;
            color: #999;
            font-size: 12px;
        }
    </style>
</head>
<body>

    <div class="main-container">
        <div class="module-header mt-30 mb-20">
            <h3>产品添加
                <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
            </h3>
        </div>


        <div class="tile mt-30">
            <div class="tile-header">
                新增产品编码
            </div>
            <div class="tile-content">
                <div class="row form">
                    <form id="dataForm" class="text-left ml-40" style="display: inline-block; ">
                        <div class="form-group form-inline form-group-sm">
                            <label>企业名称：</label>
                            <span>${enterprise.name}</span>
                        </div>
                        <div class="form-group">
                            <label>集团编码：</label>
                            <span>${enterprise.code}</span>
                        </div>
                        <div class="form-group">
                            <label>集团产品号码：</label>
                            <input type="text" id="productCode" name="productCode" class="mobileOnly" maxlength="13" minlength="13"/>
                            <div class="promote">请输入企业对应正确的13位产品编码</div>
                        </div>
                        <a id="search-btn" class="hidden"></a>
                    </form>
                </div>

            </div>
        </div>


        <div class="mt-30 text-center">
            <a class="btn btn-warning btn-sm mr-10 dialog-btn" style="width: 114px;" id="save-btn">添 加</a>
        </div>

    </div>

    <div class="modal fade dialog-lg" id="sync-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">同步信息</h5>
                </div>
                <div class="modal-body">
                    <div>企业名称：<span>${enterprise.name}</span></div>
                    <div>集团产品号码：<span id="dialog_productCode"></span></div>
                    <div class="error-tip">请确认产品同步结果，以及集团产品号码<span id="dialog_productCode1"></span>关联到<span>${enterprise.name}</span>企业中无误后，点击提交</div>
                    <div class="table-responsive mt-10" role="table">
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="text-center">
                        <button class="btn btn-gray btn-sm dialog-btn" data-dismiss="modal">取 消</button>
                        <button class="btn btn-warning btn-sm dialog-btn" id="submit-btn">提 交</button>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
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

    <script src="../../assets/lib/require.js"></script>
    <script src="../../assets/js/config.js"></script>
    <script src="../../assets/js/zepto.min.js"></script>
    <script>
    	var  entProCode= $("input[name='productCode']").val();
        var action = "${contextPath}/cq/product/productAddDetails.html?${_csrf.parameterName}=${_csrf.token}&entProCode="+entProCode+"&entId=${enterprise.id}";
        var columns = [{name: "proName", text: "产品名称"},
            {name: "discount", text: "折扣"},
            {name: "amount", text: "余额"}
        ];

        require(["common", "bootstrap","page/table"], function(a,c, tableWidget) {
            tableWidget.searchCallback = function(){
                hideToast();
                $("#sync-dialog").modal("show");
            };
            initFormValidate();

            $("#save-btn").on("click", function(){
                showSyncDialog();
            });
            
            $("#submit-btn").on("click", function(){
            	submitSyncInfo();
            });
        });

        /**
         * 表单验证
         */
        function initFormValidate(){
            $("#dataForm").validate({
                errorPlacement: function (error, element) {
                    error.addClass("error-tip");
                    if (element.next().hasClass("promote")) {
                        element.after(error);
                    } else {
                        element.parents(".form-group").append(error);
                    }
                },
                errorElement: "span",
                rules: {
                    productCode: {
                        required: true
                    }
                },
                messages: {
                    productCode: {
                        required: "请填写产品编码"
                    }
                }
            });
        }

        /**
         * 显示同步弹框
         */
        function showSyncDialog(){
            if($("#dataForm").validate().form()){
                showToast();
                entProCode= $("input[name='productCode']").val();
                action = "${contextPath}/cq/product/productAddDetails.html?${_csrf.parameterName}=${_csrf.token}&entProCode="+entProCode+"&entId=${enterprise.id}";
                $("#dialog_productCode").html($("input[name='productCode']").val());
                $("#dialog_productCode1").html($("input[name='productCode']").val());
                $("#search-btn").click();
            }
        }
        
        function submitSyncInfo(){
        	var entProCode = $("input[name='productCode']").val();
        	self.location="${contextPath}/cq/product/submitProductAdd.html?${_csrf.parameterName}=${_csrf.token}&entProCode=" + entProCode + "&entId=${enterprise.id}";
        }
    </script>
</body>
</html>