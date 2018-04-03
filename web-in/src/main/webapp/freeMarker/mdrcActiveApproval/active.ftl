<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>营销卡激活</title>
    <meta name="keywords" content="营销卡激活"/>
    <meta name="description" content="营销卡激活"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form .form-control {
            width: 220px;

        }

        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 220px;
        }

        .form label {
            width: 80px;
            text-align: right
            margin-right: 10px;
        }

        span.error {
            color: red;
            margin-left: 10px;
        }

        #comment {
            width: 200px;
            resize: none;
            height: 80px;
        }

        .promote{font-size: 12px; color: #959595; margin-left: 82px;}
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>营销卡激活
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            营销卡激活
        </div>
        <div class="tile-content">
            <div class="row form">
                <form id="dataForm">
                    <div class="col-md-12 col-md-offset-1">

                        <div class="form-group ">
                            <label>激活数量：</label>
                            <span>${mdrcActiveDetail.count!}</span>
                        </div>

                        <div class="form-group">
                            <label>卡批次：</label>
                            <div class="btn-group btn-group-sm mdrcBatchConfig-select">
                                <input name="configId" id="configId" style="width: 0; height: 0;padding: 0; opacity: 0;" required>
                                <button type="button" class="btn btn-default" style="width: 298px;">请选择</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu">
                                <#if mdrcBatchConfigs?? && mdrcBatchConfigs?size!=0>
                                    <#list mdrcBatchConfigs as mdrcBatchConfig>
                                        <li data-value="${mdrcBatchConfig.id!}"><a href="#">${mdrcBatchConfig.serialNumber!}</a></li>
                                    </#list>
                                <#else>
                                    <li data-value=""><a href="#">没有可选的批次</a></li>
                                </#if>
                                </ul>
                            </div>
                            <div class="promote"><span id="leftCount"></span></div>
                        </div>

                        <div class="form-group ">
                            <label>起始序列：</label>
                            <input type="hidden" class="form-control" id="startSerial" name="startSerial">
                            <span id="start"></span>
                        </div>

                        <div class="form-group">
                            <label>结束序列：</label>
                            <input name="endSerial" id="finishSerial" type="hidden">
                            <span id="endSerial"></span>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>

    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">确认激活</a>
    </div>

</div>


<div class="modal fade dialog-sm" id="tip-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="success-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="tips-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="tips"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="tips-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<!--[if lt IE 9]>
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    require(["common", "bootstrap"], function () {

        initFormValidate();

        submitForm();

        listeners();

        init()

    });

    function init() {
        //document.getElementById("submit-btn").disabled=true;
        $("#submit-btn").addClass("disabled");
    }

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#dataForm").validate({
            rules: {
                configId:{
                    required: true
                },
                startSerial:{
                    required: true
                }

            },
            errorElement: "span",
            messages: {
                configId:{
                    required: "请选择卡批次"
                },
                startSerial:{
                    required: ""
                }
            }
        });
    }

    function submitForm() {
        $("#submit-btn").on("click", function () {
            if ($("#dataForm").validate().form()) {
                saveForm();
            }
            return false;
        });
    }

    function listeners() {

        $(".mdrcBatchConfig-select a").on("click", function () {
            var configId = $(this).parent().attr("data-value");

            //document.getElementById("submit-btn").disabled=true;
            $("#submit-btn").addClass("disabled");

            verifyLeftCount(configId);
        });
        
        $("#tips-btn").on("click", function () {
            var code = $("#tips").val();
            //由于已经被处理，直接返回列表页面
            if(code == 2 || code == 3){
            	window.location.href = "${contextPath}/manage/mdrc/cardinfo/activeIndex.html";
            }
        });
    }

    //校验该批次剩余卡数量
    function verifyLeftCount(configId){
        var activeCount = "${mdrcActiveDetail.count!}";
        if(configId=='' || activeCount==''){
            return;
        }

        var params = {
            configId: configId,
            activeCount: activeCount
        };

        var url = "${contextPath}/manage/mdrc/cardinfo/verifyLeftCountAjax.html";
        ajaxData(url, params, function (ret) {
            if(ret){
                if (ret.result=="success") {
                    //剩余数量
                    $("#start").html(ret.startSerial);
                    $("#startSerial").val(ret.startSerial);
                    $("#leftCount").html(ret.message);
                    updateEnd();
                }
            }
            $("#leftCount").html(ret.message);
        }, null, "post");
    }

    //更新终止序列号
    function updateEnd() {
        var start = $("#startSerial").val();
        var count = "${mdrcActiveDetail.count!}";

        //判断start是否存在
        if (!start) {
            return;
        }

        count = count != "" ? parseInt(count) : 1;

        var length = 7;

        var pref = start.substring(0, start.length - length);
        var numStr = start.substr(start.length - length);
        var sum = parseInt(numStr) + count - 1;
        var sumStr = sum + "";
        var sumLength = sumStr.length;

        for (var i = 0; i < length - sumLength; i++) {
            sumStr = "0" + sumStr;
        }

        var ret = pref + sumStr;
        $("#endSerial").html(ret);
        $("#finishSerial").val(ret);

        //document.getElementById("submit-btn").disabled=false;
        $("#submit-btn").removeClass("disabled");
    }

    /**
     * 提交表单
     */
    function saveForm() {
        var requestId = "${mdrcActiveDetail.requestId!}";

        var params = {
            configId: $("#configId").val(),
            requestId: requestId,
            startSerial: $("#startSerial").val(),
            endSerial: $("#endSerial").html()
        };

        var url = "${contextPath}/manage/mdrc/cardinfo/activeAjax.html";
        ajaxData(url, params, function (ret) {
            if (ret && ret.result=="success") {
                //激活成功
                showTipDialog("激活成功");

                $("#success-btn").on("click", function () {
                    //成功后跳转
                    //window.history.go(-1);
                    window.location.href = "${contextPath}/manage/mdrc/cardinfo/activeIndex.html";
                });
            } else {
                //showTipDialog("激活失败");
                
                $("#tips").html(ret.msg);
                $("#tips").val(ret.code);
                $("#tips-dialog").modal("show");
            }
        }, null, "post");
    }
</script>
</body>
</html>