<#assign contextPath = rc.contextPath >
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${contextPath}/assets/css/flowCoupon.min.css"/>
    <title>领取流量券</title>
    <style>
        .input-wrap .input-btn {
            padding: 0;
        }

        #getCodeBtn {
            padding: 0 5px;
        }

        body {
            max-width: 800px;
            margin: 0 auto;
        }
    </style>
</head>
<body>

<#-- <img data-src="logo.png" class="justify logo"> -->
<img src="${contextPath}/assets/imgs/sh_login.png" class="mb-20 justify logo" style="width: 80%">

<div class="pd-10p ">
    <input type="hidden" id="secretPhone" name="secretPhone" value="${secretPhone!}">
    <div class="mt-40 input-wrap input-icon-left input-right-pd">
        <span class="icon icon-security"></span>
        <input type="text" name="phone" id="phone" placeholder="请输入手机号码" maxlength="11">
    </div>


    <#-- style="display:none" -->

    <div class="mt-40 input-wrap input-icon-left input-right-pd" id="smsCode">
        <span class="icon icon-security"></span>
        <input type="text" class="numberInput" id="verifyCode" placeholder="请输入验证码" maxlength="6">
        <span class="input-btn" id="getCodeBtn">点击获取验证码</span>
    </div>


    <#--
    <div class="input-wrap input-icon-left input-right-pd" id="imgCode">
        <span class="icon icon-security"></span>
        <input type="text" name="verifyImg" id="verifyImg" placeholder="图片验证码" maxlength="4">
        <label class="placeholder">图片验证码</label>
        <img class="input-btn" id="identity" src="${contextPath}/manage/virifyCode/getImg.html?_=${.now?datetime}"
             onclick="refresh()" title="点击更换图片"/>
    </div>
    -->
</div>
<div class="pd-10p error-tip">
    <span id="error-tip"></span>
</div>

<div class="pd-10p mt-30" id="smsCodeSubmit">
    <a class="btn btn-lg btn-primary" id="btn-submit">确 定</a>
</div>

<#--
<div class="pd-10p mt-30" id="imgCodeSubmit">
    <a class="btn btn-lg btn-primary" id="btn-submit-img">确 定</a>
</div>
-->

<script>
    var baseUrl = "${contextPath}/assets/imgs/";
</script>
<script src="${contextPath}/assets/js/zepto.min.js"></script>
<script src="${contextPath}/assets/js/flowcard.common.min.js"></script>


<script>

    function refresh() {
        //IE存在缓存,需要new Date()实现更换路径的作用
        document.getElementById("identity").src = "${contextPath}/manage/virifyCode/getImg.html?"
                + new Date();
    }

    /**
     * 图形验证码校验
     */
    //$("#btn-submit-img").on("click", function () {
    //    if (validateImg()) {
    //        postVerifyImg();
    //    }
    //});

    //$("#verifyImg").on("blur", function () {
    //    validateImg();
    //});


    /**
     * 短信随机验证码校验
     */
    $("#btn-submit").on("click", function () {
        if (validate()) {
            postVerifyCode();
        }
    });

    $("#verifyCode").on("blur", function () {
        validate();
    });

    $("#getCodeBtn").on("click", function () {
        if (wait == 60) {
            getCode();
            $("#verifyCode").focus();
        }
    });

    var wait = 60;
    function time(o) {
        if (wait == 0) {
            o.innerHTML = "重新获取";
            wait = 60;
        } else {
            o.innerHTML = wait + "秒后重新获取";

            wait--;
            setTimeout(function () {
                        time(o)
                    },
                    1000)
        }
    }

    /**
     * 短信随机码验证
     */
    function validate() {
        var phone = $("#phone").val();
        var verifyCode = $("#verifyCode").val();

        if(!phone.trim()){
            $("#error-tip").html("请输入手机号!");
            return false;
        }
        if (!verifyCode.trim()) {
            $("#error-tip").html("请输入短信验证码!");
            return false;
        }
        if (!/^[0-9]{6}$/.test(verifyCode)) {
            $('#error-tip').html('请输入正确短信验证码!');
            return false;
        }

        $("#error-tip").html("");
        return true;
    }

    /**
     * 图形验证码
     */
    function validateImg() {
        var verifyImg = $("#verifyImg").val();
        if (!verifyImg.trim()) {
            $("#error-tip").html("请输入图形验证码");
            return false;
        }
        if (!/^[0-9a-zA-Z]{4}$/.test(verifyImg)) {
            $('#error-tip').html('图形验证码有误！');
            return false;
        }

        $("#error-tip").html("");
        return true;
    }

    /**
     * 获取验证码
     */
    function getCode() {
        $("#error-tip").html("");
        var url = "${contextPath}/manage/message/sendFlowcardLogin.html?${_csrf.parameterName}=${_csrf.token}";
        time(document.getElementById("getCodeBtn"));

        var secretPhone = $("#phone").val();

        ajaxData(url, {"secretPhone": secretPhone}, function (ret) {
            if (ret && ret.message) {
                $("#error-tip").html(ret.message);
            }
        });
    }

    /**
     * 提交验证码
     */
    function postVerifyCode() {
    	//充值渠道
    	var channel = "${channel!}";
    	
        var url = "${contextPath}/manage/flowcard/charge/checkMsgCode.html?${_csrf.parameterName}=${_csrf.token}";
        var secretPhone = $("#phone").val();
        ajaxData(url, {
            code: $("#verifyCode").val(),
            secretPhone: $("#phone").val()
        }, function (ret) {
            if (ret && ret.message) {
                if (ret.message == "success") {
                    var secretPhone = ret.secretPhone;
                    window.location.href = "${contextPath}/manage/flowcard/charge/list/" + secretPhone + ".html?${_csrf.parameterName}=${_csrf.token}&channel="+channel;
                }
                else {
                    $("#error-tip").html(ret.msg);
                }
            } else {
                $("#error-tip").html("请输入正确短信验证码!");
            }
        })
    }

    /**
     * 提交图形验证码
     */
    function postVerifyImg() {
        var url = "${contextPath}/manage/flowcard/charge/checkImgCode.html?${_csrf.parameterName}=${_csrf.token}";
        ajaxData(url, {
            code: $("#verifyImg").val()
        }, function (ret) {
            if (ret && ret.message) {
                if (ret.message == "success") {
                    document.getElementById("imgCode").style.display = "none";//隐藏
                    document.getElementById("imgCodeSubmit").style.display = "none";//隐藏
                    document.getElementById("smsCode").style.display = "";//显示
                    document.getElementById("smsCodeSubmit").style.display = "";//显示
                }
                else {
                    $("#error-tip").html(ret.message);
                }
            } else {
                $("#error-tip").html("图形验证码错误!");
            }
        })
    }
</script>
</body>
</html>