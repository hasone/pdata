<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-更新企业管理员</title>
    <meta name="keywords" content="流量平台 更新企业管理员"/>
    <meta name="description" content="流量平台 更新企业管理员"/>

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

        .form input[type='text'] {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 5px;
            width: 180px;
        }

        .form .form-group label {
            width: 160px;
            text-align: right;
            margin-left: 15px;
        }
    </style>
</head>
<body>
<div class="main-container">
    <form action="${contextPath}/manage/enterprise/saveEnterManager.html" method="post" name="changeEnterManagerForm"
          id="changeEnterManagerForm">
        <div class="module-header mt-30 mb-20">
            <h3>更新企业管理员
                <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
            </h3>
        </div>


        <input type="hidden" id="enterId" name="enterId" value="${enterprise.id!}">
        <input type="hidden" id="originMobilePhone" name="originMobilePhone" value="${administer.mobilePhone!}">

        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>企业名称：</label>
                        <span>${enterprise.name!}</span>
                    </div>
                    <div class="form-group">
                        <label>原管理员姓名：</label>
                        <span>${administer.userName!}</span>
                    </div>

                    <div class="form-group">
                        <label>原管理员手机号码：</label>
                        <span>${administer.mobilePhone!}</span>
                    </div>
                    <div class="form-group">
                        <label>操作者手机号码：</label>
                        <span>${currentMobilePhone}</span>
                        <input type="hidden" name="currentMobilePhone" id="currentMobilePhone" value="${currentMobilePhone}">
                    </div>
                    <div class="form-group">
                        <label>验证码：</label>
                        <input type="text" name="verifyCode1" id="verifyCode1" placeholder="验证码" class="mobileOnly" maxlength="6"/>
                        <input type="button" name="hqyzm1" id="hqyzm1" value="点击获取" class="btn btn-sm btn-success">
                    </div>

                    <div class="form-group">
                        <label>新管理员手机机号码：</label>
                        <input type="text" name="mobilePhone" id="mobilePhone" class="mobileOnly" onblur="getUserName()" maxlength="11">
                    </div>
                    <div class="form-group">
                        <label>验证码：</label>
                        <input type="text" name="verifyCode2" id="verifyCode2" placeholder="验证码" class="mobileOnly" maxlength="6"/>
                        <input type="button" name="hqyzm2" id="hqyzm2" value="点击获取" class="btn btn-sm btn-success">
                    </div>
                    <div class="form-group">
                        <label>新管理员姓名：</label>
                        <input type="text" name="userName" id="userName">
                    </div>
 					<div class="form-group">
                        <label>新管理员邮箱：</label>
                        <input type="text" name="email" id="email">
                    </div>
                </div>
            </div>
        </div>


        <div class="mt-30">
            <input type="submit" class="btn btn-sm btn-warning dialog-btn" value="保存"/>&nbsp;&nbsp;&nbsp;
            <span style="color:red" id="error_msg">${errorMsg!}</span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        sendMessage();
        checkFormValidate();
    });


    function checkFormValidate() {
        $("#changeEnterManagerForm").packValidate({
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
                verifyCode1: {
                    required: true,
                    minlength: 6,
                    maxlength: 6,
                    number: true
                },
                verifyCode2: {
                    required: true,
                    minlength: 6,
                    maxlength: 6,
                    number: true
                },
                mobilePhone: {
                    required: true,
                    mobile: true,
                    remote: {
                        url: "checkRoleExtist.html",
                        type: "get",
                        dataType: "text"
                    }
                },
                userName: {
                    required: true,
                    maxlength: 32
                },
                email: {
                    required: true,
                    maxlength: 64,
                    email: true
                }
            },
            messages: {
                verifyCode1: {
                    required: "请输入短信验证码",
                    minlength: "验证码为6位数字",
                    maxlength: "验证码为6位数字",
                    positive: "验证码为6位数字"
                },
                verifyCode2: {
                    required: "请输入短信验证码",
                    minlength: "验证码为6位数字",
                    maxlength: "验证码为6位数字",
                    positive: "验证码为6位数字"
                },
                mobilePhone: {
                    required: "账号手机不能为空",
                    mobile: "请填写正确的手机号码",
                    remote: "该用户已分配了角色"
                },
                userName: {
                    required: "姓名不能为空",
                    maxlength: "姓名不超过32字符"
                },
                email: {
                    required: "邮箱不能为空",
                    maxlength: "邮箱不能超过64位",
                    email: "请正确填写电子邮箱！"
                }
            }
        });
    }
</script>

<script>
    var wait1 = 60;
    var wait2 = 60;
    document.getElementById("hqyzm1").disabled = false;
    document.getElementById("hqyzm2").disabled = false;
    function time1(o) {
        if (wait1 == 0) {
            o.removeAttribute("disabled");
            o.value = "重新获取";
            wait1 = 60;
        } else {
            o.setAttribute("disabled", true);
            o.value = wait1 + "秒后重新获取";
            wait1--;
            setTimeout(function () {
                        time1(o)
                    },
                    1000)
        }
    }
    function time2(o) {
        if (wait2 == 0) {
            o.removeAttribute("disabled");
            o.value = "重新获取";
            wait2 = 60;
        } else {
            o.setAttribute("disabled", true);
            o.value = wait2 + "秒后重新获取";
            wait2--;
            setTimeout(function () {
                        time2(o)
                    },
                    1000)
        }
    }

    function sendMessage() {
        $("#hqyzm1").click(function () {
            $("#error_msg").empty();
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: '${contextPath}/manage/message/sendChangeMobilePhone.html',
                type: 'get',
                data: 'mobilePhone=' + $("#currentMobilePhone").val() + '&username=' + $("#currentMobilePhone").val(),
                dataType: "json", //指定服务器的数据返回类型，
                async: true, //默认为true 异步
                error: function () {
                    $("#error_msg").append("网络错误，请重新尝试！");
                },
                success: function (data) {

                    if ("短信发送成功!" == data.message) {
                        time1(document.getElementById("hqyzm1"));
                        $("#error_msg").append("短信发送成功!");
                    }
                    else {
                        $("#error_msg").append(data.message);
                    }

                }
            });
        });

        $("#hqyzm2").click(function () {
            $("#error_msg").empty();
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: '${contextPath}/manage/message/sendChangeMobilePhone.html',
                type: 'get',
                data: 'mobilePhone=' + $("#mobilePhone").val() + '&username=' + $("#mobilePhone").val(),
                dataType: "json", //指定服务器的数据返回类型，
                async: true, //默认为true 异步
                error: function () {
                    $("#error_msg").append("网络错误，请重新尝试！");
                },
                success: function (data) {
                    if ("短信发送成功!" == data.message) {
                        time2(document.getElementById("hqyzm2"));
                        $("#error_msg").append("短信发送成功!");
                    }
                    else {
                        $("#error_msg").append(data.message);
                    }

                }
            });
        });
    }


    function getUserName() {
        var phone = $("#mobilePhone").val();
        $("#userName").val("");
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/user/getNameByPhone.html",
            data: {
                phone: phone
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (res) {
                if (res.exist == "yes") {
                    $("#userName").empty();
                    $("#userName").val(res.name);
                    $("#userName").attr("readonly", "readonly")//将input元素设置为readonly

                }
                else {
                    $("#userName").removeAttr("readonly");
                }
            },
            error: function () {
            }
        });
    }
    ;
</script>
</body>
</html>