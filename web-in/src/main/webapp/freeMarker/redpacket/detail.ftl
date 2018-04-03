<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-红包详情</title>
    <meta name="keywords" content="流量平台 红包详情"/>
    <meta name="description" content="流量平台 红包详情"/>

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
        <h3>红包活动详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>

    <div class="tile mt-30">
        <div class="tile-header">
            活动信息
        </div>
        <div class="tile-content">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <span class="detail-label">企业名称:</span>
                        <span class="detail-value">${entRedpacket.enterpriseName!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">活动名称:</span>
                        <span class="detail-value">${entRedpacket.AName!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">活动时间:</span>
                        <span class="detail-value">${startTime?date}至 ${endTime?date}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">每用户可抢红包数:</span>
                        <span class="detail-value">${entRedpacket.maxPerUser!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">已抢红包数:</span>
                        <span class="detail-value">${entRedpacket.usedAmount!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">红包描述:</span>
                        <span class="detail-value">${ruleTemplate.description!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">活动对象:</span>
                        <span class="detail-value">${ruleTemplate.people!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">活动规则:</span>
                        <span class="detail-value">${ruleTemplate.activityDes!}</span>
                    </div>
                <#if smsTemplate?exists>
                    <div class="form-group">
                        <span class="detail-label">短信功能:</span>
                        <span class="detail-value">已开启</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">短信内容:</span>
                        <span class="detail-value">${smsTemplate.content!}</span>
                    </div>
                </#if>
                </div>
                <div class="col-md-6">
                    <!--<div class="form-group">
                        <span class="detail-label">名单类型:</span>
                        <span class="detail-value">未设置黑白名单</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label">渠道同步:</span>
                        <span class="detail-value">微信公众号    APP</span>
                    </div>-->
                    <div class="form-group">
                        <span class="detail-label">URL:</span>
                        <span class="detail-value">${redpacketUrl!}</span>
                    </div>
                    <div class="form-group">
                        <span class="detail-label" style="vertical-align: top;">二维码:</span>
                            <span class="detail-value ml-10">
                            <#if redpacketUrl??>
                                <img src="${contextPath}/manage/entRedpacket/qrCode.html?redpacketID=${entRedpacket.id}">
                            </#if>
                            </span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- <div class="tile mt-30">
            <div class="tile-header">
                活动数据统计
            </div>
            <div class="tile-content">
                <div class="tools">
                
                	<input type="hidden" class="searchItem" name="ruleId" value="${entRedpacket.id}" id="ruleId">
              
                	<div class="form-group form-group-sm" id="search-time-range">
                        <label>中奖时间：</label>&nbsp
                        <input type="text" class="form-control search-startTime searchItem" name="startTime" value="" id="startTime" placeholder="">~
                        <input type="text" class="form-control search-endTime searchItem" name="endTime" id="endTime" value="" placeholder="">                
                    </div>
                    <a class="btn btn-sm btn-warning" id="search-btn" onclick="renderLine(echarts)">确定</a>
                    
                    <div class="pull-right">
                        <a class="icon-btn icon-refresh" title="刷新"></a>
                        <a class="icon-btn icon-save" title="保存"></a>
                        <a class="btn btn-warning ml-10">导出CSV</a>
                    </div>
                </div>

                <div class="lineCharts" style="height: 350px;">

                </div>
            </div>
        </div>-->

    <div class="tile mt-30" style="overflow:auto;">
        <div class="tile-header">
            流量配置
        </div>
        <div class="tile-content" style="padding: 0;">
            <table class="table" style="margin-bottom: 0;">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>奖项</th>
                    <th>产品编码</th>
                    <th>产品名称</th>
                    <th>奖品名称</th>
                    <th>投放数量</th>
                </tr>
                </thead>
                </tbody>
            <#if prizes??>
                <#list prizes as item>
                    <tr>
                        <td>${(item.idPrefix)!}</td>
                        <td>${(item.rankName)!}</td>
                        <td>${(item.productCode)!}</td>
                        <td>${(item.productName)!}</td>
                        <td>${(item.prizeName)!}</td>
                        <td>${(item.count)!}</td>
                    </tr>
                </#list>
            </#if>
                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-30">
        <a class="btn btn-sm btn-warning"
           href="${contextPath}/manage/entRedpacket/records.html?ruleId=${entRedpacket.id}">查看用户中奖信息</a>
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

</script>

</body>
</html>