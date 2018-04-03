<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0,minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>四川流量平台-查看明细</title>
    <meta name="keywords" content="四川流量平台 查看明细"/>
    <meta name="description" content="四川流量平台 查看明细"/>

    <link rel="stylesheet" href="${contextPath}/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/cmui.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/base.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/css/innerbase.min.css"/>

    <link rel="stylesheet" href="${contextPath}/assets/theme/default/Select.min.css"/>
    <link rel="stylesheet" href="${contextPath}/assets/lib/daterangepicker/daterangepicker.css"/>
    <!-- easy-ui-->
    <link rel="stylesheet" type="text/css" href="${contextPath}/assets/easyui/themes/default/easyui.css">


    <!--[if lt IE 9]>
    <script src="${contextPath}/assets/lib/html5shiv.js"></script>
    <script src="${contextPath}/assets/lib/respond.min.js"></script>
    <![endif]-->

    <style>
        .text-center th {
            text-align: center;
        }

        .table tbody > tr > td:last-child, .table tbody > tr > td:last-child *, .table thead > tr > th:last-child {
            max-width: 10000px;
            overflow: visible;
        }

        .table tbody > tr > td {
            vertical-align: middle;
        }

        .table tbody > tr > td input {
            margin: 0 auto;
        }

        h5 {
            line-height: 30px;
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="module-header mt-30 mb-20">
        <h3>
            限额详情
            <a class="btn btn-primary btn-sm pull-right btn-icon icon-back"
               onclick="window.location.href='${contextPath}/manage/supplierLimit/index.html'">返回</a>
        </h3>
    </div>
    <h5>供应商流量控制:</h5>
    <div>
    <#--<div class="dataTables_filter1 text-left">-->
    <#--<form class="form-inline">-->
    <#--<input type="text" class="form-control hidden" name="supplierId"-->
    <#--value="${supplierId!}"-->
    <#--autocomplete="off" maxlength="255"/>-->
    <#--<div class="form-group hidden">-->
    <#--<a tpye="submit" class="btn btn-warning btn-sm" id="search-btn1">确定</a>-->
    <#--</div>-->
    <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
    <#--</form>-->
    <#--</div>-->
        <div class="tile mt-15">
            <!--放入列表-->
            <div id="listData1" class="text-center">
                <div class="tile">
                    <div class="tile-content" style="padding:0;">
                        <div role="tabpanel" class="tab-pane active table-responsive" id="table_wrap1">
                            <div class="table-responsive">
                                <div class="table-responsive">
                                    <table class="cm-table table table table-indent text-center table-bordered-noheader mb-0">
                                        <thead>
                                        <tr>
                                            <th>供应商名称</th>
                                            <th>每日金额上限(万元)</th>
                                            <th>是否控制</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <h5 class="mt-15">BOSS产品流量控制:</h5>
    <div class="mt-15">
        <div class="dataTables_filter1 text-left">
            <form class="form-inline">
                <input type="text" class="form-control searchItem hidden" name="supplierId"
                       value="${supplierId!}"
                       autocomplete="off" maxlength="255"/>
                <div class="form-group hidden">
                    <a tpye="submit" class="btn btn-warning btn-sm" id="search-btn">确定</a>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
        <div class="tile">
            <!--放入列表-->
            <div id="listData2" class="text-center">
                <div class="tile">
                    <div class="tile-content" style="padding:0;">
                        <div class="table-responsive">
                            <div role="table" id="table"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="modal fade dialog-sm" id="tips-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="tips"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="tips-btn">确 定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade dialog-sm" id="del-dialog">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="delMsg"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" data-dismiss="modal" id="delOk">确 定</button>
                <button class="btn btn-warning btn-sm" data-dismiss="modal">取 消</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade dialog-sm" id="subject-dialog" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content bd-muted">
            <div class="modal-header bg-muted bd-muted">
                <h5 class="modal-title">提示</h5>
            </div>
            <div class="modal-body">
                <span class="message-icon message-icon-info"></span>
                <span class="message-content" id="subject-tips"></span>
            </div>
            <div class="modal-footer">
                <button class="btn btn-warning btn-sm" id="subject-btn" data-dismiss="modal">确 定</button>
            </div>
        </div>
    </div>
</div>
</body>
<script src="${contextPath}/assets/lib/require.js"></script>
<script src="${contextPath}/assets/js/config.js"></script>
<script>

    var limitMoneyFormat = function (value, column, row) {
        if (value >= 0 && row.limitMoneyFlag != -1) {
//        if (!!row.limitMoney && row.limitMoney != -1) {
            return (parseFloat(value) / 1000000).toFixed(4);
        } else {
            return "-1";
        }
    };

    var limitMoneyFlagFormat = function (value, column, row) {
        if (!!value && value == 1) {
            return "是";
        } else {
            return "否";
        }
    };

    var priceFormat = function (value, column, row) {
        if(!!value && value>0){
            return (parseFloat(value) / 100).toFixed(2);
        }
        return "-";
    }

    var opFormat2 = function (value, column, row) {
        return ["<a href='javascript:void(0)' data-edit=true onclick=editProduct("+row.id+",this)>编辑</a><span class='error-tip ml-10'></span>"];
    };

    var columns = [{name: "name", text: "BOSS产品名称"},
        {name: "size", text: "产品大小"},
        {name: "price", text: "采购价格(元)", format: priceFormat},
        {name: "roamingRegion", text: "使用范围"},
        {name: "ownershipRegion", text: "漫游范围"},
        {name: "limitMoney", text: "每日金额上限（万元）", format: limitMoneyFormat},
        {name: "limitMoneyFlag", text: "是否控制", format: limitMoneyFlagFormat},
        {name: "", text: "操作", format: opFormat2}];

    var action = "${contextPath}/manage/supplierLimit/getSupplierProducts.html?${_csrf.parameterName}=${_csrf.token}";


    require(["react", "react-dom", "page/list", "common", "bootstrap"], function () {
        getSupplierAjax();
        listeners();
    });

    function listeners() {
        $("#search-btn").click();
    }

    function selectChange(ele){
        $(ele).parent().prev().find("input").val("");
    }

    function validateNum(){
        $("input[name='limitMoney']").on("input", function() {
            var val = $(this).val();
            val = val.replace(/[^0-9\.]+/g, "");
            $(this).val(val);
        });
        $("input[name='limitMoney']").on('blur',function(){
            if($(this).val().length <= 0){
                $(this).val("");
            }else{
                $(this).val(limitMoneyFormat(parseFloat($(this).val()) * 1000000));
            }
        });
    }

    function getSupplierAjax() {
        var supplierId = "${supplierId}";
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/supplierLimit/getSupplier.html",
            data: {
                supplierId: supplierId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                if (data.suppliers) {
                    setSupplierTable(data.suppliers);
                }
            },
            error: function () {
            }
        });
    }

    function setSupplierTable(records) {
        if (records && records.length > 0) {
            for (var i = 0; i < records.length; i++) {
                var data = records[i];
                appendSupplier({
                    id: data.id,
                    name: data.name,
                    limitMoney: data.limitMoney,
                    limitMoneyFlag: data.limitMoneyFlag
                });
            }
        }
    }

    function appendSupplier(data) {
        var parent = $("#listData1 tbody");
        var trs;
        trs = $('<tr data-id="' + data["id"] + '">' +
                '<td>' + data["name"] + '</td>' +
                '<td>' + limitMoneyFormat(data["limitMoney"], 0, 0) + '</td>' +
                '<td>' + limitMoneyFlagFormat(data["limitMoneyFlag"], 0, 0) + '</td>' +
                '<td>' +
                '<a data-edit=true onclick=editSupplier(this)>编辑</a><span class="error-tip ml-10"></span>' + '</td>' +
                '</tr>');
        parent.append(trs);
    }

    function editSupplier(ele) {
        //编辑供应商限额信息
        var editFlag = $(ele).data("edit");
        if (editFlag) {
            $(ele).data("edit", false);
            $(ele).html('保存');
            var limitMoney = $(ele).parent().parent().find("td:nth-child(2)");
            var limitMoneyHTML = limitMoney.html();
            limitMoney.html("<input type='text' name='limitMoney' class='form-control' value=" + limitMoneyHTML + ">");
            var limitMoneyFlag = $(ele).parent().parent().find("td:nth-child(3)");
            var limitMoneyFlagHTML = limitMoneyFlag.html();
            var option;
            if (limitMoneyFlagHTML == "是") {
                option = "<option value='1' selected>是</option><option value='0'>否</option>"
            } else {
                option = "<option value='1'>是</option><option value='0' selected>否</option>"
            }
            limitMoneyFlag.html("<select type='text' name='limitMoneyFlag' class='form-control' onchange='selectChange(this)'>" + option + "</select>");
            validateNum();
        } else {
            saveSupplierLimit(ele);
        }

    }

    //保存供应商流量控制
    function saveSupplierLimit(ele) {
    <#-- 供应商流量控制保存，这里是否控制：是--1，否---0,，limitMoneyFlag==1,limitMoney*1000000, limitMoneyFlag==0,limitMoney= -1 -->
        var limitMoney = $(ele).parent().parent().find("input[name='limitMoney']").val();
        var limitMoneyFlag = parseInt($(ele).parent().parent().find("select[name='limitMoneyFlag']").val());
        if (limitMoneyFlag) {
            if(limitMoney){
                limitMoney = parseFloat(limitMoney) * 1000000;
            }else{
                $("#subject-tips").html("如若选择需要控制，请输入每日金额上限！");
                $("#subject-dialog").modal("show");
                return;
            }
        } else {
            limitMoney = -1;
        }
        var supplier = {
            id: "${supplierId!}",
            limitMoney: limitMoney,
            limitMoneyFlag: limitMoneyFlag
        };

        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/supplierLimit/saveSupplierLimitAjax.html",
            data: {
                supplier: JSON.stringify(supplier)
            },
            dataType: "json", //指定服务器的数据返回类型
            success: function (data) {
                $("#tips").html(data.msg);
                $("#tips").data("value", data.result);
                $("#tips-dialog").modal("show");
                if (data.result !== "false") {
                    $(ele).data("edit", true);
                    $(ele).html('编辑');
                    $(ele).parent().parent().find("td:nth-child(2)").html(limitMoneyFormat(limitMoney, 0, 0));
                    $(ele).parent().parent().find("td:nth-child(3)").html(limitMoneyFlagFormat(limitMoneyFlag, 0, 0));
                }
            },
            error: function () {
                $("#tips").html("网络出错，请重新尝试");
                $("#tips").data("value", "false");
                $("#tips-dialog").modal("show");
            }
        });
    }

    //编辑BOSS产品限额信息
    function editProduct(id,ele) {
        var editFlag = $(ele).data("edit");
        if (editFlag) {
            $(ele).data("edit", false);
            $(ele).html('保存');
            var limitMoney = $(ele).parent().parent().find("td:nth-child(6)");
            var limitMoneyHTML = limitMoney.html();
            limitMoney.html("<input type='text' name='limitMoney' class='form-control' value=" + limitMoneyHTML + ">");
            var limitMoneyFlag = $(ele).parent().parent().find("td:nth-child(7)");
            var limitMoneyFlagHTML = limitMoneyFlag.html();
            var option;
            if (limitMoneyFlagHTML == "是") {
                option = "<option value='1' selected>是</option><option value='0'>否</option>"
            } else {
                option = "<option value='1'>是</option><option value='0' selected>否</option>"
            }
            limitMoneyFlag.html("<select type='text' name='limitMoneyFlag' class='form-control' onchange='selectChange(this)'>" + option + "</select>");
            validateNum();
        } else {
            saveProductLimit(id,ele);
        }
    }

    //新增供应商产品付款记录
    function saveProductLimit(id,ele) {
    <#-- 供应商流量控制保存，这里是否控制：是--1，否---0,，limitMoneyFlag==1,limitMoney*1000000, limitMoneyFlag==0,limitMoney= -1 -->
    <#-- id是那一列的id -->
        var limitMoney = $(ele).parent().parent().find("input[name='limitMoney']").val();
        var limitMoneyFlag = parseInt($(ele).parent().parent().find("select[name='limitMoneyFlag']").val());
        if (limitMoneyFlag) {
            if(limitMoney){
                limitMoney = parseFloat(limitMoney) * 1000000;
            }else{
                $("#subject-tips").html("请输入每日金额上限！");
                $("#subject-dialog").modal("show");
                return;
            }
        } else {
            limitMoney = -1;
        }

        var supplierProduct = {
            id: id,
            supplierId: "${supplierId!}",
            limitMoney: limitMoney,
            limitMoneyFlag: limitMoneyFlag
        };

        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "POST",
            url: "${contextPath}/manage/supplierLimit/saveProductSupplierLimitAjax.html",
            data: {
                supplierProduct: JSON.stringify(supplierProduct)
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                $("#tips").html(data.msg);
                $("#tips").data("value", data.result);
                $("#tips-dialog").modal("show");
                if (data.result !== "false") {
                    $(ele).data("edit", true);
                    $(ele).html('编辑');
                    $(ele).parent().parent().find("td:nth-child(6)").html(limitMoneyFormat(limitMoney, 0, 0));
                    $(ele).parent().parent().find("td:nth-child(7)").html(limitMoneyFlagFormat(limitMoneyFlag, 0, 0));
                }
            },
            error: function () {
                $("#tips").html("网络出错，请重新尝试");
                $("#tips").data("value", "false");
                $("#tips-dialog").modal("show");
            }
        });
    }

</script>
</html>