<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-审批流程设置</title>
    <meta name="keywords" content="流量平台 审批流程设置"/>
    <meta name="description" content="流量平台 审批流程设置"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .form input[type='text'],
        .form input[type='number'],
        .form input[type='password'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .form-group label {
            width: 180px;
            text-align: right;
        }

        .form .form-group .prompt {
            padding-left: 186px;
            color: #bbb;
            padding-bottom: 0;
            margin-bottom: 0;
        }

        .btn-group .btn {
            text-align: left
        }

        .stageItem {
            display: none;
        }

        .error-tip {
            position: absolute
        }

        .btn-save {
            margin-left: 800px;
        }
    </style>
</head>
<body>

<div class="main-container">


    <div class="module-header mt-30 mb-20">
        <h3>审批流程设置
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <form class="form-horizontal" action="${contextPath}/manage/approvalDefinition/saveApprovalProcess.html"
          method="post" name="table_validate" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-content text-center">
                <div class="row form">
                    <br class="col-md-8 col-md-offset-3">
                    <div class="form-group">
                        <label>审批类型：</label>
                        <div class="btn-group btn-group-sm">
                            <input style="width: 0; height: 0; opacity: 0" name="type" id="type" required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择审批类型</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu" id="processUL">
                                <li data-value="0"><a href="#">企业开户审批流程</a></li>
                                <li data-value="1"><a href="#">产品变更审批流程</a></li>
                                <li data-value="2"><a href="#">账户变更审批流程</a></li>
                                <li data-value="3"><a href="#">营销活动审批流程</a></li>
                                <li data-value="4"><a href="#">信息变更审批流程</a></li>
                                <li data-value="5"><a href="#">企业EC审批流程</a></li>
                                <li data-value="6"><a href="#">营销卡激活审批流程</a></li>
                                <li data-value="7"><a href="#">营销卡制卡审批流程</a></li>
                                <li data-value="8"><a href="#">企业管理员信息审批流程</a></li>
                                <li data-value="9"><a href="#">企业最小额度变更审批</a></li>
                                <li data-value="10"><a href="#">企业预警值变更审批</a></li>
                                <li data-value="11"><a href="#">企业暂停值变更审批</a></li>
                            </ul>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>审批流程发起角色：</label>
                        <div class="btn-group btn-group-sm">
                            <input style="width: 0; height: 0; opacity: 0" name="originRoleId" id="originRoleId"
                                   required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择平台角色</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu" id="roleUL">
                            <#if roles??>
                                <#list roles as role>
                                    <li data-value="${(role.roleId)! }"><a href="#">${role.name!}</a></li>
                                </#list>
                            </#if>
                            </ul>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label>是否给审批流程的发起人发送提醒短信：</label>
                        <div class="btn-group btn-group-sm">

                            <input style="width: 0; height: 0; opacity: 0" name="msg" id="msg" required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择是否发送</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu" id="msgStatus">
                                <li data-value="0"><a href="#">不发送</a></li>
                                <li data-value="1"><a href="#">发送</a></li>
                            </ul>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label>是否给审批管理员发送代办任务的提醒：</label>
                        <div class="btn-group btn-group-sm">

                            <input style="width: 0; height: 0; opacity: 0" name="recvmsg" id="recvmsg" required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择是否发送</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu" id="msgStatus">
                                <li data-value="0"><a href="#">不发送</a></li>
                                <li data-value="1"><a href="#">发送</a></li>
                            </ul>
                        </div>
                    </div>
                    

                    <div class="form-group">
                        <label>审批级数：</label>
                        <div class="btn-group btn-group-sm">

                            <input style="width: 0; height: 0; opacity: 0" name="stage" id="stage" required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择审批级数</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu" id="stagechange">
                                <li data-value="0"><a href="#">无审批流程</a></li>
                                <li data-value="1"><a href="#">一级审批流程</a></li>
                                <li data-value="2"><a href="#">二级审批流程</a></li>
                                <li data-value="3"><a href="#">三级审批流程</a></li>
                                <li data-value="4"><a href="#">四级审批流程</a></li>
                                <li data-value="5"><a href="#">五级审批流程</a></li>
                            </ul>
                        </div>
                    </div>

                    <div class="form-group stageItem">
                        <label>审批流程：</label>
                        <div id="first-stage" class="btn-group btn-group-sm ">
                            <input style="width: 0; height: 0; opacity: 0" name="firstRoleId" id="firstRoleId"
                                   style="display:none;" required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择第一级审批角色</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                            <#if roles??>
                                <#list approvalRoles as approvalRole>
                                    <li data-value="${(approvalRole.roleId)! }"><a href="#">${approvalRole.name!}</a>
                                    </li>
                                </#list>
                            </#if>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group stageItem">
                        <label></label>
                        <div id="second-stage" class="btn-group btn-group-sm ">
                            <input style="width: 0; height: 0; opacity: 0" name="secondRoleId" id="secondRoleId"
                                   style="display:none;" required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择第二级审批角色</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                            <#if roles??>
                                <#list approvalRoles as approvalRole>
                                    <li data-value="${(approvalRole.roleId)! }"><a href="#">${approvalRole.name!}</a>
                                    </li>
                                </#list>
                            </#if>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group stageItem">
                        <label></label>
                        <div id="third-stage" class="btn-group btn-group-sm ">
                            <input style="width: 0; height: 0; opacity: 0" name="thirdRoleId" id="thirdRoleId"
                                   style="display:none;" required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择第三级审批角色</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                            <#if roles??>
                                <#list approvalRoles as approvalRole>
                                    <li data-value="${(approvalRole.roleId)! }"><a href="#">${approvalRole.name!}</a>
                                    </li>
                                </#list>
                            </#if>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group stageItem">
                        <label></label>
                        <div id="forth-stage" class="btn-group btn-group-sm ">
                            <input style="width: 0; height: 0; opacity: 0" name="forthRoleId" id="forthRoleId"
                                   style="display:none;" required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择第四级审批角色</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                            <#if roles??>
                                <#list approvalRoles as approvalRole>
                                    <li data-value="${(approvalRole.roleId)! }"><a href="#">${approvalRole.name!}</a>
                                    </li>
                                </#list>
                            </#if>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group stageItem">
                        <label></label>
                        <div id="fifth-stage" class="btn-group btn-group-sm ">
                            <input style="width: 0; height: 0; opacity: 0" name="fifthRoleId" id="fifthRoleId"
                                   style="display:none;" required>
                            <button type="button" class="btn btn-default" style="width: 300px">选择第五级审批角色</button>
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul class="dropdown-menu">
                            <#if roles??>
                                <#list approvalRoles as approvalRole>
                                    <li data-value="${(approvalRole.roleId)! }"><a href="#">${approvalRole.name!}</a>
                                    </li>
                                </#list>
                            </#if>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

        </div>


        <div class="mt-30 btn-save">
            <input type="submit" class="btn btn-warning btn-sm mr-10 dialog-btn" style="width: 114px;" value="保存"/>&nbsp;&nbsp;
            &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
        </div>
</div>
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    var awards = [];

     <#if roles??>
        <#list roles as role>
        awards.push({
            roleId:"${(role.roleId)! }",
            name:"${role.name!}",
        });
    </#list>
    </#if>

    require(["common", "bootstrap"], function () {

        //init();

        checkFormValidate();

        $("#stagechange a").on("click", function () {
            var stage = $(this).parent().data("value");

            if (stage == 0) {
                $(".stageItem").hide();
            } else {
                $(".stageItem:lt(" + stage + ")").show();
                $(".stageItem:lt(" + stage + ") input").show();
                $(".stageItem:gt(" + (stage - 1) + ")").hide();
                $(".stageItem:gt(" + (stage - 1) + ") input").hide();
            }
        }).eq(0).click();



        $("#processUL a").on("click", function () {
            var stage = $(this).parent().data("value");
            
            if(stage == 8){
                $("#roleUL").empty();
                $("#roleUL").append("<li data-value='2'><a href='#'>客户经理</a></li>");
                $("a","#roleUL li[data-value='2']").click();
            }
            else{
                $("#roleUL").empty();
                for (var i in awards) {
                    $("#roleUL").append("<li data-value='"+awards[i].roleId+"'><a href='#'>"+awards[i].name+"</a></li>");
                }   
            }
        });
    });

    function checkFormValidate() {
        $("#table_validate").packValidate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                $(element).parent().parent().append(error);
            },
            errorElement: "span",
            rules: {},
            messages: {
                type: {
                    required: "请选择审批流程类型"
                },
                originRoleId: {
                    required: "请选择审批流程发起角色"
                },
                stage: {
                    required: "请选择审批级数"
                },
                msg: {
                    required: "请选择是否发送短信"
                },
                recvmsg: {
                    required: "请选择是否发送短信"
                },
                firstRoleId: {
                    required: "请选择第一级审批角色"
                },
                secondRoleId: {
                    required: "请选择第二级审批角色"
                },
                thirdRoleId: {
                    required: "请选择第三级审批角色"
                },
                forthRoleId: {
                    required: "请选择第四级审批角色"
                },
                fifthRoleId: {
                    required: "请选择第五级审批角色"
                }

            }
        });
    }

    function init() {
        document.getElementById("total-stage").style.display = "none";

        $("#firstRoleId").hide();
        $("#secondRoleId").hide();
        $("#thirdRoleId").hide();
        $("#forthRoleId").hide();
        $("#fifthRoleId").hide();
    }

</script>

</body>
</html>