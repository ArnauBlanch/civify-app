package com.civify.civify.activity;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.civify.civify.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//@LargeTest
@RunWith(AndroidJUnit4.class)
public class DrawerActivityTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>(
            DrawerActivity.class);

    private void openDrawerAndClickItem(String text) {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
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
        assertEquals(0, mActivityTestRule.getActivity().getCurrentFragment());
    }

    @Test
    public void wallMenuTest() {
        openDrawerAndClickItem("Wall");
        assertEquals(1, mActivityTestRule.getActivity().getCurrentFragment());
    }

    @Test
    public void profileMenuTest() {
        openDrawerAndClickItem("Profile");
        assertEquals(2, mActivityTestRule.getActivity().getCurrentFragment());
    }

    @Test
    public void rewardsMenuTest() {
        openDrawerAndClickItem("Rewards");
        assertEquals(3, mActivityTestRule.getActivity().getCurrentFragment());
    }

    @Test
    public void achievementsMenuTest() {
        openDrawerAndClickItem("Achievements");
        assertEquals(4, mActivityTestRule.getActivity().getCurrentFragment());
    }

    @Test
    public void eventsMenuTest() {
        openDrawerAndClickItem("Events");
        assertEquals(5, mActivityTestRule.getActivity().getCurrentFragment());
    }

    @Test
    public void settingsMenuTest() {
        openDrawerAndClickItem("Settings");
        assertEquals(6, mActivityTestRule.getActivity().getCurrentFragment());
    }


}
