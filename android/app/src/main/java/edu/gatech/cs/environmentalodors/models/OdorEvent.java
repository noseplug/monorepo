package edu.gatech.cs.environmentalodors.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * OdorEvent is a group of spatiotemporally related {@link OdorReport}s.
 *
 * For example:
 *      - The smoke odor from a wildfire could affect a vast area like a huge swath of a state,
 *        but last only a few days. (geographically spread out, temporally close)
 *      - A sulfur odor from a paper mill could affect a small area like a neighborhood,
 *        but last for years. (geographically close, temporally spread out)
 *      - etc.
 *
 * These will probably have to be crowdsourced or curated by experts, rather than automatically
 * created. Unless someone here is already an expert on machine learning.
 *
 * Many OdorEvents will only have a single OdorReport.
 */
public class OdorEvent {
    public final UUID uuid = UUID.randomUUID();
    private List<OdorReport> odorReports = new ArrayList<>(1);

    public OdorEvent(OdorReport odorReport) {
        odorReports.add(odorReport);
    }

    public List<OdorReport> getOdorReports() {
        return odorReports;
    }
}
