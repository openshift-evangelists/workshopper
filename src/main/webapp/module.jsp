<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.openshift.evg.workshopper.modules.Modules" import="org.openshift.evg.workshopper.modules.Module" %>
<% Modules modules = ((Modules) application.getAttribute("modules")); %>
<% Module module = modules.get().get((String) application.getAttribute("module")); %>

<jsp:include page="header.html" />

<script type="application/javascript" src="../../../js/asciidoctor.js"></script>
<script type="application/javascript" src="../../../js/liquid.js"></script>
<link rel="stylesheet" type="text/css" href="../../../css/paper.css">
<link rel="stylesheet" type="text/css" href="../../../css/docs.css">
<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/3.2.1/css/font-awesome.min.css" />

<script type="application/javascript">
    $(function(){
        $.get("../../../api/workshops/<%= application.getAttribute("workshop") %>/env/<%= application.getAttribute("module") %>", function(env){
            var module = '<%= application.getAttribute("module") %>';
            module = module.replace("_", "/");
            $.get("../../../modules/" + module + ".adoc", function(data) {
                var asciidoctor = Asciidoctor();
                var tmpl = Liquid.parse(data);
                var html = asciidoctor.convert(tmpl.render(env.env), {attributes: ['icons=font']});
                $('#content').html(html);
            });
        });
    });
</script>

<div id="content"></div>

<a href="..">Back</a>

<jsp:include page="footer.html" />