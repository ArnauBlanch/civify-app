package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.os.Build;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.civify.R;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.User;
import com.civify.model.award.Award;
import com.civify.model.Picture;
import com.civify.service.issue.IssueService;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@LargeTest @RunWith(AndroidJUnit4.class)
public class RewardsFragmentTest {

    private static MockWebServer sMockWebServer;
    private static final String MOCK_IMAGE_URL =
            "/ca/documentacio-i-materials/fotos/fotografies/campus-nord/campus-nord-1";
    private static final String MOCK_BASE_URL = "https://daec.camins.upc.edu";

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
        sMockWebServer = new MockWebServer();
        try {
            sMockWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(sMockWebServer.url("").toString())
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

    @BeforeClass public static void setUpBeforeClass() throws InterruptedException {
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
    public void testAvailableRewards() throws ParseException {
        ViewInteraction appCompatImageButton =
                onView(allOf(withContentDescription("Open navigation profile"), withParent(
                        allOf(withId(R.id.toolbar), withParent(withId(R.id.bar_layout)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView =
                onView(allOf(withId(R.id.design_menu_item_text), withText("Rewards"),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        pressBack();
    }

    private Award mockAward() throws ParseException {
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
        sMockWebServer.enqueue(mockResponse);

        return new Award("award-title", "award-description", 35, date, date, "award-auth-token",
                "commerce-name", picture);
    }

}
