package com.civify.activity.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.civify.model.map.CivifyMarkers;
import com.civify.service.issue.IssueSimpleCallback;
import com.civify.utils.AdapterFactory;
import com.google.android.gms.maps.model.LatLng;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.ocpsoft.prettytime.PrettyTime;

public class IssueDetailsFragment extends Fragment {

    private static final String DEBUG = "debug-IssueDetails";
    private static final String TAG_ISSUE = "issue";

    private static final int DISTANCE_TO_KILOMETERS = 1000;
    private static final int DISTANCE_TO_METERS = 1000000;
    private static final int MIN_METERS_FROM_ISSUE = 70;
    private static final float DISABLED_ALPHA = 0.15f;

    private UserAdapter mUserAdapter;
    private IssueAdapter mIssueAdapter;

    private Issue mIssue;
    private float mDistance;

    private View mViewDetails;

    public IssueDetailsFragment() {
        // Required empty public constructor
    }

    public static IssueDetailsFragment newInstance(@NonNull Issue issue) {
        Bundle data = new Bundle();
        data.putSerializable(TAG_ISSUE, issue);
        IssueDetailsFragment fragment = new IssueDetailsFragment();
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
        mDistance = mIssue.getDistanceFromCurrentLocation();

        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(getActivity());
        mUserAdapter = AdapterFactory.getInstance().getUserAdapter(getActivity());
        setIssue();
        setPosition();
        updateIssue(mIssue.getIssueAuthToken());
        addUser();

        Log.v(DEBUG, "init finished");
    }

    private void setPosition() {
        addDistance();
        addStreetAsync(mIssue.getPosition());
    }

    private void setIssue() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mIssue.getTitle());

        addImageIssue();
        addIssueTitle();
        addConfirmValue();
        addCategoryValue();
        addRisk();
        addDescription();
        addTime();

        setupButtons();
    }

    private void updateIssue(String token) {
        final LatLng oldPosition = mIssue.getPosition();
        AdapterFactory.getInstance().getIssueAdapter(getContext())
                .getIssue(token, new IssueSimpleCallback() {
                    @Override
                    public void onSuccess(Issue issue) {
                        mIssue = issue;
                        setIssue();
                        if (!oldPosition.equals(issue.getPosition())) setPosition();
                    }

                    @Override
                    public void onFailure() {
                        Snackbar.make(mViewDetails, getString(R.string.couldnt_update_issue_info),
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void addUser() {
        Log.v(DEBUG, "Adding user in layout");
        mUserAdapter.getUser(mIssue.getUserAuthToken(), new UserSimpleCallback() {
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
        timeIssue.setText(new PrettyTime().format(mIssue.getCreatedAt()));
    }

    private void addDistance() {
        Log.v(DEBUG, "Adding distance in layout");
        TextView distanceIssue = (TextView) mViewDetails.findViewById(R.id.distanceText);
        distanceIssue.setText(mIssue.getDistanceFromCurrentLocationAsString());
    }

    private void addStreetAsync(LatLng position) {
        if (position != null) {
            GeocoderAdapter.getLocality(getContext(),
                    LocationAdapter.getLocation(position), getAddressAsyncCallback());
        }
    }

    private LocalityCallback getAddressAsyncCallback() {
        return new LocalityCallback() {
            @Override
            public void onLocalityResponse(@NonNull String address) {
                addStreet(address);
            }

            @Override
            public void onLocalityError() { }
        };
    }

    private void addStreet(String address) {
        Log.v(DEBUG, "Adding street in layout");
        TextView streetIssue = (TextView) mViewDetails.findViewById(R.id.streetText);
        streetIssue.setText(address);
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

    void addConfirmValue() {
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
        String url = mIssue.getPicture().getMedUrl();
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
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        mIssueAdapter.deleteIssue(mIssue.getIssueAuthToken(), getDeleteCallback());
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.delete_sure))
                    .setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.no), dialogClickListener)
                    .show();
        }
    }

    public IssueSimpleCallback getDeleteCallback() {
        return new IssueSimpleCallback() {
            @Override
            public void onSuccess(Issue issue) {
                CivifyMarkers markers = CivifyMap.getInstance().getMarkers();
                if (markers != null) markers.remove(issue.getIssueAuthToken());
                getActivity().onBackPressed();
                Log.d(DEBUG, "issue borrada");
            }

            @Override
            public void onFailure() {
                Snackbar.make(mViewDetails, "Couldn't delete issue", Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    private void setupButtons() {
        LinearLayout buttons = (LinearLayout) mViewDetails.findViewById(R.id.buttonsLayout);

        if (isTooFarFromIssue()) {
            buttons.setVisibility(View.GONE);
            mViewDetails.findViewById(R.id.too_far_message).setVisibility(View.VISIBLE);
        } else {
            setupConfirmButton();
            setupReportButton();
            setupResolveButton();
        }
    }

    private boolean isTooFarFromIssue() {
        return mDistance > MIN_METERS_FROM_ISSUE;
    }

    private void setupConfirmButton() {
        AppCompatButton button = (AppCompatButton) mViewDetails.findViewById(R.id.confirmButton);
        String issueUserToken = mIssue.getUserAuthToken();
        String userToken = UserAdapter.getCurrentUser().getUserAuthToken();

        if (issueUserToken.equals(userToken)) {
            button.setAlpha(DISABLED_ALPHA);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(getView(), R.string.cant_confirm_own_issue, Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        } else {
            if (mIssue.getConfirmedByAuthUser()) {
                changeButtonStyle(button, IssueButton.UNCONFIRM);
            }
            IssueButtonListener buttonListener =
                    new IssueButtonListener(this, mViewDetails, mIssue,
                            IssueButton.CONFIRM, IssueButton.UNCONFIRM);

            button.setOnClickListener(buttonListener);
        }
    }

    private void setupReportButton() {
        AppCompatButton button = (AppCompatButton) mViewDetails.findViewById(R.id.reportButton);
        String issueUserToken = mIssue.getUserAuthToken();
        String userToken = UserAdapter.getCurrentUser().getUserAuthToken();

        if (issueUserToken.equals(userToken)) {
            button.setAlpha(DISABLED_ALPHA);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(getView(), R.string.cant_report_own_issue, Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        } else {
            if (mIssue.getReportedByAuthUser()) {
                changeButtonStyle(button, IssueButton.UNREPORT);
            }
            IssueButtonListener buttonListener =
                    new IssueButtonListener(this, mViewDetails, mIssue,
                            IssueButton.REPORT, IssueButton.UNREPORT);
            button.setOnClickListener(buttonListener);
        }
    }

    private void setupResolveButton() {
        AppCompatButton button = (AppCompatButton) mViewDetails.findViewById(R.id.resolveButton);
        String issueUserToken = mIssue.getUserAuthToken();
        String userToken = UserAdapter.getCurrentUser().getUserAuthToken();

        if (mIssue.getResolvedByAuthUser()) {
            changeButtonStyle(button, IssueButton.UNRESOLVE);
        }
        IssueButtonListener buttonListener =
                new IssueButtonListener(this, mViewDetails, mIssue,
                        IssueButton.RESOLVE, IssueButton.UNRESOLVE);
        button.setOnClickListener(buttonListener);
    }

    public void changeButtonStyle(AppCompatButton button, IssueButton issueButton) {
        button.setText(issueButton.getText());
        button.setBackgroundResource(issueButton.getDrawable());
        button.setTextColor(getResources().getColor(issueButton.getTextColor()));
    }

    public void launchEditActivity() {
        DrawerActivity drawerActivity = (DrawerActivity) getActivity();
        Intent intent = new Intent(getActivity().getApplicationContext(), EditIssueActivity.class);
        drawerActivity.startActivity(intent);
    }
}
