var showView = (function() {
    var checkedCount = 0;
    return { // методы доступные извне
        onCheckBoxChecked: function(sender) {
            if(sender.checked)
                checkedCount++;
            else
                checkedCount--;
            document.getElementById('deleteSelected').disabled = checkedCount == 0;
        },

        onDeleteSelectedClick : function () {
            var checkedBoxes = document.querySelectorAll("input[type=checkbox]:checked");
            for(var i = 0; i < checkedBoxes.length; ++i) {
                var formID = checkedBoxes[i].dataset.form;
                document.getElementById(formID).submit();
            }
        },

        onDeleteContact: function(firstName, lastName, formId) {
            if (confirm('Вы действительно хотите удалить контакт ' + firstName + ' ' + lastName + '?')) {
                document.getElementById(formId).submit();
            }
        }
    }
}());