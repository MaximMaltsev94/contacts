var addView = (function() {
    var cityOptions;
    var i, n;

    var monthDays = [31, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    var cropBoxOptions =
        {
            imageBox: '.imageBox',
            thumbBox: '.thumbBox',
            spinner: '.spinner',
            imgSrc: '/sysImages/default.png'
        };

    var cropper;

    var getOption = function (cid, inputText, title) {
        return '<div class="hoverable text-small-bold" onmousedown="addView.onSelectCityClick(' + cid + ', \'' + inputText + '\')">' + title + '</div>';
    };

    var setDefaultSelect = function () {
        document.getElementById('cityDropdownContent').innerHTML = getOption(0, "", "Не выбрано");
        document.getElementById('cityInput').value = "";
        document.getElementById('city').value = 0;
    };

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

        selectBirthDay: function (day, month, year) {
            document.getElementById('birth_day').value = day;
            document.getElementById('birth_month').value = month;
            document.getElementById('birth_year').value = year;
        },

        selectAge: function (age1, age2) {
            document.getElementById('age1').selectedIndex = age1;
            document.getElementById('age2').selectedIndex = age2;
        },

        onChangeCountry: function(countryID) {
            var row = document.getElementById('cityRow');
            row.style.display = countryID == 0 ? 'none' : 'flex';
            setDefaultSelect();
        },

        onChangeMonth: function () {
            var birthDayElement = document.getElementById('birth_day');
            var month = document.getElementById('birth_month').value;
            if(birthDayElement.value > monthDays[month]) {
                birthDayElement.value = 0;
                var innerHtml = '<option value="0">Не выбрано</option>';
                for (var i = 1; i <= monthDays[month]; ++i)
                    innerHtml += '<option value="' + i + '">' + i + '</option>';
                birthDayElement.innerHTML = innerHtml;
            }
        },

        onCityKeyDown: function (text) {
            document.getElementById('city').value = 0;
            var citySelect = document.getElementById('cityDropdownContent');
            if(text.length === 0) {
                setDefaultSelect();
                return;
            }

            var country_id = document.getElementById('country').value;
            if(country_id != 0) {
                var params = "?q=" + text + "&country_id=" + country_id + "&count=5&callback=addView.fillCities";
                main.addScript("https://api.vk.com/method/database.getCities" + params);
            }
        },

        onCityBlur: function () {
            console.log('OnBlur');
            if(document.getElementById('city').value == 0) {
                setDefaultSelect();
            }
        },

        onSelectCityClick: function (cid, title) {
            document.getElementById('city').value = cid;
            document.getElementById('cityInput').value = title;
        },

        fillCities: function (data) {
            var cities = data.response;
            var innerHTML = "";

            for (var i = 0; i < cities.length; ++i) {
                innerHTML += getOption(cities[i].cid, cities[i].title, cities[i].title);
            }
            var content = document.getElementById('cityDropdownContent');
            content.innerHTML = innerHTML;
            if(cities.length === 0) {
                content.innerHTML = getOption(0, "", "Не выбрано");
                addView.onSelectCityClick(0, document.getElementById('cityInput').value);
            }
        },

        fillCity: function (data) {
            var city = data.response[0];
            document.getElementById('cityDropdownContent').innerHTML += getOption(city.cid, city.name, city.name);
            addView.onSelectCityClick(city.cid, city.name);
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