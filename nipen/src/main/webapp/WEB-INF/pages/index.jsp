<html>
	<head>
		<script type="text/javascript" src="resources/scripts/Chart.min.js"></script>
		<script type="text/javascript" src="resources/scripts/jquery-1.10.1.js"></script>
		<script type="text/javascript" src="resources/scripts/bootstrap.min.js"></script>

		<link href="resources/css/bootstrap.min.css" rel="stylesheet">
		<style>
			html,
			body {
				background-color: #f2f2f2;
				height: 100%;
			}

			.data-type {
				padding-top: 50px;
				padding-bottom: 40px;
				font-family: UnitSlabWebPro-Bold,Cambria,Lucida Sans; 
				color: #9a2b7e;
				border-bottom: solid;
				border-width: 1px;
			}

			.caption {
				padding-bottom: 20px;
			}

			.caption span {
				font-size: 20pt;
				font-weight: bold;
			}

			#user-name {
				position: absolute;
				right: 0px;
			}

			.newest-measure {
				float: left;
				text-align: center;
				vertical-align: middle;
				font-size: 30pt;
				border-style: dotted;
				border-width: 2.0px;
				border-radius: 25px;
				border-color: #333333;
				color: #333333;
			}

			.newest-measure i {
				display: block;
				text-align: left;
				padding-left: 15px;
				padding-top: 5px;
				font-size: 15px;
				color: #cc0099;
			}

			.newest-measure span {
				font-size: 70pt;
			}

			.navbar .navbar-inner {
			    background-color: #330d29;
			    background-image: none;
			}

			.custom-active {
			    color: #555;
			    background-color: #220c29;
			}

			/* Wrapper for page content to push down footer */
			#wrap {
				min-height: 100%;
				height: auto !important;
				height: 100%;
				/* Negative indent footer by it's height */
				margin: 0 auto -60px;
			}

			#push,
			#footer {
				height: 120px;
			}
			#footer {
				background-color: #3b2435;
			}

			@media (max-width: 767px) {
				#footer {
					margin-left: -20px;
					margin-right: -20px;
					padding-left: 20px;
					padding-right: 20px;
				}
			}

			#internal-header {
				padding-top: 50px;
				font-family: UnitSlabWebPro-Bold,Cambria,Lucida Sans;
				color: #9a2b7e;
			}

			#main-container {
				background-color: #ffffff;
				padding: 20px;
				border-top: solid;
				border-top-width: 5px;
				border-color: #9a2b7e;
			}
		</style>
	</head>
	<body>

		<div class="navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container">
					<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					</button>
					<a class="brand" href="#">NIPEN</a>
					<div class="nav-collapse collapse">
						<ul class="nav">
							<li id="home-button" class="custom-active" onclick="changePage('home-button');"><a href="#">Home</a></li>
							<li id="heart-rate-button" onclick="changePage('heart-rate-button');"><a href="#">Heart Rate</a></li>
							<li id="weight-button" onclick="changePage('weight-button');"><a href="#">Weight</a></li>
						</ul>
					</div>
					<a id="user-name" class="brand" href="#">Ola Nordmann</a>
				</div>
			</div>
		</div>

		<div id="wrap">

			<div id="internal-header" class="container">
				<h3>National Integration Platform for Citizen Centric eHealth in Norway</h3>
			</div>

		    <div id="main-container" class="container">

				<div id="heart-rate" class="data-type">
					<div class="caption">
						<span>Heart Rate</span>
						<a href="api/human/heart_rates">(json)</a>
					</div>

					<div class="newest-measure" id="newest-heart-rate"></div>
					<canvas id="heart-rate-chart"></canvas>
				</div>

				<div id="weight" class="data-type">
					<div class="caption">
						<span>Weight</span>
						<a href="api/human/weights">(json)</a>
					</div>

					<div class="newest-measure" id="newest-weight"></div>
					<canvas id="weight-chart"></canvas>
				</div>
			</div>
			<div id="push"></div>
		</div>

		<div id="footer">
			<div class="container">
				<p class="muted credit"></p>
			</div>
		</div>

		<script type="text/javascript">
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

				if (activePageButton == "home-button") {
					$("#" + canvasId).css("width", 2.0 * chartWidthFullSize / 3.0);
					$("#" + canvasId).css("height", chartHeightFullSize / 2.0);
				}
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
		</script>
	</body>
</html>