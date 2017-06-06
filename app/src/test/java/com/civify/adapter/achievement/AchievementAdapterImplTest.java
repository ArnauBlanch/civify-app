package com.civify.adapter.achievement;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;

import android.content.SharedPreferences;

import com.civify.model.Picture;
import com.civify.model.Reward;
import com.civify.model.RewardContainer;
import com.civify.model.achievement.Achievement;
import com.civify.service.achievement.AchievementService;
import com.civify.service.achievement.AchievementSimpleCallback;
import com.civify.service.achievement.ListAchievementsSimpleCallback;
import com.civify.service.award.RewardCallback;
import com.civify.utils.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ AchievementAdapterImpl.class, AchievementService.class})
public class AchievementAdapterImplTest {

    private static final String TITLE = "sample_title";
    private static final String DESCRIPTION = "description";
    private static final int NUMBER = 10;
    private static final String KIND = "kind";
    private static final int COINS = 10;
    private static final int XP = 100;
    private static final String DATE = "2017-04-22T22:11:41.000Z";
    private static final String TOKEN = "token";
    private static final int PROGRESS = 3;

    private MockWebServer mMockWebServer;
    private AchievementAdapter mAchievementAdapter;
    private Gson mGson;
    private Achievement mAchievement;
    private Picture mBadge;

    @Before
    public void setUp() throws ParseException, IOException {
        setUpAchievement();
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
        mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mMockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(mGson)).build();
        AchievementService achievementService = retrofit.create(AchievementService.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        mAchievementAdapter = new AchievementAdapterImpl(achievementService, sharedPreferences);
    }

    @After
    public void tearDown() throws IOException {
        mAchievementAdapter = null;
        mMockWebServer = null;
    }

    @Test
    public void testValidGetAchievements() throws InterruptedException {
        List<Achievement> achievementList = new ArrayList<>();
        achievementList.add(mAchievement);
        achievementList.add(mAchievement);
        String jsonBody = mGson.toJson(achievementList);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        ListAchievementsSimpleCallback mockCallback = mock(ListAchievementsSimpleCallback.class);

        mAchievementAdapter.getAchievements(mockCallback);

        RecordedRequest request = mMockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/achievements", request.getPath());

        ArgumentCaptor<List<Achievement>> argument = forClass((Class) List.class);
        verify(mockCallback, timeout(1000)).onSuccess(argument.capture());

        for (Achievement responseAchievement : argument.getValue()) {
            assertEquals(mAchievement.getTitle(), responseAchievement.getTitle());
            assertEquals(mAchievement.getDescription(), responseAchievement.getDescription());
            assertEquals(mAchievement.getNumber(), responseAchievement.getNumber());
            assertEquals(mAchievement.getKind(), responseAchievement.getKind());
            assertEquals(mAchievement.getCoins(), responseAchievement.getCoins());
            assertEquals(mAchievement.getXp(), responseAchievement.getXp());
            assertEquals(mAchievement.getCreatedAt(), responseAchievement.getCreatedAt());
            assertEquals(mAchievement.getToken(),
                    responseAchievement.getToken());
            assertEquals(mAchievement.isEnabled(), responseAchievement.isEnabled());
            assertEquals(mAchievement.getProgress(), responseAchievement.getProgress());
            assertEquals(mAchievement.isCompleted(), responseAchievement.isCompleted());
            assertEquals(mAchievement.isClaimed(), responseAchievement.isClaimed());
            assertEquals(mAchievement.getBadge().getContentType(),
                    responseAchievement.getBadge().getContentType());
        }
    }

    @Test
    public void testInvalidGetAchievements() {
        JsonObject body = new JsonObject();
        body.addProperty("message", "Doesn’t exists record");
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        ListAchievementsSimpleCallback mockCallback = mock(ListAchievementsSimpleCallback.class);

        mAchievementAdapter.getAchievements(mockCallback);

        verify(mockCallback, timeout(1000)).onFailure();
    }

    @Test
    public void testValidGetAchievement() throws InterruptedException {
        String jsonBody = mGson.toJson(mAchievement);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        AchievementSimpleCallback mockCallback = mock(AchievementSimpleCallback.class);

        mAchievementAdapter.getAchievement(mAchievement.getToken(), mockCallback);

        RecordedRequest request = mMockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/achievements/" + mAchievement.getToken(), request.getPath());

        ArgumentCaptor<Achievement> argument = forClass(Achievement.class);
        verify(mockCallback, timeout(1000)).onSucces(argument.capture());

        Achievement responseAchievement = argument.getValue();

        assertEquals(mAchievement.getTitle(), responseAchievement.getTitle());
        assertEquals(mAchievement.getDescription(), responseAchievement.getDescription());
        assertEquals(mAchievement.getNumber(), responseAchievement.getNumber());
        assertEquals(mAchievement.getKind(), responseAchievement.getKind());
        assertEquals(mAchievement.getCoins(), responseAchievement.getCoins());
        assertEquals(mAchievement.getXp(), responseAchievement.getXp());
        assertEquals(mAchievement.getCreatedAt(), responseAchievement.getCreatedAt());
        assertEquals(mAchievement.getToken(),
                responseAchievement.getToken());
        assertEquals(mAchievement.isEnabled(), responseAchievement.isEnabled());
        assertEquals(mAchievement.getProgress(), responseAchievement.getProgress());
        assertEquals(mAchievement.isCompleted(), responseAchievement.isCompleted());
        assertEquals(mAchievement.isClaimed(), responseAchievement.isClaimed());
        assertEquals(mAchievement.getBadge().getContentType(),
                responseAchievement.getBadge().getContentType());
    }

    @Test
    public void testInvalidGetAchievement() {
        JsonObject body = new JsonObject();
        body.addProperty("message", "Doesn’t exists record");
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        AchievementSimpleCallback mockCallback = mock(AchievementSimpleCallback.class);

        mAchievementAdapter.getAchievement(mAchievement.getToken(), mockCallback);

        verify(mockCallback, timeout(1000)).onFailure();
    }

    @Test
    public void testClaimAchievement() {
        String body = mGson.toJson(mock(RewardContainer.class));
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(body);
        mMockWebServer.enqueue(mockResponse);
        RewardCallback callback = mock(RewardCallback.class);

        mAchievementAdapter.claimAchievement(mAchievement.getToken(), callback);

        ArgumentCaptor<Reward> argument = forClass(Reward.class);

        verify(callback, timeout(1000)).onSuccess(argument.capture());
    }

    @Test
    public void testClaimAchievementFailure() {
        String body = mGson.toJson(mock(RewardContainer.class));
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody(body);
        mMockWebServer.enqueue(mockResponse);
        RewardCallback callback = mock(RewardCallback.class);

        mAchievementAdapter.claimAchievement(mAchievement.getToken(), callback);

        verify(callback, timeout(1000)).onFailure();
    }

    private void setUpAchievement() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT,
                Locale.getDefault());
        mBadge = new Picture();
        mBadge.setContentType("testContent");
        mAchievement = new Achievement(TITLE, DESCRIPTION, NUMBER, KIND, COINS, XP,
                dateFormat.parse(DATE), TOKEN, true, PROGRESS, false, false, mBadge);
    }
}
