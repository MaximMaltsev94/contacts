var email = (function () {
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
           if(checkedBoxes.length === 0) {
               main.showTooltip('Не выбраны получатели!', 'danger');
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