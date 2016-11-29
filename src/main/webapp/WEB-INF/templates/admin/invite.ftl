<#ftl output_format="HTML">
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="addedCode" type="java.lang.String" -->

<#include "../inner_base.ftl">

<#macro inner_page_content>

    <h1>Create invite</h1>
    <hr/>

    <#if ((addedCode!"") != "") >
        <div class="alert alert-success invite-added-message" role="alert">
            Code <span>${addedCode}</span> has been added successfully
        </div>
    </#if>
    <#if ((error!"") != "") >
        <div class="alert alert-danger invite-added-message" role="alert">
            ${error}
        </div>
    </#if>

    <form class="form form-inline invite-form" id="invite-form" method="POST">
        <input type="text" class="form-control invite-code" id="invite-code" placeholder="Enter code or leave blank to generate" name="code">
        <button type="submit" class="btn btn-primary">Generate</button>
    </form>
    <script>
        $(function () {
            AddInviteForm.init("#invite-form");
        });
    </script>

</#macro>

<@all_page_content></@all_page_content>