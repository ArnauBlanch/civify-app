package com.civify.activity.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.activity.EditIssueActivity;
import com.civify.adapter.LocalityCallback;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserSimpleCallback;
import com.civify.model.User;
import com.civify.model.issue.Issue;
import com.civify.model.map.CivifyMarker;
import com.civify.utils.ServiceGenerator;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

import org.ocpsoft.prettytime.PrettyTime;

public class IssueDetailsFragment extends Fragment {

    private static final String DEBUG = "debug-IssueDetails";
    private static final String WHITE_SPACE = " ";
    private static final String TAG_MARKER = "marker";

    private static final int MILLISECONDS_TO_DAYS = 86400000;
    private static final int DISTANCE_TO_KILOMETERS = 1000;
    private static final int DISTANCE_TO_METERS = 1000000;
    private static final int LEVEL_FAKE_USER = 3;

    private CivifyMarker<?> mMarker;
    private Issue mIssue;

    private View mViewDetails;

    public IssueDetailsFragment() {
        // Required empty public constructor
    }

    public static IssueDetailsFragment newInstance(CivifyMarker<?> marker) {
        IssueDetailsFragment fragment = new IssueDetailsFragment();
        Bundle data = new Bundle();
        data.putSerializable(TAG_MARKER, marker);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
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
        mMarker = (CivifyMarker<?>) bundle.getSerializable(TAG_MARKER);

        mIssue = mMarker.getIssue();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mMarker.getIssue().getTitle());

        addImageIssue();
        addIssueTitle();
        addConfirmValue();
        addCategoryValue();
        addRisk();
        addDescription();
        addStreet();
        addDistance();
        addTime();
        addUser();

        Log.v(DEBUG, "init finished");
    }

    private void addUser() {
        Log.v(DEBUG, "Adding user in layout");
        String authToken = mMarker.getIssue().getIssueAuthToken();
        SharedPreferences sharedPreferences = getActivity().getApplicationContext()
                .getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
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
    }

    private void addTime() {
        Log.v(DEBUG, "Adding time in layout");
        TextView timeIssue = (TextView) mViewDetails.findViewById(R.id.sinceText);
        Date dateIssue = mMarker.getIssue().getCreatedAt();
        if (mMarker.getIssue().getUpdatedAt() != null) {
            dateIssue = mMarker.getIssue().getUpdatedAt();
        }
        timeIssue.setText(new PrettyTime().format(dateIssue));
    }

    private void addDistance() {
        Log.v(DEBUG, "Adding distance in layout");
        TextView distanceIssue = (TextView) mViewDetails.findViewById(R.id.distanceText);
        String point = ".";
        float distance = mMarker.getDistanceFromCurrentLocation() / DISTANCE_TO_KILOMETERS;
        String stringDistance = String.valueOf(distance);
        StringTokenizer token = new StringTokenizer(stringDistance, point);
        String distanceToken = token.nextToken();
        if ("0".equals(distanceToken)) {
            distance = mMarker.getDistanceFromCurrentLocation() / DISTANCE_TO_METERS;
            stringDistance = String.valueOf(distance);
            token = new StringTokenizer(stringDistance, point);
            distanceToken = token.nextToken();
            distanceIssue.setText(distanceToken + WHITE_SPACE + getText(R.string.m));
        } else {
            distanceIssue.setText(distanceToken + WHITE_SPACE + getText(R.string.km));
        }
    }

    private void addStreet() {
        Log.v(DEBUG, "Adding street in layout");
        final TextView streetIssue = (TextView) mViewDetails.findViewById(R.id.streetText);
        mMarker.getAddress(new LocalityCallback() {
            @Override
            public void onLocalityResponse(@NonNull String address) {
                streetIssue.setText(address);
            }

            @Override
            public void onLocalityError() {

            }
        });
    }

    private void addDescription() {
        Log.v(DEBUG, "Adding description in layout");
        TextView descriptionIssue = (TextView) mViewDetails.findViewById(R.id.descriptionText);
        descriptionIssue.setText(mMarker.getIssue().getDescription());
        descriptionIssue.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addRisk() {
        Log.v(DEBUG, "Adding risk in layout");
        TextView riskIssue = (TextView) mViewDetails.findViewById(R.id.riskAnswer);
        riskIssue.setText(getText(R.string.no));
        if (mMarker.getIssue().isRisk()) riskIssue.setText(getText(R.string.yes));
    }

    private void addCategoryValue() {
        Log.v(DEBUG, "Adding icon and name category in layout");
        ImageView categoryIcon = (ImageView) mViewDetails.findViewById(R.id.categoryView);
        categoryIcon.setImageResource(mMarker.getIssue().getCategory().getIcon());
        TextView categoryIssue = (TextView) mViewDetails.findViewById(R.id.nameCategoryText);
        int idCategory = mMarker.getIssue().getCategory().getName();
        categoryIssue.setText(getString(idCategory));
    }

    private void addConfirmValue() {
        Log.v(DEBUG, "Adding confirm value in layout");
        TextView likesIssue = (TextView) mViewDetails.findViewById(R.id.likesText);
        likesIssue.setText("+" + String.valueOf(mMarker.getIssue().getConfirmVotes()));
    }

    private void addIssueTitle() {
        Log.v(DEBUG, "Adding issue title in layout");
        TextView nameIssue = (TextView) mViewDetails.findViewById(R.id.nameText);
        nameIssue.setText(mMarker.getIssue().getTitle());
        nameIssue.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addImageIssue() {
        Log.v(DEBUG, "Adding image issue in layout");
        ImageView imageIssue = (ImageView) mViewDetails.findViewById(R.id.eventView);
        String url = ServiceGenerator.BASE_URL + mMarker.getIssue().getPicture().getLargeUrl();
        Glide.with(this).load(url).into(imageIssue);
    }

    private User buildFakeUser() {
        String password = "ivan1234";
        User fakeUser = new User("demingo7", "Iván", "de Mingo", "ivanDeMingo@hotmail.com",
                password, password);
        fakeUser.setLevel(LEVEL_FAKE_USER);

        return fakeUser;
    }

    private void setUser(User user) {
        Log.v(DEBUG, "setUser");
        // progressBar.setProgress(user.getLevel()/utils.calcMaxLevel(userLevel) * 100);

        ProgressBar progressBar = (ProgressBar) mViewDetails.findViewById(R.id.userProgress);

        TextView name = (TextView) mViewDetails.findViewById(R.id.userName);
        name.setText(user.getName() + WHITE_SPACE + user.getSurname());

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_issue:
                Log.d("Ricard", "has clicat a edit issue");
                Intent intent = new Intent(getActivity(), EditIssueActivity.class);
                intent.putExtra("issue", mIssue);
                startActivity(intent);
                return false;
            default:
                break;
        }

        return false;
    }

}
