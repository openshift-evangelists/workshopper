var rootPattern = new RegExp(/^#[\/]?$/);
var workshopPattern = new RegExp(/^#\/workshop\/([^\/]+)[\/]?$/);
var modulePattern = new RegExp(/^#\/workshop\/([^\/]+)\/module\/([^\/]+)[\/]?$/);

var asciidoctor = Asciidoctor();

var config;
var content;
var modules;

var flush = function() {
    content.html('<div style="text-align: center;font-size: 20px;"><i class="fa fa-spinner fa-pulse fa-fw"></i> &nbsp; Loading</div>');
};

var doRouting = function() {
    var route = location.hash;

    if(route == "") {
        route = location.hash = '/';
        return;
    }

    flush();

    var match;
    if(match = rootPattern.exec(route)) {
        if(false && config['defaultWorkshop'] != null) {
            location.hash = '/workshop/' + config['defaultWorkshop'];
            return;
        }
        $.get("/api/workshops", function (data) {
            $.get('/workshops.html', function(template) {
                content.html(template);
                new Vue({
                    el: '#workshops',
                    data: {
                        workshops: data
                    }
                })
            });
        });
    }
    if(match = workshopPattern.exec(route)) {
        var workshop = match[1];
        $.get("/api/workshops/" + workshop, function (data) {
            $.get('/workshop.html', function(template) {
                content.html(template);
                new Vue({
                    el: '#workshop',
                    data: {
                        modules: modules,
                        workshop: data
                    }
                })
            });
        });
    }
    if(match = modulePattern.exec(route)) {
        var workshop = match[1];
        var module = match[2];
        var prereqs = $("<div/>");

        var data = {
            module: module,
            modules: modules
        };

        if (modules[module] !== null && modules[module].requires != null && modules[module].requires.length > 0) {
            prereqs.addClass("module-prerequisites").html("These modules are required before starting with the current module:");
            var list = $("<ul/>")
            prereqs.append(list);
            $.each(modules[module].requires, function(i, prereqModule) {
                list.append("<li><a href='" + "#/workshop/" + workshop + "/module/" + prereqModule + "'>" + modules[prereqModule].name + "</a></li>");
            });
        }

        $.get("/api/workshops/" + workshop + "/env/" + module, function(env){
            var path = module.replace("_", "/");
            var url = config['contentUrl'] + "/" + path + ".adoc";
            $.get(url, function(template) {
                var tmpl = Liquid.parse(template);
                var options = [
                    'icons=font',
                    'imagesdir=' + config['contentUrl'] + "/images"
                ];
                data.content = asciidoctor.convert(tmpl.render(env.env), {attributes: options});
                data.workshop = env.workshop;

                for(var i = 0; i < env.workshop.sortedModules.length; i++) {
                    var name = env.workshop.sortedModules[i];
                    if(name == module) {
                        data.prevModule = env.workshop.sortedModules[i - 1];
                        data.nextModule = env.workshop.sortedModules[i + 1];
                    }
                }

                $.get('/module.html', function(template) {
                    content.html(template);
                    new Vue({
                        el: '#module',
                        data: data
                    })
                });
            });
        });
    }

};
var setup = function() {
    content =  $("#content");
    $.get('/api/config', function(data){
        config = data;
        $.get('/api/modules', function(data){
            modules = data;
            doRouting();
        });
    });
};
$(function(){
    setup();

});
$(window).on('hashchange', function() {
    doRouting();
});
