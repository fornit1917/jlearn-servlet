<#ftl output_format="HTML">
<#-- @ftlvariable name="error" type="java.lang.String" -->

<#include "auth_base.ftl">

<#macro auth_body_content>
    <h1>Sign In</h1>
    <#if ((error!"") != "") >
        <p class="error">${error}</p>
    </#if>
    <form method="POST">
        <div class="form-group">
            <input type="email" name="email" class="form-control" placeholder="Email"/>
        </div>
        <div class="form-group">
            <input type="password" name="password" class="form-control" placeholder="Password"/>
        </div>
        <button type="submit" class="btn btn-primary">Sign In</button>
        <a class="or-link" href="${urlHelper.path("/signup")}">Or Sign Up</a>
    </form>
</#macro>

<@all_page_content/>