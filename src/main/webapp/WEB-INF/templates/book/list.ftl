<#ftl output_format="HTML">
<#-- @ftlvariable name="otherUser" type="jlearn.servlet.dto.User" -->
<#-- @ftlvariable name="isOtherUser" type="java.lang.Boolean" -->
<#-- @ftlvariable name="message" type="java.lang.String" -->
<#-- @ftlvariable name="criteria" type="jlearn.servlet.dto.BookSearchCriteria" -->
<#-- @ftlvariable name="books" type="jlearn.servlet.service.utility.PageResult<jlearn.servlet.dto.Book>" -->

<#include "../inner_base.ftl">
<#include "../_pager.ftl">

<#macro inner_page_content>
    <#if (isOtherUser!false) >
        <h1>Books List of User ${otherUser.getEmail()}</h1>
    <#else>
        <h1>Books List</h1>
        <a class="btn btn-success app-btn-in-head pull-right" href="${urlHelper.path("/book/add")}">Add Book</a>
    </#if>
    <hr/>
    <#if ((message!"") != "") >
        <div class="alert alert-success" role="alert">
            ${message}
        </div>
    </#if>
    <form class="form book-filter-form" id="book-filter-form">
        <div class="row">
            <div class="col-md-3">
                <div class="form-group">
                    <label>Author</label>
                    <input type="text" name="author" value="${criteria.getAuthor()}" placeholder="Author" class="form-control">
                </div>
            </div>
            <div class="col-md-3">
                <div class="form-group">
                    <label>Title</label>
                    <input type="text" name="title" value="${criteria.getTitle()}"  placeholder="Title" class="form-control">
                </div>
            </div>
            <div class="col-md-2">
                <div class="form-group">
                    <label>Status</label>
                    <select name="status" class="form-control">
                        <option ${(criteria.getStatus() == 1)?then("selected", "")} value="1">All</option>
                        <option ${(criteria.getStatus() == 2)?then("selected", "")} value="2">Unread</option>
                        <option ${(criteria.getStatus() == 3)?then("selected", "")} value="3">Read</option>
                    </select>
                </div>
            </div>
            <div class="col-md-2">
                <div class="form-group">
                    <label>Type</label>
                    <select name="type" class="form-control">
                        <option ${(criteria.getType() == 1)?then("selected", "")} value="1">All</option>
                        <option ${(criteria.getType() == 2)?then("selected", "")} value="2">Fiction</option>
                        <option ${(criteria.getType() == 3)?then("selected", "")} value="3">Not Fiction</option>
                    </select>
                </div>
            </div>
            <#if isOtherUser!false >
                <input type="hidden" name="userId" value="${otherUser.getId()?c}"/>
            </#if>
            <div class="col-md-2">
                <div class="form-group">
                    <label style="color:white;">.</label>
                    <button type="submit" class="form-control btn btn-primary">Apply filter</button>
                </div>
            </div>
        </div>
    </form>
    <#if ((books.getSlice()?size) > 0) >
    <table class="table table-striped book-list">
        <thead>
        <tr>
            <th>Author</th>
            <th>Title</th>
            <th>Status</th>
            <#if (!(isOtherUser!false)) >
                <th class="actions-column">Actions</th>
            </#if>
        </tr>
        </thead>
        <#assign deleteAction = urlHelper.path("/book/delete") />
        <tbody>
            <#list books.getSlice() as b >
            <tr>
                <td>${b.getAuthor()}</td>
                <td>${b.getTitle()}</td>
                <td>${b.getStatus().toString()}</td>
                <#if (!(isOtherUser!false)) >
                    <td class="actions-column">
                        <a href="${urlHelper.path("/book-details?id=") + b.getId() + "&redirectUrl=" + requestUrl}" class="btn btn-sm btn-primary" title="View Details">
                            <span class="glyphicon glyphicon-eye-open"></span>
                        </a>
                        <a href="${urlHelper.path("/book/update?id=") + b.getId()}" class="btn btn-sm btn-warning" title="Edit">
                            <span class="glyphicon glyphicon-pencil"></span>
                        </a>
                        <form method="post" action="${deleteAction}" class="action-form book-delete-form">
                            <input type="hidden" name="id" value="${b.getId()}">
                            <input type="hidden" name="redirectUrl" value="${requestUrl}">
                            <button type="submit" class="btn btn-sm btn-danger" title="Delete">
                                <span class="glyphicon glyphicon-trash"></span>
                            </button>
                        </form>
                    </td>
                </#if>
            </tr>
            </#list>
        </tbody>
    </table>
        <@pager pageResult=books/>
    <#else>
    <div class="app-empty-message">
        <#if (!(isOtherUser!false)) >
            <p>You don't have books by this request.</p>
            <a href="${urlHelper.path("/book/add")}" class="btn btn-success">Add Book</a>
        <#else >
            <p>This user does not have books by this request.</p>
        </#if>
    </div>
    </#if>
    <script>
        $(function () {
            BookFilterForm.init("#book-filter-form");
            DeleteButtons.init(".book-delete-form")
        });
    </script>
</#macro>

<@all_page_content></@all_page_content>