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
import static org.hamcrest.Matchers.allOf;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.civify.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void profileFragmentTest() {

        ViewInteraction appCompatButton =
                onView(allOf(withId(R.id.signInButton), withText("Sign in"), withParent(
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

        ViewInteraction appCompatImageButton =
                onView(allOf(withContentDescription("Open navigation drawer"), withParent(
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
