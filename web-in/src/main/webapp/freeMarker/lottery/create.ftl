<#assign contextPath = rc.contextPath >
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>新建大转盘活动</title>
    <meta name="keywords" content="新建大转盘活动"/>
    <meta name="description" content="新建大转盘活动"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/font-awesome.min.css"/>

<#-- artDialog -->
    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/Js/artDialog/skins/default.css">
    <script src="${contextPath}/manage2/Js/artDialog/artDialog.js"></script>
    <script src="${contextPath}/manage2/Js/artDialog/jquery.artDialog.js"></script>


    <!--[if IE 7]>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/font-awesome-ie7.min.css"/>
    <![endif]-->

    <!-- ace styles -->
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace-skins.min.css"/>

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace-ie.min.css"/>
    <![endif]-->

    <script src="${contextPath}/manage2/assets/js/ace-extra.min.js"></script>

    <!--[if lt IE 9]>
    <script src="${contextPath}/manage2/assets/js/html5shiv.js"></script>
    <script src="${contextPath}/manage2/assets/js/respond.min.js"></script>
    <![endif]-->


    <script type="text/javascript" src="${contextPath}/manage2/Js/jquery.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/assets/js/js.cookie.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap.js"></script>

    <script type="text/javascript">
        if ("ontouchend" in document) document.write("<script src='${contextPath}/manage2/assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
    </script>
    <script src="${contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <script src="${contextPath}/manage2/assets/js/typeahead-bs2.min.js"></script>

    <!-- page specific plugin scripts -->

    <!-- ace scripts -->
<#--  日期组件 -->
    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/assets/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/style.css"/>
    <script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/jquery.validate.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/Utility.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/common.js"></script>
    <script src="${contextPath}/manage2/assets/js/ace-elements.min.js"></script>
    <script src="${contextPath}/manage2/assets/js/ace.min.js"></script>
    <link rel="stylesheet" href="../../manage2/assets/css/base.css"/>
    <link rel="stylesheet" href="../../manage2/assets/js/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="../../manage2/assets/js/html5shiv.js"></script>
    <script src="../../manage2/assets/js/respond.min.js"></script>
    <![endif]-->

    <script type="text/javascript" src="${contextPath}/manage2/Js/knockout-3.2.0.js"></script>
    <style>
        body {
            font-family: "Microsoft YaHei";
            background: #F3F7FA;
        }

        .page-content {
            background: #F3F7FA;
        }

        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #a3a3a3;
            outline: none;
            border-radius: 3px !important;
            padding: 3px 10px;
            width: 190px;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
            max-width: 100px;
        }

        textarea.rule-info {
            height: 150px;
            width: 350px;
        }

        .award-table.table tbody > tr > td {
            padding: 14px 0 30px;
            position: relative;
        }

        .award-table .error-tip {
            position: absolute;
            top: 45px;
            left: 55px;
        }

        .input-checkbox {
            user-select: none;
            -webkit-user-select: none;
        }

        .input-checkbox input {
            opacity: 0;
            filter: alpha(opacity=0);
            width: 0;
            height: 0;
            outline: none;
            border: 0;
        }

        .c-checkbox {
            display: inline-block;
            background: url(${contextPath}/manage2/assets/images/icon-checkbox.png) no-repeat;
            width: 12px;
            height: 12px;
            cursor: pointer;
            margin: 0;
        }

        input:checked + .c-checkbox {
            background: url(${contextPath}/manage2/assets/images/icon-checked.png) no-repeat;
        }

        .c-checkbox.checked {
            background: url(${contextPath}/manage2/assets/images/icon-checked.png) no-repeat;
        }

        .tdCumTitle, .tdCumInstruction {
            vertical-align: top;
        }

        .tdCumInstruction {
            width: 270px;
        }

        .bg-white {
            background-color: #ffffff;
        }

        .cm-tip {
            display: none;
            position: absolute;
            top: 50px;
            left: 50px;
            text-align: center;
            border-radius: 6px;
            z-index: 9999;
            padding: 10px 25px;
            border: 1px solid #ccc;
        }

        .cm-tip:after {
            content: "";
            position: absolute;
            border: 8px solid transparent;
        }

        .cm-tip.bottom:after {
            left: 50%;
            top: -16px;
            margin-left: -9px;
            border-bottom-color: #46c37b;
        }

        .cm-tip-content {
            display: inline-block;
            -webkit-user-select: none;
            user-select: none;
            cursor: default;
        }

        .form-group input[type='text'], input[type='number'], select {
            margin-left: 5px;
        }

        .tile {
            min-width: 1200px;
        }
    </style>
</head>
<body>

<div class="page-header">
    <h1>新建大转盘<a class="btn btn-white pull-right" href="${contextPath}/manage/lotteryActivity/index.html">返回</a></h1>
</div>

<div class="activity-container" style="overflow:auto;">

    <form class="form" id="dataForm">
        <div class="tile">
            <div class="tile-header">
                基础配置
            </div>
            <div class="tile-content">
                <div class="form-group">
                    <label>活动名称：</label>
                    <input type="text" name="name" value="${(activity.name)!}" id="name" required>
                </div>
                <div class="form-group">
                    <label>企业名称：</label>
                    <select name="enterpriseName" id="enterpriseName" required>
                        <option value="">请选择</option>
                    <#list enterprises as e>
                        <option value="${e.id}" data-code="${e.code}"
                                <#if (activity.entId)?? && (activity.entId) == e.id >selected</#if>>${e.name!}</option>
                    </#list>
                    </select>
                    <input type="button" id="authorize" value="一键授权" onclick="startAuthorize();"
                           style="display: none;"/>
                </div>
                <div class="form-group">
                    <label>流量类型：</label>
                    <select name="flowType" id="flowType">
                        <option value="0">流量包</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>活动时间：</label>
                    <span id="activeTime">
                        <input type="text" name="startTime" id="startTime" value="${(activity.startTime)!}"
                               readonly="readonly" required>&nbsp;至<input type="text" id="endTime" name="endTime"
                                                                          value="${(activity.endTime)!}"
                                                                          readonly="readonly" required><label
                            class="error error-tip" id="time_tip"></label>
                    </span>
                </div>
                <div class="form-group">
                    <label>抽奖次数：</label>
                    <input type="number" name="maxPlayNumber" id="maxPlayNumber" value="${(activity.maxPlayNumber)!}"
                           min="1" max="10" required data-rule-number="true">
                </div>
                <div class="form-group">
                    <label>中奖次数：</label>
                    <input type="number" name="givedNumber" id="givedNumber" value="${(activity.givedNumber)!}" min="1"
                           max="10" required data-rule-number="true">
                </div>
                <div class="form-group">
                    <label>抽奖类型：</label>
                    <select name="lotteryType" id="lotteryType">
                        <option value="0" <#if (activity.lotteryType)?? && activity.lotteryType ==0>selected</#if>>
                            活动期间
                        </option>
                        <option value="1" <#if (activity.lotteryType)?? && activity.lotteryType ==1>selected</#if>>每天
                        </option>
                    </select>
                </div>
                <div class="form-group">
                    <label>充值时间：</label>
                    <select name="chargeType" id="chargeType">
                        <option value="0" <#if (activity.chargeType)?? && activity.chargeType == 0>selected</#if>>立即充值
                        </option>
                    </select>
                </div>

                <div class="form-group">
                    <label>活动规则：</label>
                    <textarea id="activityRule" name="activityRule" placeholder="活动规则说明。。。"></textarea>
                    <span class="help-block">活动规则长度不超过200个字符</span>
                </div>

            </div>
        </div>

        <div class="tile">
            <div class="tile-header">
                奖项配置
            </div>
            <div class="tile-content">
                <div class="">
                    <div class="">
                        <span class="input-checkbox">
                            <input type="radio" value="1" data-bind="checked: probabilityType" name="probabilityType"
                                   id="autoRandom_1">
                            <label for="autoRandom_1" class="c-checkbox mr-15 rt-1"
                                   data-bind="css : {checked: probabilityType() == '1'}"></label>
                            <span>自动调节奖品概率</span>
                        </span>
                        <span class="input-checkbox">
                            <input type="radio" value="0" data-bind="checked: probabilityType" name="probabilityType"
                                   id="autoRandom_2">
                            <label for="autoRandom_2" class="c-checkbox mr-15 rt-1"
                                   data-bind="css : {checked: probabilityType() == '2'}"></label>
                            <span>设置奖品概率</span>
                        </span>
                        <span class="ml-20" style="position: relative; top: -2px;">
                            <img src="${contextPath}/manage/assets/images/icon-question.png" data-direction="bottom"
                                 data-theme="white" data-tip='<div class="text-left">
                            <table>
                                <tbody>
                                <tr>
                                    <td class="tdCumTitle" style="width: 130px;"><p>自动调节奖品概率：</p></td>
                                    <td class="tdCumInstruction">平台会根据活动参与人数自动调节每个奖品的中奖概率，以确保奖品能够尽可能均匀地发放完毕</td>
                                </tr>
                                <tr>
                                    <td class="tdCumTitle"><p>设置奖品概率：</p></td>
                                    <td class="tdCumInstruction">人为设置奖品概率，例如，一等奖概率设置为1，若用户被随机分配落入一等奖奖池中，则必中奖。当所有奖项的概率均设置为1时，则用户必中奖。该设置不能保证奖品均匀发放，可能造成短时间内奖品已发送完毕或长时间内奖品未发送完毕</td>
                                </tr>
                            </tbody></table>
                            </div>'>
                        </span>
                        <a class="btn btn-sm btn-cyan pull-right add-award">添加</a>
                    </div>
                    <div class="table-responsive mt-20">
                        <table class="table table-bordered text-center award-table">
                            <thead>
                            <tr>
                                <th>奖项</th>
                                <th>产品名称</th>
                                <th>奖品名称</th>
                                <th>余额</th>
                                <th>配置数量</th>
                                <th data-bind="visible: probabilityDisplay">概率</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody data-bind="foreach: awards">
                            <tr>
                                <td>
                                    <input type="text" class="rankName" name="rankName" required
                                           data-bind="value: awardsText[$index()]" readonly="readonly"
                                           style="width:70px;">
                                </td>
                                <td>
                                    <select class="productName" name="productName" required
                                            data-bind="value: productName"></select>
                                </td>
                                <td>
                                    <input type="text" name="prizeName" class="prizeName" readonly
                                           style="width:80px;"></input>
                                </td>
                                <td>
                                    <input type="number" name="balance" class="balance" style="width:90px;"
                                           data-bind="value: balance" required data-rule-number="true" disabled>
                                </td>
                                <td>
                                    <input type="text" name="count" class="count" data-bind="value: count"
                                           style="width:80px;" value="0">
                                    <br/><span class="prompt">不大于账户余额的正整数</span>
                                </td>
                                <td data-bind="visible: $parent.probabilityDisplay">
                                    <input type="text" name="probability" class="probability"
                                           data-bind="value: probability" style="width: 60px;"/>
                                    <br/><span class="prompt">0到1之间的小数</span>
                                </td>
                                <td>
                                    <a class="btn btn-sm btn-link delBtn" data-bind="click: $parent.removeAward"
                                       onclick="activePreAward(this);">删除</a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <span class="error-tip" id="award-error"></span>
                        <span class="error-tip" id="award-count-error"></span>
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

    <div class="form-group text-center">
        <a class="btn btn-sm btn-cyan dialog-btn save-btn">保 存</a>
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
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<script src="../../manage2/assets/js/tooltip.js"></script>
<script src="../../manage2/assets/js/daterangepicker/moment.js"></script>
<script src="../../manage2/assets/js/daterangepicker/jquery.daterangepicker.js"></script>
<script>
    var MAXAWARDNUM = 6;
    var awardsText = ["一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖"];
    var productNames = [];
    var products = null;

    //允许上一奖项可设置
    function activePreAward(ele) {
        var preAward = $(ele).parent().parent().prev();
        preAward.find(".productName").removeAttr("disabled");
        preAward.find(".count").removeAttr("disabled");
        preAward.find(".delBtn").css("display", "block")
    }

    var viewModel = {
        awards: ko.observableArray([]),
        productNames: ko.observableArray([]),
        probabilityType: ko.observable("1"),
        removeAward: function () {
            viewModel.awards.remove(this);
        },
        changeProductCode: function () {
            console.log(arguments);
        }
    };
    viewModel.probabilityDisplay = ko.dependentObservable({
        read: function () {
            return this.probabilityType() == "0";
        },
        owner: viewModel
    });
    ko.applyBindings(viewModel);


    $(function () {
        //表单验证
        formValidate();

        //模拟文件选择器
        fileBoxHelper();

        //添加奖项信息
        addAwardListener();

        //初始化日期组件
        initDateRangePicker();

        //保存表单
        saveForm();

        $("#enterpriseName").change(function () {
            viewModel.awards.removeAll();

            enterpriseChoiceListener($("#enterpriseName"));
        });
    });

    /**
     * 添加奖项信息
     */
    function addAwardListener() {
        //点击的时候添加一行
        $(".add-award").on("click", function () {
            //隐藏错误提示
            $("#award-error").addClass("hidden");
            $("#award-error").html("");

            var awards = $(".award-table tbody tr");

            if (awards.length >= MAXAWARDNUM) {
                showTip("最多添加" + MAXAWARDNUM + "个奖项");
                return false;
            }

            if (!$("#enterpriseName").val()) {
                showTip("请先选择企业！");
                return;
            }

            if (awards.length > 0) {
                //校验上一奖项
                var pre_award = awards.last();
                var productId = pre_award.find(".productName").val();
                var balance = pre_award.find(".balance").val();
                var count = pre_award.find(".count").val();
                var probalility = pre_award.find(".probability").val();

                var probabilityType = $("input[name='probabilityType']:checked").val();
                if (productId == "" || balance == "" || count == "" || (Number(probabilityType) == 0 && probalility == '')) {
                    showTip("奖项信息不完整！");
                    return false;
                }

                if (!count.match(/^\d+$/) || Number(balance) <= 0 || Number(count) > Number(balance)) {
                    showTip("奖项数量设置错误！");
                    return false;
                }

                if (Number(probabilityType) == 0 && (!probalility.match(/^\d+(\.\d+)?$/) || Number(probalility) > 1)) {
                    showTip("奖项概率设置错误！");
                    return false;
                }

                //奖项设置正确
                pre_award.find(".enterpriseName").attr("disabled", "disabled");
                pre_award.find(".productName").attr("disabled", "disabled");
                pre_award.find(".count").attr("disabled", "disabled");
                pre_award.find(".count").removeClass("valid");
                pre_award.find(".delBtn").css("display", "none")
            }


            var obj = {
                productName: "",
                balance: "",
                count: 0,
                probability: ""
            };

            viewModel.awards.push(obj);

            appendProducts();

            var ele = $(".productName:last");
            ele.on("change", function () {
                var productId = $(this).val();
                var productSize = $("option:selected", this).attr("data-productSize");
                var parent = $(this).parents("tr");
                parent.find("input[name='prizeName']").val(productSize);
                var enterCode = $("#enterpriseName").data("code");
                var target = $(".balance", parent);
                getBalance(enterCode, productId, target);
            });

            //var enterEle = $(".enterpriseName:last");
            //enterpriseChoiceListener(enterEle);
        });
    }

    function appendProducts() {
        var ele = $(".productName:last");
        ele.empty();
        ele.append("<option value=''>请选择</option>");

        for (var i in products) {
            ele.append('<option value="' + products[i].id + '" data-code="' + products[i].productCode + '" data-productSize="' + products[i].size + '">' + products[i].name + '</option>');
        }
    }

    /**
     * 删除奖项
     */
    function doDeleteAwardItem(ele) {
        ele.parent().parent().remove();
    }

    /**
     * 模拟文件选择器
     */
    function fileBoxHelper() {
        $(".file-helper").on("change", function () {
            var path = this.value;
            var lastsep = path.lastIndexOf("\\");
            var filename = path.substring(lastsep + 1);
            $(this).parent().children(".file-input").val(filename);
        });
    }

    /**
     *
     * @param msg
     */
    function showTip(msg) {
        $("#tip-dialog .message-content").html(msg);
        $("#tip-dialog").modal("show");
    }

    /**
     * 表单验证
     */
    function formValidate() {
        $("#dataForm").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                //$("#award-error").html("");
                //$("#award-error").append(error);
                element.parent().append(error);
                if (element.attr("name") === "count") {
                    error.css("top", "65px");
                }
            },
            errorElement: "label",
            rules: {
                name: {
                    userName: true,
                    maxlength: 12,
                    remote: {
                        url: "checkActivityName.html",
                        data: {
                            activityName: function () {
                                return $('#name').val()
                            }
                        }
                    }
                },
                enterpriseName: {
                    required: true
                },
                productName: {
                    required: true
                },
                count: {
                    required: true
                },
                probability: {
                    required: true
                },
                maxPlayNumber: {
                    number: true,
                    required: true,
                },
                givedNumber: {
                    number: true,
                    required: true,
                },
                activityRule: {
                    maxlength: 200
                }
            },
            messages: {
                name: {
                    required: "请输入活动名称",
                    maxlength: "名称不得超过12个字符",
                    remote: "大转盘活动名称已存在"
                },
                givedNumber: {
                    number: "请输入数字",
                    required: "请输入抽奖次数",
                    min: "请填写1-10之间的数字",
                    max: "请填写1-10之间的数字"
                },
                maxPlayNumber: {
                    number: "请输入数字",
                    required: "请输入抽奖次数",
                    min: "请填写1-10之间的数字",
                    max: "请填写1-10之间的数字"
                },
                activityRule: {
                    maxlength: "字符个数不能超过200"
                },
                probability: {
                    required: "请输入产品概率",
                    min: "请填写0-1之间的数值",
                    max: "请填写0-1之间的数值"
                },
                count: {
                    required: "请输入奖品个数"
                },
                balance: {
                    required: "请输入产品余额"
                },
                productCode: {
                    required: "请选择产品代码"
                },
                enterpriseName: {
                    required: "请选择企业名称"
                },
                productName: {
                    required: "请选择产品"
                }
            }
        });
    }

    /**
     * 初始化日期选择器
     */
    function initDateRangePicker() {
        var ele = $('#activeTime');

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
                var validator = $("#dataForm").validate();
                if (validator.check(startEle[0])) {
                    var err = validator.errorsFor(startEle[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }

                if (validator.check(endEle[0])) {
                    var err = validator.errorsFor(endEle[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }

                $("#time_tip").hide();
                $("#time_tip").html("");
            }
        });
    }

    function checkTimeInput() {
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        if (startTime == '' || endTime == '') {
            $("#time_tip").show();
            $("#time_tip").html("请输入起止时间");
            return false;
        }
        return true;
    }

    /**
     * 保存表单
     */
    function saveForm() {
        $(".save-btn").on("click", function () {
            var flag = true;
            if (!$("#dataForm").validate().form()) {
                return false;
            }
            if (!checkTimeInput()) {
                return false;
            }
            var award_ele = $(".award-table tbody tr");
            if (!award_ele.length) {
                $("#award-error").removeClass("hidden");
                $("#award-error").html("请添加奖品");
                return false;
            }

            var params = {
                entId: $("#enterpriseName").val(),
                activityName: $("#name").val(),
                flowType: $("#flowType").val(),
                startTime: $("#startTime").val(),
                endTime: $("#endTime").val(),
                maxPlayNumber: $("#maxPlayNumber").val(),
                givedNumber: $("#givedNumber").val(),
                lotteryType: $("#lotteryType").val(),
                chargeType: $("#chargeType").val(),
                checkUrl: $("#checkUrl").val(),
                appId: $("#appId").val(),
                appSecret: $("#appSecret").val(),
                probabilityType: $("input[name='probabilityType']:checked").val(),
                activityRule: $("#activityRule").val(),
                prizes: []
            };

            var totalProbability = 0;
            award_ele.each(function (index) {
                var obj = {
                    idPrefix: index,
                    rankName: $(".rankName", this).val(),
                    productId: $(".productName", this).val(),
                    count: $(".count", this).val(),
                    prizeName: $(".prizeName", this).val(),
                    probability: $(".probability", this).val()
                };
                if (Number(params.probabilityType) == 0 && obj.probalility == '') {
                    showTip("奖项信息不完整！");
                    flag = false;
                    return false;
                }
                if (!obj.count.match(/^\d+$/) || Number(obj.count) < 0 || Number(obj.count) > Number($(".balance", this).val())) {
                    showTip("奖项数量设置错误！");
                    flag = false;
                    return false;
                }
                if (Number(params.probabilityType) == 0 && !(obj.probability.match(/^\d+(\.\d+)?$/)) || Number(obj.probability) > 1) {
                    showTip("奖项概率设置错误！");
                    flag = false;
                    return false;
                }
                totalProbability += Number(obj.probability);
                params.prizes.push(obj);
            });

            //if(Number(params.probabilityType)==0 && Number(totalProbability) > 1){//手动设置奖项概率
            //	showTip("奖项概率设置错误！");
            //  flag = false;
            //    return false;
            //}
            if (flag) {
                saveLotterySetting(params);
            }
            return false;
        });
    }

    /**
     * 向后台发送数据保存配置信息
     * @param params
     */
    function saveLotterySetting(params) {
        var entId = jQuery("#enterpriseName option:selected").val();
        var flag = true;
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: "${contextPath}/manage/lotteryActivity/isAuthorized.html",
            type: "POST",
            dataType: "JSON",
            data: {entId: entId}
        }).then(function (data) {
            var result = data.result;
            if (result == "true") {
                var paramurl = JSON.stringify(params);
                $.ajax({
                    beforeSend: function (request) {
                        var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                        var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                        request.setRequestHeader(header1, token1);
                    },
                    url: "${contextPath}/manage/lotteryActivity/save.html",
                    type: "post",
                    data: {paramurl: paramurl},
                    dataType: "json",
                    success: function (ret) {
                        //console.log(ret);
                        if (ret && ret.success == "true") {
                            //showTip("保存成功");
                            //跳转到list页面?
                            window.location.href = "${contextPath}/manage/lotteryActivity/index.html";
                        } else {
                            showTip("操作失败");
                        }
                    }
                });
            } else {
                alert("企业未授权！");
            }
        });

    }

    /**
     * 企业名称选择
     */
    function enterpriseChoiceListener(ele) {

        var enterCode = $("option:selected", ele).data("code");
        ele.data("code", enterCode);
        var val = ele.val();
        isAuthorized(val);
        getProducts(val);

        /*ele.change(function(){
            var val = $(this).val();
            var enterCode = null;
            for(var i in enterprises){
                if(enterprises[i].id == val){
                    enterCode = enterprises[i].code;
                    ele.data("code", enterCode);
                    break;
                }
            }
            var parent = ele.parents("tr");
            getProducts(val, parent);
        });*/
    }

    /*
    * 授权
    */
    function startAuthorize() {
        var entId = jQuery("#enterpriseName option:selected").val();
        if (entId == "") {
            alert("请选择企业！");
            return false;
        }
        window.open("${authorizeUrl}" + entId);
    }


    /**
     * 判断企业是否授权
     */
    function isAuthorized(entId) {
        if (entId == "") {
            $("#authorize").hide();
            return false;
        }
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: "${contextPath}/manage/lotteryActivity/isAuthorized.html",
            type: "POST",
            dataType: "JSON",
            data: {entId: entId}
        }).then(function (data) {
            var result = data.result;
            if (result == "true") {
                $("#authorize").hide();
            } else {
                $("#authorize").show();
            }
        });
    }

    /**
     * 获取企业的产品
     */
    function getProducts(enterId, parent) {
        /*parent.find(".prizeName").val("");
        parent.find(".balance").val("");
        parent.find(".count").val("");
        var pEle = parent.find(".productName");
        pEle.empty();
        pEle.append("<option value=''>请选择</option>");
        if(!enterId){
            return false;
        }*/
        console.log(enterId);
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            url: "${contextPath}/manage/product/getProductsAjax.html",
            type: "POST",
            dataType: "JSON",
            data: {enterpriseId: enterId}
        }).then(function (data) {
            products = data.products;
            /*var pnEle = $(".productName", parent);
              for(var i in products){
                  pnEle.append('<option value="'+products[i].id+'" data-code="'+products[i].productCode+'" data-productSize="'+products[i].size+'">'+products[i].name+'</option>');
              }
              $(".balance", parent).val("");*/
        });
    }

    /**
     *
     */
    function getBalance(enterCode, productID, ele) {
        var parent = ele.parents("tr");
        parent.find(".balance").val("");
        parent.find(".count").val("");
        if (enterCode && productID) {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/entproquery/getRemainAjax.html",
                type: "POST",
                dataType: "JSON",
                data: {enterpriseCode: enterCode, productID: productID}
            }).then(function (data) {
                if (data && data.message) {
                    showTip(data.message);
                } else {
                    var balance = Number(data.count);
                    var usedCount = 0;
                    var elements = $(".award-table tbody tr").not(":last");
                    var centId = parent.find("td select[name='enterpriseName'] option:selected").val();
                    var cproId = parent.find("td select[name='productName'] option:selected").val();
                    elements = elements.filter(function (index) {
                        if ($(this).find("td select[name='enterpriseName'] option:selected").val() == centId && $(this).find("td select[name='productName'] option:selected").val() == cproId) {
                            return true;
                        }
                    });
                    elements = elements.find("td input[name='count']");
                    elements.each(function (index) {
                        usedCount = usedCount + Number(this.value);
                    })
                    var availableCount = balance - usedCount;
                    ele.val(availableCount);
                    var countEle = ele.parents("tr").find(".count").last();
                    //var max = Math.floor(balance/3);
                    //countEle.attr("max", availableCount);
                    //countEle.attr("data-msg-max", "请填写1-"+availableCount+"之间的数字");
                    //countEle.attr("data-msg-min", "请填写1-"+availableCount+"之间的数字");
                }
            }).fail(function () {
                showTip("网络错误或请求超时");
            });
        }
    }
</script>
</body>
</html>