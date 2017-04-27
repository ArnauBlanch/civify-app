package com.civify.activity.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.adapter.LocalityCallback;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserSimpleCallback;
import com.civify.model.User;
import com.civify.model.map.CivifyMarker;
import com.civify.utils.ServiceGenerator;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

public class IssueDetailsFragment extends Fragment {

    private static final String DEBUG = "debug-IssueDetails";
    private static final int MILLISECONDS_TO_DAYS = 86400000;

    private View mViewDetails;

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
        mViewDetails = inflater.inflate(R.layout.fragment_issue_details, container, false);
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mViewDetails;
    }



    private void init() throws IOException {
        Log.v(DEBUG, "init");
        Log.v(DEBUG, "Getting arguments from bundle");
        Bundle bundle = getArguments();
        CivifyMarker<?> marker = (CivifyMarker<?>) bundle.getSerializable("marker");

        Log.v(DEBUG, "Adding image issue in layout");
        ImageView imageIssue = (ImageView)mViewDetails.findViewById(R.id.eventView);
        String url = ServiceGenerator.BASE_URL + marker.getIssue().getPicture().getLargeUrl();
        Glide.with(this).load(url).into(imageIssue);

        Log.v(DEBUG, "Adding issue title in layout");
        TextView nameIssue = (TextView) mViewDetails.findViewById(R.id.nameText);
        nameIssue.setText(marker.getIssue().getTitle());
        nameIssue.setMovementMethod(new ScrollingMovementMethod());

        Log.v(DEBUG, "Adding confirm value in layout");
        TextView likesIssue = (TextView)mViewDetails.findViewById(R.id.likesText);
        likesIssue.setText("+" + String.valueOf(marker.getIssue().getConfirmVotes()));

        Log.v(DEBUG, "Adding icon and name category in layout");
        ImageView categoryIcon = (ImageView)mViewDetails.findViewById(R.id.categoryView);
        categoryIcon.setImageResource(marker.getIssue().getCategory().getIcon());
        TextView categoryIssue = (TextView)mViewDetails.findViewById(R.id.nameCategoryText);
        int idCategory = marker.getIssue().getCategory().getName();
        categoryIssue.setText(getString(idCategory));

        Log.v(DEBUG, "Adding risk in layout");
        TextView riskIssue = (TextView)mViewDetails.findViewById(R.id.riskAnswer);
        riskIssue.setText(getText(R.string.no));
        if(marker.getIssue().isRisk()) riskIssue.setText(getText(R.string.yes));

        Log.v(DEBUG, "Adding description in layout");
        TextView descriptionIssue = (TextView)mViewDetails.findViewById(R.id.descriptionText);
        descriptionIssue.setText(marker.getIssue().getDescription());
        descriptionIssue.setMovementMethod(new ScrollingMovementMethod());

        Log.v(DEBUG, "Adding street in layout");
        final TextView streetIssue = (TextView)mViewDetails.findViewById(R.id.streetText);
        marker.getAddress(new LocalityCallback() {
            @Override
            public void onLocalityResponse(@NonNull String address) {
                streetIssue.setText(address);
            }

            @Override
            public void onLocalityError() {}
        });

        Log.v(DEBUG, "Adding distance in layout");
        TextView distanceIssue = (TextView)mViewDetails.findViewById(R.id.distanceText);
        float distance = marker.getDistanceFromCurrentLocation()/1000;
        String stringDistance = String.valueOf(distance);
        StringTokenizer token = new StringTokenizer(stringDistance, ".");
        String distanceToken = token.nextToken();
        if(distanceToken != "0") {
            distanceIssue.setText(distanceToken + " " + getText(R.string.km));
        }
        else {
            distance = marker.getDistanceFromCurrentLocation()/1000000;
            stringDistance = String.valueOf(distance);
            token = new StringTokenizer(stringDistance, ".");
            distanceToken = token.nextToken();
            distanceIssue.setText(distanceToken + " " + getText(R.string.m));
        }

        Log.v(DEBUG, "Adding time in layout");
        TextView timeIssue = (TextView)mViewDetails.findViewById(R.id.sinceText);
        Date date = new Date();
        Date dateIssue = marker.getIssue().getCreatedAt();
        if(marker.getIssue().getUpdatedAt() != null){
            dateIssue = marker.getIssue().getUpdatedAt();
        }
        long difference = date.getTime() - dateIssue.getTime();
        difference /= MILLISECONDS_TO_DAYS;
        timeIssue.setText(difference + " " + getText(R.string.days));

        Log.v(DEBUG, "Adding user in layout");
        String authToken = marker.getIssue().getIssueAuthToken();
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("USERPREFS",
                Context
                .MODE_PRIVATE);
        new UserAdapter(sharedPreferences).getUser(authToken, new UserSimpleCallback() {
            @Override
            public void onSuccess(User user) {
                setUser(user);
            }

            @Override
            public void onFailure() {
                setUser(buildFakeUser());
            }
        });
        Log.v(DEBUG, "init finished");
    }

    private User buildFakeUser() {
        User fakeUser = new User("demingo7", "Iván", "de Mingo", "ivanDeMingo@hotmail.com",
                "ivan1234", "ivan1234");
        fakeUser.setLevel(3);

        return fakeUser;
    }

    private void setUser(User user) {
        Log.v(DEBUG, "setUser");
        // progressBar.setProgress(user.getLevel()/utils.calcMaxLevel(userLevel) * 100);

        ProgressBar progressBar = (ProgressBar) mViewDetails.findViewById(R.id.userProgress);

        TextView name = (TextView) mViewDetails.findViewById(R.id.userName);
        name.setText(user.getName() + " " + user.getSurname());

        TextView username = (TextView) mViewDetails.findViewById(R.id.userUsername);
        username.setText(user.getUsername());

        TextView level = (TextView) mViewDetails.findViewById(R.id.userLevel);
        String userLevel = Integer.toString(user.getLevel());
        String showLevel = getString(R.string.level) + ' ' + userLevel;
        level.setText(showLevel);

        CircularImageView profileImage =
                (CircularImageView) mViewDetails.findViewById(R.id.userImage);
        //profileImage.setImageBitmap(img); // bitmap
        //profileImage.setImageIcon(img); // icon
        Log.v(DEBUG, "setUser finished");
    }

}
