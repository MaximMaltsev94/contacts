var email = (function () {
    window.onclick = function () {
        if (!event.target.matches('#receivers') && !event.target.matches('label') && !event.target.matches('.regular-checkbox')) {
            document.getElementById("myDropdown").className = 'dropdown-content';
        }
    };

   return {
       onDropdownShow: function () {
           document.getElementById("myDropdown").classList.toggle("show");
       },

       onReceiverSelected: function () {
           var checkedBoxes = document.querySelectorAll("input[type=checkbox]:checked");
           var receivers = '';
           for (var i = 0; i < checkedBoxes.length; ++i) {
               receivers += checkedBoxes[i].dataset.fio + "; ";
           }
           document.getElementById('receivers').value = receivers;
       }
   }
}());