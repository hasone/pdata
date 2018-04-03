<!DOCTYPE html>

<#global  contextPath = rc.contextPath />

<html>

<head>
    <title>
    </title>

    <meta charset="UTF-8">

</head>
<body>
<form id="createForm" method="post">
    <table class="table table-bordered table-hover definewidth m10"
           id="table_validate">
        <tr>
            <th width="15%">企业名称</th>
            <td><select name="entId" id="enter_name" onchange="chooseEnter()" style="width:300px;">
            <#list enterprises as e>
                <option value="${e.id}">${e.name}</option>
            </#list>
            </select></td>
        </tr>

        <tr>
            <td width="15%" class="tableleft">企业编码</td>
            <td>
                <select id="enter_code" name="enter_code" disabled style="width:300px;">
                <#list enterprises as e>
                    <option>${e.code}</option>
                </#list>
                </select>
            </td>
        </tr>
        <tr>
            <td width="12%" class="tableleft">产品名称</td>
            <td>
                <select name="prdId" id="product_name" style="width:300px;">
                <#if (products?size>0) >
                    <#list products as p>
                        <option value="${p.id}">${p.name}</option>
                    </#list>
                <#else>
                    <option value=''>---该企业在Boss处没有查询到产品---</option>
                </#if>
                </select>
            </td>
        </tr>

        <tr>
            <th></th>

            <td colspan="3">


                <button type="button" class="btn btn-primary" type="button" onclick="doSubmit()">查询</button>
                &nbsp;&nbsp;
                <span style="color: red" id="error_msg"><#if returnMsg??>${returnMsg}</#if></span>
            </td>
        </tr>

    </table>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>

<!-- Modal -->
<div class="modal hide fade" id="myModal" tabindex="-1" role="dialog">
    <div class="modal-header">
        <button class="close" type="button" data-dismiss="modal">×</button>
        <h5 id="myModalLabelmsg">提示信息</h5>
    </div>
    <div class="modal-body text-center">
        <h3 id="myModalLabel">Modal header</h3>
        <br/>
        <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>

    </div>
</div>

<script type="text/javascript">
    function chooseEnter() {
        var index = jQuery("#enter_name option:selected").index();
        $('#enter_code')[0].selectedIndex = index;
        var enterId = jQuery("#enter_name option:selected").val();
        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/product/getProductsAjax.html",
            data: {
                enterpriseId: enterId
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var listProducts = data.products;
                $("#product_name").empty();
                if (listProducts.length == 0) {
                    $("#product_name").append("<option value=''>---该企业在Boss处没有查询到产品---</option>");
                }
                else {

                    for (var i = 0; i < listProducts.length; i++) {
                        var id = listProducts[i].id;
                        var name = listProducts[i].name;
                        $("#product_name").append("<option value='" + id + "'>" + name + "</option>");
                    }
                }
            },
            error: function () {
                $("#prdId").empty();
            }
        });
    }

    function doSubmit() {
        $("#error_msg").empty();
        if ($('#enter_code').val() == '') {
            var message = "请选择企业";
            $("#error_msg").append(message);
            return;
        }
        if ($('#product_name').val() == '') {
            var message = "请选择产品";
            $("#error_msg").append(message);
            return;
        }

        $.ajax({
            beforeSend: function (request) {
                var token1 = $(window.parent.document).find("meta[name=_csrf]").attr("content");
                var header1 = $(window.parent.document).find("meta[name=_csrf_header]").attr("content");
                request.setRequestHeader(header1, token1);
            },
            type: "GET",
            url: "${contextPath}/manage/entproquery/exchangeAjax.html",
            data: {
                enterpriseCode: $('#enter_code').val(),
                productID: $('#product_name').val(),
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var message = data.message;
                $("#error_msg").append(message);

            },
            error: function () {
                var message = "网络出错，请重新尝试";
                $("#error_msg").append(message);
            }
        });
    }


</script>
</body>
</html>