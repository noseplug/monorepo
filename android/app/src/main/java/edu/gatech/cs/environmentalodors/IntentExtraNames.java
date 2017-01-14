package edu.gatech.cs.environmentalodors;

/**
 * IntentExtraNames are used to get "extra" data from intents.
 * To be honest, I've got no idea where these are supposed to go.
 */
final class IntentExtraNames {
    private IntentExtraNames(){} // Make constructor private.

    private static final String PKG = IntentExtraNames.class.getPackage().getName();
    static final String LOCATION = PKG + ".Location";
    static final String REPORT_DATE = PKG + ".ReportDate";
    static final String ODOR_EVENT_ID = PKG + ".OdorEventID";
    static final String ODOR_REPORT_ID = PKG + ".OdorReportID";

}
