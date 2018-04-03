<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-审批流程编辑</title>
    <meta name="keywords" content="流量平台 编辑审批流程"/>
    <meta name="description" content="流量平台 编辑审批流程"/>

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
            margin-left: 700px;
        }
    </style>
</head>
<body>

<div class="main-container">


    <div class="module-header mt-30 mb-20">
        <h3>编辑审批流程
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <form class="form-horizontal" action="${contextPath}/manage/approvalDefinition/saveEdit.html" method="post"
          name="table_validate" id="table_validate">
        <div class="tile mt-30">
            <div class="tile-content">
                <div class="row form">
                    <input type="hidden" id="id" name="id" value="${approvalProcess.id!}"/>
                    <div class="col-md-8 col-md-offset-3">


                        <div class="form-group">
                            <label>审批类型：</label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="type" type="hidden" id="type"
                                       value="${approvalProcess.type!}"  required>
                                <span class="btn btn-default" style="width: 300px">
                                    <#if approvalProcess.type==0>
                                            企业开户审批流程
                                    </#if>
                                    <#if approvalProcess.type==1>
                                            产品变更审批流程
                                    </#if>
                                    <#if approvalProcess.type==2>
                                            账户变更审批流程
                                    </#if>
                                    <#if approvalProcess.type==3>
                                            营销活动审批流程
                                    </#if>
                                    <#if approvalProcess.type==4>
                                            信息变更审批流程
                                    </#if>
                                    <#if approvalProcess.type==5>
                                            企业EC审批流程
                                    </#if>
                                    <#if approvalProcess.type==6>
                                        营销卡激活审批流程
                                    </#if>
                                    <#if approvalProcess.type==7>
                                        营销卡制卡审批流程
                                    </#if>
                                    <#if approvalProcess.type==8>
                                        企业管理员信息审批流程
                                    </#if>
                                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>审批流程发起角色：</label>
                            <div class="btn-group btn-group-sm">

                                <input style="width: 0; height: 0; opacity: 0" name="originRoleId" id="originRoleId"
                                       value="${approvalProcess.originRoleId!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择平台角色</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu originRoleIds">
                                <#if approvalProcess.type==8>
                                    <li data-value="2"><a href="#">客户经理</a></li>
                                <#else>
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

                                <input style="width: 0; height: 0; opacity: 0" name="msg" id="msg"
                                       value="${approvalProcess.msg!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择是否发送</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu msgUL">    
                                     <li data-value="0"><a href="#">不发送</a></li>
                                     <li data-value="1"><a href="#">发送</a></li>
                                </ul>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label>是否给审批管理员发送代办任务的提醒：</label>
                            <div class="btn-group btn-group-sm">

                                <input style="width: 0; height: 0; opacity: 0" name="recvmsg" id="recvmsg"
                                       value="${approvalProcess.recvmsg!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择是否发送</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu recvmsgUL">    
                                     <li data-value="0"><a href="#">不发送</a></li>
                                     <li data-value="1"><a href="#">发送</a></li>
                                </ul>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>审批级数：</label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" type="hidden" name="stage" id="stage"
                                       value="${approvalProcess.stage!}" required>
                                <span class="btn btn-default" style="width: 300px">
                                    <#if approvalProcess.stage==0>
                                        无审批流程
                                    </#if>
                                    <#if approvalProcess.stage==1>
                                        一级审批流程
                                    </#if>
                                    <#if approvalProcess.stage==2>
                                        二级审批流程
                                    </#if>
                                    <#if approvalProcess.stage==3>
                                        三级审批流程
                                    </#if>
                                    <#if approvalProcess.stage==4>
                                        四级审批流程
                                    </#if>
                                    <#if approvalProcess.stage==5>
                                        五级审批流程
                                    </#if>
                                </span>
                            </div>
                        </div>

                        <div class="form-group stageItem">
                            <label>审批流程：</label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="firstRoleId" id="firstRoleId"
                                       value="${approvalProcess.firstRoleId!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择第一级审批角色</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu firstRoleIds">
                                <#if roles??>
                                    <#list approvalRoles as approvalRole>
                                        <li data-value="${(approvalRole.roleId)! }"><a
                                                href="#">${approvalRole.name!}</a></li>
                                    </#list>
                                </#if>
                                </ul>
                            </div>
                        </div>

                        <div class="form-group stageItem">
                            <label></label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="secondRoleId" id="secondRoleId"
                                       value="${approvalProcess.secondRoleId!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择第二级审批角色</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu secondRoleIds">
                                <#if roles??>
                                    <#list approvalRoles as approvalRole>
                                        <li data-value="${(approvalRole.roleId)! }"><a
                                                href="#">${approvalRole.name!}</a></li>
                                    </#list>
                                </#if>
                                </ul>
                            </div>
                        </div>

                        <div class="form-group stageItem">
                            <label></label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="thirdRoleId" id="thirdRoleId"
                                       value="${approvalProcess.thirdRoleId!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择第三级审批角色</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu thirdRoleIds">
                                <#if roles??>
                                    <#list approvalRoles as approvalRole>
                                        <li data-value="${(approvalRole.roleId)! }"><a
                                                href="#">${approvalRole.name!}</a></li>
                                    </#list>
                                </#if>
                                </ul>
                            </div>
                        </div>

                        <div class="form-group stageItem">
                            <label></label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="forthRoleId" id="forthRoleId"
                                       value="${approvalProcess.forthRoleId!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择第四级审批角色</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu forthRoleIds">
                                <#if roles??>
                                    <#list approvalRoles as approvalRole>
                                        <li data-value="${(approvalRole.roleId)! }"><a
                                                href="#">${approvalRole.name!}</a></li>
                                    </#list>
                                </#if>
                                </ul>
                            </div>
                        </div>

                        <div class="form-group stageItem">
                            <label></label>
                            <div class="btn-group btn-group-sm">
                                <input style="width: 0; height: 0; opacity: 0" name="fifthRoleId" id="fifthRoleId"
                                       value="${approvalProcess.fifthRoleId!}" required>
                                <button type="button" class="btn btn-default" style="width: 300px">选择第五级审批角色</button>
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                        aria-haspopup="true" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu fifthRoleIds">
                                <#if roles??>
                                    <#list approvalRoles as approvalRole>
                                        <li data-value="${(approvalRole.roleId)! }"><a
                                                href="#">${approvalRole.name!}</a></li>
                                    </#list>
                                </#if>
                                </ul>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

        <div class="mt-30 btn-save">
            <input type="submit" class="btn btn-warning btn-sm mr-10 dialog-btn" style="width: 114px;" value="保存"/>&nbsp;&nbsp;
            &nbsp;&nbsp;<span style='color:red'>${errorMsg!}</span>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
     var awards = [];

     <#if approvalRoles??>
        <#list approvalRoles as role>
        awards.push({
            roleId:"${(role.roleId)! }",
            name:"${role.name!}",
        });
    </#list>
    </#if>


    require(["common", "bootstrap"], function () {

        checkFormValidate();

        init();

        var type = $("#type").val();
        $(".approval-types li[data-value='" + type + "']").children("a").click();

        var originRoleId = $("#originRoleId").val();
        $(".originRoleIds li[data-value='" + originRoleId + "']").children("a").click();
        
        var msg = $("#msg").val();
        $(".msgUL li[data-value='" + msg + "']").children("a").click();
        
        var recvmsg = $("#recvmsg").val();
        $(".recvmsgUL li[data-value='" + recvmsg + "']").children("a").click();

        var firstRoleId = $("#firstRoleId").val();
        $(".firstRoleIds li[data-value='" + firstRoleId + "']").children("a").click();

        var secondRoleId = $("#secondRoleId").val();
        $(".secondRoleIds li[data-value='" + secondRoleId + "']").children("a").click();

        var thirdRoleId = $("#thirdRoleId").val();
        $(".thirdRoleIds li[data-value='" + thirdRoleId + "']").children("a").click();

        var forthRoleId = $("#forthRoleId").val();
        $(".forthRoleIds li[data-value='" + forthRoleId + "']").children("a").click();

        var fifthRoleId = $("#fifthRoleId").val();
        $(".fifthRoleIds li[data-value='" + fifthRoleId + "']").children("a").click();

    });

    function init() {
        var stageCount = "${approvalProcess.stage}";
        console.log(stageCount);

        if (stageCount == 0) {
            $(".stageItem").hide();
        }
        else {
            $(".stageItem:lt(" + stageCount + ")").show();
            $(".stageItem:lt(" + stageCount + ") input").show();
            $(".stageItem:gt(" + (stageCount - 1) + ")").hide();
            $(".stageItem:gt(" + (stageCount - 1) + ") input").hide();
        }
    }

    function checkFormValidate() {
        $("#table_validate").packValidate({
            errorPlacement: function (error, element) {
                error.addClass("error-tip");
                $(element).parent().parent().append(error);

            },
            errorElement: "span",
            rules: {},
            messages: {
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
                    required: "请选择是否收到短信"
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
</script>

</body>
</html>