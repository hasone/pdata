/**
 * Created by cqb32_000 on 2015-11-25.
 */

(function () {
    require(["common", "bootstrap", "mock", "slider"], function () {
        listeners();

        $.fn.slider({
            renderTo: "#sampleStrategy"
        });
    });

    /**
     * 事件监听
     */
    function listeners() {
        $("#delete-app-btn").on("click", function () {
            doDeleteApp();
        });

        //删除事件点击事件
        $("#del-event-btn").on("click", function () {
            $("#delete-event-dialog").modal("show");
            var eventId = $(this).parent().parent().data("id");
            //弹框关联eventid
            $("#delete-event-dialog").data("eventId", eventId);
            return false;
        });

        //删除事件框确定点击事件
        $("#delete-event-btn").on("click", function () {
            var eventId = $("#delete-event-dialog").data("eventId");
            doDeleteEvent(eventId);
        });

        //保存sdk setting
        $("#save-sdkSetting-btn").on("click", function () {
            doSaveSDKSetting();
        });
    }

    /**
     * 删除app
     */
    function doDeleteApp() {
        var verifyCode = $("#verifyCode").val();
        var params = {app_id: app_id, verifyCode: verifyCode};

        ajaxData("deleteApp.html", params, function (ret) {
            if (ret && ret.success) {
                $("#delete-app-dialog").modal("hide");
                showTipDialog("删除成功!");
            } else {
                showTipDialog("删除失败!");
            }
        });
    }

    /**
     * 删除事件
     * @param eventId
     */
    function doDeleteEvent(eventId) {
        ajaxData("deleteEvent.html", {eventId: eventId}, function (ret) {
            if (ret && ret.success) {
                $("#delete-event-dialog").modal("hide");
                showTipDialog("删除成功!");
            } else {
                showTipDialog("删除失败!");
            }
        });
    }

    /**
     * 保存sdk setting
     */
    function doSaveSDKSetting() {
        var dataPost = $("input[name='dataPost']").val();
        var strategy = $("input[name='strategy']").val();
        var sessionTime = $("input[name='sessionTime']").val();
        var sample = $("#sampleStrategy").data("value");

        var params = {
            dataPost: dataPost,
            strategy: strategy,
            sessionTime: sessionTime,
            sample: sample
        };

        ajaxData("saveSDKSetting.html", params, function (ret) {
            if (ret && ret.success) {
                $("#sdk-dialog").modal("hide");
                showTipDialog("保存成功");
            } else {
                showTipDialog("保存失败");
            }
        });
    }
})();