package org.openshift.evg.workshopper.workshops;

import java.util.*;

public class WorkshopModules {

    private List<String> activate = new LinkedList<>();
    private Map<String, String> revisions = new HashMap<>();

    public List<String> getActivate() {
        return activate;
    }

    public void setActivate(List<String> activate) {
        this.activate = activate;
    }

    public Map<String, String> getRevisions() {
        return revisions;
    }

    public void setRevisions(Map<String, String> revisions) {
        this.revisions = revisions;
    }

    @Override
    public String toString() {
        return "WorkshopModules{" +
                "activate=" + activate +
                ", revisions=" + revisions +
                '}';
    }
}
