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
    <title>流量平台-选择角色</title>
    <meta name="keywords" content="流量平台 选择角色"/>
    <meta name="description" content="流量平台 选择角色"/>

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
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 10px;
            width: 100%;
        }

        .form {
            width: 345px;
            margin: 0 auto;
        }

        .tile-content {
            padding-bottom: 60px;
        }

        .module-header {
            padding: 15px;
            background-color: #475b75;
        }
    </style>
</head>
<body>
<div class="module-header mb-20">
    <img src="${contextPath}/assets/imgs/login_logo.png" style="width: 125px;">
</div>

<div class="main-container">
    <div class="tile mt-30">
        <div class="tile-header">
            修改密码
        </div>
        <div class="tile-content">
            <form class="form" method="post" id="form">
                <label class="mt-50 mb-20">为保障您的账户安全，请您配合回答以下问题，谢谢！</label>
                <div class="form-group">
                    <div>您的角色身份</div>
                    <select name="userRole" id="userRole">
                        <option value="">请选择</option>
                        <option value="1">企业管理员</option>
                        <option value="2">平台管理员</option>
                    </select>
                </div>
                <div id="entManager" hidden>
                    <div class="form-group">
                        <div>注册账号的企业名称</div>
                        <input type="text" name="entName" id="entName" required
                               data-bind="value: entName, valueUpdate: 'afterkeydown'">
                        <span class="error-tip diff-error"></span>
                    </div>
                    <div class="form-group">
                        <div>客户经理手机号码</div>
                        <input type="text" name="AMPhone" id="AMPhone" class="mobileOnly" maxlength="11" required>
                    </div>
                    <div class="form-group">
                        <div>企业管理员手机号码</div>
                        <input type="text" name="EMPhone" id="EMPhone" class="mobileOnly" maxlength="11" required>
                    </div>
                    <div class="mt-30 text-center">
                        <a class="btn btn-warning btn-sm mr-10 dialog-btn" data-flag="1" style="width: 114px;">提交验证</a>
                    </div>
                </div>
                <div id="platformManager" hidden>
                    <div class="form-group">
                        <div>账号姓名</div>
                        <input type="text" name="userName" id="userName" maxlength="64" required
                               data-bind="value: userName, valueUpdate: 'afterkeydown'">
                        <span class="error-tip diff-error"></span>
                    </div>
                    <div class="form-group">
                        <div>手机号码</div>
                        <input type="text" name="phone" id="phone" class="mobileOnly" maxlength="11" required>
                    </div>
                    <div class="mt-30 text-center">
                        <a class="btn btn-warning btn-sm mr-10 dialog-btn" data-flag="2" style="width: 114px;">提交验证</a>
                        <div id="msg-error" class="error-tip"></div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        checkFormValidate();
        roleChange($("#userRole"));

        $("#userRole").on("change", function () {
            roleChange($(this));
        });

        $("#entName").on("input",function(){
            $(".diff-error").html("");
        });

        $("#userName").on("input",function(){
            $(".diff-error").html("");
        });

        $(".dialog-btn").on("click", function () {
            $("#msg-error").html("");
            if ($("#form").validate().form()) {
                var button = $(this).data("flag");
                var data = {};
                if (button == '1') {
                    var entName = $("#entName").val();
                    var AMPhone = $("#AMPhone").val();
                    var EMPhone = $("#EMPhone").val();
                    data = {
                        "entName": entName,
                        "customerManagerMobile": AMPhone,
                        "mobile": EMPhone,
                        "type":1
                    }
                } else if (button == '2') {
                    var userName = $("#userName").val();
                    var phone = $("#phone").val();
                    data = {
                        "userName": userName,
                        "mobile": phone,
                        "type":2
                    }
                }
                $.ajax({
                    beforeSend: function (request) {
                        var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                        var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                        request.setRequestHeader(header1, token1);
                    },
                    url:"${contextPath}/manage/resetpwd/verifyInfo.html",
                    type:"post",
                    dataType:"json",
                    data:data,
                    success: function(data){
                        var isRight = data.verify;
                        if(!isRight){
                            $(".diff-error").html("请输入正确的账号信息");
                            $(".diff-error","div:hidden .form-group").html("");
                        }else{
                            $(".diff-error").html("");
                            window.location.href="modifyPwd.html";//跳转到输入新密码页                                          
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        if(XMLHttpRequest.status == 403){
                            window.location.href = "${contextPath}/manage/user/login.html";
                        }else{
                            alert("网络出错，请重新尝试");
                        }
                    }            
                });
            }
            return false;
        });
    });

    function roleChange(ele){
        var role = ele.val();
        if(role == '1'){
            $("#entManager").show();
            $("#platformManager").hide();
        }else if (role == '2') {
            $("#entManager").hide();
            $("#platformManager").show();
        } else {
            $("#entManager").hide();
            $("#platformManager").hide();
        }
    }


    function checkFormValidate() {
        $("#form").validate({
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
                entName: {
                    required: true
                },
                AMPhone: {
                    required: true,
                    mobile: true
                },
                EMPhone: {
                    required: true,
                    mobile: true
                },
                userName: {
                    required: true
                },
                phone: {
                    required: true,
                    mobile: true
                }
            },
            messages: {
                entName: {
                    required: "请输入正确的企业名称"
                },
                AMPhone: {
                    required: "请输入手机号码",
                    mobile: "请输入正确的手机号码"
                },
                EMPhone: {
                    required: "请输入手机号码",
                    mobile: "请输入正确的手机号码"
                },
                userName: {
                    required: "请输入账号姓名"
                },
                phone: {
                    required: "请输入手机号码",
                    mobile: "请输入正确的手机号码"
                }
            }
        });
    }

</script>
</body>
</html>