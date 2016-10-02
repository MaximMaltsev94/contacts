var searchView = (function () {
    var checkedCount = 0;
    return {
        onCheckBoxChecked: function(sender) {
            if(sender.checked)
                checkedCount++;
            else
                checkedCount--;
            document.getElementById('sectionAction').style.display = (checkedCount == 0) ? 'none' : 'block';
        }
    }
}());