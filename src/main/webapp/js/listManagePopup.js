var listManagePopup = (function () {
    var checkedCount = 0;
    var selectedCheckboxes = function () {
        return document.querySelectorAll("#listManagePopup input[type=checkbox]:checked");
    };

    return {
        onCheckBoxChecked: function (sender) {
            if(sender.checked)
                checkedCount++;
            else {
                checkedCount--;
                checkedCount = Math.max(checkedCount, 0);
            }
            document.getElementById('sendEmailGroup').disabled = checkedCount === 0;
            document.getElementById('deleteGroup').disabled = checkedCount === 0;
        },


        onDeleteSelectedClick : function () {
            var checkedBoxes = selectedCheckboxes();
            if(checkedBoxes.length === 0)
                return;

            var ids = '';
            for (var i = 0; i < checkedBoxes.length; ++i) {
                ids += checkedBoxes[i].id + ',';
            }
            main.postRequest('/contact/deleteList', 'id', ids);
        },

        onDeleteGroup: function(id) {
            main.postRequest('/contact/deleteList', 'id', 'manage-group-' + id);
        },

        onEmailGroup: function (id) {
            main.postRequest('/contact/email', 'groupId', id);
        },

        onEmailSelectedClick: function () {
            var checkedBoxes = selectedCheckboxes();
            var ids = '';
            for (var i = 0; i < checkedBoxes.length; ++i) {
                ids += checkedBoxes[i].id + ',';
            }
            main.postRequest('/contact/email', 'groupId', ids);
        },

        closeListManagePopup: function () {
            main.closePopup('listManagePopup');
            var elems = selectedCheckboxes();
            for(var i = 0; i < elems.length; ++i) {
                elems[i].checked = false;
            }

            document.getElementById('sendEmailGroup').disabled = true;
            document.getElementById('deleteGroup').disabled = true;

        },

        showListManagePopup: function () {
            main.showPopup('listManagePopup');
        }
    }
})();