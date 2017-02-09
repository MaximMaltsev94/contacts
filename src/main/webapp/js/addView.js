var addView = (function() {
    var cityOptions;
    var i, n;

    var monthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

    return { // методы доступные извне
        selectCountry: function(countryID) {
            document.getElementById('country').selectedIndex = countryID;
            this.onChangeCountry(countryID);
        },

        selectCity: function (cityID) {
            var options = document.getElementById('city');

            for(i = 0, n = options.length; i < n; ++i) {
                if(options[i].value == cityID) {
                    document.getElementById('city').selectedIndex = i;
                    return
                }
            }
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
            var citySelect = document.getElementById('city');
            citySelect.innerHTML = '\<option value="0" data-country="0">Не выбрано</option>';
            var citySelectHelper = document.getElementById('cityData');

            cityOptions = citySelectHelper.options;
            for(i = 0, n = cityOptions.length; i < n; i++) {
                if(cityOptions[i].dataset.country == selectedValue) {
                    var option = document.createElement("option");
                    option.value = cityOptions[i].value;
                    option.dataset.country = selectedValue;
                    option.textContent = cityOptions[i].textContent;
                    citySelect.appendChild(option);
                    // citySelect.appendChild(cityOptions[i]);
                }
            }
            document.getElementById('city').selectedIndex = 0;
        },

        readURL: function (input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    document.getElementById('imageAction').value = 'update';
                    document.getElementById('blah').setAttribute('src', e.target.result);
                };

                reader.readAsDataURL(input.files[0]);
            }
        },

        validateDate: function () {
            var birthDate = document.getElementById('birthDate').value;
            if(birthDate.length == 0)
                return true;

            var elems = birthDate.split('.');
            var dd = parseInt(elems[0]);
            var mm = parseInt(elems[1]) - 1;
            var yyyy = parseInt(elems[2]);

            if(mm >= 0 && mm <= 11) {
                if(dd >= 1 && dd <= monthDays[mm]) {
                    if(yyyy > 1950) {
                        var dNow = new Date();
                        var bDateObj = new Date(yyyy, mm, dd);
                        console.log("Day + " + bDateObj.getDay());
                        console.log("Month + " + bDateObj.getMonth());
                        if(bDateObj <= dNow)
                            return true;
                    }
                }
            }
            main.showTooltip("Введена недействительная дата", "danger");
            return false;
        },

        onLoadImageClick: function () {
            document.getElementById('profileImage').click();
        },

        onDeleteImageClick: function () {
            document.getElementById("profileImage").value = "";
            document.getElementById('imageAction').value = 'delete';
            document.getElementById('blah').setAttribute('src', '/sysImages/default.png');
        }
    }
}());