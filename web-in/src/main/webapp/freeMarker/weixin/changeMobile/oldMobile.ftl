<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/zhongchou.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>更换手机号</title>
    <style>
        body{
            font-size: 13px;
        }

        .input-wrap {
            border: 1px solid #c2c6cf;
            border-radius: 5px;
        }

        .input-wrap input {
            margin-top: 7px;
        }

        .btn-orange{
            color: #fff;
            background-color:#eea236;
        }
        #getCodeBtn{
            padding: 11px 10px;
            width: 50%;
            font-size: 15px;
            border-radius: 5px;
        }

        #btn-submit{
            display: inline-block;
            padding: 11px 20px;
            font-size: 15px;
            border-radius: 5px;
            width: 100%;
        }

        .btn-disabled{
            background-color:#c2c6cf;
            box-shadow: none;
        }

        #error-tip{
            font-size: 13px;
        }

        .error-tip{
            position: absolute;
        }
    </style>
</head>
<body>

<div class="pd-10p" style="margin-top: 30%;">
    <h4>原手机号：${oldMobile!}</h4>
    <div class="mt-30 clearfix">
    <div class="input-wrap pull-left" style="width: 48%">
        <input type="text" class="numberInput" id="verifyCode" placeholder="请输入验证码" maxlength="6">
    </div>
    <div class="btn-orange text-center pull-right" id="getCodeBtn">获取短信验证码</div>
    </div>
    <div class="error-tip" id="error-tip"></div>
    <div class="error-tip" id="error-tip2"></div>

    <div class="mt-30">
        <span class="btn-orange text-center btn-disabled" id="btn-submit">下一步</span>
    </div>
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
        <p class="weui_toast_content">数据提交中</p>
    </div>
</div><!-- loading end -->

<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>
<script>
    if($('#verifyCode').val().length == 6){
        $("#btn-submit").removeClass('btn-disabled');
    }
    //验证码验证
    $('#verifyCode').on('input',function(){
        var val = $(this).val();
        val = val.replace(/[^0-9]+/, "");
        $(this).val(val);
        inputMaxBlur($(this),6);
    }).on('blur',function(){
        getCodeInfo($(this));
        bindBtn();
    });

    //获取验证码
    $('#getCodeBtn').on('click',function(){
        var that = this;
        var oldMobile = "${oldMobile!}";
        var url = "${contextPath}/manage/message/sendRondomCodeToOldmobile.html?${_csrf.parameterName}=${_csrf.token}";
        if ($(this).hasClass('btn-disabled')){
            return false;
        } else {
            time($(that));
            $('#error-tip').html('');
            ajaxData(url, {"oleMobile": oldMobile},function(ret){
                if(ret && ret.message){
                    $("#error-tip").html(ret.message);
                }
            });
        }
    });

    //绑定手机号码
    $('#btn-submit').on('click',function () {
        postVerifyCode();
    });
    
    /**
     * 提交验证码
     */
    function postVerifyCode() {
        var url = "${contextPath}/wx/changeMobile/checkMsgCode.html?${_csrf.parameterName}=${_csrf.token}";
        var phone = "${oldMobile!}";
        ajaxData(url, {
            code: $("#verifyCode").val(),
            phone: phone
        }, function (ret) {
            if (ret && ret.result) {
                if (ret.result == "true") {
                    nextPage();
                } else {
                    $("#error-tip").html(ret.message);
                }
            } else {
                $("#error-tip").html("请输入正确短信验证码!");
            }
        })
    }

    /**
     * 是否输入验证码
     */
    function getCodeInfo(ele) {
        var validate = false;
        var code = ele.val();
        if (!code) {
            $('#error-tip').html('请输入验证码');
        } else if (/^[0-9]{6}$/.test(code)) {
            validate = true;
            $('#error-tip').html('');
        } else {
            $('#error-tip').html('验证码有误！');
        }
        $(ele).data('validate',validate);
    }
    
    /**
     * 跳转到绑定手机号页面
     */
     function nextPage(){
     	window.location.href = "${contextPath}/wx/changeMobile/bindTelPage.html";
     }

    /**
     * 绑定按钮状态
     */
    function bindBtn(){
        if($('#verifyCode').data('validate') == true){
            $('#btn-submit').removeClass('btn-disabled');
        }else{
            $('#btn-submit').addClass('btn-disabled');
        }
    }

    /**
     * 输入字符数达到限制自动失去焦点
     */
    function inputMaxBlur(ele,num) {
        if (ele.val().length === num) {
            window.setTimeout(function(){
                ele.blur();
            },1);
        }
    }

    /**
     * 倒计时
     */

    var timer;
    var wait = 60;
    queryRemainTime(${oldMobile!});
    function time(ele) {
        if (wait === 0) {
            ele.removeClass('btn-disabled');
            ele.html('获取短信验证码');
            wait = 60;
        } else {
            ele.addClass('btn-disabled');
            ele.html(+ wait + 's');
            wait--;
            timer = setTimeout(function() {
                time(ele);
            }, 1000);
        }
    }

    function queryRemainTime(mobile){
        var url = "${contextPath}/manage/message/getRemainedTimeAjax.html?${_csrf.parameterName}=${_csrf.token}&timestamp=" + new Date().getTime();
        ajaxData(url, {"mobile": mobile},function(ret){
            if(ret){
                wait = parseInt(ret.remainedTime/1000);
                if(wait == 0 || wait >= 60){
                    $('#getCodeBtn').removeClass('btn-disabled');
                    $('#getCodeBtn').html('获取短信验证码');
                    wait = 60;
                }else{
                    time($('#getCodeBtn'));
                }
            }
        });
    }

    function showToast() {
        $('#loadingToast').modal('show');
    }

    function hideToast() {
        window.setTimeout(function(){
            $('#loadingToast').modal('hide');
        }, 500);
    }
</script>
</body>
</html>