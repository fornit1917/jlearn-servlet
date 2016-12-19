<#ftl output_format="HTML">
<#-- @ftlvariable name="editableUser" type="jlearn.servlet.dto.User" -->

<#include "../inner_base.ftl">

<#macro inner_page_content>
<h1>Edit User ${editableUser.getEmail()}</h1>
<hr/>
    <#include "../user/_profile_form.ftl"/>
</#macro>

<@all_page_content></@all_page_content>