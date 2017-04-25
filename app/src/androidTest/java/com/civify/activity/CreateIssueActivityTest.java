package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
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
import static org.hamcrest.core.IsAnything.anything;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.civify.R;
import com.civify.activity.createissue.CreateIssueActivity;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.User;
import com.civify.model.issue.Category;
import com.civify.model.issue.Issue;
import com.civify.model.issue.Picture;
import com.civify.service.issue.IssueService;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

@LargeTest @RunWith(AndroidJUnit4.class) public class CreateIssueActivityTest {

    private static void grantPermission(String permission) {
        // In M+, trying to call a number will trigger a runtime dialog. Make sure
        // the permission is granted before running this test.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + ' ' + permission);
        }
    }

    @Rule
    public IntentsTestRule<CreateIssueActivity> intentsRule =
            new IntentsTestRule<>(CreateIssueActivity.class);

    @Before
    public void setUp() {
        Context context = getInstrumentation().getTargetContext();
        SharedPreferences userPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(userPreferences);
        loginAdapter.logout();
        loginAdapter.login("ArnauBlanch2", "Test1234", new LoginFinishedCallback() {
            @Override public void onLoginSucceeded(User u) {

            }

            @Override public void onLoginFailed(LoginError t) {

            }
        });

        setupMockIssueResponse();


    }

    @BeforeClass
    public static void setUpPermissions() {
        grantPermission("android.permission.ACCESS_FINE_LOCATION");
        grantPermission("android.permission.CAMERA");
        grantPermission("android.permission.READ_EXTERNAL_STORAGE");
        grantPermission("android.permission.WRITE_EXTERNAL_STORAGE");
    }

    private void setupMockIssueResponse() {
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
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        IssueService issueService = retrofit.create(IssueService.class);
        IssueAdapter issueAdapter =
                AdapterFactory.getInstance().getIssueAdapter(getTargetContext());
        issueAdapter.setService(issueService);

        String jsonBody;
        try {
            jsonBody = gson.toJson(mockIssue());
            MockResponse mockResponse = new MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_CREATED)
                    .setBody(jsonBody);
            mockWebServer.enqueue(mockResponse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test public void testCreateIssue() {

        ViewInteraction appCompatEditText5 = onView(allOf(withId(R.id.title_input), isDisplayed()));
        appCompatEditText5.perform(replaceText("IssueTitle"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 =
                onView(allOf(withId(R.id.button0), isDisplayed()));
        appCompatButton3.perform(click());

        onView(withId(R.id.category_spinner)).perform(click());

        onData(anything()).inAdapterView(withClassName(equalTo(
                "android.widget.DropDownListView"))).atPosition(0)
                .perform
                (click
                ());

        ViewInteraction appCompatButton4 =
                onView(allOf(withId(R.id.button0), isDisplayed()));
        appCompatButton4.perform(click());

        onView(allOf(withId(R.id.camera_button), isDisplayed())).perform(click());
        
        takeCameraPhoto();

        ViewInteraction appCompatButton7 =
                onView(allOf(withId(R.id.button0), isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatImageButton4 = onView(allOf(withContentDescription("Navigate up"),
                withParent(allOf(withId(R.id.create_issue_toolbar),
                        withParent(withId(R.id.create_issue_linearlayout)))), isDisplayed()));
        appCompatImageButton4.perform(click());

        try {
            pickGalleryPhoto();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton5 =
                onView(allOf(withId(R.id.gallery_button), isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction appCompatButton8 =
                onView(allOf(withId(R.id.button0), isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatRadioButton = onView(allOf(withId(R.id.radio_yes),
                withParent(withId(R.id.radio_group)), isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction appCompatButton9 =
                onView(allOf(withId(R.id.button0), isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction appCompatEditText6 =
                onView(allOf(withId(R.id.description_input), isDisplayed()));
        appCompatEditText6.perform(replaceText("Test description."), closeSoftKeyboard());

        ViewInteraction appCompatButton10 =
                onView(allOf(withId(R.id.button0), isDisplayed()));
        appCompatButton10.perform(click());

        onView(withText(R.string.creating_new_issue))
                .inRoot(isDialog()).check(matches(isDisplayed()));
    }

    private void takeCameraPhoto() {

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiSelector shutterSelector =
                new UiSelector().resourceId("com.android.camera:id/shutter_button");
        UiObject shutterButton = device.findObject(shutterSelector);
        try {
            shutterButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        UiSelector doneSelector = new UiSelector().resourceId("com.android.camera:id/btn_done");
        UiObject doneButton = device.findObject(doneSelector);
        try {
            doneButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void pickGalleryPhoto() throws IOException {

        Drawable drawable = getTargetContext().getResources().getDrawable(R.drawable.civify);
        Bitmap bmp = drawableToBitmap(drawable);

        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
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

    private static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private static Issue mockIssue() throws ParseException {
        String dateString = "2016-12-21T20:08:11.000Z";
        DateFormat dateFormat = new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT, Locale
                .getDefault());
        Date date = dateFormat.parse(dateString);

        Picture picture = new Picture("picture-file-name", "picture-content-type",
                "picture-content");
        Issue issue = new Issue("issue-title", "issue-description", Category.ROAD_SIGNS, true,
                45.0f, 46.0f, 0, 0, 0, date, date, "issue-auth-token", "user-auth-token",
                picture);
        return issue;
    }
}
