<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-权限管理</title>
    <meta name="keywords" content="流量平台 新增企业"/>
    <meta name="description" content="流量平台 新增企业"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/icon.css">

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 950px;
        }

        .form input[type='text'],
        .form input[type='number'],
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
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>用户管理
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/user/index.html'">返回</a>
        </h3>
    </div>

    <form action="${contextPath}/manage/user/roleSave.html" method="post" name="adminForm" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-header">
                角色分配
            </div>

            <div class="tile-content">
                <div class="row form">
                    <div class="form-group">
                        <label>用户角色：</label>
                        <select name="roleId" id="type" class="select-role" onchange="chooseRole()" required>
                        <#if !administer.roleId??> <#-- 角色不存在 -->
                            <option value=''>----------请选择--------</option>
                        </#if>
                        <#list roles as role>
                            <option value="${(role.roleId)! }"
                                    <#if administer?? && (administer.roleId)?? && administer.roleId == role.roleId>selected</#if>>${role.name!}</option>
                        </#list>
                        </select>
                    </div>

                    <div class="form-group" id="district">
                        <label>所属区域：</label>
                        <select id="districtId"></select>
                        <input id="districtIdSelect" name="districtIdSelect" type="hidden" value="" required>
                    </div>

                    <div class="form-group">
                        <label>姓名：</label>
                        <input type="text" name="userName" id="userName" value="${(administer.userName)!}"
                               class="hasPrompt" required/>
                    </div>

                    <div class="form-group">
                        <label>手机号码：</label>
                        <input type="text" name="mobilePhone" id="mobilePhone" value="${(administer.mobilePhone)!}"
                               class="hasPrompt" required/>
                        <span style="color:red" id="tip_mobilePhone"></span>
                        <div class="prompt">手机号码由数字组成，长度必须为11位</div>
                    </div>
                </div>
            </div>

            <div class="btn-save mt-30">
                <input type="submit" class="btn btn-sm btn-warning dialog-btn" value="保存"/>&nbsp;&nbsp;&nbsp;
                <span style="color:red" id="error_msg">${errorMsg!}</span>
            </div>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap", "easyui"], function () {
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

        initTree();

        /**
         * 根据选择框初始化页面
         */
        var roleId = jQuery("#type option:selected").val();

        //非省、市、区管理员不显示区域
        if (roleId == 6 || roleId == 2 || roleId == 7) {
            $("#district").show();
        }
        else {
            $("#district").hide();
        }
    }

    function onSubmit() {
        var t = $('#districtId').combotree('tree'); // 得到树对象
        var n = t.tree('getSelected'); // 得到选择的节点

        if (n != null) {
            $("#districtIdSelect").val(n.id);
        }
        $("#table_validate").submit();
        return true;
    }

    /**
     * combotree构建
     */
    function initTree() {
        $('#districtId').combotree({
            url: "${contextPath}/manage/user/getDistrictAjax.html?districtId=${(districtId)!}&${_csrf.parameterName}=${_csrf.token}",
            onBeforeExpand: function (node) {
                $('#districtId').combotree("tree").tree("options").url = "${contextPath}/manage/user/getDistrictAjax.html?${_csrf.parameterName}=${_csrf.token}&parentId=" + node.id;
            },
            onSelect: function (node) {
                $("#districtIdSelect").val(node.id);
            }
        });
    }


    function checkFormValidate() {
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
                roleId: {
                    required: true
                },
                userName: {
                    required: true,
                    cmaxLength: 32,
                },
                districtId: {
                    required: true
                },
                mobilePhone: {
                    required: true,
                    mobile: true,
                    remote: {
                        url: "checkMobileDistrict.html",
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
                distrcitId: {
                    required: "省市区管理员必须选择区域!"
                },
                userName: {
                    required: "姓名不能为空!",
                    cmaxLength: "姓名长度最大为32位"
                },
                mobilePhone: {
                    required: "手机号码不能为空!",
                    remote: "该号码已存在！"
                }
            }
        });

    }
</script>

<script>
    function chooseRole() {

        var roleId = jQuery("#type option:selected").val();

        //非省、市、区管理员不显示区域
        if (roleId == 6 || roleId == 2 || roleId == 7) {
            $("#district").show();
        }
        else {
            $("#district").hide();
        }

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