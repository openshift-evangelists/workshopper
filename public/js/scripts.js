var Route = function (pattern, route) {
    this.pattern = pattern;
    this.route = route;
};

var Router = function () {
    var self = this;

    self.routes = [];

    self.add = function(pattern, route) {
        self.routes.push(new Route(pattern, route));
    };

    self.flush = function () {};

    self.route = function () {
        var path = location.hash;
        if (path === "") {
            path = location.hash = '/';
            return;
        }

        self.flush();

        for(var x in self.routes) {
            var route = self.routes[x];
            var match = null;
            if(match = route.pattern.exec(path)) {
                route.route(match);
            }
        }
    };
};

var EvMan = function () {
    var self = this;

    self.content = $("#contentbox");

    self.router = new Router();

    self.router.flush = function() {
        self.content.html('<div style="text-align: center;font-size: 20px;"><i class="fa fa-spinner fa-pulse fa-fw"></i> &nbsp; Loading</div>');
    };

    self.index = function(match) {
        $.get('/api/workshops', function(workshops) {

            $('#workshopName').html('Workshopper');

            if (Object.keys(workshops).length === 1) {
                var ws = workshops[Object.keys(workshops)[0]];
                location.hash = '/workshop/' + ws.id;
                return;
            }

            $.get('/workshops.html', function(template) {
                self.content.html(template);
                new Vue({
                    el: '#workshops',
                    data: {
                        workshops: workshops
                    }
                })
            });

        });
    };

    self.workshop = function(match) {
        var workshop = match[1];
        $.get("/api/workshops/" + workshop, function(data) {
            location.hash = '/workshop/' + workshop + "/module/" + data.modules[0];
        });
    };

    self.module = function(match) {
        var workshop = match[1];
        var module = match[2];

        var url = new URI(document.URL);

        var data = {
            module: module
        };

        $.get('/api/workshops', function(workshops) {
            data.workshops = workshops;

            $.get("/api/workshops/" + workshop, function(tmp) {
                data.workshop = tmp;

                $.get("/api/workshops/" + workshop + "/modules", function(modules) {
                    data.modules = modules;

                    $.get("/api/workshops/" + workshop + "/content/module/" + module + "?" + url.query(), function(tmp) {
                        data.content = tmp;

                        data.doneModules = self.loadDoneModules();

                        window.name = data.workshop['name'];

                        for (var i = 0; i < data.workshop.modules.length; i++) {
                            if (data.workshop.modules[i] === module) {
                                data.prevModule = data.workshop.modules[i - 1];
                                data.nextModule = data.workshop.modules[i + 1];
                                data.currentModule = i + 1;
                            }
                        }

                        $('title').html(data.workshop.name);

                        if (modules[module] !== null && modules[module].requires !== null && modules[module].requires.length > 0) {
                            var prereqs = $("<div/>");
                            prereqs.addClass("module-prerequisites").html("These modules are required before starting with the current module:");
                            var list = $("<ul/>");
                            prereqs.append(list);
                            $.each(modules[module].requires, function(i, prereqModule) {
                                list.append("<li><a href='" + "#/workshop/" + workshop + "/module/" + prereqModule + "'>" + modules[prereqModule].name + "</a></li>");
                            });
                        }

                        $.get('/module.html', function(template) {
                            self.content.html(template);
                            new Vue({
                                el: '#module',
                                data: data
                            });
                            $('pre code').each(function(i, block) {
                                hljs.highlightBlock(block);
                            });
                            $(".mark-as-done").click(function() {
                                self.doneModule(data.doneModules, module);
                            });

                            if (Object.keys(workshops).length === 1) {
                                $('#breadcrumbs').css('display', 'none');
                            }
                            $('#workshopName').html(data.workshop.name);
                        });
                    });
                });
            });
        });
    };

    self.all = function(match) {
    };

    self.loadDoneModules = function() {
        var doneModules = Cookies.get("done-modules");

        if (typeof doneModules !== 'undefined') {
            doneModules = doneModules.split(';');
        } else {
            doneModules = [];
        }

        return doneModules;
    };

    self.doneModule = function(doneModules, module) {
        if (doneModules.indexOf(module) === -1) {
            doneModules.push(module);
        }
        Cookies.set("done-modules", doneModules.join(';'));
    };

    self.router.add(new RegExp(/^#[\/]?$/), self.index);
    self.router.add(new RegExp(/^#\/workshop\/([^\/]+)[\/]?$/), self.workshop);
    self.router.add(new RegExp(/^#\/workshop\/([^\/]+)\/all[\/]?$/), self.all);
    self.router.add(new RegExp(/^#\/workshop\/([^\/]+)\/module\/([^\/]+)[\/]?$/), self.module);
};

$(function() {
    window.evman = new EvMan();
    window.evman.router.route();
});

$(window).on('hashchange', function() {
    window.evman.router.route();
});