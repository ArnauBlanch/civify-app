package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NavigateFragmentTest {

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

    @Before
    public void setUp() {
        Context context = getInstrumentation().getTargetContext();
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(context);
        loginAdapter.logout();
        loginAdapter.login("ArnauBlanch2", "Test1234", new LoginFinishedCallback() {
            @Override
            public void onLoginSucceeded(User u) {}
            @Override
            public void onLoginFailed(LoginError t) {}
        });
    }

    @BeforeClass
    public static void setUpPermissions() {
        grantPermission("android.permission.ACCESS_FINE_LOCATION");
    }

    @Test
    public void navigateFragmentTest() {
        ViewInteraction floatingActionButton =
                onView(allOf(withId(R.id.fab_location), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(allOf(withId(R.id.fab_add), isDisplayed()));
        floatingActionButton2.perform(click());

        pressBack();
    }

}
