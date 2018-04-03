<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>

<head>
    <title>编辑用户</title>
    <meta charset="UTF-8">
</head>

<body>
<div class="page-header">
    <h1>编辑用户
        <button class="btn btn-white pull-right"
                onclick="javascript:window.location.href='${contextPath}/manage/user/index.html'">返回
        </button>
    </h1>
</div>

<div class="row">
    <div class="col-xs-12">
        <form class="form-horizontal" action="${contextPath}/manage/user/save.html"
              method="post" name="adminForm" id="table_validate">
            <input type="hidden" name="id" id="id" value="${administer.id!}"/>
            <div class="form-group">
                <label class="col-sm-3 control-label">用户角色 </label>
                <div class="col-sm-9" style="margin-top: 4px;">
                    <input type="hidden" id="roleId" name="roleId" value="${administer.roleId!}"/>
                    <span style="font-size:14px;">${role.name!}</span>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">
                <#if role.code?? && role.code == "5">
                    制卡商名称
                <#else>
                    企业名称
                </#if>
                </label>
                <div class="col-sm-9">
                    <select name="enterpriseId" id="enterpriseId">
                    <#-- 用户ID存在, 但没有关联企业时，才显示请选择 -->
                    <#if role.code?? && role.code == "3">
                        <#if administer.id?? && !(currentEnterpriseId??) >
                            <option value="">请选择</option></#if>
                        <#list enterprises as enterprise>
                            <option value="${enterprise.id }"
                                <#if currentEnterpriseId?? && currentEnterpriseId==enterprise.id>
                                    selected="selected"
                                </#if>
                            >${enterprise.name }</option>
                        </#list>
                    </#if>
                    <#if role.code?? && role.code == "5">
                        <#if administer.id?? && !(currentCardMakerId??)>
                            <option value="">请选择</option></#if>
                        <#list cardmarkers as cardmarker>
                            <option value="${cardmarker.id }"
                                <#if currentCardMakerId?? && currentCardMakerId==cardmarker.id>
                                    selected="selected"
                                </#if>
                            >${cardmarker.name }</option>
                        </#list>
                    </#if>
                    </select>
                </div>
            </div>


            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">姓名 </label>
                <div class="col-sm-9">
                    <input type="text" id="userName" name="userName" value="${(administer.userName)!}"
                           class='hasPrompt'/>
                    <br/><span class="prompt">姓名由汉字、英文字符及数字组成，长度不超过64个字符</span>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">密码</label>
                <div class="col-sm-9">
                    <input type="password" name="password" id="password"
                           value="<#if administer.id?exists>xxxx000__x</#if>"
                           class="hasPrompt" onfocus="this.value='';$('#isPassChanged').val('true')"/>
                    <br/><span class="prompt">密码必须包含字母、数字、特殊符号且长度为10到20位</span>
                    <input type="hidden" name="isPassChanged" id="isPassChanged" value="false"/>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">手机号码 </label>
                <div class="col-sm-9">
                    <input type="text" name="mobilePhone" id="mobilePhone" value="${(administer.mobilePhone)!}"
                           class="hasPrompt"/>
                    <br/><span class="prompt">手机号码由数字组成，长度必须为11位</span>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">邮箱 </label>
                <div class="col-sm-9">
                    <input type="text" name="email" id="email" value="${(administer.email)!}" class="hasPrompt"/>
                    <br/><span class="prompt">邮箱地址由英文字符、数字及下划线组成，必须为合法的邮箱地址</span>
                </div>
            </div>

            <div class="hr hr-24"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>
                <div class="col-sm-9">
                    <button type="submit" class="btn btn-info" type="button">保存</button>
                    &nbsp;&nbsp;&nbsp;
                    <span style="color:red" id="error_msg">${errorMsg!}</span>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div><!-- /.col -->
</div>

<script type="text/javascript">
    //判断手机号是否重复
    $(document).ready(function () {

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
    });

    $("#table_validate").packValidate({
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

</script>

</body>
</html>

