package com.civify.adapter;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import android.content.SharedPreferences;

import com.civify.model.AchievementsEventsContainer;
import com.civify.model.User;
import com.civify.service.AchievementsEventsCallback;
import com.civify.service.AchievementsEventsService;
import com.civify.utils.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.net.HttpURLConnection;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AchievementsEventsAdapterTest {
    private AchievementsEventsAdapter mAdapter;
    private MockWebServer mServer;
    private Gson mGson;
    private AchievementsEventsContainer mContainer;
    private User mUser;

    @Before
    public void setUp() throws IOException {
        initServer();
        initAdapter();
        initAchievementsEventsContainer();
        initUser();
    }

    @After
    public void tearDown() throws IOException {
        mAdapter = null;
        mServer.shutdown();
    }

    @Test
    public void testGetNewAchievementsEvents() throws InterruptedException {
        String body = mGson.toJson(mContainer);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(body);
        mServer.enqueue(mockResponse);
        AchievementsEventsCallback callback = mock(AchievementsEventsCallback.class);

        UserAdapter.setCurrentUser(mUser);

        mAdapter.getNewAchievementsEvents(callback);

        ArgumentCaptor<AchievementsEventsContainer> argument =
                forClass(AchievementsEventsContainer.class);
        verify(callback, timeout(1000)).onSuccess(argument.capture());
    }

    @Test
    public void testGetNewAchievementsEventsFailure() throws InterruptedException {
        String body = mGson.toJson(mContainer);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .setBody(body);
        mServer.enqueue(mockResponse);
        AchievementsEventsCallback callback = mock(AchievementsEventsCallback.class);

        UserAdapter.setCurrentUser(mUser);

        mAdapter.getNewAchievementsEvents(callback);

        verify(callback, timeout(1000)).onFailure();
    }

    private void initServer() throws IOException {
        mServer = new MockWebServer();
        mServer.start();
    }

    private void initAdapter() {
        mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(mGson)).build();
        AchievementsEventsService achievementsEventsService = retrofit.create
                (AchievementsEventsService.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        mAdapter = new AchievementsEventsAdapter(sharedPreferences);
        mAdapter = new AchievementsEventsAdapter(achievementsEventsService, sharedPreferences);
    }

    private void initAchievementsEventsContainer() {
        mContainer = mock(AchievementsEventsContainer.class);
    }

    private void initUser() {
        mUser = new User("username", "name", "surname", "email@email.com", "mypass", "mypass");
        mUser.setUserAuthToken("user-auth-token");
    }
}
