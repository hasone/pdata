<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新建营销卡数据</title>
    <meta name="keywords" content="流量平台 新建营销卡数据"/>
    <meta name="description" content="流量平台 新建营销卡数据"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!-- easy-ui-->
<#--<link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">-->
<#--<link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/icon.css">-->

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 900px;
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
            padding-left: 305px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .btn-save {
            margin-left: 300px;
        }

        .dropdown-menu {
            max-height: 360px;
        }
    </style>

    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/Js/artDialog/skins/default.css">
    <script src="${contextPath}/manage2/Js/artDialog/artDialog.js"></script>
    <script src="${contextPath}/manage2/Js/artDialog/jquery.artDialog.js"></script>
</head>

<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>新建营销卡数据
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/mdrc/batchconfig/index.html'">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header"></div>
        <div class="tile-content">
            <div class="row form">
                <form action="${contextPath}/manage/mdrc/batchconfig/save.html?${_csrf.parameterName}=${_csrf.token}" method="post"
                      name="adminForm" id="table_validate">
                    <div class="col-md-12 col-md-offset-1">
                        <div class="form-group">
                            <label for="configName">卡名称：</label>
                            <input type="text" name="configName" id="configName" value="${configName!}" maxlength="20"
                                   class="hasPrompt"/>
                        </div>

                        <div class="form-group">
                            <label for="operatorSelect">制卡商：</label>
                            <select name="cardmakerId" id="cardmakerId">
                                <option value="-1">---请选择---</option>
                            <#if cardmakerList?? && cardmakerList?size != 0>
                                <#list cardmakerList as item>
                                    <option value="${item.id}">${item.name}</option>
                                </#list>
                            </#if>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="amount">卡片数量：</label>
                            <input type="text" name="amount" id="amount" value="${amount!}" class="hasPrompt"/>

                            <div class="prompt">请输入${minCardNum}-${maxCardNum}之间的数值</div>
                        </div>

                        <div class="form-group">
                            <label>有效期：</label>
                            <span id="validTime">
                                    <input style="width: 164px" type="text" name="startTime" id="startTime"
                                           readOnly="readOnly"
                                           class="hasPrompt">&nbsp;至&nbsp;<input style="width: 164px" type="text"
                                                                                 id="endTime"
                                                                                 readOnly="readOnly" name="endTime"
                                                                                 class="hasPrompt">
                                </span>
                            <input type="text"
                                   style="width:0;height:0;padding: 0; margin: 0; border: 1px solid transparent;"
                                   name="startendTime" id="startendTime" required>
                        </div>

                        <div class="form-group">
                            <label for="themeSelect">卡面主题：</label>

                            <div class="btn-group">
                            <#if templateMap??>
                                <select onchange="selectTheme(this.options[this.options.selectedIndex].value)"
                                        id="themeSelect" name="themeSelect">

                                    <#list templateMap?keys as key>
                                        <option value="theme_${key_index}" <#if key_index == 0>
                                                selected</#if>>${key}</option>
                                    </#list>
                                </select>
                            </#if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="template">模板名称：</label>

                            <div class="btn-group">
                            <#if templateMap??>
                                <#list templateMap?keys as key>
                                    <select id="theme_${key_index}" class="templateList" disabled="disabled"
                                            style="display:none;" onchange="changePreview(this)">
                                        <#list templateMap[key] as item>
                                            <option value="${item.id}"
                                                    imgBase="${contextPath}/manage/mdrc/template/getFileS3.html?id=${item.id}&filename="
                                                    frontImage="${item.frontImage!}" rearImage="${item.rearImage!}"
                                                    tname="${item.name}">${item.name}</option>
                                        </#list>
                                    </select>
                                </#list>
                            </#if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="template">正面预览：</label>

                            <div class="btn-group">
                                <img id="previewFront" src="" style="max-width:360px;"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="template">背面预览：</label>

                            <div class="btn-group">
                                <img id="previewRear" src="" style="max-width:360px;"/>
                            </div>
                        </div>

                        <#--
                        <div class="form-group">
                            <label></label>
                            <button onclick="return doSubmit();" class="btn btn-sm btn-warning dialog-btn">生成营销卡数据</button>
                            <span style="color: red;" id="errorTip">${errorMessage!}</span>
                        </div>
                        -->

                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div><!--row form -->
        </div> <!--tile-content -->
    </div>


    <div id="dialogContent" style="display:none;width:600px;">
        <div>
            <font style="font-weight:bold;font-size:14px;">正在生成数据，请稍候。。。</font>
            <img src="${contextPath}/manage2/assets/images/load-16-16.gif"/>
        </div>
    </div>

    <div class="mt-30 text-center">
        <a class="btn btn-sm btn-warning dialog-btn" id="submit-btn">提交审批</a>
        &nbsp;&nbsp;&nbsp;&nbsp;<span style='color:red' id="errorTip"></span>
    </div>

</div>



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
                <span class="message-content">请确认是否提交审批，一旦提交不可修改！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="sure">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["moment", "common", "bootstrap", "easyui", "daterangepicker"], function (mm) {
        window.moment = mm;
        selectTheme("theme_0");

        //初始化日期组件
        initDateRangePicker2();

        init();

    });

    function init() {
        //提交表单
        $("#sure").on("click", function () {

            doSubmit();
            //$("#table_validate").submit();
        });

        submitForm();
    }

    function submitForm() {
        $("#submit-btn").on("click", function () {
            if ($("#table_validate").validate().form()) {
                $("#submit-dialog").modal("show");
            }
            return false;
        });
    }

    /**
     * 表单验证
     */
    function initFormValidate() {
        $("#table_validate").validate({
            rules: {
            },
            errorElement: "span",
            messages: {
            }
        });
    }


    /**
     * 初始化日期选择器
     */
    function initDateRangePicker2() {
        var ele = $('#validTime');

        var startEle = $('#startTime');
        var endEle = $('#endTime');
        ele.dateRangePicker({
            startDate: new Date(),
            separator: ' ~ ',
            getValue: function () {
                if (startEle.val() && endEle.val())
                    return startEle.val() + ' ~ ' + endEle.val();
                else
                    return '';
            },
            setValue: function (s, s1, s2) {
                startEle.val(s1);
                endEle.val(s2);

                $("#startendTime").val(s1 + s2);

                var validator = $("#table_validate").validate();
                if (validator.check($("#startendTime")[0])) {
                    var err = validator.errorsFor($("#startendTime")[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }
            },
            maxDays:${maxDays}
        });
    }
</script>


<script>

    function selectTheme(selected) {

        templateId = null;
        clearPreview();
        $("select.templateList").hide().attr("disabled", "disabled");

        var templates = $("#" + selected);
        templates.show().removeAttr("disabled");
        changePreview(templates[0]);
    }
    function changePreview(elem) {

        if (elem == null || elem == undefined) {
            return;
        }

        clearPreview();

        var option = elem.options[elem.options.selectedIndex];
        var $option = $(option);
        templateId = $option.val();
        var base = $option.attr("imgBase");
        var front = $option.attr("frontImage");
        var rear = $option.attr("rearImage");
        var tname = $option.attr("tname");

        changePreviewImage(base, front, rear);
    }
    function clearPreview() {

        $("#previewFront").removeAttr("src").css("display", "none");
        $("#previewRear").removeAttr("src").css("display", "none");
    }
    function changePreviewImage(base, front, rear) {

        if (front != null && front != undefined && front != "") {
            $("#previewFront").attr("src", base + front).show(500);
        }
        if (rear != null && rear != undefined && rear != "") {
            $("#previewRear").attr("src", base + rear).show(500);
        }
    }


    function toIndex() {
        window.location.href = "${contextPath}/manage/mdrc/batchconfig/index.html";
    }

    function validateForm(configName, cardmakerId, amount, startTime, endTime, themeSelect) {
        $("#errorTip").empty();
        var regName = new RegExp("^[\u4E00-\u9FA5A-Za-z0-9_]+$");
        if (configName == null || $.trim(configName).length <= 0) {
            $("#errorTip").text('卡名称不能为空!');
            return false;
        } else if ($.trim(configName).length > 20) {
            $("#errorTip").text('卡名称长度不能超过20个字符!');
            return false;
        } else if (!regName.test(configName)) {
            $("#errorTip").text('卡名称只能输入汉字字母和数字');
            return false;
        }

        if (cardmakerId == null || $.trim(cardmakerId).length <= 0 || cardmakerId == -1) {
            $("#errorTip").text('请选择制卡商!');
            return false;
        }

        var reg = new RegExp("^[0-9]*$");
        if (amount == null || $.trim(amount).length <= 0) {
            $("#errorTip").text('卡片数量不能为空!');
            return false;
        }
        if (!reg.test(amount)) {
            $("#errorTip").text('卡片数量只能是正整数!');
            return false;
        }
        if (parseInt(amount) < parseInt(${minCardNum}) || parseInt(amount) > parseInt(${maxCardNum})) {
            $("#errorTip").text("卡片数量范围为${minCardNum}到${maxCardNum}的正整数!");
            return false;
        }

//        if (startTime == null || $.trim(startTime).length <= 0) {
//            $("#errorTip").text('起始日期不能为空!');
//            return false;
//        }

        if (endTime == null || $.trim(endTime).length <= 0) {
            $("#errorTip").text('截止日期不能为空!');
            return false;
        }

        if (themeSelect == null || $.trim(themeSelect).length <= 0) {
            $("#errorTip").text('卡面主题不能为空!');
            return false;
        }

        return true;
    }

    function doSubmit() {
        var configName = $('#configName').val();
        var cardmakerId = $('#cardmakerId').val();

        var themeSelect = $('#themeSelect').val();

        var amount = $('#amount').val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();

        if (!validateForm(configName, cardmakerId, amount, startTime, endTime, themeSelect)) {
            return false;
        }
        var data = {
            'configName': configName,
            'cardmakerId': cardmakerId,
            'templateId': templateId,
            'amount': amount,
            'startTime': startTime,
            'endTime': endTime
        };


        var wheatherNeedApproval = "${wheatherNeedApproval!}";

        if(wheatherNeedApproval=="true"){
            console.log(1);
            //需要审批
            submitApproval(data);
        }
        else{
            //不需要审批
            showDialog();
            doSave(data);
        }

        return false;
    }

    var dialog = null;

    function showDialog() {
        if (dialog == null) {
            dialog = art.dialog({
                lock: true,
                title: '营销卡数据',
                content: $('#dialogContent')[0],
                drag: false,
                resize: false,
                close: function () {
                    this.hide();
                    return false;
                }
            });
        } else {
            dialog.show();
        }
    }

    function submitApproval(data) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: "${contextPath}/manage/mdrc/batchconfig/submitAjax.html",
            type: "POST",
            dataType: "json",
            async: true,
            data: data,
            success: function (data) {
                if (data && data.code && data.code === 'success') {
                    toIndex();
                } else {
                    $("#errorTip").text("提交失败!");
                }
            },
            error: function () {
                $("#errorTip").text("网络错误，请稍候重新尝试。");
            }
        });
    }

    function doSave(data) {
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: "${contextPath}/manage/mdrc/batchconfig/saveAjax.html",
            type: "POST",
            dataType: "json",
            async: true,
            data: data,
            success: function (data) {
                if (data && data.code && data.code === 'ok') {
                    toIndex();
                } else {
                    if (dialog != null) {
                        dialog.close();
                    }
                    if (data && data.code && data.code === '-2') {
                        $("#errorTip").text("名称已存在，请重新输入。");
                        $('#configName').val("");
                    } else {
                        $("#errorTip").text("配置信息有误，请检查后重新提交。");
                    }
                }
            },
            error: function () {
                $("#errorTip").text("网络错误，请稍候重新尝试。");
            }
        });
    }
    function stringToDate(dateStr) {
        return new Date(Date.parse(dateStr.replace(/-/g, "/")));

    }

</script>

</body>
</html>