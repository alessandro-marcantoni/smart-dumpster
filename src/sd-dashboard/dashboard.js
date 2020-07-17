/**
 * Parses the current date to fit the html input.
 */
Date.prototype.toDateInputValue = (function() {
    var local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0,10);
});

/**
 * Creates and displays a bar chart for the weight progress of the dumpster.
 */
function createChart(date) {
    $("canvas").remove();
    $("body > div").append('<canvas id="chart"></canvas>');
    if (!date || date > new Date().toDateInputValue()) {
        date = "2020-01-01";
    }
    $.getJSON("/sd-service/get-data.php", function(dumpsterData) {
        var weights = [];
        var dates = [];
        dumpsterData.data.data.forEach(element => {
            if (element.date >= date) {
                dates.push(element.date);
                weights.push(element.weight);
            }
        });
        var ctx = document.getElementById("chart").getContext("2d");
        var chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: dates,
                datasets: [{
                    label: "Weight progress",
                    backgroundColor: "rgba(0, 0, 255, 0.3)",
                    data: weights
                }]
            }
        });
    });
    
}

/**
 * Displays the DOM based on the state of the dumpster.
 */
function setDom() {
    $.getJSON("/sd-service/is-dumpster-available.php", function(data) {
        if (data.available == "AV") {
            $("p.state").html("Current state: AVAILABLE");
            $("button.available").css("background-color", "red");
            $("button.available").html("SET UNAVAILABLE");
            url = "/sd-service/set-dumpster-unavailable.php"
        } else {
            $("p.state").html("Current state: UNAVAILABLE");
            $("button.available").css("background-color", "green");
            $("button.available").html("SET AVAILABLE");
            url = "/sd-service/set-dumpster-available.php"
        }
        $("button.available").click(function(e) { 
            e.preventDefault();
            $.post(url, data, function (data){});
        });
        $.getJSON("/sd-service/get-data.php", function(dumpsterData) {
            $("p.num_throws").html("Total throws: " + dumpsterData.data.throws);
            $("p.current").html("Current weight: " + dumpsterData.data.data[dumpsterData.data.data.length - 1].weight);
        });
        $("button.chart").click(function() {
            createChart($("input.date").val());
        });
    });
}

$(document).ready(function () {
    setDom();
    createChart($("input.date").val());
    setInterval(setDom, 2000);
});