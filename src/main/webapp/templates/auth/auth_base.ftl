<#ftl output_format="HTML">

<#include "../base.ftl">

<#macro body_class>no-overflow</#macro>

<#macro auth_body_content></#macro>

<#macro body_content>
<div class="auth-container">
    <div class="container">
        <div class="auth-form">
            <@auth_body_content/>
        </div>
    </div>
</div>
</#macro>