var loginView = (function () {
    return {
        validatePassword: function () {
            var pass1 = document.getElementById('pass1').value;
            var pass2 = document.getElementById('pass2').value;
            console.log(pass1);
            console.log(pass2);
            if(pass1 !== pass2) {
                main.showTooltip('Пароли не совпадают', 'danger');
                return false;
            }
            return true;
        },

        validateRegistrationForm: function () {
            return loginView.validatePassword();
        },

        showRestorePasswordPopup: function () {
            main.showPopup('restorePassword');
        },

        closeRestorePasswordPopup: function () {
            main.closePopup('restorePassword');
        },

        redirectForgotPassword: function () {
            var login = document.getElementById('restorePasswordLogin').value;
            main.postRequest("/contact/forgotPassword", "login", login);
        }
    }
}());
