package com.civify.activity.fragments.issue;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.GeocoderAdapter;
import com.civify.adapter.LocalityCallback;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.SimpleCallback;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserAttacher;
import com.civify.adapter.UserSimpleCallback;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.User;
import com.civify.model.issue.Issue;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.CivifyMarkers;
import com.civify.model.map.IssueMarker;
import com.civify.service.issue.IssueSimpleCallback;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ServiceGenerator;
import com.civify.utils.StringUtils;
import com.google.android.gms.maps.model.LatLng;

import org.ocpsoft.prettytime.PrettyTime;

public class IssueDetailsFragment extends BasicFragment {

    private static final String TAG = "debug-IssueDetails";

    private static final String TAG_ISSUE = "issue";
    private static final int MIN_METERS_FROM_ISSUE = 70;
    private static final float DISABLED_ALPHA = 0.15f;
    private static final int SHOW_AS_ACTION_NEVER = 0;
    private static final int REQUEST_CODE = 0;

    private UserAdapter mUserAdapter;
    private IssueAdapter mIssueAdapter;

    private Issue mIssue;
    private Float mDistance;

    private View mViewDetails;

    public IssueDetailsFragment() {
    }

    public static IssueDetailsFragment newInstance(@NonNull Issue issue) {
        Bundle data = new Bundle();
        data.putSerializable(TAG_ISSUE, issue);
        IssueDetailsFragment fragment = new IssueDetailsFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.DETAILS_ISSUE_ID;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mIssue.getUserAuthToken().equals(mUserAdapter.getCurrentUser().getUserAuthToken())) {
            for (int i = 0; i < menu.size(); ++i) {
                menu.getItem(i).setVisible(false);
                menu.getItem(i).setShowAsAction(SHOW_AS_ACTION_NEVER);
            }
        }
    }

    private void init() {
        Log.v(TAG, "init");

        Log.v(TAG, "Getting arguments from bundle");
        Bundle bundle = getArguments();
        mIssue = (Issue) bundle.getSerializable(TAG_ISSUE);
        if (mIssue != null) {
            mDistance = mIssue.getDistanceFromCurrentLocation();
        }

        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(getActivity());
        mUserAdapter = AdapterFactory.getInstance().getUserAdapter(getActivity());
        setIssue();
        setPosition();
        setShareOptions();
        updateIssue(mIssue.getIssueAuthToken());
        addUser();

        Log.v(TAG, "init finished");
    }

    private void setPosition() {
        addDistance();
        addStreetAsync(mIssue.getPosition());
    }

    public void setIssue(Issue issue) {
        mIssue = issue;
        setIssue();
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
                        if (issue.isResolved()) {
                            LinearLayout buttons = (LinearLayout) mViewDetails
                                    .findViewById(R.id.buttonsLayout);
                            buttons.setVisibility(View.GONE);
                            TextView resolvedOrFar = (TextView) mViewDetails
                                    .findViewById(R.id.too_far_message);
                            resolvedOrFar.setText(R.string.issue_resolved);
                            resolvedOrFar.setVisibility(View.VISIBLE);
                        }
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
        Log.v(TAG, "Adding user in layout");
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
        Log.v(TAG, "Adding time in layout");
        TextView timeIssue = (TextView) mViewDetails.findViewById(R.id.sinceText);
        timeIssue.setText(new PrettyTime().format(mIssue.getCreatedAt()));
    }

    private void addDistance() {
        Log.v(TAG, "Adding distance in layout");
        TextView distanceIssue = (TextView) mViewDetails.findViewById(R.id.distanceText);
        String distanceString = mIssue.getDistanceFromCurrentLocationAsString();
        if (distanceString != null) {
            distanceIssue.setText(distanceString);
        } else {
            distanceIssue.setVisibility(View.GONE);
        }
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
        Log.v(TAG, "Adding street in layout");
        TextView streetIssue = (TextView) mViewDetails.findViewById(R.id.streetText);
        streetIssue.setText(StringUtils.capitalize(address));
    }

    private void addDescription() {
        Log.v(TAG, "Adding description in layout");
        TextView descriptionIssue = (TextView) mViewDetails.findViewById(R.id.descriptionText);
        descriptionIssue.setText(mIssue.getDescription());
        descriptionIssue.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addRisk() {
        Log.v(TAG, "Adding risk in layout");
        TextView riskIssue = (TextView) mViewDetails.findViewById(R.id.riskAnswer);
        riskIssue.setText((mIssue.isRisk()) ? R.string.yes : R.string.no);

        int green = VERSION.SDK_INT >= VERSION_CODES.M
                ? getResources().getColor(R.color.colorPrimary, getContext().getTheme())
                : getResources().getColor(R.color.colorPrimary);

        riskIssue.setTextColor((mIssue.isRisk()) ? Color.RED : green);
    }

    private void addCategoryValue() {
        Log.v(TAG, "Adding icon and name category in layout");
        ImageView categoryIcon = (ImageView) mViewDetails.findViewById(R.id.categoryView);
        categoryIcon.setImageResource(mIssue.getCategory().getIcon());
        TextView categoryIssue = (TextView) mViewDetails.findViewById(R.id.nameCategoryText);
        int idCategory = mIssue.getCategory().getName();
        categoryIssue.setText(getString(idCategory));
    }

    void addConfirmValue() {
        Log.v(TAG, "Adding confirm value in layout");
        TextView likesIssue = (TextView) mViewDetails.findViewById(R.id.likesText);
        likesIssue.setText("+" + mIssue.getConfirmVotes());
    }

    private void addIssueTitle() {
        Log.v(TAG, "Adding issue title in layout");
        TextView nameIssue = (TextView) mViewDetails.findViewById(R.id.nameText);
        nameIssue.setText(mIssue.getTitle());
        nameIssue.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addImageIssue() {
        Log.v(TAG, "Adding image issue in layout");
        ImageView imageIssue = (ImageView) mViewDetails.findViewById(R.id.eventView);
        String url = mIssue.getPicture().getMedUrl();
        Glide.with(this)
                .load(url)
                .centerCrop()
                .into(imageIssue);
    }

    private User buildFakeUser() {
        String password = "";
        return new User("", "User couldn't be retrieved", "",
                "example@mail.com", password, password);
    }

    private void setUser(User user) {
        Log.v(TAG, "setUser: init");

        UserAttacher.get(getContext(), user)
                .setFullName((TextView) mViewDetails.findViewById(R.id.userName))
                .setUsername((TextView) mViewDetails.findViewById(R.id.userUsername))
                .setLevel((TextView) mViewDetails.findViewById(R.id.userLevel))
                .setProgress((ProgressBar) mViewDetails.findViewById(R.id.userProgress))
                .setAvatar((ImageView) mViewDetails.findViewById(R.id.userImage));

        Log.v(TAG, "setUser finished");
    }

    private void setShareOptions() {
        ImageView shareIcon = (ImageView) mViewDetails.findViewById(R.id.shareView);
        TextView shareText = (TextView) mViewDetails.findViewById(R.id.shareText);
        OnClickListener shareListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, mIssue.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, "See this issue: " + mIssue.getTitle() + '\n'
                        + ServiceGenerator.BASE_WEB_URL + "/issues/" + mIssue.getIssueAuthToken());
                getContext().startActivity(Intent.createChooser(intent, "Share"));
            }
        };
        shareIcon.setOnClickListener(shareListener);
        shareText.setOnClickListener(shareListener);
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

    public SimpleCallback getDeleteCallback() {
        return new SimpleCallback() {
            @Override
            public void onSuccess() {
                CivifyMarkers markers = CivifyMap.getInstance().getMarkers();
                if (markers != null) markers.removeItem(mIssue.getIssueAuthToken());
                getActivity().onBackPressed();
                Log.d(TAG, "issue borrada");
            }

            @Override
            public void onFailure() {
                Snackbar.make(mViewDetails, R.string.couldnt_delete_issue, Snackbar.LENGTH_SHORT)
                        .show();
            }
        };
    }

    private void setupButtons() {
        LinearLayout buttons = (LinearLayout) mViewDetails.findViewById(R.id.buttonsLayout);

        if (isTooFarFromIssue()) {
            buttons.setVisibility(View.GONE);
            TextView resolvedOrFar = (TextView) mViewDetails
                    .findViewById(R.id.too_far_message);
            resolvedOrFar.setText(R.string.too_far_from_issue);
            resolvedOrFar.setVisibility(View.VISIBLE);
        } else {
            setupConfirmButton();
            setupReportButton();
            setupResolveButton();
        }
    }

    private boolean isTooFarFromIssue() {
        return mDistance == null || mDistance > MIN_METERS_FROM_ISSUE;
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
        if (mIssue.isResolved()) {
            Snackbar.make(getView(), R.string.cant_edit_resolved, Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            Intent intent = new Intent(getActivity().getApplicationContext(),
                    EditIssueActivity.class);
            Bundle data = new Bundle();
            data.putSerializable(TAG_ISSUE, mIssue);
            intent.putExtra(TAG_ISSUE, data);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle editedBundle = data.getBundleExtra(TAG_ISSUE);
                    Issue issue = (Issue) editedBundle.getSerializable(TAG_ISSUE);
                    setIssue(issue);
                    CivifyMarkers markers = CivifyMap.getInstance().getMarkers();
                    IssueMarker marker = markers.get(mIssue.getIssueAuthToken());
                    if (marker != null) marker.setIssue(mIssue);
                }
                break;

            default:
                break;
        }
    }
}
