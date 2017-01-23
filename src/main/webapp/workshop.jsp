<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.openshift.evg.workshopper.modules.Modules" %>
<%@ page import="org.openshift.evg.workshopper.workshops.Workshop" %>
<% Modules modules = ((Modules) application.getAttribute("modules")); %>
<% Workshop workshop = ((Workshop) application.getAttribute("workshop")); %>

<jsp:include page="header.html" />

<div style="width: 50%; float: left;">
    <% for(String id : workshop.getSortedModules()){ %>
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="pull-left">
                <strong><%= modules.get(id).getName() %></strong>
            </div>
            <div class="pull-right"><a href="module/<%= id %>" class="btn btn-default">Open</a></div>
        </div>
    </div>
    <% } %>
</div>

<div style="width: 50%; float: left;">
    TMP
</div>

<jsp:include page="footer.html" />