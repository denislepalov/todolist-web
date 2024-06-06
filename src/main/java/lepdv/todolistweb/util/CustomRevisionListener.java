package lepdv.todolistweb.util;

import lepdv.todolistweb.entity.Revision;
import java.time.Instant;


public class CustomRevisionListener implements org.hibernate.envers.RevisionListener {

    @Override
    public void newRevision(Object userRevision) {

        String authUsername = AuthUser.getAuthUsername();
        Revision revision = (Revision) userRevision;
        revision.setModifiedBy(authUsername);
        revision.setDateTime(Instant.now());
    }
}
