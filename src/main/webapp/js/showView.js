var showView = (function() {
    var checkedCount = 0;
    var selectedCheckboxes = function () {
        return document.querySelectorAll("input[type=checkbox]:checked")
    };

    window.onhashchange = function () {
        checkedCount = selectedCheckboxes().length;
        if(checkedCount > 0) {
            document.getElementById('deleteSelected').disabled = false;
            document.getElementById('sendEmail').disabled = false;
        }
    };

    return { // методы доступные извне
        onCheckBoxChecked: function(sender) {
            if(sender.checked)
                checkedCount++;
            else {
                checkedCount--;
                checkedCount = Math.max(checkedCount, 0);
            }
            document.getElementById('deleteSelected').disabled = checkedCount === 0;
            document.getElementById('sendEmail').disabled = checkedCount === 0;
        },

        onDeleteSelectedClick : function () {
            var checkedBoxes = selectedCheckboxes();
            if(checkedBoxes.length === 0)
                return;

            var ids = '';
            for (var i = 0; i < checkedBoxes.length; ++i) {
                ids += checkedBoxes[i].id + ',';
            }
            main.postRequest('/contact/delete', 'id', ids);
        },

        onDeleteContact: function(id) {
            main.postRequest('/contact/delete', 'id', id);
        },

        onEmailContact: function (id) {
            main.postRequest('/contact/email', 'id', id);
        },

        onEmailSelectedClick: function (id) {
            var checkedBoxes = selectedCheckboxes();
            var ids = '';
            for (var i = 0; i < checkedBoxes.length; ++i) {
                ids += checkedBoxes[i].id + ',';
            }
            main.postRequest('/contact/email', 'id', ids);
        }


    }
}());