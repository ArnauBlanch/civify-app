package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.core.Is.is;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.civify.R;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.model.User;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.MapNotReadyException;
import com.civify.utils.AdapterFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NavigateFragmentTest {

    private static final String TAG = NavigateFragmentTest.class.getSimpleName();

    private CountDownLatch mLatch;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private static void grantPermission(String permission) {
        // In M+, trying to do some actions will trigger a runtime dialog. Make sure
        // the permission is granted before running this test.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + ' ' + permission);
        }
    }

    @BeforeClass
    public static void setUpPermissions() {
        grantPermission("android.permission.ACCESS_FINE_LOCATION");
    }

    @Test
    public void testButtonsWhenMapIsNotReady() {
        signIn();
        CivifyMap.getInstance().disable();
        CivifyMap.getInstance().outdateToBeRefreshed();
        onView(allOf(withId(R.id.fab_location), isDisplayed())).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Map loading, please wait..."))).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.fab_add), isDisplayed())).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("Map loading, please wait..."))).check(matches(isDisplayed()));
    }

    @Test
    public void testButtonsWhenMapIsReady() {
        signIn();
        resetLatch();
        CivifyMap.getInstance().setOnMapReadyListener(notifyLatch());
        waitForLatch();

        // At this point map is ready
        moveTo(LocationAdapter.getLatLng(37.42, -122.084));

        onView(allOf(withId(R.id.fab_location), isDisplayed())).perform(click());
        waitFor(CivifyMap.CAMERA_ANIMATION_MILLIS);
        assertPosition(LocationAdapter.getLatLng(CivifyMap.getInstance().getCurrentLocation()));

        onView(allOf(withId(R.id.fab_add), isDisplayed())).perform(click());
        pressBack();
    }

    private void moveTo(@NonNull final LatLng fake) {
        runOnMain(new Runnable() {
            @Override
            public void run() {
                try {
                    CivifyMap.getInstance().center(LocationAdapter.getLocation(fake), false);
                } catch (MapNotReadyException e) {
                    Log.wtf(TAG, e);
                }
            }
        });
        assertPosition(fake);
    }

    private void assertPosition(@NonNull final LatLng expected) {
        runOnMain(new Runnable() {
            @Override
            public void run() {
                LatLng actual = CivifyMap.getInstance().getCurrentCameraPosition();
                assertThat(actual.latitude, is(closeTo(expected.latitude, 0.01)));
                assertThat(actual.longitude, is(closeTo(expected.longitude, 0.01)));
            }
        });
    }

    private void resetLatch() {
        mLatch = new CountDownLatch(1);
    }

    private void runOnMain(@NonNull final Runnable runnable) {
        getInstrumentation().runOnMainSync(runnable);
        getInstrumentation().waitForIdleSync();
    }

    private void waitForLatch() {
        try {
            mLatch.await();
        } catch (InterruptedException ignore) {}
    }

    private Runnable notifyLatch() {
        return new Runnable() {
            @Override
            public void run() {
                mLatch.countDown();
            }
        };
    }

    private void waitFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {}
    }

    // Sign In

    @Before
    public void setUp() {
        LoginAdapter loginAdapter =
                AdapterFactory.getInstance().getLoginAdapter(getTargetContext());
        loginAdapter.logout();
        loginAdapter.login("ArnauBlanch2", "Test1234", new LoginFinishedCallback() {
            @Override
            public void onLoginSucceeded(User u) {}
            @Override
            public void onLoginFailed(LoginError t) {}
        });
    }

    private void signIn() {
        ViewInteraction appCompatButton =
                onView(allOf(withId(R.id.signInButton), withParent(
                        allOf(withId(R.id.buttonsLayout), withParent(withId(R.id.mainLayout)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText =
                onView(allOf(withId(R.id.login_email_input), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 =
                onView(allOf(withId(R.id.login_email_input), isDisplayed()));
        appCompatEditText2.perform(replaceText("arnaublanch2"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 =
                onView(allOf(withId(R.id.login_password_input), isDisplayed()));
        appCompatEditText3.perform(replaceText("Test1234"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.bsignin), withText("Sign in"),
                withParent(allOf(withId(R.id.intro_background),
                        withParent(withId(android.R.id.content)))), isDisplayed()));
        appCompatButton2.perform(click());
    }
}
