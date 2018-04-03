<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>

<head>
    <title>
    </title>

    <meta charset="UTF-8">

</head>

<body>
<form
        action="${rc.contextPath}/manage/userInfo/edit.html"
        method="post" id="table_validate">
    <input type="hidden" name="id" value="${administer.id}"/>
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">用户角色</td>
            <td><select name="roleId" id="type" class="select-role">
            <#list list as role>
                <option value='${role.roleId }'>${role.name }</option>
            </#list>
            </select>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">企业名称</td>
            <td><select name="enterpriseId" id="enterpriseId">
                <option value="0">请选择</option>
            <#list liste as enterprise>
                <option value="${enterprise.id }">${enterprise.name }</option>
            </#list>
            </select></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">姓名(<span class="red">*</span>)</td>
            <td><input type="text" name="userName" value="${administer.userName }" class='hasPrompt'/>
                <br/><span class="prompt">姓名由汉字、英文字符及数字组成，长度不超过64个字符</span>
            </td>
        </tr>

        <!-- <tr>
            <td width="10%" class="tableleft">密码(<span class="red">*</span>)</td>
            <td> <input type="password" name="password" value=""  class="hasPrompt" />
                <br /><span class="prompt">密码由英文字符、数字及下划线组成，长度为6~16个字符</span>
            </td>
        </tr> -->

        <tr>
            <td width="10%" class="tableleft">手机号码(<span class="red">*</span>)</td>
            <td><input type="text" name="mobilePhone" id="mobilePhone" value="${administer.mobilePhone }"
                       class="hasPrompt"/>
                <br/><span class="prompt">手机号码由数字组成，长度必须为11位</span>
            </td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">邮箱(<span class="red">*</span>)</td>
            <td><input type="text" name="email" id="email" value="${administer.email }" class="hasPrompt"/>
                <br/><span class="prompt">邮箱地址由英文字符、数字及下划线组成，必须为合法的邮箱地址</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="submit" class="btn btn-primary" type="button">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid"
                        id="backid">返回个人信息
                </button>
                &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
            </td>
        </tr>
    </table>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

<#--
	<script>

$(function () {
	$('#backid').click(function(){
		window.location.href="${contextPath}/manage/userInfo/getAdminister.html";})
	});
</script>-->

<SCRIPT LANGUAGE="JavaScript">
    $("#table_validate").packValidate({
        rules: {

            password: {
                required: true,
                minlength: 6,
                maxlength: 16,
                format2: true
            },
            email: {
                required: true,
                email: true
            }
        },
        messages: {
            userName: {
                remote: "姓名称已经存在!"
            },
            password: {
                minlength: "密码长度最小为6位",
                maxlength: "密码长度最大为16位"
            },
            mobilePhone: {
                remote: "电话号码已存在！"
            }
        }
    });
    /* hasPrompt(); */

    $(function () {

        $('#type').change(function () {
            if ($('#type').val() != '${superAdminId}') { //选中企业关键人
                $('#enterpriseId').removeAttr("disabled");
                $('#enterpriseId_tr').show();
            } else {
                $("#enterpriseId").get(0).selectedIndex = 0;
                $('#enterpriseId').attr('disabled', "true");
                $('#enterpriseId_tr').hide();
            }
        });

        $('#backid').click(function () {
            window.location.href = "${contextPath}/manage/userInfo/getAdminister.html";
        });


        String.prototype.trim = function () {
            return this.replace(/(^\s*)|(\s*$)/g, '');
        }
    });
</script>
</body>
</html>

