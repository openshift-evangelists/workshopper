package org.openshift.evg.workshopper.ruby;

import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;
import org.openshift.evg.workshopper.workshops.Workshop;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RubyRuntime {

    private ScriptingContainer container;

    @PostConstruct
    public void setup() {
        this.container = new ScriptingContainer(LocalContextScope.CONCURRENT);
        this.container.runScriptlet("require 'workshopper'");
    }

    public Wrapper wrapper() {
        return new Wrapper(this.container, this.container.runScriptlet("Wrapper.new"));
    }

}
