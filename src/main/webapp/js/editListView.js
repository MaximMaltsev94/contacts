var editListView = (function () {
    var vkPage = 1;
    var createElement = function (contact) {
        var result = main.createDiv('', 'hoverable');
        result.onclick = function () {
            main.toggleCheckBox(contact.id);
        };
        result.innerHTML =
                    '<div class="jlab-row striped">' +
                        '<div class="jlab-cell-1"></div>' +
                        '<div class="jlab-cell-1 center">' +
                            '<img src="' + contact.profilePicture + '" width="25px" height="25px">'+
                        '</div>'+

                        '<div class="jlab-cell-9 center">'+
                            '<div class="text-medium">' +
                                contact.firstName + ' ' + contact.lastName +
                            '</div>'+
                        '</div>'+
                        '<div class="jlab-cell-1 center">'+
                            '<input type="checkbox" id="' + contact.id + '" name="contact-' + contact.id + '" class="regular-checkbox"/><label for="' + contact.id + '"></label>'+
                        '</div>'+
                    '</div>';
        return result;
    };

    var generateContactsHtml = function (contactList) {
        var section = document.getElementById("contactList");
        for(var i = 0; i < contactList.length; ++i) {
            section.appendChild(createElement(contactList[i]));
        }
    };

    return {
        onSelectAllClick: function (element) {
            console.log(element.checked);
            if(element.checked)
                main.selectAllCheckBoxes();
            else
                main.deselectAllCheckBoxes();
        },

        onLoadMoreClick: function (action) {
            vkPage++;
            var url = action + "/next?callback=editListView.appendContacts&vkPage=" + vkPage;
            main.addScript(url);
        },

        appendContacts: function (data) {
            if(data.length === 0) {
                document.getElementById('loadMore').style.display = 'none';
            }
            generateContactsHtml(data);
        }
    }
})();