package com.civify.adapter.award;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import android.content.SharedPreferences;

import com.civify.adapter.UserAdapter;
import com.civify.model.User;
import com.civify.model.award.Award;
import com.civify.model.issue.Picture;
import com.civify.service.award.AwardService;
import com.civify.service.award.AwardSimpleCallback;
import com.civify.service.award.ListAwardsSimpleCallback;
import com.civify.utils.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ AwardAdapterImpl.class, AwardService.class})
public class AwardAdapterImplTest {

    private AwardAdapter mAwardAdapter;
    private MockWebServer mMockWebServer;
    private ListAwardsSimpleCallback mListAwardsSimpleCallbackMock;
    private AwardSimpleCallback mAwardSimpleCallbackMock;
    private Gson mGson;
    private Award mAward;
    @Captor private ArgumentCaptor<List<Award>> mListOfAwardsCallbackCaptor;
    @Captor private ArgumentCaptor<Award> mAwardCallbackCaptor;

    @Before
    public void setUp() throws IOException, ParseException {
        setUpAward();
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();
        mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat(ServiceGenerator.RAILS_DATE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mMockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(mGson)).build();
        AwardService awardService = retrofit.create(AwardService.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        mListAwardsSimpleCallbackMock = mock(ListAwardsSimpleCallback.class);
        mAwardSimpleCallbackMock = mock(AwardSimpleCallback.class);
        mAwardAdapter = new AwardAdapterImpl(awardService, sharedPreferences);
    }

    @Test
    public void listOfOfferedAwardsOnValidGetAwardsRequest() throws ParseException,
            InterruptedException {
        List<Award> fetchedAwards = new ArrayList<>();
        fetchedAwards.add(mAward);
        fetchedAwards.add(mAward);
        fetchedAwards.add(mAward);
        String jsonBody = mGson.toJson(fetchedAwards);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        mAwardAdapter.getOfferedAwards(mListAwardsSimpleCallbackMock);
        RecordedRequest request = mMockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/awards", request.getPath());

        verify(mListAwardsSimpleCallbackMock, timeout(1000))
                .onSuccess(mListOfAwardsCallbackCaptor.capture());

        for (Award responseAward : mListOfAwardsCallbackCaptor.getValue()) {
            assertEquals(mAward.getTitle(), responseAward.getTitle());
            assertEquals(mAward.getDescription(), responseAward.getDescription());
            assertEquals(mAward.getAwardAuthToken(), responseAward.getAwardAuthToken());
            assertEquals(mAward.getCommerceOffering(), responseAward.getCommerceOffering());
            assertEquals(mAward.getCreatedAt(), responseAward.getCreatedAt());
            assertEquals(mAward.getUpdatedAt(), responseAward.getUpdatedAt());
            assertEquals(mAward.getPrice(), responseAward.getPrice());
            assertEquals(mAward.getPicture().getContentType(), responseAward.getPicture()
                    .getContentType());
        }
    }

    @Test
    public void onFailureCallbackWhenGetAwardsInvalidRequest() {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
        mMockWebServer.enqueue(mockResponse);
        mAwardAdapter.getOfferedAwards(mListAwardsSimpleCallbackMock);
        verify(mListAwardsSimpleCallbackMock, timeout(1000)).onFailure();

        mockResponse.setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
        mMockWebServer.enqueue(mockResponse);
        mAwardAdapter.getOfferedAwards(mListAwardsSimpleCallbackMock);
        verify(mListAwardsSimpleCallbackMock, timeout(1000)).onFailure();
    }

    @Test
    public void awardOnValidGetAwardRequest() throws InterruptedException {
        String jsonBody = mGson.toJson(mAward);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);
        mAwardAdapter.getOfferedAward(mAward.getAwardAuthToken(), mAwardSimpleCallbackMock);
        RecordedRequest request = mMockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/awards/" + mAward.getAwardAuthToken(), request.getPath());

        verify(mAwardSimpleCallbackMock, timeout(1000)).onSuccess(mAwardCallbackCaptor.capture());
        Award responseAward = mAwardCallbackCaptor.getValue();

        assertEquals(mAward.getTitle(), responseAward.getTitle());
        assertEquals(mAward.getDescription(), responseAward.getDescription());
        assertEquals(mAward.getAwardAuthToken(), responseAward.getAwardAuthToken());
        assertEquals(mAward.getCommerceOffering(), responseAward.getCommerceOffering());
        assertEquals(mAward.getCreatedAt(), responseAward.getCreatedAt());
        assertEquals(mAward.getUpdatedAt(), responseAward.getUpdatedAt());
        assertEquals(mAward.getPrice(), responseAward.getPrice());
        assertEquals(mAward.getPicture().getContentType(), responseAward.getPicture()
                .getContentType());
    }

    @Test
    public void onFailureCallbackWhenGetAwardInvalidRequest() {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
        mMockWebServer.enqueue(mockResponse);
        mAwardAdapter.getOfferedAward(mAward.getAwardAuthToken(), mAwardSimpleCallbackMock);
        verify(mAwardSimpleCallbackMock, timeout(1000)).onFailure();

        mockResponse.setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
        mMockWebServer.enqueue(mockResponse);
        mAwardAdapter.getOfferedAward(mAward.getAwardAuthToken(), mAwardSimpleCallbackMock);
        verify(mAwardSimpleCallbackMock, timeout(1000)).onFailure();
    }

    @Test
    public void getExchangedAwardsValid() throws InterruptedException {
        User user = new User("username", "name", "surname", "email@email.com", "mypass", "mypass");
        user.setUserAuthToken("user-auth-token");

        List<Award> fetchedAwards = new ArrayList<>();
        fetchedAwards.add(mAward);
        fetchedAwards.add(mAward);
        fetchedAwards.add(mAward);
        String jsonBody = mGson.toJson(fetchedAwards);
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(jsonBody);
        mMockWebServer.enqueue(mockResponse);

        UserAdapter.setCurrentUser(user);

        mAwardAdapter.getExchangedAwards(
                mListAwardsSimpleCallbackMock);
        RecordedRequest request = mMockWebServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("/users/" + UserAdapter.getCurrentUser().getUserAuthToken() +
                "/exchanged_awards", request.getPath());

        verify(mListAwardsSimpleCallbackMock, timeout(1000))
                .onSuccess(mListOfAwardsCallbackCaptor.capture());

        for (Award responseAward : mListOfAwardsCallbackCaptor.getValue()) {
            assertEquals(mAward.getTitle(), responseAward.getTitle());
            assertEquals(mAward.getDescription(), responseAward.getDescription());
            assertEquals(mAward.getAwardAuthToken(), responseAward.getAwardAuthToken());
            assertEquals(mAward.getCommerceOffering(), responseAward.getCommerceOffering());
            assertEquals(mAward.getCreatedAt(), responseAward.getCreatedAt());
            assertEquals(mAward.getUpdatedAt(), responseAward.getUpdatedAt());
            assertEquals(mAward.getPrice(), responseAward.getPrice());
            assertEquals(mAward.getPicture().getContentType(), responseAward.getPicture()
                    .getContentType());
        }
    }

    @Test
    public void getExchangedAwardsInvalid() {
        User user = new User("username", "name", "surname", "email@email.com", "mypass", "mypass");
        user.setUserAuthToken("user-auth-token");

        UserAdapter.setCurrentUser(user);

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
        mMockWebServer.enqueue(mockResponse);
        mAwardAdapter.getExchangedAwards(
                mListAwardsSimpleCallbackMock);
        verify(mListAwardsSimpleCallbackMock, timeout(1000)).onFailure();

        mockResponse.setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
        mMockWebServer.enqueue(mockResponse);
        mAwardAdapter.getExchangedAwards(
                mListAwardsSimpleCallbackMock);
        verify(mListAwardsSimpleCallbackMock, timeout(1000)).onFailure();
    }

    private void setUpAward() throws ParseException {
        String dateString = "2017-04-22T22:11:41.000Z";
        DateFormat dateFormat = new SimpleDateFormat(ServiceGenerator.RAILS_DATE_FORMAT,
                Locale.getDefault());
        Date date = dateFormat.parse(dateString);
        Picture picture =
                new Picture("picture-file-name", "picture-content-type", "picture-content");
        mAward = new Award("sample_award", "description", 50, date, date, "award_auth_token",
                "commerce_offering_auth_token", picture);
    }

    @After
    public void tearDown() throws IOException {
        mAwardAdapter = null;
        mMockWebServer.shutdown();
    }

}
