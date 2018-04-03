define(["react", "react-dom", "Table", "Pagination", "jquery"], function (React, ReactDOM, Table, Pagination, $) {
    "use strict";

    /**
     * Created by cqb32_000 on 2016-03-01.
     */


    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";

    var search = function search(page, pageSize) {
        $.ajax({
            url: action,
            type: "post",
            dataType: "json",
            data: getSearchParams(page, pageSize)
        }).then(function (ret) {
            table.setData(ret.data);
            pagination.update({total: ret.total, current: ret.pageNum, pageSize: ret.pageSize});
        }).fail(function () {
            console.log("get Table Data error!");
        });
    };

    function getSearchParams(page, pageSize) {
        var params = {
            pageNum: page,
            pageSize: pageSize
        };
        $(".searchItem").each(function () {
            var name = $(this).attr("name");
            params[name] = $(this).val();
        });

        return params;
    }

    search(1, 10);

    var table = ReactDOM.render(React.createElement(Table, {
        className: tableClass,
        header: columns,
        data: []
    }), document.querySelector("div[role='table']"));

    var pagination = ReactDOM.render(React.createElement(Pagination, {
        onChange: search,
        onShowSizeChange: search
    }), document.querySelector("div[role='pagination']"));

    $("#search-btn").on("click", function () {
        search(pagination.state.current, pagination.state.pageSize);
        renderPie();
    });
});
