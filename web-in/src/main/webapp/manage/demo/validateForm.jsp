<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>表单验证</title>
</head>
<body>

<form id="table_validate" action="#" method="post">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">非空</td>
            <td><input type="text" name="required"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">姓名</td>
            <td><input type="text" name="name"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">Email</td>
            <td><input type="text" name="email"/></td>
        </tr>
        <tr>
            <td class="tableleft">URL</td>
            <td><input type="text" name="url"/></td>
        </tr>
        <tr>
            <td class="tableleft">手机</td>
            <td><input type="text" name="mobile"/></td>
        </tr>
        <tr>
            <td class="tableleft">学历</td>
            <td><select name="education">
                <option value="">－－请选择你的学历－－</option>
                <option value="a">专科</option>
                <option value="b">本科</option>
                <option value="c">研究生</option>
                <option value="e">硕士</option>
                <option value="d">博士</option>
            </select></td>
        </tr>
        <tr>
            <td>密码</td>
            <td><input type="password" name="pwd" id="userPwd"/></td>
        </tr>
        <tr>
            <td>确认密码</td>
            <td><input type="password" name="verifyPwd"/></td>
        </tr>
        <tr>
            <td class="tableleft">状态</td>
            <td>
                <input type="radio" name="status" value="1" checked/> 启用
                <input type="radio" name="status" value="0"/> 禁用
            </td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="submit" class="btn btn-primary" type="button">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid">返回列表</button>
            </td>
        </tr>
    </table>
</form>


<form id="table_col2_validate" class="form-horizontal" action="#" method="post">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">非空</td>
            <td><input id="required-1" type="text" name="required"/></td>
            <td width="10%" class="tableleft">姓名</td>
            <td><input type="text" name="name"/></td>
        </tr>
        <tr>
            <td width="10%" class="tableleft">Email</td>
            <td><input id="email1" type="text" name="email"/></td>
            <td class="tableleft">URL</td>
            <td><input type="text" name="url"/></td>
        </tr>
        <tr>
            <td class="tableleft">手机</td>
            <td><input type="text" name="mobile"/></td>
            <td class="tableleft">学历</td>
            <td><select name="education">
                <option value="">－－请选择你的学历－－</option>
                <option value="a">专科</option>
                <option value="b">本科</option>
                <option value="c">研究生</option>
                <option value="e">硕士</option>
                <option value="d">博士</option>
            </select></td>
        </tr>
        <tr>
            <td class="tableleft">状态</td>
            <td colspan="3">
                <input type="radio" name="status" value="1" checked/> 启用
                <input type="radio" name="status" value="0"/> 禁用
            </td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td colspan="3">
                <button type="submit" class="btn btn-primary" type="button">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid">返回列表</button>
            </td>
        </tr>
    </table>
</form>

<script type="text/javascript">

    $("#table_validate").packValidate({
        rules: {
            required: {
                required: true
                /*remote:{
                 url:"remote.php",
                 type:"get",
                 dataType:"text"
                 }*/
            },
            name: {
                required: true,
                remote: "remote.php"
            },
            email: {
                required: true,
                email: true,
                remote: "remote.php"
            },
            url: {
                required: true,
                url: true
            },
            mobile: {
                required: true,
                mobile: true
            },
            education: {
                required: true
            },
            verifyPwd: {
                verifyPwd: '#userPwd'
            }
        },

        messages: {
            name: {
                remote: "姓名已经存在!"
            },
            email: {
                remote: "邮箱已经存在!"
            }
        },

        errorClass: "help-error",
        errorElement: "span",
        highlight: function (element, errorClass, validClass) {
            $(element).addClass('error');
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass('error');
            $(element).addClass('success');
        }
    });

    $("#table_col2_validate").packValidate({
        rules: {
            required: {
                required: true,
                noSpecial: true
                /*remote:{
                 url:"remote.php",
                 type:"get",
                 dataType:"text"
                 }*/
            },
            name: {
                required: true/*,
                 remote: "remote.php"*/
            },
            email: {
                required: true,
                //email: true,
                remote: "remote.php"
            },
            url: {
                required: true,
                url: true
            },
            mobile: {
                required: true,
                mobile: true
            },
            education: {
                required: true
            }
        },
        onsubmit: true,
        messages: {
            name: {
                remote: "姓名已经存在!"
            },
            email: {
                remote: "邮箱已经存在!"
            }
        }

        /*errorClass: "help-error",
         errorElement: "span",
         highlight:function(element, errorClass, validClass) {
         $(element).addClass('error');
         },
         unhighlight: function(element, errorClass, validClass) {
         $(element).removeClass('error');
         $(element).addClass('success');
         }*/
    });

</script>
</body>
</html>