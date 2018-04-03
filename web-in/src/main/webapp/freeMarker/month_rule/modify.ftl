<#global  contextPath = rc.contextPath />

<!DOCTYPE html>
<html>
<head>
    <title>编辑包月流量赠送</title>
    <meta charset="UTF-8">

    <script type="text/javascript">
        $(function ($) {
            $('.next-year').packDatetime({
                dateType: 'nextYear',
                todayBtn: false
            });
        });

        function chooseEnter() {
            var index = jQuery("#enter_name option:selected").index();
            $('#enter_code')[0].selectedIndex = index;
        }

        function chooseProduct() {
            var index = jQuery("#product_name option:selected").index();
            $('#product_code')[0].selectedIndex = index;
        }

        function changeStartTime() {
            var monthArray = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"];
            var startDate = $('#start_time').val();
            if (startDate == '') {
                $('#end_time').val("");
                return;
            }
            var monthCount = Number($('#month_count').val()) - 1;//赠送月数
            if (monthCount == 0)
                $('#end_time').val(startDate);
            else {
                var date = new Date(startDate);
                date.setMonth(date.getMonth() + monthCount);
                var endDate = date.getFullYear() + "-" + (monthArray[date.getMonth()]) + "-01";
                $('#end_time').val(endDate);
            }
        }

        function doSubmit(obj) {
            $("#error_msg").empty();
            if ($("#start_time").val() == '') {
                $("#error_msg").append("请选择首次赠送日期");
                return;
            } else {//验证时间是否正确
                var date = new Date($("#start_time").val());
                if (new Date() >= date) {
                    $("#error_msg").append("请重新选择首次赠送日期");
                    return;
                }
            }
            changeStartTime();
            $("#status").val(obj);
            $("#table_validate").submit();
        }

        function doback() {
            window.history.back();
        }

        $('#editZSC').click(
                function () {
                    $.layer({
                        type: 2,
                        shade: [0],
                        fix: false,
                        title: '编辑被赠送人',
                        maxmin: true,
                        iframe: {src: '${contextPath}/giveManage/addBZSR.html'},
                        area: ['800px', '440px']
                    });
                }
        );

        function changelist() {
            var myselect = document.getElementById("eName");
            var index = myselect.selectedIndex;

            var first = myselect.options[index].value;
            ;
            var arr = new Array();
            arr = first.split(",");
            document.getElementById("eCode").value = arr[1];
        }

        function choosePhones() {
            window.open('${contextPath}/manage/month_record/phones.html', '_blank', 'left=200, top=150,width=640,height=500,menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=no');
        }
    </script>
</head>

<body>
<form id="table_validate" action="${contextPath}/manage/month_rule/update.html" method="POST">
    <input type="hidden" name="id" value="${record.id}"/>
    <input type="hidden" name="version" value="${record.version}"/>
    <input type="hidden" name="status" id="status" value=""/>
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="12%" class="tableleft">企业名称</td>
            <td>
            <#if flag??>
                <select name="entId" id="enter_name" onchange="chooseEnter()">
                    <#list enterList as e>
                        <option value="${e.id}" <#if e.id == record.entId>selected</#if>>${e.name}</option>
                    </#list>
                </select>
            <#else>
            ${enter.name}
                <input type="hidden" name="entId" value="${enter.id}"/>
            </#if>
            </td>
        </tr>

    <#if flag??>
        <tr>
            <td width="12%" class="tableleft">企业编码</td>
            <td>
                <select id="enter_code" disabled>
                    <#list enterList as e>
                        <option <#if e.id == record.entId>selected</#if>>${e.code}</option>
                    </#list>
                </select>
            </td>
        </tr>
    </#if>

        <tr>
            <td width="12%" class="tableleft">产品名称</td>
            <td>
                <select name="prdId" id="product_name" onchange="chooseProduct()">
                <#list products as p>
                    <option value="${p.id}" <#if p.id == record.prdId>selected</#if>>${p.name}</option>
                </#list>
                </select>
            </td>
        </tr>

        <#if flag??><!-- 客户经理则展示 -->
        <tr>
            <td width="12%" class="tableleft">产品代码</td>
            <td>
                <select id="product_code" disabled>
                    <#list products as p>
                        <option <#if p.id == record.prdId>selected</#if>>${p.code}</option>
                    </#list>
                </select>
            </td>
        </tr>
    </#if>

        <tr>
            <td width="12%" class="tableleft">被赠送人</td>
            <td>
                已有<span id="phone_count"><#if record.total==null>0</#if>${record.total}</span>个号码
                <input type="button" onclick="choosePhones()" value="编辑被赠送人"/>
                <input type="hidden" name="phones" id="phones" value="${phones}"/>
            </td>
        </tr>
        <tr>
            <td width="12%" class="tableleft">赠送总月数</td>
            <td>
                <select name="monthCount" id="month_count" style="width:85px" onchange="changeStartTime()">
                <#list numOfMonthMap as item>
                    <option value="${item.key}" <#if item.key == record.monthCount>selected</#if>>${item.value}</option>
                </#list>
                </select>
                <br/><span class="prompt">最小值为1个月,最大值为12个月</span>
            </td>
        </tr>

        <tr>
            <td width="12%" class="tableleft">首次赠送日期(<span class="red">*</span>)</td>
            <td>
                <div class="input-append date next-year" data-date-format="yyyy-mm-dd">
                    <input size="16" type="text" readonly="" name="startTime" id="start_time"
                           onchange="changeStartTime()" value="<fmt:formatDate value="${record.startTime}" type="both"
                    pattern="yyyy-MM-dd" />"/>
                    <span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-calendar"></i></span>
                </div>
                <%-- <input type="text" name="startTime" id="start_time" onchange="changeStartTime()"
                            value="<fmt:formatDate value="${record.startTime}" type="both" pattern="yyyy-MM-dd" />"/>
                --%>
                <br/><span class="prompt">范围:当前时间+1天~当前时间+1年<br/>
					除首次赠送外，其余赠送将会在每月初进行。例如，共赠送3期，选定的首次赠送日期为2015年2月28日，第2期赠送将在2015年3月1日进行，最后一期赠送将在2015年4月1日进行。
					</span>
            </td>
        </tr>
        <tr>
            <td width="12%" class="tableleft">末次赠送日期</td>
            <td><input type="text" name="endTime" id="end_time" readonly="readonly" value="${record.endTime?date}"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="button" class="btn btn-primary" type="button" onclick="doSubmit(0)">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-primary" type="button" onclick="doSubmit(1)">开始赠送</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" onclick="doback()">返回列表</button>
                <span style="color:red" id="error_msg">${errorMsg}</span>
            </td>
        </tr>
    </table>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
</body>
</html>