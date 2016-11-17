<#ftl output_format="HTML">

<#macro pager pageResult >
    <#-- @ftlvariable name="pageResult" type="jlearn.servlet.service.utility.PageResult" -->

    <#if (pageResult.getTotalPages() > 1) >
        <#assign start = pageResult.getPageNum() - 5>
        <#assign end = pageResult.getPageNum() + 5>
        <#if (start < 1) >
            <#assign start = 1>
        </#if>
        <#if (end > pageResult.getTotalPages()) >
            <#assign end = pageResult.getTotalPages()>
        </#if>

        <#assign url = urlHelper.parseUrl(requestUrl)>

        <ul class="pagination pagination-sm">
            <#if (pageResult.getPageNum() > 1) >
                <li><a href="${url.setQueryParam("page", 1).toString()}" aria-label="First"><span aria-hidden="true">&lt;&lt;</span></a></li>
                <li><a href="${url.setQueryParam("page", pageResult.getPageNum() - 1).toString()}" aria-label="Previous"><span aria-hidden="true">&lt;</span></a></li>
            </#if>

            <#list start..end as num>
                <li class="${(pageResult.getPageNum() == num)?then("active", "")}"><a href="${url.setQueryParam("page", num).toString()}">${num}</a></li>
            </#list>

            <#if (pageResult.hasNext()) >
                <li><a href="${url.setQueryParam("page", pageResult.getPageNum() + 1).toString()}" aria-label="Next"><span aria-hidden="true">&gt;</span></a></li>
                <li><a href="${url.setQueryParam("page", pageResult.getTotalPages()).toString()}" aria-label="Last"><span aria-hidden="true">&gt;&gt;</span></a></li>
            </#if>
        </ul>
    </#if>

</#macro>