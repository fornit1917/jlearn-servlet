<#ftl output_format="HTML">
<#-- @ftlvariable name="json" type="java.lang.String" -->

<#include "../inner_base.ftl">

<#macro special_scripts>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/knockout/3.4.1/knockout-min.js"></script>
</#macro>

<#macro inner_page_content>
<h1>Book Reading History</h1>
<hr/>
<div id="history"></div>

<script>
    $(function () {
        ReadingHistory.init("#history", "${urlHelper.path("/book-reading/history")}", ${json?no_esc});
    });
</script>
</#macro>

<@all_page_content></@all_page_content>