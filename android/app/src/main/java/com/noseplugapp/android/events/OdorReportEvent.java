package com.noseplugapp.android.events;

import com.noseplugapp.android.models.OdorReport;

public class OdorReportEvent {
    public final OdorReport odorReport;
    public OdorReportEvent(OdorReport odorReport) {
        this.odorReport = odorReport;
    }
}
