package utils;

import model.Issue;
import service.IIssueService;

/**
 * Created by MSI on 27/03/2017.
 */

public class IssueAdapterDummy {

    private final String mTitle = "Cables del semafor al descobert";
    private final String mCategory = "Senyalització";
    private final String mRisk = "Si";
    private final String mDescription = "El semafor no te cap tapa per amagar els cables, "
            + "que estan completament al descobert";
    private final String mStreet = "Carrer de la Diputacio, 13, Barcelona";
    private final String mDistance = "1 km";
    private final String mDate = "fa 3 dies";

    public IssueAdapterDummy() {
    }

    public String getTitle() {
        return mTitle;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getRisk() {
        return mRisk;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getStreet() {
        return mStreet;
    }

    public String getDistance() {
        return mDistance;
    }

    public String getDate() {
        return mDate;
    }
}
