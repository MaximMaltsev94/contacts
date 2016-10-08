var addView = (function() {
    var cityOptions;
    var i, n;

    var monthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

    var strToDate = function (str) {
        var elems = str.split('.');
        return new Date(elems[2], elems[1] - 1, elems[0]);
    };

    var validateDate = function () {
        var birthDate = document.getElementById('birthDate').value;
        if(birthDate.length == 0)
            return true;

        var elems = birthDate.split('.');
        var dd = parseInt(elems[0]);
        var mm = parseInt(elems[1]) - 1;
        var yyyy = parseInt(elems[2]);

        if(mm >= 0 && mm <= 11) {
            if(dd >= 1 && dd <= monthDays[mm]) {
                var dNow = new Date();
                var bDateObj = strToDate(birthDate);
                console.log("Day + " + bDateObj.getDay());
                console.log("Month + " + bDateObj.getMonth());
                return dNow >= bDateObj;

            }
        }
        return false;
    };

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
        },
        onSubmitContactForm: function(formID) {
            if(validateDate() == true) {
                document.getElementById(formID).submit();
            } else {
                alert("Введена недействительная дата");
            }
        }
    }
}());