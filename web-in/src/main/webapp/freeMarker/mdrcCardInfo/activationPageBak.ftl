<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />

<html>

<head>
    <title>卡激活 </title>
    <meta charset="utf-8"/>
    <style>
        .col-sm-9 span {
            line-height: 29px;
            font-size: 14px;
        }
    </style>
</head>

<body>
<div class="page-header">
    <h1>卡激活 <a class="btn btn-white pull-right"
               href="${contextPath}/manage/mdrccardinfo/index.html?configId=${(config.id)!}">返回</a></h1>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div class="table-search clearfix">
            <form class="form-horizontal" role="form" id="table_validate"
                  action="${contextPath}/manage/mdrccardinfo/activate.html?configId=${(config.id)!}&status=3"
                  method="POST">
                <input type="hidden" name="type" value="${type!}"/>
                <input type="hidden" name="ids" value="${ids!}"/>
                <input type="hidden" name="activateCardNums" id="activateCardNums" value="${activateCardNums!}"/>

                <div class="form-group">
                    <label class="col-sm-3 control-label">流量包大小</label>
                    <div class="col-sm-9">
                        <span>${(template.productSize)!}</span>
                    </div>
                </div>

                <div class="space-4"></div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">关联企业</label>
                    <div class="col-sm-9">
                        <select name="enterId" id="enterId" style="width:300px;" onChange="enterChange(this)">
                            <option value=0 selected>请选择关联企业</option>
                        <#if enterpriseList?? && enterpriseList?size gt 0>
                            <#list enterpriseList as enter>
                                <option value="${enter.id}">${enter.name}</option>
                            </#list>
                        </#if>
                        </select>&nbsp;&nbsp;
                        <span style="color:red" id="tip_enterId"></span>

                    </div>
                </div>

                <div class="space-4"></div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">关联产品</label>
                    <div class="col-sm-9">
                        <select name="proId" id="proId" style="width:300px;" onChange="prdChange(this)">
                            <option value=0 selected>请选择产品</option>
                        </select>&nbsp;&nbsp;
                        <span style="color:red" id="tip_proId"></span>

                    </div>
                </div>

                <div class="space-4"></div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">账户余额</label>
                    <div class="col-sm-9">
                        <span id="balance_tip"></span>
                        <input type="hidden" id="balance" value=""/>
                    </div>
                </div>

                <div class="space-4"></div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">本次激活卡数目</label>
                    <div class="col-sm-9">
                        <span>${activateCardNums!0} 张</span> &nbsp;&nbsp;
                    </div>
                </div>

                <div class="space-4"></div>
                <div class="form-group">
                    <label class="col-sm-3 control-label">可激活卡数目</label>
                    <div class="col-sm-9">
                        <span id="acvivatableMsg">${activableCardNums!0} 张</span> &nbsp;&nbsp;
                        <input type="hidden" id="acvivatableNum" value="#{activableCardNums!0}"/>
                    </div>
                </div>

                <div class="space-4"></div>
            <#if list?? && (list?size > 0) >
                <div class="form-group">
                    <label class="col-sm-3 control-label">卡序列号</label>
                    <div class="col-sm-9">
                        <#list list as item>
                            <span>${item.cardNumber}</span><br/>
                        </#list>
                    </div>
                </div>
            </#if>

                <div class="hr hr-24"></div>
                <div class="form-group">
                    <label class="col-sm-3 control-label"></label>
                    <div class="col-sm-9">
                        <button type="button" class="btn btn-info" onclick="doSubmit(this)">激活</button>
                        &nbsp;
                        <span style="color: red" id="errMsg">${errMsg!}</span>
                    </div>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>

        <script>
            $("#table_validate").packValidate({
                rules: {
                    enterId: {
                        required: true
                    },
                    userName: {
                        required: true,
                        maxlength: 64,
                        searchBox: true,
                        remote: {
                            url: "checkuserName.html?id=-1",
                            data: {
                                configName: function () {
                                    return $('#userName').val()
                                }
                            }
                        }
                    },
                    password: {
                        required: true,
                        minlength: 10,
                        maxlength: 20,
                        strictPwd: true
                    },
                    mobilePhone: {
                        required: true,
                        mobile: true,
                        remote: {
                            url: "checkMobileValid.html",
                            type: "get",
                            dataType: "text"
                        }
                    },
                    email: {
                        required: true,
                        email: true,
                        maxlength: 64
                    }
                },
                messages: {
                    roleId: {
                        required: "用户角色不能为空!"
                    },
                    userName: {
                        remote: "用户名称已经存在!"
                    },
                    password: {
                        minlength: "密码长度最小为10位",
                        maxlength: "密码长度最大为20位"
                    },
                    mobilePhone: {
                        remote: "电话号码已存在！"
                    }
                }
            });

            //选择企业
            function enterChange(obj) {
                var enterId = obj.value;
                $("#proId").empty();
                $("#balance").empty();
                $("#balance_tip").empty();
                var option = "";
                $.ajax({
                    beforeSend: function (request) {
                        var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                        var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                        request.setRequestHeader(header1, token1);
                    },
                    type: "POST",
                    data: {
                        enterId: enterId,
                        proSize: "${(template.productSize)!}"
                    },
                    url: "${contextPath}/manage/product/getProductsAjaxBySizeEnterId.html",
                    dataType: "json", //指定服务器的数据返回类型，
                    success: function (data) {
                        option += "<option value=\"0\">请选择产品</option>"
                        $.each(data, function (k, v) {
                            option += "<option value=\"" + v['id'] + "\">" + v['name'] + "</option>";
                        });
                        $("#proId").append(option);
                    },
                    error: function () {
                        var message = "查询失败，请稍后尝试";
                        var remainNum = 0;
                        $("#acvivatableMsg").html(message);
                        $("#acvivatableNum").val(remainNum);
                    }
                });
            }
            ;

            //选择产品
            function prdChange(obj) {
                $("#tip_proId").empty();
                $("#balance").empty();
                $("#balance_tip").empty();
                var enterId = jQuery("#enterId option:selected").val();
                var prdId = jQuery("#proId option:selected").val();

                if (prdId == 0) {
                    $("#tip_proId").append("请选择产品");
                    $("#acvivatableMsg").empty();
                }
                else {
                    $.ajax({
                        beforeSend: function (request) {
                            var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                            var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                            request.setRequestHeader(header1, token1);
                        },
                        type: "GET",
                        url: "${contextPath}/manage/mdrcEnterprises/queryProductActivableAjax.html",
                        data: {
                            enterpriseID: enterId,
                            productID: prdId,
                            configId: "${configId!}"
                        },
                        dataType: "json", //指定服务器的数据返回类型，
                        success: function (data) {
                            var message = data.message;
                            var remainNum = data.remainNum;
                            $("#balance_tip").html(remainNum + " 张");
                            $("#balance").val(remainNum);
                            var acvivatableNum = $("#acvivatableNum").val();
                            if (parseInt(remainNum) > parseInt(acvivatableNum)) {
                                $("#intervalNum").data("ruleMax", acvivatableNum);
                            } else {
                                $("#intervalNum").data("ruleMax", remainNum);
                            }
                        },
                        error: function () {
                            var message = "查询失败，请稍后尝试";
                            var remainNum = 0;
                            $("#balance_tip").html(message);
                            $("#balance").val(remainNum);
                        }
                    });
                }
            }
            ;


            function doSubmit(ele) {
                if ($(ele).hasClass("disabled")) {
                    return false;
                }
                var enterId = $("#enterId").val();
                var proId = $("#proId").val();
                $("#errMsg").empty();
                $("#tip_enterId").empty();
                $("#tip_proId").empty();

                if (enterId == 0) {
                    $("#tip_enterId").append("请选择企业");
                    return;
                }
                if (proId == 0) {
                    $("#tip_proId").append("请选择产品");
                    return;
                }

                var activateCardNums = $("#activateCardNums").val();
                var acvivatableNum = $("#acvivatableNum").val();
                var balance = $("#balance").val();
                var onuse = "";
                if (Number(balance) > Number(acvivatableNum)) {
                    onuse = acvivatableNum;
                } else {
                    onuse = balance;
                }
                if (activateCardNums == '' || acvivatableNum == '' || Number(activateCardNums) > Number(onuse)) {
                    $("#errMsg").append("您当前正在激活的卡数目是" + activateCardNums + "张,超过了可激活卡数目" + onuse + "张，请重新选择关联企业");
                    return;
                }
                $("#table_validate").submit();
            }


        </script>
    </div>
</div>
</body>
</html>