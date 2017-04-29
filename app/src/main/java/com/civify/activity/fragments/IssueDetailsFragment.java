package com.civify.activity.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.EditIssueActivity;
import com.civify.adapter.GeocoderAdapter;
import com.civify.adapter.LocalityCallback;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserSimpleCallback;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.User;
import com.civify.model.issue.Issue;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.IssueMarker;
import com.civify.service.issue.IssueSimpleCallback;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ServiceGenerator;
import com.google.android.gms.maps.model.LatLng;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Date;
import java.util.StringTokenizer;

import org.ocpsoft.prettytime.PrettyTime;

public class IssueDetailsFragment extends Fragment {

    private static final String DEBUG = "debug-IssueDetails";
    private static final String TAG_ISSUE = "issue";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_POSITION = "position";

    private static final int DISTANCE_TO_KILOMETERS = 1000;
    private static final int DISTANCE_TO_METERS = 1000000;

    private Issue mIssue;
    private IssueAdapter mIssueAdapter;
    private UserAdapter mUserAdapter;
    private float mDistance;
    private String mAddress;

    private View mViewDetails;

    public IssueDetailsFragment() {
        // Required empty public constructor
    }

    public static IssueDetailsFragment newInstance(IssueMarker issueMarker) {
        IssueDetailsFragment fragment = new IssueDetailsFragment();
        Bundle data = new Bundle();
        data.putSerializable(TAG_ISSUE, issueMarker.getIssue());
        data.putParcelable(TAG_POSITION, issueMarker.getPosition());
        data.putFloat(TAG_DISTANCE, issueMarker.getDistanceFromCurrentLocation());
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
        init();
        return mViewDetails;
    }

    private void init() {
        Log.v(DEBUG, "init");

        Log.v(DEBUG, "Getting arguments from bundle");
        Bundle bundle = getArguments();
        mIssue = (Issue) bundle.getSerializable(TAG_ISSUE);
        mDistance = bundle.getFloat(TAG_DISTANCE);

        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(getActivity());
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mIssue.getTitle());

        addImageIssue();
        addIssueTitle();
        addConfirmValue();
        addCategoryValue();
        addRisk();
        addDescription();
        addDistance();
        addTime();
        addUser();
        addStreetAsync((LatLng) bundle.getParcelable(TAG_POSITION));

        Log.v(DEBUG, "init finished");
    }

    private void addUser() {
        Log.v(DEBUG, "Adding user in layout");
        String authToken = mIssue.getIssueAuthToken();
        SharedPreferences sharedPreferences = getActivity().getApplicationContext()
                .getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
        mUserAdapter = new UserAdapter(sharedPreferences);
        mUserAdapter.getUser(authToken, new UserSimpleCallback() {
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
        Date dateIssue = mIssue.getCreatedAt();
        if (mIssue.getUpdatedAt() != null) {
            dateIssue = mIssue.getUpdatedAt();
        }
        timeIssue.setText(new PrettyTime().format(dateIssue));
    }

    private void addDistance() {
        Log.v(DEBUG, "Adding distance in layout");
        TextView distanceIssue = (TextView) mViewDetails.findViewById(R.id.distanceText);
        String point = ".";
        float distance = mDistance / DISTANCE_TO_KILOMETERS;
        String stringDistance = String.valueOf(distance);
        StringTokenizer token = new StringTokenizer(stringDistance, point);
        String distanceToken = token.nextToken();
        if ("0".equals(distanceToken)) {
            distance = mDistance / DISTANCE_TO_METERS;
            stringDistance = String.valueOf(distance);
            token = new StringTokenizer(stringDistance, point);
            distanceToken = token.nextToken();
            distanceIssue.setText(distanceToken + ' ' + getText(R.string.m));
        } else {
            distanceIssue.setText(distanceToken + ' ' + getText(R.string.km));
        }
    }

    private void addStreetAsync(LatLng position) {
        if (position != null) {
            GeocoderAdapter.getLocality(getContext(),
                    LocationAdapter.getLocation(position),
                    getAddressAsyncCallback());
        }
    }

    private LocalityCallback getAddressAsyncCallback() {
        return new LocalityCallback() {
            @Override
            public void onLocalityResponse(@NonNull String address) {
                addStreet(address);
            }

            @Override
            public void onLocalityError() {}
        };
    }

    private void addStreet(String address) {
        Log.v(DEBUG, "Adding street in layout");
        mAddress = address;
        TextView streetIssue = (TextView) mViewDetails.findViewById(R.id.streetText);
        streetIssue.setText(mAddress);
    }

    private void addDescription() {
        Log.v(DEBUG, "Adding description in layout");
        TextView descriptionIssue = (TextView) mViewDetails.findViewById(R.id.descriptionText);
        descriptionIssue.setText(mIssue.getDescription());
        descriptionIssue.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addRisk() {
        Log.v(DEBUG, "Adding risk in layout");
        TextView riskIssue = (TextView) mViewDetails.findViewById(R.id.riskAnswer);
        riskIssue.setText(getText(R.string.no));
        if (mIssue.isRisk()) riskIssue.setText(getText(R.string.yes));
    }

    private void addCategoryValue() {
        Log.v(DEBUG, "Adding icon and name category in layout");
        ImageView categoryIcon = (ImageView) mViewDetails.findViewById(R.id.categoryView);
        categoryIcon.setImageResource(mIssue.getCategory().getIcon());
        TextView categoryIssue = (TextView) mViewDetails.findViewById(R.id.nameCategoryText);
        int idCategory = mIssue.getCategory().getName();
        categoryIssue.setText(getString(idCategory));
    }

    private void addConfirmValue() {
        Log.v(DEBUG, "Adding confirm value in layout");
        TextView likesIssue = (TextView) mViewDetails.findViewById(R.id.likesText);
        likesIssue.setText("+" + mIssue.getConfirmVotes());
    }

    private void addIssueTitle() {
        Log.v(DEBUG, "Adding issue title in layout");
        TextView nameIssue = (TextView) mViewDetails.findViewById(R.id.nameText);
        nameIssue.setText(mIssue.getTitle());
        nameIssue.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addImageIssue() {
        Log.v(DEBUG, "Adding image issue in layout");
        ImageView imageIssue = (ImageView) mViewDetails.findViewById(R.id.eventView);
        String url = ServiceGenerator.BASE_URL + mIssue.getPicture().getLargeUrl();
        Glide.with(this).load(url).into(imageIssue);
    }

    private User buildFakeUser() {
        String password = "";
        User fakeUser = new User("", "User couldn't be retrieved", "",
                "example@mail.com", password, password);
        fakeUser.setLevel(1);
        return fakeUser;
    }

    private void setUser(User user) {
        Log.v(DEBUG, "setUser");
        // progressBar.setProgress(user.getLevel()/utils.calcMaxLevel(userLevel) * 100);

        ProgressBar progressBar = (ProgressBar) mViewDetails.findViewById(R.id.userProgress);

        TextView name = (TextView) mViewDetails.findViewById(R.id.userName);
        name.setText(user.getName() + ' ' + user.getSurname());

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_issue:
                launchEditActivity();
                return false;
            case R.id.delete_issue:
                deleteIssue();
                return false;
            default:
                break;
        }

        return false;
    }

    private void deleteIssue() {
        if (mIssue.getUserAuthToken().equals(mUserAdapter.getCurrentUser().getUserAuthToken())) {
            DialogInterface.OnClickListener dialogClickListener =
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mIssueAdapter.deleteIssue(mIssue.getIssueAuthToken(),
                                        new IssueSimpleCallback() {
                                    @Override
                                    public void onSuccess(Issue issue) {
                                        CivifyMap.getInstance().getMarkers().get(issue
                                                .getIssueAuthToken()).remove();
                                        getActivity().onBackPressed();
                                        Log.d("Ricard", "issue borrada");
                                    }

                                    @Override
                                    public void onFailure() {
                                       // Log.d("Ricard", "error borra issue");

                                    }
                                });
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                        default:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.delete_sure))
                    .setPositiveButton(getResources().getString(R.string.yes),
                    dialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.no), dialogClickListener)
                    .show();
        }
    }

    public void launchEditActivity() {
        DrawerActivity drawerActivity = (DrawerActivity) getActivity();
        Intent intent = new Intent(getActivity().getApplicationContext(), EditIssueActivity.class);
        drawerActivity.startActivity(intent);
    }

}
