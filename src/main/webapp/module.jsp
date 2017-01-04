<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.openshift.evg.workshopper.modules.Modules" import="org.openshift.evg.workshopper.modules.Module" %>
<% Modules modules = ((Modules) application.getAttribute("modules")); %>
<% Module module = modules.getModules().get((String) application.getAttribute("module")); %>
<html>
<head>
    <title>Workshop</title>
    <script type="application/javascript" src="../../../asciidoctor.js"></script>
    <script type="application/javascript" src="../../../jquery.js"></script>
    <script type="application/javascript" src="../../../liquid.js"></script>
    <script type="application/javascript">
        $(function(){
            $.get("../../../modules/<%= application.getAttribute("module") %>.adoc", function(data) {
                var asciidoctor = Asciidoctor();
                var html = asciidoctor.convert(data);
                var tmpl = Liquid.parse(html);
                $('#content').html(tmpl.render({}));
            });
        });
    </script>
</head>
<body>

<h1><%= module.getName() %></h1>

<div id="content">

</div>
<a href="..">Back</a>

</body>
</html>
