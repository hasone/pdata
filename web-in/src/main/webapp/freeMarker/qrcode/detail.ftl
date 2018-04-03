<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-流量券详情</title>
    <meta name="keywords" content="流量平台 二维码详情"/>
    <meta name="description" content="流量平台 二维码详情"/>

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
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="javascript:window.location.href='${contextPath}/manage/qrcode/index.html'">返回</a>
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
            <div class="row">
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
                        <span class="detail-label">活动创建时间:</span>
                        <span class="detail-value">${activity.createTime?datetime}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">活动开始时间:</span>
                        <span class="detail-value">${activity.startTime?datetime}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">活动结束时间:</span>
                        <span class="detail-value">${activity.endTime?datetime}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">产品总数:</span>
                        <span class="detail-value">${activityInfo.prizeCount!}</span>
                    </div>
                <#--
                <div class="form-group">
                    <span class="detail-label">流量类型:</span>
                    <span class="detail-value">${activityInfo.prizeCount!}</span>
                </div>    -->

                    <#--
                <#if cqFlag??&&cqFlag==0>
                    <#if activity.status?? && (activity.status == 0 || activity.status == 1 ||
                    activity.status == 2 || activity.status == 5) >
                        <div class="form-group">
                            <span class="detail-label">活动规模（估计）:</span>
                            <span class="detail-value">${(activityInfo.price/100.0)?string("#.##")}
                                元=》
                                <#if activityInfo.totalProductSize??>
                                    <#if ((activityInfo.totalProductSize)<1024)>
                                        <span>${(activityInfo.totalProductSize)?string("#.##")}
                                            KB</span>
                                    </#if>
                                    <#if ((activityInfo.totalProductSize)>=1024) && ((activityInfo.totalProductSize?number)<1024*1024)>
                                        <span>${((activityInfo.totalProductSize)/1024.0)?string("#.##")}
                                            MB</span>
                                    </#if>
                                    <#if ((activityInfo.totalProductSize)>=1024*1024)>
                                        <span>${((activityInfo.totalProductSize)/1024.0/1024.0)?string("#.##")}
                                            GB</span>
                                    </#if>
                                </#if>
                          	流量=》${activityInfo.prizeCount!}份奖品=》${activityInfo.userCount!}中奖用户</span>
                        </div>
                    </#if>
                    <div class="form-group">
                        <span class="detail-label">活动规模（当前）:</span>
                            <span class="detail-value">${(currentActivityInfo.money/100.0)?string("#.##")}
                                元=》
                                <#if currentActivityInfo.flow??>
                                    <#if ((currentActivityInfo.flow)<1024)>
                                        <span>${(currentActivityInfo.flow)?string("#.##")}KB</span>
                                    </#if>
                                    <#if ((currentActivityInfo.flow)>=1024) && ((currentActivityInfo.flow)<1024*1024)>
                                        <span>${((currentActivityInfo.flow)/1024.0)?string("#.##")}
                                            MB</span>
                                    </#if>
                                    <#if ((currentActivityInfo.flow)>=1024*1024)>
                                        <span>${((currentActivityInfo.flow)/1024.0/1024.0)?string("#.##")}
                                            GB</span>
                                    </#if>
                                </#if>
                          	流量=》${currentActivityInfo.count!}份奖品=》${currentActivityInfo.userCount!}中奖用户=》
                            ${currentActivityInfo.joinCount!}参与用户</span>
                    </div>
                </#if>
                -->

                <#if activityInfo.hasWhiteOrBlack?? && activityInfo.hasWhiteOrBlack!=0>
                    <a class="btn btn-sm btn-warning"
                       href="${contextPath}/manage/flowcard/downloadPhones.html?activitiesId=${activity.activityId!}&activityType=2">下载黑白名单</a>
                </#if>
                <#if !(activityInfo.download??) || activityInfo.download !=1 >

                    <!-- <a class="btn btn-sm btn-warning" href="${contextPath}/manage/qrcode/downloadQrCode.html?activitiesId=${activity.activityId!}">下载二维码</a>-->
                    <a class="btn btn-sm btn-warning" href="javascript:void(0)" id="downloadPic"
                       onClick="downloadPic()">下载二维码</a>
                </#if>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30" style="overflow:auto;">
        <div class="tile-content" style="padding: 0;">
            <table class="table" style="margin-bottom: 0;">
                <thead>
                <tr>
                    <th>运营商</th>
                    <th>产品名称</th>
                    <th>产品编码</th>
                    <th>流量包大小</th>


                <#if cqFlag??&&cqFlag==0>
                    <th>售出价格</th>
                </#if>

                    <th>使用范围</th>
                    <th>漫游范围</th>
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

                        <#if cqFlag??&&cqFlag==0>
                        <td>
                        	<#if (item.flowType == 1) || (item.flowType == 3)>
                        		 	-
                        	<#else>
                        		 ${(item.price/100.0)?string('#.##')}元
                        	</#if>                  
                       </td>
                        </#if>

                    <td>${(item.ownershipRegion)!}</td>

                    <td>${(item.roamingRegion)!}</td>


                    </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-30">
        <a class="btn btn-sm btn-warning"
           href="${contextPath}/manage/qrcode/records.html?activityId=${activity.activityId}">查看赠送用户信息</a>
    </div>


</div>


<!--[if lt IE 9]>
<script src="../../assets/lib/es5-shim.min.js"></script>
<script src="../../assets/lib/es5-sham.min.js"></script>
<![endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>
    require(["echarts", "common", "bootstrap", "mock"], function (echarts) {

    });

    function downloadPic() {
        //$('#test').css('display','none');
        $("#downloadPic").hide();
        window.location.href =
                "${contextPath}/manage/qrcode/downloadQrCode.html?activitiesId=${activity.activityId!}";
    }
</script>

</body>
</html>