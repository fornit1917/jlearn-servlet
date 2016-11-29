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