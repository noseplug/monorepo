package edu.gatech.cs.environmentalodors;

/**
 * IntentExtraNames are used to get "extra" data from intents.
 * To be honest, I've got no idea where these are supposed to go.
 */
public final class IntentExtraNames {
    private static final String pkg = IntentExtraNames.class.getPackage().getName();
    public static final String SELECTED_LOCATION = pkg + ".SelectedLocation";
    public static final String SELECTED_REPORT_DATE = pkg + ".SelectedReportDate";
}
