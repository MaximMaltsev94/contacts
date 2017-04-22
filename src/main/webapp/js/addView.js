var addView = (function() {
    var cityOptions;
    var i, n;

    var monthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    var cropBoxOptions =
        {
            imageBox: '.imageBox',
            thumbBox: '.thumbBox',
            spinner: '.spinner',
            imgSrc: '/sysImages/default.png'
        };

    var cropper;

    return { // методы доступные извне
        selectCountry: function(countryID) {
            document.getElementById('country').value = countryID;
            addView.onChangeCountry(countryID);
        },

        selectCity: function (cityID) {
            if(cityID != 0) {
                main.addScript("https://api.vk.com/method/database.getCitiesById?callback=addView.fillCity&city_ids=" + cityID)
            }
        },

        selectRelationship: function (relationshipID) {
            document.getElementById('relationship').selectedIndex = relationshipID;
        },

        selectGender: function (genderValue) {
            document.getElementById('gender').value = genderValue;
        },

        selectAge: function (age1, age2) {
            document.getElementById('age1').selectedIndex = age1;
            document.getElementById('age2').selectedIndex = age2;
        },

        onChangeCountry: function() {
            document.getElementById('city').innerHTML = '\<option value="0">Не выбрано</option>';
            document.getElementById('cityInput').value = "";
        },

        onCityKeyDown: function (text) {
            var citySelect = document.getElementById('city');
            if(text.length === 0) {
                citySelect.innerHTML = "<option value='0'>Не выбрано</option>";
                citySelect.value = 0;
                return;
            }

            var country_id = document.getElementById('country').value;
            if(country_id != 0) {
                var params = "?q=" + text + "&country_id=" + country_id + "&count=5&callback=addView.fillCities";
                main.addScript("https://api.vk.com/method/database.getCities" + params);
            }
        },

        fillCities: function (data) {
            var citySelect = document.getElementById('city');
            var cities = data.response;
            var innerHTML = "";

            for (var i = 0; i < cities.length; ++i) {
                innerHTML += '<option value="' + cities[i].cid +  '">' + cities[i].title + '</option>';
            }
            citySelect.innerHTML = innerHTML;
            if(cities.length === 0) {
                citySelect.innerHTML = "<option value='0'>Не выбрано</option>";
                citySelect.value = 0;
            }
        },

        fillCity: function (data) {
            var city = data.response[0];
            document.getElementById('city').innerHTML = '<option value="' + city.cid +  '">' + city.name + '</option>';
        },

        readURL: function (input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    cropBoxOptions.imgSrc = e.target.result;
                    cropper = new cropbox(cropBoxOptions);
                };
                reader.readAsDataURL(input.files[0]);
                main.showPopup('cropPopup');
            }
        },

        zoomIn: function () {
            cropper.zoomIn()
        },

        zoomOut: function () {
            cropper.zoomOut()
        },

        cropImage: function () {
            var img = cropper.getDataURL();
            document.getElementById('profileImageData').value = img;
            document.getElementById('blah').setAttribute('src', img);
            document.getElementById('imageAction').value = 'update';
            main.closePopup('cropPopup');
        },

        validateDate: function () {
            var birthDate = document.getElementById('birthDate').value;
            if(birthDate.length === 0)
                return true;

            var elems = birthDate.split('.');
            var dd = parseInt(elems[0]);
            var mm = parseInt(elems[1]) - 1;
            var yyyy = parseInt(elems[2]);

            if(mm >= 0 && mm <= 11 && dd >= 1 && dd <= monthDays[mm] && yyyy > 1950) {
                var dNow = new Date();
                var bDateObj = new Date(yyyy, mm, dd);
                if(bDateObj <= dNow)
                    return true;
            }
            main.showTooltip("Введена недействительная дата", "danger");
            return false;
        },

        onLoadImageClick: function () {
            document.getElementById('profileImage').click();
        },

        onDeleteImageClick: function () {
            document.getElementById("profileImage").value = "";
            document.getElementById("profileImageData").value = "";
            document.getElementById('imageAction').value = 'delete';
            document.getElementById('blah').setAttribute('src', '/sysImages/default.png');
        },

        initCropper: function () {
            cropper = new cropbox(cropBoxOptions);
        },

        setAvatar: function (gender) {
            console.log(gender);
            var img = document.getElementById('blah');
            var manIMG = '/sysImages/default.png';
            var womanIMG = '/sysImages/girl.png';
            if (img.getAttribute('src') === manIMG || img.getAttribute('src') === womanIMG) {
                if (gender == '2') {
                    img.setAttribute('src', '/sysImages/girl.png');
                } else {
                    img.setAttribute('src', '/sysImages/default.png');
                }
            }
        }
    }
}());