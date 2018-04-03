<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>统一流量平台-流量众筹详情</title>
    <meta name="keywords" content="统一流量平台 流量众筹详情"/>
    <meta name="description" content="统一流量平台 流量众筹详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->

    <style>
    	.form-group label {
    		width: 111px;
    		text-align: right;
    		vertical-align: top;
		}
		
        .form input[type='text'],
        .form input[type='number'],
        .form select {
            background: transparent;
            border: 1px solid #ccc;
            outline: none;
            border-radius: 3px;
            padding: 3px 10px;
            width: 323px;
        }

        .form .table input[type='text'],
        .form .table input[type='number'],
        .form .table select {
            width: auto;
        }


        textarea {
            width: 323px;
            resize: none;
            height: 75px;
            outline: none;
            border: 1px solid #ccc;
        }

        th, td {
            vertical-align: middle !important;
        }

        input.count, input.probability, input.size {
            width: 100px;
            text-align: center;
        }

        table .btn-sm {
            padding: 2px 10px !important;
        }

        #setting-dialog label {
            width: 125px;
            text-align: right;
        }

        .form-group label {
            width: 111px;
            text-align: right;
        }

        .form-group span{

        }

        .preview {
            margin-left: 115px;
            margin-top: -21px;
            display: block;
            overflow: hidden;
        }

        .preview img{
            max-width: 80%;
        }

        .limit-tip {
            color: red;
            margin-left: 116px;
        }


        .discount-tip {
            color: red;
            margin: 0 auto;
        }

        .btn-disabled {
            pointer-events: none;
            cursor: not-allowed;
            background-color: #999;
            border-color: #999;
            color: #fff;
        }
        .btn-yy{
            pointer-events: none;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <#if opinions??>
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
    </#if>

    <div class="tile mt-30">
        <div class="tile-header">
            活动信息
        </div>
        <div class="tile-content">
            <form class="row form" id="dataForm">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>活动名称：</label>
                        <span style="display: inline-block; width: 70%; word-break: break-all;">
                        	${activity.name!}
                        </span>
                    </div>
                    
                    <div class="form-group">
                        <label>活动ID：</label>
                        <span style="display: inline-block; width: 70%; word-break: break-all;">
                        	${encryptActivityId!}
                    	</span>
                    </div>
                    
                    <div class="form-group">
                        <label>企业名称：</label>
                        <span>${enterprise.name!}</span>
                    </div>
                    
                    <div class="form-group">
                    	<label style="vertical-align: top;">活动BANNER：</label>
                    	<img id="banner" style="width: 355px; height: 151px;"
                         	src="${contextPath}/manage/crowdFunding/getImage.html?activityId=${activity.activityId}&type=banner"/>
                	</div>
                
                <#--
                    <div class="form-group">
                        <label>活动BANNER：</label>
                    </div>

                    <div class="form-group">
                        <label>预览：</label>
                        <span class='w-100'>
                            <span class="preview">
                                <img src="${contextPath}/manage/crowdFunding/getImage.html?activityId=${activity.activityId}&type=banner">
                            </span>
                        </span>
                        <div class="limit-tip imgBanner"></div>
                    </div>
                    -->
                    
                    <div class="form-group">
                    	<label style="vertical-align: top;">企业LOGO：</label>
                    	<img id="logo" style="width: 160px; height: 160px;"
                         	src="${contextPath}/manage/crowdFunding/getImage.html?activityId=${activity.activityId}&type=logo"/>
                	</div>

				<#--
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
                    
                   
                </div>
                <div class="col-md-6">
                
            	 	<div class="form-group">
	                    <label>活动时间：</label>
	                    <span>
	                        ${activity.startTime?datetime}&nbsp;至&nbsp;${activity.endTime?datetime}
	                    </span>
                    </div>
                    
                    <div class="form-group">
                        <label>充值时间：</label>
                        <span>
                        	<#if crowdfundingActivityDetail.chargeType?? && crowdfundingActivityDetail.chargeType==1>
                        		当月生效
                        	</#if>
                        	<#if crowdfundingActivityDetail.chargeType?? && crowdfundingActivityDetail.chargeType==2>
                        		次月生效
                        	</#if>
                        </span>
                    </div>

                    <#--
                	<div class="form-group">
                        <label>支付方式：</label>
                        <span>
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
                        <label>目标人数：</label>
                        <span>${crowdfundingActivityDetail.targetCount!}</span>
                    </div>
                    <div class="form-group">
                        <label>众筹成功人数：</label>
                        <span>${crowdfundingActivityDetail.currentCount!}</span>
                    </div>
                    <div class="form-group">
                        <label>众筹是否成功：</label>
                        <span>
                            <#if crowdfundingActivityDetail.result?? && crowdfundingActivityDetail.result==0>
                                否
                            </#if>
                            <#if crowdfundingActivityDetail.result?? && crowdfundingActivityDetail.result==1>
                                是
                            </#if>
                            <#if crowdfundingActivityDetail.result?? && crowdfundingActivityDetail.result==2>
                                否
                            </#if>
                        </span>
                    </div>
                    <div class="form-group">
                        <label style="vertical-align: top;">活动规则：</label>
                        <span style="display: inline-block; width: 70%;word-break: break-all;">
                            ${crowdfundingActivityDetail.rules!}
                        </span>
                    </div>
                    
                    <div class="form-group">
                        <label>创建时间：</label>
                        <span>${activity.createTime?datetime}</span>
                    </div>
                    <div class="form-group">
                        <label>状态：</label>
                        <span>
                            <#if activity.status?? && activity.status==0>
                                已保存
                            </#if>
                            <#if activity.status?? && activity.status==1>
                                已上架
                            </#if>
                            <#if activity.status?? && activity.status==2>
                                活动进行中
                            </#if>
                            <#if activity.status?? && activity.status==3>
                                已下架
                            </#if>
                            <#if activity.status?? && activity.status==4>
                                活动已结束
                            </#if>
                            <#if activity.status?? && activity.status==5>
                                审批中
                            </#if>
                            <#if activity.status?? && activity.status==6>
                                审批完成
                            </#if>
                            <#if activity.status?? && activity.status==7>
                                已驳回
                            </#if>
                        </span>
                    </div>
                    
                    <#--
                    <div class="form-group">
                        <label>URL：</label>
                        <span>http://sichuan.4ggogo.com/cq-web/entRedpacket/</span>
                    </div>
                    <div class="form-group">
                        <label>二维码：</label>
                        <span class="preview">
                                <img src="../../assets/imgs/qcode.png">
                            </span>
                    </div>
                    -->
                    
                    <#--
                    <div class="form-group">
                        <label>审核附件：</label>
                        <span class="file-box">
                            <a class="btn btn-sm btn-default cyan-text" href="">下载附件</a>
                        </span>
                    </div>
                    -->
                    
                    <#if crowdfundingActivityDetail.appendix?? && crowdfundingActivityDetail.appendix!=''>
                    	<div class="form-group">
                    		<label>审核附件：</label>
                    		<a name="appendix" id="appendix" onclick="downLoad(this)" style="width: 242px;">${crowdfundingActivityDetail.appendix!}</a>
                		</div>
                    </#if>
                    
                    <div class="form-group">
                        <label>用户列表：</label>
                        <#if crowdfundingActivityDetail.userList?? && crowdfundingActivityDetail.userList==1>
                            <#if crowdfundingActivityDetail.hasWhiteOrBlack?? && crowdfundingActivityDetail.hasWhiteOrBlack==0>
                                <span></span>
                            </#if>
                            <#if crowdfundingActivityDetail.hasWhiteOrBlack?? && crowdfundingActivityDetail.hasWhiteOrBlack==1>
                                <span></span>
                                <a class="btn btn-sm btn-default cyan-text" href="${contextPath}/manage/crowdFunding/downloadPhoneList.html?activityId=${activity.activityId!}">下载用户列表</a>
                            </#if>
                        
                            <#if crowdfundingActivityDetail.hasWhiteOrBlack?? && crowdfundingActivityDetail.hasWhiteOrBlack==2>
                                <span></span>
                                <a class="btn btn-sm btn-default cyan-text" href="${contextPath}/manage/crowdFunding/downloadPhoneList.html?activityId=${activity.activityId!}">下载黑名单</a>
                            </#if>
                        </#if>
                        <#if crowdfundingActivityDetail.userList?? && crowdfundingActivityDetail.userList==2>
                            <span>ADC接口查询</span>
                        </#if>
                        <#if crowdfundingActivityDetail.userList?? && crowdfundingActivityDetail.userList==3>
                            <span>企业接口查询:  ${crowdfundingQueryUrl.queryUrl}</span>
                        </#if>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            产品配置
        </div>
        <div class="tile-content">
            <div class="table-responsive mt-30 text-center">
                <table class="table table-bordered" id="awardsTable">
                    <thead>
                    <tr>
                        <td>运营商</td>
                        <td>产品名称</td>
                        <td>产品编码</td>
                        <td>流量包大小</td>
                        <td>售出价格</td>
                        <td>使用范围</td>
                        <td>漫游范围</td>
                    </tr>
                    </thead>
                    <tbody>
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
    </div>

    <div class="mt-30">
        <a class="btn btn-sm btn-warning" 
        href="${contextPath}/manage/crowdFunding/records.html?${_csrf.parameterName}=${_csrf.token}&activityId=${activity.activityId!}">参与用户列表</a>
    </div>
</div>

<script type="text/javascript" src="${contextPath}/assets/lib/require.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/config.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/imgUpload.js"></script>
<script type="text/javascript" src="${contextPath}/assets/lib/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="${contextPath}/assets/js/up.js"></script>

<script>
	function downLoad(obj) {
        location.href = "${contextPath}/manage/crowdFunding/downloadFile.html?activityId=${activity.activityId!}&type=appendix";
    }
</script>

</body>
</html>