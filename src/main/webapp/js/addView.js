var addView = (function() {
    var cityOptions;
    var i, n;
    var phoneCount;
    var phoneID;
    var popupSubmit;

    var validatePopup = function () {
        var operatorCodeRegex = /[0-9]{2,5}/;
        var phoneRegex = /[0-9]{4,9}/;

        return operatorCodeRegex.test(document.getElementById('popup_operatorCode').value)
                && phoneRegex.test(document.getElementById('popup_phoneNumber').value)
                && (document.getElementById('popup_comment').value.length < 255);
    };

    var fillPopup = function (type, countryCode, operatorCode, phoneNumber, comment) {
        document.getElementById('popup_phoneType').selectedIndex = type;
        document.getElementById('popup_countryCode').selectedIndex = countryCode;
        document.getElementById('popup_operatorCode').value = operatorCode;
        document.getElementById('popup_phoneNumber').value = phoneNumber;
        document.getElementById('popup_comment').value = comment;
    };

    var parsePopup = function(targetPhoneId) {
        var popupPhoneType = document.getElementById('popup_phoneType');
        var popupCountryCode = document.getElementById('popup_countryCode');
        var popupOperatorCode = document.getElementById('popup_operatorCode');
        var popupPhoneNumber = document.getElementById('popup_phoneNumber');
        var popupComment = document.getElementById('popup_comment');

        document.getElementById('type_' + targetPhoneId).value = popupPhoneType.selectedIndex;
        document.getElementById('country_code_' + targetPhoneId).value = popupCountryCode.selectedIndex;
        document.getElementById('op_code_' + targetPhoneId).value = popupOperatorCode.value;
        document.getElementById('number_' + targetPhoneId).value = popupPhoneNumber.value;
        document.getElementById('comment_' + targetPhoneId).value = popupComment.value;

        document.getElementById('display_type_' + targetPhoneId).textContent = popupPhoneType.options[popupPhoneType.selectedIndex].text;
        document.getElementById('display_number_' + targetPhoneId).textContent = popupCountryCode.options[popupCountryCode.selectedIndex].text + popupOperatorCode.value + popupPhoneNumber.value;
        document.getElementById('display_comment_' + targetPhoneId).textContent = popupComment.value;
    };

    var createInput = function (id) {
        var inputElement = document.createElement('input');
        inputElement.type = 'hidden';
        inputElement.id = id;
        inputElement.name = id;
        return inputElement;
    };
    
    var createDiv = function (id, className) {
        var divElement = document.createElement('div');
        if(id.length != 0)
            divElement.id = id;
        divElement.className = className;
        return divElement;
    };

    var createPhoneTemplate = function (targetPhoneId) {
        var mainDiv = createDiv(targetPhoneId, 'jlab-row margin');

        var childElement = createInput('type_' + targetPhoneId);
        mainDiv.appendChild(childElement);
        childElement = createInput('country_code_' + targetPhoneId);
        mainDiv.appendChild(childElement);
        childElement = createInput('op_code_' + targetPhoneId);
        mainDiv.appendChild(childElement);
        childElement = createInput('number_' + targetPhoneId);
        mainDiv.appendChild(childElement);
        childElement = createInput('comment_' + targetPhoneId);
        mainDiv.appendChild(childElement);

        childElement = createDiv('display_type_' + targetPhoneId, 'jlab-cell-3 align-right text-small');
        mainDiv.appendChild(childElement);

        childElement = createDiv('', 'jlab-cell-3');
        var subChildElement = createDiv('display_number_' + targetPhoneId, 'jlab-row text-medium');
        childElement.appendChild(subChildElement);
        subChildElement = createDiv('display_comment_' + targetPhoneId, 'jlab-row text text-small');
        childElement.appendChild(subChildElement);
        mainDiv.appendChild(childElement);

        childElement = createDiv('', 'jlab-cell-1');
        subChildElement = createDiv('', 'imageButton edit');
        subChildElement.onclick = function () {
            return addView.showEditPhonePopup(subChildElement)
        };
        childElement.appendChild(subChildElement);
        mainDiv.appendChild(childElement);

        childElement = createDiv('', 'jlab-cell-1');
        subChildElement = createDiv('', 'imageButton delete');
        subChildElement.onclick = function () {
            return addView.deletePhoneElement(subChildElement)
        };
        childElement.appendChild(subChildElement);
        mainDiv.appendChild(childElement);

        document.getElementById('phoneSection').appendChild(mainDiv);
    };

    return { // методы доступные извне
        setPhoneCount: function (val) {
            phoneCount = val;
        },


        deletePhoneElement: function (sender) {
            document.getElementById('phoneSection').removeChild(sender.parentNode.parentNode);
        },

        showAddPhonePopup: function () {
            popupSubmit = document.getElementById('popup_ok');
            popupSubmit.textContent = 'Добавить';
            popupSubmit.onclick = this.onAddPhoneSubmit;

            fillPopup(0, 0, '', '', '');
            location.hash = '#phonePopup'
        },

        showEditPhonePopup: function (sender) {
            phoneID = sender.parentNode.parentNode.id;

            popupSubmit = document.getElementById('popup_ok');
            popupSubmit.textContent = 'Сохранить';
            popupSubmit.onclick = this.onEditPhoneSubmit;

            fillPopup(document.getElementById('type_' + phoneID).value,
                document.getElementById('country_code_' + phoneID).value,
                document.getElementById('op_code_' + phoneID).value,
                document.getElementById('number_' + phoneID).value,
                document.getElementById('comment_' + phoneID).value);
            location.hash = "#phonePopup";
        },

        onAddPhoneSubmit: function () {
            if(validatePopup() === true) {
                phoneCount++;
                phoneID = "phone-" + phoneCount;
                console.log('new id: ' + phoneID);
                createPhoneTemplate(phoneID);
                console.log("created template");
                parsePopup(phoneID);
                console.log("parsed popup");
                location.hash = '#phoneSection';
                console.log("closed popup");
            } else {
                alert('Код оператора число 2-5 знаков, номер телефона число 5-7 знаков, длина комментария меньше 255 символов');
            }
        },

        onEditPhoneSubmit: function () {
            if(validatePopup() === true) {
                parsePopup(phoneID);
                location.hash = '#phoneSection';
            } else {
                alert('Код оператора число 2-5 знаков, номер телефона число 5-7 знаков, длина комментария меньше 255 символов');
            }
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