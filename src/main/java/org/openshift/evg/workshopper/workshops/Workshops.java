package org.openshift.evg.workshopper.workshops;

import javax.enterprise.inject.Vetoed;
import java.util.HashMap;
import java.util.Map;

@Vetoed
public class Workshops {

    private Map<String, Workshop> workshops = new HashMap<>();

    public void add(String id, Workshop workshop) {
        this.workshops.put(id, workshop);
    }

    public Workshop get(String id) {
        return this.workshops.get(id);
    }

    public Map<String, Workshop> get() {
        return workshops;
    }

}
