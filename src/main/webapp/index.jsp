<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.openshift.evg.workshopper.workshops.Workshops" %>
<% Workshops workshops = ((Workshops) application.getAttribute("workshops")); %>
<html>
<body>
<h2>Hello World!</h2>

<% for(String id : workshops.get().keySet()){ %>
<a href="workshop/<%= id %>/"><%= workshops.get(id).getName() %></a>
<br />
<% } %>

</body>
</html>
