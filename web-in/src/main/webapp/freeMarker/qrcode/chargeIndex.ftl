<#global  contextPath = rc.contextPath />
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
    <title>领取二维码</title>

    <style>
        .btn-left,
        .btn-right {
            width: 50%;
            float: left;
        }

        body {
            max-width: 800px;
            margin: 0 auto;
        }
    </style>
</head>
<body>

<img data-src="logo.png" class="justify logo">

<div class="pd-10p mt-40">
    <div class="input-wrap input-icon-left">
        <span class="icon icon-phone"></span>
        <input type="text" class="numberInput" id="mobile" placeholder="请输入手机号码领取流量" maxlength="11">
        <input type="hidden" id="token" name="token" value="${token!}">
    </div>
</div>
<div class="pd-10p error-tip">
    <span id="error-tip"></span>
</div>

<div class="pd-10p mt-30">
    <a class="btn btn-lg btn-primary" id="btn-submit">立即充值</a>
</div>

<div id="tip-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-flow hide">
            <div class="modal-body">
                <p class="body-font" id="content">请确认是否充值？</p>
                <p class="body-font"></p>
            </div>
            <div class="modal-footer">
                <p class="btn-left"><a class="close-font" style=" display: block; " id="ok"
                                       data-dismiss="modal">确 定</a>
                </p>
                <p class="btn-right"><a class="close-font" style=" display: block; "
                                        data-dismiss="modal">取 消</a></p>
            </div>
        </div>
    </div>
</div>

<script>
    var baseUrl = "${contextPath}/assets/imgs/";
</script>
<script src="${contextPath}/assets/js/zepto.min.js"></script>
<script src="${contextPath}/assets/js/flowcard.common.min.js"></script>
<script src="${contextPath}/assets/js/module.min.js"></script>

<script>
    $("#btn-submit").on("click", function () {
        if (validate()) {
            msg = "请确认是否为手机号" + $("#mobile").val() + "充值？";
            $("#content").html(msg);
            $("#tip-dialog").modal("show");
        }
    });

    $("#ok").on("click", function () {
        var url = "${contextPath}/manage/qrcode/charge/submit.html?${_csrf.parameterName}=${_csrf.token}";

        ajaxData(url, {
            mobile: $("#mobile").val(),
            token: $("#token").val()
        }, function (ret) {
            if (ret && ret.result) {
                if (ret.result == "success") {
                    window.location.href =
                            "${contextPath}/manage/qrcode/charge/success.html?token=" + $("#token")
                                    .val()                                              + "&&${_csrf.parameterName}=${_csrf.token}";
                }
                else {
                    $("#error-tip").html(ret.result);
                }

            } else {
                $("#error-tip").html("领取失败!");
            }
        })
    });

    $("#mobile").on("blur", function () {
        $('#error-tip').html('');
        if (validate()) {
            $.ajax({
                       type: "GET",
                       url: "${contextPath}/manage/qrcode/charge/phoneQuery.html",
                       data: {
                           mobile: $("#mobile").val()
                       },
                       dataType: "json", //指定服务器的数据返回类型，
                       success: function (data) {
                          if (data.success == "true") {
                                $("#btn-submit").removeClass("disabled");
                          }else{
                               $('#error-tip').html('请输入正确的' + data.success + '手机号码');
                               $("#btn-submit").addClass("disabled");
                           }
                       },
                       error: function () {
                           $('#error-tip').html('请输入正确的手机号码');
                           $("#btn-submit").addClass("disabled");
                       }
                   });
        }
    });

    /**
     *
     */
    function validate() {
        var mobile = $("#mobile").val();
        if (!mobile.trim()) {
            $("#error-tip").html("请输入手机号");
            return false;
        } else if (!/^1[3|4|5|7|8|9]\d{9}$/.test(mobile)) {
            $('#error-tip').html('请输入正确的手机号码');
            return false;
        }

        $("#error-tip").html("");
        return true;
    }

    /**
     * 提交验证码
     */
    function doCharge() {
        var url = "${contextPath}/manage/qrcode/charge/submit.html?${_csrf.parameterName}=${_csrf.token}";

        ajaxData(url, {
            mobile: $("#mobile").val(),
            token: $("#token").val()
        }, function (ret) {
            if (ret && ret.result) {
                if (ret.result == "success") {
                    window.location.href =
                            "${contextPath}/manage/qrcode/charge/success.html?token=" + $("#token")
                                    .val()                                              + "&&${_csrf.parameterName}=${_csrf.token}";
                }
                else {
                    $("#error-tip").html(ret.result);
                }

            } else {
                $("#error-tip").html("领取失败!");
            }
        })
    }
</script>
</body>
</html>