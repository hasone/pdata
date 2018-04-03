/**
 * Created by cqb32_000 on 2015-11-13.
 */
define(["jquery"], function ($) {
    $.fn.sortTable = function (options) {
        var opt = options || {};

        return this.each(function () {
            init($(this));
        });

        function init(ele) {
            $(".sort-btn", ele).click(function () {
                var ordering = "asc";
                if ($(this).hasClass("fa-sort")) {
                    $(".sort-btn.fa-sort-asc").removeClass("fa-sort-asc").addClass("fa-sort");
                    $(".sort-btn.fa-sort-desc").removeClass("fa-sort-desc").addClass("fa-sort");
                    $(this).removeClass("fa-sort");
                    $(this).addClass("fa-sort-asc");
                } else if ($(this).hasClass("fa-sort-asc")) {
                    $(this).removeClass("fa-sort-asc");
                    $(this).addClass("fa-sort-desc");
                    ordering = "desc";
                } else {
                    $(this).removeClass("fa-sort-desc");
                    $(this).addClass("fa-sort-asc");
                    ordering = "asc";
                }

                var index = $(".sort-btn", ele).index(this);

                changeTableBody(ele, ordering, index);

                $(".table-body", ele).scrollTop(0);
            });
        }

        /**
         *
         * @param ele
         * @param sort
         * @param index
         */
        function changeTableBody(ele, sort, index) {
            var data = [];
            //��ѡ����
            var trBodyList = ele.find(".table-body tr");
            trBodyList.each(function (i) {
                data[i] = [];
                $(this).find("td").each(function (j) {
                    data[i][j] = $(this).html();
                });
            });
            data.sort(function (x, y) {
                return sort == 'asc' ? x[index].localeCompare(y[index]) : y[index].localeCompare(x[index]);
            });
            trBodyList.each(function (i) {
                $(this).find("td").each(function (j) {
                    $(this).html(data[i][j]);
                });
            });
        }
    };
});