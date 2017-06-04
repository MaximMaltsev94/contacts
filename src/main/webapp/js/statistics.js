var statisticsView = (function () {
    var getRandomColor = function () {
        var letters = '0123456789ABCDEF';
        var color = '#';
        for (var i = 0; i < 6; i++ ) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    };

    var getRandomColorArray = function (size) {
        var arr = [];
        for(var i = 0; i < size; ++i) {
            arr.push(getRandomColor());
        }
        return arr;
    };

    var draw = function (ctxId, title, type, labels, values, legendDisplay) {
        var ctx = document.getElementById(ctxId).getContext('2d');
        new Chart(ctx, {
            type: type,

            data: {
                labels: labels,
                datasets: [{
                    backgroundColor: getRandomColorArray(labels.length),
                    data: values
                }]
            },

            // Configuration options go here
            options: {
                legend: {
                    display: legendDisplay,
                    position: 'left'
                },
                title: {
                    display: true,
                    text: title
                }
            }
        });
    };

    var drawCountryChart = function () {
        draw('country', 'География', 'doughnut', Object.keys(countryData), Object.values(countryData), true);
    };

    var drawGroupsChart = function () {
        draw('contactGroups', 'Списки контактов', 'horizontalBar', Object.keys(groupData), Object.values(groupData), false);
    };

    var drawAgeChart = function () {
        var ctx = document.getElementById('age').getContext('2d');
        new Chart(ctx, {
            type: 'bar',

            data: {
                labels: ageStatisticsLabels,
                datasets: [
                    {
                        label: 'Мужчины',
                        backgroundColor: '#36a2eb',
                        data: menAgeStatistics
                    },
                    {
                        label: 'Женщины',
                        backgroundColor: '#ff6384',
                        data: womenAgeStatistics
                    }]
            },

            // Configuration options go here
            options: {
                title: {
                    display: true,
                    text: 'Пол/возраст'
                }
            }
        });
    };

    return {
        drawCharts : function () {
            drawCountryChart();
            drawGroupsChart();
            drawAgeChart();
        }
    }
}());