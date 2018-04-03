/**
 * Created by cqb32_000 on 2015-11-12.
 */
require(["accordion", "bootstrap", "store"], function (Accordion, bootstrap, store) {


    $("#accordion").aop("onSelect", function (ele, evt) {
        var url = $(ele).attr("href");
        var identity = $(ele).data("identity");
        store.set("sc_menu_identity", identity);
        if (url !== "" && url !== "#") {
            $('#desktop').fadeOut(300);
            window.setTimeout(function () {
                $("#desktop").attr("src", url);
                $('#desktop').fadeIn(300);
            }, 300);
        }
        return false;
    });

    if (store.get("sc_desktop_src")) {
        $("#desktop").attr("src", store.get("sc_desktop_src"));
    }
    var iden = store.get("sc_menu_identity");
    if (iden) {
        var the_item = $("#accordion li a[data-identity='" + iden + "']");
        the_item.parent().addClass("active");
        the_item.parents(".submenu").parent().addClass("active");

        $("#accordion")[0].ins.showHideSubMenu(the_item.parents(".submenu"));
    } else {
        $("#accordion li a").eq(0).click();
    }

    $(".menu-item").on("click", function (e) {
        var url = $(this).attr("href");
        if (url !== "" && url !== "#") {
            $('#desktop').fadeOut(300);
            window.setTimeout(function () {
                $("#desktop").attr("src", url);
                $('#desktop').fadeIn(300);
            }, 300);
        }

        if (e.preventDefault) {
            e.preventDefault();
        }

        return false;
    });

    $('#desktop').on("load", function () {
        store.set("sc_desktop_src", this.contentWindow.location.href);
    });
});