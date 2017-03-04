package com.noseplugapp.android;

/**
 * IntentExtraNames are used to get "extra" data from intents.
 * To be honest, I've got no idea where these are supposed to go.
 */
final class IntentExtraNames {
    private static final String PKG = IntentExtraNames.class.getPackage().getName();
    public static final String LOCATION = PKG + ".Location";
    public static final String CREATE_DATE = PKG + ".CreateDate";
    public static final String REPORT_DATE = PKG + ".ReportDate";
    public static final String ODOR_EVENT_ID = PKG + ".OdorEventID";
    public static final String ODOR_REPORT_ID = PKG + ".OdorReportID";

    private IntentExtraNames(){} // Make constructor private.
}
