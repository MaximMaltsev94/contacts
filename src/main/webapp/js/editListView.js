var editListView = (function () {
    return {
        onSelectAllClick: function (element) {
            console.log(element.checked);
            if(element.checked)
                main.selectAllCheckBoxes();
            else
                main.deselectAllCheckBoxes();
        }
    }
})();