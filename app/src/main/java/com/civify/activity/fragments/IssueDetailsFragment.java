package com.civify.activity.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.civify.R;
import com.civify.adapter.LocalityCallback;
import com.civify.model.User;
import com.civify.model.map.CivifyMarker;

import java.util.Date;

public class IssueDetailsFragment extends Fragment {

    public IssueDetailsFragment() {
        // Required empty public constructor
    }

    public static IssueDetailsFragment newInstance(CivifyMarker<?> marker) {
        IssueDetailsFragment fragment = new IssueDetailsFragment();
        Bundle data = new Bundle();
        data.putSerializable("marker", marker);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewDetails = inflater.inflate(R.layout.fragment_issue_details, container, false);
        init(viewDetails);
        return viewDetails;
    }

    private void init(@NonNull View viewDetails) {
        TextView nameIssue = (TextView) viewDetails.findViewById(R.id.nameText);
        TextView likesIssue = (TextView)viewDetails.findViewById(R.id.likesText);
        TextView categoryIssue = (TextView)viewDetails.findViewById(R.id.categoryText);
        TextView riskIssue = (TextView)viewDetails.findViewById(R.id.riskAnswer);
        TextView descriptionIssue = (TextView)viewDetails.findViewById(R.id.descriptionText);
        final TextView streetIssue = (TextView)viewDetails.findViewById(R.id.streetText);
        TextView distanceIssue = (TextView)viewDetails.findViewById(R.id.distanceText);
        TextView timeIssue = (TextView)viewDetails.findViewById(R.id.sinceText);
        User user;
        Bundle bundle = getArguments();
        CivifyMarker<?> marker = (CivifyMarker<?>) bundle.getSerializable("marker");
        nameIssue.setText(marker.getIssue().getTitle());
        likesIssue.setText("+23");
        categoryIssue.setText("Senyalitzacio");
        riskIssue.setText("No");
        if(marker.getIssue().getRisk()) riskIssue.setText("Yes");
        descriptionIssue.setText("New description");
        marker.getAddress(new LocalityCallback() {
            @Override
            public void onLocalityResponse(@NonNull String address) {
                streetIssue.setText(address);
            }

            @Override
            public void onLocalityError() {}
        });
        distanceIssue.setText(String.valueOf(marker.getDistanceFromCurrentLocation()));
        Date date = new Date();
        Date dateIssue = marker.getIssue().getDate();
        long difference = date.getTime() - dateIssue.getTime();
        difference = difference/1000/3600/24;
        timeIssue.setText( "Since " + difference  + " days");
        //setUser( new User("PESAdicto", "Sergio", "Sanchis", "sergio@gmail"
               //+ ".com", "lalalala", "lalalala"));
    }

    /*
    private void setUser(User user) {
        View userView = mNavigationView.getHeaderView(0);
        // progressBar.setProgress(user.getLevel()/utils.calcMaxLevel(userLevel) * 100);

        ProgressBar progressBar = (ProgressBar) userView.findViewById(R.id.userProgress);

        TextView name = (TextView) userView.findViewById(R.id.userName);
        name.setText(user.getName() + " " + user.getSurname());

        TextView username = (TextView) userView.findViewById(R.id.userUsername);
        username.setText(user.getUsername());

        TextView level = (TextView) userView.findViewById(R.id.userLevel);
        String userLevel = Integer.toString(user.getLevel());
        String showLevel = getString(R.string.level) + ' ' + userLevel;
        level.setText(showLevel);

        TextView xp = (TextView) userView.findViewById(R.id.userxp);
        String userExperience = Integer.toString(user.getExperience());
        //xp.setText(userExperience + '/' + utils.calcMaxXp(userLevel));

        TextView coins = (TextView) userView.findViewById(R.id.userCoins);
        String userCoins = Integer.toString(user.getCoins());
        coins.setText(userCoins);

        CircularImageView profileImage =
                (CircularImageView) userView.findViewById(R.id.userImage);
        //profileImage.setImageBitmap(img); // bitmap
        //profileImage.setImageIcon(img); // icon
    }
    */
}
