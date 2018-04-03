<!DOCTYPE html>
<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-退款</title>
    <meta name="keywords" content="流量平台 退款"/>
    <meta name="description" content="流量平台 退款"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
    <style>
        .form {
            width: 1200px;
        }

        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 5px;
            width: 350px;
        }

        .form .form-group label {
            width: 300px;
            text-align: right;
        }

        .form .form-group .prompt {
            margin-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .btn-save {
            margin-left: 300px;
        }
        
         textarea {
            width: 350px;
            resize: none;
            outline: none;
            border: 1px solid #ccc;
            border-radius: 3px;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>退款
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <form name="refundForm" role="form" id="table_validate" action="#"  method="post">

        <div class="tile mt-30">
            <div class="tile-header">
                退款
            </div>
            <div class="tile-content">
                <div class="row form">

                    <div class="form-group">
                        <label>退款流水号(DoneCode):</label>
                       <input type="text" name="doneCode" id="doneCode" maxlength="50" onfocus="$('#error_msg').empty();"
                               value=''/>
                    </div>
                
                
                    <div class="form-group">
                        <label style="vertical-align:top;">退款原因:</label>
                        <textarea name="reason" id="reason" maxlength="256" rows="10" cols="10" onfocus="$('#error_msg').empty();"></textarea>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-save mt-30">
            <a class="btn btn-sm btn-warning dialog-btn" id="save-btn" href="javascript:void(0);">提交</a>&nbsp;&nbsp;&nbsp;
            <span style="color:red" id="error_msg"></span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        checkFormValidate();

        $("#save-btn").on("click", function () {
            $("#error_msg").empty();
            if ($("#table_validate").validate().form()) {
                sumitAdd();
            }

            return false;
        });
    });
</script>


<script>
     function checkFormValidate() {
        $("#table_validate").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if ($(element).siblings('div').hasClass('prompt')) {
                    $(element).siblings('div.prompt').before(error);
                } else {
                    $(element).parent().append(error);
                }
            },
            errorElement: "span",
            rules: {
                doneCode: {
                    required: true
                },
                reason: {
                    required: true
                }
            },
            messages: {
                doneCode: {
                    required: "退款流水号不能为空"
                },
                reason: {
                    required: "退款原因不能为空"
                }
            }
        });
    }
    

    function isEmptyOrNull(str) {
        return (str == null || str.trim().length == 0);
    }


    function sumitAdd() {
        var doneCode = $("#doneCode").val();
        var reason = $("#reason").val();
        
         $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/payplatform/yqxRefundPageAjax.html",
                data: {
                    doneCode: doneCode,
                    reason: reason
                },
                type: "post",
                dataType: "json"
            }).success(function (ret) {
                if (ret) {
                    $("#error_msg").append(ret.msg);
                }
            });

             
        return false;
    }

    
   
</script>
</body>
</html>