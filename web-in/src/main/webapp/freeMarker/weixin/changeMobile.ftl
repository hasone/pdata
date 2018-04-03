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
    <title>解绑手机号</title>
    <style>
        body{
            font-size: 13px;
        }

        .input-wrap {
            border: 1px solid #c2c6cf;
            border-radius: 5px;
        }
        #getCodeBtn{
            padding: 11px 10px;
            width: 45%;
            background-color: #c2c6cf;
            color: #fff;
            font-size: 15px;
            border: 1px solid #c2c6cf;
            border-radius: 5px;
        }
        .btn-confirm{
            background-color: #fc9c00;
            border-color: #c2c6cf;
            box-shadow: none;
        }
        .btn-disabled{
            background-color: #c2c6cf;
            border-color: #c2c6cf;
        }

        .btn:active, .btn:focus, .btn:hover {
            box-shadow: none;
        }
        .input-wrap input {
            margin-top: 7px;
        }

        .footer-color a, .footer-color a:active, .footer-color a:focus, .footer-color a:hover {
            color: #333;
        }

        .modal-backdrop {
            display: block;
        }

        .bg-muted {
            background-color: #999;
            color: #fff;
        }
    </style>
</head>
<body>

<div class="pd-10p text-center" style="margin-top: 30%;">
    <h4>原手机号：${oldMobile!}</h4>
    <div class="input-wrap mt-30">
        <input type="text" class="numberInput" id="mobile" placeholder="请输入您的新手机号" maxlength="11">
    </div>
    <div class="mt-30 clearfix">
    <div class="input-wrap pull-left" style="width: 53%">
        <input type="text" class="numberInput" id="verifyCode" placeholder="请输入验证码" maxlength="6">
    </div>
    <div class="input-btn pull-right" id="getCodeBtn">获取短信验证码</div>
    </div>
</div>

<div class="pd-10p error-tip">
    <span id="error-tip"></span>
</div>
<div class="pd-10p error-tip">
    <span id="error-tip2"></span>
</div>

<div class="pd-10p mt-30">
    <a class="btn btn-lg btn-confirm btn-disabled" id="btn-submit">确 定</a>
</div>

<div class="modal fade dialog-sm" id="success-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body" style="padding: 30px 15px;">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="msg">更改手机号成功!</span>
            </div>
        </div>
    </div>
</div>

<div id="tip-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-success hide">
            <div class="modal-body">
                <p class="body-font">绑定成功！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style=" display: block; " data-dismiss="modal">确 定</a></p>
            </div>
        </div>

        <div class="modal-content body-failure hide">
            <div class="modal-body">
                <p class="body-font">请求失败！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style=" display: block; " data-dismiss="modal">确 定</a></p>
            </div>
        </div>

        <div class="modal-content body-busy hide">
            <div class="modal-body">
                <p class="body-font">网络繁忙！</p>
            </div>
            <div class="modal-footer">
                <p class="footer-color"><a style=" display: block; " data-dismiss="modal">再试一次</a></p>
            </div>
        </div>
        <!--<div class="modal-content body-used hide">-->
            <!--<div class="modal-body">-->
                <!--<p class="body-font">抱歉，您的手机号已与参加活动的其他微信号绑定，请使用其他手机号参与活动。</p>-->
            <!--</div>-->
            <!--<div class="modal-footer">-->
                <!--<p class="footer-color"><a style=" display: block; " data-dismiss="modal">再试一次</a></p>-->
            <!--</div>-->
        <!--</div>-->
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
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div><!-- loading end -->

<script>
    var ctx = '${contextPath}';
    var baseUrl = "${contextPath}/assets/imgs/";
</script>
<script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
<script src="${contextPath}/assets/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/js/common.min.js"></script>

<script>

    //手机号码验证
    $('#mobile').on('input',function(){
        var val = $(this).val();
        val = val.replace(/[^0-9]+/, "");
        $(this).val(val);
        inputMaxBlur($(this),11);
    }).on('blur',function(){
        getTelInfo($(this));
        bindBtn();
    });

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
        var newMobile = $('#mobile').val();
        var url = "${contextPath}/manage/message/sendRondomCodeForChangeMobile.html?${_csrf.parameterName}=${_csrf.token}";
        
        if ($(this).hasClass('code-disabled')){
            return false;
        } else {
            if ($('#mobile').data("validate") === true){
                time($(that));
                $('#error-tip2').html('');
                $(this).data('isClick','true');
                ajaxData(url, {"oleMobile": oldMobile, "newMobile": newMobile},function(ret){
                	if(ret && ret.message){
                		$("#error-tip").html(ret.message);
                	}
                });
            } else {
                if ($('#mobile').val() === '') {
                    $('#error-tip').html('请输入您的新手机号！');
                }
            }
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
                	//校验通过
                    bindTel();
                } else {
                    $("#error-tip").html(ret.message);
                }
            } else {
                $("#error-tip").html("请输入正确短信验证码!");
            }
        })
    }

    /**
     * 验证手机号码
     */
    function getTelInfo(ele){
        var tel = ele.val();
        var validate = false;
        if(!tel){
            $('#error-tip').html('');
        }else if(/^1[3|4|5|7|8]\d{9}$/.test(tel)){
            $('#error-tip').html('');
            $('#getCodeBtn').css('backgroundColor','#fc9c00');
            validate = true;
        }else{
            $('#error-tip').html('请输入正确的手机号码！');
        }
        ele.data('validate', validate);
    }

    /**
     * 是否输入验证码
     */
    function getCodeInfo(ele) {
        var code = ele.val();
        var validate = false;
        if (!code) {
            $('#error-tip2').html('');
        } else if (/^[0-9]{6}$/.test(code)) {
            $('#error-tip2').html('');
            validate = true;
        } else {
            $('#error-tip2').html('验证码有误！');
        }
        ele.data("validate", validate);
    }

    /**
     * 绑定按钮状态
     */
    function bindBtn(){
        var phoneValidate = $('#mobile').data("validate") === true;
        var codeValidate = $('#verifyCode').data("validate") === true;
        var btnValidate = phoneValidate && codeValidate;
        if(btnValidate){
            $('#btn-submit').removeClass('btn-disabled');
        }else{
            $('#btn-submit').addClass('btn-disabled');
        }
    }

    /**
     * 绑定手机号码
     */
    function bindTel(){
    	var url = "${contextPath}/wx/changeMobile/submitAjax.html?${_csrf.parameterName}=${_csrf.token}";
    	
    	var oldMobile = "${oldMobile!}";
        var newMobile = $('#mobile').val();
        //验证
        getTelInfo($('#mobile'));
        bindBtn();
        
        $.ajax({
            url: url,
            type: 'POST',
            dataType: 'json',
            data: {oldMobile: oldMobile, newMobile: newMobile},
            beforeSend: function() {
                showToast();
            },
            success: function (data) {
                if (data && data.result && data.result == "true") {
                	//window.location.href="myScore.html";
                	$("#success-dialog").modal("show");
                } else {
                  	showDialogContent('更换手机号失败!');
                }
            },
            error: function () {
                showDialogContent('busy');
            },
            complete: function(){
                hideToast();
            }
        });
    }

    //显示弹窗
    function showDialogContent(clazz){
        $('.modal-content').addClass('hide');
        $('.body-'+clazz).removeClass('hide');
        $('#tip-dialog').modal('show');
    }

    //输入字符数达到限制自动失去焦点
    function inputMaxBlur(ele,num) {
        if (ele.val().length === num) {
            window.setTimeout(function(){
                ele.blur();
            },1);
        }
    }

    //倒计时
    var timer;
    var wait = 60;
    function time(ele) {
        if (wait === 0) {
            ele.removeClass('code-disabled');
            ele.html('重新发送验证码');
            wait = 60;
        } else {
            ele.addClass('code-disabled');
            ele.html(+ wait + 's');
            wait--;
            timer = setTimeout(function() {
                time(ele);
            }, 1000);
        }
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