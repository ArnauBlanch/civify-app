package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
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

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WallFragmentTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule =
            new ActivityTestRule<>(DrawerActivity.class);

    @BeforeClass
    public static void setUpPermissions() throws InterruptedException {

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

        grantPermission("android.permission.ACCESS_FINE_LOCATION");

    }

    private static void grantPermission(String permission) {
        // In M+, trying to do some actions will trigger a runtime dialog. Make sure
        // the permission is granted before running this test.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + ' ' + permission);
        }
    }

    @Test
    public void wallFragmentTest() {

        //try {
        //    Thread.sleep(5000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}

        //ViewInteraction appCompatButton =
        //        onView(allOf(withId(R.id.signInButton), withText("Sign in"), withParent(
        //                allOf(withId(R.id.buttonsLayout), withParent(withId(R.id.mainLayout)))),
        //                isDisplayed()));
        //appCompatButton.perform(click());
        //
        //ViewInteraction appCompatEditText =
        //        onView(allOf(withId(R.id.login_email_input), isDisplayed()));
        //appCompatEditText.perform(click());
        //
        //ViewInteraction appCompatEditText2 =
        //        onView(allOf(withId(R.id.login_email_input), isDisplayed()));
        //appCompatEditText2.perform(click());
        //
        //ViewInteraction appCompatEditText3 =
        //        onView(allOf(withId(R.id.login_email_input), isDisplayed()));
        //appCompatEditText3.perform(replaceText("dsegoviat"), closeSoftKeyboard());
        //
        //ViewInteraction appCompatEditText4 =
        //        onView(allOf(withId(R.id.login_password_input), isDisplayed()));
        //appCompatEditText4.perform(replaceText("Test1234"), closeSoftKeyboard());
        //
        //ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.bsignin), withText("Sign in"),
        //        withParent(allOf(withId(R.id.intro_background),
        //                withParent(withId(android.R.id.content)))), isDisplayed()));
        //appCompatButton2.perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton =
                onView(allOf(withContentDescription("Open navigation profile"), withParent(
                        allOf(withId(R.id.toolbar), withParent(withId(R.id.bar_layout)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());



        ViewInteraction appCompatCheckedTextView =
                onView(allOf(withId(R.id.design_menu_item_text), withText("Wall"), isDisplayed()));
        appCompatCheckedTextView.perform(click());

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

    }

    @After
    public void tearDown() {
        Context context = getInstrumentation().getTargetContext();
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(context);
        loginAdapter.logout();
    }
}
