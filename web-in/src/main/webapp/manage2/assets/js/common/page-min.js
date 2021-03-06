$(function () {
    var a = "page-action", c = {
        "data-id": "id",
        title: "title",
        "data-href": "href",
        "data-close": "isClose",
        "data-search": "search",
        "data-mid": "moduleId",
        "data-type": "type"
    };

    function b(d) {
        var e = {};
        $.each(d, function (h, f) {
            var g = f.nodeName, i = c[g];
            if (i) {
                e[i] = f.nodeValue
            }
        });
        return e
    }

    if (top.topManager) {
        $("body").delegate("." + a, "click", function (f) {
            var e = f.currentTarget, d = e.attributes, g = b(d);
            if (!g.type || g.type == "open") {
                top.topManager.openPage(g);
                f.preventDefault()
            } else {
                if (g.type == "setTitle") {
                    top.topManager.setPageTitle(g.title, g.moduleId)
                } else {
                    f.preventDefault();
                    top.topManager.operatePage(g.moduleId, g.id, g.type)
                }
            }
        });
        if (0) {
            alert("\u4f60\u597d\u4e48")
        }
    }
});
