<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>

<head>
    <title>个人信息编辑
    </title>

    <meta charset="UTF-8">
    <style type="text/css">
        .tdLeft {
            text-align: left
        }
    </style>
</head>

<body>
<div class="page-header">
    <h1>个人信息编辑</h1>
</div>
<form name="adminForm"
      action="${contextPath}/manage/userInfo/updateCurrentUserInfo.html"
      method="post" id="table_validate">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">姓名(<span class="red">*</span>)</td>
            <td style="text-align:left"><input type="text" name="userName" id="userName"
                                               value="${(administer.userName)!}" class='hasPrompt'/>
                <br/><span class="prompt">姓名由汉字、英文字符及数字线组成，长度不超过64个字符</span>
            </td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">手机号码(<span class="red">*</span>)</td>
            <td style="text-align:left"><input type="text" name="mobilePhone" id="mobilePhone"
                                               value="${(administer.mobilePhone)!}" class="hasPrompt"/>
                <br/><span class="prompt">手机号码由数字组成，长度必须为11位</span>
            </td>
        </tr>

        <tr>
            <td width="10%" class="tableleft">邮箱(<span class="red">*</span>)</td>
            <td style="text-align:left"><input type="text" name="email" id="email" value="${(administer.email)!}"
                                               class="hasPrompt"/>
                <br/><span class="prompt">邮箱地址由英文字符、数字及下划线组成，必须为合法的邮箱地址</span>
            </td>
        </tr>

        <tr>
            <td class="tableleft"></td>
            <td style="text-align:left">
                <button type="submit" class="btn btn-info" type="button">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid"
                        id="backid">返回个人信息
                </button>
                &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
            </td>
        </tr>
    </table>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

<SCRIPT LANGUAGE="JavaScript">
    $("#table_validate").packValidate({
        rules: {
            userName: {
                required: true,
                maxlength: 64,
                searchBox: true,
                remote: {
                    url: 'check.html',
                    data: {
                        id: ${(administer.id)!},
                        userName: function () {
                            return $('#userName').val();
                        }
                    }
                }
            },
            mobilePhone: {
                required: true,
                mobile: true,

                remote: {
                    url: 'check.html',
                    data: {
                        id: ${(administer.id)!},
                        mobilePhone: function () {
                            return $('#mobilePhone').val();
                        }
                    }
                }
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
            mobilePhone: {
                remote: "电话号码已存在！"
            }
        }
    });


    $(
            function () {
                $('#backid').click(function () {
                    window.location.href = "${contextPath}/manage/userInfo/showCurrentUserDetails.html";
                });
                String.prototype.trim = function () {
                    return this.replace(/(^\s*)|(\s*$)/g, '');
                }
            });
</script>
</body>
</html>

