package edu.gatech.cs.environmentalodors;

/**
 * IntentExtraNames are used to get "extra" data from intents.
 * To be honest, I've got no idea where these are supposed to go.
 */
final class IntentExtraNames {
    private static final String pkg = IntentExtraNames.class.getPackage().getName();
    static final String LOCATION = pkg + ".Location";
    static final String REPORT_DATE = pkg + ".ReportDate";
    static final String ODOR_EVENT_ID = pkg + ".OdorEventID";
    static final String ODOR_REPORT_ID = pkg + ".OdorReportID";

}
