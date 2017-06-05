package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
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
import android.content.Intent;
import android.location.Location;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.civify.R;
import com.civify.activity.createissue.CreateIssueActivity;
import com.civify.adapter.LocationAdapter;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.model.User;
import com.civify.model.map.CivifyMap;
import com.civify.utils.AdapterFactory;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule =
            new ActivityTestRule<>(DrawerActivity.class);

    @BeforeClass
    public static void setUp() throws InterruptedException {
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
    public void profileFragmentTest() {

        ViewInteraction appCompatImageButton =
                onView(allOf(withContentDescription("Open navigation profile"), withParent(
                        allOf(withId(R.id.toolbar), withParent(withId(R.id.bar_layout)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView =
                onView(allOf(withId(R.id.design_menu_item_text), withText("Profile"),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatTextView =
                onView(allOf(withId(android.R.id.title), withText("BADGES"), isDisplayed()));
        appCompatTextView.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 =
                onView(allOf(withId(R.id.title), withText("Sign out"), isDisplayed()));
        appCompatTextView2.perform(click());
    }
}
