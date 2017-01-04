<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.openshift.evg.workshopper.modules.Modules" %>
<% Modules modules = ((Modules) application.getAttribute("modules")); %>
<html>
<head>
    <title>Workshop</title>
</head>
<body>

<% for(String id : modules.getModules().keySet()){ %>
    <a href="module/<%= id %>"><%= modules.getModules().get(id).getName() %></a>
    <br />
<% } %>


</body>
</html>
