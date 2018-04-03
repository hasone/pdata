<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-流量众筹详情</title>
    <meta name="keywords" content="流量平台 流量众筹详情"/>
    <meta name="description" content="流量平台 流量众筹详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .detail-value {
            word-break: break-all;
            white-space: normal;
            word-wrap: break-word;
            display: inline-block;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" href="${contextPath}/manage/approval/approvalIndex.html?approvalType=3&back=1">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">审批信息查看</div>
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>审批状态</th>
                            <th>审批用户</th>
                        <#if provinceFlag??&&provinceFlag=="sc">
                            <th>用户</th>
                        <#else>
                            <th>用户职位</th>
                        </#if>
                            <th>审批时间</th>
                            <th>审批意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if opinions??>
                            <#list opinions as opinion>
                            <tr>
                                <td>${opinion.description!}</td>
                                <td>${opinion.userName!}</td>
                                <td>${opinion.managerName!}${opinion.roleName!}</td>
                                <td>${(opinion.updateTime?datetime)!}</td>
                                <td title="<#if opinion.comment??>${opinion.comment}</#if>">
                                    <#if opinion.comment??>
													${opinion.comment!}
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

    <div class="tile mt-30">
        <div class="tile-header">
            活动信息
        </div>
        <div class="tile-content">
            <div class="row form">
                <div class="col-md-6">
                    <div class="form-group">
                        <span class="detail-label">企业名称:</span>
                        <span class="detail-value">${enterprise.name!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">企业编码:</span>
                        <span class="detail-value">${enterprise.code!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">活动名称:</span>
                        <span class="detail-value">${activity.name!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">活动类型:</span>
                        <span class="detail-value">
                            流量众筹
                        </span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">创建时间:</span>
                        <span class="detail-value">${activity.createTime?datetime}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">活动时间:</span>
                        <span class="detail-value">${activity.startTime?datetime}&nbsp;~&nbsp;${activity.endTime?datetime}</span>
                    </div>

                    <#--
                    <div class="form-group">
                        <span class="detail-label">支付方式:</span>
                        <span class="detail-value">
                        	<#if crowdfundingActivityDetail.joinType?? && crowdfundingActivityDetail.joinType==1>
                        		企业统付
                        	</#if>
                        	<#if crowdfundingActivityDetail.joinType?? && crowdfundingActivityDetail.joinType==2>
                        		个人报名
                        	</#if>
						</span>
                    </div>
                    -->
                    
                    <div class="form-group">
                        <span class="detail-label">用户列表:</span>
                        <span class="detail-value">
                            <#if crowdfundingActivityDetail.hasWhiteOrBlack?? && crowdfundingActivityDetail.hasWhiteOrBlack==1>
                            	<span></span>
                            	<a class="btn btn-sm btn-default cyan-text" href="${contextPath}/manage/crowdFunding/downloadPhoneList.html?activityId=${activity.activityId!}">下载用户列表</a>
                        	</#if>
                        </span>
                    </div>
                    
                    <div class="form-group">
                        <span class="detail-label">目标众筹人数:</span>
                        <span class="detail-value">${crowdfundingActivityDetail.targetCount!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">活动规则:</span>
                        <span class="detail-value">${crowdfundingActivityDetail.rules!}</span>
                    </div>
                    
                    
                </div>

                <div class="col-md-6">
                
                <#--
                    <div class="form-group">
                        <label>活动BANNER：</label>
                    </div>

                    <div class="form-group">
                        <label>预览：</label>
                        <span class='w-100'>
                            <span class="preview">
                                <img src="../../assets/imgs/flow.png">
                            </span>
                        </span>
                        <div class="limit-tip imgBanner"></div>
                    </div>

				
                    <div class="form-group">
                        <label>企业LOGO：</label>
                    </div>

                    <div class="form-group">
                        <label>预览：</label>
                        <span class='w-100'>
                            <span class="preview">
                                <img src="../../assets/imgs/icon-girl.png">
                            </span>
                        </span>
                        <div class="limit-tip imgLogo"></div>
                    </div>
                    -->
                    <div class="form-group">
                    	<span class="detail-label" style="vertical-align: top; font-weight: 0;">活动BANNER：</span>
                    	<img id="banner" style="width: 355px; height: 151px;"
                         	src="${contextPath}/manage/crowdFunding/getImage.html?activityId=${activity.activityId}&type=banner"/>
                	</div>
                	
                    <div class="form-group">
                    	<span class="detail-label" style="vertical-align: top; font-weight: 0;">企业LOGO：</span>
                    	<img id="logo" style="width: 160px; height: 160px;"
                         	src="${contextPath}/manage/crowdFunding/getImage.html?activityId=${activity.activityId}&type=logo"/>
                	</div>

                    

					<#--
                    <div class="form-group">
                        <span class="detail-label">审批附件：</span>
                        <span class="file-box">
                            <a class="btn btn-sm btn-default cyan-text" href="">下载附件</a>
                        </span>
                    </div>
                    -->
                    <#if crowdfundingActivityDetail.appendix?? && crowdfundingActivityDetail.appendix!=''>
	                    <div class="form-group">
	                    	<span class="detail-label">审批附件：</span>
	                    	<a name="appendix" id="appendix" onclick="downLoad(this)" style="width: 242px;">${crowdfundingActivityDetail.appendix!}</a>
	                	</div>
                	</#if>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30" style="overflow:auto;">
        <div class="tile-header">
            奖品列表
        </div>
        <div class="tile-content" style="padding: 0;">
            <table class="table" style="margin-bottom: 0;">
                <thead>
                <tr>
                    <th>运营商</th>
                    <th>产品名称</th>
                    <th>产品编码</th>
                    <th>流量包大小</th>
                    <th>售出价格</th>
                    <th>使用范围</th>
                    <th>漫游范围</th>
                </tr>
                </thead>
                </tbody>
            <#if prizes??>
                <#list prizes as item>
                    <#if item.isp=="M">
                        <td>移动</td>
                    </#if>
                    <#if item.isp=="U">
                        <td>联通</td>
                    </#if>
                    <#if item.isp=="T">
                        <td>电信</td>
                    </#if>
                    <#if item.isp=="A">
                        <td>三网</td>
                    </#if>

                    <td>${(item.productName)!}</td>

                    <td>${(item.productCode)!}</td>

                    <#if (item.productSize<1024)>
                        <td>${(item.productSize)?string('#.##')}KB</td>
                    </#if>
                    <#if (item.productSize>=1024) && (item.productSize<1024*1024)>
                        <td>${(item.productSize/1024.0)?string('#.##')}MB</td>
                    </#if>
                    <#if (item.productSize>=1024*1024)>
                        <td>${(item.productSize/1024.0/1024.0)?string('#.##')}GB</td>
                    </#if>
                    <td>${(item.price/100.0)?string('#.##')}元</td>
                    <td>${(item.ownershipRegion)!}</td>

                    <td>${(item.roamingRegion)!}</td>
                    </tr>
                </#list>
            </#if>
                </tbody>
            </table>
        </div>
    </div>
	<#if canOperate?? && canOperate == 1>
    <div class="tile mt-30" style="overflow:auto;">
        <div class="tile-header">
            审批意见
        </div>
        <div class="tile-content">
            <form action="${contextPath}/manage/approval/saveActivityApproval.html" method="post" name="enterprisesForm"
                  id="table_validate">
                <input type="hidden" name="enterId" id="enterId" value="${(enterprise.id)!}">
                <input type="hidden" name="approvalRecordId" id="approvalRecordId" value="${approvalRecordId!}">
                <input type="hidden" name="requestId" id="requestId" value="${requestId!}">
                <input type="hidden" name="processId" id="processId" value="${processId!}">

                <div class="tile-content">
                    <label>审批意见：</label>
                    <textarea rows="10" cols="100" maxlength="300" name="comment" id="comment"
                              class="hasPrompt" required></textarea>
                </div>
                <div class="btn-save mt-30">
                    <input type="hidden" id="approvalStatus" name="approvalStatus" value="">
                    <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn1">通过</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="javascript:void(0)" class="btn btn-sm btn-warning dialog-btn" id="save-btn2">驳回</a>&nbsp;&nbsp;&nbsp;&nbsp;<span
                        style='color:red'>${errorMsg!}</span>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
    </div>
	</#if>
</div>


<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    require(["echarts", "common", "bootstrap", "mock"], function (echarts) {
        //初始化
        checkFormValidate();
        init();
    });

    function init() {
        //保存审批
        $("#save-btn1").on("click", function () {
            if ($("#table_validate").validate().form()) {
                $("#approvalStatus").val(1);
                $("#table_validate").submit();
            }

            return false;
        });
        $("#save-btn2").on("click", function () {
            if ($("#table_validate").validate().form()) {
                $("#approvalStatus").val(0);
                $("#table_validate").submit();
            }
            return false;
        });
    }

    function checkFormValidate() {
        $("#table_validate").validate({
            errorPlacement: function (error, element) {
                //$(element).parent().find('.error-tip').remove();
                error.addClass("error-tip");

                $(element).parent().append(error);
            },
            errorElement: "span",
            rules: {
                comment: {
                    required: true

                }
            },

            messages: {

                comment: {
                    required: "请填写审批意见!"
                }
            }

        });
    }
    
    function downLoad(obj) {
        location.href = "${contextPath}/manage/crowdFunding/downloadFile.html?activityId=${activity.activityId!}&type=appendix";
    }

</script>
</body>
</html>