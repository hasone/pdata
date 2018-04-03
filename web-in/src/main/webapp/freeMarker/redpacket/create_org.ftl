<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>新建红包</title>
    <script type="text/javascript">
        $(function () {
            //日期控件，只能选明天开始到下一年
            $('.next-year').packDatetime({
                dateType: 'nextYear',
                todayBtn: false
            });

            $('.next-year').datetimepicker("setStartDate", new Date());
        });
    </script>
    <script type="text/javascript" src="${contextPath}/manage/Js/knockout-3.2.0.js"></script>

    <style>
        .file-box {
            position: relative;
            width: 340px
        }

        .file {
            position: absolute;
            top: 0;
            right: 80px;
            height: 24px;
            filter: alpha(opacity:0);
            opacity: 0;
            width: 260px
        }

        .check-all {
            position: relative;
            top: 1px;
        }

        .table td,
        .table th {
            text-align: center;
        }

        .table-bordered > thead > tr > th, .table-bordered > thead > tr > td {
            border-bottom-width: 1px;
            background: #eee;
            -webkit-user-select: none;
            user-select: none;
            background: -webkit-linear-gradient(top, whitesmoke, #ddd);
            background: -moz-linear-gradient(top, whitesmoke, #ddd);
            background: -ms-linear-gradient(top, whitesmoke, #ddd);
            text-shadow: 0 1px .5px #fff;
        }

        .table-header-fixed {
            overflow: hidden;
        }

        .table-header {
            overflow: hidden;
        }

        .table-body {
            overflow: auto;
            max-height: 247px;
            border-bottom: 1px solid #ddd;
        }

        .table-body .table,
        .table-header .table {
            margin: 0;
            table-layout: fixed;
        }

        .table-header td,
        .table-header th,
        .table-body td {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .table-body .table tbody > tr:last-child td,
        .table-body .table-bordered {
            border-bottom: none;
        }

        .table thead > tr > th.table-tail, .table thead > tr > td.table-tail {
            padding: 0;
        }

        .table-header {
            background-color: none;
            font-size: 12px;
            padding-left: 0;
            margin-bottom: 0;
        }

        .modal-body .table-responsive {
            height: auto;
            overflow-y: auto;
        }
    </style>
</head>

<body>
<div class="page-header">
    <h1>
        新建红包
        <button class="btn btn-white pull-right"
                onclick="javascript:window.location.href='${contextPath}/manage/entRedpacket/index.html'">返回
        </button>
    </h1>
</div>
<!-- /.page-header -->
<div class="row">
    <div class="col-xs-12">
        <form id="createForm" method="post">
            <input type="hidden" name="title" value="快来抢移动流量红包啦！"/>
            <input type="hidden" name="description" value="月底还在缺流量？快来给流量冲个电吧！移动的用户们，快来抢流量红包啦！"/>
            <input type="hidden" name="phones" id="phones" value="${phones!}"/>
            <input type="hidden" name="activityType" id="activityType" value="R"/>

            <table class="table no-border table-hover table-left"
                   id="table_validate">
                <tr>
                    <th class="tableleft">企业名称</th>
                    <td><select name="entId" id="entId" onchange="chooseEnter()" style="width:300px;">
                    <#list enterprises as e>
                        <option value="${e.id}"
                                <#if (entRedpacket.entId)?? && e.id == entRedpacket.entId>selected</#if>
                        >${e.name}</option>
                    </#list>
                    </select>
                        &nbsp;&nbsp;<span style="color:red" id="tip_entId"></span>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">企业编码</th>
                    <td>
                        <select id="enter_code" name="enter_code" disabled style="width:300px;">
                        <#list enterprises as e>
                            <option
                                <#if (entRedpacket.entId)?? && e.id == entRedpacket.entId>selected</#if>
                            >${e.code}</option>
                        </#list>
                        </select>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">产品名称</th>
                    <td>
                        <select name="prdId" id="prdId" onchange="chooseProduct()" style="width:300px;">
                        <#if (products?size>0) >
                            <#list products as p>
                                <option value="${p.id}"
                                        <#if (entRedpacket.prdId)?? && p.id == entRedpacket.prdId>selected</#if>
                                >${p.name}</option>
                            </#list>
                        <#else>
                            <option value=''>---该企业在Boss处没有查询到产品---</option>
                        </#if>
                        </select>
                        &nbsp;&nbsp;<span style="color:red" id="tip_prdId"></span>
                    </td>
                </tr>

                <tr>
                    <td class="tableleft">流量包余额</td>
                    <td>
                        <span id="proNum" style="color: red"></span> &nbsp;&nbsp; <input type="button"
                                                                                         onclick="queryPro()"
                                                                                         value="查询余额"/>
                    </td>
                </tr>


                <tr>
                    <th class="tableleft">
                        抢红包截止时间(<span class="red">*</span>)
                    </th>

                    <td>
                        <div class="control-group">
                            <div class="input-append date next-year"
                                 data-date-format="yyyy-mm-dd 23:59:59">
                                <input type="text"
                                       size="32"
                                       autocomplete="off"
                                       id="deadline"
                                       name="deadline"
                                       value="<#if entRedpacket.deadline?exists>${entRedpacket.deadline?string("
                                           yyyy-MM-dd HH:mm:ss")}</#if>" readonly="readonly"/>
								<span class="add-on">
									<i class="icon-remove"></i></span> <span class="add-on"><i
                                    class="icon-calendar"></i></span>

                                &nbsp;&nbsp;<span style="color:red" id="tip_deadline"></span>

                            </div>
                        </div>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">发放红包总数(<span class="red">*</span>)</th>
                    <td><input type="text"
                               name="total"
                               id="total"
                               value="${(entRedpacket.total)!}"/>

                        &nbsp;&nbsp;<span style="color:red" id="tip_total"></span>

                        <br/><span class="prompt">红包总数由数字组成，长度不超过6个字符</span>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">每用户可抢红包数(<span class="red">*</span>)</th>
                    <td>

                        <!-- 如果当前设置规则设定了相应的用户最大红包数目，则显示该数量，否则显示默认的数量 -->
                        <input type="text" name="maxPerUser"
                               id="maxPerUser" value="${(entRedpacket.maxPerUser)! }"/>

                        &nbsp;&nbsp;<span style="color:red" id="tip_maxPerUser"></span>


                        <br/><span class="prompt">每用户最多可抢红包数由数字组成，数值范围为1~10</span>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">黑白名单</th>
                    <td>
                        <select name="isWhiteFlag" id="isWhiteFlag" onchange="updateModalTittle(this)">
                            <option value="0" selected>请选择名单类型</option>
                            <option value="1">白名单</option>
                            <option value="2">黑名单</option>
                        </select>

                        &nbsp;&nbsp;<span style="color:red" id="tip_isWhiteFlag"></span>

                    </td>
                </tr>

                <tr id="phonesCount-wrap">
                    <th class="tableleft"></th>
                    <td>
                        <div class="input-group" style="width: 28px;">
                            <input class="input-sm" type="text" id="phonesCount"
                                   value="已有<#if phoneNumber??>${phoneNumber}<#else>0</#if>个号码"/>
                                <span class="input-group-addon" data-toggle="modal" data-target="#my-modal2"
                                      style="background: url('${contextPath}/manage/assets/images/ico-write.png') no-repeat center center; padding-left: 15px; padding-right: 15px;"></span>
                        </div>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">短信功能</th>
                    <td>
                        <select name="useSms" id="useSms" onchange="chooseSms()" style="width:300px;">
                            <option value=0>不开启短信发送功能</option>
                            <option value=1>开启短信功能</option>
                        </select>

                        &nbsp;&nbsp;<span style="color:red" id="tip_useSms"></span>

                    </td>
                </tr>

                <tr>
                    <th class="tableleft"></th>
                    <td>
                        <select id="smsTemplateId" name="smsTemplateId" style="width:300px;">
                        <#if smsTemplates??>
                            <#list smsTemplates as s>
                                <option value="${s.id}"
                                        <#if (entRedpacket.smsTemplateId)?? && s.id == entRedpacket.smsTemplateId>selected</#if>
                                >${s.name}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">模板类型(<span class="red">*</span>)</th>
                    <td>
                        <select name="type" id="type" onchange="chooseTemplate()" style="width:300px;">
                            <option value=0>请选择</option>
                            <option value=1>默认模板：中国红</option>
                            <option value=2>自定义模板</option>
                        </select>

                        &nbsp;&nbsp;<span style="color:red" id="tip_type"></span>

                    </td>
                </tr>


                <tr>
                    <th class="tableleft">模板名称(<span class="red">*</span>)</th>
                    <td>
                        <select id="templateId" name="templateId" onchange="showTemplate()" style="width:300px;">
                        <#if templates??>
                            <#list templates as t>
                                <option value="${t.id}"
                                        <#if (entRedpacket.templateId)?? && t.id == entRedpacket.templateId>selected</#if>
                                >${t.name}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                </tr>

                <tr>
                    <th class="tableleft">模板预览</th>
                    <td>
                        <img id="template-preview" src="" style="width:100px;">
                        <span class="tip-text"></span>
                    </td>
                </tr>


            </table>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>

                <div class="col-sm-9">
                    <input type="button" value="保存" class="btn btn-info" onclick="doSubmit(0)"/>&nbsp;&nbsp;
                    <input type="button" value="快速上架" class="btn btn-info" onclick="doSubmit(1)"/>&nbsp;&nbsp;
                    <span style="color: red" id="error_msg">${errorMsg!}</span>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

        <div id="my-modal2" class="modal small-modal fade" style="padding-top: 100px;">
            <div class="modal-dialog" style="width: 600px;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>

                        <h5 class="modal-title">
                            名单
                        </h5>
                    </div>
                    <div class="modal-body" style="padding: 20px 50px;">

                        <form class="form-horizontal" role="form" action="#" method="post"
                              enctype="multipart/form-data" method="POST">

                            <div class="form-group">
                                <label class="col-sm-3 control-label"> 手机号码 </label>

                                <div class="col-sm-9">
                                    <input class="input-sm" type="text" id="phone" maxlength="11" value=""
                                           style="width: 180px;"/>
                                    <button class="btn btn-bg-white" onclick="add(); return false;">添加</button>
                                </div>
                            </div>

                            <div class="space-4"></div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label"> 批量输入 </label>

                                <div class="col-sm-9">
                                    <div class="file-box ">

                                        <input type='text' name='textfield' id='textfield' class='txt'/>
                                        <input type='button' class='btn btn-bg-white' value='浏览...'/>
                                        <input type="file" name="file" class="file" id="file" size="28"
                                               onchange="readFileName(this)" style=""/>
                                        <input type="button" name="submit" class="btn btn-bg-white" value="上传"
                                               onclick="upload();"/>

                                    </div>
                                    支持格式txt、xls和xlsx，每行一个号码
                                </div>
                                <div class="col-sm-9 col-sm-offset-3">
                                    <p class="red text-left" id="modal_error_msg"></p>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="search_phone"> 查询号码 </label>
                                <div class="col-sm-9">
                                    <div class="file-box ">
                                        <input id="search_phone" type="text">
                                    </div>
                                </div>
                            </div>

                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

                        <div class="table-responsive">
                            <div class="table-header">
                                <table id="sample-table-1" class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th><input type="checkbox" class="check-all">全选</th>
                                        <th>序号</th>
                                        <th>手机号码</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                </table>
                            </div>
                            <div class="table-responsive table-body">
                                <table class="table table-bordered">
                                    <tbody data-bind="foreach: phones">
                                    <tr>
                                        <td><input type="checkbox" class="check-item"></td>
                                        <td data-bind="text: $index()+1"></td>
                                        <td><label data-bind="text: phone"></label>
                                            <input type="hidden" name="phone_num" data-bind="value: phone"/></td>
                                        <td>
                                            <button data-bind="click: $parent.removePerson">删除</button>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div style="margin-bottom: 3px;">
                            <div class="text-left">
                                <label style="margin-bottom: 4px;" class="pull-left">
                                    <a class="btn btn-info" id="batchDelete">批量删除</a>
                                </label>
                            </div>
                        </div>

                        <br/>
                        <br/>

                        <p>
                            <button class="btn btn-info" data-dismiss="modal" onclick="getAllNums();">提交</button>
                        </p>
                    </div>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal-dialog -->
        </div>
        <!-- /.modal -->
    </div>
</div>

<script src="${contextPath}/manage/assets/js/ajaxfileupload.js"></script>

<script type="text/javascript">
    function resizeTable() {
        var body_height = $(".table-body").outerHeight();
        var body_table_height = $(".table-body .table").outerHeight();
        var scrollw = $('.table-body')[0].offsetWidth - $('.table-body')[0].scrollWidth;

        if (body_table_height > body_height) {
            $(".table-header tr").find(".table-tail").remove();
            $(".table-header tr").append('<th class="table-tail" style="width: ' + scrollw + 'px;"></th>');
        } else {
            $(".table-header tr th.table-tail").remove();
        }
    }

    $(function () {
        $(".check-all").on("click", function () {
            var isChecked = $(this).is(":checked");
            $(".table-body .check-item").prop("checked", isChecked);
            if (isChecked) {
                $(".table-body .check-item").attr("checked", "checked");
            } else {
                $(".table-body .check-item").removeAttr("checked");
            }
        });

        $(".table-body").on("click", ".check-item", function () {
            var allchecked = true;
            $(".table-body .check-item").each(function () {
                if (!$(this).is(":checked")) {
                    allchecked = false;
                }
            });
            $(".check-all").prop("checked", allchecked);
            if (allchecked) {
                $(".check-all").attr("checked", "checked");
            } else {
                $(".check-all").removeAttr("checked");
            }
        });

        //批量删除
        $("#batchDelete").on("click", function () {
            $(".table-body tr:not(.hide-row)").each(function () {
                var item = $(this).find(".check-item");
                if (item.is(":checked")) {
                    var phone = $(this).find("input[name='phone_num']").val();
                    viewModel.phones.remove(phoneMap[phone]);
                    delete phoneMap[phone];
                }
            });
            return false;
        });

        $("#search_phone").keyup(function () {
            var str = $(this).val();
            var parent = $(".table-body tbody");
            if (str == "") {
                $(".hide-row", parent).fadeIn();
            } else {
                $("label", parent).each(function () {
                    var phone = $(this).html();
                    var tr = $(this).parents("tr");
                    if (phone.indexOf(str) == -1) {
                        tr.addClass("hide-row");
                        tr.fadeOut();
                    } else {
                        tr.removeClass("hide-row");
                        tr.fadeIn();
                    }
                });
            }
        });

        $("#isWhiteFlag").change();
    });

    var phoneList = [];
    var phoneMap = {};
    var viewModel = {
        phones: ko.observableArray(phoneList),
        removePerson: function () {
            viewModel.phones.remove(this);
            delete phoneMap[this.phone];
        }
    };
    ko.applyBindings(viewModel);

    $("#createForm").packValidate({
        rules: {
            deadline: {
                required: true,
                maxlength: 100
            },
            total: {
                required: true,
                maxlength: 6,
                positive: true
            },
            maxPerUser: {
                required: true,
                range: [1, 10],
                digits: true
            },
            prdId: {
                required: true,
            }

        },
        messages: {
            total: {
                maxlength: "请输入最大为6位的整数"
            }
        }
    });

    function queryPro() {
        $("#proNum").empty();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/entproquery/exchangeAjax.html",
            data: {
                enterpriseCode: $('#enter_code').val(),
                productID: $('#prdId').val(),
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var message = data.message;
                $("#proNum").append(message);
            },
            error: function () {
                var message = "网络出错，请重新尝试";
                $("#proNum").append(message);
            }
        });
    }
    ;

    //返回列表
    $('#returnBack').click(function () {
        window.location.href = "${contextPath}/manage/entRedpacket/index.html"
    });

    function whiteOrBlack() {
        var isWhite = jQuery("#isWhite option:selected").val();
        jQuery("#isWhiteFlag").val(isWhite);
    }

    function isNullOrNot() {
        var phonesVal = $("#phones").val();
        var listNullVal = $("#listNull").var();
        listNullVal = (phonesVal === "" ? 1 : 2);
    }

    //图片预览
    function showTemplate() {

        var index = jQuery("#type option:selected").index();

        //	alert("index="+index);

        if (index != 2) {
            return false;
        }

        var templateId = $("#templateId").val();

        //  	alert("templateId="+templateId);

        if (templateId == undefined) {
            return false;
        }

        //ajax
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/rule_template/getPictureAjax.html",
            data: {
                templateId: templateId
            },
            dataType: "json",
            success: function (res) {
                if (res) {
                    if (res.fail == "notExist") {
                        $("#template-preview").hide();
                        $("#template-preview").parent().children(".tip-text").show().html("暂无模板");
                    }
                    else {
                        $("#template-preview").show();
                        $("#template-preview").parent().children(".tip-text").hide();


                        //		var picturePos = "${contextPath}
                        /manage/rule_template/getFile.html?id="+templateId+"&filename="+res.filename;
                        //		alert("picturePos="+picturePos);
                        $("#template-preview").attr("src", "${contextPath}/manage/rule_template/getFile.html?id=" + templateId + "&filename=" + res.filename);

                    }
                }
            },
            error: function () {
            }
        });
    }

    //选择短信模板
    function chooseSms() {
        var index = jQuery("#useSms option:selected").index();

        if (index == 0) {
            $("#smsTemplateId").empty();
            //		$("#smsTemplateId").append("<option value=''>---不设置短信模板---</option>");
            return false;
        }

        //ajax
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/rule_sms_template/getSmsTemplateAjax.html",
            data: {
                type: "RP"
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                if (data) {

                    if (data.fail && data.fail == "fail") {
                        $("#smsTemplateId").empty();
                        $("#smsTemplateId").append("<option value=''>---查找短信模板失败---</option>");
                    }
                    else if (data.fail && data.fail == "notExist") {
                        $("#smsTemplateId").empty();
                        $("#smsTemplateId").append("<option value=''>---未设置短信模板---</option>");
                    }
                    else {

                        var smsTemplates = data.templates;

                        if (smsTemplates.length && smsTemplates.length != 0) {

                            $("#smsTemplateId").empty();
                            $("#smsTemplateId").append("<option value=''>---请选择---</option>");

                            for (var i = 0; i < smsTemplates.length; i++) {
                                var id = smsTemplates[i].id;
                                var name = smsTemplates[i].name;
                                $("#smsTemplateId").append("<option value='" + id + "'>" + name + "</option>");
                            }
                        }
                        else {
                            $("#smsTemplateId").empty();
                            $("#smsTemplateId").append("<option value=''>---未设置短信模板---</option>");
                        }
                    }
                }
            }
        });
    }

    //选择红包模板
    function chooseTemplate() {
        var index = jQuery("#type option:selected").index();

        if (index == 0) {
            $("#templateId").empty();
            $("#templateId").append("<option value=''>---未选择模板---</option>");

            $("#template-preview").hide();

            return false;
        }
        else if (index == 1) {
            $("#templateId").empty();
            $("#templateId").append("<option value=''>中国红</option>");

            $("#template-preview").show();
            $("#template-preview").parent().children(".tip-text").show().html("");
            $("#template-preview").attr("src", "${contextPath}/redpacket/img/bg_1.png");
            return false;
        }

        //选择模板前隐藏
        $("#template-preview").hide();

        //ajax

        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/rule_template/getTemplateAjax.html",
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {

                if (data) {
                    if (data.fail == "fail") {
                        $("#templateId").empty();
                        $("#templateId").append("<option value=''>---获取模板列表失败---</option>");
                        $("#template-preview").hide();
                    }
                    else if (data.fail == "notExist") {
                        $("#templateId").empty();
                        $("#templateId").append("<option value=''>---未创建红包模板---</option>");
                        $("#template-preview").hide();
                    }
                    else if (data.templates && data.templates.length) {
                        var templates = data.templates;

                        if (data.templates.length != 0) {

                            //		alert("length="+ data.templates.length);

                            $("#templateId").empty();
                            $("#templateId").append("<option value=''>---请选择---</option>");

                            for (var i = 0; i < templates.length; i++) {
                                var id = templates[i].id;
                                var name = templates[i].name;
                                $("#templateId").append("<option value='" + id + "'>" + name + "</option>");
                            }

                            //    $("#templateId").change();

                        }
                        else {
                            $("#templateId").empty();
                            $("#templateId").append("<option value=''>---未创建红包模板---</option>");
                            $("#template-preview").hide();
                        }
                    }
                }
            },
            error: function () {

            }
        });
    }

    function chooseEnter() {
        var index = jQuery("#entId option:selected").index();
        if (index != undefined) {
            $('#enter_code')[0].selectedIndex = index;
        }

        var enterId = jQuery("#entId option:selected").val();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/product/getProductsAjax.html",
            data: {
                enterpriseId: enterId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var listProducts = data.products;
                $("#prdId").empty();
                if (listProducts.length == 0) {
                    $("#prdId").append("<option value=''>---该企业在Boss处没有查询到产品---</option>");
                }
                for (var i = 0; i < listProducts.length; i++) {
                    var id = listProducts[i].id;
                    var name = listProducts[i].name;
                    $("#prdId").append("<option value='" + id + "'>" + name + "</option>");
                }
            },
            error: function () {
                $("#prdId").empty();
            }
        });
    }

    function doSubmit(obj) {

        $("#tip_entId").empty();
        $("#tip_prdId").empty();
        $("#tip_deadline").empty();
        $("#tip_total").empty();
        $("#tip_maxPerUser").empty();
        $("#tip_useSms").empty();
        $("#tip_type").empty();

        $("#error_msg").empty();
        if ($("#entId").val() == '') {
            $("#tip_entId").append('企业名称不能为空!');
            return false;
        }

        if ($("#prdId").val() == '') {
            $("#tip_prdId").append("产品名称必需存在!");
            return false;
        }


        if ($("#deadline").val() == '') {
            $("#tip_deadline").append("请选择抢红包截止日期!");
            return false;
        }

        if ($("#total").val() == '') {
            $("#tip_total").append("请输入发放红包总数!");
            return false;
        }

        if ($("#maxPerUser").val() == '') {
            $("#tip_maxPerUser").append("请输入每个用户可抢红包数!");
            return false;
        }

        var index = jQuery("#useSms option:selected").index();
        var smsTemplateId = jQuery("#smsTemplateId option:selected").val();
        if (index == 1 && smsTemplateId == '') {
            $("#tip_useSms").append("已选择启用短信功能,请选择短信模板!");
            return false;
        }

        var redpackageIndex = jQuery("#type option:selected").index();
        if (redpackageIndex == 0) {
            $("#tip_type").append("请选择模板类型!");
            return false;
        }

        var templateId = jQuery("#templateId option:selected").val();
        if (redpackageIndex == 2 && templateId == '') {
            $("#tip_type").append("请选择红包模板!");
            return false;
        }

        if ($("#createForm").packValidate().form()) {

            if (Number($("#maxPerUser").val()) > Number($("#total").val())) {
                $("#tip_maxPerUser").append("每个用户可抢红包数不能大于发放红包总数!");
                return false;
            }

            if (obj == 1) {
                if (!confirm("是否确定保存记录并上架？")) {
                    return false;
                }

                //快速上架，将上架参数设为true
                $('#createForm').attr("action",
                        "${contextPath}/manage/entRedpacket/save.html?startFlag=true");
                $('#createForm').submit();


            } else {
                if (!confirm("是否确定保存记录？")) {
                    return false;
                }

                $('#createForm').attr("action",
                        "${contextPath}/manage/entRedpacket/save.html");
                $('#createForm').submit();

            }
        }
    }

    function chooseProduct() {
        var index = jQuery("#prdId option:selected").index();
        if (index != undefined) {
            //$('#product_code')[0].selectedIndex = index;
        }
    }


    function upload() {
        $("#loading").ajaxStart(function () {
            $(this).show();
        }).ajaxComplete(function () {
            $(this).hide();
        });
        $.ajaxFileUpload
        (
                {
                    url: '${contextPath}/manage/month_record/uploadPhones.html',
                    secureuri: false,
                    fileElementId: 'file',
                    dataType: 'text',
                    success: function (data, status) {
                        clearSearch();
                        //开始处理<pre>标签的问题
                        data = data.indexOf("<") == -1 ? data : $.trim($(data).html());

                        //去掉了系统加上的标签后得到的封装后的数据
                        //第一层以;分割
                        //0;25;18867102018，18867101219...   返回的是成功数据，第一项为标识，第二项为成功处理个数，第三项为电话号码字符串，逗号鳄梨
                        //1;只支持...                     返回的是失败数据，第一项为标识，第二项为错误原因
                        var usefulData = data;

                        var splitDatas = usefulData.split(";");
                        $("#img_id").remove();
                        if (splitDatas[0] == "0") {
                            var phonenums = splitDatas[2].split(","); //从后台得到添加的电话号码数据
                            var formerCount = phoneList.length;//得到添加到knockout插件前，插件里的个数
                            for (var i = 0; i < phonenums.length; i++) {
                                if (!phoneMap[phonenums[i]]) {
                                    phoneMap[phonenums[i]] = {'phone': phonenums[i]};
                                    viewModel.phones.push(phoneMap[phonenums[i]]);
                                }
                            }

                            var addedCount = phoneList.length - formerCount;  //得到添加到knockout插件之后，增加的个数
                            $("#modal_error_msg").html("成功导入" + addedCount + "条移动手机号码");

                            sort_phones();

                            resizeTable();
                        }
                        else if (splitDatas[0] == "1") {
                            $("#modal_error_msg").html(splitDatas[1]);

                        }
                        else {
                            $("#modal_error_msg").html("上传文件失败");
                        }


                    },
                    error: function (data, status, e) {

                        $("#error_msg").html("上传文件失败");
                    }
                }
        );
        return false;
    }

    /**
     *
     * @param ele
     */
    function readFileName(ele) {
        var path = ele.value;
        var lastsep = path.lastIndexOf("\\");
        var filename = path.substring(lastsep + 1);
        document.getElementById("textfield").value = filename;
    }

    /**
     *
     */
    function add() {
        clearSearch();

        var phone = $("#phone").val();
        var rex = /^1[3-8][0-9]{9}$/;
        if (!rex.test(phone)) {
            $("#modal_error_msg").html("手机号码格式不正确");
            return;
        }
        var i = checkCQMobile(phone);
        if (i == 0) {
            $("#modal_error_msg").html("非移动手机号码，请重新输入");
            return;
        }
        else {

        }

        if (phoneMap[phone]) {
            $("#modal_error_msg").html("该手机号码已经存在");
            return;
        }

        phoneMap[phone] = {'phone': phone};
        viewModel.phones.push(phoneMap[phone]);
        sort_phones();

        resizeTable();

        $("#phone").val("");
        $("#modal_error_msg").html("");

        return false;
    }

    function clearSearch() {
        $("#search_phone").val("");
        $("#search_phone").keyup();
    }

    function sort_phones() {
        viewModel.phones.sort(function (a, b) {
            return a.phone - b.phone;
        });
    }

    /**
     *
     * @param obj
     * @returns {*}
     */
    function checkCQMobile(obj) {
        var i;
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "post",
            url: "${contextPath}/manage/month_rule/checkCQMobileAjax.html",
            data: {
                mobile: obj
            },
            async: false,
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var checkPhone = data.checkPhone;
                if (checkPhone == 'true') {
                    i = 1;
                }
                else {
                    i = 0;
                }
            },
            error: function () {
                i = 0;
            }
        });
        return i;
    }

    /**
     *
     */
    function getAllNums() {
        var phoneNums = $("input[name='phone_num']");
        if (phoneNums != null && phoneNums != '') {
            var phoneStr = "";
            for (var i = 0; i < phoneNums.length; i++) {
                phoneStr += phoneNums[i].value;
                if (i < phoneNums.length - 1)
                    phoneStr += ",";
            }
            $("#phones").val(phoneStr);//支持多次上传
            $("#phonesCount").val("已有" + phoneNums.length + "个号码");
        }
    }

    function updateModalTittle(ele) {
        var value = ele.value;

        if (value == "0") {
            $("#phonesCount-wrap").hide();
            $("#phonesCount").val("已有0个号码");
            $("#phones").val("");
        } else {
            $("#phonesCount-wrap").show();
        }
        var tittle = "";
        if (value == 2) {
            tittle = "添加黑名单";
        }
        else if (value == 1) {
            tittle = "添加白名单";
        }
        else {
            tittle = "添加名单";
        }
        $("#my-modal2 .modal-title").html(tittle);
    }
</script>
</body>


</html>

