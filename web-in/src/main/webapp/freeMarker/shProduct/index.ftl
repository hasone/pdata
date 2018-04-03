<!DOCTYPE html>
<#global  contextPath = rc.contextPath />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>流量平台-产品管理</title>
    <meta name="keywords" content="流量平台 产品管理" />
    <meta name="description" content="流量平台 产品管理" />

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/weui_toast.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Tree.min.css"/>
    <style>
        .icon-detail { color: #ca95db;background-image: url(../../assets/imgs/icon-detail.png); }
        .icon-edit { color: #5faee3;background-image: url(../../assets/imgs/icon-edit.png); }
        .icon-del { color: #fa8564;background-image: url(../../assets/imgs/icon-del.png); }
        .icon-down { color: #aec785;background-image: url(../../assets/imgs/icon-down.png); }
        .icon-online{color: #aec785;background-image: url(../../assets/imgs/icon-online.png);}
        .icon-search{color: #ca95db;background-image: url(../../assets/imgs/icon-search.png);}
        .form-control[readonly] {  background-color: #fff;  }
    </style>

    <!--[if lt IE 9]>
    <script src="../../assets/lib/html5shiv.js"></script>
    <script src="../../assets/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>

    <div class="main-container">
        <div class="tools row mt-20">
            <div class="module-header col-sm-2">
                <h3>订购组列表</h3>
            </div>
            <div class="col-sm-10 dataTables_filter text-right">
                <div class="form-inline">
                    <div class="form-group mr-10 form-group-sm">
                        <label>企业名称：</label>
                        <input type="hidden" name="name" id="name" class="form-control searchItem enterprise_autoComplete" autocomplete="off"
                           placeholder="" value=""
                           maxlength="255">
                    </div>
                    <div class="form-group mr-10 form-group-sm">
                        <label>企业编码：</label>
                        <input type="text" class="form-control searchItem" placeholder="" name="code" id="code">
                    </div>
                    <span class="search-separator mt-10"></span>
					<div class="form-group form-group-sm" id="search-time-range">
						<label>创建时间：</label>&nbsp
 						<input type="text" class="form-control search-startTime searchItem"
  							name="startTime"
							value="${(pageResult.queryObject.queryCriterias.beginTime)!}"
							id="startTime" placeholder="">~
 						<input type="text" class="form-control search-endTime searchItem" name="endTime"
                           	id="endTime"
                           	value="${(pageResult.queryObject.queryCriterias.endTime)!}"
                           	placeholder="">

                	</div>
                    <a class="btn btn-sm btn-warning" id="search-btn">确定</a>
                    <!-- <a onclick="createFile()" class="btn btn-sm btn-warning">报表导出</a> -->
                </div>
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
    </div>
    
    <div class="modal fade dialog-sm" id="tip-dialog">
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


    <script src="../../assets/lib/require.js"></script>
    <script src="../../assets/js/config.js"></script>
    <script>
    
    var nameFormat = function (value, column, row) {
        return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?cqSync=1&&changeTag=1&&id=" + row.id + "'>" + row.name + "</a>";
    };

    var codeFormat = function (value, column, row) {
        if (row.code != null) {
            return "<a href='${contextPath}/manage/enterprise/showEnterpriseDetail.html?cqSync=1&&changeTag=1&&id=" + row.id + "'>" + row.code + "</a>";
        } else {
            return "";
        }

    };
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
        	console.log(value);
            return dateFormat(new Date(value), "yyyy-MM-dd hh:mm:ss");
        }
        return "";
    }
        var action = "${contextPath}/sh/product/enterpriseList.html";
        var columns = [{name: "name", text: "企业名称", tip: true},
            {name: "code", text: "企业编码",tip: true},
            {name: "enterpriseCity", text: "所属地区",tip: true},
            {name: "createTime", text: "创建时间", format: "DateTimeFormat"},
            {name: "op", text: "操作", format: function(value, column, row){
                return ["<a href='${contextPath}/sh/product/entOrderList.html?entId=" + row.id + "'>详情</a>",
                		"<a class='btn-icon icon-sync mr-5 sync-btn' onclick='sync("+ row.id +")'>同步产品</a>"]
            }}
        ];
         
        require(["page/list","common", "bootstrap","daterangepicker"], function() {
        	initSearchDateRangePicker();
        });
        
        function sync(id){
        	ajaxData("${contextPath}/sh/product/syncProduct.html", {entId:id}, function(ret){
                if(ret && ret.success){
                	$(".message-content").html(ret.success);
                	$('#tip-dialog').modal('show');
                }else{
                	$(".message-content").html('');
                	$('#tip-dialog').modal('show');
                }
            });
        }
        function initSearchDateRangePicker() {
            var ele = $('#search-time-range');

            var startEle = $('#startTime');
            var endEle = $('#endTime');

            ele.dateRangePicker({
                                    separator: ' ~ ',
                                    showShortcuts: true,
                                    shortcuts: {
                                        'prev': ['week', 'month']
                                    },
                                    beforeShowDay: function (t) {
                                        var valid = t.getTime() < new Date().getTime();
                                        return [valid, '', ''];
                                    },
                                    customShortcuts: [
                                        {
                                            name: '半年内',
                                            dates: function () {
                                                var end = new Date();
                                                var start = new Date();
                                                start.setDate(start.getDate() - 182);
                                                return [start, end];
                                            }
                                        },
                                        {
                                            name: '一年内',
                                            dates: function () {
                                                var end = new Date();
                                                var start = new Date();
                                                start.setDate(start.getDate() - 360);
                                                return [start, end];
                                            }
                                        }
                                    ],
                                    getValue: function () {
                                        if (startEle.val() && endEle.val()) {
                                            return startEle.val() + ' ~ ' + endEle.val();
                                        } else {
                                            return '';
                                        }
                                    },
                                    setValue: function (s, s1, s2) {
                                        startEle.val(s1);
                                        endEle.val(s2);
                                    }
                                });
        }
        function createFile() {
            var code = document.getElementById('code').value;
            code = code.replace("%", "%25");
            var name = document.getElementById('name').value;
            name = name.replace("%", "%25");
            var startTime = document.getElementById('startTime').value;
            var endTime = document.getElementById('endTime').value;
            window.open(
                    "${contextPath}/manage/enterprise/creatCSVfile.html?code=" + code + "&&name=" + name
                    + "&&startTime=" + startTime + "&&endTime="
                    + endTime);
        }

    </script>
</body>
</html>