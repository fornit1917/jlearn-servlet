var AddInviteForm = {};

AddInviteForm.init = function (nodeOrSelector) {
    var $form = $(nodeOrSelector);
    var $code = $form.find(".invite-code");
    var $button = $form.find("button[type='submit']");
    this._setButtonText($code, $button);
    $code.on("input", function () {
        AddInviteForm._setButtonText($code, $button);
    });
};

AddInviteForm._setButtonText = function ($code, $button) {
    if ($code.val().trim().length > 0) {
        $button.text("Add");
    } else {
        $button.text("Generate");
    }
};



var UserFilterForm = {};

UserFilterForm.init = function (nodeOrSelector) {
    var $form = $(nodeOrSelector);
    var $state = $form.find(".user-filter-state");
    $state.on("change", function () {
        $form.submit();
    });
}


var BookFilterForm = {};

BookFilterForm.init = function (nodeOrSelector) {
    var $form = $(nodeOrSelector);
    var $dropdowns = $form.find("select");
    $dropdowns.on("change", function () {
        $form.submit();
    })
};


var BookStatus = {
    UNREAD: 1,
    IN_PROGRESS: 2,
    FINISHED: 3,
    ABORTED: 4,    
};

var ReadingInfoForm = {};

ReadingInfoForm.init = function (nodeOrSelector) {
    var $form = $(nodeOrSelector);
    var $statusDropdown = $form.find(".book-status");
    var $historyForm = $form.find(".reading-history-item-form")
    var $timeStart = $form.find(".time-start");
    var $timeEnd = $form.find(".time-end");

    function updateControlsVisible() {
        var status = parseInt($statusDropdown.val());
        switch (status) {
            case BookStatus.UNREAD:
                $historyForm.hide();
                break;
            case BookStatus.IN_PROGRESS:
                $historyForm.show();
                $timeStart.show();
                $timeEnd.hide();
                break;
            case BookStatus.FINISHED:
            case BookStatus.ABORTED:
                $historyForm.show();
                $timeStart.show();
                $timeEnd.show();
                break;
        }
    }

    $statusDropdown.on("change", updateControlsVisible);

    updateControlsVisible();
};


var DeleteButtons = {

};

DeleteButtons.init = function (nodeOrSelectorForm) {
    var $forms = $(nodeOrSelectorForm);
    $forms.submit(function (e) {
        var message = $(this).attr("data-message");
        if (typeof message === "undefined") {
            message = "Are you sure you want to delete?";
        }
        if (!confirm(message + "")) {
            e.preventDefault();
            return false;
        }
        return true;
    });
};


var ReadingHistory = {};

ReadingHistory.YearViewModel = function (year, items) {
    this.year = year;
    this.items = ko.observableArray(items);
};

ReadingHistory.ItemViewModel = function (item) {
    this.title = item.title;
    this.author = item.author;
    this.start = item.start;
    this.end = item.end;
    this.isReread = item.isReread;
    this.status = item.status;
    this.statusName = item.statusName;
};

ReadingHistory.ItemViewModel.prototype.isInactive = function () {
    return this.status == BookStatus.ABORTED || this.isReread;
};

ReadingHistory.ItemViewModel.prototype.getDisplayStatus = function () {
    if (this.isReread) {
        return "Reread";
    }
    return this.statusName;
};

ReadingHistory.ItemViewModel.prototype.showDate = function (dateType) {
    if (dateType != "start" && dateType != "end") {
        console.log("Wrong date type: " + dateType);
        return "";
    }
    return this[dateType] !== "";
};

ReadingHistory.init = function (nodeOrSelector, requestUrl, data) {
    var viewModelData = this._prepareViewModelData(data);
    var viewModel = {
        years: Object.keys(viewModelData).sort().reverse().map(function (year) {
            return viewModelData[year]
        }),
    }
    ko.applyBindings(viewModel, $(nodeOrSelector).get(0));
};

ReadingHistory._prepareViewModelData = function (data) {
    var result = {};
    for (var key in data.data) {
        var item = data.data[key];
        var year = item.year;
        if (typeof result[year] === "undefined") {
            result[year] = new ReadingHistory.YearViewModel(year, []);
        }
        result[year].items.push(new ReadingHistory.ItemViewModel(item));
    }
    return result;
};
