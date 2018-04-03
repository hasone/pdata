<!DOCTYPE html>

<#global  contextPath = rc.contextPath />

<html>

<head>
    <title>

    </title>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="keywords" content="流量平台 企业流控设置"/>
    <meta name="description" content="流量平台 企业流控设置"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .form-group label {
            width: 180px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 186px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
            display: block;
        }


    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>
            企业流控设置
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="javascript:history.go(-1);">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/entFlowControl/saveEntFlowControl.html"
          method="post" name="entFlowControlForm" id="table_validate">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">
                    <input type="hidden" form-control id="enterId" name="enterId" value="${enterId! }"/>

                    <div class="form-group">
                        <label>日上限金额:</label>
                        <input type="text" id="countUpper" name="countUpper" maxlength="8" required/>(元)
                        <span class="prompt">请填写1-99999999的正整数，计数金额将以平台成功发送请求为准,-1表示不限制消费额度</span>
                        <label class="error error-tip" id="time_tip"></label>
                    </div>

                </div>
            </div>
        </div>

        <div class="btn-save mt-30 text-center">
            <a class="btn btn-sm btn-warning dialog-btn" id="save-btn">保存</a>
            <span style="color:red" id="error_msg">${errorMsg!}</span>
        </div>
    </form>

    <div class="modal fade dialog-sm" id="submit-dialog">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content">确认此次设置生效？</span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {

        $("#countUpper").on("input", function () {
            var val = $(this).val();
            if(val == "0"){
            	val = "";
            }
            if (val.substr(0, 1) == "-" && val.length > 1) {
                val = "-1";
            } else {
                if (val.substr(0, 1) == "-") {
                    val = "-" + val.substr(1).replace(/[^0-9]+/, "");
                } else {
                    val = val.replace(/[^0-9]+/, "");
                }
            }

            $(this).val(val);
        });
        $("#table_validate").validate({
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
                countUpper: {
                    required: true,
                    maxlength: 10
                },
            },
            messages: {
                countUpper: {
                    required: "请输入上限额度",
                    maxlength: "额度格式错误",
                    positive: "额度格式错误"
                },
            }
        });

        submitForm();
    });

    function submitForm() {
        $("#save-btn").on("click", function () {
            $("#submit-dialog").modal("show");
        });

        $("#sure").on("click", function () {
            $("#table_validate").submit();
        });
    }
</script>

</body>
</html>