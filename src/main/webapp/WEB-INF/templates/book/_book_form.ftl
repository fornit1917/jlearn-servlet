<#ftl output_format="HTML">
<#-- @ftlvariable name="statuses" type="jlearn.servlet.dto.BookStatus[]" -->
<#-- @ftlvariable name="book" type="jlearn.servlet.dto.Book" -->
<#-- @ftlvariable name="bookReading" type="jlearn.servlet.dto.BookReading" -->

<#if ((error!"") != "") >
    <div class="alert alert-danger" role="alert">
        ${error}
    </div>
</#if>

<form class="form form-horizontal book-form" method="POST">

    <fieldset>
        <div class="form-group">
            <label for="inputAuthor" class="col-sm-1 control-label">Author</label>
            <div class="col-sm-6">
                <input type="text" name="author" required value="${book.getAuthor()}" class="form-control" id="inputAuthor" placeholder="Author">
            </div>
        </div>

        <div class="form-group">
            <label for="inputTitle" class="col-sm-1 control-label">Title</label>
            <div class="col-sm-6">
                <input type="text" name="title" required value="${book.getTitle()}" class="form-control" id="inputTitle" placeholder="Title">
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-1 col-sm-10">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="is_fiction" id="checkboxIsFiction" ${book.isFiction()?then("checked", "")} name="is_fiction" value="1">
                        It's fiction (belles-lettres)
                    </label>
                </div>
            </div>
        </div>
    </fieldset>

    <#include "_reading_subform.ftl">

    <div class="form-group">
        <div class="col-sm-offset-1 col-sm-10">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </div>

</form>

