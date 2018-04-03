define(["react", "react-dom", "Table", "jquery"], function (React, ReactDOM, Table, $) {
    "use strict";

    var tableClass = "table table-indent text-center table-bordered-noheader mb-0";


    var TableWidget = {
        search: function search() {
            $.ajax({
                url: action,
                type: "post",
                dataType: "json",
                data: getSearchParams()
            }).then(function (ret) {
                table.setData(ret.data);
                if(TableWidget.searchCallback){
                    TableWidget.searchCallback(ret);
                }
            }).fail(function () {
                console.log("get Table Data error!");
            });
        },
        searchCallback: function(){}
    };

    function getSearchParams() {
        var params = {
        };
        $(".searchItem").each(function () {
            var name = $(this).attr("name");
            params[name] = $(this).val();
        });

        return params;
    }

    var table = ReactDOM.render(React.createElement(Table, { className: tableClass, header: columns, data: [] }), document.querySelector("div[role='table']"));

    $("#search-btn").on("click", function () {
        TableWidget.search();
    });

    return TableWidget;
});
