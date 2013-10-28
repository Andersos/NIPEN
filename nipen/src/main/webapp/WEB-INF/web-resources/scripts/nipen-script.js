var activePageButton = "home-button";
var chartWidthFullSize = "900";
var chartHeightFullSize = "550";
var pollingFrequency = 1000;
var allowedPollingKey = 0;

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
    if (data.length != 0)
        createNewestMeasureDisplay(divId, data[data.length-1].value, data[data.length-1].unit, data[data.length-1].timestamp);
    createChart(canvasId, data);
}

function createNewestMeasureDisplay(divId, value, unit, time) {
    $("#" + divId).html("<i>Latest measure (" + time + "): </i><span>" + value + "</span> " + unit);

    $("#" + divId).css("width", chartWidthFullSize / 3.0);
    $("#" + divId).css("height", chartHeightFullSize / 2.0);
    $("#" + divId).find("span:first").css("line-height", (chartHeightFullSize / 2.0 - 60) + "px");
}

function createChart(canvasId, data) {
    timestamps = [];
    values = [];

    for (var i = 0; i < data.length; i++) {
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