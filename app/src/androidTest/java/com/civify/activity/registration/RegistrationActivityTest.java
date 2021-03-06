package com.civify.activity.registration;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;

import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.civify.R;
import com.civify.adapter.UserAdapter;
import com.civify.service.UserService;
import com.civify.utils.AdapterFactory;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTest {
    private MockWebServer mMockServer;

    @Rule
    public ActivityTestRule<RegistrationActivity> mActivityTestRule =
            new ActivityTestRule<>(RegistrationActivity.class);

    @Before
    public void setUp() throws Exception {
        mMockServer = new MockWebServer();
        mMockServer.start();
        Retrofit retrofit = (new Retrofit.Builder().baseUrl(mMockServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create())).build();
        AdapterFactory.getInstance().getUserAdapter()
                .setService(retrofit.create(UserService.class));
    }

    @Test
    public void registrationActivityTest() throws InterruptedException {
        ViewInteraction textView = onView(allOf(withId(R.id.title0), withText(R.string.whats_your_name),
                childAtPosition(withParent(withId(R.id.viewpager)), 0), isDisplayed()));
        textView.check(matches(withText(R.string.whats_your_name)));

        ViewInteraction appCompatButton =
                onView(allOf(withId(R.id.button0), withText(R.string.continue_button), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView2 = onView(allOf(withId(R.id.name_validation_text),
                withText(R.string.name_not_validated), childAtPosition(
                        allOf(withId(R.id.name_validation), childAtPosition(
                                IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1)), 1),
                isDisplayed()));
        textView2.check(matches(withText(R.string.name_not_validated)));

        ViewInteraction textView3 = onView(allOf(withId(R.id.surname_validation_text),
                withText(R.string.surname_not_validated), childAtPosition(
                        allOf(withId(R.id.surname_validation), childAtPosition(
                                IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1)), 1),
                isDisplayed()));
        textView3.check(matches(withText(R.string.surname_not_validated)));

        ViewInteraction appCompatEditText3 = onView(allOf(withId(R.id.name_input), isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(allOf(withId(R.id.name_input), isDisplayed()));
        appCompatEditText4.perform(replaceText("TestName"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 =
                onView(allOf(withId(R.id.name_input), withText("TestName"), isDisplayed()));
        appCompatEditText5.perform(pressImeActionButton());

        ViewInteraction appCompatEditText6 =
                onView(allOf(withId(R.id.surname_input), isDisplayed()));
        appCompatEditText6.perform(replaceText("TestSurname"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 =
                onView(allOf(withId(R.id.surname_input), withText("TestSurname"), isDisplayed()));
        appCompatEditText7.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 =
                onView(allOf(withId(R.id.button0), withText(R.string.continue_button), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView4 = onView(allOf(withId(R.id.title1), withText(R.string.choose_username),
                childAtPosition(withParent(withId(R.id.viewpager)), 0), isDisplayed()));
        textView4.check(matches(withText(R.string.choose_username)));

        ViewInteraction appCompatButton01 =
                onView(allOf(withId(R.id.button1), withText(R.string.continue_button), isDisplayed()));
        appCompatButton01.perform(click());

        pressBack();
        textView.check(matches(withText(R.string.whats_your_name)));
        appCompatButton2.perform(click());


        ViewInteraction appCompatEditText8 =
                onView(allOf(withId(R.id.username_input), isDisplayed()));
        appCompatEditText8.perform(click());

        ViewInteraction appCompatEditText9 =
                onView(allOf(withId(R.id.username_input), isDisplayed()));
        appCompatEditText9.perform(replaceText("test-username"), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 =
                onView(allOf(withId(R.id.username_input), withText("test-username"),
                        isDisplayed()));
        appCompatEditText10.perform(pressImeActionButton());

        ViewInteraction appCompatButton3 =
                onView(allOf(withId(R.id.button1), withText(R.string.continue_button), isDisplayed()));
        appCompatButton3.perform(click());

        sleep(200);

        ViewInteraction textView5 = onView(allOf(withId(R.id.username_validation_text),
                childAtPosition(
                        childAtPosition(IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1),
                        1), isDisplayed()));
        textView5.check(matches(withText(R.string.invalid_username)));

        ViewInteraction appCompatEditText11 =
                onView(allOf(withId(R.id.username_input), withText("test-username"),
                        isDisplayed()));
        appCompatEditText11.perform(click());

        // MOCK SERVER RESPONSE (username)
        mockRequest(5, HttpURLConnection.HTTP_NOT_FOUND, UserAdapter.USER_DOESNT_EXIST);

        ViewInteraction appCompatEditText12 =
                onView(allOf(withId(R.id.username_input),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("testUsername"), closeSoftKeyboard());

        sleep(200);

        ViewInteraction textView6 = onView(allOf(withId(R.id.username_validation_text),
                childAtPosition(
                        childAtPosition(IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1),
                        1), isDisplayed()));
        textView6.check(matches(withText(R.string.valid_unused_username)));

        ViewInteraction appCompatEditText13 =
                onView(allOf(withId(R.id.username_input), withText("testUsername"), isDisplayed()));
        appCompatEditText13.perform(pressImeActionButton());

        // MOCK SERVER RESPONSE
        mockRequest(1, HttpURLConnection.HTTP_NOT_FOUND, UserAdapter.USER_DOESNT_EXIST);

        ViewInteraction appCompatButton4 =
                onView(allOf(withId(R.id.button1), withText(R.string.continue_button), isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction textView7 =
                onView(allOf(withId(R.id.time), withText(R.string.choose_email),
                        childAtPosition(withParent(withId(R.id.viewpager)), 0), isDisplayed()));
        textView7.check(matches(withText(R.string.choose_email)));

        ViewInteraction appCompatButton02 =
                onView(allOf(withId(R.id.button2), withText(R.string.continue_button),
                        isDisplayed()));
        appCompatButton02.perform(click());

        ViewInteraction appCompatEditText14 =
                onView(allOf(withId(R.id.email_input), isDisplayed()));
        appCompatEditText14.perform(click());

        ViewInteraction appCompatEditText15 =
                onView(allOf(withId(R.id.email_input), isDisplayed()));
        appCompatEditText15.perform(replaceText("testemail.com"), closeSoftKeyboard());

        sleep(200);

        ViewInteraction textView8 =
                onView(allOf(withId(R.id.email_validation_text),
                        childAtPosition(childAtPosition(
                                IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1), 1),
                        isDisplayed()));
        textView8.check(matches(withText(R.string.invalid_email)));

        ViewInteraction appCompatButton5 =
                onView(allOf(withId(R.id.button2), withText(R.string.continue_button), isDisplayed()));
        appCompatButton5.perform(click());

        // MOCK SERVER RESPONSE (email)
        mockRequest(3, HttpURLConnection.HTTP_NOT_FOUND, UserAdapter.USER_DOESNT_EXIST);

        ViewInteraction appCompatEditText16 =
                onView(allOf(withId(R.id.email_input), isDisplayed()));
        appCompatEditText16.perform(replaceText("test@email.com"), closeSoftKeyboard());

        sleep(200);

        ViewInteraction textView9 = onView(allOf(withId(R.id.email_validation_text), childAtPosition(
                        childAtPosition(IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1),
                        1), isDisplayed()));
        textView9.check(matches(withText(R.string.valid_unused_email)));

        ViewInteraction appCompatEditText17 =
                onView(allOf(withId(R.id.email_input), withText("test@email.com"), isDisplayed()));
        appCompatEditText17.perform(pressImeActionButton());

        // MOCK SERVER RESPONSE
        mockRequest(1, HttpURLConnection.HTTP_NOT_FOUND, UserAdapter.USER_DOESNT_EXIST);

        ViewInteraction appCompatButton6 =
                onView(allOf(withId(R.id.button2), withText(R.string.continue_button), isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction textView10 =
                onView(allOf(withId(R.id.title3), withText(R.string.choose_password),
                        childAtPosition(withParent(withId(R.id.viewpager)), 0), isDisplayed()));
        textView10.check(matches(withText(R.string.choose_password)));

        ViewInteraction appCompatButton7 =
                onView(allOf(withId(R.id.button3), isDisplayed()));
        appCompatButton7.perform(click());

        sleep(200);

        ViewInteraction textView11 = onView(allOf(withId(R.id.password_validation_text),
                childAtPosition(
                        childAtPosition(IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1),
                        1), isDisplayed()));
        textView11.check(matches(withText(
                R.string.invalid_password)));

        sleep(200);

        ViewInteraction textView12 = onView(allOf(withId(R.id.password2_validation_text),
                childAtPosition(
                        childAtPosition(IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1),
                        1), isDisplayed()));
        textView12.check(matches(withText(R.string.matching_passwords)));

        ViewInteraction appCompatEditText19 =
                onView(allOf(withId(R.id.password_input), isDisplayed()));
        appCompatEditText19.perform(click());

        ViewInteraction appCompatEditText20 =
                onView(allOf(withId(R.id.password_input), isDisplayed()));
        appCompatEditText20.perform(click());

        ViewInteraction appCompatEditText21 =
                onView(allOf(withId(R.id.password_input), isDisplayed()));
        appCompatEditText21.perform(replaceText("Test1234"), closeSoftKeyboard());

        sleep(200);

        ViewInteraction textView13 = onView(allOf(withId(R.id.password_validation_text),
                childAtPosition(
                        childAtPosition(IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1),
                        1), isDisplayed()));
        textView13.check(matches(withText(R.string.valid_password)));

        ViewInteraction appCompatEditText22 =
                onView(allOf(withId(R.id.password2_input), isDisplayed()));
        appCompatEditText22.perform(replaceText("Test"), closeSoftKeyboard());

        sleep(200);

        ViewInteraction textView14 = onView(allOf(withId(R.id.password_validation_text),
                childAtPosition(
                        childAtPosition(IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1),
                        1), isDisplayed()));
        textView14.check(matches(withText(R.string.valid_password)));

        ViewInteraction textView15 = onView(allOf(withId(R.id.password2_validation_text),
                childAtPosition(
                        childAtPosition(IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1),
                        1), isDisplayed()));
        textView15.check(matches(withText(R.string.not_matching_passwords)));

        ViewInteraction appCompatEditText23 =
                onView(allOf(withId(R.id.password2_input),isDisplayed()));
        appCompatEditText23.perform(replaceText("Test1234"), closeSoftKeyboard());
        appCompatEditText23.perform(pressImeActionButton());

        sleep(200);

        ViewInteraction textView16 = onView(allOf(withId(R.id.password2_validation_text),
                childAtPosition(
                        childAtPosition(IsInstanceOf.<View>instanceOf(TextInputLayout.class), 1),
                        1), isDisplayed()));
        textView16.check(matches(withText(R.string.matching_passwords)));

        // MOCK SERVER RESPONSE
        mockRequest(1, HttpURLConnection.HTTP_CREATED, UserAdapter.USER_CREATED);

        ViewInteraction appCompatButton8 =
                onView(allOf(withId(R.id.button3), withText(R.string.finish), isDisplayed()));
        appCompatButton8.perform(click());
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

    private void mockRequest(int times, int code, String message) {
        for (int i = 0; i < times; ++i) {
            JsonObject body = new JsonObject();
            body.addProperty("message", message);
            mMockServer.enqueue(new MockResponse()
                    .setResponseCode(code)
                    .setBody(body.toString()));
        }
    }
}
