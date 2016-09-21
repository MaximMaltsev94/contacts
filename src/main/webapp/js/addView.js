var addView = (function() {
    var cityOptions;
    var i, n;
    return { // методы доступные извне
        onChangeCountry: function(selectedValue) {
            cityOptions = document.getElementById('city').options;
            for(i = 0, n = cityOptions.length; i < n; i++) {
                cityOptions[i].style.display = 'none';
                if(cityOptions[i].dataset.country === selectedValue) {
                    cityOptions[i].style.display = 'block';
                }
            }
            cityOptions[0].style.display = 'block';
            document.getElementById('city').selectedIndex = 0;
        },

        readURL: function (input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                document.getElementById('blah').setAttribute('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
        }
    }
    }
}());