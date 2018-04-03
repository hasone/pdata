<!DOCTYPE html>
<#assign contextPath = rc.contextPath >
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="format-detection" content="telephone=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- 防止csrf攻击 -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <title>签到</title>

    <link href="${contextPath}/assets/css/jiayouzhan.min.css" rel="stylesheet">
    <link href="${contextPath}/assets/css/mdater.min.css" rel="stylesheet">
    <style type="text/css">
        .md_panel {
            overflow: hidden;
        }

        #submit-btn {
            position: fixed;
            left: 0;
            right: 0;
            bottom: 0;
            margin-bottom: 1em;
        }
        
        .modal-body .success-img{
            width: 210px;
        }

        .modal-body{
            padding-top: 38px;
        }

        .modal-body .check-success{
            position: absolute;
            top: -99px;
            left: 0;
            width: 100%;
        }
    </style>
</head>
<body>
<div id="sign" hidden></div>
<div class="operate-box" id="submit-btn">
    <a class="md_ok btn border-radius" role="button">今日签到</a>
</div>

<div id="tip-dialog" class="modal fade text-center" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content body-success hide">
            <div class="modal-body ">
                <img src="${contextPath}/assets/imgs/qiandao.png" alt="" class="check-success">
                <div class="font-black"><span class="font-red mr-5" style="font-size: 20px;" id="signbonus"></span></div>
                <div class="mt-10">
                    <img src="${contextPath}/assets/imgs/sign-success.png" class="success-img">
                </div>
            </div>
            <div class="modal-footer" id="sign-success">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal">确 定</a></p>
            </div>
        </div>
        
        <div class="modal-content body-failure hide">
            <div class="modal-body">
                <p>签到失败！</p>
            </div>
            <div class="modal-footer fail-btn">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal">再试一次</a></p>
            </div>
        </div>
        <div class="modal-content body-busy hide">
            <div class="modal-body">
                <p>网络繁忙</p>
            </div>
            <div class="modal-footer fail-btn">
                <p class="footer-color"><a style="display: block;" data-dismiss="modal">再试一次</a></p>
            </div>
        </div>
    </div>
</div>

<script src="${contextPath}/assets/js/zepto1.min.js"></script>
<script src="${contextPath}/assets/js/mdater.min.js"></script>
<script src="${contextPath}/assets/js/other.min.js"></script>

<script>
    var signedDate = [];
    var date_ele = $('#sign');
    var todayStr = dateFormat(new Date(), "yyyy-MM-dd");
    var todaySigned = false;
 
    $(function () {
        /**
         * 加载页面渲染日历
         */
        date_ele.mdater({
            updated: function (ele) {
                var startDate = getStartDate(ele);
                var endDate = getEndDate(ele);
                //当前视图第一天属于未来日期不获取签到
                if (new Date(startDate).getTime() < new Date().getTime()) {
                    getSignedDate(startDate, endDate);
                }
            }
        });

        /**
         * 点击今日签到按钮
         */
        $('.md_ok').on('click', function () {
            $('.md_ok').addClass('btn-disabled');
            $.ajax({
               beforeSend: function (request) {
                   var token1 = $(document).find("meta[name=_csrf]").attr("content");
                   var header1 = $(document).find("meta[name=_csrf_header]")
                           .attr("content");
                   request.setRequestHeader(header1, token1);
               },
               url: "${contextPath}/wx/sign/signin.html?_=" + new Date(),
               type: 'POST',
               dataType: 'json',
               success: function (data) {
                   if (data.success) {
                       todaySigned = true;
                       signedDate.push(todayStr);
                       var bonus = data.signBonus;
                       $("#signbonus").html('+' + bonus + '流量币');
                       showDialogContent('success');
                   } else {
                       showDialogContent('failure');
                   }
               },
               error: function () {
                   showDialogContent('busy');
               }
           });
        });

        $('#sign-success').on('click', function () {
            $('.current').addClass('sign-today');
            $('.md_ok').text('今日已签，明日继续');
        });

        $('.fail-btn').on('click', function () {
            $('.current').removeClass('sign-today');
            $('.md_ok').removeClass('btn-disabled');
        });
    });

    /**
     * 之前所有签到日期标记
     */
    function getSignedDate(startDate, endDate) {
        $.ajax({
                   beforeSend: function (request) {
                       var token1 = $(document).find("meta[name=_csrf]").attr("content");
                       var header1 = $(document).find("meta[name=_csrf_header]").attr("content");
                       request.setRequestHeader(header1, token1);
                   },
                   url: "${contextPath}/wx/sign/records.html?=" + new Date(),
                   type: 'GET',
                   dataType: 'json',
                   data: {startDate: startDate, endDate: endDate},
                   success: function (data) {
                       signedDate = data.records;
                       renderSigned();
                   },
                   error: function () {
                   }
               });
    }

    /**
     * 之前所有签到日期标记
     */
    function renderSigned() {
        $('.md_datearea li').each(function () {
            var isTodaySigned = sign($(this), signedDate);
            if (isTodaySigned) {
                todaySigned = true;
            }
        });

        if (todaySigned) {
            $('.md_ok').addClass('btn-disabled').text('今日已签，明日继续');
        } else {
            $('.md_ok').removeClass('btn-disabled').text('今日签到');
        }
    }

    /**
     * 获取当前月历视图中第一个日期
     */
    function getStartDate(ele) {
        return getEleDate($("li:first-child", ele));
    }

    /**
     * 获取当前月历视图中最后一个日期
     */
    function getEndDate(ele) {
        return getEleDate($("li:last-child", ele));
    }

    /**
     * 根据是否当前天数签到做不同标记
     * @param ele
     * @param data
     */
    function sign(ele, data) {
        var daystr = getEleDate(ele);
        if (data && indexOfInArray(data, daystr)) {
            ele.hasClass('current') ? ele.addClass('sign-today') : ele.removeClass('sign-today')
                    .addClass('sign-befor');
            if (daystr === todayStr) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前元素的日期
     */
    function getEleDate(ele) {
        var dateValue = date_ele[0].data;
        //判断是否点击的是前一月或后一月的日期
        var add = 0;
        if (ele.hasClass('nextdate')) {
            add = 1;
        }
        else if (ele.hasClass('prevdate')) {
            add = -1;
        }
        var year = dateValue.year;
        var month = dateValue.month + add;

        if (month > 11) {
            month = 0;
            year++;
        }
        else if (month < 0) {
            month = 11;
            year--;
        }

        var date_value = new Date();
        date_value.setFullYear(year);
        date_value.setDate(ele.data("day"));
        date_value.setMonth(month);
        return dateFormat(date_value, 'yyyy-MM-dd');
    }

    /**
     * indexOfInArray
     */
    function indexOfInArray(arr, value) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return true;
            }
        }
        return false;
    }
    /**
     * 显示结果弹框
     * @param clazz
     */
    function showDialogContent(clazz) {
        $('#tip-dialog .modal-content').addClass('hide');
        $('.body-' + clazz).removeClass('hide');

        $("#tip-dialog").modal("show");
    }
    /**
     *日期格式化
     */
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
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt =
                        fmt.replace(RegExp.$1,
                                    (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(
                                            ("" + o[k]).length)));
            }
        }
        return fmt;
    }
</script>
</body>
</html>