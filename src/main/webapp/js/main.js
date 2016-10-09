var main = (function () {
    return {
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

        onFileChangeAction: function() {
            var fileName = document.getElementById('file').value;
            fileName = fileName.split('\\');
            fileName = fileName[fileName.length - 1];

            document.getElementById('fileLabel').innerHTML =  '\<div class="imageButton upload"></div>' + '\<span class="test-medium">' + fileName + '\</span>';

            console.log(document.getElementById('file').value);
        }
    }
}());