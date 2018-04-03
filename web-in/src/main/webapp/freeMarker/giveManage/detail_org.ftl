<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <script type="text/javascript">

        function doback() {
            window.location.href = "${contextPath}/manage/giveRuleManager/index.html"
        }

        function choosePhones(ruleId) {
            window.location.href = "${contextPath}/manage/giveRecordManager/giveResult.html?ruleId=" + ruleId;
        }
    </script>
    <title>普通赠送详情</title>
</head>

<body>
<div class="page-header">
    <h1>普通赠送详情
        <button type="button" class="btn btn-white pull-right" onclick="doback()">返回</button>

    </h1>
</div>


<input type="hidden" name="id" value="${record.id}"/>

<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->

        <form class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-3 control-label">企业名称 </label>

                <div class="col-sm-9">
                    <label>${record.eName}</label>
                </div>
            </div>

            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"> 产品名称 </label>
                <div class="col-sm-9">
                    <label>${record.pName}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 被赠送人 </label>
                <div class="col-sm-9">
                    <label>已有<span id="phone_count">${total!0}</span>个号码</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 被赠送人 </label>
                <div class="col-sm-9">
                    <label>
                    <#if record.useSms == 1>
                        启用短信通知功能
                    <#else>
                        禁用短信通知功能
                    </#if>
                    </label>
                </div>
            </div>


            <div class="hr hr-24"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"></label>

                <div class="col-sm-9">
                    <button type="button" class="btn btn-info" data-toggle="modal" data-target="#my-modal"
                            onclick="choosePhones(${record.id})">查看被赠送记录
                    </button>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div><!-- /.row -->


</body>
</html>