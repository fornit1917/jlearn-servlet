<#ftl output_format="HTML">
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="query" type="java.lang.String" -->
<#-- @ftlvariable name="bookDetails" type="jlearn.servlet.dto.BookDetails" -->
<#-- @ftlvariable name="redirectUrl" type="java.lang.String" -->
<#-- @ftlvariable name="book" type="jlearn.servlet.dto.Book" -->

<#include "../inner_base.ftl">

<#macro inner_page_content>
<h1>Book Details</h1>
<a href="${redirectUrl}" class="btn btn-default pull-right app-btn-in-head">
    <span class="glyphicon glyphicon-arrow-left"></span>
    Back to Books
</a>
<hr/>
    <#if ((error!"") != "") >
        <div class="app-empty-message">
            <p>Unfortunately, we cannot find details for book <i>${book.getAuthor()}, &laquo;${book.getTitle()}&raquo;</i>.</p>
            <a href="${redirectUrl}">Back to the List</a>
        </div>
    <#else >
        <table class="table table-bordered">
            <tr>
                <td><b>Author</b></td>
                <td>${bookDetails.getAuthor()}</td>
            </tr>
            <tr>
                <td><b>Title</b></td>
                <td>${bookDetails.getTitle()}</td>
            </tr>
            <tr>
                <td><b>Genre</b></td>
                <td>${bookDetails.getGenre()}</td>
            </tr>
            <tr>
                <td><b>Annotation</b></td>
                <td>${bookDetails.getAnnotation()?no_esc}</td>
            </tr>
            <tr>
                <td><b>Year</b></td>
                <td>${bookDetails.getYear()}</td>
            </tr>
            <tr>
                <td><b>Read Online</b></td>
                <td>
                    <#if (bookDetails.getReadLink()??) >
                        <a href="${bookDetails.getReadLink()}" target="_blank">Link</a>
                    <#else>
                        Not Available
                    </#if>
                </td>
            </tr>
            <tr>
                <td><b>Download</b></td>
                <td>
                    <#if (bookDetails.getDownloadLinks()?size > 0)>
                        |
                        <#list bookDetails.getDownloadLinks()?keys as format>
                            &nbsp;&nbsp;<a href="${bookDetails.getDownloadLinks()[format]}" target="_blank">${format}</a>&nbsp;&nbsp;|
                        </#list>
                    <#else>
                        Not Available
                    </#if>
                </td>
            </tr>
        </table>
        <p>
            <a class="btn btn-default" href="http://www.e-reading.club?query=${query}" target="_blank">
                <span class="glyphicon glyphicon-search"></span>
                All Search Results
            </a>
        </p>

    </#if>
</#macro>

<@all_page_content></@all_page_content>