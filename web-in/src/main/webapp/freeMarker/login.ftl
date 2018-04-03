<!DOCTYPE html>
<#global  contextPath = rc.contextPath />

<html>
<head>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <meta charset="utf-8"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>登录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <!-- basic styles -->
    <link href="${contextPath}/manage/bootstrap3/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/font-awesome.min.css"/>

    <!--[if IE 7]>
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/font-awesome-ie7.min.css"/>
    <![endif]-->

    <!-- page specific plugin styles -->

    <!-- fonts -->

    <!-- ace styles -->

    <link rel="stylesheet" href="${contextPath}/manage/assets/css/ace.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/ace-rtl.min.css"/>

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="${contextPath}/manage/assets/css/ace-ie.min.css"/>
    <![endif]-->

    <!-- inline styles related to this page -->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <!--[if lt IE 9]>
    <script src="${contextPath}/manage/assets/js/html5shiv.js"></script>
    <script src="${contextPath}/manage/assets/js/respond.min.js"></script>
    <![endif]-->
    <style>
        body {
            background: #f0f1f9 url("${contextPath}/manage/assets/images/login_bg.jpg") no-repeat top center;
            background-size: 100% 100%;
        }

        .login-layout {
            background-color: transparent;
        }

        .modal-backdrop.fade.in {
            background-color: #212629;
            opacity: .6;
            filter: alpha(opacity=60);
            z-index: 0;
        }

        .login-container {
            margin-top: 80px;
            width: 300px;
        }

        .login-container .center {
            margin-bottom: 50px;
        }

        #login-box {
            color: #f1f3f4;
            font-size: 16px;
        }

        #login-box .tab-content {
            margin-top: 20px;
            border: none;
            padding-left: 0;
            padding-right: 0;
            overflow: hidden !important;
        }

        .nav-tabs li {
            width: 100%
        }

        .nav-tabs > li > a {
            text-align: center
        }

        /*.nav-tabs>li.active>a{ background-color: transparent; border-width: 0 0 2px 0; border-color:#42bdff;  }*/
        .nav-tabs > li > a, .nav-tabs > li > a:hover, .nav-tabs > li > a:focus {
            background-color: transparent;
            border: none;
            color: #f1f3f4
        }

        .nav-tabs > li.active > a, .nav-tabs > li.active > a:hover, .nav-tabs > li.active > a:focus {
            background-color: transparent;
            border-width: 0 0 2px 0;
            border-color: #42bdff;
            color: #42bdff;
            box-shadow: none;
        }

        .tab-content input {
            height: 22px;
            padding: 0;
        }

        .tab-content input, .tab-content button {
            border: none;
            text-indent: 8px;
            outline: none;
        }

        .code-group input {
            float: left;
            width: 100%
        }

        .code-group img {
            float: right;
            width: 30%;
            height: 45px;
        }

        .tab-content button, .tab-content button:hover, .tab-content button:focus {
            width: 100%;
            padding: 0;
            line-height: 35px;
            font-size: 16px;
            background-color: #399fd6;
            background-color: #399fd6 !important;
            border: none;
        }

        .code-group .get-code, .code-group:hover .get-code, .code-group .get-code:focus {
            float: right;
            width: 40%;
            background-color: #ffac53 !important;
            padding: 5px;
            border-radius: 2px !important;
        }

        #msg, #smsMsg, #smgPrompt {
            color: #CD9165;
            font-size: 14px;
        }

        .form-row {
            position: relative;
            height: 45px;
            padding: 12px 0;
            background: white;
            border-radius: 2px !important;
            margin-bottom: 15px;
            box-sizing: border-box;
        }

        .placeholder {
            position: absolute;
            color: #858585;
            z-index: 1;
            top: 9px;
            left: 14px;
            cursor: text;
            white-space: nowrap;
        }

        select.form-control {
            height: 17px;
            padding: 0 6px;
            border: none;
        }

        a:link, a:visited, a:hover,a:active {
            outline: 0;
            color: #42bdff;
            text-decoration: none;
        }
        #forgetPwd{
            display: inline-block;
            margin-top: 5px;
            font-size: 14px;
        }
    </style>

    <script type="text/javascript" src="${contextPath}/manage/Js/jquery.js"></script>
    <script type="text/javascript" src="${contextPath}/manage/Js/ckform.js"></script>
    <script type="text/ecmascript" src="${contextPath}/manage/Js/md5.js"></script>

</head>
<body>
<div class="login-layout">
    <div class="modal-backdrop fade in"></div>
    <div class="main-container">
        <div class="main-content">
            <div class="row">
                <div class="col-sm-10 col-sm-offset-1">
                    <div class="login-container">
                        <div class="center">
                            <img src="${contextPath}/manage/assets/images/login_logo.png">
                        </div>

                        <div class="space-6"></div>

                        <div class="position-relative">
                            <div id="login-box" role="tabpanel">

                                <!-- Nav tabs -->
                                <ul class="nav nav-tabs" role="tablist">
                                    <li role="presentation" class="active"><a href="#home" aria-controls="home"
                                                                              role="tab" data-toggle="tab">登 录</a></li>
                                </ul>

                                <!-- Tab panes -->
                                <div class="tab-content">
                                    <div role="tabpanel" class="tab-pane active" id="home">
                                        <input type="hidden" id="key" value="${Session.SESSION_LOGIN_TOKEN}"/>
                                        <form id="frmLogin" method="post"
                                              action="${contextPath}/j_spring_security_check">
                                            <div class="form-row">
                                                <input type="text" name="j_username" id="j_username"
                                                       value="${username!}" class="form-control" placeholder="手机号码" style="display: none;">
                                                <input type="text" id="j_username_origin" onkeyup="j_username.value=this.value" class="form-control" placeholder="手机号码" autocomplete="off">
                                                <!--[if lt IE 10]>
                                                <label class="placeholder">手机号码</label>
                                                <![endif]-->
                                            </div>
                                        <#if logintype??&&logintype=='1'>
                                            <div class="form-row">
                                                <select class="form-control" id="type" name="type"
                                                        onchange="chooseType()">
                                                    <option value="1" selected>静态密码登录</option>
                                                    <!-- <option value="2">随机密码登录</option> -->
                                                </select>
                                            </div>

                                            <div class="form-row" id="psw">
                                                <input type="password" id="j_code_origin" onkeyup="j_code.value=this.value" placeholder="静态密码" class="form-control" autocomplete="off">
                                                <input type="password" name="j_code" id="j_code" style="display: none;">
                                                <!--[if lt IE 10]>
                                                <label class="placeholder">密码</label>
                                                <![endif]-->
                                            </div>
                                            <div class="code-group clearfix" id="msgPsw" style="display:none">
                                                <div class="form-row" style="width: 58%; float: left;">
                                                    <input type="text" name="randomPassword" id="randomPassword" class="form-control input-code" placeholder="短信验证码">
                                                    <!--[if lt IE 10]>
                                                    <label class="placeholder">手机验证码</label>
                                                    <![endif]-->
                                                </div>
                                                <button type="button" name="hqyzmagain" id="hqyzmagain"
                                                        class="btn btn-default get-code" onclick="sendAgain()">获取随机密码
                                                </button>
                                            </div>
                                        </#if>
                                        <#if logintype??&&logintype=='2'>
                                            <div class="form-row">
                                                <select class="form-control" id="type" name="type" onchange="chooseType()">
                                                    <!-- <option value="1">静态密码登录</option> -->
                                                    <option value="2" selected>随机密码登录</option>
                                                </select>
                                            </div>

                                            <div class="form-row" id="psw" style="display:none">
                                                <input type="password" id="j_code_origin" onkeyup="j_code.value=this.value" placeholder="静态密码" class="form-control" autocomplete="off">
                                                <input type="password" name="j_code" id="j_code" style="display: none;">
                                                <!--[if lt IE 10]>
                                                <label class="placeholder">密码</label>
                                                <![endif]-->
                                            </div>
                                            <div class="code-group clearfix" id="msgPsw">
                                                <div class="form-row" style="width: 58%; float: left;">
                                                    <input type="text" name="randomPassword" id="randomPassword" class="form-control input-code" placeholder="短信验证码">
                                                    <!--[if lt IE 10]>
                                                    <label class="placeholder">手机验证码</label>
                                                    <![endif]-->
                                                </div>
                                                <button type="button" name="hqyzmagain" id="hqyzmagain"
                                                        class="btn btn-default get-code" onclick="sendAgain()" style="text-indent: 0;font-size: 15px;">获取随机密码
                                                </button>
                                            </div>
                                        </#if>
                                        <#if logintype??&&logintype=='3'>
                                            <div class="form-row">
                                                <select class="form-control" id="type" name="type"
                                                        onchange="chooseType()">
                                                    <option value="1" selected>静态密码登录</option>
                                                    <option value="2">随机密码登录</option>
                                                </select>
                                            </div>

                                            <div class="form-row" id="psw">
                                                <input type="password" id="j_code_origin" onkeyup="j_code.value=this.value" placeholder="静态密码" class="form-control" autocomplete="off">
                                                <input type="password" name="j_code" id="j_code" style="display: none;">
                                                <!--[if lt IE 10]>
                                                <label class="placeholder">密码</label>
                                                <![endif]-->
                                            </div>
                                            <div class="code-group clearfix" id="msgPsw" style="display:none">
                                                <div class="form-row" style="width: 58%; float: left;">
                                                    <input type="text" name="randomPassword" id="randomPassword" class="form-control input-code" placeholder="短信验证码">
                                                    <!--[if lt IE 10]>
                                                    <label class="placeholder">手机验证码</label>
                                                    <![endif]-->
                                                </div>
                                                <button type="button" name="hqyzmagain" id="hqyzmagain"
                                                        class="btn btn-default get-code" onclick="sendAgain()" style="text-indent: 0;font-size: 15px;">获取随机密码
                                                </button>
                                            </div>
                                        </#if>
                                        <#if logintype??&&logintype=='4'>
                                            <div class="form-row">
                                                <select class="form-control" id="type" name="type"
                                                        onchange="chooseType()">
                                                    <!-- <option value="1" disabled="">静态密码登录</option>
                                                    <option value="2" disabled="">随机密码登录</option> -->
                                                    <option value="3" selected>静态/随机密码登录</option>
                                                </select>

                                            </div>
                                            <div class="form-row" id="psw">
                                                <input type="password" id="j_code_origin" onkeyup="j_code.value=this.value" placeholder="静态密码" class="form-control" autocomplete="off">
                                                <input type="password" name="j_code" id="j_code" style="display: none;">
                                                <!--[if lt IE 10]>
                                                <label class="placeholder">密码</label>
                                                <![endif]-->
                                            </div>
                                            <div class="code-group clearfix" id="msgPsw">
                                                <div class="form-row" style="width: 58%; float: left;">
                                                    <input type="text" name="randomPassword" id="randomPassword" class="form-control input-code" placeholder="短信验证码">
                                                    <!--[if lt IE 10]>
                                                    <label class="placeholder">手机验证码</label>
                                                    <![endif]-->
                                                </div>
                                                <button type="button" name="hqyzmagain" id="hqyzmagain"
                                                        class="btn btn-default get-code" onclick="sendAgain()" style="text-indent: 0;font-size: 15px;">获取随机密码
                                                </button>
                                            </div>
                                        </#if>
                                            <div class="code-group clearfix">
                                                <div class="form-row" style="width: 58%; float: left;">
                                                    <input type="text" name="verify" id="verify"
                                                           class="form-control input-code" placeholder="图片验证码">
                                                    <!--[if lt IE 10]>
                                                    <label class="placeholder">图片验证码</label>
                                                    <![endif]-->
                                                </div>
                                                <img id="identity"
                                                     src="${contextPath}/manage/virifyCode/getImg.html?_=${.now?datetime}"
                                                     style="width: 120px;" onclick="refresh()" title="点击更换图片"/>
                                            </div>

                                            <button onclick="doSubmit(); event.returnValue = false; return false;"
                                                    class="btn btn-default" type="submit" id="btnSubmit">登 录
                                            </button>
                                            <#if resetPwd?? && resetPwd>
                                            <a href="${contextPath}/manage/resetpwd/reset.html" id="forgetPwd">忘记密码</a>
                                            </#if>
                                        <#if errorMsg?exists>
                                            <h1 id="smsMsg">${errorMsg}</h1>
                                        <#elseif SPRING_SECURITY_LAST_EXCEPTION?exists>
                                            <h1 id="smsMsg">${SPRING_SECURITY_LAST_EXCEPTION.message}</h1>
                                        <#else>
                                            <h1 id="smsMsg"></h1>
                                        </#if>
                                            <h1 id="smgPrompt"></h1>
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        </form>
                                    </div>
                                </div>

                            </div>

                        </div><!-- /position-relative -->
                    </div>
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div>
    </div><!-- /.main-container -->
</div>

   <div class="modal fade dialog-sm" id="tip-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-body">
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>

<!-- basic scripts -->
<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='${contextPath}/manage/assets/js/jquery-2.0.3.min.js'>" + "<" + "/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='${contextPath}/manage/assets/js/jquery-1.10.2.min.js'>" + "<" + "/script>");
</script>
<![endif]-->

<script type="text/javascript">
    if ("ontouchend" in document) document.write("<script src='${contextPath}/manage/assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>
<script src="${contextPath}/manage/bootstrap3/js/bootstrap.min.js"></script>
<script src="${contextPath}/assets/lib/store.min.js"></script>
<script src="${contextPath}/assets/lib/crypto-js/core.js"></script>
<script src="${contextPath}/assets/lib/crypto-js/crypto-js.js"></script>
<script src="${contextPath}/assets/lib/crypto-js/aes.js"></script>
<script src="${contextPath}/assets/lib/crypto-js/mode-ecb.js"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
    chooseType();

    //跳出框架
    $(function () {
        if (top != self) {
            top.location = "${contextPath}" + "/manage/user/login.html"
        }
    });
    function refresh() {
        //IE存在缓存,需要new Date()实现更换路径的作用
        document.getElementById("identity").src = "${contextPath}/manage/virifyCode/getImg.html?"
                + new Date();
    }
    String.prototype.trim = function () {
        return this.replace(/(^\s*)|(\s*$)/g, "");
    };

    //默认回车确认
    document.onkeydown = function () {
        if (event.keyCode == 13)//回车键
        {
            return true;
        }
    };

    var key = $('#key').val();
    var parseKey  = CryptoJS.enc.Utf8.parse(key);

    function Encrypt(str){
        var srcs = CryptoJS.enc.Utf8.parse(str);
        var encrypted = CryptoJS.AES.encrypt(srcs, parseKey, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
        return encrypted.ciphertext.toString();
    }

    //加密处理
    function encryptFun(){
        var password = Encrypt($("input[name='j_code']").val());
        var mobile = Encrypt($("input[name='j_username']").val());
        $("input[name='j_code']").val(password);
        $("input[name='j_username']").val(mobile);
    }

    function show_box(id) {
        jQuery('.widget-box.visible').removeClass('visible');
        jQuery('#' + id).addClass('visible');
    }

    var wait = 60;
    document.getElementById("hqyzmagain").disabled = false;
    function time(o) {
        if (wait == 0) {
            o.removeAttribute("disabled");
            o.innerHTML = "重新获取";
            wait = 60;
        } else {
            o.setAttribute("disabled", true);
            o.innerHTML = wait + "秒后重新获取";
            wait--;
            setTimeout(function () {
                time(o)
            }, 1000);
        }
    }

    function chooseType() {
        var type = $("#type").val();
        if(type=="1"){
            $("#msgPsw").hide();
            $("#psw").show();
        }

        if(type=="2"){
            $("#msgPsw").show();
            $("#psw").hide();
        }

        if(type=="3"){
            $("#msgPsw").show();
            $("#psw").show();
        }

    }
    //重新发送验证码
    function sendAgain() {
        if (yz($("#j_username_origin").val())) {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $("meta[name='_csrf']").attr("content");
                    var header1 = $("meta[name='_csrf_header']").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: '${contextPath}/manage/message/sendOutLoginMessage.html',
                type: 'post',
                data: {
                    mobilePhone: $('#j_username_origin').val()
                },
                async: true,
                dataType: "json", //指定服务器的数据返回类型，
                success: function (data) {
                    if (data && data.message) {
                        if ("短信发送成功!" == data.message) {

                            time(document.getElementById("hqyzmagain"));
                            $("#inputButton").hide();
                            $("#inputCode").show();
                            $("#smgPrompt").html("短信验证码已发送至您的手机！");

                        }
                        else if ("请勿在1分钟之内重复发送" == data.message) {
                            $("#inputButton").hide();
                            $("#inputCode").show();
                            $("#smgPrompt").html(data.message);
                        }
                        else {
                            $("#smgPrompt").html(data.message);

                        }
                    }
                },
                error: function () {
                    var message = "网络出错，请重新尝试";
                    $("#smgPrompt").html(message);

                }
            });
        }
        else {
            $("#smsMsg").html("手机号码为空或格式不正确!");
        }
    }

    $(function () {
        $("#j_username_origin").attr('placeholder', '手机号码');
        $("#inputButton").show();
        $("#inputCode").hide();
        $("#inputName").show();
        $("#inputPass").show();
    });

    $("#j_username_origin").focus(function () {
        $("#msg").html("");
        $("#smsMsg").html("");
    });

    $("#j_code_origin").focus(function () {
        $("#msg").html("");
        $("#smsMsg").html("");
    });

    $("#randomPassword").focus(function () {
        $("#msg").html("");
        $("#smsMsg").html("");
    });

    $("#verify").focus(function () {
        $("#msg").html("");
        $("#smsMsg").html("");
    });
    $("#j_username_origin").blur(function () {
        $("input[name='j_username']").val($("#j_username_origin").val());
        var temp = $("input[name='j_username']").val();

        if (isEmptyOrNull(temp)) {
            $("#smsMsg").html("手机号码不能为空!");
        } else {
            if (isNaN(temp)) {
                $("#smsMsg").html("请输入正确的手机号码!");
            } else {
                if (temp.length == 11) {
                    $("#smsMsg").html("");
                } else {
                    $("#smsMsg").html("请输入正确的手机号码!");
                }
            }
        }
    });

    $("#j_code_origin").blur(function () {
        $("input[name='j_code']").val($("#j_code_origin").val());
        var temp = $("input[name='j_code']").val();

        if (isEmptyOrNull(temp)) {
            $("#smsMsg").html("静态密码不能为空!");
        } else {
            $("#smsMsg").html("");
        }
    });

    $("input[name='verify']").blur(function () {
        var temp = $("input[name='verify']").val();

        if (isEmptyOrNull(temp)) {
            $("#smsMsg").html("验证码不能为空!");
        } else {
            $("#smsMsg").html("");
        }
    });
    function isEmptyOrNull(str) {
        return (str == null || str.trim().length == 0);
    }

    function checkPhone(ph) {
        var a = /^((\(\d{3}\))|(\d{3}\-))?13\d{9}|14[5-9]\d{8}|15[0-3,5-9]\d{8}|17[0-8]\d{8}|18\d{9}|19[89]\d{8}$/;
        if (ph.length != 11 || !ph.match(a)) {
            return false;
        }
        return true;
    }

    function doSubmit() {
        //清除缓存
        store.remove("sc_menu_identity");
        sumitAdd();
    }

    function sumitAdd() {
        var type = $("#type").val();
        $("input[name='j_username']").val($("#j_username_origin").val());
        $("input[name='j_code']").val($("#j_code_origin").val());
        flag = true;
        var temp = $("#j_username_origin").val();
        if (isEmptyOrNull(temp)) {
            flag = false;
            $("#smsMsg").html("手机号码不能为空!");
            return;
        }

        if (!checkPhone(temp)) {
            flag = false;
            $("#smsMsg").html("请输入正确的手机号码!");
            return;
        }

        var temp = $("input[name='randomPassword']").val();
        if (type != "1" && isEmptyOrNull(temp)) {
            flag = false;
            $("#smsMsg").html("短信验证码必需填写!");
            return;
        }

        var temp = $("input[name='verify']").val();
        if (isEmptyOrNull(temp)) {
            flag = false;
            $("#smsMsg").html("验证码不能为空!");
            return;
        }

        var temp = $("input[name='j_code']").val();
        if (type != "2") {
            if (isEmptyOrNull(temp)) {
                flag = false;
                $("#smsMsg").html("静态密码不能为空!");
                return;
            } else {
                $("input[name='j_code']").val(temp);
            }
        }

        if (flag) {
               checkPasswordUpdateTime();
        }
    }

    //出现密码超期提示后，点击确定提交表单
    $("#ok").on("click", function () {
        encryptFun();
        $("#frmLogin").submit();
    });

        //校验密码更新时间
     	function checkPasswordUpdateTime(){
                var type = $("#type").val();
                var password = Encrypt($("input[name='j_code']").val());
                var imgCode = $("input[name='verify']").val();
                var verifyCode = $("input[name='randomPassword']").val();
                var mobile = Encrypt($("input[name='j_username']").val());
                $.ajax({
            type: "POST",
            url: "${contextPath}/manage/userInfo/checkPasswordUpdateTime.html?${_csrf.parameterName}=${_csrf.token}",
            data: {
                 mobile: mobile,
                 password: password,
                 type: type,
                 imgCode: imgCode,
                 verifyCode: verifyCode

            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                if (data.expire == "true") {
                        var msg = "您已超过" + data.date + "天未更换密码";
                    $("#tip-dialog .message-content").html(msg);
                                $("#tip-dialog").modal("show");

                }
                else {
                    encryptFun();
                    $("#frmLogin").submit();
                }

            },
            error: function () {

            }
        });
    }

    function yz(v) {
        var a = /^((\(\d{3}\))|(\d{3}\-))?13\d{9}|14[5-9]\d{8}|15[0-3,5-9]\d{8}|17[0-8]\d{8}|18\d{9}|19[89]\d{8}$/;
        if (v.length != 11 || !v.match(a)) {
            $("#nameS").html("手机号码不能为空或者格式不正确!");
            return false;
        }
        return true;
    }
    function polyFillPlaceHolder() {
        jQuery("input.form-control").each(function () {
            if (this.value !== "") {
                $(this).next("label").hide();
            }
        });
        jQuery("input.form-control").on("keydown.placeholder", function () {
            $(this).next("label").hide();
        });
        jQuery("input.form-control").on("blur.placeholder", function () {
            if (this.value === "") {
                $(this).next("label").show();
            }
        });

        jQuery(".placeholder").on("click", function () {
            $(this).prev("input").focus();
        });
    }
    polyFillPlaceHolder();
</script>
</body>
</html>