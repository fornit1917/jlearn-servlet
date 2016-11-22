<#-- @ftlvariable name="statuses" type="jlearn.servlet.entity.BookStatus[]" -->
<#-- @ftlvariable name="book" type="jlearn.servlet.entity.Book" -->
<form class="form form-horizontal book-form">

    <div class="form-group">
        <label for="inputAuthor" class="col-sm-2 control-label">Author</label>
        <div class="col-sm-6">
            <input type="text" value="${book.getAuthor()}" class="form-control" id="inputAuthor" placeholder="Author">
        </div>
    </div>

    <div class="form-group">
        <label for="inputTitle" class="col-sm-2 control-label">Title</label>
        <div class="col-sm-10">
            <input type="text" value="${book.getTitle()}" class="form-control" id="inputTitle" placeholder="Title">
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <div class="checkbox">
                <label>
                    <input type="checkbox" id="checkboxIsFiction" ${book.isFiction()?then("checked", "")} name="is_fiction" value="1">
                    It's fiction (belles-lettres)
                </label>
            </div>
        </div>
    </div>

    <div class="form-group">
        <label for="selectStatus" class="col-sm-2 control-label">Status</label>
        <div class="col-sm-6">
            <select class="form-control" id="selectStatus" name="status">
                <#list statuses as status >
                    <option value="${status.getValue()}">${status}</option>
                </#list>
            </select>
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </div>

</form>