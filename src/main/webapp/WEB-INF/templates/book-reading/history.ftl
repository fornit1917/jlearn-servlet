<#ftl output_format="HTML">
<#-- @ftlvariable name="json" type="java.lang.String" -->

<#include "../inner_base.ftl">

<#macro special_scripts>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/knockout/3.4.1/knockout-min.js"></script>
</#macro>

<#macro inner_page_content>
<h1>Book Reading History</h1>
<hr/>
<div id="history">
    <div data-bind="foreach: years">
        <div class="app-history-year">
            <h3 data-bind="text: year"></h3>
            <div class="app-history-year-content" data-bind="foreach: items">
                <div class="app-history-item" data-bind="css: {inactive: isInactive()}">
                    <div class="app-history-item-status" data-bind="text: getDisplayStatus()"></div>
                    <div class="app-history-item-book-info">
                        <span class="app-history-item-author" data-bind="text: author"></span>,
                        <span class="app-history-item-title" data-bind="text: title"></span>
                    </div>
                    <div class="app-history-item-dates">
                        <div class="app-history-item-date" data-bind="if: showDate('start')">
                            Started: <span data-bind="text: start"></span>
                        </div>
                        <div class="app-history-item-date" data-bind="if: showDate('end')">
                            Ended: <span data-bind="text: end"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="app-history-load">
        <button class="btn btn-primary" data-bind="click: onLoadClick, visible: !isLoading() && hasNextPage()">More</button>
        <div class="loader" data-bind="visible: isLoading"></div>
    </div>
</div>

<script>
    $(function () {
        ReadingHistory.init("#history", "${urlHelper.path("/book-reading/history")}", ${json?no_esc});
    });
</script>
</#macro>

<@all_page_content></@all_page_content>