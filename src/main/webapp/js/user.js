var userView = (function () {
    return {
        deleteUser: function () {
            if(confirm("Это действие невозможно отменить. Вы действительно хотите удалить учетную запись?")) {
                document.getElementById('deleteUserForm').submit();
            }
        }
    }
}());
