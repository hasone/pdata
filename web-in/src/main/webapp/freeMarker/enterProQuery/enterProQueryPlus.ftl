<!DOCTYPE html>

<#global  contextPath = rc.contextPath />

<html>

<head>
    <title>
        实时流量包查询
    </title>
    <meta charset="UTF-8">
</head>

<body>
<div class="page-header">
    <h1>实时流量包查询</h1>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div class="table-search">
            <form id="table_validate" class="form-inline" method="post"
                  action="${contextPath}/manage/entproquery/indexPlus.html" method="post">
            <#if role == "CUSTOM_MANAGER">   <!--客户经理 -->
                <div class="form-group">
                    <label>企业名称：</label>
                    <select name="entId" id="enter_name" onchange="chooseEnter()">
                        <#list enterprises as e>
                            <option value="${e.id}" <#if enter.id == e.id> selected </#if>>${e.name}</option>
                        </#list>
                    </select>
                </div>
                &nbsp; &nbsp;
                <div class="form-group">
                    <label>企业编码：</label>
                    <select id="enter_code" name="enter_code" disabled style="width:300px;">
                        <#list enterprises as e>
                            <option>${e.code}</option>
                        </#list>
                    </select>
                </div>
                &nbsp; &nbsp;
                <div class="form-group">
                    <button class="btn btn-info" type="button" onclick="doSubmit()">查询</button>
                </div>
            <#else>           <!--企业关键人 -->
                <div class="form-group">

                    <#list enterprises as e>
                        <label>企业名称： <span style="font-size:14px"><#if enter.id == e.id>${e.name}</#if></span></label>
                        <input name="entId" class="invisible" id="enter_name" onchange="chooseEnter()"
                               value="<#if enter.id == e.id>${e.name}</#if>"/>
                        <label>企业编码： <span style="font-size:14px">${e.code}</span></label>
                        <input name="enter_code" class="invisible" id="enter_code" value="${e.code}"/>
                    </#list>
                </div>

            </#if>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
        </div>


        <div class="table-responsive">

            <table id="sample-table-1" class="table table-hover">
                <tr>
                    <th>产品名称</th>
                    <th>产品编码</th>
                    <th>产品剩余个数</th>
                    <th>操作</th>
                </tr>


            <#if (products?size>0) >
                <#list products as p>
                    <tr>
                        <td class="center">${p.name}</td>
                        <td>${p.productCode}</td>
                        <td class="color-yellow"><span id="error_msg${p.id}">-</span></td>
                        <td><a href="#" onclick="query(${p.id});">查询</button></td>
                    </tr>
                </#list>
            </#if>
            </table>

        </div>
    </div>
</div>

<script type="text/javascript">
    function chooseEnter() {
        var index = jQuery("#enter_name option:selected").index();
        $('#enter_code')[0].selectedIndex = index;
        var enterId = jQuery("#enter_name option:selected").val();

    }
    ;


    $(document).ready(function () {
        chooseEnter();
    });

    function query(id) {
        var span = "#error_msg" + id;
        $(span).empty();
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
                productID: id,
            },
            dataType: "json", //指定服务器的数据返回类型，
            success: function (data) {
                var message = data.message;
                $(span).append(message);

            },
            error: function () {
                var message = "网络出错，请重新尝试";
                $(span).append(message);
            }
        });
    }


    function doSubmit() {
        $("#table_validate").submit();
    }


</script>
</body>
</html>