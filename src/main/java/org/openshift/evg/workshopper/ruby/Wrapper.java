package org.openshift.evg.workshopper.ruby;

import org.jruby.embed.ScriptingContainer;
import org.openshift.evg.workshopper.workshops.Workshop;

public class Wrapper {

    private final ScriptingContainer container;
    private final Object wrapped;

    public Wrapper(ScriptingContainer container, Object wrapped) {
        this.container = container;
        this.wrapped = wrapped;
    }

    public void put(String name, Object value) {
        this.container.callMethod(this.wrapped, "put", name, value);
    }

    public String render(String src) {
        this.container.put(this.wrapped, "@data", src);
        return (String) this.container.callMethod(this.wrapped, "render");
    }

    public void setup(Workshop workshop) {
        this.container.put(this.wrapped, "@name", workshop.getId());
    }

}
