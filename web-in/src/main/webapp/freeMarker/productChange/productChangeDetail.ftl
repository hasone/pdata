<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品变更明细</title>
    <meta name="keywords" content="流量平台 产品变更明细"/>
    <meta name="description" content="流量平台 产品变更明细"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form {
            width: 900px;
        }

        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 4px 5px;
            width: 350px;
        }

        .form label {
            width: 300px;
            text-align: right;
        }

        .form select {
            text-align: right;
            margin-left: 10px;
        }

        .dropdown-menu > li > a {
            padding: 6px 20px;
        }

        .form-group span {
            word-break: break-all;
        }

        .btn-save {
            margin-left: 650px;
        }

    </style>
</head>
<body>
<div class="main-container">
    <!-- <input type="hidden" name="id" value="1" /> 暂时用1替代  -->

    <div class="module-header mt-30 mb-20">
        <h3>产品变更明细
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>
    
    <#if productTemplateFlag == 'true'>
    	<div class="">
    		<div>
    			<lable>企业现在关联的产品模板：</lable>&nbsp;&nbsp;<span>${nowProductTemplateName!}</span>
    		</div>
    		<div>
    			<lable>变更后关联的产品模板：</lable>&nbsp;&nbsp;<span>${newProductTemplateName!}</span>
    		</div>
    	</div>
    </#if>

    <div class="tile mt-30">

        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>产品名称</th>
                        <#if flag ==1>
                            <th>折扣</th>
                        </#if>
                            <th>价格（元）</th>
                            <th>操作类型</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if opinions??>
                            <#list opinions as opinion>
                            <tr>
                                <td>${opinion.prdName!}</td>
                                <#if flag ==1>
                                    <td>
                                        <#if opinion.discount??&&opinion.discount==100>
                                            无折扣
                                        </#if>
                                        <#if opinion.discount??&&opinion.discount!=100>
                                        ${(opinion.discount/10)?string('#.#')}折
                                        </#if>
                                    </td>
                                </#if>
                                <td>
                                    <#if opinion.price??>
												    ${(opinion.price/100)?string("0.00")}
												</#if>
                                </td>
                                <td>
                                    <#if opinion.operator??&&opinion.operator==0>
                                        删除
                                    </#if>
                                    <#if opinion.operator??&&opinion.operator==1>
                                        增加
                                    </#if>
                                    <#if opinion.operator??&&opinion.operator==2>
                                        折扣变更
                                    </#if>
                                </td>
                            </tr>
                            </#list>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="tile-content">
        <form action="${contextPath}/manage/enterProductChange/cancelProductChange.html" method="post"
              id="table_validate">
            <input type="hidden" name="enterId" id="enterId" value="${(enterId)!}">

            <div class="btn-save mt-30">
                <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="cancel-btn">撤销变更</a>&nbsp;&nbsp;&nbsp;&nbsp;
                <span style='color:red'>${errorMsg!}</span>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>

</div>

<div class="modal fade dialog-sm" id="tip-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">请确认是否撤销产品变更，一旦撤销不可复原！</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="ok">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>

    require(["moment", "common", "bootstrap", "daterangepicker"], function (mm, a, b, c) {
        //初始化
        init();
        window["moment"] = mm;
        goback();

    });

    /**
     * 初始化
     */
    function init() {

        //撤销产品变更
        $("#cancel-btn").on("click", function () {
            $("#tip-dialog").modal("show");
            $("#ok").on("click", function () {
                $("#table_validate").submit();
            });
        });
    }

    function goback() {
        if ($('#isModifyChanged').val() == 'true') {
            return confirm("是否确定不保存记录退出？");
        }
        else {
            return true;
        }
    }
    ;

</script>
</body>
</html>