<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量余额</title>

    <link rel="stylesheet" href="${contextPath}/assets_individual/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/platform/iconfont.css"/>

    <link rel="stylesheet" href="${contextPath}/assets_individual/theme/default/theme.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets_individual/css/main.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets_individual/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets_individual/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mb-20">
        <h3>业务查询-流量余额-套餐流量查询</h3>
    </div>
    <div class="form-group">
        <span>当前账户:</span>
        <span>${mobile!}</span>
    </div>
    <div class="mt-30">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#thismonth" role="tab" data-toggle="tab">当月</a></li>
            <li role="presentation"><a href="#aug" role="tab" data-toggle="tab">2016年08月</a></li>
            <li role="presentation"><a href="#jul" role="tab" data-toggle="tab">2016年07月</a></li>
            <li role="presentation"><a href="#jun" role="tab" data-toggle="tab">2016年06月</a></li>
            <li role="presentation"><a href="#may" role="tab" data-toggle="tab">2016年05月</a></li>
            <li role="presentation"><a href="#apr" role="tab" data-toggle="tab">2016年04月</a></li>
            <li role="presentation"><a href="#more" role="tab" data-toggle="tab">更多>></a></li>
        </ul>

        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="thismonth">
                <div class="tile gray-border tile-noTopBorder">
                    <div class="mt-20">
                        <div class="form-inline">
                            <h5>截止至2016-09-08 14:39:03，您本月的套餐流量使用量信息如下:</h5>
                            <hr>
                            <div class="mt-20 mb-30">
                                <span>手机上网自选100元包（3GB）（上月结转）：</span>
                                <span class="pull-right">剩余<span class="green-text-normal">2</span>GB<span
                                        class="green-text-normal">64.6</span>MB/总量<span
                                        class="green-text-normal">3</span>GB</span>
                                <div class="progress">
                                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40"
                                         aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                                        <span class="sr-only">40% Complete (success)</span>
                                    </div>
                                </div>
                            </div>
                            <div class="mb-30">
                                <span>手机上网自选100元包（3GB）（本月）：</span>
                                <span class="pull-right">剩余<span class="green-text-normal">2</span>GB<span
                                        class="green-text-normal">80.6</span>MB/总量<span
                                        class="green-text-normal">3</span>GB</span>
                                <div class="progress">
                                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="20"
                                         aria-valuemin="0" aria-valuemax="100" style="width: 20%">
                                        <span class="sr-only">20% Complete (success)</span>
                                    </div>
                                </div>
                            </div>
                            <div class="mb-30">
                                <span>办3GB流量包送1GB流量（本月）：</span>
                                <span class="pull-right">剩余<span class="green-text-normal">1</span>GB<span
                                        class="green-text-normal">64.6</span>MB/总量<span
                                        class="green-text-normal">3</span>GB</span>
                                <div class="progress">
                                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="60"
                                         aria-valuemin="0" aria-valuemax="100" style="width: 60%">
                                        <span class="sr-only">60% Complete (success)</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" id="aug">
                <div class="tile gray-border tile-noTopBorder">
                    <div class="mt-20">
                        <div class="form-inline">
                            <h5>您2016年08月的套餐流量余量信息如下:</h5>
                            <hr>
                            <div class="mt-20 mb-30">
                                <span>国内数据流量：</span>
                                <span class="pull-right">剩余<span class="green-text-normal">2</span>GB<span
                                        class="green-text-normal">64.6</span>MB/总量<span
                                        class="green-text-normal">3</span>GB</span>
                                <div class="progress">
                                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="50"
                                         aria-valuemin="0" aria-valuemax="100" style="width: 50%">
                                        <span class="sr-only">50% Complete (success)</span>
                                    </div>
                                </div>
                            </div>
                            <div class="mb-30">
                                <span>国内数据流量－结转:</span>
                                <span class="pull-right">剩余<span class="green-text-normal">2</span>GB<span
                                        class="green-text-normal">80.6</span>MB/总量<span
                                        class="green-text-normal">3</span>GB</span>
                                <div class="progress">
                                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="10"
                                         aria-valuemin="0" aria-valuemax="100" style="width: 10%">
                                        <span class="sr-only">10% Complete (success)</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--[if lt IE 9]>
<script src="${contextPath}/assets_individual/lib/es5-shim.min.js"></script>
<script src="${contextPath}/assets_individual/lib/es5-sham.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script>
    Object.getPrototypeOf = function getPrototypeOf(object) {
        return object.__proto__;
    };
</script>
<![endif]-->

<script>
    if (window.ActiveXObject) {
        var reg = /10\.0/;
        var str = navigator.userAgent;
        if (reg.test(str)) {
            Object.getPrototypeOf = function getPrototypeOf(object) {
                return object.__proto__;
            };
        }
    }
</script>


<script src="${contextPath}/assets_individual/lib/require.js"></script>
<script src="${contextPath}/assets_individual/dist/config.js"></script>
<script>
    var messageBox, closeConfirmBox;
    require(["react", "MessageBox", "common", "bootstrap", "daterangepicker"], function (React, ReactDOM, ListData, MessageBox) {

    });


    /**
     * 监听
     */
    function listeners() {

        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            $("#nav-text").html($(this).text());
        });

    }

</script>
</body>
</html>