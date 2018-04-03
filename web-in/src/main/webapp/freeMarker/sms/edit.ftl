<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>

<head>
    <title>编辑短信模板</title>

    <meta charset="UTF-8">

</head>

<body>
<div class="page-header">
    <h1>
        编辑短信模板
        <button class="btn btn-white pull-right"
                onclick="javascript:window.location.href='${rc.contextPath}/manage/sms/index.html'">返回
        </button>
    </h1>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <form
                id="table_validate"
                action="${rc.contextPath}/manage/sms/update.html"
                method="post">
            <input type="hidden" name="id" value="${smsTemplate.id}"/>
            <table class="table no-border table-hover table-left">
                <tr>
                    <td class="tableleft" width="15%">短信模板名称(<span class="red">*</span>)</td>
                    <td>
                        <input type="text"
                               name="name" maxlength="60"
                               value="${smsTemplate.name}" class="hasPrompt"/>
                        <br/><span class="prompt">短信模板名称由汉字、英文字符及数字组成，长度不超过64个字符</span>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" width="10%">短信模板内容(<span class="red">*</span>)</td>
                    <td>
                        <textarea rows="10" cols="100" maxlength="500" id="content" name="content"
                                  class="hasPrompt">${smsTemplate.content}</textarea>
                        <br/><span class="prompt">1.短信模板内容长度不超过400个字符</span>
                        <br/><span class="prompt">2.短信模板内容中必须包含至少一个<c:out value="\"></c:out>标签,表示模板中通用的替换内容。number是从0开始的数字，每个标签数字按顺序加一，
					                                                                          例如...<c:out
                                value="\"></c:out>...<c:out value="\"></c:out>...<c:out value="\"></c:out></span>
                    </td>
                </tr>
            </table>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>
                <div class="col-sm-9">
                    <button type="submit" class="btn btn-info" type="button" id="btn_sub">保存</button>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>
</div>
<SCRIPT LANGUAGE="JavaScript">
    $("#table_validate").packValidate({
        rules: {
            name: {
                required: true,
                maxlength: 64,
                remote: "updateCheck.html?id=${smsTemplate.id}",
                searchBox: true
            },
            content: {
                required: true,
                maxlength: 400,
                remote: {
                    url: "checkSmsContent.html",
                    type: "get",
                    dataType: "text"
                }
            }
        },
        messages: {
            name: {
                remote: "姓名称已经存在!"
            },
            content: {
                remote: "格式不正确!"
            }
        }
    });
    /* hasPrompt(); */

    $(
            function () {
                $('#backid').click(function () {
                    window.location.href = "${contextPath}/manage/sms/index.html";
                });


                String.prototype.trim = function () {
                    return this.replace(/(^\s*)|(\s*$)/g, '');
                }
            });

</script>
</body>
</html>

