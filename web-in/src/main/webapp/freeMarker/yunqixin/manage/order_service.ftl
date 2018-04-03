<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-售后处理</title>
    <meta name="keywords" content="流量平台 售后处理"/>
    <meta name="description" content="流量平台 售后处理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    
    <style>
        .nav-tabs > li > a {
            border: 1px solid #ddd;
            background-color: #ddd;
            color: #787878;
            padding: 10px 25px;
        }

        .nav > li > a:focus, .nav > li > a:hover {
            background-color: #ddd;
            color: #787878;
        }

        .tile {
            box-shadow: 0px 0px 1px rgba(0, 0, 0, .2);
        }

        #submit-btn {
            padding: 8px 45px;
            margin: 0 auto;
            margin-bottom: 30px;
            font-size: 14px;
        }
        .error-tip{
            margin-left: 238px;
        }

        textarea{
            resize: none;
            outline: none;
            border-color: #ccc;
        }
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>售后处理
        <a class="btn btn-primary btn-sm btn-icon icon-back pull-right" onclick="history.go(-1)" >返回</a>
        </h3>
    </div>
    <div class="mt-30">
        <div class="tile gray-border tile-noTopBorder">
            <div class="tile-header">售后处理确认单</div>
            <div class="mt-10 tile-content">
                <form class="row form" id="dataForm" action="${contextPath}/manage/yqx/order/submitRefundProcessing.html">
                   
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" id="doneCode" name="doneCode" value="${doneCode!}">
                    <input type="hidden" id="reasonContent" name="reasonContent">
                    
                    <div class="row">
                        <label class="col-xs-2 text-right">售后处理类型</label>
                    <span class="col-xs-10">
                        <span class="input-checkbox">
                            <input type="radio" value="1" name="refund" id="refund" checked="checked" class="hidden"
                                   required>
                            <label for="refund" class="c-checkbox mr-15 rt-1"></label>
                            <span>退款</span>
                        </span>
                    </span>
                    </div>
                    <div class="row mt-10">
                        <label class="col-xs-2 text-right">处理原因</label>
                    <span class="col-xs-10">
                        <span class="input-checkbox">
                            <input type="radio" value="1" name="reason" id="reason_1" checked="checked" class="hidden"
                                   required>
                            <label for="reason_1" class="c-checkbox mr-15 rt-1"></label>
                            <span>充值失败</span>
                        </span>
                        <span class="input-checkbox ml-20">
                            <input type="radio" value="2" name="reason" id="reason_2" class="hidden">
                            <label for="reason_2" class="c-checkbox mr-15 rt-1"></label>
                            <span>重复支付</span>
                        </span>
                        <span class="input-checkbox ml-20">
                            <input type="radio" value="3" name="reason" id="reason_3" class="hidden">
                            <label for="reason_3" class="c-checkbox mr-15 rt-1"></label>
                            <span>投诉流量未到账</span>
                        </span>
                        <span class="input-checkbox ml-20">
                            <input type="radio" value="4" name="reason" id="reason_4" class="hidden">
                            <label for="reason_4" class="c-checkbox mr-15 rt-1"></label>
                            <span>其他</span>
                        </span>
                    </span>
                    </div>
                    <div class="row mt-10" id="other-warp" hidden>
                        <label class="col-xs-2 text-right">其他原因</label>
                        <span class="col-xs-10">
                            <textarea id="otherReason" cols="52" rows="10" maxlength="300"></textarea>
                        </span>
                        <div class="error-tip" hidden>如选择其他原因，请填写原因内容</div>
                    </div>
                    <div class="row mt-10">
                        <label class="col-xs-2 text-right">处理人姓名</label>
                        <span class="col-xs-10">${administer.userName!}</span>
                    </div>
                    <div class="row mt-10">
                        <label class="col-xs-2 text-right">处理人手机号号码</label>
                        <span class="col-xs-10">${administer.mobilePhone!}</span>
                    </div>
                </form>
            </div>
            <div class="mt-30 text-center">
                <a class="btn btn-primary btn-sm" id="submit-btn">确认提交</a>
                <span style="color:red" id="error_msg">${errorMsg!}</span>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="confirm-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">无法撤销处理，确认提交?</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok-btn">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script>

    require(["common"], function () {
        listeners();
    });

    function listeners() {  
        $(".input-checkbox .c-checkbox").on('click',function(){
            var reason = $(this).prev('input').val();
            if (reason == '4') {
                $("#other-warp").show();
            }else{
                $("#other-warp").hide();
            }
        });
          
        $("#submit-btn").on('click', function () {
            $("#confirm-dialog").modal('show');
        });
        
        $("#ok-btn").on('click', function () {
            showToast();
            var reason = $("input[name='reason']:checked").val();           
            if (reason == '4') {
                var val = $("#otherReason").val();
                if (val.length <= 0) {
                    $(".error-tip").show();
                    return false;
                } else {
                    $(".error-tip").hide();
                    $("#reasonContent").val(val);
                    $("#dataForm").submit();
                }
            }else{
                $("#reasonContent").val($("input[name='reason']:checked").parent().find('span').html());
                $("#dataForm").submit();
            }
        });
    }
</script>
</body>
</html>