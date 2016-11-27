package edu.gatech.cs.environmentalodors.events;

import edu.gatech.cs.environmentalodors.models.OdorReport;

public class OdorReportEvent {
    public final OdorReport odorReport;
    public OdorReportEvent(OdorReport odorReport) {
        this.odorReport = odorReport;
    }
}
