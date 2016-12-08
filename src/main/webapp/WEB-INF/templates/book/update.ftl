<#ftl output_format="HTML">

<#include "../inner_base.ftl">

<#macro inner_page_content>
<h1>Update Book</h1>
<hr/>
    <#if ((success!false) != false) >
        <div class="alert alert-success" role="alert">
        Book has been updated successfully. <a href="${urlHelper.path("/book/list")}">Show list</a>
    </div>
    </#if>
    <#include "_book_form.ftl">
</#macro>

<@all_page_content></@all_page_content>