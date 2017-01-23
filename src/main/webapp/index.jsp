<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.openshift.evg.workshopper.workshops.Workshops" %>
<% Workshops workshops = ((Workshops) application.getAttribute("workshops")); %>
<jsp:include page="header.html" />

<% for(String id : workshops.get().keySet()){ %>
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="pull-left">
                <strong><%= workshops.get(id).getName() %></strong>
            </div>
            <div class="pull-right"><a href="workshop/<%= id %>/" class="btn btn-default">Open</a></div>
        </div>
    </div>
<% } %>


<jsp:include page="footer.html" />
