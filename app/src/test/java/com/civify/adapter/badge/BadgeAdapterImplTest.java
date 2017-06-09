package com.civify.adapter.badge;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import android.content.SharedPreferences;

import com.civify.adapter.UserAdapter;
import com.civify.model.User;
import com.civify.model.badge.Badge;
import com.civify.service.badge.BadgeService;
import com.civify.service.badge.ListBadgesSimpleCallback;
import com.civify.utils.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({BadgeAdapterImpl.class, BadgeService.class})
public class BadgeAdapterImplTest {

    private static final String TITLE = "sample_title";
    private static final String CONTENTTYPE = "picture-content-type";
    private static final String URL = "badge_url";
    private static final int TIME_DATE = 123456789;
    private static final String TYPE = "new_type";
    private static final String TOKEN = "new_token";

    private BadgeAdapter mBadgeAdapter;
    private MockWebServer mMockWebServer;
    private Gson mGson;
    private Badge mBadge;
    private User mUser;
    private ListBadgesSimpleCallback mListBadgesSimpleCallback;
    @Captor private ArgumentCaptor<List<Badge>> mListOfBadgesCallbackCaptor;

    @Before
    public void setUp() throws IOException {
        setUpBadge();
        setUpUser();
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
        mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mMockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(mGson)).build();
        mBadgeAdapter = new BadgeAdapterImpl(retrofit.create(BadgeService.class),
                mock(SharedPreferences.class));
        mListBadgesSimpleCallback = mock(ListBadgesSimpleCallback.class);

    }

    @After
    public void tearDown() throws IOException {
        mMockWebServer.shutdown();
    }

    @Test
    public void getUserBadgesValid() throws InterruptedException {

        List<Badge> fetchedUserBadges = new ArrayList<>();
        fetchedUserBadges.add(mBadge);
        fetchedUserBadges.add(mBadge);
        fetchedUserBadges.add(mBadge);
        String jsonBody = mGson.toJson(fetchedUserBadges);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        mBadgeAdapter.getUserBadges(UserAdapter.getCurrentUser().getUserAuthToken(),
                mListBadgesSimpleCallback);

        RecordedRequest request = mMockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/users/" + UserAdapter.getCurrentUser().getUserAuthToken() +
                "/badges", request.getPath());

        verify(mListBadgesSimpleCallback, timeout(1000))
                .onSuccess(mListOfBadgesCallbackCaptor.capture());
        for (Badge responseBadge: mListOfBadgesCallbackCaptor.getValue()) {
            assertEquals(mBadge.getTitle(), responseBadge.getTitle());
            assertEquals(mBadge.getContentType(), responseBadge.getContentType());
            assertEquals(mBadge.getUrl(), responseBadge.getUrl());
            assertEquals(mBadge.getObtainedDate(), responseBadge.getObtainedDate());
            assertEquals(mBadge.getCorrespondsToType(), responseBadge.getCorrespondsToType());
            assertEquals(mBadge.getCorrespondsToToken(), responseBadge.getCorrespondsToToken());
        }

    }

    @Test
    public void getUserBadgesInvalid() {

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
        mMockWebServer.enqueue(mockResponse);
        mBadgeAdapter.getUserBadges(UserAdapter.getCurrentUser().getUserAuthToken(),
                mListBadgesSimpleCallback);
        verify(mListBadgesSimpleCallback, timeout(1000)).onFailure();

        mockResponse.setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
        mMockWebServer.enqueue(mockResponse);
        mBadgeAdapter.getUserBadges(UserAdapter.getCurrentUser().getUserAuthToken(),
                mListBadgesSimpleCallback);
        verify(mListBadgesSimpleCallback, timeout(1000)).onFailure();
    }

    private void setUpUser() {
        mUser = new User("username", "name", "surname", "email@email.com", "mypass", "mypass");
        mUser.setUserAuthToken("user-auth-token");
        UserAdapter.setCurrentUser(mUser);
    }

    private void setUpBadge() {
        mBadge = new Badge(TITLE, CONTENTTYPE, URL, new Date(TIME_DATE), TYPE, TOKEN);
    }
}
