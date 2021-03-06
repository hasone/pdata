<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>广东流量卡平台</title>
    <meta name="keywords" content="广东流量卡平台"/>
    <meta name="description" content="广东流量卡平台"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/index.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/mask.min.css"/>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
    <style>
        .cm-accordion-menu ul li {
            z-index: 1041;
        }

        #accordion {
            position: absolute;
        }

        .btn-icon{
            padding-left: 0;
        }

        a.btn-mode:active, a.btn-mode:focus, a.btn-mode:hover, a.btn-mode:link {
            text-decoration: none;
            color: #fff;
        }

        .table-responsive{
            overflow-x: visible;
        }
    </style>
</head>
<body>
<!-- header start -->
<div class="navbar-fixed-top navbar">
    <div class="navbar-header navbar-right user-info">
        <div class="photo">
            <img src="${contextPath}/assets/imgs/photo.png">
            <span class="ml-10 mr-10">欢迎您，<span id="userName">企业关键人</span></span>
        </div>
            <span class="logout">
                <a class="" href="" title="退出"></a>
            </span>
    </div>
        <span class="sm-navbar">

        </span>
</div><!-- header end -->

<div class="section client">
    <div id="accordion" class="cm-accordion-menu black">
        <div class="menu-header">
            <img class="pull-left" src="${contextPath}/assets/imgs/logo.png"><span>广东流量卡平台</span>
        </div>
        <ul>
            <li>
                <a href="javascript:void(0);">
                    <i class="menu-icon icon-qygl">
                    </i>
                    审批管理
                </a>
            </li>
            <li>
                <a href="javascript:void(0);" style="padding-left: 56px;">
                    激活审批
                </a>
            </li>
        </ul>
        <div class="mask introduction-wrap" style="top: 160px">
            <div class="arrow arrow-up"></div>
            <div class="content" style="width: 200px;">管理员在激活审批中对激活申请进行审批，审批通过卡激活成功，可正常投入使用</div>
        </div>
    </div>
</div>

<div class="desktop-wrap client">
    <div class="main-container">
        <div class="module-header mt-30 mb-20">
            <h3>激活审批列表</h3>
        </div>

        <div class="tools row">
            <div class="col-sm-2">
            </div>
            <div class="col-sm-10 dataTables_filter text-right">
                <form class="form-inline searchForm" id="table_validate" method="POST">
                    <div class="form-group mr-10">
                        <label>企业名称</label>&nbsp;
                        <select class="form-control">
                            <option></option>
                        </select>
                    </div>
                    <div class="form-group mr-10">
                        <label>卡批次号</label>&nbsp;
                        <input type="text" class="form-control">
                    </div>
                    <div class="form-group mr-10">
                        <label>审批状态</label>&nbsp;
                        <select class="form-control">
                            <option>全部</option>
                        </select>
                    </div>
                    <a class="btn btn-warning">查 询</a>
                </form>
            </div>
        </div>
        <div class="tile mt-30" style="position: relative;z-index: 1041;">
            <div class="tile-content" style="padding: 0;">
                <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                    <div class="table-responsive">
                        <table class="table table-indent text-center table-bordered-noheader mb-0">
                            <thead>
                            <tr>
                                <th>企业名称</th>
                                <th>卡批次号</th>
                                <th>制卡数量</th>
                                <th>激活数量</th>
                                <th>流量大小</th>
                                <th>审批状态</th>
                                <th width="96px" style="overflow: visible;position: relative;">操作
                                    <div class="mask introduction-wrap" style="top: -105px;">
                                        <div class="content" style="display: block; height: 5.5rem; margin-left: -55px; padding: 2px 0">管理员激活审批，审批通过<br>激活成功，卡正常使用</div>
                                        <div class="arrow arrow-down"></div>
                                    </div>
                                </th>
                                <th>申请时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>长虹集团</td>
                                <td>001</td>
                                <td>1000</td>
                                <td>1000</td>
                                <td>1G</td>
                                <td>审批中</td>
                                <td><a class="btn-icon icon-detail" href="javascript:void(0);">审批</a></td>
                                <td>2017-08-07 11:00:00</td>
                            </tr>
                            <tr>
                                <td>五粮液集团</td>
                                <td>002</td>
                                <td>1000</td>
                                <td>1000</td>
                                <td>2G</td>
                                <td>已通过</td>
                                <td><a class="btn-icon icon-detail" href="javascript:void(0);">详情</a></td>
                                <td>2017-08-04 12:00:00</td>
                            </tr>
                            <tr>
                                <td>奥克斯集团</td>
                                <td>003</td>
                                <td>1000</td>
                                <td>1000</td>
                                <td>500M</td>
                                <td>已驳回</td>
                                <td><a class="btn-icon icon-detail" href="javascript:void(0);">详情</a></td>
                                <td>2017-08-01 09:00:00</td>
                            </tr>
                            <tr>
                                <td>日立集团</td>
                                <td>003</td>
                                <td>1000</td>
                                <td>1000</td>
                                <td>500M</td>
                                <td>审批失败</td>
                                <td><a class="btn-icon icon-detail" href="javascript:void(0);">详情</a></td>
                                <td>2017-08-01 08:00:00</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="clearfix">
            <div class="data-page pull-right mt-15">
                <ul class="pagination" style="float: left;">
                    <li class="prev disabled">
                        <a href="#">
                            <i class="fa fa-angle-left"></i>
                        </a>
                    </li>
                    <li class="active"><a href="#">1</a></li>
                    <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#">4</a></li>
                    <li><a href="#">5</a></li>
                    <li><a href="#">6</a></li>
                    <li class="more"><span>...</span></li>
                    <li class="next">
                        <a href="#">
                            <i class="fa fa-angle-right"></i>
                        </a>
                    </li>
                </ul>
                <div style="display: inline-block;float: left;margin: 25px 0;">
                    <span class="ml-10">共100页</span>&nbsp;
                <span class="page-code">
                    每页
                        <select>
                            <option>10</option>
                            <option>50</option>
                            <option>100</option>
                        </select>
                    条
                </span>&nbsp;
                <span class="page-code">
                    到第 <input type="text" style="width: 40px;"/>页
                </span>
                </div>
            </div>
        </div>
        <a class="mask btn-mode btn-known" href="${contextPath}/manage/mdrc/masking/cardStatisticsIntro.html" style="top: 10%; display: block;">下一步</a>
    </div>
</div>

<div class="mask">
    <div class="modal-backdrop fade in"></div>

    <div class="btn-close">
        <img src="${contextPath}/assets/imgs/icon-close1.png"/>
    </div>

    <div class="modal-thx" hidden>
        <img src="${contextPath}/assets/imgs/thx.png"/>
        <h4>感谢您的收看！</h4>
        <div style="margin-top: 38px;">
            <div class="pull-left btn-mode btn-next">下次仍然说明</div>
            <div class="pull-right btn-mode btn-never">不再提醒</div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script src="${contextPath}/assets/js/index-side.js"></script>
<script>
    require(["common"], function () {
        listeners();
    });

    function listeners(){
        //关闭按钮控制显示是否提醒确认框
        $(".btn-close").on("click", function () {
            $(".introduction-wrap").hide();
            $(".btn-known").hide();
            $(".modal-backdrop").css("z-index", "1041");
            $(".modal-thx").show();
        });

        //是否再次提醒框
        $(".btn-next,.btn-never").on("click", function () {
            var flag = 1;
            if ($(this).hasClass("btn-next")) {
                flag = 1;
            } else {
                flag = 0;
            }
            $.ajax({
                url: "${contextPath}/manage/mdrc/masking/maskingAjax.html?${_csrf.parameterName}=${_csrf.token}",
                type: "POST",
                data: {
                    flag: flag
                },
                dataType: "JSON",
                success: function () {
                    $(".mask").hide();
                    window.location.href = "${contextPath}/manage/index.html?needmask=no";//跳转到真实的URL
                }
            });
        })
    }
</script>
</body>
</html>