<html>
    <head>
        <link href="bootstrap.min.css" rel="stylesheet">
        <link href="webapp.css" rel="stylesheet">

        <script type="text/javascript">
            function login() {
                location.replace("<%= request.getContextPath() %>/things/weight");
            }
        </script>
    </head>
    <body>
        <div class="container">
            <h2>Welcome to HealthVault weight polling service!</h2>
            <div class="login">
                <h4>Press on the button below to sign in to HealthVault</h4>
                <button type="button" class="btn btn-primary" onclick="login()">Login</button>
            </div>
        </div>
    </body>
</html>
