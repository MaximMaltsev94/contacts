var showView = (function() {
    var checkedCount = 0;
    var postDeleteRequest = function(id) {
        var form = document.createElement('form');
        form.setAttribute('method', 'post');
        form.setAttribute('action', '/contact/');

        var input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('name', 'action');
        input.value = 'delete';
        form.appendChild(input);

        input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('name', 'id');
        input.value = id;
        form.appendChild(input);

        document.body.appendChild(form);
        form.submit();
    };

    return { // методы доступные извне
        onCheckBoxChecked: function(sender) {
            if(sender.checked)
                checkedCount++;
            else
                checkedCount--;
            document.getElementById('deleteSelected').disabled = checkedCount == 0;
        },

        onDeleteSelectedClick : function () {
            if(confirm('Вы действительно хотите удалить выбранные контакты?')) {
                var checkedBoxes = document.querySelectorAll("input[type=checkbox]:checked");
                var ids = '';
                for (var i = 0; i < checkedBoxes.length; ++i) {
                    ids += checkedBoxes[i].id + ',';
                }
                postDeleteRequest(ids);
            }
        },

        onDeleteContact: function(firstName, lastName, id) {
            if (confirm('Вы действительно хотите удалить контакт ' + firstName + ' ' + lastName + '?')) {
                postDeleteRequest(id);
            }
        }
    }
}());