package com.civify.activity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.civify.R;
import com.civify.utils.AdapterFactory;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        AdapterFactory.getInstance().getLoginAdapter(getInstrumentation().getTargetContext())
                .logout();
    }

    @Test
    public void mainActivityTest() {
        ViewInteraction imageView = onView(allOf(withId(R.id.titleView), childAtPosition(
                allOf(withId(R.id.mainLayout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 0), isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(allOf(withId(R.id.logoView), childAtPosition(
                allOf(withId(R.id.mainLayout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 1), isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction button = onView(allOf(withId(R.id.registerButton), childAtPosition(
                allOf(withId(R.id.buttonsLayout), childAtPosition(withId(R.id.mainLayout), 2)), 0),
                isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(allOf(withId(R.id.signInButton), childAtPosition(
                allOf(withId(R.id.buttonsLayout), childAtPosition(withId(R.id.mainLayout), 2)), 1),
                isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction linearLayout3 = onView(allOf(withId(R.id.buttonsLayout), childAtPosition(
                allOf(withId(R.id.mainLayout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 2), isDisplayed()));
        linearLayout3.check(matches(isDisplayed()));

        ViewInteraction linearLayout4 = onView(allOf(withId(R.id.mainLayout),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 0),
                isDisplayed()));
        linearLayout4.check(matches(isDisplayed()));

        ViewInteraction appCompatButton =
                onView(allOf(withId(R.id.registerButton), withText("Register"), withParent(
                        allOf(withId(R.id.buttonsLayout), withParent(withId(R.id.mainLayout)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView2 = onView(allOf(withId(R.id.title0), withText("What's your name?"),
                childAtPosition(withParent(withId(R.id.viewpager)), 0), isDisplayed()));
        textView2.check(matches(withText("What's your name?")));

        pressBack();

        ViewInteraction appCompatButton2 =
                onView(allOf(withId(R.id.signInButton), withText("Sign in"), withParent(
                        allOf(withId(R.id.buttonsLayout), withParent(withId(R.id.mainLayout)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        pressBack();

        ViewInteraction appCompatButton7 =
                onView(allOf(withId(android.R.id.button1), withText("Close")));
        appCompatButton7.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher,
            final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(
                        ((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
