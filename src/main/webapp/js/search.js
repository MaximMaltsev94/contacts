var searchView = (function () {
    var checkedCount = 0;
    var showClass = 'show';

    var showSectionAction = function () {
        var sectionAction = document.getElementById('sectionAction');
        if(!sectionAction.classList.contains(showClass)) {
            sectionAction.classList.add(showClass);
        }
    };

    var hideSectionAction = function () {
        var sectionAction = document.getElementById('sectionAction');
        sectionAction.classList.remove(showClass);
    };

    return {
        onCheckBoxChecked: function(sender) {
            if(sender.checked)
                checkedCount++;
            else
                checkedCount--;

            if(checkedCount == 0) {
                hideSectionAction();
            } else {
                showSectionAction();
            }
        },

        submitForm: function(pageNumber) {
            var pageInput = document.getElementById('page');
            pageInput.value = pageNumber;
            document.getElementById('searchForm').submit();
        }
    }
}());