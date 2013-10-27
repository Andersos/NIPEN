<html>
    <head>
        <script type="text/javascript" src="resources/scripts/Chart.min.js"></script>
        <script type="text/javascript" src="resources/scripts/jquery-1.10.1.js"></script>
        <script type="text/javascript" src="resources/scripts/bootstrap.min.js"></script>
        <script type="text/javascript" src="resources/scripts/nipen-script.js"></script>

        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/nipen-css.css" rel="stylesheet">
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
		            <a href="../webservice-1.0" target="_blank" style="float: right; margin-right: 40px;">(HealthVault Polling Service)</a>
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
    </body>
</html>