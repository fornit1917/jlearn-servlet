<#-- @ftlvariable name="statuses" type="jlearn.servlet.entity.BookStatus[]" -->
<#-- @ftlvariable name="book" type="jlearn.servlet.entity.Book" -->

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

    <fieldset class="reading-info-form" id="reading-info-form">
        <legend>Reading info</legend>

        <div class="form-group">
            <label for="select-status" class="col-sm-1 control-label">Status</label>
            <div class="col-sm-6">
                <select name="status" class="form-control book-status" id="select-status">
                <#list statuses as status >
                    <option ${(book.getStatus().getValue() == status.getValue())?then("selected", "")} value="${status.getValue()}">${status}</option>
                </#list>
                </select>
            </div>
        </div>

        <div class="reading-history-item-form" style="display: none">

            <div class="form-group">
                <div class="col-sm-offset-1 col-sm-10">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="is_reread" id="checkbox-is-reread" name="is_reread" value="1">
                            It's reread
                        </label>
                    </div>
                </div>
            </div>

            <div class="form-group time-start">
                <label class="col-sm-1">Start</label>
                <div class="col-sm-2">
                    <div class="help">Year</div>
                    <select name="start_year" class="form-control">
                        <option>2016</option>
                        <option>2015</option>
                        <option>2014</option>
                        <option>2013</option>
                        <option>2012</option>
                        <option>2011</option>
                    </select>
                </div>
                <div class="col-sm-4">
                    <div class="help">Month</div>
                    <select name="start_month" class="form-control">
                        <option>January</option>
                        <option>February</option>
                        <option>March</option>
                        <option>April</option>
                        <option>May</option>
                        <option>June</option>
                        <option>July</option>
                        <option>August</option>
                        <option>September</option>
                        <option>October</option>
                        <option>November</option>
                        <option>December</option>
                    </select>
                </div>
            </div>

            <div class="form-group time-end">
                <label class="col-sm-1">End</label>
                <div class="col-sm-2">
                    <div class="help">Year</div>
                    <select name="end_year" class="form-control">
                        <option>2016</option>
                        <option>2015</option>
                        <option>2014</option>
                        <option>2013</option>
                        <option>2012</option>
                        <option>2011</option>
                    </select>
                </div>
                <div class="col-sm-4">
                    <div class="help">Month</div>
                    <select name="end_month" class="form-control">
                        <option>January</option>
                        <option>February</option>
                        <option>March</option>
                        <option>April</option>
                        <option>May</option>
                        <option>June</option>
                        <option>July</option>
                        <option>August</option>
                        <option>September</option>
                        <option>October</option>
                        <option>November</option>
                        <option>December</option>
                    </select>
                </div>
            </div>
        </div>
    </fieldset>
    <script>
        $(function () {
            ReadingInfoForm.init("#reading-info-form");
        });
    </script>


    <div class="form-group">
        <div class="col-sm-offset-1 col-sm-10">
            <button type="submit" class="btn btn-primary">Save</button>
        </div>
    </div>

</form>

