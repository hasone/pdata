<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-赠送用户信息</title>
    <meta name="keywords" content="流量平台 赠送用户信息"/>
    <meta name="description" content="流量平台 赠送用户信息"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <style>
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
    </style>

    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>赠送用户信息
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back" onclick="history.go(-1)">返回</a>
        </h3>
    </div>
    <div class="tools row">
        <div class="col-sm-12 dataTables_filter text-right">
            <form class="form-inline" id="table_validate"
                  action="${contextPath}/manage/giveRecordManager/giveResult.html">
                <input type="hidden" name="ruleId" id="ruleId" class="searchItem" value="${ruleId!}"/>
                <div class="form-group mr-10 form-group-sm">
                    <label for="exampleInputName2">电话号码：</label>
                    <input class="form-control searchItem" type="text" name="mobile" autocomplete="off"
                           placeholder="" maxlength="64">
                </div>

                <div class="form-group mr-10">
                    <label>赠送结果：</label>
                    <div class="btn-group btn-group-sm">
                        <input style="width: 0; font-size: 0; height: 0; opacity: 0" class="form-control searchItem"
                               name="status" value="">
                        <button type="button" class="btn btn-default" style="width: 110px">请选择</button>
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li data-value=""><a href="#">全部</a></li>
                            <li data-value="1"><a href="#">待充值</a></li>
                            <li data-value="2"><a href="#">已发送充值请求</a></li>
                            <li data-value="3"><a href="#">充值成功</a></li>
                            <li data-value="4"><a href="#">充值失败</a></li>
                        </ul>
                    </div>
                </div>

                <a class="btn btn-sm btn-warning" id="search-btn" href="javascript:void(0)">确定</a>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>

    </div>

    <div class="tile mt-30">
        <div class="tile-content" style="padding: 0;">
            <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                <div class="table-responsive">
                    <div role="table"></div>
                </div>
            </div>
        </div>
    </div>

    <div role="pagination"></div>

    <div class="modal fade dialog-sm" id="tip-dialog" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content bd-muted">
                <div class="modal-header bg-muted bd-muted">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h5 class="modal-title">提示</h5>
                </div>
                <div class="modal-body">
                    <span class="message-icon message-icon-info"></span>
                    <span class="message-content"></span>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-warning btn-sm" data-dismiss="modal">确 定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>


</div>


<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>

<script type="text/javascript">
    function checkFormValidate() {
        $("#searchForm").packValidate({
            rules: {
                mobile: {
                    digits: true,
                    maxlength: 11
                }

            }
        });
    }

    function showFailDetail() {

        $(".btnShow").click(function () {

            $.ajax({
                beforeSend: function (request) {
                    var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                    var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                    request.setRequestHeader(header1, token1);
                },
                url: '${contextPath}/manage/giveRecordManager/getRecordInfoAjax.html?id=' + $(this).attr('id'),
                success: function (data) {
                    //$('#dialogContent').html(data);
                    showTipDialog(data);
                    //alert(data);
                },
                cache: false
            });

        });
    }

    var statusFormat = function (value, column, row) {
        var msg = row.errorMessage;
        if (value == 1) {
            return "待充值";
        } else if (value == 2) {
            return "已发送充值请求";
        } else if (value == 3) {
            return "充值成功";
        } else {
            if (msg != null) {
                msg = msg.replace("\"", "&quot;");
            }
            return ['充值失败', '<a class="btn-icon icon-detail mr-5" onclick="showTip(this)" data-msg="' + msg + '">查看失败原因</a>'];
            //return ["充值失败", "<a href='#' id='"+row.id+"' class='btnShow' onclick=\"showTipDialog('" + msg +"');\">查看原因</a>"];
        }
    };

    /**
     *
     */
    function showTip(ele) {
        showTipDialog($(ele).data("msg"));
    }

    function dateFormat(date, fmt) {
        var o = {
            "M+": date.getMonth() + 1,                 //月份
            "d+": date.getDate(),                    //日
            "h+": date.getHours(),                   //小时
            "m+": date.getMinutes(),                 //分
            "s+": date.getSeconds(),                 //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    var dateFormator = function (value) {
        if (value) {
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }
    var effectTypeFormator = function (value) {
		if(value == '2'){
			return "下月生效";
		} else {
			return "立即生效";
		}
    }

    var flag = "${provinceFlag!}";
    var columns = [];
    if(flag == "chongqing") {
		columns = [{name: "sysSerialNum", text: "系统流水号", tip: true},
			{name: "mobile", text: "被赠送人号码"},
			{name: "prdName", text: "产品名称"},
			{name: "effectType", text: "生效方式", format: effectTypeFormator},
			{name: "operateTime", text: "赠送时间", format: "DateTimeFormat"},
			{name: "status", text: "赠送结果", format: statusFormat}];
    } else {
		columns = [{name: "sysSerialNum", text: "系统流水号", tip: true},
			{name: "mobile", text: "被赠送人号码"},
			{name: "prdName", text: "产品名称"},
			{name: "operateTime", text: "赠送时间", format: "DateTimeFormat"},
			{name: "status", text: "赠送结果", format: statusFormat}];
    }


    var action = "${contextPath}/manage/giveRecordManager/search.html?${_csrf.parameterName}=${_csrf.token}";

    require(["common", "bootstrap", "page/list"], function () {
        checkFormValidate();
        showFailDetail();
    });

</script>

</body>
</html>