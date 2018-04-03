<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>
    <title>新建红包模板</title>
    <meta charset="UTF-8">
</head>

<body>
<div class="page-header">
    <h1>新建红包模板<a class="btn btn-white pull-right" href="${contextPath}/manage/rule_template/index.html">返回</a></h1>
</div>
<div class="row">
    <div class="col-xs-12">

        <form id="createForm" class="form-horizontal" role="form" action="${contextPath}/manage/rule_template/save.html"
              method="POST">
            <input type="hidden" name="ruleType" id="ruleType" value="RP"/>

            <div class="form-group">
                <label class="col-sm-3 control-label" for="name">模板名称</label>
                <div class="col-sm-9">
                    <input type="text" name="name" id="name" value="${name!}"/>
                    &nbsp;&nbsp;<span style="color:red" id="tip_name"></span>
                    </br><span class="help-block">模板名称由汉字、英文字母及数字组成，长度不超过64个字符</span>
                </div>
            </div>

            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label" for="title">活动主题</label>
                <div class="col-sm-9">
                    <input type="text" name="title" id="title" value="${title!}"/>
                    &nbsp;&nbsp;<span style="color:red" id="tip_title"></span>
                    <span class="help-block">活动主题不能超过45个字符</span>
                </div>
            </div>

            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label" for="description">红包描述</label>
                <div class="col-sm-9">
                    <textarea rows="10" cols="100" maxlength="500" id="description" name="description"
                              class="hasPrompt">${description!}</textarea>
                    &nbsp;&nbsp;<span style="color:red" id="tip_description"></span>
                    <span class="help-block">活动描述长度不超过500个字符,需要换行的字符字符之间用"|"间隔。</span>
                </div>
            </div>

            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label" for="people">活动对象</label>
                <div class="col-sm-9">
                    <textarea rows="10" cols="100" maxlength="500" id="people" name="people"
                              class="hasPrompt">${people!}</textarea>
                    &nbsp;&nbsp;<span style="color:red" id="tip_people"></span>
                    <span class="help-block">活动对象描述长度不超过45个字符。</span>
                </div>
            </div>

            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label" for="activityDes">活动说明</label>
                <div class="col-sm-9">
                    <textarea rows="10" cols="100" maxlength="500" id="activityDes" name="activityDes"
                              class="hasPrompt">${activityDes!}</textarea>
                    &nbsp;&nbsp;<span style="color:red" id="tip_activityDes"></span>
                    <span class="help-block">活动说明长度不超过140个字符,需要换行的字符字符之间用"|"间隔。</span>
                </div>
            </div>

            <div class="hr hr-24"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>
                <div class="col-sm-9">
                    <button type="button" class="btn btn-info" onclick="doSubmit()">保存</button>
                    <span style="color: red;" id="error_msg">${errorMessage!}</span>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>
</div>
<script type="text/javascript">

    $("#createForm").packValidate({
        rules: {
            name: {
                required: true,
                maxlength: 64,
                searchBox: true,
                remote: {
                    url: "checkName.html?id=-1",
                    data: {
                        configName: function () {
                            return $('#name').val()
                        }
                    }
                }
            },
            title: {

                maxlength: 45
            },
            description: {

                maxlength: 500
            },
            people: {

                maxlength: 45
            },
            activityDes: {

                maxlength: 140
            }
        },
        messages: {
            name: {
                maxlength: "红包模板名称不能超过64个字符!",
                remote: "模板名称已经存在!"
            },
            title: {

                maxlength: "红包主题不能超过45个字符!"
            },
            description: {

                maxlength: "红包描述不能超过500个字符!"
            },
            people: {

                maxlength: "活动对象描述不能超过45个字符!"
            },
            activityDes: {

                maxlength: "活动说明不能超过140个字符!"
            }
        }
    });

    function doSubmit() {

        $("#tip_name").empty();
        $("#tip_title").empty();
        $("#tip_description").empty();
        $("#tip_people").empty();
        $("#tip_activityDes").empty();

        if ($("#name").val() == '') {
            $("#tip_name").append('红包模板名称不能为空!');
            return false;
        }

        if ($("#title").val() == '') {

            $("#tip_title").append('红包主题不能为空!');
            return false;
        }

        if ($("#description").val() == '') {
            $("#tip_description").append('红包描述不能不能为空!');
            return false;
        }

        if ($("#people").val() == '') {
            $("#tip_people").append('活动对象描述不能为空!');
            return false;
        }

        if ($("#activityDes").val() == '') {
            $("#tip_activityDes").append('活动说明不能为空!');
            return false;
        }
        $('#createForm').submit();


    }


</script>
</body>
</html>