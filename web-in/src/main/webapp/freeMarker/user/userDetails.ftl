<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>

<head>
    <title>
    </title>

    <meta charset="UTF-8">

</head>

<body>

<form name="adminForm"
      action="http://localhost:8080${contextPath}/manage/user/update.html"
      method="post" id="table_validate">

    <table class="table table-bordered table-hover definewidth m10">

        <tr>
            <td width="10%" class="tableleft">用户角色</td>
            <td>${role.name}</td>
        </tr>
        <tr id="enterpriseId_tr">
            <td width="10%" class="tableleft">企业名称</td>
            <td>${adminenter}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">姓名</td>
            <td> ${administer.userName}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">手机号码</td>
            <td>${administer.mobilePhone}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">邮箱</td>
            <td>${administer.email}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">创建时间</td>
            <td>${administer.createTime?datetime}</td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">修改时间</td>
            <td> ${administer.updateTime?datetime}</td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" name="backid"
                        id="backid">修改个人信息
                </button>
                <button type="button" class="btn btn-success" name="xgmm"
                        id="xgmm">修改密码
                </button>
            </td>
        </tr>
    </table>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
<SCRIPT LANGUAGE="JavaScript">


    $(
            function () {
                $('#backid').click(function () {
                    window.location.href = "${contextPath}/manage/userInfo/edit.html";
                });

                $('#xgmm').click(function () {
                    window.location.href = "${contextPath}/manage/userInfo/updatePassword.html";
                });

                String.prototype.trim = function () {
                    return this.replace(/(^\s*)|(\s*$)/g, '');
                }
            });
</script>

</body>
</html>

