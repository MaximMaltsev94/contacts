var addView = (function() {
    var cityOptions;
    var i, n;

    return { // методы доступные извне
        selectCountry: function(countryID) {
            document.getElementById('country').selectedIndex = countryID;
            this.onChangeCountry(countryID);
        },

        selectCity: function (cityID) {
            document.getElementById('city').selectedIndex = cityID;
        },

        selectRelationship: function (relationshipID) {
            document.getElementById('relationship').selectedIndex = relationshipID;
        },

        selectGender: function (genderValue) {
            if(genderValue == true)
                genderValue = 1;
            else if(genderValue == false)
                genderValue = 0;
            document.getElementById('r' + genderValue).checked = true;
        },

        selectAge: function (age1, age2) {
            document.getElementById('age1').selectedIndex = age1;
            document.getElementById('age2').selectedIndex = age2;
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