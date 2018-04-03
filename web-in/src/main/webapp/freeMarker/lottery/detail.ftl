<#import "../Util.ftl" as Util>
<#global  contextPath = rc.contextPath />

<html>

<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-添加大转盘</title>
    <meta name="keywords" content="流量平台 添加大转盘"/>
    <meta name="description" content="流量平台 添加大转盘"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <script type="text/javascript" src="${contextPath}/manage2/Js/jquery.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/assets/js/js.cookie.js"></script>
    <script type="text/javascript" src="${contextPath}/manage2/Js/bootstrap.js"></script>

    <script type="text/javascript">
        if ("ontouchend" in document) document.write("<script src='${contextPath}/manage2/assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
    </script>
<#--<script src="${contextPath}/bootstrap/js/bootstrap.min.js"></script>-->
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
    <link rel="stylesheet" href="../../manage2/assets/css/base.css"/>
    <link rel="stylesheet" href="../../manage2/assets/js/daterangepicker/daterangepicker.css"/>

    <script type="text/javascript" src="${contextPath}/manage2/Js/knockout-3.2.0.js"></script>

    <style>
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

        #startTime {
            width: 150px;
        }

        #endTime {
            width: 150px;
        }

        textarea {
            width: 323px;
            resize: none;
            height: 75px;
            outline: none;
            border: 1px solid #ccc;
        }

        .preview {
            width: 190px;
            height: 336px;
            border: 1px solid #ccc;
            margin: 0 auto;
        }

        .break-block {
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
        <h3>活动详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>


    <div class="tile mt-30" style="overflow:auto;">
        <div class="tile-header">
            基本信息
        </div>
        <div class="tile-content">
            <label class="control-label">活动id: </label><label>${(activity.activityId)!}</label><br/>
            <label class="control-label">活动名称: </label><label>${(activity.name)!}</label><br/>
            <label class="control-label">企业名称: </label><label>${(activity.enterpriseName)!}</label><br/>
            <label class="control-label">流量类型: </label><label>流量包</label><br/>
            <label class="control-label">活动状态: </label><label>
        <#if (activity.status)??>
            <#if activity.status == 0>活动未创建
            <#elseif activity.status == 1>活动未开始
            <#elseif activity.status == 2>活动进行中
            <#elseif activity.status == 3>活动结束
            </#if>
        </#if>
        </label><br/>
            <label class="control-label">活动时间: </label><label>
        <#if (activity.startTime)?? && (activity.endTime)??>
        ${(activity.startTime)?datetime} &nbsp;&nbsp;至&nbsp;&nbsp; ${(activity.endTime)?datetime}
        </#if>
        </label><br/>
            <label class="control-label">抽奖类型: </label><label>
        <#if (activity.lotteryType)??>
            <#if activity.lotteryType==0>活动期间
            <#elseif activity.lotteryType==1>每天
            </#if>
        </#if>
        </label><br/>
            <label class="control-label">抽奖次数: </label><label>${(activity.maxPlayNumber)!}次</label><br/>
            <label class="control-label">中奖次数: </label><label>${(activity.givedNumber)!}次</label><br/>
            <label class="control-label">中奖概率: </label><label>
        <#if (activity.probabilityType)??>
            <#if activity.probabilityType==1>固定概率（奖项概率固定）
            <#elseif activity.probabilityType==0>非固定概率(奖项概率自动调整)
            </#if>
        </#if>
        </label><br/>
            <label class="control-label">充值时间: </label><label>
        <#if (activity.chargeType)??>
            <#if activity.chargeType==1>立即充值
            <#elseif activity.chargeType==0>延后充值
            </#if>
        </#if>
        </label><br/>
            <label class="control-label">创建时间: </label><label>${(activity.createTime)?datetime}</label><br/>
            <label class="control-label">更新时间: </label><label>${(activity.updateTime)?datetime}</label><br/>
            <label class="control-label">大转盘url: </label><label class="break-block">
            <a href="${activity.url!}" target="_blank">${(activity.url)!}</a></label><br/>
            <label class="control-label">二维码: </label><label>
        <#if (activity.url)?? && (activity.url)!="">
            <img src="${contextPath}/manage/lotteryActivity/qrCode.html?url=${activity.url}"
                 alt="${(activity.url)!}" width="256" height="256">
        </#if>
        </label><br/>
            <label class="control-label">活动规则: </label><label
                style="word-break:break-all">${(activity.rule)!}</label><br/>
        </div>
    </div>


    <div class="tile" style="overflow:auto;">
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
                    <th>中奖概率</th>
                </tr>
                </thead>
                </tbody>
            <#if ActivityPrizeList??>
                <#list ActivityPrizeList as item>
                    <tr>
                        <td>${(item.idPrefix)!}</td>
                        <td>${(item.rankName)!}</td>
                        <td>${(item.productCode)!}</td>
                        <td>${(item.productName)!}</td>
                        <td>${(item.prizeName)!}</td>
                        <td>${(item.count)!}</td>
                        <td>${(item.probability)!'自动调整'}</td>
                    </tr>
                </#list>
            </#if>
                </tbody>
            </table>
        </div>

        <div class="mt-30">
            <a class="btn btn-sm btn-warning"
               href="${contextPath}/manage/lotteryActivity/records.html?id=${activity.id}">查看用户中奖信息</a>
        </div>

    </div>

</body>
</html>