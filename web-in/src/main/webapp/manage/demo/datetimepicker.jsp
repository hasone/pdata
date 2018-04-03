<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>日期组件</title>
</head>
<body>

<section id="demo_advanced">

    <form action="" class="form-horizontal">
        <fieldset>
            <legend>DEMO</legend>
            <div class="control-group datetime-group">
                <label class="control-label">一组日期(可为同一天)：</label>
                <div class="input-append date base-group" data-group="date-begin" data-date-format="yyyy-mm-dd">
                    <input size="16" type="text" value="" readonly="">
                    <span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-calendar"></i></span>
                </div>
                <div class="input-append date base-group" data-group="date-end" data-date-format="yyyy-mm-dd">
                    <input size="16" type="text" value="" readonly="">
                    <span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-calendar"></i></span>
                </div>
            </div>
            <div class="control-group datetime-group">
                <label class="control-label">一组日期(不可同一天)：</label>
                <div class="input-append date group1" data-group="date-begin" data-date-format="yyyy-mm-dd">
                    <input size="16" type="text" value="" readonly="">
                    <span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-calendar"></i></span>
                </div>
                <div class="input-append date group1" data-group="date-end" data-date-format="yyyy-mm-dd">
                    <input size="16" type="text" value="" readonly="">
                    <span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-calendar"></i></span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">基本单个日期：</label>
                <div class="input-append date base" data-date-format="yyyy-mm-dd">
                    <input size="16" type="text" value="" readonly="">
                    <span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-calendar"></i></span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">下一年：</label>
                <div class="input-append date next-year" data-date-format="yyyy-mm-dd">
                    <input size="16" type="text" value="" readonly="">
                    <span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-calendar"></i></span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">下一月：</label>
                <div class="input-append date next-month" data-date-format="yyyy-mm-dd">
                    <input size="16" type="text" value="" readonly="">
                    <span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-calendar"></i></span>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label">日期与时间：</label>
                <div class="input-append date dateTime" data-date-format="yyyy-mm-dd hh:ii:ss">
                    <input size="16" type="text" value="" readonly="">
                    <span class="add-on"><i class="icon-remove"></i></span>
                    <span class="add-on"><i class="icon-calendar"></i></span>
                </div>
            </div>
        </fieldset>
    </form>
</section>

<script type="text/javascript">
    //一组日期，开始结束日期可为同一天
    $('.base-group').packDatetime()
    //一组日期，开始结束日期不可为同一天
    $('.group1').packDatetime({
        userSameDay: false
    })
    //基本单个日期
    $('.base').packDatetime()
    //下一年
    $('.next-year').packDatetime({
        dateType: 'nextYear',
        todayBtn: false
    })
    //下一月
    $('.next-month').packDatetime({
        dateType: 'nextMonth',
        todayBtn: false
    })
    //日期和时间
    $('.dateTime').packDatetime({minView: 0})


</script>

</body>
</html>