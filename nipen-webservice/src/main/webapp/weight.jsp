<jsp:useBean id="weights" type="java.util.List" scope="request" />
<%@ page import="com.microsoft.hsg.things.*" %> 
<%@ page import="java.util.*" %>
<html>
<body>
<form action="<%= request.getContextPath() %>/things/weight"
	method="post">
  Weight: <input type="text" name="weight" /><br />
  <input type="submit" value="Submit to HealthVault" />
</form>
<%
boolean isPolling = ((Boolean)request.getAttribute("isPolling")).booleanValue();
if (!isPolling) {
%>
<form id="pollForm" action="<%= request.getContextPath() %>/things/weight"
	method="post">
  <input id="startPolling" type="hidden" name="poll" />
</form>
<button type="button" onclick="pollButton('startPolling', 'true')">Start polling</button>
<%
}
else {
%>
<form id="pollForm" action="<%= request.getContextPath() %>/things/weight"
	method="post">
  <input id="stopPolling" type="hidden" name="poll" />
</form>
<button type="button" onclick="pollButton('stopPolling', 'false')">Stop polling</button>
<%
}
%>

<script>
    function pollButton(id, value) {
        document.getElementById(id).value = value;
        document.getElementById("pollForm").submit();
    }
</script>
</body>
</html>