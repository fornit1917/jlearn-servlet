<#ftl output_format="HTML">
<#-- @ftlvariable name="criteria" type="jlearn.servlet.dto.UserSearchCriteria" -->
<#-- @ftlvariable name="users" type="jlearn.servlet.service.utility.PageResult<jlearn.servlet.entity.User>" -->

<#include "../inner_base.ftl">
<#include "../_pager.ftl">

<#macro inner_page_content>

<h1>User list</h1>

<form class="form form-inline user-filter-form" id="user-filter-form" method="GET">
    <div class="form-group">
        <input class="form-control user-filter-email" type="text" name="email" value="${criteria.getEmail()}" placeholder="Email"/>
    </div>
    <div class="form-group">
        <label for="state">State</label>
        <select name="state" id="state" class="form-control user-filter-state">
            <option value="1" ${(criteria.getState() == 1)?then("selected", "")}>All</option>
            <option value="2" ${(criteria.getState() == 2)?then("selected", "")}>Only active</option>
            <option value="3" ${(criteria.getState() == 3)?then("selected", "")}>Only inactive</option>
        </select>
    </div>
    <button type="submit" class="btn btn-primary">Apply filter</button>
</form>
<script>
    $(function () {
        UserFilterForm.init("#user-filter-form");
    });
</script>

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
            <#assign setActiveUrl = urlHelper.path("/admin/user-set-active") />
            <#list users.getSlice() as u >
                <tr class="${u.isActive()?then("", "disabled")}">
                    <td>${u.getId()}</td>
                    <td>${u.getEmail()}</td>
                    <td>${u.isAdmin()?then("Yes", "No")}</td>
                    <td>${u.getCreateDate("yyyy-MM-dd")}</td>
                    <td>
                        <#if u.isActive() >
                            <form method="POST" action="${setActiveUrl}">
                                <input type="hidden" name="id" value="${u.getId()}">
                                <input type="hidden" name="active" value="0">
                                <input type="hidden" name="redirectUrl" value="${requestUrl}">
                                <button type="submit" class="btn btn-sm btn-danger">
                                    <span class="glyphicon glyphicon-remove"></span> Ban
                                </button>
                            </form>
                        <#else>
                            <form method="POST" action="${setActiveUrl}">
                                <input type="hidden" name="id" value="${u.getId()}">
                                <input type="hidden" name="active" value="1">
                                <input type="hidden" name="redirectUrl" value="${requestUrl}">
                                <button type="submit" class="btn btn-sm btn-success">
                                    <span class="glyphicon glyphicon-ok"></span> Activate
                                </button>

                            </form>
                        </#if>
                    </td>
                </tr>
            </#list>
        </tbody>
    </table>
    <@pager pageResult=users/>
<#else>
    <p>Your request does not have any result</p>
</#if>

</#macro>

<@all_page_content></@all_page_content>