package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.civify.R;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DrawerActivityTest {

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
    }

    @AfterClass
    public static void tearDown() {
        Context context = getInstrumentation().getTargetContext();
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(context);
        loginAdapter.logout();
    }

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>(
            DrawerActivity.class);

    private void openDrawerAndClickItem(String text) {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation profile"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.bar_layout)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(R.id.design_menu_item_text), withText(text), isDisplayed()));
        appCompatCheckedTextView.perform(click());
    }

    @Test
    public void navigateMenuTest() {
        openDrawerAndClickItem("Navigate");
        assertEquals(0, mActivityTestRule.getActivity().getCurrentFragmentId());
    }

    @Test
    public void wallMenuTest() {
        openDrawerAndClickItem("Wall");
        assertEquals(1, mActivityTestRule.getActivity().getCurrentFragmentId());
    }

    @Test
    public void profileMenuTest() {
        openDrawerAndClickItem("Profile");
        assertEquals(2, mActivityTestRule.getActivity().getCurrentFragmentId());
    }

    @Test
    public void rewardsMenuTest() {
        openDrawerAndClickItem("Rewards");
        assertEquals(3, mActivityTestRule.getActivity().getCurrentFragmentId());
    }

    @Test
    public void achievementsMenuTest() {
        openDrawerAndClickItem("Achievements");
        assertEquals(4, mActivityTestRule.getActivity().getCurrentFragmentId());
    }

    @Test
    public void eventsMenuTest() {
        openDrawerAndClickItem("Events");
        assertEquals(5, mActivityTestRule.getActivity().getCurrentFragmentId());
    }

    @Test
    public void settingsMenuTest() {
        openDrawerAndClickItem("Settings");
        assertEquals(6, mActivityTestRule.getActivity().getCurrentFragmentId());
    }
}
