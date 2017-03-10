<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="org.openshift.evg.workshopper.workshops.Workshops" %>
<% Workshops workshops = ((Workshops) application.getAttribute("workshops")); %>
<html>
<head>
    <title>Workshopper</title>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/css/tether.css">
    <link rel="stylesheet" type="text/css" href="/css/coreui.css">
    <link rel="stylesheet" type="text/css" href="/css/font-awesome.css">
    <%--<link rel="stylesheet" type="text/css" href="/css/darcula.css">--%>
    <link rel="stylesheet" type="text/css" href="/css/custom.css">
    <script type="application/javascript" src="/js/jquery.js"></script>
    <script type="application/javascript" src="/js/tether.js"></script>
    <script type="application/javascript" src="/js/bootstrap.js"></script>
    <script type="application/javascript" src="/js/liquid.js"></script>
    <script type="application/javascript" src="/js/vue.js"></script>
    <script type="application/javascript" src="/js/asciidoctor.js"></script>
    <script type="application/javascript" src="/js/js.cookie.js"></script>
    <%--<script type="application/javascript" src="/js/highlight.js"></script>--%>
    <script type="application/javascript" src="/js/scripts.js"></script>
</head>

<body>
<nav class="navbar fixed-top navbar-inverse bg-inverse navbar-toggleable-md">
    <div class="container">
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>
        <a class="navbar-brand" href="#">Workshopper</a>
        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav mr-auto">
            </ul>
            <span class="navbar-text">
                <a href="https://github.com/osevg/workshopper" target="_blank">
                    Source code @ Github
                </a>
            </span>
        </div>
    </div>
</nav>

<main class="main">
    <div class="container" id="content">
    </div>
</main>

</body>
</html>