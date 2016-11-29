<#ftl output_format="HTML">
<#-- @ftlvariable name="message" type="java.lang.String" -->
<#-- @ftlvariable name="criteria" type="jlearn.servlet.dto.BookSearchCriteria" -->
<#-- @ftlvariable name="books" type="jlearn.servlet.service.utility.PageResult<jlearn.servlet.entity.Book>" -->

<#include "../inner_base.ftl">
<#include "../_pager.ftl">

<#macro inner_page_content>
    <h1>Books List</h1>
    <hr/>
    <#if ((message!"") != "") >
    <div class="alert alert-success" role="alert">
        ${message}
    </div>
    </#if>
    <#if ((books.getSlice()?size) > 0) >
    <table class="table table-striped book-list">
        <thead>
        <tr>
            <th>Author</th>
            <th>Title</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <#assign deleteAction = urlHelper.path("/book/delete") />
        <tbody>
            <#list books.getSlice() as b >
            <tr>
                <td>${b.getAuthor()}</td>
                <td>${b.getTitle()}</td>
                <td>${b.getStatus().toString()}</td>
                <td>
                    <form method="post" action="${deleteAction}">
                        <input type="hidden" name="id" value="${b.getId()}">
                        <input type="hidden" name="redirectUrl" value="${requestUrl}">
                        <button type="submit" class="btn btn-sm btn-danger" title="Delete">
                            <span class="glyphicon glyphicon-trash"></span>
                        </button>
                    </form>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
        <@pager pageResult=books/>
    <#else>
    <div style="text-align: center">
        <p>You didn't add any books yet.</p>
        <a href="${urlHelper.path("/book/add")}" class="btn btn-primary">Add Book</a>
    </div>
    </#if>
</#macro>

<@all_page_content></@all_page_content>