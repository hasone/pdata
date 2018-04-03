<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />

<html>

<head>
    <meta charset="utf-8"/>
    <title>中奖纪录</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/font-awesome.min.css"/>

<#-- artDialog -->
    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/Js/artDialog/skins/default.css">
    <script src="${contextPath}/manage2/Js/artDialog/artDialog.js"></script>
    <script src="${contextPath}/manage2/Js/artDialog/jquery.artDialog.js"></script>


    <!--[if IE 7]>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/font-awesome-ie7.min.css"/>
    <![endif]-->

    <!-- ace styles -->
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace-skins.min.css"/>

    <!--[if lte IE 8]>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/ace-ie.min.css"/>
    <![endif]-->

    <script src="${contextPath}/manage2/assets/js/ace-extra.min.js"></script>

    <!--[if lt IE 9]>
    <script src="${contextPath}/manage2/assets/js/html5shiv.js"></script>
    <script src="${contextPath}/manage2/assets/js/respond.min.js"></script>
    <![endif]-->


    <script type="text/javascript" src="${contextPath}/manage2/Js/jquery.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/assets/js/js.cookie.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap.js"></script>

    <script type="text/javascript">
        if ("ontouchend" in document) document.write("<script src='${contextPath}/manage2/assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
    </script>
    <script src="${contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <script src="${contextPath}/manage2/assets/js/typeahead-bs2.min.js"></script>

    <!-- page specific plugin scripts -->

    <!-- ace scripts -->
<#--  日期组件 -->
    <link rel="stylesheet" type="text/css" href="${contextPath}/manage2/assets/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="${contextPath}/manage2/assets/css/style.css"/>
    <script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/jquery.validate.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/Utility.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/common.js"></script>
    <script src="${contextPath}/manage2/assets/js/ace-elements.min.js"></script>
    <script src="${contextPath}/manage2/assets/js/ace.min.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/knockout-3.2.0.js"></script>
</head>


<body style="overflow:scroll;">
<div class="page-header">
    <h1>中奖纪录<a class="btn btn-white pull-right" href="${contextPath}/manage2/lotteryActivity/index.html">返回</a></h1>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">

        <!-- PAGE CONTENT BEGINS -->
        <div class="table-search clearfix">
            <form class="form-inline definewidth m20" id="table_validate"
                  action="${contextPath}/manage/lotteryActivity/winRecord.html" method="POST">

                <div class="col-sm-2">
                    <div class="form-group">
                        <input type="hidden" name="id" id="id" value='${id!}'>
                    </div>
                </div>
                <div class="col-sm-10 form-inline dataTables_filter">
                    <div class="form-group">
                        <label for="name">手机号码：</label>
                        <input type="text" name="mobile" id="mobile" autocomplete="off" placeholder=""
                               value="${(pageResult.queryObject.queryCriterias.mobile)!}" maxlength="255"/>
                    </div>
                    &nbsp;&nbsp;
                    <div class="form-group">
                        <button class="btn btn-bg-white" type="submit">查询</button>
                    </div>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>

    <#-- tableBody -->
    <#assign fieldsConfig= [
    {
    "name": "活动名称",
    "template": r"${row.activityName!}"
    },
    {
    "name": "奖项",
    "template": r"${row.rankName!}"
    },

    {
    "name": "手机号",
    "template": r"${row.mobile!}"
    },

    {
    "name": "产品编码(boss)",
    "template": r"${row.enterpriseCode!}"
    },

    {
    "name": "奖品名称",
    "template": r"${row.productName!}"
    },

    {
    "name": "中奖时间",
    "template": r" ${row.winTime?datetime}"
    },

    {
    "name": "充值情况",
    "template": r"<#if row.status??>
										<#if row.status==1>
											待充值
										<#elseif row.status==2>
											已发送充值请求
										<#elseif row.status==3>
											充值成功
										<#else>
											充值失败
										</#if>
									</#if>"
    },

    {
    "name": "失败原因",
    "template": r"
										<#if row.status??>
											<#if row.status == 4>
												${row.reason!}
											<#else>
												-
											</#if>
										</#if>
                                      "
    }
    ] />

    <@Util.getListView pageResult.list fieldsConfig />

    <#-- pageInfo -->
    <#include "../pageInfo.ftl" encoding="utf-8" >

        <!-- PAGE CONTENT ENDS -->
    </div>
</div>
<script type="text/javascript">

    $(function () {
        $("#table_validate").validate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                //$("#award-error").html("");
                //$("#award-error").append(error);
                element.parent().parent().append(error);
            },
            errorElement: "label",
            rules: {
                mobile: {
                    mobile: true
                }
            }
        });
    });
</script>

</body>
</html>