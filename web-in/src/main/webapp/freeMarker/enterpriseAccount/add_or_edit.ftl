<!DOCTYPE html>

<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>

<head>
    <title>
        新建企业账户
    </title>
    <meta charset="UTF-8">
</head>

<body>
<div class="page-header">
    <h1>新建企业账户
        <button class="btn btn-white pull-right"
                onclick="javascript:window.location.href='${contextPath}/manage/enterpriseAccount/index.html'">返回
        </button>
    </h1>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <form class="form-horizontal" action="${contextPath}/manage/enterpriseAccount/addOrEditSubmit.html"
              method="post"
              name="globalConfigForm" id="table_validate" method="post">
            <div class="form-group">
                <label class="col-sm-3 control-label">企业名称 </label>
                <div class="col-sm-9">
                <#if enId??>
                    <input type="hidden" name="entId" value="${enId}"/>
                    <input type="text" name="eName" value="${enterpriseAccount.eName!}" readonly="true"
                           class='hasPrompt'/>
                <#else>
                    <select name="entId" id="entId" class="select-role">
                        <#list list as en>
                            <option value=${en.entId } <#if enId?? && enId==en.entId>
                                    selected="selected"
                            </#if> >${en.eName}</option>
                        </#list>
                    </select>
                </#if>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">剩余流量 </label>
                <div class="col-sm-9">
                    <input type="text" name="volume" id="volume" value="${enterpriseAccount.volume!}"
                           class='hasPrompt'/>
                    <br/><span class="prompt">剩余流量只能输入数字,长度不能超过10位!</span>
                </div>
            </div>

            <div class="hr hr-24"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>
                <div class="col-sm-9">
                    <input type="submit" class="btn btn-primary" value="保存"/>&nbsp;&nbsp;
                    <a class="btn btn-success" href="${contextPath}/manage/enterpriseAccount/index.html">返回列表</a>&nbsp;&nbsp;
                    &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
                </div>
            </div>

            <script>
                $("#table_validate").validate({
                    rules: {
                        volume: {
                            required: true,
                            maxlength: 10,
                            noSpecial: true,
                            digits: true
                        }
                    },
                    messages: {}
                });

                $(function () {
                    $('#backid').click(function () {
                        window.location.href = "<%=request.getContextPath()%>/manage/globalConfig/index.html";
                    });

                    String.prototype.trim = function () {
                        return this.replace(/(^\s*)|(\s*$)/g, '');
                    }
                });
            </script>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div><!-- /.col -->
</div>
</body>
</html>

