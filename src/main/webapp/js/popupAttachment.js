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


    var fillAttachmentPopup = function (fileName, comment) {
        document.getElementById('popupAttachment_fileName').value = fileName;
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

        child = main.createDiv('', 'jlab-cell-3');
        var subChild = main.createDiv('display_name_' + targetID, 'jlab-row text-medium');
        child.appendChild(subChild);
        subChild = main.createDiv('display_comment_' + targetID, 'jlab-row text text-small');
        child.appendChild(subChild);
        mainDiv.appendChild(child);

        child = main.createDiv('', 'jlab-cell-1');
        subChild = document.createElement('input');
        subChild.type = 'file';
        subChild.name = 'file_' + targetID;
        subChild.id = 'file_' + targetID;
        subChild.required = true;
        subChild.onchange = function () {
            return main.onFileChangeAction(targetID);
        };
        subChild.className = 'inputfile';
        child.appendChild(subChild);

        subChild = document.createElement('label');
        subChild.className = 'text-small';
        subChild.setAttribute('for', 'file_' + targetID);
        // subChild.for = 'file_' + targetID;
        subChild.id = 'fileLabel_' + targetID;
        subChild.innerHTML = '<div class="imageButton upload"></div>';
        child.appendChild(subChild);

        // child.innerHTML = '\<input type="file" name="file_' + targetID + '" id="file_' + targetID +' " required onchange="main.onFileChangeAction(\''+ targetID + '\')" class="inputfile" />'+
        //                     '\<label class="text-small" for="file_' + targetID + '" id="fileLabel_' + targetID + '"><div class="imageButton upload"></div></label>';
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

        document.getElementById('attachmentSection').appendChild(mainDiv);
    } ;

    var validatePopup = function () {
            var fileNameRegex = /^[\wа-яА-Я\s\-]{2,50}$/;

            return fileNameRegex.test(document.getElementById('popupAttachment_fileName').value);
        };

    return{
        setAttachmentCount: function (val) {
            attachmentCount = val;
        },

        deleteAttachmentElement : function (sender) {
            document.getElementById('attachmentSection').removeChild(sender.parentNode.parentNode);
        },

        showAddAttachmentPopup: function () {
            popupAttachment_ok = document.getElementById('popupAttachment_ok');
            popupAttachment_ok.textContent = 'Добавить';
            popupAttachment_ok.onclick = this.onAddAttachmentSubmit;

            fillAttachmentPopup('', '');

            location.hash = '#attachmentPopup';
        },

        showEditAttachmentPopup: function (sender) {
            attachmentID = sender.parentNode.parentNode.id;

            fillAttachmentPopup(document.getElementById('name_' + attachmentID).value,
                                document.getElementById('comment_' + attachmentID).value);

            popupAttachment_ok = document.getElementById('popupAttachment_ok');
            popupAttachment_ok.textContent = 'Сохранить';
            popupAttachment_ok.onclick = this.onEditAttachmentSubmit;
            location.hash = '#attachmentPopup';
        },

        onAddAttachmentSubmit: function () {
            if(validatePopup()) {
                attachmentCount++;
                attachmentID = 'attachment-' + attachmentCount;
                createAttachmentTemplate(attachmentID);
                parseAttachmentPopup(false);
                location.hash = '#attachmentSection';
            } else {
                alert('Enter file name');
            }
        },

        onEditAttachmentSubmit: function () {
            if(validatePopup()) {
                parseAttachmentPopup(true);
                location.hash = '#attachmentSection';
            } else {
                alert('Введено неверное имя файла');
            }
        }
    }
}());