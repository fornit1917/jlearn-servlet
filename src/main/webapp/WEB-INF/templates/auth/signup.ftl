<#ftl output_format="HTML">
<#-- @ftlvariable name="error" type="" -->

<#include "auth_base.ftl">

<#macro auth_body_content>
    <h1>Sign Up</h1>
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
        <div class="form-group">
            <input type="password" name="password_repeat" class="form-control" placeholder="Repeat Password"/>
        </div>
        <div class="form-group" style="margin-top:25px">
            <input type="text" name="invite" class="form-control" placeholder="Invite Code"/>
        </div>
        <button type="submit" class="btn btn-primary">Sign Up</button>
        <a class="or-link" href="${urlHelper.path("/signin")}">Or Sign In</a>
    </form>
</#macro>

<@all_page_content/>