<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
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
    <script type="text/ecmascript" src="${contextPath}/manage/Js/md5.js"></script>
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
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>账号信息
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <form name="adminForm" role="form" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-header">
                修改密码
            </div>
            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>手机号码:</label>
                        <input type="text" name="mobilePhone" id="mobilePhone" readonly='readonly'
                               value='${admin.mobilePhone!}'/></label>
                    </div>
                <#if logintype??&&logintype='1'>
                <div class="form-group">
                        <label>旧密码:</label>
                        <input type="password" name="sjmmInput" id="sjmmInput" placeholder="旧密码" required/>
                    </div>
                </#if>
                <#if logintype??&&logintype!='1'>
                <div class="form-group">
                        <label>验证码:</label>
                        <input type="text" name="sjmmInput" id="sjmmInput" placeholder="验证码" required/>
                        <input type="button" name="hqyzm" id="hqyzm" value="点击获取" class="btn btn-sm btn-success"">
                    </div>
                </#if>
                    

                    <div class="form-group">
                        <label>创建新密码:</label>
                        <input type="password" name="saisirPass" id="saisirPass" required/>
                    </div>

                    <div class="form-group">
                        <label>确认密码:</label>
                        <input type="password" name="confirmPass" id="confirmPass" required/>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-save mt-30">
            <a class="btn btn-sm btn-warning dialog-btn" id="save-btn" href="javascript:void(0);">保存</a>&nbsp;&nbsp;&nbsp;
            <span style="color:red" id="error_msg">${errorMsg!}</span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

    <form name="adminForm"
          action="${rc.contextPath}/manage/userInfo/savePassword.html"
          method="post" class="form-horizontal" role="form" id="table_submit">

        <input type="hidden" name="mobilePhone" id="mobilePhone" value='${admin.mobilePhone!}'/>
        <input type="hidden" name="sjmm" id="sjmm"/>
        <input type="hidden" name="passWord" id="passWord"/>
        <input type="hidden" name="passWord2" id="passWord2"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        checkFormValidate();
        //sumitAdd();
        sendMessage();

        $("#save-btn").on("click", function () {
            if ($("#table_validate").validate().form()) {
                sumitAdd();
            }

            return false;
        });
    });
</script>

<script type="text/javascript">
    var wait = 60;
    document.getElementById("hqyzm").disabled = false;
    function time(o) {

        if (wait == 0) {
            o.removeAttribute("disabled");
            o.value = "重新获取";
            wait = 60;
        } else {
            o.setAttribute("disabled", true);
            o.value = wait + "秒后重新获取";
            wait--;
            setTimeout(function () {
                        time(o)
                    },
                    1000)
        }
    }

    function onSubmit() {
        sumitAdd();
    }
</script>

<script>
    function checkFormValidate() {
        $("#table_validate").packValidate({
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
                sjmmInput: {
                    required: true
                },
                saisirPass: {
                    required: true,
                    minlength: 10,
                    maxlength: 20,
                    strictPwd: true,

                },
                confirmPass: {
                    required: true,
                    minlength: 10,
                    maxlength: 20,
                    verifyPwd: $('#saisirPass')
                }
            },
            messages: {
                sjmmInput: {
                    required: "验证码不能为空"
                },
                saisirPass: {
                    required: "新密码不能为空",
                },
                confirmPass: {
                    required: "确认密码不能为空",
                    verifyPwd: "密码不一致！"
                }
            }
        });
    }

    function isEmptyOrNull(str) {
        return (str == null || str.trim().length == 0);
    }
    ;

    function sumitAdd() {
        flag = true;

        var temp = $("input[name='sjmmInput']").val();

        if (isEmptyOrNull(temp)) {
            flag = false;
            return flag;
        }
        else {
            $('#sjmm').val(temp);
        }


        var temp = $("input[name='saisirPass']").val();

        if (isEmptyOrNull(temp)) {
            flag = false;
            return flag;
        }
        else {
            $('#passWord').val(temp);
        }


        var temp = $("input[name='confirmPass']").val();

        if (isEmptyOrNull(temp)) {
            flag = false;
            return flag;
        }
        else {
            $('#passWord2').val(temp);
        }

        $('#table_submit').submit();
        return false;
    }
    ;

    function sendMessage() {
        $("#hqyzm").click(function () {
            $("#getMsg").empty();
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: '${contextPath}/manage/message/sendChangePassMessage.html',
                type: 'POST',
                data: 'mobilePhone=' + $("#mobilePhone").val() + '&username=' + $("#mobilePhone").val(),
                dataType: "json", //指定服务器的数据返回类型，
                async: true, //默认为true 异步
                error: function () {
                    $("#getMsg").append("网络错误，请重新尝试！");
                },
                success: function (data) {

                    if ("短信发送成功!" == data.message) {
                        time(document.getElementById("hqyzm"));
                        $("#getMsg").append("短信发送成功!");
                    }
                    else {
                        $("#getMsg").append(data.message);
                    }

                }
            });
        });
    }
</script>
</body>
</html>