<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>

<head>
    <title>密码修改
    </title>

    <meta charset="UTF-8">

    <script type="text/ecmascript" src="${contextPath}/manage/Js/md5.js"></script>

</head>

<body>
<div class="page-header">
    <h1>密码修改
        <!--<a class="btn btn-white pull-right" href="${contextPath}/manage/userInfo/showCurrentUserDetails.html">返回</a> -->
    </h1>
</div>


<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <form name="adminForm"
              action="${rc.contextPath}/manage/userInfo/savePassword.html" onsubmit="return sumitAdd();"
              method="post" class="form-horizontal" role="form" id="table_validate">
            <div class="form-group">
                <label class="col-sm-3 control-label"> 手机号码 </label>

                <div class="col-sm-9">
                    <label><input type="text" name="mobilePhone"
                                  id="mobilePhone"
                                  readonly='readonly'
                                  value='${admin.mobilePhone!}'/></label>
                </div>
            </div>

            <div class="space-4"></div>


            <div class="form-group">
                <label class="col-sm-3 control-label">验证码 </label>
                <div class="col-sm-9">
                    <label><input type="text" name="sjmmInput" id="sjmmInput" style="width: 20%"
                                  class="input-block-level"
                                  placeholder="验证码" maxlength="6">
                        <input type="button"
                               name="hqyzm"
                               id="hqyzm"
                               class="btn btn-success"
                               value="获取随机密码"/>
                        &nbsp;&nbsp;
                        <span style="color: red" id="getMsg"></span></label>
                </div>
            </div>

            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"> 创建新密码(<span class="red">*</span>) </label>
                <div class="col-sm-9">
                    <label><input type="password" name="saisirPass" id="saisirPass"/>
                        <br/><span class="prompt">密码必须包含字母、数字、特殊符号且长度为10到20位</span></label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">确认密码(<span class="red">*</span>)</label>
                <div class="col-sm-9">
                    <label><input type="password" name="confirmPass" id="confirmPass"/>
                        <br/><span class="prompt">密码必须包含字母、数字、特殊符号且长度为10到20位</span></label>
                </div>
            </div>


            <div class="hr hr-24"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>

                <div class="col-sm-9">
                    <input type="submit" name="generate" value="保存" class="btn btn-primary"/>
                    &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div><!-- /.row -->

<form name="adminForm"
      action="${rc.contextPath}/manage/userInfo/savePassword.html"
      method="post" class="form-horizontal" role="form" id="table_submit">

    <input type="hidden" name="mobilePhone" id="mobilePhone" value='${admin.mobilePhone!}'/>
    <input type="hidden" name="sjmm" id="sjmm"/>
    <input type="hidden" name="passWord" id="passWord"/>
    <input type="hidden" name="passWord2" id="passWord2"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>


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
</script>

<script>
    $("#table_validate").packValidate({
        rules: {
            sjmmInput: {
                required: true
            },
            saisirPass: {
                required: true,
                minlength: 10,
                maxlength: 32,
                strictPwd: true,

            },
            confirmPass: {
                required: true,
                minlength: 10,
                maxlength: 32,
                verifyPwd: $('#saisirPass')
            }
        },
        messages: {
            sjmmInput: {
                required: "随机密码不能为空"
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
            var hash = hex_md5(temp);

            $('#passWord').val(hash);
        }


        var temp = $("input[name='saisirPass']").val();

        if (isEmptyOrNull(temp)) {
            flag = false;
            return flag;
        }
        else {
            var hash = hex_md5(temp);

            $('#passWord2').val(hash);
        }

        $('#table_submit').submit();
        return false;
    }
    ;


    $('#backid').click(function () {
        window.location.href = "${contextPath}/manage/userInfo/showCurrentUserDetails.html";
    });

    $("#hqyzm").click(function () {
        $("#getMsg").empty();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: '${contextPath}/manage/message/sendChangePassMessage.html',
            type: 'get',
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
</script>
</body>
</html>

