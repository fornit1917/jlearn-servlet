<#ftl output_format="HTML">
<#-- @ftlvariable name="criteria" type="jlearn.servlet.dto.UserSearchCriteria" -->
<#-- @ftlvariable name="users" type="jlearn.servlet.service.utility.PageResult<jlearn.servlet.dto.User>" -->

<#include "../inner_base.ftl">
<#include "../_pager.ftl">

<#macro inner_page_content>
<h1>Public User List</h1>
<hr/>
<form class="form form-inline user-filter-form" id="user-filter-form" method="GET">
    <div class="form-group">
        <input class="form-control user-filter-email" type="text" name="email" value="${criteria.getEmail()}" placeholder="Email"/>
    </div>
    <button type="submit" class="btn btn-primary">Apply filter</button>
</form>
    <#if ((users.getSlice()?size) > 0) >
        <table class="table">
            <#list users.getSlice() as u >
                <tr>
                    <td>${u.getEmail()}</td>
                    <td><a href="${urlHelper.path("/book/list?userId=" + u.getId())}">Books</a></td>
                    <td><a href="${urlHelper.path("/book-reading/history?userId=" + u.getId())}">Reading History</a></td>
                </tr>
            </#list>
        </table>
        <@pager pageResult=users/>
    <#else>
        <div style="text-align: center">
            <p>List of public users is empty.</p>
        </div>
    </#if>
</#macro>

<@all_page_content></@all_page_content>