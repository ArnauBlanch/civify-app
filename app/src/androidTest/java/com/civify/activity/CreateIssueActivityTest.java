package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.EditText;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.createissue.CreateIssueActivity;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.User;
import com.civify.model.issue.Category;
import com.civify.model.issue.Issue;
import com.civify.model.Picture;
import com.civify.model.map.CivifyMap;
import com.civify.service.issue.IssueService;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ServiceGenerator;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
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

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateIssueActivityTest {

    public static final String DROPDOWN_LISTVIEW = "android.widget.DropDownListView";
    public static final String NAVIGATE_UP = "Navigate up";

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
    public IntentsTestRule<DrawerActivity> intentsRule =
            new IntentsTestRule<>(DrawerActivity.class);

    @BeforeClass
    public static void setUpBeforeClass() throws InterruptedException {
        Context context = getInstrumentation().getTargetContext();
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(context);
        loginAdapter.logout();
        loginAdapter.login("TestUser001", "Test1234", new LoginFinishedCallback() {
            @Override
            public void onLoginSucceeded(User u) {
            }

            @Override
            public void onLoginFailed(LoginError t) {
            }
        });

        sleep(5000);
    }

    @Before
    public void setUp() {
        intentsRule.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                CivifyMap.setContext(intentsRule.getActivity());
                Location mockLocation = LocationAdapter.getLocation(new LatLng(0.0, 0.0));
                //CivifyMap.getInstance().setMockLocation(mockLocation);
            }
        });
        Intent intent = new Intent(intentsRule.getActivity().getApplicationContext(),
                CreateIssueActivity.class);
        intentsRule.getActivity().startActivity(intent);

        setupMockIssueResponse();
    }

    @After
    public void tearDown() {
        Context context = getInstrumentation().getTargetContext();
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(context);
        loginAdapter.logout();
    }

    @BeforeClass
    public static void setUpPermissions() throws InterruptedException {
        grantPermission("android.permission.ACCESS_FINE_LOCATION");
        grantPermission("android.permission.CAMERA");
        grantPermission("android.permission.READ_EXTERNAL_STORAGE");
        grantPermission("android.permission.WRITE_EXTERNAL_STORAGE");

        Context context = getInstrumentation().getTargetContext();
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(context);
        loginAdapter.logout();
        final boolean[] ended = { false };
        loginAdapter.login("TestUser001", "Test1234", new LoginFinishedCallback() {
            @Override
            public void onLoginSucceeded(User u) {
                ended[0] = true;
            }

            @Override
            public void onLoginFailed(LoginError t) {
                ended[0] = true;
            }
        });

        while (!ended[0]) {
            sleep(200);
        }
    }

    private static void setupMockIssueResponse() {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            mockWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        IssueService issueService = retrofit.create(IssueService.class);
        IssueAdapter issueAdapter =
                AdapterFactory.getInstance().getIssueAdapter(getTargetContext());
        issueAdapter.setService(issueService);

        String jsonBody;
        try {
            jsonBody = gson.toJson(mockIssue());
            MockResponse mockResponse =
                    new MockResponse().setResponseCode(HttpURLConnection.HTTP_CREATED)
                            .setBody(jsonBody);
            mockWebServer.enqueue(mockResponse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateIssue() throws IOException, UiObjectNotFoundException {
        // Title
        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        onView(allOf(isDescendantOfA(withId(R.id.title_input_layout)),
                not(isAssignableFrom(EditText.class)), isAssignableFrom(TextView.class))).check(
                matches(withText(R.string.must_insert_issue_title)));

        onView(allOf(withId(R.id.title_input), isDisplayed())).perform(replaceText("IssueTitle"),
                closeSoftKeyboard());

        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        // Category
        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.category_validation), withText(R.string.must_choose_category),
                isDisplayed()));

        onView(withId(R.id.category_spinner)).perform(click());

        onData(anything()).inAdapterView(withClassName(equalTo(DROPDOWN_LISTVIEW)))
                .atPosition(0)
                .perform(click());

        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        // Photo
        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.photo_validation), withText(R.string.must_choose_photo),
                isDisplayed()));

        onView(allOf(withId(R.id.camera_button), isDisplayed())).perform(click());

        takeCameraPhoto();

        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        onView(allOf(withContentDescription(NAVIGATE_UP), withParent(
                allOf(withId(R.id.create_issue_toolbar),
                        withParent(withId(R.id.create_issue_linearlayout)))),
                isDisplayed())).perform(click());

        pickGalleryPhoto();

        onView(allOf(withId(R.id.gallery_button), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        // Risk
        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.risk_validation), withText(R.string.must_choose_option),
                isDisplayed()));

        onView(allOf(withId(R.id.radio_yes), withParent(withId(R.id.radio_group)),
                isDisplayed())).perform(click());

        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        // Description
        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        onView(allOf(isDescendantOfA(withId(R.id.description_layout)),
                not(isAssignableFrom(EditText.class)), isAssignableFrom(TextView.class))).check(
                matches(withText(R.string.must_insert_description)));

        onView(allOf(withId(R.id.description_input), isDisplayed())).perform(
                replaceText("Test description."), closeSoftKeyboard());

        onView(allOf(withId(R.id.button0), isDisplayed())).perform(click());

        // Rewards Dialog
        onView(allOf(withId(android.R.id.button1), isDisplayed())).perform(click());
    }

    private static void takeCameraPhoto() throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiSelector shutterSelector =
                new UiSelector().resourceId("com.android.camera:id/shutter_button");
        UiObject shutterButton = device.findObject(shutterSelector);
        shutterButton.click();

        UiSelector doneSelector = new UiSelector().resourceId("com.android.camera:id/btn_done");
        UiObject doneButton = device.findObject(doneSelector);
        doneButton.click();
    }

    private static void pickGalleryPhoto() throws IOException {

        Drawable drawable = getTargetContext().getResources().getDrawable(R.drawable.upc_library);
        Bitmap bmp = drawableToBitmap(drawable);

        File storageDir =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        "Camera");
        File image = File.createTempFile("testImage", ".jpg", storageDir);

        FileOutputStream out = new FileOutputStream(image);
        bmp.compress(CompressFormat.JPEG, 100, out);

        // Build a result to return from the Camera app
        Intent resultData = new Intent();
        resultData.setData(Uri.fromFile(image));
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
        intending(not(isInternal())).respondWith(result);
        intended(not(isInternal()));
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap =
                Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private static Issue mockIssue() throws ParseException {
        String dateString = "2016-12-21T20:08:11.000Z";
        DateFormat dateFormat =
                new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT, Locale.getDefault());
        Date date = dateFormat.parse(dateString);

        Picture picture =
                new Picture("picture-file-name", "picture-content-type", "picture-content");
        Issue issue =
                new Issue("issue-title", "issue-description", Category.ROAD_SIGNS, true, 45.0f,
                        46.0f, 0, 0, 0, date, date, "issue-auth-token", "user-auth-token", picture);
        return issue;
    }
}
