package edu.gatech.cs.environmentalodors.database;

import java.util.UUID;

import edu.gatech.cs.environmentalodors.models.OdorEvent;
import edu.gatech.cs.environmentalodors.models.OdorReport;
import edu.gatech.cs.environmentalodors.models.User;

/**
 * Created by Esteban on 2/8/17.
 */

public interface NoseplugAPIInterface {

    public OdorEvent getOdorEvent(UUID uuid);
    public OdorReport getOdorReport(UUID uuid);
    public User getUser(UUID uuid);
    public UUID addOdorEvent(OdorEvent event);
    public Iterable<OdorEvent> getOdorEvents();
    public UUID registerUser(User user);
}
