var showView = (function() {
    var checkedCount = 0;
    window.onhashchange = function () {
        checkedCount = document.querySelectorAll("input[type=checkbox]:checked").length;
        if(checkedCount > 0) {
            document.getElementById('deleteSelected').disabled = false;
            document.getElementById('sendEmail').disabled = false;
        }
    };

    var postDeleteRequest = function(id) {
        var form = document.createElement('form');
        form.style.visibility = 'hidden';
        form.setAttribute('method', 'post');
        form.setAttribute('action', contextPath + '/contact/delete');

        var input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('name', 'id');
        input.value = id;
        form.appendChild(input);

        document.body.appendChild(form);
        form.submit();
    };

    var postEmailRequest = function (id) {
        var form = document.createElement('form');
        form.style.visibility = 'hidden';
        form.setAttribute('method', 'post');
        form.setAttribute('action', contextPath + '/contact/email');

        var input = document.createElement('input');
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
            else {
                checkedCount--;
                checkedCount = Math.max(checkedCount, 0);
            }
            document.getElementById('deleteSelected').disabled = checkedCount == 0;
            document.getElementById('sendEmail').disabled = checkedCount == 0;
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
        },

        onEmailContact: function (id) {
            postEmailRequest(id);
        },

        onEmailSelectedClick: function (id) {
            var checkedBoxes = document.querySelectorAll("input[type=checkbox]:checked");
            var ids = '';
            for (var i = 0; i < checkedBoxes.length; ++i) {
                ids += checkedBoxes[i].id + ',';
            }
            postEmailRequest(ids);
        }


    }
}());