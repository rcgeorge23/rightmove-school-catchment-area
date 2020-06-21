var lcag = lcag || {};

lcag.Common = lcag.Common || {
    urlPrefix: "",
    alertPleaseWait: function() {
         toastr.info("Please wait...", {
            "timeOut": "0",
            "extendedTimeout": "0",
            "maxOpened": "1"
        });
    },
    hidePleaseWait: function() {
        $("div.toast.toast-info").remove();
    },
    alertSuccess: function() {
        toastr.success("Updated successfully", {
            "maxOpened": "1"
        });
    },
    alertError: function(message) {
        if (message != null && message != "") {
            toastr.error(message);
        } else {
            toastr.error("An error occurred");
        }
    }
}