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

var ReadingInfoForm = {
    STATUS_UNREAD: 1,
    STATUS_IN_PROGRESS: 2,
    STATUS_FINISHED: 3,
    STATUS_ABORTED: 4,
};

ReadingInfoForm.init = function (nodeOrSelector) {
    var $form = $(nodeOrSelector);
    var $statusDropdown = $form.find(".book-status");
    var $historyForm = $form.find(".reading-history-item-form")
    var $timeStart = $form.find(".time-start");
    var $timeEnd = $form.find(".time-end");

    function updateControlsVisible() {
        var status = parseInt($statusDropdown.val());
        switch (status) {
            case ReadingInfoForm.STATUS_UNREAD:
                $historyForm.hide();
                break;
            case ReadingInfoForm.STATUS_IN_PROGRESS:
                $historyForm.show();
                $timeStart.show();
                $timeEnd.hide();
                break;
            case ReadingInfoForm.STATUS_FINISHED:
            case ReadingInfoForm.STATUS_ABORTED:
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

ReadingHistory.init = function (nodeOrSelector, requestUrl, data) {
    console.log(data);
};