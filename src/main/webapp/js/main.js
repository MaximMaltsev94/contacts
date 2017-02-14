var main = (function () {
    return {
        showTooltip: function (tooltipText, tooltipType) {
            if(tooltipText == '')
                return;

            var bgColor = tooltipType === 'success' ? "rgb(190, 235, 159)" : "rgb(239, 100, 105)";

            var tooltipElement = document.getElementById('tooltip');
            tooltipElement.style.display = "block";
            tooltipElement.innerHTML = tooltipText;
            tooltipElement.style.backgroundColor = bgColor;
            tooltipElement.className = 'tooltip-animation';

            setTimeout(function(){
                console.log('hiding');
                tooltipElement.style.display = 'none';
            }, 5000);
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

        onFileChangeAction: function(attachmentID) {
            var fileName = document.getElementById('file_' + attachmentID).value;
            fileName = fileName.split('\\');
            fileName = fileName[fileName.length - 1];

            document.getElementById('fileLabel_' + attachmentID).innerHTML =  '\<div class="imageButton upload"></div>' + '\<span class="test-medium">' + fileName + '\</span>';

            console.log(document.getElementById('file_' + attachmentID).value);
        }
    }
}());