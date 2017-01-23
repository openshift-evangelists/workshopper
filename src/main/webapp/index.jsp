<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.openshift.evg.workshopper.workshops.Workshops" %>
<% Workshops workshops = ((Workshops) application.getAttribute("workshops")); %>
<jsp:include page="header.html" />

<div class="container" id="content"></div>

<jsp:include page="footer.html" />
