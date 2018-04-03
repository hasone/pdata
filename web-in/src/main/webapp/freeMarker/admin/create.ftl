<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-新增用户</title>
    <meta name="keywords" content="流量平台 新增企业"/>
    <meta name="description" content="流量平台 新增企业"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

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
        }
    </style>
</head>
<body>

<div class="main-container">
    <form class="form-horizontal" action="${contextPath}/manage/user/save.html"
          method="post" name="adminForm" id="table_validate">

        <div class="module-header mt-30 mb-20">
            <h3>用户管理
                <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
                   onclick="javascript:window.location.href='${contextPath}/manage/user/index.html'">返回</a>
            </h3>
        </div>


        <div class="tile mt-30">
            <div class="tile-header">
                新增用户
            </div>
            <div class="tile-content">
                <div class="row form">

                    <div class="col-md-6 col-md-offset-3">
                        <div class="form-group">
                            <label>用户角色：</label>
                            <select name="roleId" id="type" class="select-role" onchange="chooseRole()">
                            <#if !administer.roleId??> <#-- 角色不存在 -->
                                <option value=''>----请选择----</option>
                            </#if>
                            <#list roles as role>
                                <option value="${(role.roleId)! }"
                                        <#if administer?? && (administer.roleId)?? && administer.roleId == role.roleId>selected</#if>>${role.name!}</option>
                            </#list>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>姓名：</label>
                            <input type="text" name="userName" value="${(administer.userName)!}" class='hasPrompt'/>
                            <span style="color:red" id="tip_userName"></span>
                            <br/><span class="prompt">姓名由汉字、英文字符及数字组成，长度不超过64个字符</span>
                        </div>
                        <div class="form-group">
                            <label>企业名称：</label>
                            <select name="enterpriseId" id="enterpriseId">
                            <#-- 用户ID存在, 但没有关联企业时，才显示请选择 -->
                            <#if administer.roleId??&&!(currentEnterpriseId??)&&!(currentCardMakerId??)>
                                <option value="">请选择</option>
                            <#else>
                                <#if currentRoleId?? && (currentRoleId==1 || currentRoleId==2)>
                                    <#if enterprises??>
                                        <#list enterprises as enterprise>
                                            <option value="${enterprise.id }"
                                                <#if currentEnterpriseId?? && currentEnterpriseId==enterprise.id>
                                                    selected="selected"
                                                </#if>
                                            >${enterprise.name }</option>
                                        </#list>
                                    </#if>
                                </#if>
                                <#if currentRoleId?? && currentRoleId==4>
                                    <#list cardmarkers as cardmarker>
                                        <option value="${cardmarker.id }"
                                            <#if currentCardMakerId?? && currentCardMakerId==cardmarker.id>
                                                selected="selected"
                                            </#if>
                                        >${cardmarker.name }</option>
                                    </#list>
                                </#if>
                            </#if>
                            </select>
                            <span style="color:red" id="tip_enterpriseId"></span>
                        </div>
                        <div class="form-group">
                            <label>密码：</label>
                            <input type="password" name="password" value="<#if administer.id?exists>xxxx000__x</#if>"
                                   class="hasPrompt" onfocus="this.value='';$('#isPassChanged').val('true')"/>
                            <br/><span class="prompt">密码必须包含字母、数字、特殊符号且长度为10到20位</span>
                            <input type="hidden" name="isPassChanged" id="isPassChanged" value="false"/>
                        </div>
                        <div class="form-group">
                            <label>手机号码：</label>
                            <input type="text" name="mobilePhone" id="mobilePhone" value="${(administer.mobilePhone)!}"
                                   class="hasPrompt"/>
                            <span style="color:red" id="tip_mobilePhone"></span>
                            <br/><span class="prompt">手机号码由数字组成，长度必须为11位</span>
                        </div>
                        <div class="form-group">
                            <label>邮箱：</label>
                            <input type="text" name="email" id="email" value="${(administer.email)!}"
                                   class="hasPrompt"/>
                            <span style="color:red" id="tip_email"></span>
                            <br/><span class="prompt">邮箱地址由英文字符、数字及下划线组成，必须为合法的邮箱地址</span>
                        </div>
                    </div>
                </div>

            </div>
        </div>


        <div class="mt-30 text-center">
            <input type="submit" class="btn btn-warning btn-sm mr-10 dialog-btn" style="width: 114px;" value="保存"/>&nbsp;&nbsp;
            &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {

        init();
        //验证
        checkFormValidate();
    });

    /**
     * 初始化
     */
    function init() {
        $("#entName").empty();
        $("#entName").append("企业名称");

        if ("${(administer.id)!}" == "1") {
            $("#type").empty();
            $("#type").append("<option value='1'>超级管理员</option>");
            $("#type").val("1");
        }
    }

    function checkFormValidate() {
        $("#table_validate").packValidate({
            errorElement: "span",
            rules: {
                roleId: {
                    required: true
                },
                userName: {
                    required: true,
                    maxlength: 64,
                    searchBox: true
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
                    remote: "姓名称已经存在!"
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
    }
</script>

<script>
    function chooseRole() {

        var roleId = jQuery("#type option:selected").val();

        if (roleId == '') {
            return false;
        }

        if (roleId == 5) {
            $("#entName").empty();
            $("#entName").html("制卡商名称");
        } else {
            $("#entName").empty();
            $("#entName").html("企业名称");
        }

        //ajax
        if (roleId == 3 || roleId == 5) {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                type: "GET",
                url: "${contextPath}/manage/user/getEntersAjax.html",
                data: {
                    roleId: roleId
                },
                dataType: "json", //指定服务器的数据返回类型，
                success: function (res) {
                    $("#enterpriseId").empty();
                    if (res.enters) {
                        var listProducts = res.enters;
                        if (listProducts.length == 0) {
                            $("#enterpriseId").append("<option value=''>---当前没有未绑定的制卡商，请新增或修改绑定后重新尝试---</option>");
                        } else {
                            for (var i = 0; i < listProducts.length; i++) {
                                var id = listProducts[i].id;
                                var name = listProducts[i].name;
                                $("#enterpriseId").append("<option value='" + id + "'>" + name + "</option>");
                            }
                        }
                    }
                    else {
                        $("#enterpriseId").append("<option value=''>-----</option>");
                    }
                },
                error: function () {

                }
            });
        }
        else {
            $("#enterpriseId").empty();
        }
    }
</script>
</body>
</html>