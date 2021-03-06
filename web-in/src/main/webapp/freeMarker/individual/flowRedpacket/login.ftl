<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/redPacket.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css">
    <title>四川移动流量红包</title>
    <style>        
        .footer-color a, .footer-color a:active, .footer-color a:focus, .footer-color a:hover {
            color: #000;
        }
    </style>
</head>

<body>
    
    <div id='wx_pic' style='margin:0 auto;display:none;'>
        <img src="${contextPath}/assets/imgs/cmcc-logo300-300.png">
    </div>

    <div class="img-20 justify">
        <img src="${contextPath}/assets/imgs/logo-sc.png">
    </div>
    <div class="pd-10p mt-40">
        <div class="input-wrap input-icon-left">
            <span class="icon icon-phone"></span>
            <input type="text" class="numberInput" id="mobile" placeholder="请输入手机号码" maxlength="11">
        </div>
    </div>
    <div class="pd-10p">
        <div class="input-wrap input-icon-left input-right-pd">
            <span class="icon icon-security"></span>
            <input type="text" class="numberInput" id="verifyCode" placeholder="请输入验证码" maxlength="6">
            <span class="input-btn" id="getCodeBtn">获取验证码</span>
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
    <div id="tip-dialog" class="modal fade text-center" data-backdrop="static">
        <div class="modal-dialog">
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
            <div class="modal-content body-sichuan hide">
                <div class="modal-body">
                    <p class="body-font">仅限四川手机号参加哦~</p>
                </div>
                <div class="modal-footer">
                    <p class="footer-color"><a style=" display: block; " data-dismiss="modal">确 定</a></p>
                </div>
            </div>
            <div class="modal-content body-error hide">
                <div class="modal-body">
                    <p class="body-font">短信验证码错误！</p>
                </div>
                <div class="modal-footer">
                    <p class="footer-color"><a style=" display: block; " data-dismiss="modal">确 定</a></p>
                </div>
            </div>
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

    <script src="${contextPath}/assets/js/jquery-1.11.3.min.js"></script>
    <script src="${contextPath}/assets/js/bootstrap.min.js"></script>
    <script src="${contextPath}/assets/js/common.min.js"></script>
    
    <script>
        var baseUrl = "${contextPath}/assets/imgs/";
        //手机号码验证
        $('#mobile').on('input', function () {
            var val = $(this).val();
            val = val.replace(/[^0-9]+/, "");
            $(this).val(val);
            inputMaxBlur($(this), 11);
        }).on('blur', function () {
            getTelInfo($(this));
            bindBtn();
        });

        //验证码验证
        $('#verifyCode').on('input', function () {
            var val = $(this).val();
            val = val.replace(/[^0-9]+/, "");
            $(this).val(val);
            inputMaxBlur($(this), 6);
        }).on('blur', function () {
            getCodeInfo($(this));
            bindBtn();
        });

        //获取验证码
        $('#getCodeBtn').on('click', function () {
            var that = this;
            var telNo = $('#mobile').val();
            if ($(this).hasClass('code-disabled')) {
                return false;
            } else {
                if ($('#mobile').data("validate") === true) {                    
                    $('#error-tip2').html('');
                    $(this).data('isClick', 'true');
                    var msgType="login";
                    ajaxData('${contextPath}/individual/flowredpacket/sendRandPass.html?${_csrf.parameterName}=${_csrf.token}', {mobile: telNo, msgType: msgType}, function (data) {
                        if(data.sc!=null && !data.sc){
                            $('#error-tip').html('仅限四川手机号参加哦~');
                        }else if(data.result!=null && data.result){
                            time($(that));
                        }else{
                            $('#error-tip').html('短信验证码发送失败！');
                        }
                    });
                } else {
                    if ($('#mobile').val() === '') {
                        $('#error-tip').html('请输入手机号码！');
                    }
                }
            }
        });

        //绑定手机号码
        $('#btn-submit').on('click', function () {
            bindTel();
        });

        /**
         * 验证手机号码
         */
        function getTelInfo(ele) {
            var tel = ele.val();
            var validate = false;
            if (!tel) {
                $('#error-tip').html('');
            } else if (/^1[3|4|5|7|8]\d{9}$/.test(tel)) {
                $('#error-tip').html('');
                validate = true;
                $('#getCodeBtn').css('color', '#90c31f');                
                
            } else {
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
        function bindBtn() {
            var phoneValidate = $('#mobile').data("validate") === true;
            var codeValidate = $('#verifyCode').data("validate") === true;
            var btnValidate = phoneValidate && codeValidate;
            if (btnValidate) {
                $('#btn-submit').removeClass('btn-disabled');
            } else {
                $('#btn-submit').addClass('btn-disabled');
            }
        }

        /**
         * 绑定手机号码
         */
        function bindTel() {
            var telEle = $('#mobile');
            var codeEle = $('#verifyCode');
            //验证
            getTelInfo(telEle);
            getCodeInfo(codeEle);
            bindBtn();
            var tel = telEle.val();
            var code = codeEle.val();
            if ($('#getCodeBtn').data('isClick') === 'true') {
                $.ajax({
                    url: '${contextPath}/individual/flowredpacket/login.html?${_csrf.parameterName}=${_csrf.token}',
                    type: 'POST',
                    dataType: 'json',
                    data: {tel: tel, code: code},
                    beforeSend: function () {
                        showToast();
                    },
                    success: function (data) {
                        if (data.success) {
                            window.location.href = "${contextPath}/individual/flowredpacket/list.html";
                        }else if(data.sc!=null && !data.sc){
                            showDialogContent('sichuan');
                        }else{
                            showDialogContent('error');
                        }
                    },
                    error: function () {
                        showDialogContent('busy');
                    },
                    complete: function () {
                        hideToast();
                    }
                });
            }
        }

        //显示弹窗
        function showDialogContent(clazz) {
            $('.modal-content').addClass('hide');
            $('.body-' + clazz).removeClass('hide');
            $('#tip-dialog').modal('show');
        }

        //输入字符数达到限制自动失去焦点
        function inputMaxBlur(ele, num) {
            if (ele.val().length === num) {
                window.setTimeout(function () {
                    ele.blur();
                }, 1);
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
                ele.html(+wait + 's');
                wait--;
                timer = setTimeout(function () {
                    time(ele);
                }, 1000);
            }
        }

        function showToast() {
            $('#loadingToast').modal('show');
        }

        function hideToast() {
            window.setTimeout(function () {
                $('#loadingToast').modal('hide');
            }, 500);
        }
    </script>
</body>
</html>