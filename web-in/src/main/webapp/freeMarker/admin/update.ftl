<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-编辑用户</title>
    <meta name="keywords" content="流量平台 编辑用户"/>
    <meta name="description" content="流量平台 编辑用户"/>

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
    <form class="form-horizontal" action="${contextPath}/manage/user/save.html" method="post" name="adminForm"
          id="table_validate">
        <input type="hidden" name="id" id="id" value="${administer.id!}"/>

        <div class="module-header mt-30 mb-20">
            <h3>用户管理
                <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
            </h3>
        </div>


        <div class="tile mt-30">
            <div class="tile-header">
                编辑用户
            </div>

            <div class="tile-content">
                <div class="row form">
                    <div class="col-md-6 col-md-offset-3">
                        <div class="form-group">
                            <label>用户角色：</label>
                            <input type="hidden" id="roleId" name="roleId" value="${administer.roleId!}"/>
                            <span style="font-size:14px;">${role.name!}</span>
                        </div>

                        <div class="form-group">
                        <#if role.code?? && role.code == "5">
                            <label>制卡商名称：</label>
                        <#else>
                            <label>企业名称：</label>
                        </#if>
                            <span style="font-size:14px;">${currentEnterpriseName!}</span>

                            <!--<select name="enterpriseId" id="enterpriseId" >-->
                        <#-- 用户ID存在, 但没有关联企业时，才显示请选择 -->
                            <!--    <#if role.code?? && role.code == "3">
	                            	<#if administer.id?? && !(currentEnterpriseId??) ><option  value="">请选择</option></#if>
								    <#list enterprises as enterprise>
									    <option value="${enterprise.id }"
										    <#if currentEnterpriseId?? && currentEnterpriseId==enterprise.id>
											    selected="selected"
										    </#if>
									    >${enterprise.name }</option>
								    </#list>
								</#if>
								<#if role.code?? && role.code == "5">
									<#if administer.id?? && !(currentCardMakerId??)><option  value="">请选择</option></#if>
									<#list cardmarkers as cardmarker>
										<option value="${cardmarker.id }"
										<#if currentCardMakerId?? && currentCardMakerId==cardmarker.id>	
											selected="selected"
										</#if>
										>${cardmarker.name }</option>
									</#list>
								</#if>
					       	</select>-->
                        </div>

                        <div class="form-group">
                            <label>姓名：</label>
                            <input type="text" name="userName" id="userName" maxlength="64" required
                                   data-bind="value: userName, valueUpdate: 'afterkeydown'"
                                   value="${(administer.userName)!}" class='hasPrompt'>
                            <p class="prompt">姓名由汉字、英文字符及数字组成，长度不超过64个字符.</p>
                        </div>

                        <div class="form-group">
                            <label>密码：</label>

                            <input type="password" name="password" id="password"
                                   value="<#if administer.id?exists>xxxx000__x</#if>"
                                   class="hasPrompt" onfocus="this.value='';$('#isPassChanged').val('true')"/>
                            <p class="prompt">密码必须包含字母、数字、特殊符号且长度为10到20位</p>
                            <input type="hidden" name="isPassChanged" id="isPassChanged" value="false"/>

                        </div>
                        <div class="form-group">
                            <label>手机号码：</label>
                            <input type="text" name="mobilePhone" id="mobilePhone" value="${(administer.mobilePhone)!}"
                                   required data-rule-number="true" class="hasPrompt"/>
                        </div>
                        <div class="form-group">
                            <label>邮箱：</label>
                            <input type="text" name="email" id="email" value="${(administer.email)!}"
                                   class="hasPrompt"/>
                        </div>
                    </div>
                </div>

            </div>
        </div>


        <div class="mt-30 text-center">
            <button type="submit" class="btn btn-warning btn-sm mr-10 dialog-btn" style="width: 114px;" type="button">
                保存
            </button>
            &nbsp;&nbsp;&nbsp;
            <span style="color:red" id="error_msg">${errorMsg!}</span>
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
</script>

<script type="text/javascript">
    //判断手机号是否重复
    function init() {

        $("#mobilePhone").blur(function () {
            var mobilePhone = $("#mobilePhone").val();
            var configId = ${administer.id!};
            if (mobilePhone == "" || configId == "") {
                return false;
            }
            //ajax
            $("#tip_mobilePhone").empty();
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                type: "POST",
                url: "${contextPath}/manage/user/checkPhoneAjax.html",
                data: {
                    configId: configId,
                    mobilePhone: mobilePhone
                },
                dataType: "json", //指定服务器的数据返回类型，
                success: function (res) {
                    if (res) {
                        if (res.inValid == "failure") {
                            $("#tip_mobilePhone").append("请正确输入手机号！");
                        }
                        else if (res.failure == "used") {
                            $("#tip_mobilePhone").append("该手机号已经被注册！");
                        }
                    }
                },
                error: function () {

                }
            })
        });
    }
    function checkFormValidate() {
        $("#table_validate").packValidate({
            errorElement: "span",
            rules: {
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
                    mobile: true
                },
                email: {
                    required: true,
                    email: true,
                    maxlength: 64
                }
            },
            messages: {
                userName: {
                    required: "姓名不能为空！",
                    maxlength: "姓名不能超过64个字符!"
                },
                password: {
                    required: "密码不能为空！",
                    minlength: "密码长度最小为10位！",
                    maxlength: "密码长度最大为20位！"
                },
                mobilePhone: {
                    required: "手机号不能为空！"
                },
                email: {
                    required: "电子邮箱不能为空！",
                    email: "请输入正确格式的电子邮件!"

                }
            }
        });
    }

</script>
</body>
</html>