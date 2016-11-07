<#ftl output_format="HTML">
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
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                        Books <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="#"><a href="#"><span class="glyphicon glyphicon-list"></span> All</a></li>
                        <li><a href="#"><a href="#"><span class="glyphicon glyphicon-tasks"></span> Unread</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="#"><a href="#"><span class="glyphicon glyphicon-plus"></span> Add</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#">History of Read</a>
                </li>
                <#if (user.isAdmin()) >
                    <li>
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                            Admin Area <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a href="#"><a href="#"><span class="glyphicon glyphicon-user"></span> Users</a></li>
                            <li><a href="#"><a href="#"><span class="glyphicon glyphicon-hand-right"></span> Invites</a></li>
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
</#macro>
