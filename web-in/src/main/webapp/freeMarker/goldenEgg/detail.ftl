<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-砸金蛋详情</title>
    <meta name="keywords" content="流量平台 砸金蛋详情"/>
    <meta name="description" content="流量平台 砸金蛋详情"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]-->
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <!--[endif]-->


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
        <h3>砸金蛋详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="history.go(-1)">返回</a>
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
                        <span class="detail-label">活动名称:</span>
                        <span class="detail-value">${activities.name!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">企业名称:</span>
                        <span class="detail-value">${enterprise.name!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">活动状态:</span>
                        <span class="detail-value">
                        <#if activities?? && activities.status??>
                            <#if activities.status == 0 || activities.status == 7>
                                已保存
                            </#if>
                            <#if activities.status == 5>
                                审批中
                            </#if>
                            <#if activities.status == 6>
                                审批结束
                            </#if>
                            <#if activities.status == 1 || activities.status == 2>
                                活动进行中
                            </#if>
                            <#if activities.status == 4 || activities.status == 3>
                                活动已结束
                            </#if>
                        </#if>
                        </span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">活动时间:</span>
                        <span class="detail-value">&nbsp;&nbsp;${(activities.startTime?datetime)!}
                        ~&nbsp;&nbsp;${(activities.endTime?datetime)!}</span>
                    </div>

                <#if activityTemplate?? && activityTemplate.description??>
                    <div class="form-group">
                        <span class="detail-label">活动描述:</span>
                        <span class="detail-value">${activityTemplate.description!}</span>
                    </div>
                </#if>

                <#if activityTemplate?? && activityTemplate.object??>
                    <div class="form-group">
                        <span class="detail-label">活动对象:</span>
                        <span class="detail-value">${activityTemplate.object!}</span>
                    </div>
                </#if>

                <#if activityTemplate?? && activityTemplate.rules??>
                    <div class="form-group">
                        <span class="detail-label">活动规则:</span>
                        <span class="detail-value">${activityTemplate.rules!}</span>
                    </div>
                </#if>

                    <div class="form-group">
                        <span class="detail-label">用户可中最大数量:</span>
                        <span class="detail-value">${activityTemplate.givedNumber!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">用户次数每天是否重置:</span>
                        <span class="detail-value">
                        <#if activityTemplate?? && activityTemplate.daily??>
                            <#if activityTemplate.daily == 0>
                                是
                            </#if>
                            <#if activityTemplate.daily == 1>
                                否
                            </#if>
                        </#if>
                        </span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">用户最大可玩次数:</span>
                        <span class="detail-value">${activityTemplate.maxPlayNumber!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">是否使用微信鉴权:</span>
                        <span class="detail-value">
                        <#if activityTemplate?? && activityTemplate.userType??>
                            <#if activityTemplate.userType==0>
                                否
                            </#if>
                            <#if activityTemplate.userType==1>
                                是
                            </#if>
                        </#if>
                        </span>
                        &nbsp;&nbsp;
                        <input type="button" id="authorize" value="一键授权"
                               onclick="startAuthorize();"
                               style="display: none;"/>
                    </div>

                    <div class="form-group">
                        <span class="detail-label">活动规则:</span>
                        <span class="detail-value">${activityTemplate.rules!}</span>
                    </div>
                </div>

                <div class="col-md-6">
                
                	<div class="form-group">
                        <span class="detail-label">进入活动人次:</span>
                        <span class="detail-value">${activityInfo.visitCount!}</span>
                    </div>
                
                	<div class="form-group">
                        <span class="detail-label">参与活动人次:</span>
                        <span class="detail-value">${activityInfo.playCount!}</span>
                    </div>
                
                	<div class="form-group">
                        <span class="detail-label">中奖人次:</span>
                        <span class="detail-value">${activityInfo.givedUserCount!}</span>
                    </div>
                    
                <#if activityInfo.url??>
                    <div class="form-group">
                        <span class="detail-label">砸金蛋URL:</span>
                        <span class="detail-value">${activityInfo.url!}</span>
                    </div>

                    <div class="form-group">
                        <span class="detail-label" style="vertical-align: top;"></span>
                            <span class="detail-value ml-10">
                                    <img src="${contextPath}/manage/goldenegg/qrCode.html?activityId=${activities.activityId!}">
                                </span>
                    </div>
                </#if>
                </div>
            </div>
        </div>
    </div>

    <div class="tile mt-30" style="overflow:auto;">
        <div class="tile-header">
            流量配置
        </div>
        <div class="tile-content" style="padding: 0;">
            <table class="table table-bordered text-center" style="margin-bottom: 0;"
                   id="award-table">
                <thead>
                <tr>
                    <th>奖项</th>
                    <th>运营商</th>
                    <th>产品名称</th>
                    <th>产品编码</th>
                    <th>流量包大小</th>
                <#if cqFlag??&&cqFlag==0>
                    <th>售出价格</th>
                </#if>
                    <th>使用范围</th>
                    <th>漫游范围</th>
                    <th>投放数量</th>
                    <th>中奖概率</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                <#if cqFlag??&&cqFlag==0>
                    <td></td>
                </#if>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-30">
        <a class="btn btn-sm btn-warning"
           href="${contextPath}/manage/goldenegg/records.html?activityId=${activities.activityId!}">查看用户中奖信息</a>
    </div>
</div>


<!--[if lt IE 9]-->
<script src="${contextPath}/assets/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets/lib/es5-sham.min.js"></script>
<!--[endif]-->
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script>

	/**
     * 判断这个活动是否授权
     */
    function isAuthorized(activityId) {
        $.ajax({
           beforeSend: function (request) {
               var token1 = $(window.parent.document).find("meta[name=_csrf]")
                       .attr("content");
               var header1 = $(window.parent.document).find("meta[name=_csrf_header]")
                       .attr("content");
               request.setRequestHeader(header1, token1);
           },
           url: "${contextPath}/manage/lottery/isAuthorized.html",
           type: "POST",
           dataType: "JSON",
           data: {activityId: activityId}
           }).then(function (data) {
            var result = data.result;
            if (result == "true") {
                //已经授权过
                $("#authorize").hide();
            } else {
            	//未授权
                $("#authorize").show();
            }
        });
    }
	
	/**
     * 授权
     * 芳华，这里就要开始查询了
     */
    function startAuthorize() {
        var activityId = "${activityId!}";
        window.open("${authorizeUrl}" + activityId);
    }
	
    var data = ${activityPrizes};

    require(["common", "bootstrap"], function () {
        init();
    });

    function init() {
        var parent = $("#award-table tbody");
        parent.empty();

        data.forEach(function (award, index) {
            renderAward(award, index);
        });
        
        var activityId = "${activityId!}";
		var userType = "${activityTemplate.userType!}";
		if(userType == "1"){
			isAuthorized(activityId);
		}
    }

    function sizeFun(size) {
        if (size == null) {
            return "-";
        }
        if (size < 1024) {
            return size + "KB";
        }
        if (size >= 1024 && size < 1024 * 1024) {
            return (size * 1.0 / 1024) + "MB";
        }
        if (size >= 1024 * 1024) {
            return (size * 1.0 / 1024 / 1024) + "GB";
        }

        return size * 1.0 / 1024 + "MB";
    }

    function priceFun(price) {
        if (price == null) {
            return "-";
        }
        return (price / 100.0).toFixed(2) + "元";
    }

    /**
     *
     * @param awards
     * @param index
     */
    function renderAward(awards, index) {
        var parent = $("#award-table tbody");

        var length = awards.length;

        var awardsText = ["一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖"];
        awards.forEach(function (award, i) {
            var tr = $("<tr>");

            var isp;
            if (award.isp == "M") {
                isp = "移动";
            }
            if (award.isp == "U") {
                isp = "联通";
            }
            if (award.isp == "T") {
                isp = "电信";
            }

            var price = priceFun(award.price);
            var productSize = sizeFun(award.productSize);

            if (i == 0) {
                tr.append('<td rowspan="' + length + '" style="vertical-align: middle;">'
                          + awardsText[index] + '</td>' +
                          '<td>' + isp + '</td>' +
                          '<td>' + award.productName + '</td>' +
                          '<td>' + award.productCode + '</td>' +
                          '<td>' + productSize + '</td>' +
                          <#if cqFlag??&&cqFlag==0>
                          '<td>' + price + '</td>' +
                          </#if>
                          '<td>' + award.ownershipRegion + '</td>' +
                          '<td>' + award.roamingRegion + '</td>' +
                          '<td rowspan="' + length + '"  style="vertical-align: middle;">'
                          + award.count + '</td>' +
                          '<td rowspan="' + length + '"  style="vertical-align: middle;">'
                          + award.probability + '</td>');
            } else {
                tr.append('<td>' + isp + '</td>' +
                          '<td>' + award.productName + '</td>' +
                          '<td>' + award.productCode + '</td>' +
                          '<td>' + productSize + '</td>' +
                          <#if cqFlag??&&cqFlag==0>
                          '<td>' + price + '</td>' +
                          </#if>
                          '<td>' + award.ownershipRegion + '</td>' +
                          '<td>' + award.roamingRegion + '</td>');
            }
            parent.append(tr);
        });
    }

</script>

</body>
</html>