var popupPhone = (function () {
    var phoneCount;
    var phoneID;
    var popupSubmit;
    var validatePopup = function () {
        var operatorCodeRegex = /[0-9]{2,5}/;
        var phoneRegex = /[0-9]{4,9}/;

        return operatorCodeRegex.test(document.getElementById('popupPhone_operatorCode').value)
            && phoneRegex.test(document.getElementById('popupPhone_phoneNumber').value)
            && (document.getElementById('popupPhone_comment').value.length < 255);
    };

    var fillPhonePopup = function (type, countryCode, operatorCode, phoneNumber, comment) {
        document.getElementById('popupPhone_phoneType').selectedIndex = type;
        document.getElementById('popupPhone_countryCode').selectedIndex = countryCode;
        document.getElementById('popupPhone_operatorCode').value = operatorCode;
        document.getElementById('popupPhone_phoneNumber').value = phoneNumber;
        document.getElementById('popupPhone_comment').value = comment;
    };

    var parsePhonePopup = function(targetPhoneId) {
        var popupPhoneType = document.getElementById('popupPhone_phoneType');
        var popupCountryCode = document.getElementById('popupPhone_countryCode');
        var popupOperatorCode = document.getElementById('popupPhone_operatorCode');
        var popupPhoneNumber = document.getElementById('popupPhone_phoneNumber');
        var popupComment = document.getElementById('popupPhone_comment');

        document.getElementById('type_' + targetPhoneId).value = popupPhoneType.selectedIndex;
        document.getElementById('country_code_' + targetPhoneId).value = popupCountryCode.selectedIndex;
        document.getElementById('op_code_' + targetPhoneId).value = popupOperatorCode.value;
        document.getElementById('number_' + targetPhoneId).value = popupPhoneNumber.value;
        document.getElementById('comment_' + targetPhoneId).value = popupComment.value;

        document.getElementById('display_type_' + targetPhoneId).textContent = popupPhoneType.options[popupPhoneType.selectedIndex].text;
        document.getElementById('display_number_' + targetPhoneId).textContent = popupCountryCode.options[popupCountryCode.selectedIndex].text + popupOperatorCode.value + popupPhoneNumber.value;
        document.getElementById('display_comment_' + targetPhoneId).textContent = popupComment.value;
    };


    var createPhoneTemplate = function (targetPhoneId) {
        var mainDiv = main.createDiv(targetPhoneId, 'jlab-row margin');

        var childElement = main.createInput('type_' + targetPhoneId, 'text', 'none');
        mainDiv.appendChild(childElement);
        childElement = main.createInput('country_code_' + targetPhoneId, 'text', 'none');
        mainDiv.appendChild(childElement);
        childElement = main.createInput('op_code_' + targetPhoneId, 'text', 'none');
        mainDiv.appendChild(childElement);
        childElement = main.createInput('number_' + targetPhoneId, 'text', 'none');
        mainDiv.appendChild(childElement);
        childElement = main.createInput('comment_' + targetPhoneId, 'text', 'none');
        mainDiv.appendChild(childElement);

        childElement = main.createDiv('display_type_' + targetPhoneId, 'jlab-cell-3 align-right text-small');
        mainDiv.appendChild(childElement);

        childElement = main.createDiv('', 'jlab-cell-3');
        var subChildElement = main.createDiv('display_number_' + targetPhoneId, 'jlab-row text-medium');
        childElement.appendChild(subChildElement);
        subChildElement = main.createDiv('display_comment_' + targetPhoneId, 'jlab-row text text-small');
        childElement.appendChild(subChildElement);
        mainDiv.appendChild(childElement);

        childElement = main.createDiv('', 'jlab-cell-1');
        mainDiv.appendChild(childElement);

        childElement = main.createDiv('', 'jlab-cell-1');
        subChildElement = main.createDiv('', 'imageButton edit');
        subChildElement.onclick = function () {
            return popupPhone.showEditPhonePopup(subChildElement)
        };
        childElement.appendChild(subChildElement);
        mainDiv.appendChild(childElement);

        childElement = main.createDiv('', 'jlab-cell-1');
        subChildElement = main.createDiv('', 'imageButton delete');
        subChildElement.onclick = function () {
            return popupPhone.deletePhoneElement(subChildElement)
        };
        childElement.appendChild(subChildElement);
        mainDiv.appendChild(childElement);

        document.getElementById('phoneSection').appendChild(mainDiv);
    };
    return {
        setPhoneCount: function (val) {
            phoneCount = val;
        },

        deletePhoneElement: function (sender) {
            document.getElementById('phoneSection').removeChild(sender.parentNode.parentNode);
        },

        showAddPhonePopup: function () {
            popupSubmit = document.getElementById('popupPhone_ok');
            popupSubmit.textContent = 'Добавить';
            popupSubmit.onclick = this.onAddPhoneSubmit;

            fillPhonePopup(0, 0, '', '', '');
            location.hash = '#phonePopup'
        },

        showEditPhonePopup: function (sender) {
            phoneID = sender.parentNode.parentNode.id;

            popupSubmit = document.getElementById('popupPhone_ok');
            popupSubmit.textContent = 'Сохранить';
            popupSubmit.onclick = this.onEditPhoneSubmit;

            fillPhonePopup(document.getElementById('type_' + phoneID).value,
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
                createPhoneTemplate(phoneID);
                parsePhonePopup(phoneID);
                location.hash = '#phoneSection';
            } else {
                alert('Код оператора число 2-5 знаков, номер телефона число 5-7 знаков, длина комментария меньше 255 символов');
            }
        },

        onEditPhoneSubmit: function () {
            if(validatePopup() === true) {
                parsePhonePopup(phoneID);
                location.hash = '#phoneSection';
            } else {
                alert('Код оператора число 2-5 знаков, номер телефона число 5-7 знаков, длина комментария меньше 255 символов');
            }
        }
    }
}());
