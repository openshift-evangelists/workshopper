var rootPattern = new RegExp(/^#[\/]?$/);
var workshopPattern = new RegExp(/^#\/workshop\/([^\/]+)[\/]?$/);
var modulePattern = new RegExp(/^#\/workshop\/([^\/]+)\/module\/([^\/]+)[\/]?$/);

var asciidoctor = Asciidoctor();

var config;
var content;
var workshops = {};

var flush = function() {
    content.html('<div style="text-align: center;font-size: 20px;"><i class="fa fa-spinner fa-pulse fa-fw"></i> &nbsp; Loading</div>');
};

var doRouting = function() {
    var route = location.hash;

    if (route === "") {
        route = location.hash = '/';
        return;
    }

    flush();

    var match;

    if (match = rootPattern.exec(route)) {
        if (config['defaultWorkshop'] != null) {
            location.hash = '/workshop/' + config['defaultWorkshop'];
            return;
        }

        if (Object.keys(workshops).length === 1) {
            var ws = workshops[Object.keys(workshops)[0]];
            location.hash = '/workshop/' + ws.id;
            return;
        }

        $.get('/workshops.html', function(template) {
            content.html(template);
            new Vue({
                el: '#workshops',
                data: {
                    workshops: workshops
                }
            })
        });
        $('#workshopName').html('Workshopper');
    }

    if (match = workshopPattern.exec(route)) {
        var workshop = match[1];
        $.get("/api/workshops/" + workshop, function(data) {
            location.hash = '/workshop/' + workshop + "/module/" + data.sortedModules[0];
        });
    }

    if (match = modulePattern.exec(route)) {
        var workshop = match[1];
        var module = match[2];
        var prereqs = $("<div/>");

        $.get("/api/workshops/" + workshop + "/modules", function(modules) {
            var data = {
                module: module,
                modules: modules
            };

            if (modules[module] !== null && modules[module].requires !== null && modules[module].requires.length > 0) {
                prereqs.addClass("module-prerequisites").html("These modules are required before starting with the current module:");
                var list = $("<ul/>");
                prereqs.append(list);
                $.each(modules[module].requires, function(i, prereqModule) {
                    list.append("<li><a href='" + "#/workshop/" + workshop + "/module/" + prereqModule + "'>" + modules[prereqModule].name + "</a></li>");
                });
            }

            $.get("/api/workshops/" + workshop + "/env/" + module, function(env) {
                $.get("/api/workshops/" + workshop + "/content/module/" + module, function(template) {
                    var tmpl = Liquid.parse(template);
                    var options = [
                        'icons=font',
                        'imagesdir=/api/workshops/' + workshop + '/content/assets/images',
                        'source-highlighter=highlightjs'
                    ];

                    var url = new URI(document.URL).query(true);

                    for (var name in env.env) {
                        if (url[name] != null) {
                            env.env[name] = url[name];
                        }
                    }

                    data.content = asciidoctor.convert(tmpl.render(env.env), { attributes: options });
                    data.workshop = env.workshop;

                    // update page title
                    $('title').html(data.workshop.name);

                    data.doneModules = loadDoneModules();

                    for (var i = 0; i < env.workshop.sortedModules.length; i++) {
                        if (env.workshop.sortedModules[i] === module) {
                            data.prevModule = env.workshop.sortedModules[i - 1];
                            data.nextModule = env.workshop.sortedModules[i + 1];
                            data.currentModule = i + 1;
                        }
                    }

                    $.get('/module.html', function(template) {
                        content.html(template);
                        new Vue({
                            el: '#module',
                            data: data
                        });
                        // $('pre code').each(function(i, block) {
                        //     hljs.highlightBlock(block);
                        // });
                        $(".mark-as-done").click(function() {
                            doneModule(data.doneModules, module);
                        });

                        if (Object.keys(workshops).length === 1) {
                            $('#breadcrumbs').css('display', 'none');
                        }
                        $('#workshopName').html(data.workshop.name);
                    });
                });
            });
        });
    }
};

$(function() {
    content = $("#content");
    $.get('/api/config', function(data) {
        config = data;
        $.get("/api/workshops", function(data) {
            workshops = data;
            doRouting();
        });
    });
});

$(window).on('hashchange', function() {
    doRouting();
});

var loadDoneModules = function() {
    var doneModules = Cookies.get("done-modules");

    if (typeof doneModules !== 'undefined') {
        doneModules = doneModules.split(';');
    } else {
        doneModules = [];
    }

    return doneModules;
};

var doneModule = function(doneModules, module) {
    if (doneModules.indexOf(module) === -1) {
        doneModules.push(module);
    }
    Cookies.set("done-modules", doneModules.join(';'));
};