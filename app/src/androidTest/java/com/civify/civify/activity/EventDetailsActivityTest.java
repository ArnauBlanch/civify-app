package com.civify.civify.activity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.civify.activity.events.EventDetailsActivity;
import com.civify.civify.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest @RunWith(AndroidJUnit4.class) public class EventDetailsActivityTest {

    @Rule public ActivityTestRule<EventDetailsActivity> mActivityTestRule =
            new ActivityTestRule<>(EventDetailsActivity.class);

    @Test public void eventDetailsActivityTest() {
        ViewInteraction textView =
                onView(allOf(withId(R.id.nameText), withText("Semafor trencat al carrer"),
                        childAtPosition(allOf(withId(R.id.nameLikesShareLayout),
                                childAtPosition(withId(R.id.mainLayout), 1)), 0), isDisplayed()));
        textView.check(matches(withText("Semafor trencat al carrer")));

        ViewInteraction imageView = onView(allOf(withId(R.id.shareView), childAtPosition(
                allOf(withId(R.id.shareLayout), childAtPosition(withId(R.id.likesShareLayout), 0)),
                0), isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(allOf(withId(R.id.shareText), withText("share"),
                childAtPosition(allOf(withId(R.id.shareLayout),
                        childAtPosition(withId(R.id.likesShareLayout), 0)), 1), isDisplayed()));
        textView2.check(matches(withText("share")));

        ViewInteraction linearLayout = onView(allOf(withId(R.id.shareLayout), childAtPosition(
                allOf(withId(R.id.likesShareLayout),
                        childAtPosition(withId(R.id.nameLikesShareLayout), 1)), 0), isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction relativeLayout = onView(allOf(withId(R.id.likesShareLayout),
                childAtPosition(allOf(withId(R.id.nameLikesShareLayout),
                        childAtPosition(withId(R.id.mainLayout), 1)), 1), isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

        ViewInteraction linearLayout2 = onView(allOf(withId(R.id.nameLikesShareLayout),
                childAtPosition(allOf(withId(R.id.mainLayout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 1), isDisplayed()));
        linearLayout2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(allOf(withId(R.id.categoryText), withText("Category:"),
                childAtPosition(allOf(withId(R.id.categoryLayout),
                        childAtPosition(withId(R.id.mainLayout), 2)), 0), isDisplayed()));
        textView3.check(matches(withText("Category:")));

        ViewInteraction imageView2 = onView(allOf(withId(R.id.categoryView), childAtPosition(
                allOf(withId(R.id.categoryLayout), childAtPosition(withId(R.id.mainLayout), 2)), 1),
                isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction textView4 =
                onView(allOf(withId(R.id.nameCategoryText), withText("Senyalització"),
                        childAtPosition(allOf(withId(R.id.categoryLayout),
                                childAtPosition(withId(R.id.mainLayout), 2)), 2), isDisplayed()));
        textView4.check(matches(withText("Senyalització")));

        ViewInteraction linearLayout3 = onView(allOf(withId(R.id.categoryLayout), childAtPosition(
                allOf(withId(R.id.mainLayout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 2), isDisplayed()));
        linearLayout3.check(matches(isDisplayed()));

        ViewInteraction textView5 =
                onView(allOf(withId(R.id.riskQuestion), withText("Poses a risk?"), childAtPosition(
                        allOf(withId(R.id.riskLayout), childAtPosition(withId(R.id.mainLayout), 3)),
                        0), isDisplayed()));
        textView5.check(matches(withText("Poses a risk?")));

        ViewInteraction textView6 = onView(allOf(withId(R.id.riskAnswer), withText("Si"),
                childAtPosition(
                        allOf(withId(R.id.riskLayout), childAtPosition(withId(R.id.mainLayout), 3)),
                        1), isDisplayed()));
        textView6.check(matches(withText("Si")));

        ViewInteraction linearLayout4 = onView(allOf(withId(R.id.riskLayout), childAtPosition(
                allOf(withId(R.id.mainLayout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 3), isDisplayed()));
        linearLayout4.check(matches(isDisplayed()));

        ViewInteraction textView7 = onView(allOf(withId(R.id.descriptionText), withText(
                "El semafor no te cap tapa per amagar els cables, que estan completament al descobert."),
                childAtPosition(allOf(withId(R.id.mainLayout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 4), isDisplayed()));
        textView7.check(matches(withText(
                "El semafor no te cap tapa per amagar els cables, que estan completament al descobert.")));

        ViewInteraction textView8 = onView(allOf(withId(R.id.streetText),
                withText("Carrer de la Diputacio, 13, Barcelona"), childAtPosition(
                        allOf(withId(R.id.dataLayout), childAtPosition(withId(R.id.mainLayout), 5)),
                        0), isDisplayed()));
        textView8.check(matches(withText("Carrer de la Diputacio, 13, Barcelona")));

        ViewInteraction textView9 = onView(allOf(withId(R.id.sinceText), withText("fa 3 dies"),
                childAtPosition(allOf(withId(R.id.timeDistanceLayout),
                        childAtPosition(withId(R.id.dataLayout), 1)), 0), isDisplayed()));
        textView9.check(matches(withText("fa 3 dies")));

        ViewInteraction textView10 = onView(allOf(withId(R.id.distanceText), withText("1 km"),
                childAtPosition(allOf(withId(R.id.timeDistanceLayout),
                        childAtPosition(withId(R.id.dataLayout), 1)), 1), isDisplayed()));
        textView10.check(matches(withText("1 km")));

        ViewInteraction linearLayout5 = onView(allOf(withId(R.id.timeDistanceLayout),
                childAtPosition(
                        allOf(withId(R.id.dataLayout), childAtPosition(withId(R.id.mainLayout), 5)),
                        1), isDisplayed()));
        linearLayout5.check(matches(isDisplayed()));

        ViewInteraction linearLayout6 = onView(allOf(withId(R.id.dataLayout), childAtPosition(
                allOf(withId(R.id.mainLayout),
                        childAtPosition(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                0)), 5), isDisplayed()));
        linearLayout6.check(matches(isDisplayed()));

        ViewInteraction linearLayout7 = onView(allOf(withId(R.id.mainLayout),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 0),
                isDisplayed()));
        linearLayout7.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher,
            final int position) {

        return new TypeSafeMatcher<View>() {
            @Override public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(
                        ((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
