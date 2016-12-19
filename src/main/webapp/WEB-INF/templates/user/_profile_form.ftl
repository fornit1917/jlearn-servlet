<#ftl output_format="HTML">
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="success" type="java.lang.String" -->
<#-- @ftlvariable name="redirectUrl" type="java.lang.String" -->
<#-- @ftlvariable name="editableUser" type="jlearn.servlet.dto.User" -->

<#if ((error!"") != "") >
<div class="alert alert-danger" role="alert">
${error}
</div>
</#if>

<#if ((success!"") != "") >
<div class="alert alert-success" role="alert">
${success}
</div>
</#if>

<form class="form form-horizontal app-profile-form" method="POST">
    <fieldset>
        <legend>Settings</legend>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="is_public" ${editableUser.isPublic()?then("checked", "")} value="1">
                        Public profile
                    </label>
                </div>
            </div>
        </div>
    </fieldset>
    <fieldset>
        <legend>Change password</legend>
        <div class="form-group">
            <label for="inputPassword" class="col-sm-2 control-label">New password</label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" id="inputPassword" placeholder="New password">
            </div>
        </div>

        <div class="form-group">
            <label for="inputPasswordRepeat" class="col-sm-2 control-label">New password repeat</label>
            <div class="col-sm-6">
                <input type="password" name="password_repeat" class="form-control" id="inputPasswordRepeat" placeholder="New password repeat">
            </div>
        </div>
    </fieldset>

    <#if ((redirectUrl!"") != "") >
        <input type="hidden" name="redirectUrl" value="${redirectUrl}"/>
    </#if>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </div>
</form>