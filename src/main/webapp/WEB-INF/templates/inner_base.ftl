<#ftl output_format="HTML">
<#-- @ftlvariable name="menuBook" type="java.lang.Boolean" -->
<#-- @ftlvariable name="menuBookAdd" type="java.lang.Boolean" -->
<#-- @ftlvariable name="menuHistory" type="java.lang.Boolean" -->
<#-- @ftlvariable name="menuAdmin" type="java.lang.Boolean" -->
<#-- @ftlvariable name="menuAdminUser" type="java.lang.Boolean" -->
<#-- @ftlvariable name="menuAdminInvite" type="java.lang.Boolean" -->
<#-- @ftlvariable name="user" type="jlearn.servlet.entity.User" -->

<#include "base.ftl">

<#macro inner_page_content></#macro>

<#macro body_content>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Books Tracker</a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="dropdown ${(menuBook!false)?then("active", "")}">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        Books <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="${urlHelper.path("/book/list")}"><span class="glyphicon glyphicon-list"></span> All</a></li>
                        <li><a href="${urlHelper.path("/book/list/unread")}"><span class="glyphicon glyphicon-tasks"></span> Unread</a></li>
                        <li role="separator" class="divider"></li>
                        <li class="${(menuBookAdd!false)?then("active", "")}"><a href="#"><span class="glyphicon glyphicon-plus"></span> Add</a></li>
                    </ul>
                </li>
                <li class="${(menuHistory!false)?then("active", "")}">
                    <a href="${urlHelper.path("book-history")}">History of Read</a>
                </li>
                <#if (user.isAdmin()) >
                    <li class="dropdown ${(menuAdmin!false)?then("active", "")}">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                            Admin Area <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li class="${(menuAdminUser!false)?then("active", "")}">
                                <a href="#"><span class="glyphicon glyphicon-user"></span> Users</a>
                            </li>
                            <li class="${(menuAdminInvite!false)?then("active", "")}">
                                <a href="${urlHelper.path("/admin/invite")}"><span class="glyphicon glyphicon-hand-right"></span> Add invite</a>
                            </li>
                        </ul>
                    </li>
                </#if>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        ${user.getEmail()} <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="#"><span class="glyphicon glyphicon-cog"></span> Settings</a></li>
                        <li><a href="${urlHelper.path("/logout")}"><span class="glyphicon glyphicon-log-out"></span> Exit</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container inner-page-container">
    <@inner_page_content/>
</div>

</#macro>
