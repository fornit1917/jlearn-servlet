<#ftl output_format="HTML">
<#-- @ftlvariable name="criteria" type="jlearn.servlet.dto.UserSearchCriteria" -->
<#-- @ftlvariable name="users" type="jlearn.servlet.service.utility.PageResult<jlearn.servlet.entity.User>" -->

<#include "../inner_base.ftl">

<#macro inner_page_content>

<h1>User list</h1>

<#if ((users.getSlice()?size) > 0) >
    <table class="table table-striped user-list">
        <thead>
            <tr>
                <th>ID</th>
                <th>Email</th>
                <th>Admin</th>
                <th>Date</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <#list users.getSlice() as u >
                <tr class="${u.isActive()?then("", "disabled")}">
                    <td>${u.getId()}</td>
                    <td>${u.getEmail()}</td>
                    <td>${u.isAdmin()?then("Yes", "No")}</td>
                    <td>${u.getCreateDate()}</td>
                    <td>
                        <#if u.isActive() >
                            <span class="glyphicon glyphicon-remove" style="color: red" title="Ban"></span>
                        <#else>
                            <span class="glyphicon glyphicon-ok" style="color: green" title="Activate"></span>
                        </#if>
                    </td>
                </tr>
            </#list>
        </tbody>
    </table>
<#else>
    <p>Your request does not have any result</p>
</#if>

</#macro>

<@all_page_content></@all_page_content>