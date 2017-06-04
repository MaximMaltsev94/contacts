var popupSocial = (function () {
    var idPopup = 'socialPopup';
    var social = ['vk', 'ok', 'facebook', 'instagram', 'twitter', 'youtube', 'linkedin', 'skype'];
    return {
        showSocialPopup: function () {
            for(var i = 0; i < social.length; ++i) {
                var elem = document.getElementById('popupSocial_' + social[i]);
                elem.value = document.getElementById(social[i] + 'Id').value;
                elem.value = elem.value == 0 ? "" : elem.value;
            }
            main.showPopup(idPopup);
        },

        closeSocialPopup: function () {
            main.closePopup(idPopup);
        },

        onSaveClick: function () {
            for(var i = 0; i < social.length; ++i) {
                var popupValue = document.getElementById('popupSocial_' + social[i]).value;
                document.getElementById(social[i] + 'Id').value = popupValue;
                if(social[i] === 'skype') {
                    if(popupValue != '')
                        popupValue += '?chat';
                }
                var img = document.getElementById('img_' + social[i]);
                (function (ind, val) {
                    img.onclick = function () {
                        window.open(urls[ind] + val, '_blank');
                    };
                }(i, popupValue));
                if(popupValue == '') {
                    img.setAttribute('hidden', '');
                } else {
                    img.removeAttribute('hidden');
                }
            }
            popupSocial.closeSocialPopup();
        }
    }
}());