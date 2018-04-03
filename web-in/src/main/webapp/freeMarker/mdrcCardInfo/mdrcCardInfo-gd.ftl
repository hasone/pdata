<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>广东移动集团流量卡</title>
    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <!--<link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>-->
    <!--<link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>-->
    <!--<link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>-->
    <style>
        .login-wrap {
            padding: 20px;
        }

        img {
            width: 32%;
            margin-top: 20px;
            margin-bottom: 5px;
        }

        .form {
            margin-top: 30px;
        }

        .form-group {
            position: relative;
            margin-bottom: 20px;
        }

        .form-control {
            box-shadow: none !important;
            -webkit-box-shadow: none !important;
        }

        .form-group div {
            color: red;
            position: absolute;
            font-size: 12px;
            line-height: 20px;
        }

        .question {
            display: block;
            /*color: #00a2d4;*/
        }

        .introduction p {
            font-size: 13px;
            margin-bottom: 0px;
        }

        a:focus, a:hover {
            color: #337ab7;
            outline: none;
            text-decoration: none;
        }

        .btn-primary:focus, .btn-primary:hover {
            background-color: #337ab7;
            border-color: #2e6da4;
        }

        .btn-disabled {
            pointer-events: none;
            background-color: #ddd;
            border-color: #ddd;
        }
    </style>
</head>
<body>
<div class="login-wrap text-center">
    <img src="${contextPath}/assets/imgs/logo-sc.png">
    <h4>广东移动集团流量卡领用</h4>
    <form class="form" id="dataForm" method="POST" action="${contextPath}/manage/mdrc/cardinfo/submit.html">
        <div class="form-group">
            <input type="text" name="chargeMobile" id="chargeMobile" class="form-control mobileOnly" maxlength="11"
                   placeholder="请输入广东移动手机号码" value='${chargeMobile!}'/>
        </div>
        <div class="form-group">
            <input type="text" name="cardnumber" id="cardnumber" class="form-control mobileOnly" maxlength="30"
                   placeholder="请输入流量卡卡号" value='${cardnumber!}'/>
        </div>
        <div class="form-group">
            <input type="password" name="cardpsw" id="cardpsw" class="form-control" maxlength="30"
                   placeholder="请刮卡涂层，输入密码"/>
        </div>
        
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <div class="form-group" style="margin-bottom: 10px;">
        <a class="btn btn-primary btn-disabled" style="width: 100%;" id="submit-btn">确认提交</a>
    </div>
    
    <div class="text-center">
         <label class="prompt text-center error-tip" style="color:red;" id="submit-error">${errMsg!}</label>
    </div>

    <a class="question text-right" href="${contextPath}/manage/mdrc/cardinfo/qustions.html">常见问题</a>
    <div class="introduction text-left">
        <p>温馨提示：</p>
        <p>1、本流量卡限广东移动用户使用。</p>
        <p>2、请确保手机号码状态正常，如您的手机号码处于欠费停机或办理了携号转品牌业务、预销户等状态，将无法成功领取流量。</p>
        <p>3、如流量领取不成功，请联系本流量卡背面客服电话或更换其他广东移动号码使用。</p>
        <p>4、流量卡为不记名流量领用凭证，请您妥善保管。</p>
        <p>5、本流量卡所含流量领取成功后，将优先使用。</p>
    </div>
</div>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
        checkFormValidate();
        listeners();
    });

    function listeners() {
        $("#submit-btn").on("click", function () {
            if ($("#dataForm").validate().form()) {
                $("#dataForm").submit();
            }
        });

        $("#chargeMobile, #cardnumber, #cardpsw").on("input", function () {
            if ($("#chargeMobile").val() && $("#cardnumber").val() && $("#cardpsw").val() && $("#dataForm").validate().form()){
                $("#submit-btn").removeClass("btn-disabled");
            }else{
                $("#submit-btn").addClass("btn-disabled");
            }
        });
        
       $("#chargeMobile, #cardnumber, #cardpsw").on("focus", function () {
            $("#submit-error").html("");
        });
    }

    function checkFormValidate() {
        $("#dataForm").validate({
            onfocusout: function(element) {
                $(element).valid();
            },
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                $(element).parent().append(error);
            },
            errorElement: "div",
            rules: {
                chargeMobile: {
                    required: true,
                    mobile: true,
                    remote: {
	            	url: "${contextPath}/manage/mdrc/cardinfo/checkMobile.html?${_csrf.parameterName}=${_csrf.token}",
                        data: {
                            mobile: function () {
                                return $('#chargeMobile').val()
                            }
                        }
                    }
                },
                cardnumber: {
                    required: true
                },
                cardpsw: {
                    required: true
                }
            },
            messages: {
                chargeMobile: {
                    required: "请输入正确广东移动手机号码",
                    mobile: "请输入正确广东移动手机号码",
                    remote: "请输入正确广东移动手机号码"
                },
                cardnumber: {
                    required: "请输入正确流量卡卡号"

                },
                cardpsw: {
                    required: "请输入流量卡密码"
                }
            }
        });
    }
</script>
</body>
</html>