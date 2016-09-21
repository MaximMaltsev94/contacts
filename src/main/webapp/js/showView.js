/**
 * Created by maxim on 21.09.2016.
 */
var showView = (function() {
    return { // методы доступные извне
        onDeleteContact: function(firstName, lastName, formId) {
            if (confirm('Вы действительно хотите удалить контакт ' + firstName + ' ' + lastName + '?')) {
                document.getElementById(formId).submit();
            }
        }
    }
}());