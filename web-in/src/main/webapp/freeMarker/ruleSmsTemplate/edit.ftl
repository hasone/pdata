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
                onclick="javascript:window.location.href='${rc.contextPath}/manage/rule_sms_template/index.html?pageNum=${pageNum}'">
            返回
        </button>
    </h1>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <form
                id="table_validate"
                action="${rc.contextPath}/manage/rule_sms_template/update.html"
                method="post">
            <input type="hidden" name="id" value="${ruleSmsTemplate.id}"/>
            <table class="table no-border table-hover table-left">
                <tr>
                    <td class="tableleft" width="15%">短信模板名称(<span class="red">*</span>)</td>
                    <td>
                        <input type="text"
                               name="name" maxlength="60"
                               value="${ruleSmsTemplate.name}" class="hasPrompt"/>
                        <br/><span class="prompt">短信模板名称由汉字、英文字符及数字组成，长度不超过30个字符</span>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" width="15%">模板类型(<span class="red">*</span>)</td>
                    <td>
                        <span id="tamplateType">${ruleSmsTemplate.typeName}</span>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" width="10%">短信模板内容(<span class="red">*</span>)</td>
                    <td>
                        <textarea rows="10" cols="100" maxlength="500" id="content" name="content"
                                  class="hasPrompt">${ruleSmsTemplate.content}</textarea>
                        <div><span class="prompt">1.短信模板内容长度不超过400个字符</span></div>
                        <div><span
                                class="prompt">2.<span>$<span>{0}表示时间;<span>$<span>{1}表示企业名称;<span>$<span>{2}表示活动主题;<span>$<span>{3}流量包大小;</span>
                        </div>
                        <div><span
                                class="prompt">3.上述四个字段依次循环出现三次，即可用的占位符为表示时间的占位符为:<span>$<span>{0}、<span>$<span>{4}、<span>$<span>{8},
        					表示企业的占位符:<span>$<span>{1}、<span>$<span>{5}、<span>$<span>{9},表示活动主题的占位符:<span>$<span>{2}、<span>$<span>{6}、<span>$<span>{10},表示流量包大小的占位符:<span>$<span>{3}、<span>$<span>{7}、<span>$<span>{11};</span>
                        </div>
                        <div><span class="prompt" id="example">4.示例:'您好,恭喜您于<span>$<span>{0}抢到<span>$<span>{3}流量红包!感谢您参加<span>$<span>{5}举办的<span>$<span>{10}!',
        					解析后即为：'您好,恭喜您于2015-12-25 12:14:30抢到30MB流量红包!感谢您参加中国移动公司举办的圣诞嘉年华活动!'</span></div>
                        <div><span
                                class="prompt">5.模板可以选择使用或者不使用占位符;如果使用占位符模板中使用的占位符必须按从小到大出现,如<span>$<span>{0}<span>$<span>{6}<span>$<span>{11},<span>$<span>{0}<span>$<span>{1}<span>$<span>{2}<span>$<span>{3};但不能出现<span>$<span>{0}<span>$<span>{11}<span>$<span>{2}等。</span>
                        </div>
                    </td>
                </tr>
            </table>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>
                <div class="col-sm-9">
                    <button type="submit" class="btn btn-info" id="btn_sub">保存</button>
                    &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
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
                maxlength: 30,
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
            }
        }
    });
    $(
            function () {
                $('#backid').click(function () {
                    window.location.href = "${contextPath}/manage/rule_sms_template/index.html?pageNum=${pageNum}";
                });


                String.prototype.trim = function () {
                    return this.replace(/(^\s*)|(\s*$)/g, '');
                }
            });

</script>
</body>
</html>

