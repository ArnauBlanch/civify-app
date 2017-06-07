package com.civify.adapter.event;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import android.content.SharedPreferences;

import com.civify.model.Picture;
import com.civify.model.Reward;
import com.civify.model.RewardContainer;
import com.civify.model.event.Event;
import com.civify.service.achievement.AchievementSimpleCallback;
import com.civify.service.award.RewardCallback;
import com.civify.service.event.EventService;
import com.civify.service.event.EventSimpleCallback;
import com.civify.service.event.ListEventsSimpleCallback;
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
@PrepareForTest({ EventAdapterImpl.class, EventService.class})
public class EventAdapterImplTest {

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
    private EventAdapter mEventAdapter;
    private Gson mGson;
    private Event mEvent;

    @Before
    public void setUp() throws ParseException, IOException {
        setUpEvent();
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
        mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mMockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(mGson)).build();
        EventService eventService = retrofit.create(EventService.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        mEventAdapter = new EventAdapterImpl(eventService, sharedPreferences);
    }

    @After
    public void tearDown() throws IOException {
        mEventAdapter = null;
        mMockWebServer = null;
    }

    @Test
    public void testValidGetEvents() throws InterruptedException {
        List<Event> eventList = new ArrayList<>();
        eventList.add(mEvent);
        eventList.add(mEvent);
        String jsonBody = mGson.toJson(eventList);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        ListEventsSimpleCallback mockCallback = mock(ListEventsSimpleCallback.class);

        mEventAdapter.getEvents(mockCallback);

        RecordedRequest request = mMockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/events", request.getPath());

        ArgumentCaptor<List<Event>> argument = ArgumentCaptor.forClass((Class) List.class);
        verify(mockCallback, timeout(1000)).onSuccess(argument.capture());

        for (Event responseEvent : argument.getValue()) {
            assertEquals(mEvent.getTitle(), responseEvent.getTitle());
            assertEquals(mEvent.getDescription(), responseEvent.getDescription());
            assertEquals(mEvent.getNumber(), responseEvent.getNumber());
            assertEquals(mEvent.getKind(), responseEvent.getKind());
            assertEquals(mEvent.getCoins(), responseEvent.getCoins());
            assertEquals(mEvent.getXp(), responseEvent.getXp());
            assertEquals(mEvent.getCreatedAt(), responseEvent.getCreatedAt());
            assertEquals(mEvent.getToken(),
                    responseEvent.getToken());
            assertEquals(mEvent.isEnabled(), responseEvent.isEnabled());
            assertEquals(mEvent.getProgress(), responseEvent.getProgress());
            assertEquals(mEvent.isCompleted(), responseEvent.isCompleted());
            assertEquals(mEvent.isClaimed(), responseEvent.isClaimed());
            assertEquals(mEvent.getBadge().getContentType(),
                    responseEvent.getBadge().getContentType());
            assertEquals(mEvent.getStartDate(), responseEvent.getStartDate());
            assertEquals(mEvent.getEndDate(), responseEvent.getEndDate());
            assertEquals(mEvent.getUpdatedAt(), responseEvent.getUpdatedAt());
            assertEquals(mEvent.getPicture().getContentType(),
                    responseEvent.getPicture().getContentType());
        }
    }

    @Test
    public void testInvalidGetEvents() {
        JsonObject body = new JsonObject();
        body.addProperty("message", "Doesn’t exists record");
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        ListEventsSimpleCallback mockCallback = mock(ListEventsSimpleCallback.class);

        mEventAdapter.getEvents(mockCallback);

        verify(mockCallback, timeout(1000)).onFailure();
    }

    @Test
    public void testValidGetEvent() throws InterruptedException {
        String jsonBody = mGson.toJson(mEvent);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        EventSimpleCallback mockCallback = mock(EventSimpleCallback.class);

        mEventAdapter.getEvent(mEvent.getToken(), mockCallback);

        RecordedRequest request = mMockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/events/" + mEvent.getToken(), request.getPath());

        ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(mockCallback, timeout(1000)).onSucces(argument.capture());

        Event responseEvent = argument.getValue();

        assertEquals(mEvent.getTitle(), responseEvent.getTitle());
        assertEquals(mEvent.getDescription(), responseEvent.getDescription());
        assertEquals(mEvent.getNumber(), responseEvent.getNumber());
        assertEquals(mEvent.getKind(), responseEvent.getKind());
        assertEquals(mEvent.getCoins(), responseEvent.getCoins());
        assertEquals(mEvent.getXp(), responseEvent.getXp());
        assertEquals(mEvent.getCreatedAt(), responseEvent.getCreatedAt());
        assertEquals(mEvent.getToken(),
                responseEvent.getToken());
        assertEquals(mEvent.isEnabled(), responseEvent.isEnabled());
        assertEquals(mEvent.getProgress(), responseEvent.getProgress());
        assertEquals(mEvent.isCompleted(), responseEvent.isCompleted());
        assertEquals(mEvent.isClaimed(), responseEvent.isClaimed());
        assertEquals(mEvent.getBadge().getContentType(),
                responseEvent.getBadge().getContentType());
        assertEquals(mEvent.getStartDate(), responseEvent.getStartDate());
        assertEquals(mEvent.getEndDate(), responseEvent.getEndDate());
        assertEquals(mEvent.getUpdatedAt(), responseEvent.getUpdatedAt());
        assertEquals(mEvent.getPicture().getContentType(),
                responseEvent.getPicture().getContentType());
    }

    @Test
    public void testInvalidGetAchievement() {
        JsonObject body = new JsonObject();
        body.addProperty("message", "Doesn’t exists record");
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(body.toString());
        mMockWebServer.enqueue(mockResponse);
        EventSimpleCallback mockCallback = mock(EventSimpleCallback.class);

        mEventAdapter.getEvent(mEvent.getToken(), mockCallback);

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

        mEventAdapter.claimEvent(mEvent.getToken(), callback);

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

        mEventAdapter.claimEvent(mEvent.getToken(), callback);

        verify(callback, timeout(1000)).onFailure();
    }

    private void setUpEvent() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT,
                Locale.getDefault());
        Picture badge = new Picture();
        Picture picture = new Picture();
        badge.setContentType("testBadge");
        picture.setContentType("testPicture");
        mEvent = new Event(TITLE, DESCRIPTION, NUMBER, KIND, COINS, XP,
                dateFormat.parse(DATE), TOKEN, true, PROGRESS, false, false, badge, dateFormat
                .parse(DATE), dateFormat.parse(DATE), dateFormat.parse(DATE), TOKEN, picture);
    }
}
