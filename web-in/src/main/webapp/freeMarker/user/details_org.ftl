<#assign contextPath = rc.contextPath >

<!DOCTYPE html>
<html>

<head>
    <title>个人信息
    </title>

    <meta charset="UTF-8">

</head>
<style type="text/css">
    .tdLeft {
        text-align: left;
    }
</style>
<body>
<div class="page-header">
    <h1>个人信息 </h1>
</div>
<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->

        <form class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-3 control-label"> 用户角色 </label>

                <div class="col-sm-9">
                    <label>${(roleName)!}</label>
                </div>
            </div>

            <div class="space-4"></div>

        <#if enterpriseName?exists>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 企业名称 </label>
                <div class="col-sm-9">
                    <label>${enterpriseName!}</label>
                </div>
            </div>
        </#if>
            <div class="space-4"></div>

            <div class="form-group">
                <label class="col-sm-3 control-label"> 姓名 </label>
                <div class="col-sm-9">
                    <label>${administer.userName}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 手机号码 </label>
                <div class="col-sm-9">
                    <label>${administer.mobilePhone}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 邮箱 </label>
                <div class="col-sm-9">
                    <label>${administer.email} </label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 创建时间 </label>
                <div class="col-sm-9">
                    <label>${administer.createTime?datetime}</label>
                </div>
            </div>

            <div class="space-4"></div>
            <div class="form-group">
                <label class="col-sm-3 control-label"> 修改时间 </label>
                <div class="col-sm-9">
                    <label>${administer.updateTime?datetime}</label>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div><!-- /.row -->


<SCRIPT LANGUAGE="JavaScript">


    $(
            function () {
                String.prototype.trim = function () {
                    return this.replace(/(^\s*)|(\s*$)/g, '');
                }
            });
</script>

</body>
</html>

