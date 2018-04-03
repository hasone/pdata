$(function () {
    $('.deleteConfirm').click(function (ev) {


        var szAlert = $(this).attr("info") || "确定要删除吗?";

        var href = $(this).attr("href");

        art.dialog({
            icon: 'question',
            fixed: true,
            lock: true,
            content: szAlert,
            ok: function () {
                window.location = href;
                return true;
            },
            cancel: function () {
                return true;
            }
        });

        return false;
    });
});