<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <script type="text/javascript">

        function doback() {
            window.location.href = "${contextPath}/manage/month_rule/index.html"
        }

        function choosePhones(ruleId) {
            window.location.href = "${contextPath}/manage/month_record/pager.html?ruleId=" + ruleId;
        }
    </script>
    <title>包月赠送详情</title>
</head>

<body>
<div class="page-header">
    <h1>包月赠送详情
        <button type="button" class="btn btn-white pull-right" onclick="doback()">返回</button>

    </h1>
</div>


<input type="hidden" name="id" id="row_id" value="${record.id}"/>

<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->

        <form class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-3 control-label">企业名称 </label>

                <div class="col-sm-9">
                    <label>${record.entName}</label>
                </div>
            </div>

            <div class="space-4"></div>

        <#if enterpriseName?exists>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 企业编码 </label>
                <div class="col-sm-9">
                    <label>${record.entCode}</label>
                </div>
            </div>
        </#if>
            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"> 产品名称 </label>
                <div class="col-sm-9">
                    <label>${record.prdName}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 被赠送人 </label>
                <div class="col-sm-9">
                    <label>已有<span id="phone_count">${record.total!0}</span>个号码</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 首次赠送日期 </label>
                <div class="col-sm-9">
                    <label>${record.startTime?datetime }</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 赠送总月数 </label>
                <div class="col-sm-9">
                    <label>${giveMonthsCnt!}/${numOfMonthMap[record.monthCount?c]}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 末次赠送日期 </label>
                <div class="col-sm-9">
                    <label>${record.endTime?datetime}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 短信功能</label>
                <div class="col-sm-9">
                    <label>
                    <#if record.useSms?? && record.useSms == 1>
                        启用短信通知功能
                    <#else>
                        禁用短信通知功能
                    </#if>
                    </label>
                </div>
            </div>

            <div class="hr hr-24"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label">
                    <button type="button" class="btn btn-info" data-toggle="modal" data-target="#my-modal"
                            onclick="choosePhones(${record.id})">查看被赠送记录
                    </button>
                </label>

                <label class="col-sm-9">

                    &nbsp;&nbsp;
                    <button type="button" class="btn btn-info" <#if record.status == 3> disabled </#if>
                            onclick="return Cancel();">取消包月赠送
                    </button>
                </label>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div><!-- /.row -->

<script>
    function Cancel() {
        if (window.confirm("是否取消包月赠送？")) {
            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: "${contextPath}/manage/month_rule/cancel.html",
                data: {id: $("#row_id").val()},
                type: "post",
                dataType: "json"
            }).success(function (ret) {
                if (ret.success) {
                    alert(ret.success);
                    window.location.href = "${contextPath}/manage/month_rule/index.html";
                } else {
                    alert(ret.fail);
                }
            });
        }
        return false;
    }
</script>


</body>
</html>