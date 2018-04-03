<!DOCTYPE html>
<#global contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-添加红包</title>
    <meta name="keywords" content="流量平台 添加红包"/>
    <meta name="description" content="流量平台 添加红包"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

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
            padding: 3px 10px;
            width: 323px;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
        }

        #startTime {
            width: 150px;
        }

        #endTime {
            width: 150px;
        }

        textarea {
            width: 323px;
            resize: none;
            height: 75px;
            outline: none;
            border: 1px solid #ccc;
        }

        .preview {
            width: 190px;
            height: 336px;
            border: 1px solid #ccc;
            margin: 0 auto;
        }

        .award-table td {
            position: relative;
        }

        .table > tbody > tr > td {
            padding: 8px 0 20px;
        }

        .award-table .error-tip {
            position: absolute;
            top: 35px;
            left: 0;
            right: 0;
            display: block;
            text-align: center;
            max-width: 100%;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>新增红包
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/entRedpacket/save.html" method="post" id="table_validate"
          enctype="multipart/form-data">
        <input type="hidden" name="phones"/>
        <input type="hidden" name="isWhiteFlag" value="0"/>
        <input type="hidden" name="startFlag" id="startFlag" value="true"/>
        <input type="hidden" name="activityType" id="activityType" value="R"/>
        <input type="hidden" name="type" id="type" value="0"/>

        <div class="tile mt-30">

            <div class="tile-header">
                活动信息
            </div>
            <div class="tile-content">
                <div class="row form">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>企业名称：</label>
                            <select required name="entId" id="enterpriseName">
                                <option value="">---请选择---</option>
                            <#list enterprises as e>
                                <option value="${e.id}"
                                        <#if (entRedpacket.entId)?? && e.id == entRedpacket.entId>selected</#if>
                                >${e.name}</option>
                            </#list>
                            </select>
                            <input type="button" id="authorize" value="一键授权" onclick="startAuthorize();"
                                   style="display: none;"/>
                        </div>
                        <div class="form-group">
                            <label>是否使用微信鉴权：</label>
                            <input type="radio" name="wechatAuth" id="useWechat" value="0" checked="checked"/>否 &nbsp;&nbsp;
                            <input type="radio" name="wechatAuth" id="nouseWechat" value="1"/>是
                        </div>
                        <div class="form-group">
                            <label>活动名称：</label>
                            <input type="text" name="aName" id="aName" class="hasPrompt" required>
                        </div>

                        <div class="form-group">
                            <label>活动时间：</label>
                                <span id="activeTime">
                                    <input type="text" name="startTime" id="startTime" readOnly="readOnly"
                                           class="hasPrompt">&nbsp;至&nbsp;<input type="text" id="endTime"
                                                                                 readOnly="readOnly" name="endTime"
                                                                                 class="hasPrompt">
                                </span>
                            <input type="text"
                                   style="width:0;height:0;padding: 0; margin: 0; border: 1px solid transparent;"
                                   name="startendTime" id="startendTime" required>
                        </div>

                        <div class="form-group">
                            <label>企业余额：</label>
                            <span id="account"></span>
                        </div>

                    </div>
                    <!--<div class="col-md-6">                        
                        <div class="form-group">
                            <label>黑白名单：</label>
                            <div class="file-box" style="display: inline-block">
                                <input type="text" name="blackWhiteList" id="blackWhiteList">
                                <input type="file" class="file-helper">
                            </div>
                        </div>
                        
                        
                        <div class="form-group">
                            <label>渠道同步（可选项）：</label>
                            <span class="input-checkbox">
                                <input type="radio" value="1" name="sync" id="sync_1" checked="checked" class="hidden">
                                <label for="sync_1" class="c-checkbox mr-15 rt-1"></label>
                                <span>微信公众号</span>
                            </span>
                            <span class="input-checkbox ml-30">
                                <input type="radio" value="2" name="sync" id="sync_2" class="hidden">
                                <label for="sync_2" class="c-checkbox mr-15 rt-1"></label>
                                <span>APP</span>
                            </span>
                        </div>
                    </div>-->
                </div>

            </div>
        </div>


        <div class="tile mt-30">
            <div class="tile-header">
                模板配置
            </div>
            <div class="tile-content">
                <div class="row">
                    <div class="col-md-5">
                        <div class="form-group col-sm-9">
                            <span>模板选择:</span>
                        <span class="input-checkbox">
                            <input type="radio" value="1" name="templateType" id="templateType_1" class="hidden"
                                   checked="checked">
                            <label for="templateType_1" class="c-checkbox mr-15 rt-1"></label>
                            <label>固定模板</label>
                        </span>
                        </div>
                        <!--<span class="input-checkbox ml-30">
                            <input type="radio" value="2" name="templateType" data-bind="checked: templateType" id="templateType_2" class="hidden">
                            <label for="templateType_2" class="c-checkbox mr-15 rt-1"></label>
                            <span>自定义模板</span>
                        </span>-->
                        <div class="form-group col-sm-9">
                            <label>红包描述</label>
                            <textarea rows="10" cols="100" maxlength="500" id="description" name="description"
                                      class="hasPrompt" required>${description!}</textarea>
                            <span class="help-block">活动描述长度不超过100个字符,需要换行的字符之间用"|"间隔。</span>
                            <span style="color: red" id="error_msg">${(localLabelConfig['REDPACKET_DESCRIPTION_RED_WARNING'])!}</span>
                        </div>

                        <div class="form-group col-sm-9">
                            <label>活动对象</label>
                            <textarea rows="10" cols="100" maxlength="500" id="people" name="people" class="hasPrompt"
                                      required>${people!}</textarea>
                            <span class="help-block">活动对象描述长度不超过100个字符,需要换行的字符之间用"|"间隔。</span>
                            <span style="color: red" id="error_msg">${(localLabelConfig['REDPACKET_PEOPLE_RED_WARNING'])!}</span>
                        </div>

                        <div class="form-group col-sm-9">
                            <label>活动规则</label>
                            <textarea rows="10" cols="100" maxlength="500" id="activityDes" name="activityDes"
                                      class="hasPrompt" required>${activityDes!}</textarea>
                            <span class="help-block">活动规则长度不超过100个字符,需要换行的字符之间用"|"间隔。</span>
                            <span style="color: red" id="error_msg">${(localLabelConfig['REDPACKET_ACTIVITYDES_RED_WARNING'])!}</span>
                        </div>

                    </div>


                    <div class="col-md-7">
                        <span class="pull-left mr-20" style="vertical-align: top">模板预览</span>

                        <div class="pull-left mr-30 text-center">
                            <span>中奖页面</span>
                            <div class="preview">
                                <img src="${contextPath}/assets/imgs/hb.jpg" style="width: 100%">
                            </div>
                            <!--<span class="file-box" style="top: 5px;">
                                <a class="mt-10">上传图片</a>
                                <input type="file" class="file-helper">
                            </span>-->
                        </div>
                        <!--<div class="pull-left text-center">
                            <span>错误页面</span>
                            <div class="preview">

                            </div>
                            <span class="file-box" style="top: 5px;">
                                <a class="mt-10">上传图片</a>
                                <input type="file" class="file-helper">
                            </span>
                        </div>-->
                    </div>
                </div>
            </div>
        </div>

        <!-- 流量配置 -->
        <div class="tile mt-30">
            <div class="tile-header">
                流量配置
                <span class="caret upArrow pull-right mt-20 mr-10"></span>
            </div>
            <div class="tile-content">
                <div class="">
                    <div class="">
                        <span class="gray-text mr-20">注：奖品数量为1-999999数值</span>

                        <a class="btn btn-sm btn-cyan pull-right add-award">添 加</a>
                    </div>
                    <div class="table-responsive mt-20">
                        <table class="table table-bordered text-center award-table">
                            <thead>
                            <tr>
                                <th>奖项</th>
                                <th>产品名称</th>
                                <th>奖品数量</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody data-bind="foreach: awards">
                            <tr>
                                <td>
                                    <input type="text" class="rankName" name="rankName" required
                                           data-bind="value: awardsText[$index()]" readonly="readonly">
                                </td>
                                <td>
                                    <select name="productName" class="productName" data-bind="value: productName"
                                            required></select>
                                </td>
                                <td>
                                    <input type="text" data-bind="value: count" class="count" name="count" maxlength="6"
                                           value="1" required data-rule-number="true">
                                </td>

                                <td>
                                    <a class="btn btn-sm btn-link" data-bind="click: $parent.removeAward">删除</a>
                                    <i class="icon icon-arrow-up" data-bind="click: $parent.upAward"
                                       style="cursor: pointer;"></i>
                                    <i class="icon icon-arrow-down" data-bind="click: $parent.downAward"
                                       style="cursor: pointer;"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <span class="error-tip hidden" id="award-error"></span>
                    </div>
                </div>
            </div>
        </div><!-- 流量配置 end -->

        <div class="mt-30 text-center">
            <a href="javascript:void(0)" class="btn btn-warning btn-sm mr-10 dialog-btn" id="save-btn">提交</a>
            <span style='color:red' id="error_msg">${errorMsg!}</span>
        </div>

        </from>
</div>

<!-- loading -->
<div id="loadingToast" class="weui_loading_toast">
    <div class="weui_mask_transparent"></div>
    <div class="weui_toast">
        <div class="weui_loading">
            <div class="weui_loading_leaf weui_loading_leaf_0"></div>
            <div class="weui_loading_leaf weui_loading_leaf_1"></div>
            <div class="weui_loading_leaf weui_loading_leaf_2"></div>
            <div class="weui_loading_leaf weui_loading_leaf_3"></div>
            <div class="weui_loading_leaf weui_loading_leaf_4"></div>
            <div class="weui_loading_leaf weui_loading_leaf_5"></div>
            <div class="weui_loading_leaf weui_loading_leaf_6"></div>
            <div class="weui_loading_leaf weui_loading_leaf_7"></div>
            <div class="weui_loading_leaf weui_loading_leaf_8"></div>
            <div class="weui_loading_leaf weui_loading_leaf_9"></div>
            <div class="weui_loading_leaf weui_loading_leaf_10"></div>
            <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">数据加载中</p>
    </div>
</div>
<!-- loading end -->

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


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>

    var MAXAWARDNUM = 6;
    var awardsText = ["一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖"];
    var viewModel, products;

    require(["common", "bootstrap", "daterangepicker", "knockout"], function (a, b, c, ko) {

        initViewModel(ko);
        //初始化日期组件
        initDateRangePicker2();

        chooseSmsTemplate();

        checkFormValidate();

        listeners();

        addAwardListener();

        $("#enterpriseName").change(function () {
            viewModel.awards.removeAll();
            products = [];

            var wechatAuth = $("input[name='wechatAuth']:checked").val();
            enterpriseChoiceListener($("#enterpriseName"), wechatAuth);
        });

        $("input[name='wechatAuth']").change(function () {


            var wechatAuth = $("input[name='wechatAuth']:checked").val();
            enterpriseChoiceListener($("#enterpriseName"), wechatAuth);

        });

    });

    /**
     * 产品名称
     */
    function appendProducts() {
        var ele = $(".productName:last");
        ele.empty();
        ele.append("<option value=''>请选择</option>");

        for (var i in products) {
            ele.append('<option value="' + products[i].id + '" data-code="' + products[i].productCode + '" data-productSize="' + products[i].size + '">' + products[i].name + '</option>');
        }
    }

    /**
     * 活动信息中的产品名称
     */
    function appendActiveInfoProducts() {
        var ele = $("#prdId1");
        ele.empty();
        ele.append("<option value=''>---请选择---</option>");

        for (var i in products) {
            ele.append('<option value="' + products[i].id + '" data-code="' + products[i].productCode + '" data-productSize="' + products[i].size + '">' + products[i].name + '</option>');
        }
    }


    /**
     * 企业名称选择
     */
    function enterpriseChoiceListener(ele, wechatAuth) {
        var enterCode = $("option:selected", ele).data("code");
        ele.data("code", enterCode);
        var val = ele.val();
        if (wechatAuth == "1") {
            isAuthorized(val);
        }
        else {
            $("#authorize").hide();
        }
        getProducts(val);


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
    function getProducts(enterId) {
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

            appendActiveInfoProducts();

            if (data.account != null) {
                var account = data.account / 100.0;
                $("#account").html(account + " 元");
            }

        });
    }
    /**
     * 事件监听
     */
    function listeners() {

        $("#save-btn").on("click", function () {

            var awards = $(".award-table tbody tr");
            if (awards.length > 0) {
                //校验上一奖项
                var pre_award = awards.last();
                var productId = pre_award.find(".productName").val();
                var count = pre_award.find(".count").val();

                if (productId == "" || count == "") {
                    showTip("奖项信息不完整！");
                    return false;
                }

                if (!count.match(/^\d+$/) || Number(count) < 0 || Number(count) > Number($(".balance", this).val())) {

                    showTip("奖项数量设置错误！");
                    return false;
                }
            }

            if ($("#table_validate").validate().form()) {
                showToast();
                var entId = $("#enterpriseName").val();
                var aName = $('#aName').val();
                var maxPerUser = $('#maxPerUser').val();
                var description = $('#description').val();
                var people = $('#people').val();
                var activityDes = $('#activityDes').val();
                var probabilityType = $("input[name='probabilityType']:checked").val();
                var startFlag = $('#startFlag').val();
                var startTime = $('#startTime').val();
                var endTime = $('#endTime').val();


                var params = {
                    entId: entId,
                    wechatAuth: $("input[name='wechatAuth']:checked").val(),
                    activityName: aName,
                    maxPerUser: maxPerUser,
                    description: description,
                    people: people,
                    activityDes: activityDes,
                    startFlag: startFlag,
                    probabilityType: probabilityType,
                    startTime: startTime,
                    endTime: endTime,
                    prizes: []
                };


                var award_ele = $(".award-table tbody tr");
                if (!award_ele.length) {
                    $("#award-error").removeClass("hidden");
                    $("#award-error").html("请添加奖品！");
                    hideToast();
                    return false;
                }

                var totalProbability = 0;
                award_ele.each(function (index) {
                    var obj = {
                        idPrefix: index,
                        rankName: $(".rankName", this).val(),
                        productId: $(".productName", this).val(),
                        count: $(".count", this).val(),
                        probability: $(".probability", this).val()
                    };

                    if (!obj.count.match(/^\d+$/) || Number(obj.count) < 0 || Number(obj.count) > Number($(".balance", this).val())) {

                        hideToast();
                        showTip("奖项数量设置错误！");
                        return false;
                    }
                    params.prizes.push(obj);
                });

                var entId = jQuery("#enterpriseName option:selected").val();
                if (params.wechatAuth == "0") {
                    var paramurl = JSON.stringify(params);
                    $.ajax({
                        beforeSend: function (request) {
                            var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                            var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                            request.setRequestHeader(header1, token1);
                        },
                        type: "POST",
                        url: "${contextPath}/manage/entRedpacket/save.html",
                        data: {
                            redPacketPage: paramurl,
                            entId: entId
                        },
                        dataType: "json",
                        success: function (ret) {
                            hideToast();
                            //console.log(ret);
                            if (ret && ret.success == "true") {
                                //showTip("保存成功");
                                //跳转到list页面?
                                window.location.href = "${contextPath}/manage/entRedpacket/index.html";
                            } else {
                                $("#error_msg").html(ret.msg);
                                hideToast();
                            }
                        },
                        error: function () {
                        }
                    });
                }
                else {
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
                                url: "${contextPath}/manage/entRedpacket/save.html",
                                type: "post",
                                data: {
                                    redPacketPage: paramurl,
                                    entId: entId
                                },
                                dataType: "json",
                                success: function (ret) {
                                    //console.log(ret);
                                    hideToast();
                                    if (ret && ret.success == "true") {
                                        //showTip("保存成功");
                                        //跳转到list页面?
                                        window.location.href = "${contextPath}/manage/entRedpacket/index.html";
                                    } else {
                                        $("#error_msg").html(ret.msg);
                                        hideToast();
                                    }
                                }
                            });
                        } else {
                            hideToast();
                            alert("企业未授权！");
                        }
                    });
                }


            }

            return false;
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
     * 初始化日期选择器
     */
    function initDateRangePicker2() {

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

                $("#startendTime").val(s1 + s2);

                var validator = $("#table_validate").validate();
                if (validator.check($("#startendTime")[0])) {
                    var err = validator.errorsFor($("#startendTime")[0]);
                    if (err && err.length) {
                        err.hide();
                    }
                }
            }
        });
    }

    function chooseSmsTemplate() {
        var smsTemplateId = $("#smsTemplateId option:selected").val();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/sms/getSmsTemplateAjax.html",
            data: {
                smsTemplateId: smsTemplateId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                $("#smsContent").html(data.content);
            },
            error: function () {
                $("#smsContent").html("");
            }
        });
    }


    function checkFormValidate() {
        $("#table_validate").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                element.parent().append(error);
                if ($(element).siblings('div').hasClass('prompt')) {
                    $(element).siblings('div.prompt').before(error);
                } else {
                    $(element).parent().append(error);
                }
            },
            errorElement: "span",
            rules: {
                aName: {
                    required: true,
                    maxlength: 20
                },
                entId: {
                    required: true
                },
                startendTime: {
                    required: true
                },
                description: {
                    required: true,
                    maxlength: 100,
                    remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="+$('#description').val()
                },
                people: {
                    required: true,
                    maxlength: 100,
                    remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="+$('#people').val()
                },
                activityDes: {
                    required: true,
                    maxlength: 100,
                    remote: "${contextPath}/manage/sensitiveWords/checkSensitiveWords.html?name="+$('#activityDes').val()
                }
                /*,
                productName : {
                    required: true
                },
                count : {
                    required: true,
                    positive : true
                }*/
            },
            messages: {
                aName: {
                    required: "请填写活动名称",
                    maxlength: "活动名称不超过20个字符!"
                },
                entId: {
                    required: "必须选择企业"
                },
                startendTime: {
                    required: "请填写活动时间"
                },
                description: {
                    required: "请填写红包描述",
                    maxlength: "红包描述不能超过100个字符!",
                    remote: "文案中有不良信息，请删除后再次提交"
                },
                people: {
                    required: "请填写活动对象描述",
                    maxlength: "活动对象描述不能超过100个字符!",
                    remote: "文案中有不良信息，请删除后再次提交"
                },
                activityDes: {
                    required: "请填写活动规则",
                    maxlength: "活动规则不能超过100个字符!",
                    remote: "文案中有不良信息，请删除后再次提交"
                }
                /*,
                productName : {
                    required: "请选择产品"
                },
                count : {
                    required: "请填写奖品数量",
                    positive : "奖品数量需正整数"
                }*/
            }
        });
    }


    function initViewModel(ko) {
        viewModel = {
            probabilityType: ko.observable("0"),
            awards: ko.observableArray([]),
            removeAward: function () {
                viewModel.awards.remove(this);
            }
        };

        viewModel.probabilityDisplay = ko.dependentObservable({
            read: function () {
                return this.probabilityType() == "1";
            },
            owner: viewModel
        });

        ko.applyBindings(viewModel);
    }


    /**
     * 添加奖项信息
     */
    function addAwardListener() {
        //点击的时候添加一行
        $(".add-award").on("click", function () {
            //隐藏错误提示
            $("#award-error").addClass("hidden");
            $("#award-error").html("");

            var body = $(".award-table tbody");

            if (body.children("tr").length >= MAXAWARDNUM) {
                showTip("最多添加" + MAXAWARDNUM + "个奖项");
                return false;
            }

            var awards = $(".award-table tbody tr");
            if (awards.length > 0) {
                //校验上一奖项
                var pre_award = awards.last();
                var productId = pre_award.find(".productName").val();
                var count = pre_award.find(".count").val();

                if (productId == "" || count == "") {
                    showTip("奖项信息不完整！");
                    return false;
                }

                if (!count.match(/^\d+$/)) {
                    showTip("奖项数量设置错误！");
                    return false;
                }


                //奖项设置正确
                pre_award.find(".enterpriseName").attr("disabled", "disabled");
                pre_award.find(".productName").attr("disabled", "disabled");
                pre_award.find(".count").attr("disabled", "disabled");
                pre_award.find(".count").removeClass("valid");
                pre_award.find(".delBtn").css("display", "none")
            }

            viewModel.awards.push({
                productName: "",
                count: 1,
                probability: 0
            });

            appendProducts();
        });
    }
</script>
</body>
</html>