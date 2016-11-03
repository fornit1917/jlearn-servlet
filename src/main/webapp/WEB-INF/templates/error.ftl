<#ftl output_format="HTML">
<#-- @ftlvariable name="verbose" type="java.lang.String" -->
<#-- @ftlvariable name="text" type="java.lang.String" -->
<#-- @ftlvariable name="code" type="java.lang.Number" -->

<#include "base.ftl">

<#macro body_content>
    <div class="error-container">
        <div class="container">
            <h1>Error ${code}</h1>
            <p class="error-text">${text}</p>
            <p><a class="go-to-main" href="${urlHelper.path("/")}">Go back to the main page</a></p>
            <#if ((verbose!"") != "") >
                <p class="error-verbose">
                <pre>
                ${verbose?no_esc}
            </pre>
            </#if>
        </div>
    </div>
</#macro>

<@all_page_content></@all_page_content>