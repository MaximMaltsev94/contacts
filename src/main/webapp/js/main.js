var main = (function () {
    var isTooltipShown = false;
    return {
        showTooltip: function (tooltipText, tooltipType) {
            if (isTooltipShown === false && tooltipText !== '') {
                isTooltipShown = true;

                var bgColor = tooltipType === 'success' ? "rgb(190, 235, 159)" : "rgb(239, 100, 105)";

                var tooltipElement = document.getElementById('tooltip');

                tooltipElement.style.display = "block";
                tooltipElement.innerHTML = tooltipText;
                tooltipElement.style.backgroundColor = bgColor;
                tooltipElement.className = 'tooltip-animation';

                setTimeout(function () {
                    tooltipElement.style.display = 'none';
                    isTooltipShown = false;
                }, 5000);
            }
        },
        createInput: function (id, type, display) {
            var inputElement = document.createElement('input');
            inputElement.type = type;
            inputElement.style.display = display;
            inputElement.id = id;
            inputElement.name = id;
            return inputElement;
        },

        createDiv: function (id, className) {
            var divElement = document.createElement('div');
            if(id.length != 0)
                divElement.id = id;
            divElement.className = className;
            return divElement;
        },

        showPopup: function(id) {
            document.getElementById(id).className = "popupBack";
        },

        closePopup: function (id) {
            document.getElementById(id).className = "popupBack hidden";
        },

        closeConfirmDialog: function () {
            main.closePopup("confirmPopup");
        },

        showConfirmDialog: function(text, onConfirmAction) {
            document.getElementById('confirmPopupText').innerHTML = text;
            document.getElementById('confirmPopup_ok').onclick = onConfirmAction;
            main.showPopup("confirmPopup");
        },

        postRequest: function(url, idParamName, id) {
            var form = document.createElement('form');
            form.style.visibility = 'hidden';
            form.setAttribute('method', 'post');
            form.setAttribute('action', contextPath + url);

            var input = document.createElement('input');
            input.setAttribute('type', 'text');
            input.setAttribute('name', idParamName);
            input.value = id;
            form.appendChild(input);

            document.body.appendChild(form);
            form.submit();
        },

        addScript: function (url) {
            var elem = document.createElement("script");
            elem.src = url;
            document.head.appendChild(elem);
        },

        toggleCheckBox: function (id) {
            var checkBox = document.getElementById(id);
            checkBox.checked = !checkBox.checked;
        },

        selectAllCheckBoxes: function () {
            var list = document.querySelectorAll('input[type=checkbox]');
            for(var i = 0; i < list.length; ++i) {
                list[i].checked = true;
            }
        },

        deselectAllCheckBoxes: function () {
            var list = document.querySelectorAll('input[type=checkbox]:checked');
            for(var i = 0; i < list.length; ++i) {
                list[i].checked = false;
            }
        }
    }
}());