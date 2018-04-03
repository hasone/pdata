<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-修改密码</title>
    <meta name="keywords" content="流量平台 修改密码"/>
    <meta name="description" content="流量平台 修改密码"/>

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
            width: 345px;
            margin: 0 auto;
        }

        .form input[type='text'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 10px;
            width: 100%;
        }

        .form .form-group .prompt {
            color: #bbb;
        }

        .tile-content {
            padding-bottom: 60px;
        }

        .module-header {
            padding: 15px;
            background-color: #475b75;
        }

        .code-disabled {
            pointer-events: none;
            cursor: not-allowed;
            -webkit-box-shadow: none;
            box-shadow: none;
        }

        .code-sent{
            position: absolute;
            font-size: 12px;
            color: red;
        }

        #msg-error{
            display: block;
        }
    </style>
</head>
<body>

<div class="module-header mb-20">
    <img src="${contextPath}/assets/imgs/login_logo.png" style="width: 125px">
</div>

<div class="main-container">
    <div class="tile mt-30">
        <div class="tile-header">
            修改密码
        </div>
        <div class="tile-content">
            <input type="hidden" id="key" value="${Session.SESSION_LOGIN_TOKEN}"/>
            <form class="form" id="form">
                <div class="form-group" style="margin-bottom: 18px;">
                    <div class="form-inline">
                        <input type="text" name="code" id="code" style="width: 240px;" maxlength="6" required>
                        <div class="btn btn-sm btn-success" style="vertical-align: top; width: 100px; height: 30px;"
                             id="getCode">发送验证码</div>
                        <span class="error-tip getCode-error"></span>
                    </div>
                    <div class="code-sent" hidden><span id="send-msg"></span></div>
                </div>
                <div class="form-group">
                    <div>新密码</div>
                    <input type="password" name="newPwd_origin" id="newPwd_origin" onkeyup="newPwd.value=this.value" required>
                    <input type="password" name="newPwd" id="newPwd" style="position: absolute; width: 0; height: 0; padding: 0; opacity: 0;">
                    <div class="prompt">密码必须包含字母、数字、特殊符号且长度10到20位</div>
                </div>
                <div class="form-group form-inline">
                    <div>确认新密码</div>
                    <input type="password" name="confirmPwd_origin" id="confirmPwd_origin" onkeyup="confirmPwd.value=this.value" required>
                    <input type="password" name="confirmPwd" id="confirmPwd" style="position: absolute; width: 0; height: 0; padding: 0; opacity: 0;">
                </div>
            </form>
            <div class="text-center mt-30">
                <a class="btn btn-sm btn-warning dialog-btn">提交验证</a>
            </div>
        </div>
    </div>
</div>
<script src="${contextPath}/assets/lib/crypto-js/core.js"></script>
<script src="${contextPath}/assets/lib/crypto-js/crypto-js.js"></script>
<script src="${contextPath}/assets/lib/crypto-js/aes.js"></script>
<script src="${contextPath}/assets/lib/crypto-js/mode-ecb.js"></script>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    //倒计时
    var timer,key,parseKey;
    var wait = 60;
    require(["common", "bootstrap"], function () {
        checkFormValidate();
        listeners();
        key = $('#key').val();
        parseKey  = CryptoJS.enc.Utf8.parse(key);
    });

    function Encrypt(str){
        var srcs = CryptoJS.enc.Utf8.parse(str);
        var encrypted = CryptoJS.AES.encrypt(srcs, parseKey, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
        return encrypted.toString();
    }

    function time(ele) {
        if (wait === 0) {
            ele.removeClass('code-disabled');
            ele.html('发送验证码');
            $(".code-sent").hide();
            wait = 60;
        } else {
            ele.addClass('code-disabled');
            ele.html(+wait + 's');
            wait--;
            timer = setTimeout(function () {
                time(ele);
            }, 1000);
        }
    }

    function checkFormValidate() {
        $("#form").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                if ($(element).siblings('div').hasClass('prompt')) {
                    $(element).siblings('div.prompt').after(error);
                } else {
                    $(element).parent().append(error);
                }
            },
            errorElement: "span",
            rules: {
                code: {
                    required: true
                },
                newPwd_origin: {
                    required: true,
                    strictPwd: true
                },
                confirmPwd_origin: {
                    required: true,
                    verifyPwd: '#newPwd'
                }
            },
            messages: {
                code: {
                    required: "请输入正确的验证码"
                },
                newPwd_origin: {
                    required: "请输入新密码",
                    strictPwd: "请输入包含字母、数字、特殊字符的10-20位密码"
                },
                confirmPwd_origin: {
                    required: "请输入确认新密码",
                    verifyPwd: "确认密码与新密码不一致"
                }
            }
        });
    }

    function listeners(){
        $('#getCode').on('click', function () {
            $(".getCode-error").html("");
            var that = this;
            if ($(this).hasClass('code-disabled')) {
                return false;
            } else {
                time($(that));
                $.ajax({
                    url: "${contextPath}/manage/message/sendResetPassword.html?${_csrf.parameterName}=${_csrf.token}",
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        $(".code-sent").show();
                        $("#send-msg").html(data.message);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        if (XMLHttpRequest.status == 403) {
                            window.location.href = "${contextPath}/manage/user/login.html";
                        } else {
                            alert("网络出错，请重新尝试");
                        }
                    }
                });
            }
        });

        $("#code").on("input", function () {
            $(".getCode-error").html("");
        });

        $('.dialog-btn').on('click', function () {
            if ($("#form").validate().form()) {
                $.ajax({
                    url: "${contextPath}/manage/resetpwd/savePassword.html?${_csrf.parameterName}=${_csrf.token}",
                    type: "post",
                    dataType: "json",
                    data: {
                        "code": $("#code").val(),
                        "newPwd": Encrypt($("#newPwd").val()),
                        "confirmPwd": Encrypt($("#confirmPwd").val())
                    },
                    success: function (data) {
                        $("#send-msg").html("");
                        if (data.done) {
                            window.location.href = "confirmBack.html";
                        } else {
                            $(".getCode-error").html(data.errorMsg);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        if (XMLHttpRequest.status == 403) {
                            window.location.href = "${contextPath}/manage/user/login.html";
                        } else {
                            alert("网络出错，请重新尝试");
                        }
                    }
                });
            }
            return false;
        });

    }
</script>
</body>
</html>