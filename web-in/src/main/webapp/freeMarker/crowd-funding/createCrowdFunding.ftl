<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量众筹平台</title>
    <meta name="keywords" content="流量众筹平台"/>
    <meta name="description" content="流量众筹平台"/>

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

        .btn.btn-danger {
            z-index: 1041;
        }

        #accordion {
            position: absolute;
        }

        a.btn-mode:active, a.btn-mode:focus, a.btn-mode:hover, a.btn-mode:link {
            text-decoration: none;
            color: #fff;
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
            <img class="pull-left" src="${contextPath}/assets/imgs/logo.png"><span>流量众筹平台</span>
        </div>
        <ul>
            <li>
                <a href="javascript:void(0);">
                    <i class="menu-icon icon-qygl">
                    </i>
                    营销管理
                </a>
            </li>
            <li>
                <a href="javascript:void(0);" style="padding-left: 56px;">
                    流量众筹
                </a>
            </li>
        </ul>
        <div class="mask introduction-wrap" style="top: 160px">
            <div class="arrow arrow-up"></div>
            <div class="content" style="width: 200px;">在营销管理下的流量众筹中，可创建流量众筹活动</div>
        </div>
    </div>
</div>

<div class="desktop-wrap client">
    <div class="main-container">
        <div class="module-header mt-30 mb-20">
            <h3>流量众筹</h3>
        </div>

        <div class="tools row">
            <div class="col-sm-2" style="position: relative;">
                <a href="javascript:void(0);">
                    <button class="btn btn-danger">新建流量众筹</button>
                </a>
                <div class="mask introduction-wrap" style="top: -13px; left: 152px; width: 204px;">
                    <div class="arrow arrow-left pull-left" style="margin-top: 17px;"></div>
                    <div class="content text-center" style="height: 5rem; width: 13rem;">新增众筹活动</div>
                </div>
            </div>

            <div class="col-sm-10 dataTables_filter text-right">
                <form class="form-inline searchForm" id="table_validate" method="POST">
                    <div class="form-group mr-10">
                        <label>活动名称</label>&nbsp;
                        <input type="text" class="form-control">
                    </div>
                    <div class="form-group mr-10">
                        <label>状态</label>&nbsp;
                        <select class="form-control">
                            <option>全部</option>
                        </select>
                    </div>
                    <div class="form-group mr-10">
                        <label>创建时间</label>&nbsp;
                        <input type="text" class="form-control">&nbsp;-&nbsp;<input type="text" class="form-control">
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
                                <th>活动名称</th>
                                <th>开始时间</th>
                                <th>结束时间</th>
                                <th>状态</th>
                                <th>众筹成功人数</th>
                                <th>众筹是否成功</th>
                                <th>创建时间</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>众筹接口专用活动</td>
                                <td>2017-02-17 15:50:00</td>
                                <td>2018-12-29 15:40:00</td>
                                <td>进行中</td>
                                <td>1234</td>
                                <td>否</td>
                                <td>2017-02-17 15:41:32</td>
                                <td><a class="mr-5" href="javascript:void(0);">下架</a><a href="javascript:void(0);">详情</a></td>
                            </tr>
                            <tr>
                                <td>抢红包活动</td>
                                <td>2017-02-16 15:50:00</td>
                                <td>2018-12-25 15:40:00</td>
                                <td>已保存</td>
                                <td>0</td>
                                <td>否</td>
                                <td>2017-02-16 15:41:32</td>
                                <td><a class="mr-5" href="javascript:void(0);">编辑</a><a class="mr-5" href="javascript:void(0);">详情</a><a href="javascript:void(0);">提交审批</a></td>
                            </tr>
                            <tr>
                                <td>大转盘活动</td>
                                <td>2017-02-15 15:55:00</td>
                                <td>2018-12-25 15:40:00</td>
                                <td>审核结束</td>
                                <td>0</td>
                                <td>否</td>
                                <td>2017-02-15 15:41:32</td>
                                <td><a class="mr-5" href="javascript:void(0);">上架</a><a href="javascript:void(0);">详情</a></td>
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
        <a class="mask btn-mode btn-known" href="approvalCrowdFunding.html" style="top: 10%; display: block;">下一步</a>
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
                url: "${contextPath}/manage/crowdFunding/maskingAjax.html?${_csrf.parameterName}=${_csrf.token}",
                type: "POST",
                data: {
                    flag: flag
                },
                dataType: "JSON",
                success: function () {
                    $(".mask").hide();
                    window.location.href = "${contextPath}/manage/index.html?needmask=no";
                }
            });
        })
    }
</script>

</body>
</html>