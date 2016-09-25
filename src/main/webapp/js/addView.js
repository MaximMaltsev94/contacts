var addView = (function() {
    var cityOptions;
    var i, n;
    return { // методы доступные извне
        deletePhoneElement: function (sender) {
            document.getElementById('phoneSection').removeChild(sender.parentNode.parentNode);
        },

        editPhoneElement: function (sender) {
            var phoneID = sender.parentNode.parentNode.id;
            console.log(phoneID);
            document.getElementById('popup_phoneType').selectedIndex = document.getElementById('type_' + phoneID).value;
            document.getElementById('popup_countryCode').selectedIndex = document.getElementById('country_code_' + phoneID).value;
            document.getElementById('popup_operatorCode').value = document.getElementById('op_code_' + phoneID).value;
            document.getElementById('popup_phoneNumber').value = document.getElementById('number_' + phoneID).value;
            document.getElementById('popup_comment').value = document.getElementById('comment_' + phoneID).value;
            location.hash = "phonePopup";
        },

        createNodeFromPopup: function () {

        },

        selectCountryAndCity: function(countryID, cityID) {
            document.getElementById('country').selectedIndex = countryID;
            this.onChangeCountry(countryID);
            document.getElementById('city').selectedIndex = cityID;
        },
        onChangeCountry: function(selectedValue) {
            cityOptions = document.getElementById('city').options;
            for(i = 0, n = cityOptions.length; i < n; i++) {
                cityOptions[i].style.display = 'none';
                if(cityOptions[i].dataset.country == selectedValue) {
                    cityOptions[i].style.display = 'block';
                }
            }
            cityOptions[0].style.display = 'block';
            document.getElementById('city').selectedIndex = 0;
        },

        readURL: function (input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    document.getElementById('blah').setAttribute('src', e.target.result);
                };

                reader.readAsDataURL(input.files[0]);
            }
        }
    }
}());