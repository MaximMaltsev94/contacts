var email = (function () {
    window.onclick = function () {
        if (!Element.prototype.matches) {
            Element.prototype.matches =
                Element.prototype.matchesSelector ||
                Element.prototype.mozMatchesSelector ||
                Element.prototype.msMatchesSelector ||
                Element.prototype.oMatchesSelector ||
                Element.prototype.webkitMatchesSelector ||
                function(s) {
                    var matches = (this.document || this.ownerDocument).querySelectorAll(s),
                        i = matches.length;
                    while (--i >= 0 && matches.item(i) !== this) {}
                    return i > -1;
                };
        }
        if (!event.target.matches('#receivers') &&
            !event.target.matches('label') &&
            !event.target.matches('.regular-checkbox') &&
            !event.target.matches('.imageButton.help')) {
            document.getElementById("myDropdown").className = 'dropdown-content';
            document.getElementById("helpDropDown").className = 'dropdown-content';
        }
    };
    var postEmailSubmit = function(ids, subject, text) {
        var form = document.createElement('form');
        form.style.visibility = 'hidden';
        form.setAttribute('method', 'post');
        form.setAttribute('action', contextPath + '/contact/submitEmail');

        var input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('name', 'id');
        input.value = ids;
        form.appendChild(input);

        input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('name', 'subject');
        input.value = subject;
        form.appendChild(input);

        input = document.createElement('textarea');
        input.setAttribute('name', 'text');
        input.value = text;
        form.appendChild(input);

        document.body.appendChild(form);
        form.submit();
    };

   return {
       onDropdownShow: function (dropDownId) {
           document.getElementById(dropDownId).classList.toggle("show");
       },

       onReceiverSelected: function () {
           var checkedBoxes = document.querySelectorAll("input[type=checkbox]:checked");
           var receivers = '';
           for (var i = 0; i < checkedBoxes.length; ++i) {
               receivers += checkedBoxes[i].dataset.fio + "; ";
           }
           document.getElementById('receivers').value = receivers;
       },

       onEmailSubmit: function () {
           var checkedBoxes = document.querySelectorAll("input[type=checkbox]:checked");
           if(checkedBoxes.length == 0) {
               alert('Не выбраны получатели!');
           } else {
               var ids = '';
               for (var i = 0; i < checkedBoxes.length; ++i) {
                   ids += checkedBoxes[i].id + ',';
               }
               var subject = document.getElementById('mailSubject').value;
               var text = document.getElementById('mailText').value;
               postEmailSubmit(ids, subject, text);
           }
       },

       onTemplateChange: function () {
           var index = document.getElementById("templateSelect").selectedIndex;
           var mailText = document.getElementById('mailText');
           if(index == 0) {
               mailText.value = "";
           } else {
               mailText.value = document.getElementById('template-' + index).value;
           }

       }
   }
}());