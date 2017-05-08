package com.civify.activity.issue;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.civify.adapter.issue.IssueAdapter.CONFIRMED_BY_USER_WITH_AUTH_TOKEN;
import static com.civify.adapter.issue.IssueAdapter.ISSUE_WITH_AUTH_TOKEN;
import static com.civify.adapter.issue.IssueAdapter.REPORTED_BY_USER_WITH_AUTH_TOKEN;
import static com.civify.adapter.issue.IssueAdapter.RESOLUTION_ADDED;
import static com.civify.adapter.issue.IssueAdapter.RESOLUTION_DELETED;
import static com.civify.adapter.issue.IssueAdapter.UN;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.User;
import com.civify.model.issue.Category;
import com.civify.model.issue.Issue;
import com.civify.model.issue.Picture;
import com.civify.model.map.CivifyMap;
import com.civify.service.issue.IssueService;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ServiceGenerator;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(AndroidJUnit4.class) public class IssueDetailsTest {

    private static final double MOCK_LATITUDE = 41.678307;
    private static final double MOCK_LONGITUDE = 2.7796739;
    private static final String MOCK_IMAGE_URL =
            "/ca/documentacio-i-materials/fotos/fotografies/campus-nord/campus-nord-1";
    private static final String MOCK_BASE_URL = "https://daec.camins.upc.edu";
    private static final String USER_AUTH_TOKEN = "user-auth-token";
    private static final String ISSUE_AUTH_TOKEN = "issue-auth-token";
    private static MockWebServer mMockWebServer;

    private static void grantPermission(String permission) {
        // In M+, trying to do some actions will trigger a runtime dialog. Make sure
        // the permission is granted before running this test.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation()
                    .executeShellCommand(
                            "pm grant " + getTargetContext().getPackageName() + ' ' + permission);
        }
    }

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule =
            new ActivityTestRule<>(DrawerActivity.class);

    @Before
    public void setUp() throws ParseException {
        mMockWebServer = new MockWebServer();
        try {
            mMockWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mMockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        IssueService issueService = retrofit.create(IssueService.class);
        IssueAdapter issueAdapter =
                AdapterFactory.getInstance().getIssueAdapter(getTargetContext());
        issueAdapter.setService(issueService);
    }

    @AfterClass
    public static void tearDown() {
        Context context = getInstrumentation().getTargetContext();
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(context);
        loginAdapter.logout();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws InterruptedException {
        grantPermission("android.permission.ACCESS_FINE_LOCATION");

        Context context = getInstrumentation().getTargetContext();
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(context);
        loginAdapter.logout();
        final boolean[] ended = { false };
        loginAdapter.login("TestUser001", "Test1234", new LoginFinishedCallback() {
            @Override public void onLoginSucceeded(User u) {
                ended[0] = true;
            }

            @Override public void onLoginFailed(LoginError t) {
                ended[0] = true;
            }
        });

        while (!ended[0]) {
            sleep(200);
        }
    }

    @Test
    public void componentsAreShownCorrectly() throws ParseException, InterruptedException {
        Location mockLocation =
                LocationAdapter.getLocation(new LatLng(MOCK_LATITUDE, MOCK_LONGITUDE));
        CivifyMap.getInstance().setMockLocation(mockLocation);
        Issue mockIssue = mockIssue(USER_AUTH_TOKEN);
        mockIssue.showIssueDetails();

        sleep(2250);

        onView(withId(R.id.details_scrollview)).check(matches(isDisplayed()));

        onView(withId(R.id.eventView)).check(matches(isDisplayed()));

        onView(withId(R.id.nameText)).check(matches(isDisplayed()));
        onView(withId(R.id.nameText)).check(matches(withText(mockIssue.getTitle())));
        onView(withId(R.id.likesText)).check(matches(isDisplayed()));
        onView(withId(R.id.likesText)).check(matches(withText("+" + mockIssue.getConfirmVotes())));
        onView(withId(R.id.shareView)).check(matches(isDisplayed()));
        onView(withId(R.id.shareText)).check(matches(isDisplayed()));
        onView(withId(R.id.shareText)).check(matches(withText("share")));

        onView(withId(R.id.categoryText)).check(matches(isDisplayed()));
        onView(withId(R.id.categoryView)).check(matches(isDisplayed()));
        //onView(withId(R.id.nameCategoryText)).check(matches(withText(
        //        InstrumentationRegistry.getContext().getResources().getString(mockIssue
        //        .getCategory().getName()))));

        onView(withId(R.id.riskQuestion)).check(matches(isDisplayed()));
        onView(withId(R.id.riskAnswer)).check(matches(isDisplayed()));
        onView(withId(R.id.riskAnswer)).check(matches(withText(mockIssue.isRisk()? "Yes" : "No")));

        onView(withId(R.id.descriptionText)).check(matches(isDisplayed()));
        onView(withId(R.id.descriptionText)).check(matches(withText(mockIssue.getDescription())));

        onView(withId(R.id.streetText)).check(matches(isDisplayed()));
        //String street
        //onView(withId(R.id.streetText)).check(matches(withText(street)));
        onView(withId(R.id.sinceText)).check(matches(isDisplayed()));
        //String since
        //onView(withId(R.id.sinceText)).check(matches(withText(street)));
        onView(withId(R.id.distanceText)).check(matches(isDisplayed()));
        //String distance
        //onView(withId(R.id.distanceText)).check(matches(withText(street)));

        onView(withId(R.id.confirmButton)).check(matches(isDisplayed()));
        onView(withId(R.id.resolveButton)).check(matches(isDisplayed()));
        onView(withId(R.id.reportButton)).check(matches(isDisplayed()));

        onView(withId(R.id.details_scrollview)).perform(swipeUp());

        onView(withId(R.id.userImage)).check(matches(isDisplayed()));
        onView(withId(R.id.userName)).check(matches(isDisplayed()));
        //onView(withId(R.id.userUsername)).check(matches(isDisplayed()));
        //onView(withId(R.id.distanceText)).check(matches(withText(street)));
        onView(withId(R.id.userProgress)).check(matches(isDisplayed()));
        onView(withId(R.id.userLevel)).check(matches(isDisplayed()));
        //onView(withId(R.id.distanceText)).check(matches(withText(street)));
    }

    @Test
    public void testConfirmResolveReport() throws ParseException, InterruptedException {
        Location mockLocation =
                LocationAdapter.getLocation(new LatLng(MOCK_LATITUDE, MOCK_LONGITUDE));
        CivifyMap.getInstance().setMockLocation(mockLocation);
        mockIssue(USER_AUTH_TOKEN).showIssueDetails();

        sleep(2250);

        String userAuthToken = UserAdapter.getCurrentUser().getUserAuthToken();

        onView(withId(R.id.details_scrollview)).perform(swipeUp());

        // Confirm issue
        mockResponse(ISSUE_WITH_AUTH_TOKEN + ISSUE_AUTH_TOKEN + ' ' +
                CONFIRMED_BY_USER_WITH_AUTH_TOKEN + userAuthToken);
        onView(withId(R.id.confirmButton)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.confirm_message))))
                .check(matches(isDisplayed()));

        // Resolve issue
        mockResponse(RESOLUTION_ADDED);
        onView(withId(R.id.resolveButton)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.resolve_message))))
                .check(matches(isDisplayed()));

        // Report issue
        mockResponse(ISSUE_WITH_AUTH_TOKEN + ISSUE_AUTH_TOKEN + ' ' +
                REPORTED_BY_USER_WITH_AUTH_TOKEN + userAuthToken);
        onView(withId(R.id.reportButton)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.report_message))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testUnconfirmUnresolveUnreport() throws ParseException, InterruptedException {
        Location mockLocation =
                LocationAdapter.getLocation(new LatLng(MOCK_LATITUDE, MOCK_LONGITUDE));
        CivifyMap.getInstance().setMockLocation(mockLocation);
        Issue mockIssue = mockIssue(USER_AUTH_TOKEN);
        mockIssue.setConfirmedByAuthUser(true);
        mockIssue.setResolvedByAuthUser(true);
        mockIssue.setReportedByAuthUser(true);
        mockIssue.showIssueDetails();

        String userAuthToken = UserAdapter.getCurrentUser().getUserAuthToken();

        sleep(2250);

        onView(withId(R.id.details_scrollview)).perform(swipeUp());

        // Unconfirm issue
        mockResponse(ISSUE_WITH_AUTH_TOKEN + ISSUE_AUTH_TOKEN + UN +
                CONFIRMED_BY_USER_WITH_AUTH_TOKEN + userAuthToken);
        onView(withId(R.id.confirmButton)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.unconfirm_message))))
                .check(matches(isDisplayed()));

        // Unresolve issue
        mockResponse(RESOLUTION_DELETED);
        onView(withId(R.id.resolveButton)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.unresolve_message))))
                .check(matches(isDisplayed()));

        // Unreport issue
        mockResponse(ISSUE_WITH_AUTH_TOKEN + ISSUE_AUTH_TOKEN + UN +
                REPORTED_BY_USER_WITH_AUTH_TOKEN + userAuthToken);
        onView(withId(R.id.reportButton)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.unreport_message))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testYourOwnIssue() throws ParseException, InterruptedException {
        String userAuthToken = UserAdapter.getCurrentUser().getUserAuthToken();
        Location mockLocation =
                LocationAdapter.getLocation(new LatLng(MOCK_LATITUDE, MOCK_LONGITUDE));
        CivifyMap.getInstance().setMockLocation(mockLocation);
        mockIssue(userAuthToken).showIssueDetails();

        sleep(2250);

        onView(withId(R.id.details_scrollview)).perform(swipeUp());

        // Confirm your own issue (should show error message)

        onView(withId(R.id.confirmButton)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.cant_confirm_own_issue))))
                .check(matches(isDisplayed()));

        // Resolve your own issue (should show ok message)
        mockResponse(RESOLUTION_ADDED);

        onView(withId(R.id.resolveButton)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.resolve_message))))
                .check(matches(isDisplayed()));

        mockResponse(IssueAdapter.RESOLUTION_DELETED);

        onView(withId(R.id.resolveButton)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.unresolve_message))))
                .check(matches(isDisplayed()));

        // Report your own issue (should show error message)

        onView(allOf(withId(R.id.reportButton), isDisplayed())).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.cant_report_own_issue))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testTooFarFromIssue() throws ParseException, InterruptedException {
        Location mockLocation =
                LocationAdapter.getLocation(new LatLng(MOCK_LATITUDE - 1.0, MOCK_LONGITUDE - 1.0));
        CivifyMap.getInstance().setMockLocation(mockLocation);
        mockIssue(USER_AUTH_TOKEN).showIssueDetails();

        sleep(2250);

        onView(withId(R.id.details_scrollview)).perform(swipeUp());

        onView(allOf(withId(R.id.too_far_message), isDisplayed()));
    }

    @Test
    public void testErrorMessages() throws ParseException, InterruptedException {
        Location mockLocation =
                LocationAdapter.getLocation(new LatLng(MOCK_LATITUDE, MOCK_LONGITUDE));
        CivifyMap.getInstance().setMockLocation(mockLocation);
        mockIssue(USER_AUTH_TOKEN).showIssueDetails();

        sleep(2250);

        onView(withId(R.id.details_scrollview)).perform(swipeUp());

        // Error confirming issue
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST));

        onView(allOf(withId(R.id.confirmButton), isDisplayed())).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.confirm_error_message))))
                .check(matches(isDisplayed()));

        // Error resolving
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST));

        onView(allOf(withId(R.id.resolveButton), isDisplayed())).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.resolve_error_message))))
                .check(matches(isDisplayed()));

        // Report your own issue (should show error message)
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST));

        onView(allOf(withId(R.id.reportButton), isDisplayed())).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(getTargetContext().getString(R.string.report_error_message))))
                .check(matches(isDisplayed()));
    }

    private Issue mockIssue(String userAuthToken) throws ParseException {
        String dateString = "2016-12-21T20:08:11.000Z";
        DateFormat dateFormat =
                new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT, Locale.getDefault());
        Date date = dateFormat.parse(dateString);

        Picture picture =
                new Picture("picture-file-name", "picture-content-type", "picture-content");
        picture.setBaseUrl(MOCK_BASE_URL);
        picture.setSmallUrl(MOCK_IMAGE_URL);
        picture.setMedUrl(MOCK_IMAGE_URL);
        picture.setLargeUrl(MOCK_IMAGE_URL);

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
        mMockWebServer.enqueue(mockResponse);

        return new Issue("issue-title", "issue-description", Category.ROAD_SIGNS, true,
                MOCK_LONGITUDE, MOCK_LATITUDE, 0, 0, 0, date, date, ISSUE_AUTH_TOKEN,
                userAuthToken, picture);
    }

    private void mockResponse(String expMessage) {
        JsonObject body = new JsonObject();
        body.addProperty("message", expMessage);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
    }
}
