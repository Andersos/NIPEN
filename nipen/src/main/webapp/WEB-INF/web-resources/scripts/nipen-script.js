var activePageButton = "home-button";
var chartWidthFullSize = "900";
var chartHeightFullSize = "550";
var pollingFrequency = 1000;
var allowedPollingKey = 0;
var maxValuesDisplayedInChart = 10;

$(document).ready(function() {
    updatePage();
});

function updatePage() {
    allowedPollingKey++;
    startPolling("/nipen/api/human/heart_rates", -1, updateHeartRates, allowedPollingKey);
    startPolling("/nipen/api/human/weights", -1, updateWeights, allowedPollingKey);
}

function startPolling(url, lastTimestamp, updateFunction, pollingKey) {
    $.ajax({
        url: url,
        dataType: "json",
        success: function(data) {
            if (typeof data === 'undefined' || data.length == 0)
                return;

            if (lastTimestamp != data[data.length-1].timestamp) {
                lastTimestamp = data[data.length-1].timestamp;
                data = getDataWithFormatedTimestamps(data);
                updateFunction(data);
            }
        },
        complete: setTimeout(function() {
            if (pollingKey == allowedPollingKey)
                startPolling(url, lastTimestamp, updateFunction, pollingKey);
            }, pollingFrequency),
        timeout: 30000
    });
}

function getDataWithFormatedTimestamps(data) {
    for (var i = 0; i < data.length; i++) {
        data[i].timestamp = formatTimestamp(data[i].timestamp);
    }

    return data;
}

function formatTimestamp(timestamp) {
    var date = timestamp.split(" ")[0].split("-");
    var time = timestamp.split(" ")[1].split(":");

    timestamp = date[2] + "." + date[1] + "." + date[0];
    timestamp += "|" + time[0] + ":" + time[1] + ":" + time[2].split(".")[0];

    return timestamp;
}

function updateHeartRates(heartRateData) {
    visualizeData("newest-heart-rate", "heart-rate-chart", heartRateData);
}

function updateWeights(weightData) {
    visualizeData("newest-weight", "weight-chart", weightData);
}

function visualizeData(divId, canvasId, data) {
    if (data.length > 1)
        createNewestMeasureDisplay(divId, data[data.length-1], data[data.length-2]);
    createChart(canvasId, data);
}

function createNewestMeasureDisplay(divId, newData, oldData) {
    $("#" + divId).css("width", chartWidthFullSize / 3.0);
    $("#" + divId).css("height", chartHeightFullSize / 2.0);

    var latestMeasure = "<div class='latest-measure'>";
    latestMeasure += "<span>" + newData.value + "</span> " + newData.unit + "<i>" + newData.timestamp + " (latest)</i>";
    latestMeasure += "</div>";
    var lastMeasure = "<div class='last-measure'>";
    lastMeasure += "<span>" + oldData.value + "</span> " + oldData.unit + "<i>" + oldData.timestamp + "</i>";
    lastMeasure += "</div>";
    $("#" + divId).html(latestMeasure + lastMeasure);

    $("#" + divId).find(".latest-measure:first").find("span:first").css("line-height", (chartHeightFullSize * 0.236) + "px");
    $("#" + divId).find(".last-measure:first").find("span:first").css("line-height", (chartHeightFullSize * 0.182) + "px");
}

function createChart(canvasId, data) {
    timestamps = [];
    values = [];
    var startIndex = 0;

    if (data.length > maxValuesDisplayedInChart) {
        startIndex = data.length - maxValuesDisplayedInChart;
    }

    for (var i = startIndex; i < data.length; i++) {
        timestamps.push(data[i].timestamp);
        values.push(data[i].value);
    }

    var lineChartData = {
        labels: timestamps,
        datasets: [
            {
                fillColor: "rgba(165,196,222,0.7)",
                strokeColor: "rgba(51,51,51,0.9)",
                data: values
            }
        ]
    }

    var opts = {
        scaleFontSize: 15,
        scaleFontStyle: "bold",
        scaleFontColor: "#333333",
    }

    $("#" + canvasId).attr("width", chartWidthFullSize);
    $("#" + canvasId).attr("height", chartHeightFullSize);

    new Chart($("#" + canvasId)[0].getContext("2d")).Bar(lineChartData, opts);

    var chartWidth = chartWidthFullSize;
    var chartHeight = chartHeightFullSize;
    if (activePageButton == "home-button") {
        chartWidth = 2.0 * chartWidthFullSize / 3.0;
        chartHeight = chartHeightFullSize / 2.0;
        $("#" + canvasId).css("width", chartWidth);
        $("#" + canvasId).css("height", chartHeight);
    }

    var unitId = canvasId.split("-chart")[0] + "-unit";
    $("#" + unitId).text(data[0].unit);
    $("#" + unitId).css("left", chartWidth*0.05);
    $("#" + unitId).css("top", -chartHeight*0.95);
}

function changePage(buttonId) {
    var animationTime = 400;

    updatePage();

    $("#" + buttonId).attr("class", "custom-active");
    $("#" + activePageButton).attr("class", "");
    activePageButton = buttonId;

    switch (buttonId) {
    case "home-button":
        $("#heart-rate-chart").css("width", 2.0 * chartWidthFullSize / 3.0);
        $("#heart-rate-chart").css("height", chartHeightFullSize / 2.0);
        $("#weight-chart").css("width", 2.0 * chartWidthFullSize / 3.0);
        $("#weight-chart").css("height", chartHeightFullSize / 2.0);
        showAllDataTypes(animationTime);
        break;
    case "heart-rate-button":
        hideAllDataTypes();
        $("#heart-rate-chart").css("width", chartWidthFullSize);
        $("#heart-rate-chart").css("height", chartHeightFullSize);
        $("#heart-rate").show(animationTime);
        break;
    case "weight-button":
        hideAllDataTypes();
        $("#weight-chart").css("width", chartWidthFullSize);
        $("#weight-chart").css("height", chartHeightFullSize);
        $("#weight").show(animationTime);
        break;
    }
}

function showAllDataTypes(animationTime) {
    $("#heart-rate").show(animationTime);
    $("#newest-heart-rate").show(animationTime);
    $("#weight").show(animationTime);
    $("#newest-weight").show(animationTime);
}

function hideAllDataTypes() {
    $("#heart-rate").hide();
    $("#newest-heart-rate").hide();
    $("#weight").hide();
    $("#newest-weight").hide();
}

function resetData() {
    var isGoingToResetData = window.confirm("Are you sure you want to delete all data?");

    if (isGoingToResetData) {
        $.ajax({
            type: "POST",
            url: "/nipen/api/human/heart_rate/reset"
        });
        $.ajax({
            type: "POST",
            url: "/nipen/api/human/weight/reset"
        });

        updatePage();

        var populateNewData = window.confirm("The database is now empty. Do you want to populate the charts with some mockup data?");
        if (populateNewData)
            populateDatabaseWithRandomValues();
        else
            location.reload();
    }
}

function populateDatabaseWithRandomValues() {
    var numValues = 10;

    for (var i = 0; i < numValues; i++) {
        var date = new Date(new Date().getTime() - (numValues-i) * 123456789);
        addHeartRate(Math.floor((Math.random()*20)+60), date);
        date = new Date(new Date().getTime() - (numValues-i) * 132446319);
        addWeight(Math.floor((Math.random()*10)+70), date);
    }
}

function addHeartRate(value, date) {
    var timestampString;

    if (typeof date === 'undefined')
        timestampString = createTimestamp(new Date());
    else
        timestampString = createTimestamp(date);

    var json = {
        userId: 1,
        timestamp: timestampString,
        value: value,
        unit: "bpm"
    };

    $.ajax({
        type: "POST",
        url: "/nipen/api/human/heart_rate",
        data: JSON.stringify(json),
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    });
}

function addWeight(value, date) {
    var timestampString;

    if (typeof date === 'undefined')
        timestampString = createTimestamp(new Date());
    else
        timestampString = createTimestamp(date);

    var json = {
        userId: 1,
        timestamp: timestampString,
        value: value,
        unit: "kg"
    };

    $.ajax({
        type: "POST",
        url: "/nipen/api/human/weight",
        data: JSON.stringify(json),
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    });
}

function createTimestamp(date) {
    var year = date.getFullYear() + "";
    var month = (parseInt(date.getMonth(), 10) + 1) + "";
    var day = date.getDate() + "";
    var hours = date.getHours() + "";
    var minutes = date.getMinutes() + "";
    var seconds = date.getSeconds() + "";

    var stringTime = year;
    stringTime += "-" + ((month.length == 1) ? "0" + month : month);
    stringTime += "-" + ((day.length == 1) ? "0" + day : day);
    stringTime += " " + ((hours.length == 1) ? "0" + hours : hours);
    stringTime += ":" + ((minutes.length == 1) ? "0" + minutes : minutes);
    stringTime += ":" + ((seconds.length == 1) ? "0" + seconds : seconds);

    return stringTime;
}
