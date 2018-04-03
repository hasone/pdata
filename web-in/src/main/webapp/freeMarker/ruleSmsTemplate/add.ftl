<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>

<head>
    <title>新增短信模板</title>

    <meta charset="UTF-8">

</head>

<body>
<div class="page-header">
    <h1>
        新增短信模板
        <button class="btn btn-white pull-right"
                onclick="javascript:window.location.href='${rc.contextPath}/manage/rule_sms_template/index.html'">返回
        </button>
    </h1>
</div><!-- /.page-header -->


<div class="row">
    <div class="col-xs-12">
        <form
                id="table_validate"
                action="${rc.contextPath}/manage/rule_sms_template/save.html"
                method="post">
            <table class="table no-border table-hover table-left">
                <tr>
                    <td class="tableleft" width="15%">短信模板名称(<span class="red">*</span>)</td>
                    <td>
                    <input type="text"
                           name="name" id="name"
                            value=<#if (ruleSmsTemplate.name)??>"${ruleSmsTemplate.name}"<#else>""</#if>

                        class="hasPrompt" />

                        <br/><span class="prompt">短信模板名称由汉字、英文字符及数字组成，长度不超过30个字符</span>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" width="15%">活动类型(<span class="red">*</span>)</td>
                    <td>
                        <select name="type" id="type" onchange="changeContent()">

                            <option value="RP">红包短信模板</option>
                            <option value="MG">包月赠送短信模板</option>
                            <option value="CG">普通赠送短信模板</option>

                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" width="10%">短信模板内容(<span class="red">*</span>)</td>
                    <td>
				    <textarea rows="10" cols="100" id="content" name="content" class="hasPrompt">
                    <#if (ruleSmsTemplate.content)??>${ruleSmsTemplate.content}</#if>
				    </textarea>
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
                    <input type="hidden" name="deleteFlag" value="0"/>
                    <button type="button" class="btn btn-info" type="button" id="btn_sub" onclick="doSubmit()">保存
                    </button>
                    &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>
<#--
<div class="col-xs-12">
  <form id="table_test" action="${rc.contextPath}/manage/rule_sms_template/test.html" method="post">
      <table class="table no-border table-hover table-left">
          <tr>
              <td class="tableleft" width="10%">短信模板示例演示</td>
              <td>
                  <textarea rows="10" cols="100" id="content_test" name="content_test" class="hasPrompt"></textarea>
                  <br/><span class="prompt">例如:您好，感谢您参与{1}发起的{2}活动,恭喜您于{4}抽到{7}流量包!之后还将陆续推出更多精彩活动!敬请期待!{1}恭喜{4}收到{6}的{7}流量包!</span>
              </td>
          </tr>
          <tr>
              <td class="tableleft" width="10%">示例显示</td>
              <td>
                  <span>${test!}</span>
              </td>
          </tr>
      </table>
       <div class="form-group">
          <label class="col-sm-3 control-label" ></label>
          <div class="col-sm-9">
                  <input type="hidden" name="deleteFlag" value="0" />
                  <button type="submit" class="btn btn-info" id="btn_test">示例演示</button>
          </div>
       </div>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</div>
-->

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
                required: "短信模板名称不能为空!",
                maxlength: "短信模板名称不能超过30个字符"
            },
            content: {
                required: "短信模板内容不能为空!"
            }
        }
    });
    /* hasPrompt(); */

    function doSubmit() {
        if ($("#table_validate").packValidate().form()) {
            $('#table_validate').submit();
        }
    }

    $(
            function () {
                $('#backid').click(function () {
                    window.location.href = "${contextPath}/manage/rule_sms_template/index.html";
                });


                String.prototype.trim = function () {
                    return this.replace(/(^\s*)|(\s*$)/g, '');
                }
            });


    //选择红包模板
    function changeContent() {
        var index = jQuery("#type option:selected").val();

        if (index == 'RP') {
            $("#fields").empty();
            $("#fields").html("2.{0}表示时间;{1}表示企业名称;{2}表示活动主题;{3}表示流量包大小;");

            $("#num").empty();
            $("#num").html("3.上述四个字段依次循环出现三次，即可用的占位符有，时间:{0}{4}{8},企业名称:{1}{5}{9},活动主题:{2}{6}{10},流量包大小:{3}{7}{11};");

            $("#example").empty();
            $("#example").html("4.示例:'您好,恭喜您于{0}抢到{3}流量红包!感谢您参加{5}举办的{10}!',解析后即为：'您好,恭喜您于2015-12-25 12:14:30抢到30MB流量红包!感谢您参加中国移动公司举办的圣诞嘉年华活动!'");
        }
        else if (index == 'MG') {
            $("#fields").empty();
            $("#fields").html("2.{0}表示时间;{1}表示企业名称;{2}表示赠送开始时间;{3}表示赠送结束时间;{4}表示赠送总月数;{5}表示流量包大小;");

            $("#num").empty();
            $("#num").html("3.上述六个字段依次循环出现三次,即可用的占位符有,时间:{0}{6}{12},企业名称:{1}{7}{13},赠送开始时间:{2}{8}{14},赠送结束时间:{3}{9}{15},赠送总月数:{4}{10}{16},流量包大小:{5}{11}{17};");

            $("#example").empty();
            $("#example").html("4.示例:'您好,您于{0}向{1}订购了{4}个月{5}流量包,首次充值时间为{8},结束时间为{9}!',解析后即为：'您好,您于2015-11-11 12:14:30向中国移动公司订购了3个月30MB流量包,首次充值时间为2015-11-13,结束时间为2015-12-01!'");

        }
        else {
            $("#fields").empty();
            $("#fields").html("2.{0}表示时间;{1}表示企业名称;{2}表示流量包大小;");

            $("#num").empty();
            $("#num").html("3.上述三个字段依次循环出现三次,即可用的占位符有，时间:{0}{3}{6},企业名称:{1}{4}{7},流量包大小:{2}{5}{8};");

            $("#example").empty();
            $("#example").html("4.示例:'您好,您于{0}向{1}订购了{2}流量包!',解析后即为：'您好,恭喜您于2015-12-25 12:14:30向中国移动订购了30MB流量包!'");
        }
    }

</script>
</body>
</html>




