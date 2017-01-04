package org.openshift.evg.workshopper.workshops;

import java.util.List;
import java.util.Map;

public class WorkshopModules {

    private List<String> activate;
    private Map<String, String> revisions;

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

}
