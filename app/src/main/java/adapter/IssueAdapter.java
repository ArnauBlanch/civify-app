package adapter;

import model.Issue;
import service.IIssueService;

/**
 * Created by MSI on 27/03/2017.
 */

public class IssueAdapter {

    IIssueService mIIsueService;
    Issue mIssue;


    public IssueAdapter() {
    }

    public String getTitle() {
        return mIssue.getTitle();
    }

    public String getCategory() {
        return mIssue.getCaregory();
    }

    public boolean getRisk() {
        return mIssue.isRisk();
    }

    public String getDescription() {
        return mIssue.getDescription();
    }

    public String getStreet() {
        //TODO become coord in street
        String street = "";
        return street;
    }

    public int getDistance() {
        int distance = 0;
        return distance;
    }

    public String getDate() {
        String date = "3 days ago";
        return date;
    }
}
