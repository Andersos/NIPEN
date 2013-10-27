<jsp:useBean id="weights" type="java.util.List" scope="request" />
<%@ page import="com.microsoft.hsg.things.*" %> 
<%@ page import="java.util.*" %>
<html>
<head>
<link href="../bootstrap.min.css" rel="stylesheet">
<link href="../webapp.css" rel="stylesheet">
</head>
<body>
<div class="container">
<h2 style="margin-bottom: 50px;">HealthVault Weight Polling Service</h2>

<div class="center-div">
    <%
    boolean isPolling = ((Boolean)request.getAttribute("isPolling")).booleanValue();
    if (!isPolling) {
    %>
    <div class="page-element">
        <h4>The application is currently not pulling data.<br />
        Click on the button below to start polling:</h4>
        <form id="pollForm" action="<%= request.getContextPath() %>/things/weight"
            method="post">
          <input id="startPolling" type="hidden" name="poll" />
        </form>
        <button type="button" class="btn btn-success" onclick="pollButton('startPolling', 'true')">Start polling</button>
    </div>
    <%
    }
    else {
    %>
    <div class="page-element">
        <h4>The application is pulling new data from HealthVault and sending it to NIPEN.
        Click on the button below to stop polling:</h4>
        <form id="pollForm" action="<%= request.getContextPath() %>/things/weight"
            method="post">
          <input id="stopPolling" type="hidden" name="poll" />
        </form>
        <button type="button" class="btn btn-danger" onclick="pollButton('stopPolling', 'false')">Stop polling</button>
    </div>
    <%
    }
    %>
    <div class="page-element">
        <h4>Enter a value and send it to HealthVault</h4>
        <form action="<%= request.getContextPath() %>/things/weight"
            method="post">
          <b>Weight (kg):</b> <input type="text" name="weight" /><br />
          <input type="submit" class="btn btn-primary" value="Submit to HealthVault" />
        </form>
    </div>
    </div>
</div>

<script>
    function pollButton(id, value) {
        document.getElementById(id).value = value;
        document.getElementById("pollForm").submit();
    }
</script>
</body>
</html>