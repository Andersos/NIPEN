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
int pollingTimeLeft = ((Integer)request.getAttribute("pollingTimeLeft")).intValue();
if (pollingTimeLeft == 0) {
%>
<form id="pollForm" action="<%= request.getContextPath() %>/things/weight"
	method="post">
  <input id="startPolling" type="hidden" name="poll" />
</form>
<button type="button" onclick="pollButton('startPolling', 'true')">Start polling for 10 min.</button>
<%
}
else {
%>
<form id="pollForm" action="<%= request.getContextPath() %>/things/weight"
	method="post">
  <input id="stopPolling" type="hidden" name="poll" />
  <% /* %>
  <b>Polling ends after: <span id="time"></span> seconds.</b>
  <script>
    var timeLeft = <%out.println(pollingTimeLeft);%>;

    setInterval(function() {
        timeLeft--;
        if (timeLeft <= 0) {
            location.reload();
        }
        document.getElementById("time").innerHTML = timeLeft;
        },1000);
  </script>
  <% */ %>
</form>
<button type="button" onclick="pollButton('stopPolling', 'false')">Stop polling</button>
<%
}
%>

<table>
    		<tr>
    			<td>Id:</td>
    			<td>Value:</td>
			</tr>
<%   /*
Iterator iterator = weights.iterator();
while ( iterator.hasNext() ){
	Weight weight = (Weight)iterator.next();
	out.println("<tr><td>" + weight.getId() + "</td><td>" + weight.getValue() + "</td></tr>");
}
*/ %>
</table>
<script>
    function pollButton(id, value) {
        document.getElementById(id).value = value;
        document.getElementById("pollForm").submit();
    }
</script>
</body>
</html>