var popupAttachment = (function () {
    var attachmentCount;
    var popupAttachment_ok;
    var attachmentID;

    var monthNames = [
        "января", "февраля", "марта",
        "апреля", "мая", "июня", "июля",
        "августа", "сентября", "октября",
        "ноября", "декабря"
    ];

    var getDateString = function (date) {
        var dd = date.getDate();
        if(dd < 10) dd = '0' + dd;

        var hh = date.getHours();
        if(hh < 10) hh = '0' + hh;
        var MM = date.getMinutes();
        if(MM < 10) MM = '0' + MM;
        var ss = date.getSeconds();
        if(ss < 10) ss = '0' + ss;

        return '' + dd + ' ' + monthNames[date.getMonth()] + ' ' + date.getFullYear() + ' г. ' + hh + ':' + MM + ':' + ss;
    };

    var getServerDateString = function (date) {
        var dd = date.getDate();
        if(dd < 10) dd = '0' + dd;
        var mm = date.getMonth() + 1;
        if(mm < 10) mm = '0' + mm;

        var hh = date.getHours();
        if(hh < 10) hh = '0' + hh;
        var MM = date.getMinutes();
        if(MM < 10) MM = '0' + MM;
        var ss = date.getSeconds();
        if(ss < 10) ss = '0' + ss;

        return date.getFullYear() + '-' + mm + '-' + dd + ' ' + hh + ':' + MM + ':' + ss + '.0';
    };


    var fillAttachmentPopup = function (fileName, comment, isEdit) {
        document.getElementById('popupAttachment_fileName').value = fileName;

        var popupAttachment_file = document.getElementById('popupAttachment_file');

        if(isEdit)
            popupAttachment_file.setAttribute('hidden', "");
        else
            popupAttachment_file.removeAttribute('hidden');

        popupAttachment_file.value = '';
        document.getElementById('popupAttachment_comment').value = comment;
    };

    var parseAttachmentPopup = function (isEdit) {
        var popAttFileName = document.getElementById('popupAttachment_fileName').value;
        var popAttComment = document.getElementById('popupAttachment_comment').value;

        document.getElementById('name_' + attachmentID).value = popAttFileName;
        document.getElementById('comment_' + attachmentID).value = popAttComment;

        document.getElementById('display_name_' + attachmentID).textContent = popAttFileName;
        document.getElementById('display_comment_' + attachmentID).textContent = popAttComment;

        if(isEdit === false) {
            var dNow = new Date();
            document.getElementById('date_' + attachmentID).value = getServerDateString(dNow);
            document.getElementById('display_date_' + attachmentID).textContent = getDateString(dNow);
        }
    };

    var createAttachmentTemplate = function (targetID) {
        var mainDiv = main.createDiv(targetID, 'jlab-row margin');

        var child = main.createInput('name_' + targetID, 'text', 'none');
        mainDiv.appendChild(child);

        child = main.createInput('date_' + targetID, 'text', 'none');
        mainDiv.appendChild(child);

        child = main.createInput('comment_' + targetID, 'text', 'none');
        mainDiv.appendChild(child);

        child = main.createDiv('display_date_' + targetID, 'jlab-cell-3 align-right text-small');
        mainDiv.appendChild(child);

        child = main.createDiv('', 'jlab-cell-4');
        var subChild = main.createDiv('display_name_' + targetID, 'jlab-row text-small-bold');
        child.appendChild(subChild);
        subChild = main.createDiv('display_comment_' + targetID, 'jlab-row text text-small');
        child.appendChild(subChild);
        mainDiv.appendChild(child);

        child = main.createDiv('', 'jlab-cell-3');

        subChild = document.getElementById('popupAttachment_file').cloneNode(true);
        subChild.name = 'file_' + targetID;
        subChild.id = 'file_' + targetID;
        subChild.required = true;

        subChild.className = 'inputfile';
        subChild.setAttribute("hidden", "hidden");
        child.appendChild(subChild);

        var fileName = subChild.value;
        fileName = fileName.split('\\');
        fileName = fileName[fileName.length - 1];

        subChild = document.createElement('label');
        subChild.className = 'text-small';
        subChild.setAttribute('for', 'file_' + targetID);
        subChild.id = 'fileLabel_' + targetID;
        subChild.innerHTML = '<span>' + fileName + '\</span>';

        child.appendChild(subChild);
        mainDiv.appendChild(child);

        child = main.createDiv('', 'jlab-cell-1');
        subChild = main.createDiv('', 'imageButton edit');
        subChild.onclick = function () {
            return popupAttachment.showEditAttachmentPopup(subChild);
        };
        child.appendChild(subChild);
        mainDiv.appendChild(child);

        child = main.createDiv('', 'jlab-cell-1');
        subChild = main.createDiv('', 'imageButton delete');
        subChild.onclick = function () {
            return popupAttachment.deleteAttachmentElement(subChild);
        };
        child.appendChild(subChild);
        mainDiv.appendChild(child);

        document.querySelectorAll('#attachmentSection>.jlab-row>.jlab-cell-12')[0].appendChild(document.createElement('hr'));
        document.querySelectorAll('#attachmentSection>.jlab-row>.jlab-cell-12')[0].appendChild(mainDiv);
    } ;

    var validatePopup = function (idEdit) {
            var fileNameRegex = /^[\wа-яА-Я\s\-]{2,50}$/;

            var popupAttachment_file = document.getElementById('popupAttachment_file');
            if (!idEdit && (popupAttachment_file.value == '' || popupAttachment_file.value == this.defaultValue)) {
                main.showTooltip('Не выбран файл', 'dander');
                return false;
            }
            if(!fileNameRegex.test(document.getElementById('popupAttachment_fileName').value)) {
                main.showTooltip('Неверное имя файла. Формат: русские буквы от 2 до 50 символов', 'danger');
                return false;
            }

            return true;
    };

    var showAttachmentPopup = function () {
        main.showPopup('attachmentPopup');
    };

    return{
        setAttachmentCount: function (val) {
            attachmentCount = val;
        },

        deleteAttachmentElement : function (sender) {

            document.querySelectorAll('#attachmentSection>.jlab-row>.jlab-cell-12')[0].removeChild(sender.parentNode.parentNode.previousElementSibling);
            document.querySelectorAll('#attachmentSection>.jlab-row>.jlab-cell-12')[0].removeChild(sender.parentNode.parentNode);
        },

        showAddAttachmentPopup: function () {
            popupAttachment_ok = document.getElementById('popupAttachment_ok');
            popupAttachment_ok.textContent = 'Добавить';
            popupAttachment_ok.onclick = this.onAddAttachmentSubmit;

            fillAttachmentPopup('', '', false);
            showAttachmentPopup();
        },

        showEditAttachmentPopup: function (sender) {
            attachmentID = sender.parentNode.parentNode.id;

            fillAttachmentPopup(document.getElementById('name_' + attachmentID).value,
                                document.getElementById('comment_' + attachmentID).value,
                                true);

            popupAttachment_ok = document.getElementById('popupAttachment_ok');
            popupAttachment_ok.textContent = 'Сохранить';
            popupAttachment_ok.onclick = this.onEditAttachmentSubmit;

            showAttachmentPopup();
        },

        onAddAttachmentSubmit: function () {
            if(validatePopup(false) === true) {
                attachmentCount++;
                attachmentID = 'attachment-' + attachmentCount;
                createAttachmentTemplate(attachmentID);
                parseAttachmentPopup(false);
                this.closeAttachmentPopup();
            }
        },

        onEditAttachmentSubmit: function () {
            if(validatePopup(true) === true) {
                parseAttachmentPopup(true);
                this.closeAttachmentPopup()
            }
        },

        closeAttachmentPopup: function () {
            main.closePopup('attachmentPopup');
        }
    }
}());