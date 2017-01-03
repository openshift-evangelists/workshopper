<%@ page contentType="text/html;charset=UTF-8" language="java" import="eu.mjelen.workshopper.modules.Modules" import="eu.mjelen.workshopper.modules.Module" %>
<% Modules modules = ((Modules) application.getAttribute("modules")); %>
<% Module module = modules.getModules().get((String) application.getAttribute("module")); %>
<html>
<head>
    <title>Workshop</title>
</head>
<body>

<h1><%= module.getName() %></h1>

<a href="..">Back</a>

</body>
</html>
