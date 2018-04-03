<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-营销管理</title>
    <meta name="keywords" content="流量平台 营销管理"/>
    <meta name="description" content="流量平台 营销管理"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
        .table > thead > tr > th {
            line-height: initial;
        }

        .table > thead > tr > th, .table > tbody > tr > td {
            vertical-align: middle;
        }

        .icon-detail {
            color: #ca95db;
            background-image: url(${contextPath}/assets/imgs/icon-detail.png);
        }

        .icon-edit {
            color: #5faee3;
            background-image: url(${contextPath}/assets/imgs/icon-edit.png);
        }

        .icon-del {
            color: #fa8564;
            background-image: url(${contextPath}/assets/imgs/icon-del.png);
        }

        .icon-down {
            color: #aec785;
            background-image: url(${contextPath}/assets/imgs/icon-down.png);
        }

    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>红包</h3>
    </div>

    <div class="tools row">
        <div class="col-sm-2">
            <a href="${contextPath}/manage/entRedpacket/create.html">
                <button class="btn btn-danger"><i class="fa fa-plus mr-5"></i>新增红包</button>
            </a>
        </div>
        <div class="col-sm-10 dataTables_filter text-right">
            <form class="form-inline">
                <div class="form-group mr-10">
                    <label for="exampleInputName2">企业名称：</label>
                    <input type="text" class="form-control" id="exampleInputName2" placeholder="">
                </div>
                <div class="form-group">
                    <label>状态：</label>&nbsp
                    <div class="btn-group">
                        <button type="button" class="btn btn-default">上架</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a href="#">上架</a></li>
                            <li><a href="#">下架</a></li>
                            <!--<li><a href="#">宜宾</a></li>-->
                        </ul>
                    </div>
                </div>
                <button type="submit" class="btn btn-warning">确定</button>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>
    </div>

    <div class="tile mt-30">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <table class="table table-indent text-center table-bordered-noheader mb-0">
                        <thead>
                        <tr>
                            <th>企业名称</th>
                            <th>活动名称</th>
                            <th>活动开始时间/<br>活动结束时间</th>
                            <th>状态</th>
                            <th>累计参与人数</th>
                            <th>累计中奖人数</th>
                            <th>累计奖品发放/<br>总量</th>
                            <th width="20%">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>长虹集团</td>
                            <td>元旦送红包</td>
                            <td>2015-12-01 11:00:00/<br>2015-12-01 11:00:00</td>
                            <td>上架</td>
                            <td>8050</td>
                            <td>1200</td>
                            <td>1000/2000</td>
                            <td width="20%">
                                <a class="btn-icon icon-detail mr-5" href="redpacket_detail.html">详情</a>
                                <a class="btn-icon icon-edit mr-5" href="./redpacket_edit.html">编辑</a>
                                <a class="btn-icon icon-del mr-5" href="" data-target="#remove-dialog"
                                   data-toggle="modal">删除</a>
                                <a class="btn-icon icon-down" href="" data-target="#offline-dialog" data-toggle="modal">下架</a>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="data-page pull-right mt-30">
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
        <div style="display: inline-block;float: left;margin: 20px 0;">
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
                </span>&nbsp;
            <button class="btn btn-sm btn-info">确定</button>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="remove-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">删除成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="offline-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content">下架成功</span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>
    require(["common", "bootstrap"], function () {
    });
</script>
</body>
</html>