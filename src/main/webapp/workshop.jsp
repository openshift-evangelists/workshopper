<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.openshift.evg.workshopper.modules.Modules" %>
<%@ page import="org.openshift.evg.workshopper.workshops.Workshop" %>
<% Modules modules = ((Modules) application.getAttribute("modules")); %>
<% Workshop workshop = ((Workshop) application.getAttribute("workshop")); %>
<html>
<head>
    <title>Workshop</title>
</head>
<body>

<% for(String id : workshop.getSortedModules()){ %>
    <a href="module/<%= id %>"><%= modules.get(id).getName() %></a>
    <br />
<% } %>


</body>
</html>
